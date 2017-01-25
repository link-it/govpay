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
	
		this.PRG = new Field("prg",long.class,"IUV",IUV.class);
		this.IUV = new Field("iuv",java.lang.String.class,"IUV",IUV.class);
		this.APPLICATION_CODE = new Field("applicationCode",int.class,"IUV",IUV.class);
		this.DATA_GENERAZIONE = new Field("dataGenerazione",java.util.Date.class,"IUV",IUV.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"IUV",IUV.class));
		this.TIPO_IUV = new Field("tipoIuv",java.lang.String.class,"IUV",IUV.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"IUV",IUV.class));
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"IUV",IUV.class);
	
	}
	
	public IUVModel(IField father){
	
		super(father);
	
		this.PRG = new ComplexField(father,"prg",long.class,"IUV",IUV.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"IUV",IUV.class);
		this.APPLICATION_CODE = new ComplexField(father,"applicationCode",int.class,"IUV",IUV.class);
		this.DATA_GENERAZIONE = new ComplexField(father,"dataGenerazione",java.util.Date.class,"IUV",IUV.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"IUV",IUV.class));
		this.TIPO_IUV = new ComplexField(father,"tipoIuv",java.lang.String.class,"IUV",IUV.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"IUV",IUV.class));
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"IUV",IUV.class);
	
	}
	
	

	public IField PRG = null;
	 
	public IField IUV = null;
	 
	public IField APPLICATION_CODE = null;
	 
	public IField DATA_GENERAZIONE = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public IField TIPO_IUV = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField COD_VERSAMENTO_ENTE = null;
	 

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