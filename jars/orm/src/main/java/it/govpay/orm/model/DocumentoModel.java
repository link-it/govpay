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

import it.govpay.orm.Documento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Documento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class DocumentoModel extends AbstractModel<Documento> {

	public DocumentoModel(){
	
		super();
	
		this.COD_DOCUMENTO = new Field("codDocumento",java.lang.String.class,"Documento",Documento.class);
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"Documento",Documento.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"Documento",Documento.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Documento",Documento.class));
	
	}
	
	public DocumentoModel(IField father){
	
		super(father);
	
		this.COD_DOCUMENTO = new ComplexField(father,"codDocumento",java.lang.String.class,"Documento",Documento.class);
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"Documento",Documento.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"Documento",Documento.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Documento",Documento.class));
	
	}
	
	

	public IField COD_DOCUMENTO = null;
	 
	public IField DESCRIZIONE = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 

	@Override
	public Class<Documento> getModeledClass(){
		return Documento.class;
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