/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Stampa;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Stampa 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class StampaModel extends AbstractModel<Stampa> {

	public StampaModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"Stampa",Stampa.class));
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"Stampa",Stampa.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"Stampa",Stampa.class);
		this.PDF = new Field("pdf",byte[].class,"Stampa",Stampa.class);
		this.ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new Field("idDocumento",it.govpay.orm.IdDocumento.class,"Stampa",Stampa.class));
	
	}
	
	public StampaModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"Stampa",Stampa.class));
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"Stampa",Stampa.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"Stampa",Stampa.class);
		this.PDF = new ComplexField(father,"pdf",byte[].class,"Stampa",Stampa.class);
		this.ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new ComplexField(father,"idDocumento",it.govpay.orm.IdDocumento.class,"Stampa",Stampa.class));
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField TIPO = null;
	 
	public IField PDF = null;
	 
	public it.govpay.orm.model.IdDocumentoModel ID_DOCUMENTO = null;
	 

	@Override
	public Class<Stampa> getModeledClass(){
		return Stampa.class;
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
