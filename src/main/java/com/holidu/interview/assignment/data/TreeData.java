package com.holidu.interview.assignment.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "spc_common", "x_sp", "y_sp" })
public class TreeData {

	@JsonProperty("spc_common")
	private String spcCommon = "Default"; //Default name value for records for which spc_common is null
	@JsonProperty("x_sp")
	private String xSp;
	@JsonProperty("y_sp")
	private String ySp;
}