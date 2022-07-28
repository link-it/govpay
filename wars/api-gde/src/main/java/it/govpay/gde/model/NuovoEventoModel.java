package it.govpay.gde.model;

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

	private Long idTracciato;
	
	private Long idFr;
	
	private Long idRiconciliazione;
}

