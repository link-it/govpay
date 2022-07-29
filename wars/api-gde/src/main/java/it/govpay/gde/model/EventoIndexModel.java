package it.govpay.gde.model;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;
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
@JsonRootName(value = "evento")
@Relation(collectionRelation = "eventi")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoIndexModel extends RepresentationModel<EventoIndexModel>{

	private Long id;

	private ComponenteEvento componente;

	private CategoriaEvento categoriaEvento;

	private RuoloEvento ruolo;

	@Size(max = 255, message = "Il valore presente nel campo tipoEvento non rispetta la lunghezza massima di 255 caratteri.")
	private String tipoEvento;

	private EsitoEvento esito;

	@org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataEvento;

	@PositiveOrZero(message = "Il campo durataEvento deve contenere un valore >= 0.")
	private Long durataEvento;

	@Size(max = 255, message = "Il valore presente nel campo sottotipoEvento non rispetta la lunghezza massima di 255 caratteri.")
	private String sottotipoEvento;

	@Size(max = 255, message = "Il valore presente nel campo sottotipoEsito non rispetta la lunghezza massima di 255 caratteri.")
	private String sottotipoEsito;

	private String dettaglioEsito;

	@Pattern(regexp = "(^([0-9]){11}$)", message = "Il valore presente nel campo idDominio non rispetta il pattern previsto.")
	private String idDominio;

	@Size(max = 35, message = "Il valore presente nel campo iuv non rispetta la lunghezza massima di 35 caratteri.")
	private String iuv;

	@Size(max = 35, message = "Il valore presente nel campo ccp non rispetta la lunghezza massima di 35 caratteri.")
	private String ccp;

	@Pattern(regexp = "(^[a-zA-Z0-9\\-_]{1,35}$)", message = "Il valore presente nel campo idDominio non rispetta il pattern previsto.")
	private String idA2A;

	@Pattern(regexp = "(^[a-zA-Z0-9\\-_]{1,35}$)", message = "Il valore presente nel campo idDominio non rispetta il pattern previsto.")
	private String idPendenza;

	@Size(max = 35, message = "Il valore presente nel campo idPagamento non rispetta la lunghezza massima di 35 caratteri.")
	private String idPagamento;

	private DatiPagoPAModel datiPagoPA;

	@Min(value = 0, message = "Il campo severita deve contenere un valore >= 0.") @Max(value = 5, message = "Il campo severita deve contenere un valore <= 5.")
	private Integer severita;
}
