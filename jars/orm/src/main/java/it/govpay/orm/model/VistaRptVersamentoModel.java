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

import it.govpay.orm.VistaRptVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VistaRptVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRptVersamentoModel extends AbstractModel<VistaRptVersamento> {

	public VistaRptVersamentoModel(){
	
		super();
	
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new Field("idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.COD_CARRELLO = new Field("codCarrello",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.CCP = new Field("ccp",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_MSG_RICHIESTA = new Field("codMsgRichiesta",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_MSG_RICHIESTA = new Field("dataMsgRichiesta",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.STATO = new Field("stato",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_SESSIONE = new Field("codSessione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_SESSIONE_PORTALE = new Field("codSessionePortale",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.PSP_REDIRECT_URL = new Field("pspRedirectURL",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.XML_RPT = new Field("xmlRPT",byte[].class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_AGGIORNAMENTO_STATO = new Field("dataAggiornamentoStato",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.CALLBACK_URL = new Field("callbackURL",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.MODELLO_PAGAMENTO = new Field("modelloPagamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_MSG_RICEVUTA = new Field("codMsgRicevuta",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_MSG_RICEVUTA = new Field("dataMsgRicevuta",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_ESITO_PAGAMENTO = new Field("codEsitoPagamento",java.lang.Integer.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.IMPORTO_TOTALE_PAGATO = new Field("importoTotalePagato",java.lang.Double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.XML_RT = new Field("xmlRT",byte[].class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_CANALE = new Field("codCanale",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_INTERMEDIARIO_PSP = new Field("codIntermediarioPsp",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.TIPO_IDENTIFICATIVO_ATTESTANTE = new Field("tipoIdentificativoAttestante",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.IDENTIFICATIVO_ATTESTANTE = new Field("identificativoAttestante",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DENOMINAZIONE_ATTESTANTE = new Field("denominazioneAttestante",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_STAZIONE = new Field("codStazione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_TRANSAZIONE_RPT = new Field("codTransazioneRPT",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_TRANSAZIONE_RT = new Field("codTransazioneRT",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.STATO_CONSERVAZIONE = new Field("statoConservazione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DESCRIZIONE_STATO_CONS = new Field("descrizioneStatoCons",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_CONSERVAZIONE = new Field("dataConservazione",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.BLOCCANTE = new Field("bloccante",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID = new Field("vrsId",long.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_VERSAMENTO_ENTE = new Field("vrsCodVersamentoEnte",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_NOME = new Field("vrsNome",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new Field("vrsIdTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("vrsIdTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("vrsIdDominio",it.govpay.orm.IdDominio.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_UO = new it.govpay.orm.model.IdUoModel(new Field("vrsIdUo",it.govpay.orm.IdUo.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("vrsIdApplicazione",it.govpay.orm.IdApplicazione.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_IMPORTO_TOTALE = new Field("vrsImportoTotale",double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_STATO_VERSAMENTO = new Field("vrsStatoVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DESCRIZIONE_STATO = new Field("vrsDescrizioneStato",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_AGGIORNABILE = new Field("vrsAggiornabile",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_CREAZIONE = new Field("vrsDataCreazione",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_VALIDITA = new Field("vrsDataValidita",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_SCADENZA = new Field("vrsDataScadenza",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_ORA_ULTIMO_AGG = new Field("vrsDataOraUltimoAgg",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_CAUSALE_VERSAMENTO = new Field("vrsCausaleVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_TIPO = new Field("vrsDebitoreTipo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_IDENTIFICATIVO = new Field("vrsDebitoreIdentificativo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_ANAGRAFICA = new Field("vrsDebitoreAnagrafica",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_INDIRIZZO = new Field("vrsDebitoreIndirizzo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_CIVICO = new Field("vrsDebitoreCivico",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_CAP = new Field("vrsDebitoreCap",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_LOCALITA = new Field("vrsDebitoreLocalita",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_PROVINCIA = new Field("vrsDebitoreProvincia",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_NAZIONE = new Field("vrsDebitoreNazione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_EMAIL = new Field("vrsDebitoreEmail",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_TELEFONO = new Field("vrsDebitoreTelefono",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_CELLULARE = new Field("vrsDebitoreCellulare",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_FAX = new Field("vrsDebitoreFax",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_TASSONOMIA_AVVISO = new Field("vrsTassonomiaAvviso",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_TASSONOMIA = new Field("vrsTassonomia",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_LOTTO = new Field("vrsCodLotto",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_VERSAMENTO_LOTTO = new Field("vrsCodVersamentoLotto",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_ANNO_TRIBUTARIO = new Field("vrsCodAnnoTributario",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_BUNDLEKEY = new Field("vrsCodBundlekey",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATI_ALLEGATI = new Field("vrsDatiAllegati",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_INCASSO = new Field("vrsIncasso",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ANOMALIE = new Field("vrsAnomalie",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IUV_VERSAMENTO = new Field("vrsIuvVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_NUMERO_AVVISO = new Field("vrsNumeroAvviso",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ACK = new Field("vrsAck",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ANOMALO = new Field("vrsAnomalo",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DIVISIONE = new Field("vrsDivisione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DIREZIONE = new Field("vrsDirezione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID_SESSIONE = new Field("vrsIdSessione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_PAGAMENTO = new Field("vrsDataPagamento",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IMPORTO_PAGATO = new Field("vrsImportoPagato",java.lang.Double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IMPORTO_INCASSATO = new Field("vrsImportoIncassato",java.lang.Double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_STATO_PAGAMENTO = new Field("vrsStatoPagamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IUV_PAGAMENTO = new Field("vrsIuvPagamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_SRC_DEBITORE_IDENTIFICATIVO = new Field("vrsSrcDebitoreIdentificativo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_RATA = new Field("vrsCodRata",java.lang.Integer.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new Field("vrsIdDocumento",it.govpay.orm.IdDocumento.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_TIPO = new Field("vrsTipo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
	
	}
	
	public VistaRptVersamentoModel(IField father){
	
		super(father);
	
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new ComplexField(father,"idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.COD_CARRELLO = new ComplexField(father,"codCarrello",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_MSG_RICHIESTA = new ComplexField(father,"codMsgRichiesta",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_MSG_RICHIESTA = new ComplexField(father,"dataMsgRichiesta",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_SESSIONE = new ComplexField(father,"codSessione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_SESSIONE_PORTALE = new ComplexField(father,"codSessionePortale",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.PSP_REDIRECT_URL = new ComplexField(father,"pspRedirectURL",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.XML_RPT = new ComplexField(father,"xmlRPT",byte[].class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_AGGIORNAMENTO_STATO = new ComplexField(father,"dataAggiornamentoStato",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.CALLBACK_URL = new ComplexField(father,"callbackURL",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.MODELLO_PAGAMENTO = new ComplexField(father,"modelloPagamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_MSG_RICEVUTA = new ComplexField(father,"codMsgRicevuta",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_MSG_RICEVUTA = new ComplexField(father,"dataMsgRicevuta",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_ESITO_PAGAMENTO = new ComplexField(father,"codEsitoPagamento",java.lang.Integer.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.IMPORTO_TOTALE_PAGATO = new ComplexField(father,"importoTotalePagato",java.lang.Double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.XML_RT = new ComplexField(father,"xmlRT",byte[].class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_CANALE = new ComplexField(father,"codCanale",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_INTERMEDIARIO_PSP = new ComplexField(father,"codIntermediarioPsp",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.TIPO_IDENTIFICATIVO_ATTESTANTE = new ComplexField(father,"tipoIdentificativoAttestante",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.IDENTIFICATIVO_ATTESTANTE = new ComplexField(father,"identificativoAttestante",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DENOMINAZIONE_ATTESTANTE = new ComplexField(father,"denominazioneAttestante",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_STAZIONE = new ComplexField(father,"codStazione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_TRANSAZIONE_RPT = new ComplexField(father,"codTransazioneRPT",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.COD_TRANSAZIONE_RT = new ComplexField(father,"codTransazioneRT",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.STATO_CONSERVAZIONE = new ComplexField(father,"statoConservazione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DESCRIZIONE_STATO_CONS = new ComplexField(father,"descrizioneStatoCons",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.DATA_CONSERVAZIONE = new ComplexField(father,"dataConservazione",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.BLOCCANTE = new ComplexField(father,"bloccante",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID = new ComplexField(father,"vrsId",long.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_VERSAMENTO_ENTE = new ComplexField(father,"vrsCodVersamentoEnte",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_NOME = new ComplexField(father,"vrsNome",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new ComplexField(father,"vrsIdTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"vrsIdTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"vrsIdDominio",it.govpay.orm.IdDominio.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"vrsIdUo",it.govpay.orm.IdUo.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"vrsIdApplicazione",it.govpay.orm.IdApplicazione.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_IMPORTO_TOTALE = new ComplexField(father,"vrsImportoTotale",double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_STATO_VERSAMENTO = new ComplexField(father,"vrsStatoVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DESCRIZIONE_STATO = new ComplexField(father,"vrsDescrizioneStato",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_AGGIORNABILE = new ComplexField(father,"vrsAggiornabile",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_CREAZIONE = new ComplexField(father,"vrsDataCreazione",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_VALIDITA = new ComplexField(father,"vrsDataValidita",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_SCADENZA = new ComplexField(father,"vrsDataScadenza",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_ORA_ULTIMO_AGG = new ComplexField(father,"vrsDataOraUltimoAgg",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_CAUSALE_VERSAMENTO = new ComplexField(father,"vrsCausaleVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_TIPO = new ComplexField(father,"vrsDebitoreTipo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_IDENTIFICATIVO = new ComplexField(father,"vrsDebitoreIdentificativo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_ANAGRAFICA = new ComplexField(father,"vrsDebitoreAnagrafica",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_INDIRIZZO = new ComplexField(father,"vrsDebitoreIndirizzo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_CIVICO = new ComplexField(father,"vrsDebitoreCivico",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_CAP = new ComplexField(father,"vrsDebitoreCap",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_LOCALITA = new ComplexField(father,"vrsDebitoreLocalita",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_PROVINCIA = new ComplexField(father,"vrsDebitoreProvincia",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_NAZIONE = new ComplexField(father,"vrsDebitoreNazione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_EMAIL = new ComplexField(father,"vrsDebitoreEmail",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_TELEFONO = new ComplexField(father,"vrsDebitoreTelefono",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_CELLULARE = new ComplexField(father,"vrsDebitoreCellulare",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DEBITORE_FAX = new ComplexField(father,"vrsDebitoreFax",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_TASSONOMIA_AVVISO = new ComplexField(father,"vrsTassonomiaAvviso",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_TASSONOMIA = new ComplexField(father,"vrsTassonomia",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_LOTTO = new ComplexField(father,"vrsCodLotto",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_VERSAMENTO_LOTTO = new ComplexField(father,"vrsCodVersamentoLotto",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_ANNO_TRIBUTARIO = new ComplexField(father,"vrsCodAnnoTributario",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_BUNDLEKEY = new ComplexField(father,"vrsCodBundlekey",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATI_ALLEGATI = new ComplexField(father,"vrsDatiAllegati",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_INCASSO = new ComplexField(father,"vrsIncasso",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ANOMALIE = new ComplexField(father,"vrsAnomalie",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IUV_VERSAMENTO = new ComplexField(father,"vrsIuvVersamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_NUMERO_AVVISO = new ComplexField(father,"vrsNumeroAvviso",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ACK = new ComplexField(father,"vrsAck",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ANOMALO = new ComplexField(father,"vrsAnomalo",boolean.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DIVISIONE = new ComplexField(father,"vrsDivisione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DIREZIONE = new ComplexField(father,"vrsDirezione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID_SESSIONE = new ComplexField(father,"vrsIdSessione",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_DATA_PAGAMENTO = new ComplexField(father,"vrsDataPagamento",java.util.Date.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IMPORTO_PAGATO = new ComplexField(father,"vrsImportoPagato",java.lang.Double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IMPORTO_INCASSATO = new ComplexField(father,"vrsImportoIncassato",java.lang.Double.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_STATO_PAGAMENTO = new ComplexField(father,"vrsStatoPagamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_IUV_PAGAMENTO = new ComplexField(father,"vrsIuvPagamento",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_SRC_DEBITORE_IDENTIFICATIVO = new ComplexField(father,"vrsSrcDebitoreIdentificativo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_COD_RATA = new ComplexField(father,"vrsCodRata",java.lang.Integer.class,"VistaRptVersamento",VistaRptVersamento.class);
		this.VRS_ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new ComplexField(father,"vrsIdDocumento",it.govpay.orm.IdDocumento.class,"VistaRptVersamento",VistaRptVersamento.class));
		this.VRS_TIPO = new ComplexField(father,"vrsTipo",java.lang.String.class,"VistaRptVersamento",VistaRptVersamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdPagamentoPortaleModel ID_PAGAMENTO_PORTALE = null;
	 
	public IField COD_CARRELLO = null;
	 
	public IField IUV = null;
	 
	public IField CCP = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField COD_MSG_RICHIESTA = null;
	 
	public IField DATA_MSG_RICHIESTA = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField COD_SESSIONE = null;
	 
	public IField COD_SESSIONE_PORTALE = null;
	 
	public IField PSP_REDIRECT_URL = null;
	 
	public IField XML_RPT = null;
	 
	public IField DATA_AGGIORNAMENTO_STATO = null;
	 
	public IField CALLBACK_URL = null;
	 
	public IField MODELLO_PAGAMENTO = null;
	 
	public IField COD_MSG_RICEVUTA = null;
	 
	public IField DATA_MSG_RICEVUTA = null;
	 
	public IField COD_ESITO_PAGAMENTO = null;
	 
	public IField IMPORTO_TOTALE_PAGATO = null;
	 
	public IField XML_RT = null;
	 
	public IField COD_CANALE = null;
	 
	public IField COD_PSP = null;
	 
	public IField COD_INTERMEDIARIO_PSP = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 
	public IField TIPO_IDENTIFICATIVO_ATTESTANTE = null;
	 
	public IField IDENTIFICATIVO_ATTESTANTE = null;
	 
	public IField DENOMINAZIONE_ATTESTANTE = null;
	 
	public IField COD_STAZIONE = null;
	 
	public IField COD_TRANSAZIONE_RPT = null;
	 
	public IField COD_TRANSAZIONE_RT = null;
	 
	public IField STATO_CONSERVAZIONE = null;
	 
	public IField DESCRIZIONE_STATO_CONS = null;
	 
	public IField DATA_CONSERVAZIONE = null;
	 
	public IField BLOCCANTE = null;
	 
	public IField VRS_ID = null;
	 
	public IField VRS_COD_VERSAMENTO_ENTE = null;
	 
	public IField VRS_NOME = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoDominioModel VRS_ID_TIPO_VERSAMENTO_DOMINIO = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoModel VRS_ID_TIPO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdDominioModel VRS_ID_DOMINIO = null;
	 
	public it.govpay.orm.model.IdUoModel VRS_ID_UO = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel VRS_ID_APPLICAZIONE = null;
	 
	public IField VRS_IMPORTO_TOTALE = null;
	 
	public IField VRS_STATO_VERSAMENTO = null;
	 
	public IField VRS_DESCRIZIONE_STATO = null;
	 
	public IField VRS_AGGIORNABILE = null;
	 
	public IField VRS_DATA_CREAZIONE = null;
	 
	public IField VRS_DATA_VALIDITA = null;
	 
	public IField VRS_DATA_SCADENZA = null;
	 
	public IField VRS_DATA_ORA_ULTIMO_AGG = null;
	 
	public IField VRS_CAUSALE_VERSAMENTO = null;
	 
	public IField VRS_DEBITORE_TIPO = null;
	 
	public IField VRS_DEBITORE_IDENTIFICATIVO = null;
	 
	public IField VRS_DEBITORE_ANAGRAFICA = null;
	 
	public IField VRS_DEBITORE_INDIRIZZO = null;
	 
	public IField VRS_DEBITORE_CIVICO = null;
	 
	public IField VRS_DEBITORE_CAP = null;
	 
	public IField VRS_DEBITORE_LOCALITA = null;
	 
	public IField VRS_DEBITORE_PROVINCIA = null;
	 
	public IField VRS_DEBITORE_NAZIONE = null;
	 
	public IField VRS_DEBITORE_EMAIL = null;
	 
	public IField VRS_DEBITORE_TELEFONO = null;
	 
	public IField VRS_DEBITORE_CELLULARE = null;
	 
	public IField VRS_DEBITORE_FAX = null;
	 
	public IField VRS_TASSONOMIA_AVVISO = null;
	 
	public IField VRS_TASSONOMIA = null;
	 
	public IField VRS_COD_LOTTO = null;
	 
	public IField VRS_COD_VERSAMENTO_LOTTO = null;
	 
	public IField VRS_COD_ANNO_TRIBUTARIO = null;
	 
	public IField VRS_COD_BUNDLEKEY = null;
	 
	public IField VRS_DATI_ALLEGATI = null;
	 
	public IField VRS_INCASSO = null;
	 
	public IField VRS_ANOMALIE = null;
	 
	public IField VRS_IUV_VERSAMENTO = null;
	 
	public IField VRS_NUMERO_AVVISO = null;
	 
	public IField VRS_ACK = null;
	 
	public IField VRS_ANOMALO = null;
	 
	public IField VRS_DIVISIONE = null;
	 
	public IField VRS_DIREZIONE = null;
	 
	public IField VRS_ID_SESSIONE = null;
	 
	public IField VRS_DATA_PAGAMENTO = null;
	 
	public IField VRS_IMPORTO_PAGATO = null;
	 
	public IField VRS_IMPORTO_INCASSATO = null;
	 
	public IField VRS_STATO_PAGAMENTO = null;
	 
	public IField VRS_IUV_PAGAMENTO = null;
	 
	public IField VRS_SRC_DEBITORE_IDENTIFICATIVO = null;
	 
	public IField VRS_COD_RATA = null;
	 
	public it.govpay.orm.model.IdDocumentoModel VRS_ID_DOCUMENTO = null;
	 
	public IField VRS_TIPO = null;
	 

	@Override
	public Class<VistaRptVersamento> getModeledClass(){
		return VistaRptVersamento.class;
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
