/**
 * 
 */
package it.govpay.web.rs.sonde;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * 
 */
public class CheckSonda {

	private String name;
	private boolean coda;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isCoda() {
		return coda;
	}
	public void setCoda(boolean coda) {
		this.coda = coda;
	}
	

	public static CheckSonda getCheckSonda(String nome) {
		List<CheckSonda> checkLst = getListaCheckSonda();
		
		for(CheckSonda check: checkLst) {
			if(nome.equals(check.getName()))
				return check;
		}
		return null;
	}
	

	public static List<CheckSonda> getListaCheckSonda() {
		List<CheckSonda> check = new ArrayList<CheckSonda>();
		CheckSonda sondaUpdatePsp = new CheckSonda();
		sondaUpdatePsp.setName(it.govpay.core.business.Operazioni.psp);
		check.add(sondaUpdatePsp);
		CheckSonda sondaUpdateRnd = new CheckSonda();
		sondaUpdateRnd.setName(it.govpay.core.business.Operazioni.rnd);
		check.add(sondaUpdateRnd);
		CheckSonda sondaUpdatePnd = new CheckSonda();
		sondaUpdatePnd.setName(it.govpay.core.business.Operazioni.pnd);
		check.add(sondaUpdatePnd);
		CheckSonda sondaUpdateNtfy = new CheckSonda();
		sondaUpdateNtfy.setName(it.govpay.core.business.Operazioni.ntfy);
		check.add(sondaUpdateNtfy);
		CheckSonda sondaUpdateConto = new CheckSonda();
		sondaUpdateConto.setName(it.govpay.core.business.Operazioni.conto);
		check.add(sondaUpdateConto);
		CheckSonda sondaCheckNtfy = new CheckSonda();
		sondaCheckNtfy.setName(it.govpay.core.business.Operazioni.check_ntfy);
		sondaCheckNtfy.setCoda(true);
		check.add(sondaCheckNtfy);
		CheckSonda sondaCheckTracciati = new CheckSonda();
		sondaCheckTracciati.setName(it.govpay.core.business.Operazioni.check_tracciati);
		sondaCheckTracciati.setCoda(true);
		check.add(sondaCheckTracciati);
		CheckSonda sondaBatchTracciati = new CheckSonda();
		sondaBatchTracciati.setName(it.govpay.core.business.Operazioni.batch_tracciati);
		check.add(sondaBatchTracciati);
		CheckSonda sondaBatchConservazioneReq = new CheckSonda();
		sondaBatchConservazioneReq.setName(it.govpay.core.business.Operazioni.conservazione_req);
		check.add(sondaBatchConservazioneReq);
		CheckSonda sondaBatchConservazioneEsito = new CheckSonda();
		sondaBatchConservazioneEsito.setName(it.govpay.core.business.Operazioni.conservazione_esito);
		check.add(sondaBatchConservazioneEsito);
		CheckSonda sondaBatchStampaAvvisi = new CheckSonda();
		sondaBatchStampaAvvisi.setName(it.govpay.core.business.Operazioni.batch_generazione_avvisi);
		check.add(sondaBatchStampaAvvisi);
		return check;
	}
}
