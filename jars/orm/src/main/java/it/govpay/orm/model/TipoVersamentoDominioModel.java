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

import it.govpay.orm.TipoVersamentoDominio;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TipoVersamentoDominio 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoDominioModel extends AbstractModel<TipoVersamentoDominio> {

	public TipoVersamentoDominioModel(){
	
		super();
	
		this.TIPO_VERSAMENTO = new it.govpay.orm.model.TipoVersamentoModel(new Field("tipoVersamento",it.govpay.orm.TipoVersamento.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.CODIFICA_IUV = new Field("codificaIuv",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAGA_TERZI = new Field("pagaTerzi",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.ABILITATO = new Field("abilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_FORM_TIPO = new Field("boFormTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_FORM_DEFINIZIONE = new Field("boFormDefinizione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_VALIDAZIONE_DEF = new Field("boValidazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_TRASFORMAZIONE_TIPO = new Field("boTrasformazioneTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_TRASFORMAZIONE_DEF = new Field("boTrasformazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_COD_APPLICAZIONE = new Field("boCodApplicazione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_ABILITATO = new Field("boAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_FORM_TIPO = new Field("pagFormTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_FORM_DEFINIZIONE = new Field("pagFormDefinizione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_FORM_IMPAGINAZIONE = new Field("pagFormImpaginazione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_VALIDAZIONE_DEF = new Field("pagValidazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_TRASFORMAZIONE_TIPO = new Field("pagTrasformazioneTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_TRASFORMAZIONE_DEF = new Field("pagTrasformazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_COD_APPLICAZIONE = new Field("pagCodApplicazione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_ABILITATO = new Field("pagAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_ABILITATO = new Field("avvMailPromAvvAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_PDF = new Field("avvMailPromAvvPdf",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_TIPO = new Field("avvMailPromAvvTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_OGGETTO = new Field("avvMailPromAvvOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_MESSAGGIO = new Field("avvMailPromAvvMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_ABILITATO = new Field("avvMailPromRicAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_PDF = new Field("avvMailPromRicPdf",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_TIPO = new Field("avvMailPromRicTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_OGGETTO = new Field("avvMailPromRicOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_MESSAGGIO = new Field("avvMailPromRicMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_ESEGUITI = new Field("avvMailPromRicEseguiti",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_ABILITATO = new Field("avvMailPromScadAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_PREAVVISO = new Field("avvMailPromScadPreavviso",java.lang.Integer.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_TIPO = new Field("avvMailPromScadTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_OGGETTO = new Field("avvMailPromScadOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_MESSAGGIO = new Field("avvMailPromScadMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.VISUALIZZAZIONE_DEFINIZIONE = new Field("visualizzazioneDefinizione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_TIPO = new Field("tracCsvTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_HEADER_RISPOSTA = new Field("tracCsvHeaderRisposta",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_TEMPLATE_RICHIESTA = new Field("tracCsvTemplateRichiesta",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_TEMPLATE_RISPOSTA = new Field("tracCsvTemplateRisposta",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.APP_IO_API_KEY = new Field("appIoApiKey",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_ABILITATO = new Field("avvAppIoPromAvvAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_TIPO = new Field("avvAppIoPromAvvTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_OGGETTO = new Field("avvAppIoPromAvvOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_MESSAGGIO = new Field("avvAppIoPromAvvMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_ABILITATO = new Field("avvAppIoPromRicAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_TIPO = new Field("avvAppIoPromRicTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_OGGETTO = new Field("avvAppIoPromRicOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_MESSAGGIO = new Field("avvAppIoPromRicMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_ESEGUITI = new Field("avvAppIoPromRicEseguiti",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_ABILITATO = new Field("avvAppIoPromScadAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_PREAVVISO = new Field("avvAppIoPromScadPreavviso",java.lang.Integer.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_TIPO = new Field("avvAppIoPromScadTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_OGGETTO = new Field("avvAppIoPromScadOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_MESSAGGIO = new Field("avvAppIoPromScadMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
	
	}
	
	public TipoVersamentoDominioModel(IField father){
	
		super(father);
	
		this.TIPO_VERSAMENTO = new it.govpay.orm.model.TipoVersamentoModel(new ComplexField(father,"tipoVersamento",it.govpay.orm.TipoVersamento.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.CODIFICA_IUV = new ComplexField(father,"codificaIuv",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAGA_TERZI = new ComplexField(father,"pagaTerzi",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.ABILITATO = new ComplexField(father,"abilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_FORM_TIPO = new ComplexField(father,"boFormTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_FORM_DEFINIZIONE = new ComplexField(father,"boFormDefinizione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_VALIDAZIONE_DEF = new ComplexField(father,"boValidazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_TRASFORMAZIONE_TIPO = new ComplexField(father,"boTrasformazioneTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_TRASFORMAZIONE_DEF = new ComplexField(father,"boTrasformazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_COD_APPLICAZIONE = new ComplexField(father,"boCodApplicazione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.BO_ABILITATO = new ComplexField(father,"boAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_FORM_TIPO = new ComplexField(father,"pagFormTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_FORM_DEFINIZIONE = new ComplexField(father,"pagFormDefinizione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_FORM_IMPAGINAZIONE = new ComplexField(father,"pagFormImpaginazione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_VALIDAZIONE_DEF = new ComplexField(father,"pagValidazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_TRASFORMAZIONE_TIPO = new ComplexField(father,"pagTrasformazioneTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_TRASFORMAZIONE_DEF = new ComplexField(father,"pagTrasformazioneDef",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_COD_APPLICAZIONE = new ComplexField(father,"pagCodApplicazione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAG_ABILITATO = new ComplexField(father,"pagAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_ABILITATO = new ComplexField(father,"avvMailPromAvvAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_PDF = new ComplexField(father,"avvMailPromAvvPdf",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_TIPO = new ComplexField(father,"avvMailPromAvvTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_OGGETTO = new ComplexField(father,"avvMailPromAvvOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_AVV_MESSAGGIO = new ComplexField(father,"avvMailPromAvvMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_ABILITATO = new ComplexField(father,"avvMailPromRicAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_PDF = new ComplexField(father,"avvMailPromRicPdf",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_TIPO = new ComplexField(father,"avvMailPromRicTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_OGGETTO = new ComplexField(father,"avvMailPromRicOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_MESSAGGIO = new ComplexField(father,"avvMailPromRicMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_RIC_ESEGUITI = new ComplexField(father,"avvMailPromRicEseguiti",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_ABILITATO = new ComplexField(father,"avvMailPromScadAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_PREAVVISO = new ComplexField(father,"avvMailPromScadPreavviso",java.lang.Integer.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_TIPO = new ComplexField(father,"avvMailPromScadTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_OGGETTO = new ComplexField(father,"avvMailPromScadOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_MAIL_PROM_SCAD_MESSAGGIO = new ComplexField(father,"avvMailPromScadMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.VISUALIZZAZIONE_DEFINIZIONE = new ComplexField(father,"visualizzazioneDefinizione",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_TIPO = new ComplexField(father,"tracCsvTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_HEADER_RISPOSTA = new ComplexField(father,"tracCsvHeaderRisposta",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_TEMPLATE_RICHIESTA = new ComplexField(father,"tracCsvTemplateRichiesta",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TRAC_CSV_TEMPLATE_RISPOSTA = new ComplexField(father,"tracCsvTemplateRisposta",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.APP_IO_API_KEY = new ComplexField(father,"appIoApiKey",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_ABILITATO = new ComplexField(father,"avvAppIoPromAvvAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_TIPO = new ComplexField(father,"avvAppIoPromAvvTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_OGGETTO = new ComplexField(father,"avvAppIoPromAvvOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_AVV_MESSAGGIO = new ComplexField(father,"avvAppIoPromAvvMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_ABILITATO = new ComplexField(father,"avvAppIoPromRicAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_TIPO = new ComplexField(father,"avvAppIoPromRicTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_OGGETTO = new ComplexField(father,"avvAppIoPromRicOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_MESSAGGIO = new ComplexField(father,"avvAppIoPromRicMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_RIC_ESEGUITI = new ComplexField(father,"avvAppIoPromRicEseguiti",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_ABILITATO = new ComplexField(father,"avvAppIoPromScadAbilitato",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_PREAVVISO = new ComplexField(father,"avvAppIoPromScadPreavviso",java.lang.Integer.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_TIPO = new ComplexField(father,"avvAppIoPromScadTipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_OGGETTO = new ComplexField(father,"avvAppIoPromScadOggetto",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.AVV_APP_IO_PROM_SCAD_MESSAGGIO = new ComplexField(father,"avvAppIoPromScadMessaggio",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
	
	}
	
	

	public it.govpay.orm.model.TipoVersamentoModel TIPO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField CODIFICA_IUV = null;
	 
	public IField PAGA_TERZI = null;
	 
	public IField ABILITATO = null;
	 
	public IField BO_FORM_TIPO = null;
	 
	public IField BO_FORM_DEFINIZIONE = null;
	 
	public IField BO_VALIDAZIONE_DEF = null;
	 
	public IField BO_TRASFORMAZIONE_TIPO = null;
	 
	public IField BO_TRASFORMAZIONE_DEF = null;
	 
	public IField BO_COD_APPLICAZIONE = null;
	 
	public IField BO_ABILITATO = null;
	 
	public IField PAG_FORM_TIPO = null;
	 
	public IField PAG_FORM_DEFINIZIONE = null;
	 
	public IField PAG_FORM_IMPAGINAZIONE = null;
	 
	public IField PAG_VALIDAZIONE_DEF = null;
	 
	public IField PAG_TRASFORMAZIONE_TIPO = null;
	 
	public IField PAG_TRASFORMAZIONE_DEF = null;
	 
	public IField PAG_COD_APPLICAZIONE = null;
	 
	public IField PAG_ABILITATO = null;
	 
	public IField AVV_MAIL_PROM_AVV_ABILITATO = null;
	 
	public IField AVV_MAIL_PROM_AVV_PDF = null;
	 
	public IField AVV_MAIL_PROM_AVV_TIPO = null;
	 
	public IField AVV_MAIL_PROM_AVV_OGGETTO = null;
	 
	public IField AVV_MAIL_PROM_AVV_MESSAGGIO = null;
	 
	public IField AVV_MAIL_PROM_RIC_ABILITATO = null;
	 
	public IField AVV_MAIL_PROM_RIC_PDF = null;
	 
	public IField AVV_MAIL_PROM_RIC_TIPO = null;
	 
	public IField AVV_MAIL_PROM_RIC_OGGETTO = null;
	 
	public IField AVV_MAIL_PROM_RIC_MESSAGGIO = null;
	 
	public IField AVV_MAIL_PROM_RIC_ESEGUITI = null;
	 
	public IField AVV_MAIL_PROM_SCAD_ABILITATO = null;
	 
	public IField AVV_MAIL_PROM_SCAD_PREAVVISO = null;
	 
	public IField AVV_MAIL_PROM_SCAD_TIPO = null;
	 
	public IField AVV_MAIL_PROM_SCAD_OGGETTO = null;
	 
	public IField AVV_MAIL_PROM_SCAD_MESSAGGIO = null;
	 
	public IField VISUALIZZAZIONE_DEFINIZIONE = null;
	 
	public IField TRAC_CSV_TIPO = null;
	 
	public IField TRAC_CSV_HEADER_RISPOSTA = null;
	 
	public IField TRAC_CSV_TEMPLATE_RICHIESTA = null;
	 
	public IField TRAC_CSV_TEMPLATE_RISPOSTA = null;
	 
	public IField APP_IO_API_KEY = null;
	 
	public IField AVV_APP_IO_PROM_AVV_ABILITATO = null;
	 
	public IField AVV_APP_IO_PROM_AVV_TIPO = null;
	 
	public IField AVV_APP_IO_PROM_AVV_OGGETTO = null;
	 
	public IField AVV_APP_IO_PROM_AVV_MESSAGGIO = null;
	 
	public IField AVV_APP_IO_PROM_RIC_ABILITATO = null;
	 
	public IField AVV_APP_IO_PROM_RIC_TIPO = null;
	 
	public IField AVV_APP_IO_PROM_RIC_OGGETTO = null;
	 
	public IField AVV_APP_IO_PROM_RIC_MESSAGGIO = null;
	 
	public IField AVV_APP_IO_PROM_RIC_ESEGUITI = null;
	 
	public IField AVV_APP_IO_PROM_SCAD_ABILITATO = null;
	 
	public IField AVV_APP_IO_PROM_SCAD_PREAVVISO = null;
	 
	public IField AVV_APP_IO_PROM_SCAD_TIPO = null;
	 
	public IField AVV_APP_IO_PROM_SCAD_OGGETTO = null;
	 
	public IField AVV_APP_IO_PROM_SCAD_MESSAGGIO = null;
	 

	@Override
	public Class<TipoVersamentoDominio> getModeledClass(){
		return TipoVersamentoDominio.class;
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
