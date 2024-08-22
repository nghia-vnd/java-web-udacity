package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

	@JsonProperty
	@NotBlank(message = "Username is required")
	private String username;

	@JsonProperty
	@NotBlank(message = "Password is required")
	@Size(min = 7, message = "Password must be at least 7 characters long")
	private String password;

	@JsonProperty
	@NotBlank(message = "Confirm Password is required")
	private String confirmPassword;
}
