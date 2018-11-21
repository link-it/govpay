/**
 * 
 */
package it.govpay.backoffice.v1.sonde;

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
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isCoda() {
		return this.coda;
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
		List<CheckSonda> check = new ArrayList<>();
		CheckSonda sondaUpdateRnd = new CheckSonda();
		sondaUpdateRnd.setName(it.govpay.core.business.Operazioni.RND);
		check.add(sondaUpdateRnd);
		CheckSonda sondaUpdatePnd = new CheckSonda();
		sondaUpdatePnd.setName(it.govpay.core.business.Operazioni.PND);
		check.add(sondaUpdatePnd);
		CheckSonda sondaUpdateNtfy = new CheckSonda();
		sondaUpdateNtfy.setName(it.govpay.core.business.Operazioni.NTFY);
		check.add(sondaUpdateNtfy);
		CheckSonda sondaCheckNtfy = new CheckSonda();
		sondaCheckNtfy.setName(it.govpay.core.business.Operazioni.CHECK_NTFY);
		sondaCheckNtfy.setCoda(true);
		check.add(sondaCheckNtfy);
		CheckSonda sondaBatchStampaAvvisi = new CheckSonda();
		sondaBatchStampaAvvisi.setName(it.govpay.core.business.Operazioni.BATCH_GENERAZIONE_AVVISI);
		check.add(sondaBatchStampaAvvisi);
		return check;
	}
}
