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

import it.govpay.orm.VistaRendicontazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VistaRendicontazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRendicontazioneModel extends AbstractModel<VistaRendicontazione> {

	public VistaRendicontazioneModel(){
	
		super();
	
		this.FR_COD_PSP = new Field("frCodPsp",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_COD_DOMINIO = new Field("frCodDominio",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_COD_FLUSSO = new Field("frCodFlusso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_STATO = new Field("frStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DESCRIZIONE_STATO = new Field("frDescrizioneStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_IUR = new Field("frIur",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DATA_ORA_FLUSSO = new Field("frDataOraFlusso",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DATA_REGOLAMENTO = new Field("frDataRegolamento",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DATA_ACQUISIZIONE = new Field("frDataAcquisizione",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_NUMERO_PAGAMENTI = new Field("frNumeroPagamenti",long.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_IMPORTO_TOTALE_PAGAMENTI = new Field("frImportoTotalePagamenti",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_COD_BIC_RIVERSAMENTO = new Field("frCodBicRiversamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_ID = new Field("frId",long.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_ID_INCASSO = new it.govpay.orm.model.IdIncassoModel(new Field("frIdIncasso",it.govpay.orm.IdIncasso.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.FR_RAGIONE_SOCIALE_PSP = new Field("frRagioneSocialePsp",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_RAGIONE_SOCIALE_DOMINIO = new Field("frRagioneSocialeDominio",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_IUV = new Field("rndIuv",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_IUR = new Field("rndIur",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_INDICE_DATI = new Field("rndIndiceDati",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_IMPORTO_PAGATO = new Field("rndImportoPagato",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_ESITO = new Field("rndEsito",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_DATA = new Field("rndData",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_STATO = new Field("rndStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_ANOMALIE = new Field("rndAnomalie",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_ID_PAGAMENTO = new it.govpay.orm.model.IdPagamentoModel(new Field("rndIdPagamento",it.govpay.orm.IdPagamento.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.SNG_ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new Field("sngIdTributo",it.govpay.orm.IdTributo.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.SNG_COD_SING_VERS_ENTE = new Field("sngCodSingVersEnte",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_STATO_SINGOLO_VERSAMENTO = new Field("sngStatoSingoloVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_IMPORTO_SINGOLO_VERSAMENTO = new Field("sngImportoSingoloVersamento",double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_DESCRIZIONE = new Field("sngDescrizione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_DATI_ALLEGATI = new Field("sngDatiAllegati",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_INDICE_DATI = new Field("sngIndiceDati",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_DESCRIZIONE_CAUSALE_RPT = new Field("sngDescrizioneCausaleRPT",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID = new Field("vrsId",long.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_VERSAMENTO_ENTE = new Field("vrsCodVersamentoEnte",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_NOME = new Field("vrsNome",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new Field("vrsIdTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("vrsIdTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("vrsIdDominio",it.govpay.orm.IdDominio.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_UO = new it.govpay.orm.model.IdUoModel(new Field("vrsIdUo",it.govpay.orm.IdUo.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("vrsIdApplicazione",it.govpay.orm.IdApplicazione.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_IMPORTO_TOTALE = new Field("vrsImportoTotale",double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_STATO_VERSAMENTO = new Field("vrsStatoVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DESCRIZIONE_STATO = new Field("vrsDescrizioneStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_AGGIORNABILE = new Field("vrsAggiornabile",boolean.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_CREAZIONE = new Field("vrsDataCreazione",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_VALIDITA = new Field("vrsDataValidita",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_SCADENZA = new Field("vrsDataScadenza",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_ORA_ULTIMO_AGG = new Field("vrsDataOraUltimoAgg",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_CAUSALE_VERSAMENTO = new Field("vrsCausaleVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_TIPO = new Field("vrsDebitoreTipo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_IDENTIFICATIVO = new Field("vrsDebitoreIdentificativo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_ANAGRAFICA = new Field("vrsDebitoreAnagrafica",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_INDIRIZZO = new Field("vrsDebitoreIndirizzo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_CIVICO = new Field("vrsDebitoreCivico",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_CAP = new Field("vrsDebitoreCap",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_LOCALITA = new Field("vrsDebitoreLocalita",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_PROVINCIA = new Field("vrsDebitoreProvincia",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_NAZIONE = new Field("vrsDebitoreNazione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_EMAIL = new Field("vrsDebitoreEmail",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_TELEFONO = new Field("vrsDebitoreTelefono",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_CELLULARE = new Field("vrsDebitoreCellulare",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_FAX = new Field("vrsDebitoreFax",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_TASSONOMIA_AVVISO = new Field("vrsTassonomiaAvviso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_TASSONOMIA = new Field("vrsTassonomia",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_LOTTO = new Field("vrsCodLotto",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_VERSAMENTO_LOTTO = new Field("vrsCodVersamentoLotto",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_ANNO_TRIBUTARIO = new Field("vrsCodAnnoTributario",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_BUNDLEKEY = new Field("vrsCodBundlekey",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATI_ALLEGATI = new Field("vrsDatiAllegati",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_INCASSO = new Field("vrsIncasso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ANOMALIE = new Field("vrsAnomalie",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IUV_VERSAMENTO = new Field("vrsIuvVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_NUMERO_AVVISO = new Field("vrsNumeroAvviso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ACK = new Field("vrsAck",boolean.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ANOMALO = new Field("vrsAnomalo",boolean.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DIVISIONE = new Field("vrsDivisione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DIREZIONE = new Field("vrsDirezione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID_SESSIONE = new Field("vrsIdSessione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_PAGAMENTO = new Field("vrsDataPagamento",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IMPORTO_PAGATO = new Field("vrsImportoPagato",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IMPORTO_INCASSATO = new Field("vrsImportoIncassato",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_STATO_PAGAMENTO = new Field("vrsStatoPagamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IUV_PAGAMENTO = new Field("vrsIuvPagamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_RATA = new Field("vrsCodRata",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new Field("vrsIdDocumento",it.govpay.orm.IdDocumento.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_TIPO = new Field("vrsTipo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
	
	}
	
	public VistaRendicontazioneModel(IField father){
	
		super(father);
	
		this.FR_COD_PSP = new ComplexField(father,"frCodPsp",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_COD_DOMINIO = new ComplexField(father,"frCodDominio",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_COD_FLUSSO = new ComplexField(father,"frCodFlusso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_STATO = new ComplexField(father,"frStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DESCRIZIONE_STATO = new ComplexField(father,"frDescrizioneStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_IUR = new ComplexField(father,"frIur",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DATA_ORA_FLUSSO = new ComplexField(father,"frDataOraFlusso",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DATA_REGOLAMENTO = new ComplexField(father,"frDataRegolamento",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_DATA_ACQUISIZIONE = new ComplexField(father,"frDataAcquisizione",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_NUMERO_PAGAMENTI = new ComplexField(father,"frNumeroPagamenti",long.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_IMPORTO_TOTALE_PAGAMENTI = new ComplexField(father,"frImportoTotalePagamenti",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_COD_BIC_RIVERSAMENTO = new ComplexField(father,"frCodBicRiversamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_ID = new ComplexField(father,"frId",long.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_ID_INCASSO = new it.govpay.orm.model.IdIncassoModel(new ComplexField(father,"frIdIncasso",it.govpay.orm.IdIncasso.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.FR_RAGIONE_SOCIALE_PSP = new ComplexField(father,"frRagioneSocialePsp",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.FR_RAGIONE_SOCIALE_DOMINIO = new ComplexField(father,"frRagioneSocialeDominio",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_IUV = new ComplexField(father,"rndIuv",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_IUR = new ComplexField(father,"rndIur",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_INDICE_DATI = new ComplexField(father,"rndIndiceDati",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_IMPORTO_PAGATO = new ComplexField(father,"rndImportoPagato",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_ESITO = new ComplexField(father,"rndEsito",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_DATA = new ComplexField(father,"rndData",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_STATO = new ComplexField(father,"rndStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_ANOMALIE = new ComplexField(father,"rndAnomalie",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.RND_ID_PAGAMENTO = new it.govpay.orm.model.IdPagamentoModel(new ComplexField(father,"rndIdPagamento",it.govpay.orm.IdPagamento.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.SNG_ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new ComplexField(father,"sngIdTributo",it.govpay.orm.IdTributo.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.SNG_COD_SING_VERS_ENTE = new ComplexField(father,"sngCodSingVersEnte",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_STATO_SINGOLO_VERSAMENTO = new ComplexField(father,"sngStatoSingoloVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_IMPORTO_SINGOLO_VERSAMENTO = new ComplexField(father,"sngImportoSingoloVersamento",double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_DESCRIZIONE = new ComplexField(father,"sngDescrizione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_DATI_ALLEGATI = new ComplexField(father,"sngDatiAllegati",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_INDICE_DATI = new ComplexField(father,"sngIndiceDati",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.SNG_DESCRIZIONE_CAUSALE_RPT = new ComplexField(father,"sngDescrizioneCausaleRPT",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID = new ComplexField(father,"vrsId",long.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_VERSAMENTO_ENTE = new ComplexField(father,"vrsCodVersamentoEnte",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_NOME = new ComplexField(father,"vrsNome",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new ComplexField(father,"vrsIdTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"vrsIdTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"vrsIdDominio",it.govpay.orm.IdDominio.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"vrsIdUo",it.govpay.orm.IdUo.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"vrsIdApplicazione",it.govpay.orm.IdApplicazione.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_IMPORTO_TOTALE = new ComplexField(father,"vrsImportoTotale",double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_STATO_VERSAMENTO = new ComplexField(father,"vrsStatoVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DESCRIZIONE_STATO = new ComplexField(father,"vrsDescrizioneStato",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_AGGIORNABILE = new ComplexField(father,"vrsAggiornabile",boolean.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_CREAZIONE = new ComplexField(father,"vrsDataCreazione",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_VALIDITA = new ComplexField(father,"vrsDataValidita",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_SCADENZA = new ComplexField(father,"vrsDataScadenza",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_ORA_ULTIMO_AGG = new ComplexField(father,"vrsDataOraUltimoAgg",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_CAUSALE_VERSAMENTO = new ComplexField(father,"vrsCausaleVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_TIPO = new ComplexField(father,"vrsDebitoreTipo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_IDENTIFICATIVO = new ComplexField(father,"vrsDebitoreIdentificativo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_ANAGRAFICA = new ComplexField(father,"vrsDebitoreAnagrafica",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_INDIRIZZO = new ComplexField(father,"vrsDebitoreIndirizzo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_CIVICO = new ComplexField(father,"vrsDebitoreCivico",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_CAP = new ComplexField(father,"vrsDebitoreCap",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_LOCALITA = new ComplexField(father,"vrsDebitoreLocalita",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_PROVINCIA = new ComplexField(father,"vrsDebitoreProvincia",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_NAZIONE = new ComplexField(father,"vrsDebitoreNazione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_EMAIL = new ComplexField(father,"vrsDebitoreEmail",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_TELEFONO = new ComplexField(father,"vrsDebitoreTelefono",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_CELLULARE = new ComplexField(father,"vrsDebitoreCellulare",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DEBITORE_FAX = new ComplexField(father,"vrsDebitoreFax",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_TASSONOMIA_AVVISO = new ComplexField(father,"vrsTassonomiaAvviso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_TASSONOMIA = new ComplexField(father,"vrsTassonomia",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_LOTTO = new ComplexField(father,"vrsCodLotto",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_VERSAMENTO_LOTTO = new ComplexField(father,"vrsCodVersamentoLotto",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_ANNO_TRIBUTARIO = new ComplexField(father,"vrsCodAnnoTributario",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_BUNDLEKEY = new ComplexField(father,"vrsCodBundlekey",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATI_ALLEGATI = new ComplexField(father,"vrsDatiAllegati",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_INCASSO = new ComplexField(father,"vrsIncasso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ANOMALIE = new ComplexField(father,"vrsAnomalie",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IUV_VERSAMENTO = new ComplexField(father,"vrsIuvVersamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_NUMERO_AVVISO = new ComplexField(father,"vrsNumeroAvviso",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ACK = new ComplexField(father,"vrsAck",boolean.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ANOMALO = new ComplexField(father,"vrsAnomalo",boolean.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DIVISIONE = new ComplexField(father,"vrsDivisione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DIREZIONE = new ComplexField(father,"vrsDirezione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID_SESSIONE = new ComplexField(father,"vrsIdSessione",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_DATA_PAGAMENTO = new ComplexField(father,"vrsDataPagamento",java.util.Date.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IMPORTO_PAGATO = new ComplexField(father,"vrsImportoPagato",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IMPORTO_INCASSATO = new ComplexField(father,"vrsImportoIncassato",java.lang.Double.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_STATO_PAGAMENTO = new ComplexField(father,"vrsStatoPagamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_IUV_PAGAMENTO = new ComplexField(father,"vrsIuvPagamento",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_COD_RATA = new ComplexField(father,"vrsCodRata",java.lang.Integer.class,"VistaRendicontazione",VistaRendicontazione.class);
		this.VRS_ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new ComplexField(father,"vrsIdDocumento",it.govpay.orm.IdDocumento.class,"VistaRendicontazione",VistaRendicontazione.class));
		this.VRS_TIPO = new ComplexField(father,"vrsTipo",java.lang.String.class,"VistaRendicontazione",VistaRendicontazione.class);
	
	}
	
	

	public IField FR_COD_PSP = null;
	 
	public IField FR_COD_DOMINIO = null;
	 
	public IField FR_COD_FLUSSO = null;
	 
	public IField FR_STATO = null;
	 
	public IField FR_DESCRIZIONE_STATO = null;
	 
	public IField FR_IUR = null;
	 
	public IField FR_DATA_ORA_FLUSSO = null;
	 
	public IField FR_DATA_REGOLAMENTO = null;
	 
	public IField FR_DATA_ACQUISIZIONE = null;
	 
	public IField FR_NUMERO_PAGAMENTI = null;
	 
	public IField FR_IMPORTO_TOTALE_PAGAMENTI = null;
	 
	public IField FR_COD_BIC_RIVERSAMENTO = null;
	 
	public IField FR_ID = null;
	 
	public it.govpay.orm.model.IdIncassoModel FR_ID_INCASSO = null;
	 
	public IField FR_RAGIONE_SOCIALE_PSP = null;
	 
	public IField FR_RAGIONE_SOCIALE_DOMINIO = null;
	 
	public IField RND_IUV = null;
	 
	public IField RND_IUR = null;
	 
	public IField RND_INDICE_DATI = null;
	 
	public IField RND_IMPORTO_PAGATO = null;
	 
	public IField RND_ESITO = null;
	 
	public IField RND_DATA = null;
	 
	public IField RND_STATO = null;
	 
	public IField RND_ANOMALIE = null;
	 
	public it.govpay.orm.model.IdPagamentoModel RND_ID_PAGAMENTO = null;
	 
	public it.govpay.orm.model.IdTributoModel SNG_ID_TRIBUTO = null;
	 
	public IField SNG_COD_SING_VERS_ENTE = null;
	 
	public IField SNG_STATO_SINGOLO_VERSAMENTO = null;
	 
	public IField SNG_IMPORTO_SINGOLO_VERSAMENTO = null;
	 
	public IField SNG_DESCRIZIONE = null;
	 
	public IField SNG_DATI_ALLEGATI = null;
	 
	public IField SNG_INDICE_DATI = null;
	 
	public IField SNG_DESCRIZIONE_CAUSALE_RPT = null;
	 
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
	 
	public IField VRS_COD_RATA = null;
	 
	public it.govpay.orm.model.IdDocumentoModel VRS_ID_DOCUMENTO = null;
	 
	public IField VRS_TIPO = null;
	 

	@Override
	public Class<VistaRendicontazione> getModeledClass(){
		return VistaRendicontazione.class;
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
