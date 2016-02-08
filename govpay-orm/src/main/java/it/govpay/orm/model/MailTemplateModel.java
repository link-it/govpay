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

import it.govpay.orm.MailTemplate;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model MailTemplate 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MailTemplateModel extends AbstractModel<MailTemplate> {

	public MailTemplateModel(){
	
		super();
	
		this.MITTENTE = new Field("mittente",java.lang.String.class,"MailTemplate",MailTemplate.class);
		this.TEMPLATE_OGGETTO = new Field("templateOggetto",java.lang.String.class,"MailTemplate",MailTemplate.class);
		this.TEMPLATE_MESSAGGIO = new Field("templateMessaggio",java.lang.String.class,"MailTemplate",MailTemplate.class);
		this.ALLEGATI = new Field("allegati",java.lang.String.class,"MailTemplate",MailTemplate.class);
	
	}
	
	public MailTemplateModel(IField father){
	
		super(father);
	
		this.MITTENTE = new ComplexField(father,"mittente",java.lang.String.class,"MailTemplate",MailTemplate.class);
		this.TEMPLATE_OGGETTO = new ComplexField(father,"templateOggetto",java.lang.String.class,"MailTemplate",MailTemplate.class);
		this.TEMPLATE_MESSAGGIO = new ComplexField(father,"templateMessaggio",java.lang.String.class,"MailTemplate",MailTemplate.class);
		this.ALLEGATI = new ComplexField(father,"allegati",java.lang.String.class,"MailTemplate",MailTemplate.class);
	
	}
	
	

	public IField MITTENTE = null;
	 
	public IField TEMPLATE_OGGETTO = null;
	 
	public IField TEMPLATE_MESSAGGIO = null;
	 
	public IField ALLEGATI = null;
	 

	@Override
	public Class<MailTemplate> getModeledClass(){
		return MailTemplate.class;
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