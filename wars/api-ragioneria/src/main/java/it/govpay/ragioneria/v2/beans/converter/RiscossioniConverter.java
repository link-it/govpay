package it.govpay.ragioneria.v2.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.SingoloVersamento;
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
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Riscossione rsModel = new Riscossione();
		try {
			rsModel.setDominio(DominiConverter.toRsModelIndex(input.getDominio(configWrapper)));
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			switch (input.getTipo()) {
			case ALTRO_INTERMEDIARIO:
				rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
				break;
			case ENTRATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA);
				break;
			case MBT:
				rsModel.setTipo(TipoRiscossione.MBT);
				break;
			}


			// solo per i pagamenti interni
			if(!input.getTipo().equals(TipoPagamento.ALTRO_INTERMEDIARIO)) {
				Stato stato = input.getStato();
				if(stato != null) {
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
				}



				rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(input.getSingoloVersamento(null), input.getIndiceDati()));
				if(input.getRpt(null) != null)
					rsModel.setRt(ConverterUtils.getRtJson(input.getRpt(null)));
			}
			
			if(input.getIncasso(null)!=null)
				rsModel.setRiconciliazione(UriBuilderUtils.getRiconciliazioniByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

	public static RiscossioneIndex toRsModelIndexOld(Pagamento input) throws NotFoundException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneIndex rsModel = new RiscossioneIndex();
		try {
			rsModel.setDominio(DominiConverter.toRsModelIndex(input.getDominio(configWrapper)));
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			switch (input.getTipo()) {
			case ALTRO_INTERMEDIARIO:
				rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
				break;
			case ENTRATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA);
				break;
			case MBT:
				rsModel.setTipo(TipoRiscossione.MBT);
				break;
			}

			// solo per i pagamenti interni
			if(!input.getTipo().equals(TipoPagamento.ALTRO_INTERMEDIARIO)) {
				Stato stato = input.getStato();
				if(stato != null) {
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
				}

				rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(input.getSingoloVersamento(null), input.getIndiceDati()));
			}
			if(input.getIncasso(null)!=null)
				rsModel.setRiconciliazione(UriBuilderUtils.getRiconciliazioniByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));

		} catch(ServiceException e) {}

		return rsModel;
	}

	public static RiscossioneIndex toRsModelIndex(it.govpay.bd.viste.model.Pagamento dto) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneIndex rsModel = new RiscossioneIndex();
		try {
			Pagamento input = dto.getPagamento();
			SingoloVersamento singoloVersamento = dto.getSingoloVersamento();
			Incasso incasso = dto.getIncasso();

			rsModel.setDominio(DominiConverter.toRsModelIndex(input.getDominio(configWrapper)));
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			switch (input.getTipo()) {
			case ALTRO_INTERMEDIARIO:
				rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
				break;
			case ENTRATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA);
				break;
			case MBT:
				rsModel.setTipo(TipoRiscossione.MBT);
				break;
			} 

			// solo per i pagamenti interni
			if(!input.getTipo().equals(TipoPagamento.ALTRO_INTERMEDIARIO)) {
				Stato stato = input.getStato();
				if(stato != null) {
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
				}


				rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(singoloVersamento, input.getIndiceDati()));
			}
			if(incasso !=null)
				rsModel.setRiconciliazione(UriBuilderUtils.getIncassiByIdDominioIdIncasso(incasso.getCodDominio(), incasso.getTrn()));

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

}
