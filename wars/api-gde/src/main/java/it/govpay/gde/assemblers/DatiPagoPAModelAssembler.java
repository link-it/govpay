package it.govpay.gde.assemblers;

import java.math.BigDecimal;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import it.govpay.gde.controller.GdeController;
import it.govpay.gde.entity.DatiPagoPAEntity;
import it.govpay.gde.model.DatiPagoPAModel;

@Component
public class DatiPagoPAModelAssembler extends RepresentationModelAssemblerSupport<DatiPagoPAEntity, DatiPagoPAModel>  {

	public DatiPagoPAModelAssembler() {
		super(GdeController.class, DatiPagoPAModel.class);
	}

	@Override
	public DatiPagoPAModel toModel(DatiPagoPAEntity entity) {
		DatiPagoPAModel model = instantiateModel(entity);
		
		model.setIdCanale(entity.getCodCanale());
		model.setIdPsp(entity.getCodPsp());
		model.setIdIntermediarioPsp(entity.getCodIntermediarioPsp());
		model.setIdIntermediario(entity.getCodIntermediario());
		model.setIdStazione(entity.getCodStazione());
		model.setIdDominio(entity.getCodDominio());
		model.setTipoVersamento(entity.getTipoVersamento());
		model.setModelloPagamento(entity.getModelloPagamento());
		
		model.setIdFlusso(entity.getCodFlusso());
		if(entity.getIdTracciato() != null)
			model.setIdTracciato(new BigDecimal(entity.getIdTracciato()));
		model.setIdRiconciliazione(entity.getTrn());
		model.setSct(entity.getSct());
		
		return model;
	}
	
}