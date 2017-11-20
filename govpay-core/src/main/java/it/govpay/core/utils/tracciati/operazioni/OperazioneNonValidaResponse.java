package it.govpay.core.utils.tracciati.operazioni;

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.Operazione.StatoOperazioneType;

public class OperazioneNonValidaResponse extends AbstractOperazioneResponse {

	@Override
	protected List<String> listDati() {
		List<String> lst = new ArrayList<String>();
		lst.add(StatoOperazioneType.NON_VALIDO.toString());
		lst.add(this.getDescrizioneEsito());
		return lst;
	}

}
