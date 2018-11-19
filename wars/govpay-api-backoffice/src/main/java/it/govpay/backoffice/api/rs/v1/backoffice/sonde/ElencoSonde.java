/**
 * 
 */
package it.govpay.backoffice.api.rs.v1.backoffice.sonde;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 07/mar/2017 $
 * 
 */
public class ElencoSonde {

	private List<SommarioSonda> items;

	public List<SommarioSonda> getItems() {
		if(this.items == null)
			this.items = new ArrayList<>();
		return this.items;
	}
}
