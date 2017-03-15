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
		sondaUpdatePsp.setName("update-psp");
		check.add(sondaUpdatePsp);
		CheckSonda sondaUpdateRnd = new CheckSonda();
		sondaUpdateRnd.setName("update-rnd");
		check.add(sondaUpdateRnd);
		CheckSonda sondaUpdatePnd = new CheckSonda();
		sondaUpdatePnd.setName("update-pnd");
		check.add(sondaUpdatePnd);
		CheckSonda sondaUpdateNtfy = new CheckSonda();
		sondaUpdateNtfy.setName("update-ntfy");
		check.add(sondaUpdateNtfy);
		CheckSonda sondaUpdateConto = new CheckSonda();
		sondaUpdateConto.setName("update-conto");
		check.add(sondaUpdateConto);
		CheckSonda sondaCheckNtfy = new CheckSonda();
		sondaCheckNtfy.setName("check-ntfy");
		sondaCheckNtfy.setCoda(true);
		check.add(sondaCheckNtfy);
		return check;
	}
}
