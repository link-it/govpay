package it.govpay.gde.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class DatiPagoPAEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codPsp;
	private String tipoVersamento;
	private String modelloPagamento;
	private String fruitore;
	private String erogatore;
	private String codStazione;
	private String codCanale;
	private String codIntermediario;
	private String codIntermediarioPsp;
	private String codDominio;
	private String codFlusso;
	private String trn;
	private String sct;
	private Long idTracciato;
	
	@JsonIgnore
	private LocalDateTime dataFlusso;
}
