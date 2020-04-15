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

import it.govpay.orm.NotificaAppIO;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model NotificaAppIO 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class NotificaAppIOModel extends AbstractModel<NotificaAppIO> {

	public NotificaAppIOModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"NotificaAppIO",NotificaAppIO.class));
		this.ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new Field("idTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"NotificaAppIO",NotificaAppIO.class));
		this.DEBITORE_IDENTIFICATIVO = new Field("debitoreIdentificativo",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.COD_APPLICAZIONE = new Field("codApplicazione",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.IUV = new Field("iuv",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.TIPO_ESITO = new Field("tipoEsito",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"NotificaAppIO",NotificaAppIO.class);
		this.STATO = new Field("stato",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.DATA_AGGIORNAMENTO_STATO = new Field("dataAggiornamentoStato",java.util.Date.class,"NotificaAppIO",NotificaAppIO.class);
		this.DATA_PROSSIMA_SPEDIZIONE = new Field("dataProssimaSpedizione",java.util.Date.class,"NotificaAppIO",NotificaAppIO.class);
		this.TENTATIVI_SPEDIZIONE = new Field("tentativiSpedizione",java.lang.Long.class,"NotificaAppIO",NotificaAppIO.class);
		this.ID_MESSAGGIO = new Field("idMessaggio",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.STATO_MESSAGGIO = new Field("statoMessaggio",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
	
	}
	
	public NotificaAppIOModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"NotificaAppIO",NotificaAppIO.class));
		this.ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new ComplexField(father,"idTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"NotificaAppIO",NotificaAppIO.class));
		this.DEBITORE_IDENTIFICATIVO = new ComplexField(father,"debitoreIdentificativo",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.COD_APPLICAZIONE = new ComplexField(father,"codApplicazione",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.TIPO_ESITO = new ComplexField(father,"tipoEsito",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"NotificaAppIO",NotificaAppIO.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.DATA_AGGIORNAMENTO_STATO = new ComplexField(father,"dataAggiornamentoStato",java.util.Date.class,"NotificaAppIO",NotificaAppIO.class);
		this.DATA_PROSSIMA_SPEDIZIONE = new ComplexField(father,"dataProssimaSpedizione",java.util.Date.class,"NotificaAppIO",NotificaAppIO.class);
		this.TENTATIVI_SPEDIZIONE = new ComplexField(father,"tentativiSpedizione",java.lang.Long.class,"NotificaAppIO",NotificaAppIO.class);
		this.ID_MESSAGGIO = new ComplexField(father,"idMessaggio",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
		this.STATO_MESSAGGIO = new ComplexField(father,"statoMessaggio",java.lang.String.class,"NotificaAppIO",NotificaAppIO.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoDominioModel ID_TIPO_VERSAMENTO_DOMINIO = null;
	 
	public IField DEBITORE_IDENTIFICATIVO = null;
	 
	public IField COD_VERSAMENTO_ENTE = null;
	 
	public IField COD_APPLICAZIONE = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField TIPO_ESITO = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField DATA_AGGIORNAMENTO_STATO = null;
	 
	public IField DATA_PROSSIMA_SPEDIZIONE = null;
	 
	public IField TENTATIVI_SPEDIZIONE = null;
	 
	public IField ID_MESSAGGIO = null;
	 
	public IField STATO_MESSAGGIO = null;
	 

	@Override
	public Class<NotificaAppIO> getModeledClass(){
		return NotificaAppIO.class;
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