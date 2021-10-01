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

import it.govpay.orm.IbanAccredito;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IbanAccredito 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IbanAccreditoModel extends AbstractModel<IbanAccredito> {

	public IbanAccreditoModel(){
	
		super();
	
		this.COD_IBAN = new Field("codIban",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.BIC_ACCREDITO = new Field("bicAccredito",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.POSTALE = new Field("postale",boolean.class,"IbanAccredito",IbanAccredito.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"IbanAccredito",IbanAccredito.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"IbanAccredito",IbanAccredito.class));
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.INTESTATARIO = new Field("intestatario",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.AUT_STAMPA_POSTE = new Field("autStampaPoste",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
	
	}
	
	public IbanAccreditoModel(IField father){
	
		super(father);
	
		this.COD_IBAN = new ComplexField(father,"codIban",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.BIC_ACCREDITO = new ComplexField(father,"bicAccredito",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.POSTALE = new ComplexField(father,"postale",boolean.class,"IbanAccredito",IbanAccredito.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"IbanAccredito",IbanAccredito.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"IbanAccredito",IbanAccredito.class));
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.INTESTATARIO = new ComplexField(father,"intestatario",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
		this.AUT_STAMPA_POSTE = new ComplexField(father,"autStampaPoste",java.lang.String.class,"IbanAccredito",IbanAccredito.class);
	
	}
	
	

	public IField COD_IBAN = null;
	 
	public IField BIC_ACCREDITO = null;
	 
	public IField POSTALE = null;
	 
	public IField ABILITATO = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField DESCRIZIONE = null;
	 
	public IField INTESTATARIO = null;
	 
	public IField AUT_STAMPA_POSTE = null;
	 

	@Override
	public Class<IbanAccredito> getModeledClass(){
		return IbanAccredito.class;
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
