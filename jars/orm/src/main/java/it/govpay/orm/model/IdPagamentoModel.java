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

import it.govpay.orm.IdPagamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdPagamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdPagamentoModel extends AbstractModel<IdPagamento> {

	public IdPagamentoModel(){
	
		super();
	
		this.ID_PAGAMENTO = new Field("idPagamento",long.class,"id-pagamento",IdPagamento.class);
		this.INDICE_DATI = new Field("indiceDati",int.class,"id-pagamento",IdPagamento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"id-pagamento",IdPagamento.class);
	
	}
	
	public IdPagamentoModel(IField father){
	
		super(father);
	
		this.ID_PAGAMENTO = new ComplexField(father,"idPagamento",long.class,"id-pagamento",IdPagamento.class);
		this.INDICE_DATI = new ComplexField(father,"indiceDati",int.class,"id-pagamento",IdPagamento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"id-pagamento",IdPagamento.class);
	
	}
	
	

	public IField ID_PAGAMENTO = null;
	 
	public IField INDICE_DATI = null;
	 
	public IField IUV = null;
	 

	@Override
	public Class<IdPagamento> getModeledClass(){
		return IdPagamento.class;
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
