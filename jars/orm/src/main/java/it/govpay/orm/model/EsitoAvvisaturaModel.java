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

import it.govpay.orm.EsitoAvvisatura;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model EsitoAvvisatura 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EsitoAvvisaturaModel extends AbstractModel<EsitoAvvisatura> {

	public EsitoAvvisaturaModel(){
	
		super();
	
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.IDENTIFICATIVO_AVVISATURA = new Field("identificativoAvvisatura",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.TIPO_CANALE = new Field("tipoCanale",java.lang.Integer.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.COD_CANALE = new Field("codCanale",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.DATA = new Field("data",java.util.Date.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.COD_ESITO = new Field("codEsito",java.lang.Integer.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.DESCRIZIONE_ESITO = new Field("descrizioneEsito",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciato",it.govpay.orm.IdTracciato.class,"EsitoAvvisatura",EsitoAvvisatura.class));
	
	}
	
	public EsitoAvvisaturaModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.IDENTIFICATIVO_AVVISATURA = new ComplexField(father,"identificativoAvvisatura",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.TIPO_CANALE = new ComplexField(father,"tipoCanale",java.lang.Integer.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.COD_CANALE = new ComplexField(father,"codCanale",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.DATA = new ComplexField(father,"data",java.util.Date.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.COD_ESITO = new ComplexField(father,"codEsito",java.lang.Integer.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.DESCRIZIONE_ESITO = new ComplexField(father,"descrizioneEsito",java.lang.String.class,"EsitoAvvisatura",EsitoAvvisatura.class);
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciato",it.govpay.orm.IdTracciato.class,"EsitoAvvisatura",EsitoAvvisatura.class));
	
	}
	
	

	public IField COD_DOMINIO = null;
	 
	public IField IDENTIFICATIVO_AVVISATURA = null;
	 
	public IField TIPO_CANALE = null;
	 
	public IField COD_CANALE = null;
	 
	public IField DATA = null;
	 
	public IField COD_ESITO = null;
	 
	public IField DESCRIZIONE_ESITO = null;
	 
	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO = null;
	 

	@Override
	public Class<EsitoAvvisatura> getModeledClass(){
		return EsitoAvvisatura.class;
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