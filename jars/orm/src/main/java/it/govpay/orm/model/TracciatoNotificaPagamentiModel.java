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

import it.govpay.orm.TracciatoNotificaPagamenti;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TracciatoNotificaPagamenti 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoNotificaPagamentiModel extends AbstractModel<TracciatoNotificaPagamenti> {

	public TracciatoNotificaPagamentiModel(){
	
		super();
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class));
		this.NOME_FILE = new Field("nomeFile",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.VERSIONE = new Field("versione",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.STATO = new Field("stato",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_RT_DA = new Field("dataRtDa",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_RT_A = new Field("dataRtA",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_CARICAMENTO = new Field("dataCaricamento",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_COMPLETAMENTO = new Field("dataCompletamento",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.RAW_CONTENUTO = new Field("rawContenuto",byte[].class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.BEAN_DATI = new Field("beanDati",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.IDENTIFICATIVO = new Field("identificativo",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
	
	}
	
	public TracciatoNotificaPagamentiModel(IField father){
	
		super(father);
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class));
		this.NOME_FILE = new ComplexField(father,"nomeFile",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.VERSIONE = new ComplexField(father,"versione",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_RT_DA = new ComplexField(father,"dataRtDa",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_RT_A = new ComplexField(father,"dataRtA",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_CARICAMENTO = new ComplexField(father,"dataCaricamento",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.DATA_COMPLETAMENTO = new ComplexField(father,"dataCompletamento",java.util.Date.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.RAW_CONTENUTO = new ComplexField(father,"rawContenuto",byte[].class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.BEAN_DATI = new ComplexField(father,"beanDati",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
		this.IDENTIFICATIVO = new ComplexField(father,"identificativo",java.lang.String.class,"TracciatoNotificaPagamenti",TracciatoNotificaPagamenti.class);
	
	}
	
	

	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField NOME_FILE = null;
	 
	public IField TIPO = null;
	 
	public IField VERSIONE = null;
	 
	public IField STATO = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField DATA_RT_DA = null;
	 
	public IField DATA_RT_A = null;
	 
	public IField DATA_CARICAMENTO = null;
	 
	public IField DATA_COMPLETAMENTO = null;
	 
	public IField RAW_CONTENUTO = null;
	 
	public IField BEAN_DATI = null;
	 
	public IField IDENTIFICATIVO = null;
	 

	@Override
	public Class<TracciatoNotificaPagamenti> getModeledClass(){
		return TracciatoNotificaPagamenti.class;
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
