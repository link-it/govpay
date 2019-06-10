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

import it.govpay.orm.Evento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Evento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EventoModel extends AbstractModel<Evento> {

	public EventoModel(){
	
		super();
	
		this.COMPONENTE = new Field("componente",java.lang.String.class,"Evento",Evento.class);
		this.RUOLO = new Field("ruolo",java.lang.String.class,"Evento",Evento.class);
		this.CATEGORIA_EVENTO = new Field("categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new Field("tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new Field("sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.DATA = new Field("data",java.util.Date.class,"Evento",Evento.class);
		this.INTERVALLO = new Field("intervallo",long.class,"Evento",Evento.class);
		this.ESITO = new Field("esito",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_ESITO = new Field("sottotipoEsito",java.lang.String.class,"Evento",Evento.class);
		this.DETTAGLIO_ESITO = new Field("dettaglioEsito",java.lang.String.class,"Evento",Evento.class);
		this.PARAMETRI_RICHIESTA = new Field("parametriRichiesta",byte[].class,"Evento",Evento.class);
		this.PARAMETRI_RISPOSTA = new Field("parametriRisposta",byte[].class,"Evento",Evento.class);
		this.DATI_PAGO_PA = new Field("datiPagoPA",java.lang.String.class,"Evento",Evento.class);
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"Evento",Evento.class);
		this.COD_APPLICAZIONE = new Field("codApplicazione",java.lang.String.class,"Evento",Evento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"Evento",Evento.class);
		this.CCP = new Field("ccp",java.lang.String.class,"Evento",Evento.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Evento",Evento.class);
		this.ID_SESSIONE = new Field("idSessione",java.lang.String.class,"Evento",Evento.class);
	
	}
	
	public EventoModel(IField father){
	
		super(father);
	
		this.COMPONENTE = new ComplexField(father,"componente",java.lang.String.class,"Evento",Evento.class);
		this.RUOLO = new ComplexField(father,"ruolo",java.lang.String.class,"Evento",Evento.class);
		this.CATEGORIA_EVENTO = new ComplexField(father,"categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new ComplexField(father,"tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new ComplexField(father,"sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.DATA = new ComplexField(father,"data",java.util.Date.class,"Evento",Evento.class);
		this.INTERVALLO = new ComplexField(father,"intervallo",long.class,"Evento",Evento.class);
		this.ESITO = new ComplexField(father,"esito",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_ESITO = new ComplexField(father,"sottotipoEsito",java.lang.String.class,"Evento",Evento.class);
		this.DETTAGLIO_ESITO = new ComplexField(father,"dettaglioEsito",java.lang.String.class,"Evento",Evento.class);
		this.PARAMETRI_RICHIESTA = new ComplexField(father,"parametriRichiesta",byte[].class,"Evento",Evento.class);
		this.PARAMETRI_RISPOSTA = new ComplexField(father,"parametriRisposta",byte[].class,"Evento",Evento.class);
		this.DATI_PAGO_PA = new ComplexField(father,"datiPagoPA",java.lang.String.class,"Evento",Evento.class);
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"Evento",Evento.class);
		this.COD_APPLICAZIONE = new ComplexField(father,"codApplicazione",java.lang.String.class,"Evento",Evento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Evento",Evento.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"Evento",Evento.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Evento",Evento.class);
		this.ID_SESSIONE = new ComplexField(father,"idSessione",java.lang.String.class,"Evento",Evento.class);
	
	}
	
	

	public IField COMPONENTE = null;
	 
	public IField RUOLO = null;
	 
	public IField CATEGORIA_EVENTO = null;
	 
	public IField TIPO_EVENTO = null;
	 
	public IField SOTTOTIPO_EVENTO = null;
	 
	public IField DATA = null;
	 
	public IField INTERVALLO = null;
	 
	public IField ESITO = null;
	 
	public IField SOTTOTIPO_ESITO = null;
	 
	public IField DETTAGLIO_ESITO = null;
	 
	public IField PARAMETRI_RICHIESTA = null;
	 
	public IField PARAMETRI_RISPOSTA = null;
	 
	public IField DATI_PAGO_PA = null;
	 
	public IField COD_VERSAMENTO_ENTE = null;
	 
	public IField COD_APPLICAZIONE = null;
	 
	public IField IUV = null;
	 
	public IField CCP = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField ID_SESSIONE = null;
	 

	@Override
	public Class<Evento> getModeledClass(){
		return Evento.class;
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
