package it.govpay.gde.model;

import javax.validation.constraints.Positive;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data	
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "nuovoEvento")
@Relation(collectionRelation = "eventi")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NuovoEventoModel extends EventoModel  {

	@Positive(message = "Il campo idTracciato deve contenere un valore >= 0.")
	private Long idTracciato;
	
	@Positive(message = "Il campo idFr deve contenere un valore >= 0.")
	private Long idFr;
	
	@Positive(message = "Il campo idRiconciliazione deve contenere un valore >= 0.")
	private Long idRiconciliazione;
}

