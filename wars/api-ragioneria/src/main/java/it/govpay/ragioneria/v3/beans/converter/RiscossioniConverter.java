package it.govpay.ragioneria.v3.beans.converter;

import java.io.IOException;
import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.bd.model.SingoloVersamento;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.ragioneria.v3.beans.RiscossioneIndex;
import it.govpay.ragioneria.v3.beans.TipoRiscossione;
import it.govpay.rs.BaseRsService;

public class RiscossioniConverter {

	public static RiscossioneIndex toRsModelIndex(it.govpay.bd.model.Pagamento input) throws IOException, ValidationException {
		RiscossioneIndex rsModel = new RiscossioneIndex();
		try {
			SingoloVersamento singoloVersamento = input.getSingoloVersamento();

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
//				Stato stato = input.getStato();
//				if(stato != null) {
//					switch(stato) {
//					case INCASSATO: rsModel.setStato(StatoRiscossione.INCASSATA);
//					break;
//					case PAGATO: rsModel.setStato(StatoRiscossione.RISCOSSA);
//					break;
//					case PAGATO_SENZA_RPT: rsModel.setStato(StatoRiscossione.RISCOSSA);
//					break;
//					default:
//						break;
//					}
//				}


				rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(singoloVersamento, input.getIndiceDati()));
			}

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

}
