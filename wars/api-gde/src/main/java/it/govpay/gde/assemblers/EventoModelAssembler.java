package it.govpay.gde.assemblers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import it.govpay.gde.controller.GdeController;
import it.govpay.gde.entity.DatiPagoPAEntity;
import it.govpay.gde.entity.EventoEntity;
import it.govpay.gde.entity.converter.DatiPagoPAConverter;
import it.govpay.gde.model.CategoriaEvento;
import it.govpay.gde.model.ComponenteEvento;
import it.govpay.gde.model.EsitoEvento;
import it.govpay.gde.model.EventoModel;
import it.govpay.gde.model.RuoloEvento;
import it.govpay.gde.model.raw.RawObject;

@Component
public class EventoModelAssembler extends RepresentationModelAssemblerSupport<EventoEntity, EventoModel>  {

	@Autowired
	private DatiPagoPAModelAssembler pagoPAModelAssembler;
	
	public EventoModelAssembler() {
		super(GdeController.class, EventoModel.class);
	}

	@Override
	public EventoModel toModel(EventoEntity entity) {
		EventoModel model = instantiateModel(entity);
		
		model.setId(entity.getId());
		
		if(StringUtils.isNotBlank(entity.getComponente())) {
			model.setComponente(ComponenteEvento.fromValue(entity.getComponente()));
		}

		if(entity.getCategoriaEvento() != null) {
			switch (entity.getCategoriaEvento()) {
			case I:
				model.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
				break;
			case B:
				model.setCategoriaEvento(CategoriaEvento.INTERNO);
				break;
			case U:
				model.setCategoriaEvento(CategoriaEvento.UTENTE);
				break;
			}
		}

		if(entity.getRuoloEvento() != null) {
			switch (entity.getRuoloEvento()) {
			case C:
				model.setRuolo(RuoloEvento.CLIENT);
				break;
			case S:
				model.setRuolo(RuoloEvento.SERVER);
				break;
			}
		}

		model.setTipoEvento(entity.getTipoEvento());

		if(entity.getEsitoEvento() != null) {
			switch (entity.getEsitoEvento()) {
			case FAIL:
				model.setEsito(EsitoEvento.FAIL);
				break;
			case KO:
				model.setEsito(EsitoEvento.KO);
				break;
			case OK:
				model.setEsito(EsitoEvento.OK);
				break;
			}
		}

		model.setDataEvento(entity.getData());
		model.setDurataEvento(entity.getIntervallo() != null ? entity.getIntervallo().longValue() : 0l);
		model.setSottotipoEvento(entity.getSottotipoEvento()); 
		model.setSottotipoEsito(entity.getSottotipoEsito());
		model.setDettaglioEsito(entity.getDettaglioEsito());

		model.setIdDominio(entity.getCodDominio());
		model.setIuv(entity.getIuv());
		model.setCcp(entity.getCcp());
		model.setIdA2A(entity.getCodApplicazione());
		model.setIdPendenza(entity.getCodVersamentoEnte());
		model.setIdPagamento(entity.getIdSessione());
		
		if(entity.getDatiPagoPA() != null) {
			DatiPagoPAConverter converter = new DatiPagoPAConverter();
			DatiPagoPAEntity datiPagoPAEntity = converter.convertToEntityAttribute(entity.getDatiPagoPA());
			model.setDatiPagoPA(this.pagoPAModelAssembler.toModel(datiPagoPAEntity));
		}
		model.setSeverita(entity.getSeverita());
		
		if(entity.getParametriRichiesta() != null) {
			model.setParametriRichiesta(new RawObject(new String(entity.getParametriRichiesta())));
//			
//			ParametriRichiestaConverter converter = new ParametriRichiestaConverter();
//			ParametriRichiestaEntity parametriRichiestaEntity = converter.convertToEntityAttribute(entity.getParametriRichiesta());
//			model.setParametriRichiesta(this.parametriRichiestaModelAssembler.toModel(parametriRichiestaEntity));
		}
		if(entity.getParametriRisposta() != null) {
			model.setParametriRisposta(new RawObject(new String(entity.getParametriRisposta())));
//			
//			ParametriRispostaConverter converter = new ParametriRispostaConverter();
//			ParametriRispostaEntity parametriRispostaEntity = converter.convertToEntityAttribute(entity.getParametriRisposta());
//			model.setParametriRisposta(this.parametriRispostaModelAssembler.toModel(parametriRispostaEntity));
		}
		
		return model;
	}
	
}