/**
 * 
 */
package it.govpay.bd.model;

import java.util.List;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class ListaNote {

	private List<Nota> lista;

	public List<Nota> getLista() {
		return this.lista;
	}

	public void setLista(List<Nota> lista) {
		this.lista = lista;
	}

}
