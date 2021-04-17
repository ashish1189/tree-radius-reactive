package com.holidu.interview.assignment.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.holidu.interview.assignment.data.AggregatedData;
import com.holidu.interview.assignment.data.Circle;
import com.holidu.interview.assignment.data.RecordCount;
import com.holidu.interview.assignment.data.TreeData;
import com.holidu.interview.assignment.exception.TreesException;
import com.holidu.interview.assignment.utils.Utilities;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.holidu.interview.assignment.utils.Constants.*;

/**
 * Service bean holding core processing logic.
 * 
 * @author Ashish Deshpande
 *
 */
@Slf4j
@Service
public class TreesServiceImpl implements TreesService {
	
	@Autowired
	@Qualifier("allTrees")
	private WebClient webClient;
	
	@Autowired
	private Utilities utilities;

	private Integer limit = 10000;
	private LongAdder failureStat = new LongAdder();

	/**
	 * Service method which gets the reactive Mono containing all trees along with its 
	 * co-ordinates and counts number of each tree within the given radius.
	 * 
	 *  @param x
	 *  @param y
	 *  @param radius
	 *  @return
	 */
	@Override
	public Mono<Map<String, Integer>> getTrees(double x, double y, double radius) {
		int pages = 0;
		//Convert Radius in meters to feet
		double radiusInFeet = utilities.metersToFeet(radius);
		
		//Where clause to limit the search area along XY plane
		String whereClause = utilities.getWhereClause(new Circle(x, y, radiusInFeet));

		//Get total number of pages for the trees data service
		pages = (getTotalPages(whereClause) / limit) + 1;

		Flux<TreeData> treeFlux = getTreesData(pages, whereClause);
		
		return treeFlux
				.filter(treeData -> 
					Math.sqrt (
							Math.pow((Double.valueOf(treeData.getXSp()) - x), 2) + 
							Math.pow((Double.valueOf(treeData.getYSp()) - y), 2)
							) <= radiusInFeet
					)
				.groupBy(TreeData::getSpcCommon)
				.flatMap(key -> key.collectList()
						.map(list -> new AggregatedData(key.key(), list.size()))
					)
				.doOnError(e -> {
					failureStat.increment();
					log.error("Failed to process the element due to internal Error - "+e);
				})
				.onErrorMap(ex -> new TreesException(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERR_MSG, ex))
				.log()
				.collectMap(AggregatedData::getSpcCommon, AggregatedData::getCount);

	}

	/**
	 * Method invokes a reactive webClient for the external service to get the records.
	 * This will return a Mono which can be further processed for desired results.
	 * 
	 * @param maxPages
	 * @param whereClause
	 * @return
	 */
	private Flux<TreeData> getTreesData(int maxPages, String whereClause) {

		return Flux.range(0, maxPages)
				.map(offset -> offset * limit)
				.flatMap(
						offset -> webClient
						.get()
						.uri(uriBuilder -> uriBuilder
								.queryParam("$limit", limit)
								.queryParam("$offset", offset)
								.queryParam("$select", "spc_common, x_sp, y_sp")
								.queryParam("$where", whereClause)
								.build())
						.retrieve()
						.onStatus(httpStatus -> HttpStatus.BAD_REQUEST.equals(httpStatus),
								  clientResponse -> clientResponse
								  					.bodyToMono(TreesException.class)
								  					.flatMap(errorResponse -> Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, TOO_MANY_REQ_MSG))))
						.onStatus(HttpStatus::is4xxClientError,
								  clientResponse -> clientResponse
								  					.bodyToMono(TreesException.class)
								  					.flatMap(errorResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issues observed with requests."))))
						.onStatus(HttpStatus::is5xxServerError, 
								  clientResponse -> clientResponse
								  					.bodyToMono(TreesException.class)
								  					.flatMap(errorResponse -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERR_MSG))))
						.bodyToFlux(TreeData.class)
						.doOnError(e -> {
							failureStat.increment();
							log.error("Failed to process the element during processin at "+offset+" Error - "+e);
						})
						.onErrorMap(ex -> new TreesException(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERR_MSG, ex))
						.timeout(Duration.ofSeconds(50L))
					)
				.log();
	}
	
	/**
	 * This method invokes count query against the external service to get the count 
	 * of records that will be returned with provided predicate. This is required as
	 * the external service returns paginated response and we have to move record
	 * offset parameter accordingly.
	 * 
	 * @param whereClause
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Integer getTotalPages(String whereClause) {
		
		List<RecordCount> list = webClient
				.get()
				.uri(uriBuilder -> uriBuilder
						.queryParam("$select", "count(*)")
						.queryParam("$where", whereClause)
						.build())
				.retrieve()
				.onStatus(httpStatus -> HttpStatus.TOO_MANY_REQUESTS.equals(httpStatus),
						  clientResponse -> clientResponse
						  					.bodyToMono(TreesException.class)
						  					.flatMap(errorResponse -> Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, TOO_MANY_REQ_MSG))))
				.onStatus(HttpStatus::is4xxClientError,
						  clientResponse -> clientResponse
						  					.bodyToMono(TreesException.class)
						  					.flatMap(errorResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issues observed with requests."))))
				.onStatus(HttpStatus::is5xxServerError, 
						  clientResponse -> clientResponse
						  					.bodyToMono(TreesException.class)
						  					.flatMap(errorResponse -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERR_MSG))))
				.bodyToFlux(RecordCount.class)
				.doOnError(e -> {
					failureStat.increment();
					log.error("Failed to process the element due to interal Error - "+e);
				})
				.timeout(Duration.ofSeconds(50L))
				.doOnError(ex -> log.error("Failed to process the element due to interal Error - "+ex))
				.onErrorMap(ex -> new TreesException(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERR_MSG, ex))
				.collectList()
				.toProcessor()
				.block();
		
		if (list == null || list.isEmpty())
			return 0;

		return list.get(0).getCount();

	}
}
