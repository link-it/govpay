package it.govpay.ragioneria.v3.beans.converter;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.ragioneria.v3.beans.Allegato;
import it.govpay.ragioneria.v3.beans.Allegato.TipoEnum;
import it.govpay.ragioneria.v3.beans.Riscossione;
import it.govpay.ragioneria.v3.beans.RiscossioneVocePagata;
import it.govpay.ragioneria.v3.beans.StatoRiscossione;
import it.govpay.ragioneria.v3.beans.TipoRiscossione;
import it.govpay.rs.BaseRsService;

public class RiscossioniConverter {

	public static Riscossione toRsModel(it.govpay.bd.viste.model.Pagamento dto) throws IOException, ValidationException {
		return toRsModel(dto.getPagamento(), dto.getVersamento());
	}

	public static Riscossione toRsModel(it.govpay.bd.model.Pagamento input) throws IOException, ValidationException {
		return toRsModel(input, null);
	}

	public static Riscossione toRsModel(it.govpay.bd.model.Pagamento input, Versamento versamento) throws IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Riscossione rsModel = new Riscossione();
		try {
			SingoloVersamento singoloVersamento = input.getSingoloVersamento();

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
			case ENTRATA_PA_NON_INTERMEDIATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA_PA_NON_INTERMEDIATA);
				break;
			} 

			if(input.getAllegato() != null) {
				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromCodifica(input.getTipoAllegato().toString()));
				rsModel.setAllegato(allegato);
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

				if(versamento == null)
					versamento = singoloVersamento.getVersamento(configWrapper);

				rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(singoloVersamento, input.getIndiceDati(), versamento));
			}

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

	public static RiscossioneVocePagata toRiscossioneVocePagataRsModel(it.govpay.bd.model.Pagamento input) throws IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneVocePagata rsModel = new RiscossioneVocePagata();
		try {
			rsModel.setIdDominio(input.getDominio(configWrapper).getCodDominio());
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
			case ENTRATA_PA_NON_INTERMEDIATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA_PA_NON_INTERMEDIATA);
				break;
			} 
			
			if(input.getAllegato() != null) {
				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromCodifica(input.getTipoAllegato().toString()));
				rsModel.setAllegato(allegato);
			}

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

}
