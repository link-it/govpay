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

import it.govpay.orm.IUV;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IUV 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IUVModel extends AbstractModel<IUV> {

	public IUVModel(){
	
		super();
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"IUV",IUV.class));
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"IUV",IUV.class);
		this.PRG = new Field("prg",long.class,"IUV",IUV.class);
		this.IUV = new Field("iuv",java.lang.String.class,"IUV",IUV.class);
		this.DATA_GENERAZIONE = new Field("dataGenerazione",java.util.Date.class,"IUV",IUV.class);
		this.APPLICATION_CODE = new Field("applicationCode",java.lang.Integer.class,"IUV",IUV.class);
		this.AUX_DIGIT = new Field("auxDigit",java.lang.Integer.class,"IUV",IUV.class);
	
	}
	
	public IUVModel(IField father){
	
		super(father);
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"IUV",IUV.class));
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"IUV",IUV.class);
		this.PRG = new ComplexField(father,"prg",long.class,"IUV",IUV.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"IUV",IUV.class);
		this.DATA_GENERAZIONE = new ComplexField(father,"dataGenerazione",java.util.Date.class,"IUV",IUV.class);
		this.APPLICATION_CODE = new ComplexField(father,"applicationCode",java.lang.Integer.class,"IUV",IUV.class);
		this.AUX_DIGIT = new ComplexField(father,"auxDigit",java.lang.Integer.class,"IUV",IUV.class);
	
	}
	
	

	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField PRG = null;
	 
	public IField IUV = null;
	 
	public IField DATA_GENERAZIONE = null;
	 
	public IField APPLICATION_CODE = null;
	 
	public IField AUX_DIGIT = null;
	 

	@Override
	public Class<IUV> getModeledClass(){
		return IUV.class;
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