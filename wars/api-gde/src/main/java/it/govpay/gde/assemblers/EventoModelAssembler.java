package it.govpay.gde.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import it.govpay.gde.controller.GdeController;
import it.govpay.gde.entity.EventoEntity;
import it.govpay.gde.model.EventoModel;
import it.govpay.gde.model.raw.RawObject;

@Component
public class EventoModelAssembler extends RepresentationModelAssemblerSupport<EventoEntity, EventoModel>  {

	@Autowired
	private EventoIndexModelAssembler eventoIndexModelAssembler;
	
	public EventoModelAssembler() {
		super(GdeController.class, EventoModel.class);
	}

	@Override
	public EventoModel toModel(EventoEntity entity) {
		
		EventoModel model = instantiateModel(entity);
		
		this.eventoIndexModelAssembler.toModel(entity, model);
		
		if(entity.getParametriRichiesta() != null) {
			model.setParametriRichiesta(new RawObject(new String(entity.getParametriRichiesta())));
		}
		if(entity.getParametriRisposta() != null) {
			model.setParametriRisposta(new RawObject(new String(entity.getParametriRisposta())));
		}
		
		return model;
	}
	
}