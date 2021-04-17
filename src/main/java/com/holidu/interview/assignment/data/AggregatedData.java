package com.holidu.interview.assignment.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "spc_common", "count" })
public class AggregatedData {

	@JsonProperty("spc_common")
	private String spcCommon;
	@JsonProperty("count")
	private Integer count;
}

