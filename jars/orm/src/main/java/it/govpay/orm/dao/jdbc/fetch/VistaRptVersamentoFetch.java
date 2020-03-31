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
package it.govpay.orm.dao.jdbc.fetch;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;

import it.govpay.orm.VistaRptVersamento;


/**     
 * VistaRptVersamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRptVersamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaRptVersamento.model())){
				VistaRptVersamento object = new VistaRptVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodCarrello", VistaRptVersamento.model().COD_CARRELLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_carrello", VistaRptVersamento.model().COD_CARRELLO.getFieldType()));
				setParameter(object, "setIuv", VistaRptVersamento.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", VistaRptVersamento.model().IUV.getFieldType()));
				setParameter(object, "setCcp", VistaRptVersamento.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", VistaRptVersamento.model().CCP.getFieldType()));
				setParameter(object, "setCodDominio", VistaRptVersamento.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", VistaRptVersamento.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setCodMsgRichiesta", VistaRptVersamento.model().COD_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_richiesta", VistaRptVersamento.model().COD_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setDataMsgRichiesta", VistaRptVersamento.model().DATA_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_richiesta", VistaRptVersamento.model().DATA_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setStato", VistaRptVersamento.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", VistaRptVersamento.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", VistaRptVersamento.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", VistaRptVersamento.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setCodSessione", VistaRptVersamento.model().COD_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione", VistaRptVersamento.model().COD_SESSIONE.getFieldType()));
				setParameter(object, "setCodSessionePortale", VistaRptVersamento.model().COD_SESSIONE_PORTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione_portale", VistaRptVersamento.model().COD_SESSIONE_PORTALE.getFieldType()));
				setParameter(object, "setPspRedirectURL", VistaRptVersamento.model().PSP_REDIRECT_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_redirect_url", VistaRptVersamento.model().PSP_REDIRECT_URL.getFieldType()));
				setParameter(object, "setXmlRPT", VistaRptVersamento.model().XML_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rpt", VistaRptVersamento.model().XML_RPT.getFieldType()));
				setParameter(object, "setDataAggiornamentoStato", VistaRptVersamento.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_aggiornamento_stato", VistaRptVersamento.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
				setParameter(object, "setCallbackURL", VistaRptVersamento.model().CALLBACK_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "callback_url", VistaRptVersamento.model().CALLBACK_URL.getFieldType()));
				setParameter(object, "setModelloPagamento", VistaRptVersamento.model().MODELLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "modello_pagamento", VistaRptVersamento.model().MODELLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setCodMsgRicevuta", VistaRptVersamento.model().COD_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_ricevuta", VistaRptVersamento.model().COD_MSG_RICEVUTA.getFieldType()));
				setParameter(object, "setDataMsgRicevuta", VistaRptVersamento.model().DATA_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_ricevuta", VistaRptVersamento.model().DATA_MSG_RICEVUTA.getFieldType()));
				setParameter(object, "setCodEsitoPagamento", VistaRptVersamento.model().COD_ESITO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_esito_pagamento", VistaRptVersamento.model().COD_ESITO_PAGAMENTO.getFieldType()));
				setParameter(object, "setImportoTotalePagato", VistaRptVersamento.model().IMPORTO_TOTALE_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagato", VistaRptVersamento.model().IMPORTO_TOTALE_PAGATO.getFieldType()));
				setParameter(object, "setXmlRT", VistaRptVersamento.model().XML_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rt", VistaRptVersamento.model().XML_RT.getFieldType()));
				setParameter(object, "setCodCanale", VistaRptVersamento.model().COD_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_canale", VistaRptVersamento.model().COD_CANALE.getFieldType()));
				setParameter(object, "setCodPsp", VistaRptVersamento.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", VistaRptVersamento.model().COD_PSP.getFieldType()));
				setParameter(object, "setCodIntermediarioPsp", VistaRptVersamento.model().COD_INTERMEDIARIO_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_intermediario_psp", VistaRptVersamento.model().COD_INTERMEDIARIO_PSP.getFieldType()));
				setParameter(object, "setTipoVersamento", VistaRptVersamento.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", VistaRptVersamento.model().TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setTipoIdentificativoAttestante", VistaRptVersamento.model().TIPO_IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_identificativo_attestante", VistaRptVersamento.model().TIPO_IDENTIFICATIVO_ATTESTANTE.getFieldType()));
				setParameter(object, "setIdentificativoAttestante", VistaRptVersamento.model().IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "identificativo_attestante", VistaRptVersamento.model().IDENTIFICATIVO_ATTESTANTE.getFieldType()));
				setParameter(object, "setDenominazioneAttestante", VistaRptVersamento.model().DENOMINAZIONE_ATTESTANTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "denominazione_attestante", VistaRptVersamento.model().DENOMINAZIONE_ATTESTANTE.getFieldType()));
				setParameter(object, "setCodStazione", VistaRptVersamento.model().COD_STAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_stazione", VistaRptVersamento.model().COD_STAZIONE.getFieldType()));
				setParameter(object, "setCodTransazioneRPT", VistaRptVersamento.model().COD_TRANSAZIONE_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rpt", VistaRptVersamento.model().COD_TRANSAZIONE_RPT.getFieldType()));
				setParameter(object, "setCodTransazioneRT", VistaRptVersamento.model().COD_TRANSAZIONE_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rt", VistaRptVersamento.model().COD_TRANSAZIONE_RT.getFieldType()));
				setParameter(object, "setStatoConservazione", VistaRptVersamento.model().STATO_CONSERVAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_conservazione", VistaRptVersamento.model().STATO_CONSERVAZIONE.getFieldType()));
				setParameter(object, "setDescrizioneStatoCons", VistaRptVersamento.model().DESCRIZIONE_STATO_CONS.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato_cons", VistaRptVersamento.model().DESCRIZIONE_STATO_CONS.getFieldType()));
				setParameter(object, "setDataConservazione", VistaRptVersamento.model().DATA_CONSERVAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_conservazione", VistaRptVersamento.model().DATA_CONSERVAZIONE.getFieldType()));
				setParameter(object, "setBloccante", VistaRptVersamento.model().BLOCCANTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bloccante", VistaRptVersamento.model().BLOCCANTE.getFieldType()));
				setParameter(object, "setVrsId", VistaRptVersamento.model().VRS_ID.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_id", VistaRptVersamento.model().VRS_ID.getFieldType()));
				setParameter(object, "setVrsCodVersamentoEnte", VistaRptVersamento.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_versamento_ente", VistaRptVersamento.model().VRS_COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setVrsNome", VistaRptVersamento.model().VRS_NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_nome", VistaRptVersamento.model().VRS_NOME.getFieldType()));
				setParameter(object, "setVrsImportoTotale", VistaRptVersamento.model().VRS_IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_totale", VistaRptVersamento.model().VRS_IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setVrsStatoVersamento", VistaRptVersamento.model().VRS_STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_stato_versamento", VistaRptVersamento.model().VRS_STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsDescrizioneStato", VistaRptVersamento.model().VRS_DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_descrizione_stato", VistaRptVersamento.model().VRS_DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setVrsAggiornabile", VistaRptVersamento.model().VRS_AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_aggiornabile", VistaRptVersamento.model().VRS_AGGIORNABILE.getFieldType()));
				setParameter(object, "setVrsDataCreazione", VistaRptVersamento.model().VRS_DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_creazione", VistaRptVersamento.model().VRS_DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setVrsDataValidita", VistaRptVersamento.model().VRS_DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_validita", VistaRptVersamento.model().VRS_DATA_VALIDITA.getFieldType()));
				setParameter(object, "setVrsDataScadenza", VistaRptVersamento.model().VRS_DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_scadenza", VistaRptVersamento.model().VRS_DATA_SCADENZA.getFieldType()));
				setParameter(object, "setVrsDataOraUltimoAgg", VistaRptVersamento.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_ora_ultimo_agg", VistaRptVersamento.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType()));
				setParameter(object, "setVrsCausaleVersamento", VistaRptVersamento.model().VRS_CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_causale_versamento", VistaRptVersamento.model().VRS_CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsDebitoreTipo", VistaRptVersamento.model().VRS_DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_tipo", VistaRptVersamento.model().VRS_DEBITORE_TIPO.getFieldType()));
				setParameter(object, "setVrsDebitoreIdentificativo", VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_identificativo", VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setVrsDebitoreAnagrafica", VistaRptVersamento.model().VRS_DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_anagrafica", VistaRptVersamento.model().VRS_DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setVrsDebitoreIndirizzo", VistaRptVersamento.model().VRS_DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_indirizzo", VistaRptVersamento.model().VRS_DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setVrsDebitoreCivico", VistaRptVersamento.model().VRS_DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_civico", VistaRptVersamento.model().VRS_DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setVrsDebitoreCap", VistaRptVersamento.model().VRS_DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_cap", VistaRptVersamento.model().VRS_DEBITORE_CAP.getFieldType()));
				setParameter(object, "setVrsDebitoreLocalita", VistaRptVersamento.model().VRS_DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_localita", VistaRptVersamento.model().VRS_DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setVrsDebitoreProvincia", VistaRptVersamento.model().VRS_DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_provincia", VistaRptVersamento.model().VRS_DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setVrsDebitoreNazione", VistaRptVersamento.model().VRS_DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_nazione", VistaRptVersamento.model().VRS_DEBITORE_NAZIONE.getFieldType()));
				setParameter(object, "setVrsDebitoreEmail", VistaRptVersamento.model().VRS_DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_email", VistaRptVersamento.model().VRS_DEBITORE_EMAIL.getFieldType()));
				setParameter(object, "setVrsDebitoreTelefono", VistaRptVersamento.model().VRS_DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_telefono", VistaRptVersamento.model().VRS_DEBITORE_TELEFONO.getFieldType()));
				setParameter(object, "setVrsDebitoreCellulare", VistaRptVersamento.model().VRS_DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_cellulare", VistaRptVersamento.model().VRS_DEBITORE_CELLULARE.getFieldType()));
				setParameter(object, "setVrsDebitoreFax", VistaRptVersamento.model().VRS_DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_fax", VistaRptVersamento.model().VRS_DEBITORE_FAX.getFieldType()));
				setParameter(object, "setVrsTassonomiaAvviso", VistaRptVersamento.model().VRS_TASSONOMIA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tassonomia_avviso", VistaRptVersamento.model().VRS_TASSONOMIA_AVVISO.getFieldType()));
				setParameter(object, "setVrsTassonomia", VistaRptVersamento.model().VRS_TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tassonomia", VistaRptVersamento.model().VRS_TASSONOMIA.getFieldType()));
				setParameter(object, "setVrsCodLotto", VistaRptVersamento.model().VRS_COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_lotto", VistaRptVersamento.model().VRS_COD_LOTTO.getFieldType()));
				setParameter(object, "setVrsCodVersamentoLotto", VistaRptVersamento.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_versamento_lotto", VistaRptVersamento.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType()));
				setParameter(object, "setVrsCodAnnoTributario", VistaRptVersamento.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_anno_tributario", VistaRptVersamento.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType()));
				setParameter(object, "setVrsCodBundlekey", VistaRptVersamento.model().VRS_COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_bundlekey", VistaRptVersamento.model().VRS_COD_BUNDLEKEY.getFieldType()));
				setParameter(object, "setVrsDatiAllegati", VistaRptVersamento.model().VRS_DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_dati_allegati", VistaRptVersamento.model().VRS_DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setVrsIncasso", VistaRptVersamento.model().VRS_INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_incasso", VistaRptVersamento.model().VRS_INCASSO.getFieldType()));
				setParameter(object, "setVrsAnomalie", VistaRptVersamento.model().VRS_ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_anomalie", VistaRptVersamento.model().VRS_ANOMALIE.getFieldType()));
				setParameter(object, "setVrsIuvVersamento", VistaRptVersamento.model().VRS_IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_iuv_versamento", VistaRptVersamento.model().VRS_IUV_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsNumeroAvviso", VistaRptVersamento.model().VRS_NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_numero_avviso", VistaRptVersamento.model().VRS_NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setVrsAck", VistaRptVersamento.model().VRS_ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_ack", VistaRptVersamento.model().VRS_ACK.getFieldType()));
				setParameter(object, "setVrsAnomalo", VistaRptVersamento.model().VRS_ANOMALO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_anomalo", VistaRptVersamento.model().VRS_ANOMALO.getFieldType()));
				setParameter(object, "setVrsDivisione", VistaRptVersamento.model().VRS_DIVISIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_divisione", VistaRptVersamento.model().VRS_DIVISIONE.getFieldType()));
				setParameter(object, "setVrsDirezione", VistaRptVersamento.model().VRS_DIREZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_direzione", VistaRptVersamento.model().VRS_DIREZIONE.getFieldType()));
				setParameter(object, "setVrsIdSessione", VistaRptVersamento.model().VRS_ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_id_sessione", VistaRptVersamento.model().VRS_ID_SESSIONE.getFieldType()));
				setParameter(object, "setVrsDataPagamento", VistaRptVersamento.model().VRS_DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_pagamento", VistaRptVersamento.model().VRS_DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsImportoPagato", VistaRptVersamento.model().VRS_IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_pagato", VistaRptVersamento.model().VRS_IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setVrsImportoIncassato", VistaRptVersamento.model().VRS_IMPORTO_INCASSATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_incassato", VistaRptVersamento.model().VRS_IMPORTO_INCASSATO.getFieldType()));
				setParameter(object, "setVrsStatoPagamento", VistaRptVersamento.model().VRS_STATO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_stato_pagamento", VistaRptVersamento.model().VRS_STATO_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsIuvPagamento", VistaRptVersamento.model().VRS_IUV_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_iuv_pagamento", VistaRptVersamento.model().VRS_IUV_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsSrcDebitoreIdentificativo", VistaRptVersamento.model().VRS_SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_src_debitore_identificativ", VistaRptVersamento.model().VRS_SRC_DEBITORE_IDENTIFICATIVO.getFieldType()));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , Map<String,Object> map ) throws ServiceException {
		
		try{

			if(model.equals(VistaRptVersamento.model())){
				VistaRptVersamento object = new VistaRptVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodCarrello", VistaRptVersamento.model().COD_CARRELLO.getFieldType(),
					this.getObjectFromMap(map,"codCarrello"));
				setParameter(object, "setIuv", VistaRptVersamento.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setCcp", VistaRptVersamento.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				setParameter(object, "setCodDominio", VistaRptVersamento.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setCodMsgRichiesta", VistaRptVersamento.model().COD_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRichiesta"));
				setParameter(object, "setDataMsgRichiesta", VistaRptVersamento.model().DATA_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"dataMsgRichiesta"));
				setParameter(object, "setStato", VistaRptVersamento.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", VistaRptVersamento.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setCodSessione", VistaRptVersamento.model().COD_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"codSessione"));
				setParameter(object, "setCodSessionePortale", VistaRptVersamento.model().COD_SESSIONE_PORTALE.getFieldType(),
					this.getObjectFromMap(map,"codSessionePortale"));
				setParameter(object, "setPspRedirectURL", VistaRptVersamento.model().PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"pspRedirectURL"));
				setParameter(object, "setXmlRPT", VistaRptVersamento.model().XML_RPT.getFieldType(),
					this.getObjectFromMap(map,"xmlRPT"));
				setParameter(object, "setDataAggiornamentoStato", VistaRptVersamento.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					this.getObjectFromMap(map,"dataAggiornamentoStato"));
				setParameter(object, "setCallbackURL", VistaRptVersamento.model().CALLBACK_URL.getFieldType(),
					this.getObjectFromMap(map,"callbackURL"));
				setParameter(object, "setModelloPagamento", VistaRptVersamento.model().MODELLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"modelloPagamento"));
				setParameter(object, "setCodMsgRicevuta", VistaRptVersamento.model().COD_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRicevuta"));
				setParameter(object, "setDataMsgRicevuta", VistaRptVersamento.model().DATA_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"dataMsgRicevuta"));
				setParameter(object, "setCodEsitoPagamento", VistaRptVersamento.model().COD_ESITO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"codEsitoPagamento"));
				setParameter(object, "setImportoTotalePagato", VistaRptVersamento.model().IMPORTO_TOTALE_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoTotalePagato"));
				setParameter(object, "setXmlRT", VistaRptVersamento.model().XML_RT.getFieldType(),
					this.getObjectFromMap(map,"xmlRT"));
				setParameter(object, "setCodCanale", VistaRptVersamento.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				setParameter(object, "setCodPsp", VistaRptVersamento.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				setParameter(object, "setCodIntermediarioPsp", VistaRptVersamento.model().COD_INTERMEDIARIO_PSP.getFieldType(),
					this.getObjectFromMap(map,"codIntermediarioPsp"));
				setParameter(object, "setTipoVersamento", VistaRptVersamento.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				setParameter(object, "setTipoIdentificativoAttestante", VistaRptVersamento.model().TIPO_IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					this.getObjectFromMap(map,"tipoIdentificativoAttestante"));
				setParameter(object, "setIdentificativoAttestante", VistaRptVersamento.model().IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					this.getObjectFromMap(map,"identificativoAttestante"));
				setParameter(object, "setDenominazioneAttestante", VistaRptVersamento.model().DENOMINAZIONE_ATTESTANTE.getFieldType(),
					this.getObjectFromMap(map,"denominazioneAttestante"));
				setParameter(object, "setCodStazione", VistaRptVersamento.model().COD_STAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codStazione"));
				setParameter(object, "setCodTransazioneRPT", VistaRptVersamento.model().COD_TRANSAZIONE_RPT.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneRPT"));
				setParameter(object, "setCodTransazioneRT", VistaRptVersamento.model().COD_TRANSAZIONE_RT.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneRT"));
				setParameter(object, "setStatoConservazione", VistaRptVersamento.model().STATO_CONSERVAZIONE.getFieldType(),
					this.getObjectFromMap(map,"statoConservazione"));
				setParameter(object, "setDescrizioneStatoCons", VistaRptVersamento.model().DESCRIZIONE_STATO_CONS.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStatoCons"));
				setParameter(object, "setDataConservazione", VistaRptVersamento.model().DATA_CONSERVAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataConservazione"));
				setParameter(object, "setBloccante", VistaRptVersamento.model().BLOCCANTE.getFieldType(),
					this.getObjectFromMap(map,"bloccante"));
				setParameter(object, "setVrsId", VistaRptVersamento.model().VRS_ID.getFieldType(),
					this.getObjectFromMap(map,"vrsId"));
				setParameter(object, "setVrsCodVersamentoEnte", VistaRptVersamento.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"vrsCodVersamentoEnte"));
				setParameter(object, "setVrsNome", VistaRptVersamento.model().VRS_NOME.getFieldType(),
					this.getObjectFromMap(map,"vrsNome"));
				setParameter(object, "setVrsImportoTotale", VistaRptVersamento.model().VRS_IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoTotale"));
				setParameter(object, "setVrsStatoVersamento", VistaRptVersamento.model().VRS_STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsStatoVersamento"));
				setParameter(object, "setVrsDescrizioneStato", VistaRptVersamento.model().VRS_DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"vrsDescrizioneStato"));
				setParameter(object, "setVrsAggiornabile", VistaRptVersamento.model().VRS_AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"vrsAggiornabile"));
				setParameter(object, "setVrsDataCreazione", VistaRptVersamento.model().VRS_DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDataCreazione"));
				setParameter(object, "setVrsDataValidita", VistaRptVersamento.model().VRS_DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"vrsDataValidita"));
				setParameter(object, "setVrsDataScadenza", VistaRptVersamento.model().VRS_DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"vrsDataScadenza"));
				setParameter(object, "setVrsDataOraUltimoAgg", VistaRptVersamento.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType(),
					this.getObjectFromMap(map,"vrsDataOraUltimoAgg"));
				setParameter(object, "setVrsCausaleVersamento", VistaRptVersamento.model().VRS_CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCausaleVersamento"));
				setParameter(object, "setVrsDebitoreTipo", VistaRptVersamento.model().VRS_DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreTipo"));
				setParameter(object, "setVrsDebitoreIdentificativo", VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreIdentificativo"));
				setParameter(object, "setVrsDebitoreAnagrafica", VistaRptVersamento.model().VRS_DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreAnagrafica"));
				setParameter(object, "setVrsDebitoreIndirizzo", VistaRptVersamento.model().VRS_DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreIndirizzo"));
				setParameter(object, "setVrsDebitoreCivico", VistaRptVersamento.model().VRS_DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCivico"));
				setParameter(object, "setVrsDebitoreCap", VistaRptVersamento.model().VRS_DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCap"));
				setParameter(object, "setVrsDebitoreLocalita", VistaRptVersamento.model().VRS_DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreLocalita"));
				setParameter(object, "setVrsDebitoreProvincia", VistaRptVersamento.model().VRS_DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreProvincia"));
				setParameter(object, "setVrsDebitoreNazione", VistaRptVersamento.model().VRS_DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreNazione"));
				setParameter(object, "setVrsDebitoreEmail", VistaRptVersamento.model().VRS_DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreEmail"));
				setParameter(object, "setVrsDebitoreTelefono", VistaRptVersamento.model().VRS_DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreTelefono"));
				setParameter(object, "setVrsDebitoreCellulare", VistaRptVersamento.model().VRS_DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCellulare"));
				setParameter(object, "setVrsDebitoreFax", VistaRptVersamento.model().VRS_DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreFax"));
				setParameter(object, "setVrsTassonomiaAvviso", VistaRptVersamento.model().VRS_TASSONOMIA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"vrsTassonomiaAvviso"));
				setParameter(object, "setVrsTassonomia", VistaRptVersamento.model().VRS_TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"vrsTassonomia"));
				setParameter(object, "setVrsCodLotto", VistaRptVersamento.model().VRS_COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodLotto"));
				setParameter(object, "setVrsCodVersamentoLotto", VistaRptVersamento.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodVersamentoLotto"));
				setParameter(object, "setVrsCodAnnoTributario", VistaRptVersamento.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodAnnoTributario"));
				setParameter(object, "setVrsCodBundlekey", VistaRptVersamento.model().VRS_COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"vrsCodBundlekey"));
				setParameter(object, "setVrsDatiAllegati", VistaRptVersamento.model().VRS_DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"vrsDatiAllegati"));
				setParameter(object, "setVrsIncasso", VistaRptVersamento.model().VRS_INCASSO.getFieldType(),
					this.getObjectFromMap(map,"vrsIncasso"));
				setParameter(object, "setVrsAnomalie", VistaRptVersamento.model().VRS_ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"vrsAnomalie"));
				setParameter(object, "setVrsIuvVersamento", VistaRptVersamento.model().VRS_IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsIuvVersamento"));
				setParameter(object, "setVrsNumeroAvviso", VistaRptVersamento.model().VRS_NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"vrsNumeroAvviso"));
				setParameter(object, "setVrsAck", VistaRptVersamento.model().VRS_ACK.getFieldType(),
					this.getObjectFromMap(map,"vrsAck"));
				setParameter(object, "setVrsAnomalo", VistaRptVersamento.model().VRS_ANOMALO.getFieldType(),
					this.getObjectFromMap(map,"vrsAnomalo"));
				setParameter(object, "setVrsDivisione", VistaRptVersamento.model().VRS_DIVISIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDivisione"));
				setParameter(object, "setVrsDirezione", VistaRptVersamento.model().VRS_DIREZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDirezione"));
				setParameter(object, "setVrsIdSessione", VistaRptVersamento.model().VRS_ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsIdSessione"));
				setParameter(object, "setVrsDataPagamento", VistaRptVersamento.model().VRS_DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsDataPagamento"));
				setParameter(object, "setVrsImportoPagato", VistaRptVersamento.model().VRS_IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoPagato"));
				setParameter(object, "setVrsImportoIncassato", VistaRptVersamento.model().VRS_IMPORTO_INCASSATO.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoIncassato"));
				setParameter(object, "setVrsStatoPagamento", VistaRptVersamento.model().VRS_STATO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsStatoPagamento"));
				setParameter(object, "setVrsIuvPagamento", VistaRptVersamento.model().VRS_IUV_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsIuvPagamento"));
				setParameter(object, "setVrsSrcDebitoreIdentificativo", VistaRptVersamento.model().VRS_SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"vrsSrcDebitoreIdentificativo"));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	
	@Override
	public IKeyGeneratorObject getKeyGeneratorObject( IModel<?> model )  throws ServiceException {
		
		try{

			if(model.equals(VistaRptVersamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_rpt_versamenti","id","seq_v_rpt_versamenti","v_rpt_versamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
