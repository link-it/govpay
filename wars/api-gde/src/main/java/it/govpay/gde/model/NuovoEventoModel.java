package it.govpay.gde.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.server.core.Relation;

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
@JsonRootName(value = "nuovoEvento")
@Relation(collectionRelation = "eventi")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NuovoEventoModel   {

  private Long id;

  private ComponenteEvento componente;

  private CategoriaEvento categoriaEvento;

  private RuoloEvento ruolo;

  private String tipoEvento;

  private EsitoEvento esito;

  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime dataEvento;

  private Long durataEvento;

  private String sottotipoEvento;

  private String sottotipoEsito;

  private String dettaglioEsito;

  private String idDominio;

  private String iuv;

  private String ccp;

  private String idA2A;

  private String idPendenza;

  private String idPagamento;

  private DatiPagoPAModel datiPagoPA;

  private Integer severita;

  private Object parametriRichiesta;

  private Object parametriRisposta;
}

