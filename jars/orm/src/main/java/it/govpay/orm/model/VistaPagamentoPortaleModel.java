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

import it.govpay.orm.VistaPagamentoPortale;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VistaPagamentoPortale 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaPagamentoPortaleModel extends AbstractModel<VistaPagamentoPortale> {

	public VistaPagamentoPortaleModel(){
	
		super();
	
		this.COD_CANALE = new Field("codCanale",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.NOME = new Field("nome",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.IMPORTO = new Field("importo",double.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.VERSANTE_IDENTIFICATIVO = new Field("versanteIdentificativo",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_SESSIONE = new Field("idSessione",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_SESSIONE_PORTALE = new Field("idSessionePortale",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_SESSIONE_PSP = new Field("idSessionePsp",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.STATO = new Field("stato",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.CODICE_STATO = new Field("codiceStato",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.PSP_REDIRECT_URL = new Field("pspRedirectURL",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.PSP_ESITO = new Field("pspEsito",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.DATA_RICHIESTA = new Field("dataRichiesta",java.util.Date.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.URL_RITORNO = new Field("urlRitorno",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.MULTI_BENEFICIARIO = new Field("multiBeneficiario",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ACK = new Field("ack",boolean.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.TIPO = new Field("tipo",int.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.PRINCIPAL = new Field("principal",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.TIPO_UTENZA = new Field("tipo_utenza",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new Field("idUo",it.govpay.orm.IdUo.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.DEBITORE_IDENTIFICATIVO = new Field("debitoreIdentificativo",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
	
	}
	
	public VistaPagamentoPortaleModel(IField father){
	
		super(father);
	
		this.COD_CANALE = new ComplexField(father,"codCanale",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.NOME = new ComplexField(father,"nome",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.IMPORTO = new ComplexField(father,"importo",double.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.VERSANTE_IDENTIFICATIVO = new ComplexField(father,"versanteIdentificativo",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_SESSIONE = new ComplexField(father,"idSessione",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_SESSIONE_PORTALE = new ComplexField(father,"idSessionePortale",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_SESSIONE_PSP = new ComplexField(father,"idSessionePsp",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.CODICE_STATO = new ComplexField(father,"codiceStato",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.PSP_REDIRECT_URL = new ComplexField(father,"pspRedirectURL",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.PSP_ESITO = new ComplexField(father,"pspEsito",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.DATA_RICHIESTA = new ComplexField(father,"dataRichiesta",java.util.Date.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.URL_RITORNO = new ComplexField(father,"urlRitorno",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.MULTI_BENEFICIARIO = new ComplexField(father,"multiBeneficiario",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ACK = new ComplexField(father,"ack",boolean.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.TIPO = new ComplexField(father,"tipo",int.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.PRINCIPAL = new ComplexField(father,"principal",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.TIPO_UTENZA = new ComplexField(father,"tipo_utenza",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"idUo",it.govpay.orm.IdUo.class,"VistaPagamentoPortale",VistaPagamentoPortale.class));
		this.DEBITORE_IDENTIFICATIVO = new ComplexField(father,"debitoreIdentificativo",java.lang.String.class,"VistaPagamentoPortale",VistaPagamentoPortale.class);
	
	}
	
	

	public IField COD_CANALE = null;
	 
	public IField NOME = null;
	 
	public IField IMPORTO = null;
	 
	public IField VERSANTE_IDENTIFICATIVO = null;
	 
	public IField ID_SESSIONE = null;
	 
	public IField ID_SESSIONE_PORTALE = null;
	 
	public IField ID_SESSIONE_PSP = null;
	 
	public IField STATO = null;
	 
	public IField CODICE_STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField PSP_REDIRECT_URL = null;
	 
	public IField PSP_ESITO = null;
	 
	public IField DATA_RICHIESTA = null;
	 
	public IField URL_RITORNO = null;
	 
	public IField COD_PSP = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 
	public IField MULTI_BENEFICIARIO = null;
	 
	public IField ACK = null;
	 
	public IField TIPO = null;
	 
	public IField PRINCIPAL = null;
	 
	public IField TIPO_UTENZA = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoModel ID_TIPO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public it.govpay.orm.model.IdUoModel ID_UO = null;
	 
	public IField DEBITORE_IDENTIFICATIVO = null;
	 

	@Override
	public Class<VistaPagamentoPortale> getModeledClass(){
		return VistaPagamentoPortale.class;
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
