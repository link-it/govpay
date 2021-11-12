package it.govpay.core.ec.v2.converter;

import java.io.IOException;
import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.ec.v2.beans.Riscossione;
import it.govpay.ec.v2.beans.StatoRiscossione;
import it.govpay.ec.v2.beans.TipoRiscossione;

public class RiscossioniConverter {

	public static Riscossione toRsModel(it.govpay.bd.viste.model.Pagamento dto) throws IOException, ValidationException {
		return toRsModel(dto.getPagamento(), dto.getVersamento());
	}
	
	public static Riscossione toRsModel(it.govpay.bd.model.Pagamento input) throws IOException, ValidationException {
		return toRsModel(input, null);
	}
	
	public static Riscossione toRsModel(it.govpay.bd.model.Pagamento input, Versamento versamento) throws ValidationException {
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
			LoggerWrapperFactory.getLogger(RiscossioniConverter.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

}
