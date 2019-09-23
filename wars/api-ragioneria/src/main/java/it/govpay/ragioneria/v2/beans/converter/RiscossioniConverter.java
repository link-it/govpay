package it.govpay.ragioneria.v2.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;

import it.govpay.bd.model.Pagamento;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.ragioneria.v2.beans.Riscossione;
import it.govpay.ragioneria.v2.beans.RiscossioneIndex;
import it.govpay.ragioneria.v2.beans.StatoRiscossione;
import it.govpay.ragioneria.v2.beans.TipoRiscossione;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.ConverterUtils;

public class RiscossioniConverter {
	
	public static Riscossione toRsModel(Pagamento input) throws NotFoundException {
		Riscossione rsModel = new Riscossione();
		try {
			rsModel.setDominio(DominiConverter.toRsModelIndex(input.getDominio(null)));
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			Stato stato = input.getStato();
			switch(stato) {
			case INCASSATO: rsModel.setStato(StatoRiscossione.INCASSATA);
				break;
			case PAGATO: rsModel.setStato(StatoRiscossione.RISCOSSA);
				break;
			case PAGATO_SENZA_RPT: rsModel.setStato(StatoRiscossione.RISCOSSA);
				break; 
			default:
				break;
			}
			
			if(input.getTipo().equals(TipoPagamento.ENTRATA)) {
				rsModel.setTipo(TipoRiscossione.ENTRATA);
			} else {
				rsModel.setTipo(TipoRiscossione.MBT);
			} 
			
			rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(input.getSingoloVersamento(null), input.getIndiceDati()));
			if(input.getRpt(null) != null)
				rsModel.setRt(ConverterUtils.getRtJson(input.getRpt(null)));
			
			if(input.getIncasso(null)!=null)
				rsModel.setRiconciliazione(UriBuilderUtils.getRiconciliazioniByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));
			
//			if(input.getIncasso(null) != null)
//				rsModel.setRiconciliazione(IncassiConverter.toRsIndexModel(input.getIncasso(null)));

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}
	
	public static RiscossioneIndex toRsModelIndex(Pagamento input) throws NotFoundException {
		RiscossioneIndex rsModel = new RiscossioneIndex();
		try {
			rsModel.setDominio(DominiConverter.toRsModelIndex(input.getDominio(null)));
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			Stato stato = input.getStato();
			switch(stato) {
			case INCASSATO: rsModel.setStato(StatoRiscossione.INCASSATA);
				break;
			case PAGATO: rsModel.setStato(StatoRiscossione.RISCOSSA);
				break;
			case PAGATO_SENZA_RPT: rsModel.setStato(StatoRiscossione.RISCOSSA);
				break;
			default:
				break;
			}
			
			if(input.getTipo().equals(TipoPagamento.ENTRATA)) {
				rsModel.setTipo(TipoRiscossione.ENTRATA);
			} else {
				rsModel.setTipo(TipoRiscossione.MBT);
			} 
			
			rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(input.getSingoloVersamento(null), input.getIndiceDati()));
			if(input.getIncasso(null)!=null)
				rsModel.setRiconciliazione(UriBuilderUtils.getRiconciliazioniByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));
			
		} catch(ServiceException e) {}

		return rsModel;
	}
	
}
