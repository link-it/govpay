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

import it.govpay.orm.Avviso;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Avviso 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AvvisoModel extends AbstractModel<Avviso> {

	public AvvisoModel(){
	
		super();
	
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Avviso",Avviso.class);
		this.IUV = new Field("iuv",java.lang.String.class,"Avviso",Avviso.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"Avviso",Avviso.class);
		this.STATO = new Field("stato",java.lang.String.class,"Avviso",Avviso.class);
		this.PDF = new Field("pdf",byte[].class,"Avviso",Avviso.class);
		this.OPERAZIONE = new it.govpay.orm.model.OperazioneModel(new Field("operazione",it.govpay.orm.Operazione.class,"Avviso",Avviso.class));
	
	}
	
	public AvvisoModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Avviso",Avviso.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Avviso",Avviso.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"Avviso",Avviso.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Avviso",Avviso.class);
		this.PDF = new ComplexField(father,"pdf",byte[].class,"Avviso",Avviso.class);
		this.OPERAZIONE = new it.govpay.orm.model.OperazioneModel(new ComplexField(father,"operazione",it.govpay.orm.Operazione.class,"Avviso",Avviso.class));
	
	}
	
	

	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField STATO = null;
	 
	public IField PDF = null;
	 
	public it.govpay.orm.model.OperazioneModel OPERAZIONE = null;
	 

	@Override
	public Class<Avviso> getModeledClass(){
		return Avviso.class;
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