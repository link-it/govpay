package it.govpay.pagopa.v2.authentication.model;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data	
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "problem")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemModel  {

	private URI type;
	private String title;
	private Integer status;
	private String detail;
	private URI instance;
	
}
