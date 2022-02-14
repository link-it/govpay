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
import org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType;

import it.govpay.orm.VistaVersamentoNonRendicontato;


/**     
 * VistaVersamentoNonRendicontatoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaVersamentoNonRendicontatoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaVersamentoNonRendicontato.model())){
				VistaVersamentoNonRendicontato object = new VistaVersamentoNonRendicontato();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setSngCodSingVersEnte", VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_cod_sing_vers_ente", VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE.getFieldType()));
				setParameter(object, "setSngStatoSingoloVersamento", VistaVersamentoNonRendicontato.model().SNG_STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_stato_singolo_versamento", VistaVersamentoNonRendicontato.model().SNG_STATO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setSngImportoSingoloVersamento", VistaVersamentoNonRendicontato.model().SNG_IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_importo_singolo_versamento", VistaVersamentoNonRendicontato.model().SNG_IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setSngDescrizione", VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_descrizione", VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE.getFieldType()));
				setParameter(object, "setSngDatiAllegati", VistaVersamentoNonRendicontato.model().SNG_DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_dati_allegati", VistaVersamentoNonRendicontato.model().SNG_DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setSngIndiceDati", VistaVersamentoNonRendicontato.model().SNG_INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_indice_dati", VistaVersamentoNonRendicontato.model().SNG_INDICE_DATI.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setSngDescrizioneCausaleRPT", VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE_CAUSALE_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_descrizione_causale_rpt", VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE_CAUSALE_RPT.getFieldType()));
				setParameter(object, "setSngContabilita", VistaVersamentoNonRendicontato.model().SNG_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_contabilita", VistaVersamentoNonRendicontato.model().SNG_CONTABILITA.getFieldType()));
				setParameter(object, "setVrsId", VistaVersamentoNonRendicontato.model().VRS_ID.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_id", VistaVersamentoNonRendicontato.model().VRS_ID.getFieldType()));
				setParameter(object, "setVrsCodVersamentoEnte", VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_versamento_ente", VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setVrsNome", VistaVersamentoNonRendicontato.model().VRS_NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_nome", VistaVersamentoNonRendicontato.model().VRS_NOME.getFieldType()));
				setParameter(object, "setVrsImportoTotale", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_totale", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setVrsStatoVersamento", VistaVersamentoNonRendicontato.model().VRS_STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_stato_versamento", VistaVersamentoNonRendicontato.model().VRS_STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsDescrizioneStato", VistaVersamentoNonRendicontato.model().VRS_DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_descrizione_stato", VistaVersamentoNonRendicontato.model().VRS_DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setVrsAggiornabile", VistaVersamentoNonRendicontato.model().VRS_AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_aggiornabile", VistaVersamentoNonRendicontato.model().VRS_AGGIORNABILE.getFieldType()));
				setParameter(object, "setVrsDataCreazione", VistaVersamentoNonRendicontato.model().VRS_DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_creazione", VistaVersamentoNonRendicontato.model().VRS_DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setVrsDataValidita", VistaVersamentoNonRendicontato.model().VRS_DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_validita", VistaVersamentoNonRendicontato.model().VRS_DATA_VALIDITA.getFieldType()));
				setParameter(object, "setVrsDataScadenza", VistaVersamentoNonRendicontato.model().VRS_DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_scadenza", VistaVersamentoNonRendicontato.model().VRS_DATA_SCADENZA.getFieldType()));
				setParameter(object, "setVrsDataOraUltimoAgg", VistaVersamentoNonRendicontato.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_ora_ultimo_agg", VistaVersamentoNonRendicontato.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType()));
				setParameter(object, "setVrsCausaleVersamento", VistaVersamentoNonRendicontato.model().VRS_CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_causale_versamento", VistaVersamentoNonRendicontato.model().VRS_CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsDebitoreTipo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_tipo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TIPO.getFieldType()));
				setParameter(object, "setVrsDebitoreIdentificativo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_identificativo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setVrsDebitoreAnagrafica", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_anagrafica", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setVrsDebitoreIndirizzo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_indirizzo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setVrsDebitoreCivico", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_civico", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setVrsDebitoreCap", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_cap", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CAP.getFieldType()));
				setParameter(object, "setVrsDebitoreLocalita", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_localita", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setVrsDebitoreProvincia", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_provincia", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setVrsDebitoreNazione", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_nazione", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_NAZIONE.getFieldType()));
				setParameter(object, "setVrsDebitoreEmail", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_email", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_EMAIL.getFieldType()));
				setParameter(object, "setVrsDebitoreTelefono", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_telefono", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TELEFONO.getFieldType()));
				setParameter(object, "setVrsDebitoreCellulare", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_cellulare", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CELLULARE.getFieldType()));
				setParameter(object, "setVrsDebitoreFax", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_fax", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_FAX.getFieldType()));
				setParameter(object, "setVrsTassonomiaAvviso", VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tassonomia_avviso", VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA_AVVISO.getFieldType()));
				setParameter(object, "setVrsTassonomia", VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tassonomia", VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA.getFieldType()));
				setParameter(object, "setVrsCodLotto", VistaVersamentoNonRendicontato.model().VRS_COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_lotto", VistaVersamentoNonRendicontato.model().VRS_COD_LOTTO.getFieldType()));
				setParameter(object, "setVrsCodVersamentoLotto", VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_versamento_lotto", VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType()));
				setParameter(object, "setVrsCodAnnoTributario", VistaVersamentoNonRendicontato.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_anno_tributario", VistaVersamentoNonRendicontato.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType()));
				setParameter(object, "setVrsCodBundlekey", VistaVersamentoNonRendicontato.model().VRS_COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_bundlekey", VistaVersamentoNonRendicontato.model().VRS_COD_BUNDLEKEY.getFieldType()));
				setParameter(object, "setVrsDatiAllegati", VistaVersamentoNonRendicontato.model().VRS_DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_dati_allegati", VistaVersamentoNonRendicontato.model().VRS_DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setVrsIncasso", VistaVersamentoNonRendicontato.model().VRS_INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_incasso", VistaVersamentoNonRendicontato.model().VRS_INCASSO.getFieldType()));
				setParameter(object, "setVrsAnomalie", VistaVersamentoNonRendicontato.model().VRS_ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_anomalie", VistaVersamentoNonRendicontato.model().VRS_ANOMALIE.getFieldType()));
				setParameter(object, "setVrsIuvVersamento", VistaVersamentoNonRendicontato.model().VRS_IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_iuv_versamento", VistaVersamentoNonRendicontato.model().VRS_IUV_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsNumeroAvviso", VistaVersamentoNonRendicontato.model().VRS_NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_numero_avviso", VistaVersamentoNonRendicontato.model().VRS_NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setVrsAck", VistaVersamentoNonRendicontato.model().VRS_ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_ack", VistaVersamentoNonRendicontato.model().VRS_ACK.getFieldType()));
				setParameter(object, "setVrsAnomalo", VistaVersamentoNonRendicontato.model().VRS_ANOMALO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_anomalo", VistaVersamentoNonRendicontato.model().VRS_ANOMALO.getFieldType()));
				setParameter(object, "setVrsDivisione", VistaVersamentoNonRendicontato.model().VRS_DIVISIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_divisione", VistaVersamentoNonRendicontato.model().VRS_DIVISIONE.getFieldType()));
				setParameter(object, "setVrsDirezione", VistaVersamentoNonRendicontato.model().VRS_DIREZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_direzione", VistaVersamentoNonRendicontato.model().VRS_DIREZIONE.getFieldType()));
				setParameter(object, "setVrsIdSessione", VistaVersamentoNonRendicontato.model().VRS_ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_id_sessione", VistaVersamentoNonRendicontato.model().VRS_ID_SESSIONE.getFieldType()));
				setParameter(object, "setVrsDataPagamento", VistaVersamentoNonRendicontato.model().VRS_DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_pagamento", VistaVersamentoNonRendicontato.model().VRS_DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsImportoPagato", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_pagato", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setVrsImportoIncassato", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_INCASSATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_incassato", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_INCASSATO.getFieldType()));
				setParameter(object, "setVrsStatoPagamento", VistaVersamentoNonRendicontato.model().VRS_STATO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_stato_pagamento", VistaVersamentoNonRendicontato.model().VRS_STATO_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsIuvPagamento", VistaVersamentoNonRendicontato.model().VRS_IUV_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_iuv_pagamento", VistaVersamentoNonRendicontato.model().VRS_IUV_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsCodRata", VistaVersamentoNonRendicontato.model().VRS_COD_RATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_rata", VistaVersamentoNonRendicontato.model().VRS_COD_RATA.getFieldType()));
				setParameter(object, "setVrsTipo", VistaVersamentoNonRendicontato.model().VRS_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tipo", VistaVersamentoNonRendicontato.model().VRS_TIPO.getFieldType()));
				setParameter(object, "setVrsProprieta", VistaVersamentoNonRendicontato.model().VRS_PROPRIETA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_proprieta", VistaVersamentoNonRendicontato.model().VRS_PROPRIETA.getFieldType()));
				setParameter(object, "setPagCodDominio", VistaVersamentoNonRendicontato.model().PAG_COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_cod_dominio", VistaVersamentoNonRendicontato.model().PAG_COD_DOMINIO.getFieldType()));
				setParameter(object, "setPagIuv", VistaVersamentoNonRendicontato.model().PAG_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_iuv", VistaVersamentoNonRendicontato.model().PAG_IUV.getFieldType()));
				setParameter(object, "setPagIndiceDati", VistaVersamentoNonRendicontato.model().PAG_INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_indice_dati", VistaVersamentoNonRendicontato.model().PAG_INDICE_DATI.getFieldType()));
				setParameter(object, "setPagImportoPagato", VistaVersamentoNonRendicontato.model().PAG_IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_importo_pagato", VistaVersamentoNonRendicontato.model().PAG_IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setPagDataAcquisizione", VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_data_acquisizione", VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setPagIur", VistaVersamentoNonRendicontato.model().PAG_IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_iur", VistaVersamentoNonRendicontato.model().PAG_IUR.getFieldType()));
				setParameter(object, "setPagDataPagamento", VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_data_pagamento", VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setPagCommissioniPsp", VistaVersamentoNonRendicontato.model().PAG_COMMISSIONI_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_commissioni_psp", VistaVersamentoNonRendicontato.model().PAG_COMMISSIONI_PSP.getFieldType()));
				setParameter(object, "setPagTipoAllegato", VistaVersamentoNonRendicontato.model().PAG_TIPO_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_tipo_allegato", VistaVersamentoNonRendicontato.model().PAG_TIPO_ALLEGATO.getFieldType()));
				setParameter(object, "setPagAllegato", VistaVersamentoNonRendicontato.model().PAG_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_allegato", VistaVersamentoNonRendicontato.model().PAG_ALLEGATO.getFieldType()));
				setParameter(object, "setPagDataAcquisizioneRevoca", VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_data_acquisizione_revoca", VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE_REVOCA.getFieldType()));
				setParameter(object, "setPagCausaleRevoca", VistaVersamentoNonRendicontato.model().PAG_CAUSALE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_causale_revoca", VistaVersamentoNonRendicontato.model().PAG_CAUSALE_REVOCA.getFieldType()));
				setParameter(object, "setPagDatiRevoca", VistaVersamentoNonRendicontato.model().PAG_DATI_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_dati_revoca", VistaVersamentoNonRendicontato.model().PAG_DATI_REVOCA.getFieldType()));
				setParameter(object, "setPagImportoRevocato", VistaVersamentoNonRendicontato.model().PAG_IMPORTO_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_importo_revocato", VistaVersamentoNonRendicontato.model().PAG_IMPORTO_REVOCATO.getFieldType()));
				setParameter(object, "setPagEsitoRevoca", VistaVersamentoNonRendicontato.model().PAG_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_esito_revoca", VistaVersamentoNonRendicontato.model().PAG_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setPagDatiEsitoRevoca", VistaVersamentoNonRendicontato.model().PAG_DATI_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_dati_esito_revoca", VistaVersamentoNonRendicontato.model().PAG_DATI_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setPagStato", VistaVersamentoNonRendicontato.model().PAG_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_stato", VistaVersamentoNonRendicontato.model().PAG_STATO.getFieldType()));
				setParameter(object, "setPagTipo", VistaVersamentoNonRendicontato.model().PAG_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_tipo", VistaVersamentoNonRendicontato.model().PAG_TIPO.getFieldType()));
				setParameter(object, "setRptIuv", VistaVersamentoNonRendicontato.model().RPT_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rpt_iuv", VistaVersamentoNonRendicontato.model().RPT_IUV.getFieldType()));
				setParameter(object, "setRptCcp", VistaVersamentoNonRendicontato.model().RPT_CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rpt_ccp", VistaVersamentoNonRendicontato.model().RPT_CCP.getFieldType()));
				setParameter(object, "setRncTrn", VistaVersamentoNonRendicontato.model().RNC_TRN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnc_trn", VistaVersamentoNonRendicontato.model().RNC_TRN.getFieldType()));
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

			if(model.equals(VistaVersamentoNonRendicontato.model())){
				VistaVersamentoNonRendicontato object = new VistaVersamentoNonRendicontato();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setSngCodSingVersEnte", VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE.getFieldType(),
					this.getObjectFromMap(map,"sngCodSingVersEnte"));
				setParameter(object, "setSngStatoSingoloVersamento", VistaVersamentoNonRendicontato.model().SNG_STATO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"sngStatoSingoloVersamento"));
				setParameter(object, "setSngImportoSingoloVersamento", VistaVersamentoNonRendicontato.model().SNG_IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"sngImportoSingoloVersamento"));
				setParameter(object, "setSngDescrizione", VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"sngDescrizione"));
				setParameter(object, "setSngDatiAllegati", VistaVersamentoNonRendicontato.model().SNG_DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"sngDatiAllegati"));
				setParameter(object, "setSngIndiceDati", VistaVersamentoNonRendicontato.model().SNG_INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"sngIndiceDati"));
				setParameter(object, "setSngDescrizioneCausaleRPT", VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE_CAUSALE_RPT.getFieldType(),
					this.getObjectFromMap(map,"sngDescrizioneCausaleRPT"));
				setParameter(object, "setSngContabilita", VistaVersamentoNonRendicontato.model().SNG_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"sngContabilita"));
				setParameter(object, "setVrsId", VistaVersamentoNonRendicontato.model().VRS_ID.getFieldType(),
					this.getObjectFromMap(map,"vrsId"));
				setParameter(object, "setVrsCodVersamentoEnte", VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"vrsCodVersamentoEnte"));
				setParameter(object, "setVrsNome", VistaVersamentoNonRendicontato.model().VRS_NOME.getFieldType(),
					this.getObjectFromMap(map,"vrsNome"));
				setParameter(object, "setVrsImportoTotale", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoTotale"));
				setParameter(object, "setVrsStatoVersamento", VistaVersamentoNonRendicontato.model().VRS_STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsStatoVersamento"));
				setParameter(object, "setVrsDescrizioneStato", VistaVersamentoNonRendicontato.model().VRS_DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"vrsDescrizioneStato"));
				setParameter(object, "setVrsAggiornabile", VistaVersamentoNonRendicontato.model().VRS_AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"vrsAggiornabile"));
				setParameter(object, "setVrsDataCreazione", VistaVersamentoNonRendicontato.model().VRS_DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDataCreazione"));
				setParameter(object, "setVrsDataValidita", VistaVersamentoNonRendicontato.model().VRS_DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"vrsDataValidita"));
				setParameter(object, "setVrsDataScadenza", VistaVersamentoNonRendicontato.model().VRS_DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"vrsDataScadenza"));
				setParameter(object, "setVrsDataOraUltimoAgg", VistaVersamentoNonRendicontato.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType(),
					this.getObjectFromMap(map,"vrsDataOraUltimoAgg"));
				setParameter(object, "setVrsCausaleVersamento", VistaVersamentoNonRendicontato.model().VRS_CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCausaleVersamento"));
				setParameter(object, "setVrsDebitoreTipo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreTipo"));
				setParameter(object, "setVrsDebitoreIdentificativo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreIdentificativo"));
				setParameter(object, "setVrsDebitoreAnagrafica", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreAnagrafica"));
				setParameter(object, "setVrsDebitoreIndirizzo", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreIndirizzo"));
				setParameter(object, "setVrsDebitoreCivico", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCivico"));
				setParameter(object, "setVrsDebitoreCap", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCap"));
				setParameter(object, "setVrsDebitoreLocalita", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreLocalita"));
				setParameter(object, "setVrsDebitoreProvincia", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreProvincia"));
				setParameter(object, "setVrsDebitoreNazione", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreNazione"));
				setParameter(object, "setVrsDebitoreEmail", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreEmail"));
				setParameter(object, "setVrsDebitoreTelefono", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreTelefono"));
				setParameter(object, "setVrsDebitoreCellulare", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCellulare"));
				setParameter(object, "setVrsDebitoreFax", VistaVersamentoNonRendicontato.model().VRS_DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreFax"));
				setParameter(object, "setVrsTassonomiaAvviso", VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"vrsTassonomiaAvviso"));
				setParameter(object, "setVrsTassonomia", VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"vrsTassonomia"));
				setParameter(object, "setVrsCodLotto", VistaVersamentoNonRendicontato.model().VRS_COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodLotto"));
				setParameter(object, "setVrsCodVersamentoLotto", VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodVersamentoLotto"));
				setParameter(object, "setVrsCodAnnoTributario", VistaVersamentoNonRendicontato.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodAnnoTributario"));
				setParameter(object, "setVrsCodBundlekey", VistaVersamentoNonRendicontato.model().VRS_COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"vrsCodBundlekey"));
				setParameter(object, "setVrsDatiAllegati", VistaVersamentoNonRendicontato.model().VRS_DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"vrsDatiAllegati"));
				setParameter(object, "setVrsIncasso", VistaVersamentoNonRendicontato.model().VRS_INCASSO.getFieldType(),
					this.getObjectFromMap(map,"vrsIncasso"));
				setParameter(object, "setVrsAnomalie", VistaVersamentoNonRendicontato.model().VRS_ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"vrsAnomalie"));
				setParameter(object, "setVrsIuvVersamento", VistaVersamentoNonRendicontato.model().VRS_IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsIuvVersamento"));
				setParameter(object, "setVrsNumeroAvviso", VistaVersamentoNonRendicontato.model().VRS_NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"vrsNumeroAvviso"));
				setParameter(object, "setVrsAck", VistaVersamentoNonRendicontato.model().VRS_ACK.getFieldType(),
					this.getObjectFromMap(map,"vrsAck"));
				setParameter(object, "setVrsAnomalo", VistaVersamentoNonRendicontato.model().VRS_ANOMALO.getFieldType(),
					this.getObjectFromMap(map,"vrsAnomalo"));
				setParameter(object, "setVrsDivisione", VistaVersamentoNonRendicontato.model().VRS_DIVISIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDivisione"));
				setParameter(object, "setVrsDirezione", VistaVersamentoNonRendicontato.model().VRS_DIREZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDirezione"));
				setParameter(object, "setVrsIdSessione", VistaVersamentoNonRendicontato.model().VRS_ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsIdSessione"));
				setParameter(object, "setVrsDataPagamento", VistaVersamentoNonRendicontato.model().VRS_DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsDataPagamento"));
				setParameter(object, "setVrsImportoPagato", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoPagato"));
				setParameter(object, "setVrsImportoIncassato", VistaVersamentoNonRendicontato.model().VRS_IMPORTO_INCASSATO.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoIncassato"));
				setParameter(object, "setVrsStatoPagamento", VistaVersamentoNonRendicontato.model().VRS_STATO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsStatoPagamento"));
				setParameter(object, "setVrsIuvPagamento", VistaVersamentoNonRendicontato.model().VRS_IUV_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsIuvPagamento"));
				setParameter(object, "setVrsCodRata", VistaVersamentoNonRendicontato.model().VRS_COD_RATA.getFieldType(),
					this.getObjectFromMap(map,"vrsCodRata"));
				setParameter(object, "setVrsTipo", VistaVersamentoNonRendicontato.model().VRS_TIPO.getFieldType(),
					this.getObjectFromMap(map,"vrsTipo"));
				setParameter(object, "setVrsProprieta", VistaVersamentoNonRendicontato.model().VRS_PROPRIETA.getFieldType(),
					this.getObjectFromMap(map,"vrsProprieta"));
				setParameter(object, "setPagCodDominio", VistaVersamentoNonRendicontato.model().PAG_COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"pagCodDominio"));
				setParameter(object, "setPagIuv", VistaVersamentoNonRendicontato.model().PAG_IUV.getFieldType(),
					this.getObjectFromMap(map,"pagIuv"));
				setParameter(object, "setPagIndiceDati", VistaVersamentoNonRendicontato.model().PAG_INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"pagIndiceDati"));
				setParameter(object, "setPagImportoPagato", VistaVersamentoNonRendicontato.model().PAG_IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"pagImportoPagato"));
				setParameter(object, "setPagDataAcquisizione", VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"pagDataAcquisizione"));
				setParameter(object, "setPagIur", VistaVersamentoNonRendicontato.model().PAG_IUR.getFieldType(),
					this.getObjectFromMap(map,"pagIur"));
				setParameter(object, "setPagDataPagamento", VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"pagDataPagamento"));
				setParameter(object, "setPagCommissioniPsp", VistaVersamentoNonRendicontato.model().PAG_COMMISSIONI_PSP.getFieldType(),
					this.getObjectFromMap(map,"pagCommissioniPsp"));
				setParameter(object, "setPagTipoAllegato", VistaVersamentoNonRendicontato.model().PAG_TIPO_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"pagTipoAllegato"));
				setParameter(object, "setPagAllegato", VistaVersamentoNonRendicontato.model().PAG_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"pagAllegato"));
				setParameter(object, "setPagDataAcquisizioneRevoca", VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"pagDataAcquisizioneRevoca"));
				setParameter(object, "setPagCausaleRevoca", VistaVersamentoNonRendicontato.model().PAG_CAUSALE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"pagCausaleRevoca"));
				setParameter(object, "setPagDatiRevoca", VistaVersamentoNonRendicontato.model().PAG_DATI_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"pagDatiRevoca"));
				setParameter(object, "setPagImportoRevocato", VistaVersamentoNonRendicontato.model().PAG_IMPORTO_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"pagImportoRevocato"));
				setParameter(object, "setPagEsitoRevoca", VistaVersamentoNonRendicontato.model().PAG_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"pagEsitoRevoca"));
				setParameter(object, "setPagDatiEsitoRevoca", VistaVersamentoNonRendicontato.model().PAG_DATI_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"pagDatiEsitoRevoca"));
				setParameter(object, "setPagStato", VistaVersamentoNonRendicontato.model().PAG_STATO.getFieldType(),
					this.getObjectFromMap(map,"pagStato"));
				setParameter(object, "setPagTipo", VistaVersamentoNonRendicontato.model().PAG_TIPO.getFieldType(),
					this.getObjectFromMap(map,"pagTipo"));
				setParameter(object, "setRptIuv", VistaVersamentoNonRendicontato.model().RPT_IUV.getFieldType(),
					this.getObjectFromMap(map,"rptIuv"));
				setParameter(object, "setRptCcp", VistaVersamentoNonRendicontato.model().RPT_CCP.getFieldType(),
					this.getObjectFromMap(map,"rptCcp"));
				setParameter(object, "setRncTrn", VistaVersamentoNonRendicontato.model().RNC_TRN.getFieldType(),
					this.getObjectFromMap(map,"rncTrn"));
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

			if(model.equals(VistaVersamentoNonRendicontato.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_vrs_non_rnd","id","seq_v_vrs_non_rnd","v_vrs_non_rnd_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
