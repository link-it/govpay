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

import it.govpay.orm.ACL;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model ACL 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ACLModel extends AbstractModel<ACL> {

	public ACLModel(){
	
		super();
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"ACL",ACL.class));
		this.ID_PORTALE = new it.govpay.orm.model.IdPortaleModel(new Field("idPortale",it.govpay.orm.IdPortale.class,"ACL",ACL.class));
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new Field("idOperatore",it.govpay.orm.IdOperatore.class,"ACL",ACL.class));
		this.COD_TIPO = new Field("codTipo",java.lang.String.class,"ACL",ACL.class);
		this.COD_SERVIZIO = new Field("codServizio",java.lang.String.class,"ACL",ACL.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"ACL",ACL.class));
		this.ID_TIPO_TRIBUTO = new it.govpay.orm.model.IdTipoTributoModel(new Field("idTipoTributo",it.govpay.orm.IdTipoTributo.class,"ACL",ACL.class));
	
	}
	
	public ACLModel(IField father){
	
		super(father);
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"ACL",ACL.class));
		this.ID_PORTALE = new it.govpay.orm.model.IdPortaleModel(new ComplexField(father,"idPortale",it.govpay.orm.IdPortale.class,"ACL",ACL.class));
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new ComplexField(father,"idOperatore",it.govpay.orm.IdOperatore.class,"ACL",ACL.class));
		this.COD_TIPO = new ComplexField(father,"codTipo",java.lang.String.class,"ACL",ACL.class);
		this.COD_SERVIZIO = new ComplexField(father,"codServizio",java.lang.String.class,"ACL",ACL.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"ACL",ACL.class));
		this.ID_TIPO_TRIBUTO = new it.govpay.orm.model.IdTipoTributoModel(new ComplexField(father,"idTipoTributo",it.govpay.orm.IdTipoTributo.class,"ACL",ACL.class));
	
	}
	
	

	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdPortaleModel ID_PORTALE = null;
	 
	public it.govpay.orm.model.IdOperatoreModel ID_OPERATORE = null;
	 
	public IField COD_TIPO = null;
	 
	public IField COD_SERVIZIO = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public it.govpay.orm.model.IdTipoTributoModel ID_TIPO_TRIBUTO = null;
	 

	@Override
	public Class<ACL> getModeledClass(){
		return ACL.class;
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