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
		this.FORM_TIPO = new Field("formTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.FORM_DEFINIZIONE = new Field("formDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.VALIDAZIONE_DEFINIZIONE = new Field("validazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRASFORMAZIONE_TIPO = new Field("trasformazioneTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRASFORMAZIONE_DEFINIZIONE = new Field("trasformazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.COD_APPLICAZIONE = new Field("codApplicazione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_ABILITATO = new Field("promemoriaAvvisoAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_PDF = new Field("promemoriaAvvisoPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_TIPO = new Field("promemoriaAvvisoTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_OGGETTO = new Field("promemoriaAvvisoOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_MESSAGGIO = new Field("promemoriaAvvisoMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_ABILITATO = new Field("promemoriaRicevutaAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_TIPO = new Field("promemoriaRicevutaTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_PDF = new Field("promemoriaRicevutaPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_OGGETTO = new Field("promemoriaRicevutaOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_MESSAGGIO = new Field("promemoriaRicevutaMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.VISUALIZZAZIONE_DEFINIZIONE = new Field("visualizzazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TIPO = new Field("tracCsvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_HEADER_RISPOSTA = new Field("tracCsvHeaderRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RICHIESTA = new Field("tracCsvTemplateRichiesta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RISPOSTA = new Field("tracCsvTemplateRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
	
	}
	
	public TipoVersamentoModel(IField father){
	
		super(father);
	
		this.COD_TIPO_VERSAMENTO = new ComplexField(father,"codTipoVersamento",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.CODIFICA_IUV = new ComplexField(father,"codificaIuv",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAGA_TERZI = new ComplexField(father,"pagaTerzi",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.FORM_TIPO = new ComplexField(father,"formTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.FORM_DEFINIZIONE = new ComplexField(father,"formDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.VALIDAZIONE_DEFINIZIONE = new ComplexField(father,"validazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRASFORMAZIONE_TIPO = new ComplexField(father,"trasformazioneTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRASFORMAZIONE_DEFINIZIONE = new ComplexField(father,"trasformazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.COD_APPLICAZIONE = new ComplexField(father,"codApplicazione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_ABILITATO = new ComplexField(father,"promemoriaAvvisoAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_PDF = new ComplexField(father,"promemoriaAvvisoPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_TIPO = new ComplexField(father,"promemoriaAvvisoTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_OGGETTO = new ComplexField(father,"promemoriaAvvisoOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_AVVISO_MESSAGGIO = new ComplexField(father,"promemoriaAvvisoMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_ABILITATO = new ComplexField(father,"promemoriaRicevutaAbilitato",boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_TIPO = new ComplexField(father,"promemoriaRicevutaTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_PDF = new ComplexField(father,"promemoriaRicevutaPdf",Boolean.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_OGGETTO = new ComplexField(father,"promemoriaRicevutaOggetto",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PROMEMORIA_RICEVUTA_MESSAGGIO = new ComplexField(father,"promemoriaRicevutaMessaggio",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.VISUALIZZAZIONE_DEFINIZIONE = new ComplexField(father,"visualizzazioneDefinizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TIPO = new ComplexField(father,"tracCsvTipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_HEADER_RISPOSTA = new ComplexField(father,"tracCsvHeaderRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RICHIESTA = new ComplexField(father,"tracCsvTemplateRichiesta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TRAC_CSV_TEMPLATE_RISPOSTA = new ComplexField(father,"tracCsvTemplateRisposta",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
	
	}
	
	

	public IField COD_TIPO_VERSAMENTO = null;
	 
	public IField DESCRIZIONE = null;
	 
	public IField CODIFICA_IUV = null;
	 
	public IField TIPO = null;
	 
	public IField PAGA_TERZI = null;
	 
	public IField ABILITATO = null;
	 
	public IField FORM_TIPO = null;
	 
	public IField FORM_DEFINIZIONE = null;
	 
	public IField VALIDAZIONE_DEFINIZIONE = null;
	 
	public IField TRASFORMAZIONE_TIPO = null;
	 
	public IField TRASFORMAZIONE_DEFINIZIONE = null;
	 
	public IField COD_APPLICAZIONE = null;
	 
	public IField PROMEMORIA_AVVISO_ABILITATO = null;
	 
	public IField PROMEMORIA_AVVISO_PDF = null;
	 
	public IField PROMEMORIA_AVVISO_TIPO = null;
	 
	public IField PROMEMORIA_AVVISO_OGGETTO = null;
	 
	public IField PROMEMORIA_AVVISO_MESSAGGIO = null;
	 
	public IField PROMEMORIA_RICEVUTA_ABILITATO = null;
	 
	public IField PROMEMORIA_RICEVUTA_TIPO = null;
	 
	public IField PROMEMORIA_RICEVUTA_PDF = null;
	 
	public IField PROMEMORIA_RICEVUTA_OGGETTO = null;
	 
	public IField PROMEMORIA_RICEVUTA_MESSAGGIO = null;
	 
	public IField VISUALIZZAZIONE_DEFINIZIONE = null;
	 
	public IField TRAC_CSV_TIPO = null;
	 
	public IField TRAC_CSV_HEADER_RISPOSTA = null;
	 
	public IField TRAC_CSV_TEMPLATE_RICHIESTA = null;
	 
	public IField TRAC_CSV_TEMPLATE_RISPOSTA = null;
	 

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
