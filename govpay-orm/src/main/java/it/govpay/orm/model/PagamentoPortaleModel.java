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

import it.govpay.orm.PagamentoPortale;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model PagamentoPortale 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoPortaleModel extends AbstractModel<PagamentoPortale> {

	public PagamentoPortaleModel(){
	
		super();
	
		this.COD_PORTALE = new Field("codPortale",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.COD_CANALE = new Field("codCanale",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.NOME = new Field("nome",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.VERSANTE_IDENTIFICATIVO = new Field("versanteIdentificativo",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.ID_SESSIONE = new Field("idSessione",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.ID_SESSIONE_PORTALE = new Field("idSessionePortale",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.ID_SESSIONE_PSP = new Field("idSessionePsp",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.STATO = new Field("stato",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.CODICE_STATO = new Field("codiceStato",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.PSP_REDIRECT_URL = new Field("pspRedirectURL",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.PSP_ESITO = new Field("pspEsito",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.JSON_REQUEST = new Field("jsonRequest",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_ID_DOMINIO = new Field("wispIdDominio",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_KEY_PA = new Field("wispKeyPA",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_KEY_WISP = new Field("wispKeyWisp",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_HTML = new Field("wispHtml",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.DATA_RICHIESTA = new Field("dataRichiesta",java.util.Date.class,"PagamentoPortale",PagamentoPortale.class);
		this.URL_RITORNO = new Field("urlRitorno",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
	
	}
	
	public PagamentoPortaleModel(IField father){
	
		super(father);
	
		this.COD_PORTALE = new ComplexField(father,"codPortale",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.COD_CANALE = new ComplexField(father,"codCanale",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.NOME = new ComplexField(father,"nome",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.VERSANTE_IDENTIFICATIVO = new ComplexField(father,"versanteIdentificativo",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.ID_SESSIONE = new ComplexField(father,"idSessione",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.ID_SESSIONE_PORTALE = new ComplexField(father,"idSessionePortale",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.ID_SESSIONE_PSP = new ComplexField(father,"idSessionePsp",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.CODICE_STATO = new ComplexField(father,"codiceStato",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.PSP_REDIRECT_URL = new ComplexField(father,"pspRedirectURL",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.PSP_ESITO = new ComplexField(father,"pspEsito",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.JSON_REQUEST = new ComplexField(father,"jsonRequest",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_ID_DOMINIO = new ComplexField(father,"wispIdDominio",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_KEY_PA = new ComplexField(father,"wispKeyPA",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_KEY_WISP = new ComplexField(father,"wispKeyWisp",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.WISP_HTML = new ComplexField(father,"wispHtml",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.DATA_RICHIESTA = new ComplexField(father,"dataRichiesta",java.util.Date.class,"PagamentoPortale",PagamentoPortale.class);
		this.URL_RITORNO = new ComplexField(father,"urlRitorno",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"PagamentoPortale",PagamentoPortale.class);
	
	}
	
	

	public IField COD_PORTALE = null;
	 
	public IField COD_CANALE = null;
	 
	public IField NOME = null;
	 
	public IField VERSANTE_IDENTIFICATIVO = null;
	 
	public IField ID_SESSIONE = null;
	 
	public IField ID_SESSIONE_PORTALE = null;
	 
	public IField ID_SESSIONE_PSP = null;
	 
	public IField STATO = null;
	 
	public IField CODICE_STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField PSP_REDIRECT_URL = null;
	 
	public IField PSP_ESITO = null;
	 
	public IField JSON_REQUEST = null;
	 
	public IField WISP_ID_DOMINIO = null;
	 
	public IField WISP_KEY_PA = null;
	 
	public IField WISP_KEY_WISP = null;
	 
	public IField WISP_HTML = null;
	 
	public IField DATA_RICHIESTA = null;
	 
	public IField URL_RITORNO = null;
	 
	public IField COD_PSP = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 

	@Override
	public Class<PagamentoPortale> getModeledClass(){
		return PagamentoPortale.class;
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