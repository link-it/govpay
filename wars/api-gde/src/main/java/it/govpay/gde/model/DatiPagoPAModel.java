package it.govpay.gde.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

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
@JsonRootName(value = "datiPagoPA")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatiPagoPAModel extends RepresentationModel<DatiPagoPAModel>  {

  private String idPsp;

  private String idCanale;

  private String idIntermediarioPsp;

  private String tipoVersamento;

  private String modelloPagamento;

  private String idDominio;

  private String idIntermediario;

  private String idStazione;

  private String idRiconciliazione;

  private String sct;

  private String idFlusso;

  private BigDecimal idTracciato;

}

