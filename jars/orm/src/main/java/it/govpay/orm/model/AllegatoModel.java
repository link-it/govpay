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

import it.govpay.orm.Allegato;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Allegato 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AllegatoModel extends AbstractModel<Allegato> {

	public AllegatoModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"Allegato",Allegato.class));
		this.NOME = new Field("nome",java.lang.String.class,"Allegato",Allegato.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"Allegato",Allegato.class);
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"Allegato",Allegato.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"Allegato",Allegato.class);
		this.RAW_CONTENUTO = new Field("rawContenuto",byte[].class,"Allegato",Allegato.class);
	
	}
	
	public AllegatoModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"Allegato",Allegato.class));
		this.NOME = new ComplexField(father,"nome",java.lang.String.class,"Allegato",Allegato.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"Allegato",Allegato.class);
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"Allegato",Allegato.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"Allegato",Allegato.class);
		this.RAW_CONTENUTO = new ComplexField(father,"rawContenuto",byte[].class,"Allegato",Allegato.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public IField NOME = null;
	 
	public IField TIPO = null;
	 
	public IField DESCRIZIONE = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField RAW_CONTENUTO = null;
	 

	@Override
	public Class<Allegato> getModeledClass(){
		return Allegato.class;
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
