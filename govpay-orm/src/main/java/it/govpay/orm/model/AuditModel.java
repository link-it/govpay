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

import it.govpay.orm.Audit;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Audit 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AuditModel extends AbstractModel<Audit> {

	public AuditModel(){
	
		super();
	
		this.DATA = new Field("data",java.util.Date.class,"Audit",Audit.class);
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new Field("idOperatore",it.govpay.orm.IdOperatore.class,"Audit",Audit.class));
		this.ID_OGGETTO = new Field("idOggetto",long.class,"Audit",Audit.class);
		this.TIPO_OGGETTO = new Field("tipoOggetto",java.lang.String.class,"Audit",Audit.class);
		this.OGGETTO = new Field("oggetto",java.lang.String.class,"Audit",Audit.class);
	
	}
	
	public AuditModel(IField father){
	
		super(father);
	
		this.DATA = new ComplexField(father,"data",java.util.Date.class,"Audit",Audit.class);
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new ComplexField(father,"idOperatore",it.govpay.orm.IdOperatore.class,"Audit",Audit.class));
		this.ID_OGGETTO = new ComplexField(father,"idOggetto",long.class,"Audit",Audit.class);
		this.TIPO_OGGETTO = new ComplexField(father,"tipoOggetto",java.lang.String.class,"Audit",Audit.class);
		this.OGGETTO = new ComplexField(father,"oggetto",java.lang.String.class,"Audit",Audit.class);
	
	}
	
	

	public IField DATA = null;
	 
	public it.govpay.orm.model.IdOperatoreModel ID_OPERATORE = null;
	 
	public IField ID_OGGETTO = null;
	 
	public IField TIPO_OGGETTO = null;
	 
	public IField OGGETTO = null;
	 

	@Override
	public Class<Audit> getModeledClass(){
		return Audit.class;
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