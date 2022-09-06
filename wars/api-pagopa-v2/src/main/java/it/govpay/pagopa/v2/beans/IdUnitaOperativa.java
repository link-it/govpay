package it.govpay.pagopa.v2.beans;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter	
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IdUnitaOperativa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idUnita; 
	private Long idDominio;
	private Long id;
}