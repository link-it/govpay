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

import it.govpay.orm.IdStampa;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdStampa 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdStampaModel extends AbstractModel<IdStampa> {

	public IdStampaModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"id-stampa",IdStampa.class));
		this.ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new Field("idDocumento",it.govpay.orm.IdDocumento.class,"id-stampa",IdStampa.class));
		this.TIPO = new Field("tipo",java.lang.String.class,"id-stampa",IdStampa.class);
	
	}
	
	public IdStampaModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"id-stampa",IdStampa.class));
		this.ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new ComplexField(father,"idDocumento",it.govpay.orm.IdDocumento.class,"id-stampa",IdStampa.class));
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"id-stampa",IdStampa.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdDocumentoModel ID_DOCUMENTO = null;
	 
	public IField TIPO = null;
	 

	@Override
	public Class<IdStampa> getModeledClass(){
		return IdStampa.class;
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