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

import it.govpay.orm.TipoVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TipoVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoModel extends AbstractModel<TipoVersamento> {

	public TipoVersamentoModel(){
	
		super();
	
		this.COD_TIPO_VERSAMENTO = new Field("codTipoVersamento",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.CODIFICA_IUV = new Field("codificaIuv",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAGA_TERZI = new Field("pagaTerzi",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.BO_FORM_TIPO = new Field("boFormTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_FORM_DEFINIZIONE = new Field("boFormDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_VALIDAZIONE_DEF = new Field("boValidazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_TRASFORMAZIONE_TIPO = new Field("boTrasformazioneTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_TRASFORMAZIONE_DEF = new Field("boTrasformazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_COD_APPLICAZIONE = new Field("boCodApplicazione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_ABILITATO = new Field("boAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_FORM_TIPO = new Field("pagFormTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_FORM_DEFINIZIONE = new Field("pagFormDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_VALIDAZIONE_DEF = new Field("pagValidazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_TRASFORMAZIONE_TIPO = new Field("pagTrasformazioneTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_TRASFORMAZIONE_DEF = new Field("pagTrasformazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_COD_APPLICAZIONE = new Field("pagCodApplicazione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_ABILITATO = new Field("pagAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_ABILITATO = new Field("avvMailPromAvvAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_PDF = new Field("avvMailPromAvvPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_TIPO = new Field("avvMailPromAvvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_OGGETTO = new Field("avvMailPromAvvOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_MESSAGGIO = new Field("avvMailPromAvvMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_ABILITATO = new Field("avvMailPromRicAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_PDF = new Field("avvMailPromRicPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_TIPO = new Field("avvMailPromRicTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_OGGETTO = new Field("avvMailPromRicOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_MESSAGGIO = new Field("avvMailPromRicMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_ESEGUITI = new Field("avvMailPromRicEseguiti",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_ABILITATO = new Field("avvMailPromScadAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_PREAVVISO = new Field("avvMailPromScadPreavviso",java.lang.Integer.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_TIPO = new Field("avvMailPromScadTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_OGGETTO = new Field("avvMailPromScadOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_MESSAGGIO = new Field("avvMailPromScadMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.VISUALIZZAZIONE_DEFINIZIONE = new Field("visualizzazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TIPO = new Field("tracCsvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_HEADER_RISPOSTA = new Field("tracCsvHeaderRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RICHIESTA = new Field("tracCsvTemplateRichiesta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RISPOSTA = new Field("tracCsvTemplateRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_ABILITATO = new Field("avvAppIoPromAvvAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_TIPO = new Field("avvAppIoPromAvvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_OGGETTO = new Field("avvAppIoPromAvvOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_MESSAGGIO = new Field("avvAppIoPromAvvMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_ABILITATO = new Field("avvAppIoPromRicAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_TIPO = new Field("avvAppIoPromRicTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_OGGETTO = new Field("avvAppIoPromRicOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_MESSAGGIO = new Field("avvAppIoPromRicMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_ESEGUITI = new Field("avvAppIoPromRicEseguiti",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_ABILITATO = new Field("avvAppIoPromScadAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_PREAVVISO = new Field("avvAppIoPromScadPreavviso",java.lang.Integer.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_TIPO = new Field("avvAppIoPromScadTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_OGGETTO = new Field("avvAppIoPromScadOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_MESSAGGIO = new Field("avvAppIoPromScadMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
	
	}
	
	public TipoVersamentoModel(IField father){
	
		super(father);
	
		this.COD_TIPO_VERSAMENTO = new ComplexField(father,"codTipoVersamento",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.CODIFICA_IUV = new ComplexField(father,"codificaIuv",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAGA_TERZI = new ComplexField(father,"pagaTerzi",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.BO_FORM_TIPO = new ComplexField(father,"boFormTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_FORM_DEFINIZIONE = new ComplexField(father,"boFormDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_VALIDAZIONE_DEF = new ComplexField(father,"boValidazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_TRASFORMAZIONE_TIPO = new ComplexField(father,"boTrasformazioneTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_TRASFORMAZIONE_DEF = new ComplexField(father,"boTrasformazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_COD_APPLICAZIONE = new ComplexField(father,"boCodApplicazione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.BO_ABILITATO = new ComplexField(father,"boAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_FORM_TIPO = new ComplexField(father,"pagFormTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_FORM_DEFINIZIONE = new ComplexField(father,"pagFormDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_VALIDAZIONE_DEF = new ComplexField(father,"pagValidazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_TRASFORMAZIONE_TIPO = new ComplexField(father,"pagTrasformazioneTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_TRASFORMAZIONE_DEF = new ComplexField(father,"pagTrasformazioneDef",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_COD_APPLICAZIONE = new ComplexField(father,"pagCodApplicazione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAG_ABILITATO = new ComplexField(father,"pagAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_ABILITATO = new ComplexField(father,"avvMailPromAvvAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_PDF = new ComplexField(father,"avvMailPromAvvPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_TIPO = new ComplexField(father,"avvMailPromAvvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_OGGETTO = new ComplexField(father,"avvMailPromAvvOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_AVV_MESSAGGIO = new ComplexField(father,"avvMailPromAvvMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_ABILITATO = new ComplexField(father,"avvMailPromRicAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_PDF = new ComplexField(father,"avvMailPromRicPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_TIPO = new ComplexField(father,"avvMailPromRicTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_OGGETTO = new ComplexField(father,"avvMailPromRicOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_MESSAGGIO = new ComplexField(father,"avvMailPromRicMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_RIC_ESEGUITI = new ComplexField(father,"avvMailPromRicEseguiti",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_ABILITATO = new ComplexField(father,"avvMailPromScadAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_PREAVVISO = new ComplexField(father,"avvMailPromScadPreavviso",java.lang.Integer.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_TIPO = new ComplexField(father,"avvMailPromScadTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_OGGETTO = new ComplexField(father,"avvMailPromScadOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_MAIL_PROM_SCAD_MESSAGGIO = new ComplexField(father,"avvMailPromScadMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.VISUALIZZAZIONE_DEFINIZIONE = new ComplexField(father,"visualizzazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TIPO = new ComplexField(father,"tracCsvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_HEADER_RISPOSTA = new ComplexField(father,"tracCsvHeaderRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RICHIESTA = new ComplexField(father,"tracCsvTemplateRichiesta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RISPOSTA = new ComplexField(father,"tracCsvTemplateRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_ABILITATO = new ComplexField(father,"avvAppIoPromAvvAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_TIPO = new ComplexField(father,"avvAppIoPromAvvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_OGGETTO = new ComplexField(father,"avvAppIoPromAvvOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_AVV_MESSAGGIO = new ComplexField(father,"avvAppIoPromAvvMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_ABILITATO = new ComplexField(father,"avvAppIoPromRicAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_TIPO = new ComplexField(father,"avvAppIoPromRicTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_OGGETTO = new ComplexField(father,"avvAppIoPromRicOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_MESSAGGIO = new ComplexField(father,"avvAppIoPromRicMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_RIC_ESEGUITI = new ComplexField(father,"avvAppIoPromRicEseguiti",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_ABILITATO = new ComplexField(father,"avvAppIoPromScadAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_PREAVVISO = new ComplexField(father,"avvAppIoPromScadPreavviso",java.lang.Integer.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_TIPO = new ComplexField(father,"avvAppIoPromScadTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_OGGETTO = new ComplexField(father,"avvAppIoPromScadOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.AVV_APP_IO_PROM_SCAD_MESSAGGIO = new ComplexField(father,"avvAppIoPromScadMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
	
	}
	
	

	public IField COD_TIPO_VERSAMENTO = null;
	 
	public IField DESCRIZIONE = null;
	 
	public IField CODIFICA_IUV = null;
	 
	public IField TIPO = null;
	 
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
	public Class<TipoVersamento> getModeledClass(){
		return TipoVersamento.class;
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
