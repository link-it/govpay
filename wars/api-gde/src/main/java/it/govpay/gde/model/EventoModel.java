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
@JsonRootName(value = "evento")
@Relation(collectionRelation = "eventi")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoModel extends EventoIndexModel{

	private Object parametriRichiesta;

	private Object parametriRisposta;

}
