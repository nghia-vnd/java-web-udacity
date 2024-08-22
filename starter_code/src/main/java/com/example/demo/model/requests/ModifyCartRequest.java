package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCartRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	private long itemId;

	@JsonProperty
	private int quantity;
}
