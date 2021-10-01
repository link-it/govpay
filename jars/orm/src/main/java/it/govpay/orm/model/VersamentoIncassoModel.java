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

import it.govpay.orm.VersamentoIncasso;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VersamentoIncasso 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoIncassoModel extends AbstractModel<VersamentoIncasso> {

	public VersamentoIncassoModel(){
	
		super();
	
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.NOME = new Field("nome",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new Field("idTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new Field("idUo",it.govpay.orm.IdUo.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new Field("idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.IUV = new it.govpay.orm.model.IuvSearchModel(new Field("iuv",it.govpay.orm.IuvSearch.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.IMPORTO_TOTALE = new Field("importoTotale",double.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.STATO_VERSAMENTO = new Field("statoVersamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.AGGIORNABILE = new Field("aggiornabile",boolean.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_VALIDITA = new Field("dataValidita",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_SCADENZA = new Field("dataScadenza",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_ORA_ULTIMO_AGGIORNAMENTO = new Field("dataOraUltimoAggiornamento",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.CAUSALE_VERSAMENTO = new Field("causaleVersamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_TIPO = new Field("debitoreTipo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_IDENTIFICATIVO = new Field("debitoreIdentificativo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_ANAGRAFICA = new Field("debitoreAnagrafica",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_INDIRIZZO = new Field("debitoreIndirizzo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_CIVICO = new Field("debitoreCivico",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_CAP = new Field("debitoreCap",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_LOCALITA = new Field("debitoreLocalita",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_PROVINCIA = new Field("debitoreProvincia",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_NAZIONE = new Field("debitoreNazione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_EMAIL = new Field("debitoreEmail",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_TELEFONO = new Field("debitoreTelefono",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_CELLULARE = new Field("debitoreCellulare",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_FAX = new Field("debitoreFax",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.TASSONOMIA_AVVISO = new Field("tassonomiaAvviso",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.TASSONOMIA = new Field("tassonomia",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_LOTTO = new Field("codLotto",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_VERSAMENTO_LOTTO = new Field("codVersamentoLotto",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_ANNO_TRIBUTARIO = new Field("codAnnoTributario",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_BUNDLEKEY = new Field("codBundlekey",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATI_ALLEGATI = new Field("datiAllegati",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.INCASSO = new Field("incasso",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ANOMALIE = new Field("anomalie",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IUV_VERSAMENTO = new Field("iuvVersamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.NUMERO_AVVISO = new Field("numeroAvviso",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ACK = new Field("ack",boolean.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ANOMALO = new Field("anomalo",boolean.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_PAGAMENTO = new Field("dataPagamento",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",java.lang.Double.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IMPORTO_INCASSATO = new Field("importoIncassato",java.lang.Double.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.STATO_PAGAMENTO = new Field("statoPagamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IUV_PAGAMENTO = new Field("iuvPagamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DIVISIONE = new Field("divisione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DIREZIONE = new Field("direzione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SMART_ORDER_DATE = new Field("smartOrderDate",long.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SMART_ORDER_RANK = new Field("smartOrderRank",java.lang.Integer.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ID_SESSIONE = new Field("idSessione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SRC_IUV = new Field("srcIuv",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SRC_DEBITORE_IDENTIFICATIVO = new Field("srcDebitoreIdentificativo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_RATA = new Field("codRata",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_DOCUMENTO = new Field("codDocumento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DOC_DESCRIZIONE = new Field("docDescrizione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.PROPRIETA = new Field("proprieta",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
	
	}
	
	public VersamentoIncassoModel(IField father){
	
		super(father);
	
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.NOME = new ComplexField(father,"nome",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new ComplexField(father,"idTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"idUo",it.govpay.orm.IdUo.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new ComplexField(father,"idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.IUV = new it.govpay.orm.model.IuvSearchModel(new ComplexField(father,"iuv",it.govpay.orm.IuvSearch.class,"VersamentoIncasso",VersamentoIncasso.class));
		this.IMPORTO_TOTALE = new ComplexField(father,"importoTotale",double.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.STATO_VERSAMENTO = new ComplexField(father,"statoVersamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.AGGIORNABILE = new ComplexField(father,"aggiornabile",boolean.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_VALIDITA = new ComplexField(father,"dataValidita",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_SCADENZA = new ComplexField(father,"dataScadenza",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_ORA_ULTIMO_AGGIORNAMENTO = new ComplexField(father,"dataOraUltimoAggiornamento",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.CAUSALE_VERSAMENTO = new ComplexField(father,"causaleVersamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_TIPO = new ComplexField(father,"debitoreTipo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_IDENTIFICATIVO = new ComplexField(father,"debitoreIdentificativo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_ANAGRAFICA = new ComplexField(father,"debitoreAnagrafica",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_INDIRIZZO = new ComplexField(father,"debitoreIndirizzo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_CIVICO = new ComplexField(father,"debitoreCivico",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_CAP = new ComplexField(father,"debitoreCap",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_LOCALITA = new ComplexField(father,"debitoreLocalita",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_PROVINCIA = new ComplexField(father,"debitoreProvincia",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_NAZIONE = new ComplexField(father,"debitoreNazione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_EMAIL = new ComplexField(father,"debitoreEmail",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_TELEFONO = new ComplexField(father,"debitoreTelefono",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_CELLULARE = new ComplexField(father,"debitoreCellulare",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DEBITORE_FAX = new ComplexField(father,"debitoreFax",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.TASSONOMIA_AVVISO = new ComplexField(father,"tassonomiaAvviso",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.TASSONOMIA = new ComplexField(father,"tassonomia",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_LOTTO = new ComplexField(father,"codLotto",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_VERSAMENTO_LOTTO = new ComplexField(father,"codVersamentoLotto",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_ANNO_TRIBUTARIO = new ComplexField(father,"codAnnoTributario",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_BUNDLEKEY = new ComplexField(father,"codBundlekey",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATI_ALLEGATI = new ComplexField(father,"datiAllegati",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.INCASSO = new ComplexField(father,"incasso",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ANOMALIE = new ComplexField(father,"anomalie",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IUV_VERSAMENTO = new ComplexField(father,"iuvVersamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.NUMERO_AVVISO = new ComplexField(father,"numeroAvviso",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ACK = new ComplexField(father,"ack",boolean.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ANOMALO = new ComplexField(father,"anomalo",boolean.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DATA_PAGAMENTO = new ComplexField(father,"dataPagamento",java.util.Date.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",java.lang.Double.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IMPORTO_INCASSATO = new ComplexField(father,"importoIncassato",java.lang.Double.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.STATO_PAGAMENTO = new ComplexField(father,"statoPagamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.IUV_PAGAMENTO = new ComplexField(father,"iuvPagamento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DIVISIONE = new ComplexField(father,"divisione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DIREZIONE = new ComplexField(father,"direzione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SMART_ORDER_DATE = new ComplexField(father,"smartOrderDate",long.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SMART_ORDER_RANK = new ComplexField(father,"smartOrderRank",java.lang.Integer.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.ID_SESSIONE = new ComplexField(father,"idSessione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SRC_IUV = new ComplexField(father,"srcIuv",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.SRC_DEBITORE_IDENTIFICATIVO = new ComplexField(father,"srcDebitoreIdentificativo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_RATA = new ComplexField(father,"codRata",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.COD_DOCUMENTO = new ComplexField(father,"codDocumento",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.DOC_DESCRIZIONE = new ComplexField(father,"docDescrizione",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
		this.PROPRIETA = new ComplexField(father,"proprieta",java.lang.String.class,"VersamentoIncasso",VersamentoIncasso.class);
	
	}
	
	

	public IField COD_VERSAMENTO_ENTE = null;
	 
	public IField NOME = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoDominioModel ID_TIPO_VERSAMENTO_DOMINIO = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoModel ID_TIPO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public it.govpay.orm.model.IdUoModel ID_UO = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdPagamentoPortaleModel ID_PAGAMENTO_PORTALE = null;
	 
	public it.govpay.orm.model.IuvSearchModel IUV = null;
	 
	public IField IMPORTO_TOTALE = null;
	 
	public IField STATO_VERSAMENTO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField AGGIORNABILE = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField DATA_VALIDITA = null;
	 
	public IField DATA_SCADENZA = null;
	 
	public IField DATA_ORA_ULTIMO_AGGIORNAMENTO = null;
	 
	public IField CAUSALE_VERSAMENTO = null;
	 
	public IField DEBITORE_TIPO = null;
	 
	public IField DEBITORE_IDENTIFICATIVO = null;
	 
	public IField DEBITORE_ANAGRAFICA = null;
	 
	public IField DEBITORE_INDIRIZZO = null;
	 
	public IField DEBITORE_CIVICO = null;
	 
	public IField DEBITORE_CAP = null;
	 
	public IField DEBITORE_LOCALITA = null;
	 
	public IField DEBITORE_PROVINCIA = null;
	 
	public IField DEBITORE_NAZIONE = null;
	 
	public IField DEBITORE_EMAIL = null;
	 
	public IField DEBITORE_TELEFONO = null;
	 
	public IField DEBITORE_CELLULARE = null;
	 
	public IField DEBITORE_FAX = null;
	 
	public IField TASSONOMIA_AVVISO = null;
	 
	public IField TASSONOMIA = null;
	 
	public IField COD_LOTTO = null;
	 
	public IField COD_VERSAMENTO_LOTTO = null;
	 
	public IField COD_ANNO_TRIBUTARIO = null;
	 
	public IField COD_BUNDLEKEY = null;
	 
	public IField DATI_ALLEGATI = null;
	 
	public IField INCASSO = null;
	 
	public IField ANOMALIE = null;
	 
	public IField IUV_VERSAMENTO = null;
	 
	public IField NUMERO_AVVISO = null;
	 
	public IField ACK = null;
	 
	public IField ANOMALO = null;
	 
	public IField DATA_PAGAMENTO = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public IField IMPORTO_INCASSATO = null;
	 
	public IField STATO_PAGAMENTO = null;
	 
	public IField IUV_PAGAMENTO = null;
	 
	public IField DIVISIONE = null;
	 
	public IField DIREZIONE = null;
	 
	public IField SMART_ORDER_DATE = null;
	 
	public IField SMART_ORDER_RANK = null;
	 
	public IField ID_SESSIONE = null;
	 
	public IField SRC_IUV = null;
	 
	public IField SRC_DEBITORE_IDENTIFICATIVO = null;
	 
	public IField COD_RATA = null;
	 
	public IField COD_DOCUMENTO = null;
	 
	public IField TIPO = null;
	 
	public IField DOC_DESCRIZIONE = null;
	 
	public IField PROPRIETA = null;
	 

	@Override
	public Class<VersamentoIncasso> getModeledClass(){
		return VersamentoIncasso.class;
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
