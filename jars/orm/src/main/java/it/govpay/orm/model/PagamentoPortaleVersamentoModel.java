/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.orm.model;

import it.govpay.orm.PagamentoPortaleVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model PagamentoPortaleVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoPortaleVersamentoModel extends AbstractModel<PagamentoPortaleVersamento> {

	public PagamentoPortaleVersamentoModel(){
	
		super();
	
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new Field("idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"PagamentoPortaleVersamento",PagamentoPortaleVersamento.class));
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"PagamentoPortaleVersamento",PagamentoPortaleVersamento.class));
	
	}
	
	public PagamentoPortaleVersamentoModel(IField father){
	
		super(father);
	
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new ComplexField(father,"idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"PagamentoPortaleVersamento",PagamentoPortaleVersamento.class));
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"PagamentoPortaleVersamento",PagamentoPortaleVersamento.class));
	
	}
	
	

	public it.govpay.orm.model.IdPagamentoPortaleModel ID_PAGAMENTO_PORTALE = null;
	 
	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 

	@Override
	public Class<PagamentoPortaleVersamento> getModeledClass(){
		return PagamentoPortaleVersamento.class;
	}
	
	@Override
	public String toString(){
		if(this.getModeledClass()!=null){
			return this.getModeledClass().getName();
		}else{
			return "N.D.";
		}
	}

}