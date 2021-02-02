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

import it.govpay.orm.TracciatoMyPivot;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TracciatoMyPivot 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoMyPivotModel extends AbstractModel<TracciatoMyPivot> {

	public TracciatoMyPivotModel(){
	
		super();
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"TracciatoMyPivot",TracciatoMyPivot.class));
		this.NOME_FILE = new Field("nomeFile",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.STATO = new Field("stato",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_RT_DA = new Field("dataRtDa",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_RT_A = new Field("dataRtA",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_CARICAMENTO = new Field("dataCaricamento",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_COMPLETAMENTO = new Field("dataCompletamento",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.REQUEST_TOKEN = new Field("requestToken",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.UPLOAD_URL = new Field("uploadUrl",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.AUTHORIZATION_TOKEN = new Field("authorizationToken",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.RAW_CONTENUTO = new Field("rawContenuto",byte[].class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.BEAN_DATI = new Field("beanDati",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
	
	}
	
	public TracciatoMyPivotModel(IField father){
	
		super(father);
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"TracciatoMyPivot",TracciatoMyPivot.class));
		this.NOME_FILE = new ComplexField(father,"nomeFile",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_RT_DA = new ComplexField(father,"dataRtDa",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_RT_A = new ComplexField(father,"dataRtA",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_CARICAMENTO = new ComplexField(father,"dataCaricamento",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.DATA_COMPLETAMENTO = new ComplexField(father,"dataCompletamento",java.util.Date.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.REQUEST_TOKEN = new ComplexField(father,"requestToken",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.UPLOAD_URL = new ComplexField(father,"uploadUrl",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.AUTHORIZATION_TOKEN = new ComplexField(father,"authorizationToken",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.RAW_CONTENUTO = new ComplexField(father,"rawContenuto",byte[].class,"TracciatoMyPivot",TracciatoMyPivot.class);
		this.BEAN_DATI = new ComplexField(father,"beanDati",java.lang.String.class,"TracciatoMyPivot",TracciatoMyPivot.class);
	
	}
	
	

	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField NOME_FILE = null;
	 
	public IField STATO = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField DATA_RT_DA = null;
	 
	public IField DATA_RT_A = null;
	 
	public IField DATA_CARICAMENTO = null;
	 
	public IField DATA_COMPLETAMENTO = null;
	 
	public IField REQUEST_TOKEN = null;
	 
	public IField UPLOAD_URL = null;
	 
	public IField AUTHORIZATION_TOKEN = null;
	 
	public IField RAW_CONTENUTO = null;
	 
	public IField BEAN_DATI = null;
	 

	@Override
	public Class<TracciatoMyPivot> getModeledClass(){
		return TracciatoMyPivot.class;
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
