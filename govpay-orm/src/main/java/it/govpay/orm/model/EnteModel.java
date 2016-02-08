/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import it.govpay.orm.Ente;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Ente 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EnteModel extends AbstractModel<Ente> {

	public EnteModel(){
	
		super();
	
		this.ID_ANAGRAFICA_ENTE = new it.govpay.orm.model.IdAnagraficaModel(new Field("idAnagraficaEnte",it.govpay.orm.IdAnagrafica.class,"Ente",Ente.class));
		this.COD_ENTE = new Field("codEnte",java.lang.String.class,"Ente",Ente.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"Ente",Ente.class));
		this.ABILITATO = new Field("abilitato",boolean.class,"Ente",Ente.class);
		this.ID_TEMPLATE_RPT = new it.govpay.orm.model.IdMailTemplateModel(new Field("idTemplateRPT",it.govpay.orm.IdMailTemplate.class,"Ente",Ente.class));
		this.ID_TEMPLATE_RT = new it.govpay.orm.model.IdMailTemplateModel(new Field("idTemplateRT",it.govpay.orm.IdMailTemplate.class,"Ente",Ente.class));
		this.INVIO_MAIL_RPTABILITATO = new Field("invioMailRPTAbilitato",boolean.class,"Ente",Ente.class);
		this.INVIO_MAIL_RTABILITATO = new Field("invioMailRTAbilitato",boolean.class,"Ente",Ente.class);
	
	}
	
	public EnteModel(IField father){
	
		super(father);
	
		this.ID_ANAGRAFICA_ENTE = new it.govpay.orm.model.IdAnagraficaModel(new ComplexField(father,"idAnagraficaEnte",it.govpay.orm.IdAnagrafica.class,"Ente",Ente.class));
		this.COD_ENTE = new ComplexField(father,"codEnte",java.lang.String.class,"Ente",Ente.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"Ente",Ente.class));
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Ente",Ente.class);
		this.ID_TEMPLATE_RPT = new it.govpay.orm.model.IdMailTemplateModel(new ComplexField(father,"idTemplateRPT",it.govpay.orm.IdMailTemplate.class,"Ente",Ente.class));
		this.ID_TEMPLATE_RT = new it.govpay.orm.model.IdMailTemplateModel(new ComplexField(father,"idTemplateRT",it.govpay.orm.IdMailTemplate.class,"Ente",Ente.class));
		this.INVIO_MAIL_RPTABILITATO = new ComplexField(father,"invioMailRPTAbilitato",boolean.class,"Ente",Ente.class);
		this.INVIO_MAIL_RTABILITATO = new ComplexField(father,"invioMailRTAbilitato",boolean.class,"Ente",Ente.class);
	
	}
	
	

	public it.govpay.orm.model.IdAnagraficaModel ID_ANAGRAFICA_ENTE = null;
	 
	public IField COD_ENTE = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField ABILITATO = null;
	 
	public it.govpay.orm.model.IdMailTemplateModel ID_TEMPLATE_RPT = null;
	 
	public it.govpay.orm.model.IdMailTemplateModel ID_TEMPLATE_RT = null;
	 
	public IField INVIO_MAIL_RPTABILITATO = null;
	 
	public IField INVIO_MAIL_RTABILITATO = null;
	 

	@Override
	public Class<Ente> getModeledClass(){
		return Ente.class;
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