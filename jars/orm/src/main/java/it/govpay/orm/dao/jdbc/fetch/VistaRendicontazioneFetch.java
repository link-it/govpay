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

import it.govpay.orm.VistaRendicontazione;


/**     
 * VistaRendicontazioneFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRendicontazioneFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaRendicontazione.model())){
				VistaRendicontazione object = new VistaRendicontazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setFrCodPsp", VistaRendicontazione.model().FR_COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_cod_psp", VistaRendicontazione.model().FR_COD_PSP.getFieldType()));
				setParameter(object, "setFrCodDominio", VistaRendicontazione.model().FR_COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_cod_dominio", VistaRendicontazione.model().FR_COD_DOMINIO.getFieldType()));
				setParameter(object, "setFrCodFlusso", VistaRendicontazione.model().FR_COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_cod_flusso", VistaRendicontazione.model().FR_COD_FLUSSO.getFieldType()));
				setParameter(object, "setFrStato", VistaRendicontazione.model().FR_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_stato", VistaRendicontazione.model().FR_STATO.getFieldType()));
				setParameter(object, "setFrDescrizioneStato", VistaRendicontazione.model().FR_DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_descrizione_stato", VistaRendicontazione.model().FR_DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setFrIur", VistaRendicontazione.model().FR_IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_iur", VistaRendicontazione.model().FR_IUR.getFieldType()));
				setParameter(object, "setFrDataOraFlusso", VistaRendicontazione.model().FR_DATA_ORA_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_data_ora_flusso", VistaRendicontazione.model().FR_DATA_ORA_FLUSSO.getFieldType()));
				setParameter(object, "setFrDataRegolamento", VistaRendicontazione.model().FR_DATA_REGOLAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_data_regolamento", VistaRendicontazione.model().FR_DATA_REGOLAMENTO.getFieldType()));
				setParameter(object, "setFrDataAcquisizione", VistaRendicontazione.model().FR_DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_data_acquisizione", VistaRendicontazione.model().FR_DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setFrNumeroPagamenti", VistaRendicontazione.model().FR_NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_numero_pagamenti", VistaRendicontazione.model().FR_NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setFrImportoTotalePagamenti", VistaRendicontazione.model().FR_IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_importo_totale_pagamenti", VistaRendicontazione.model().FR_IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				setParameter(object, "setFrCodBicRiversamento", VistaRendicontazione.model().FR_COD_BIC_RIVERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_cod_bic_riversamento", VistaRendicontazione.model().FR_COD_BIC_RIVERSAMENTO.getFieldType()));
				setParameter(object, "setFrId", VistaRendicontazione.model().FR_ID.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_id", VistaRendicontazione.model().FR_ID.getFieldType()));
				setParameter(object, "setRndIuv", VistaRendicontazione.model().RND_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_iuv", VistaRendicontazione.model().RND_IUV.getFieldType()));
				setParameter(object, "setRndIur", VistaRendicontazione.model().RND_IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_iur", VistaRendicontazione.model().RND_IUR.getFieldType()));
				setParameter(object, "setRndIndiceDati", VistaRendicontazione.model().RND_INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_indice_dati", VistaRendicontazione.model().RND_INDICE_DATI.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setRndImportoPagato", VistaRendicontazione.model().RND_IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_importo_pagato", VistaRendicontazione.model().RND_IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setRndEsito", VistaRendicontazione.model().RND_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_esito", VistaRendicontazione.model().RND_ESITO.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setRndData", VistaRendicontazione.model().RND_DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_data", VistaRendicontazione.model().RND_DATA.getFieldType()));
				setParameter(object, "setRndStato", VistaRendicontazione.model().RND_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_stato", VistaRendicontazione.model().RND_STATO.getFieldType()));
				setParameter(object, "setRndAnomalie", VistaRendicontazione.model().RND_ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnd_anomalie", VistaRendicontazione.model().RND_ANOMALIE.getFieldType()));
				setParameter(object, "setSngCodSingVersEnte", VistaRendicontazione.model().SNG_COD_SING_VERS_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_cod_sing_vers_ente", VistaRendicontazione.model().SNG_COD_SING_VERS_ENTE.getFieldType()));
				setParameter(object, "setSngStatoSingoloVersamento", VistaRendicontazione.model().SNG_STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_stato_singolo_versamento", VistaRendicontazione.model().SNG_STATO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setSngImportoSingoloVersamento", VistaRendicontazione.model().SNG_IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_importo_singolo_versamento", VistaRendicontazione.model().SNG_IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setSngDescrizione", VistaRendicontazione.model().SNG_DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_descrizione", VistaRendicontazione.model().SNG_DESCRIZIONE.getFieldType()));
				setParameter(object, "setSngDatiAllegati", VistaRendicontazione.model().SNG_DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_dati_allegati", VistaRendicontazione.model().SNG_DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setSngIndiceDati", VistaRendicontazione.model().SNG_INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_indice_dati", VistaRendicontazione.model().SNG_INDICE_DATI.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setSngDescrizioneCausaleRPT", VistaRendicontazione.model().SNG_DESCRIZIONE_CAUSALE_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_descrizione_causale_rpt", VistaRendicontazione.model().SNG_DESCRIZIONE_CAUSALE_RPT.getFieldType()));
				setParameter(object, "setVrsId", VistaRendicontazione.model().VRS_ID.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_id", VistaRendicontazione.model().VRS_ID.getFieldType()));
				setParameter(object, "setVrsCodVersamentoEnte", VistaRendicontazione.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_versamento_ente", VistaRendicontazione.model().VRS_COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setVrsNome", VistaRendicontazione.model().VRS_NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_nome", VistaRendicontazione.model().VRS_NOME.getFieldType()));
				setParameter(object, "setVrsImportoTotale", VistaRendicontazione.model().VRS_IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_totale", VistaRendicontazione.model().VRS_IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setVrsStatoVersamento", VistaRendicontazione.model().VRS_STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_stato_versamento", VistaRendicontazione.model().VRS_STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsDescrizioneStato", VistaRendicontazione.model().VRS_DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_descrizione_stato", VistaRendicontazione.model().VRS_DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setVrsAggiornabile", VistaRendicontazione.model().VRS_AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_aggiornabile", VistaRendicontazione.model().VRS_AGGIORNABILE.getFieldType()));
				setParameter(object, "setVrsDataCreazione", VistaRendicontazione.model().VRS_DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_creazione", VistaRendicontazione.model().VRS_DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setVrsDataValidita", VistaRendicontazione.model().VRS_DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_validita", VistaRendicontazione.model().VRS_DATA_VALIDITA.getFieldType()));
				setParameter(object, "setVrsDataScadenza", VistaRendicontazione.model().VRS_DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_scadenza", VistaRendicontazione.model().VRS_DATA_SCADENZA.getFieldType()));
				setParameter(object, "setVrsDataOraUltimoAgg", VistaRendicontazione.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_ora_ultimo_agg", VistaRendicontazione.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType()));
				setParameter(object, "setVrsCausaleVersamento", VistaRendicontazione.model().VRS_CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_causale_versamento", VistaRendicontazione.model().VRS_CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsDebitoreTipo", VistaRendicontazione.model().VRS_DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_tipo", VistaRendicontazione.model().VRS_DEBITORE_TIPO.getFieldType()));
				setParameter(object, "setVrsDebitoreIdentificativo", VistaRendicontazione.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_identificativo", VistaRendicontazione.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setVrsDebitoreAnagrafica", VistaRendicontazione.model().VRS_DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_anagrafica", VistaRendicontazione.model().VRS_DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setVrsDebitoreIndirizzo", VistaRendicontazione.model().VRS_DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_indirizzo", VistaRendicontazione.model().VRS_DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setVrsDebitoreCivico", VistaRendicontazione.model().VRS_DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_civico", VistaRendicontazione.model().VRS_DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setVrsDebitoreCap", VistaRendicontazione.model().VRS_DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_cap", VistaRendicontazione.model().VRS_DEBITORE_CAP.getFieldType()));
				setParameter(object, "setVrsDebitoreLocalita", VistaRendicontazione.model().VRS_DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_localita", VistaRendicontazione.model().VRS_DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setVrsDebitoreProvincia", VistaRendicontazione.model().VRS_DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_provincia", VistaRendicontazione.model().VRS_DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setVrsDebitoreNazione", VistaRendicontazione.model().VRS_DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_nazione", VistaRendicontazione.model().VRS_DEBITORE_NAZIONE.getFieldType()));
				setParameter(object, "setVrsDebitoreEmail", VistaRendicontazione.model().VRS_DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_email", VistaRendicontazione.model().VRS_DEBITORE_EMAIL.getFieldType()));
				setParameter(object, "setVrsDebitoreTelefono", VistaRendicontazione.model().VRS_DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_telefono", VistaRendicontazione.model().VRS_DEBITORE_TELEFONO.getFieldType()));
				setParameter(object, "setVrsDebitoreCellulare", VistaRendicontazione.model().VRS_DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_cellulare", VistaRendicontazione.model().VRS_DEBITORE_CELLULARE.getFieldType()));
				setParameter(object, "setVrsDebitoreFax", VistaRendicontazione.model().VRS_DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_debitore_fax", VistaRendicontazione.model().VRS_DEBITORE_FAX.getFieldType()));
				setParameter(object, "setVrsTassonomiaAvviso", VistaRendicontazione.model().VRS_TASSONOMIA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tassonomia_avviso", VistaRendicontazione.model().VRS_TASSONOMIA_AVVISO.getFieldType()));
				setParameter(object, "setVrsTassonomia", VistaRendicontazione.model().VRS_TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tassonomia", VistaRendicontazione.model().VRS_TASSONOMIA.getFieldType()));
				setParameter(object, "setVrsCodLotto", VistaRendicontazione.model().VRS_COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_lotto", VistaRendicontazione.model().VRS_COD_LOTTO.getFieldType()));
				setParameter(object, "setVrsCodVersamentoLotto", VistaRendicontazione.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_versamento_lotto", VistaRendicontazione.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType()));
				setParameter(object, "setVrsCodAnnoTributario", VistaRendicontazione.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_anno_tributario", VistaRendicontazione.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType()));
				setParameter(object, "setVrsCodBundlekey", VistaRendicontazione.model().VRS_COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_bundlekey", VistaRendicontazione.model().VRS_COD_BUNDLEKEY.getFieldType()));
				setParameter(object, "setVrsDatiAllegati", VistaRendicontazione.model().VRS_DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_dati_allegati", VistaRendicontazione.model().VRS_DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setVrsIncasso", VistaRendicontazione.model().VRS_INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_incasso", VistaRendicontazione.model().VRS_INCASSO.getFieldType()));
				setParameter(object, "setVrsAnomalie", VistaRendicontazione.model().VRS_ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_anomalie", VistaRendicontazione.model().VRS_ANOMALIE.getFieldType()));
				setParameter(object, "setVrsIuvVersamento", VistaRendicontazione.model().VRS_IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_iuv_versamento", VistaRendicontazione.model().VRS_IUV_VERSAMENTO.getFieldType()));
				setParameter(object, "setVrsNumeroAvviso", VistaRendicontazione.model().VRS_NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_numero_avviso", VistaRendicontazione.model().VRS_NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setVrsAck", VistaRendicontazione.model().VRS_ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_ack", VistaRendicontazione.model().VRS_ACK.getFieldType()));
				setParameter(object, "setVrsAnomalo", VistaRendicontazione.model().VRS_ANOMALO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_anomalo", VistaRendicontazione.model().VRS_ANOMALO.getFieldType()));
				setParameter(object, "setVrsDivisione", VistaRendicontazione.model().VRS_DIVISIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_divisione", VistaRendicontazione.model().VRS_DIVISIONE.getFieldType()));
				setParameter(object, "setVrsDirezione", VistaRendicontazione.model().VRS_DIREZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_direzione", VistaRendicontazione.model().VRS_DIREZIONE.getFieldType()));
				setParameter(object, "setVrsIdSessione", VistaRendicontazione.model().VRS_ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_id_sessione", VistaRendicontazione.model().VRS_ID_SESSIONE.getFieldType()));
				setParameter(object, "setVrsDataPagamento", VistaRendicontazione.model().VRS_DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_data_pagamento", VistaRendicontazione.model().VRS_DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsImportoPagato", VistaRendicontazione.model().VRS_IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_pagato", VistaRendicontazione.model().VRS_IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setVrsImportoIncassato", VistaRendicontazione.model().VRS_IMPORTO_INCASSATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_importo_incassato", VistaRendicontazione.model().VRS_IMPORTO_INCASSATO.getFieldType()));
				setParameter(object, "setVrsStatoPagamento", VistaRendicontazione.model().VRS_STATO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_stato_pagamento", VistaRendicontazione.model().VRS_STATO_PAGAMENTO.getFieldType()));
				setParameter(object, "setVrsIuvPagamento", VistaRendicontazione.model().VRS_IUV_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_iuv_pagamento", VistaRendicontazione.model().VRS_IUV_PAGAMENTO.getFieldType()));
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

			if(model.equals(VistaRendicontazione.model())){
				VistaRendicontazione object = new VistaRendicontazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setFrCodPsp", VistaRendicontazione.model().FR_COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"frCodPsp"));
				setParameter(object, "setFrCodDominio", VistaRendicontazione.model().FR_COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"frCodDominio"));
				setParameter(object, "setFrCodFlusso", VistaRendicontazione.model().FR_COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"frCodFlusso"));
				setParameter(object, "setFrStato", VistaRendicontazione.model().FR_STATO.getFieldType(),
					this.getObjectFromMap(map,"frStato"));
				setParameter(object, "setFrDescrizioneStato", VistaRendicontazione.model().FR_DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"frDescrizioneStato"));
				setParameter(object, "setFrIur", VistaRendicontazione.model().FR_IUR.getFieldType(),
					this.getObjectFromMap(map,"frIur"));
				setParameter(object, "setFrDataOraFlusso", VistaRendicontazione.model().FR_DATA_ORA_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"frDataOraFlusso"));
				setParameter(object, "setFrDataRegolamento", VistaRendicontazione.model().FR_DATA_REGOLAMENTO.getFieldType(),
					this.getObjectFromMap(map,"frDataRegolamento"));
				setParameter(object, "setFrDataAcquisizione", VistaRendicontazione.model().FR_DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"frDataAcquisizione"));
				setParameter(object, "setFrNumeroPagamenti", VistaRendicontazione.model().FR_NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"frNumeroPagamenti"));
				setParameter(object, "setFrImportoTotalePagamenti", VistaRendicontazione.model().FR_IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"frImportoTotalePagamenti"));
				setParameter(object, "setFrCodBicRiversamento", VistaRendicontazione.model().FR_COD_BIC_RIVERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"frCodBicRiversamento"));
				setParameter(object, "setFrId", VistaRendicontazione.model().FR_ID.getFieldType(),
					this.getObjectFromMap(map,"frId"));
				setParameter(object, "setRndIuv", VistaRendicontazione.model().RND_IUV.getFieldType(),
					this.getObjectFromMap(map,"rndIuv"));
				setParameter(object, "setRndIur", VistaRendicontazione.model().RND_IUR.getFieldType(),
					this.getObjectFromMap(map,"rndIur"));
				setParameter(object, "setRndIndiceDati", VistaRendicontazione.model().RND_INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"rndIndiceDati"));
				setParameter(object, "setRndImportoPagato", VistaRendicontazione.model().RND_IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"rndImportoPagato"));
				setParameter(object, "setRndEsito", VistaRendicontazione.model().RND_ESITO.getFieldType(),
					this.getObjectFromMap(map,"rndEsito"));
				setParameter(object, "setRndData", VistaRendicontazione.model().RND_DATA.getFieldType(),
					this.getObjectFromMap(map,"rndData"));
				setParameter(object, "setRndStato", VistaRendicontazione.model().RND_STATO.getFieldType(),
					this.getObjectFromMap(map,"rndStato"));
				setParameter(object, "setRndAnomalie", VistaRendicontazione.model().RND_ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"rndAnomalie"));
				setParameter(object, "setSngCodSingVersEnte", VistaRendicontazione.model().SNG_COD_SING_VERS_ENTE.getFieldType(),
					this.getObjectFromMap(map,"sngCodSingVersEnte"));
				setParameter(object, "setSngStatoSingoloVersamento", VistaRendicontazione.model().SNG_STATO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"sngStatoSingoloVersamento"));
				setParameter(object, "setSngImportoSingoloVersamento", VistaRendicontazione.model().SNG_IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"sngImportoSingoloVersamento"));
				setParameter(object, "setSngDescrizione", VistaRendicontazione.model().SNG_DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"sngDescrizione"));
				setParameter(object, "setSngDatiAllegati", VistaRendicontazione.model().SNG_DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"sngDatiAllegati"));
				setParameter(object, "setSngIndiceDati", VistaRendicontazione.model().SNG_INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"sngIndiceDati"));
				setParameter(object, "setSngDescrizioneCausaleRPT", VistaRendicontazione.model().SNG_DESCRIZIONE_CAUSALE_RPT.getFieldType(),
					this.getObjectFromMap(map,"sngDescrizioneCausaleRPT"));
				setParameter(object, "setVrsId", VistaRendicontazione.model().VRS_ID.getFieldType(),
					this.getObjectFromMap(map,"vrsId"));
				setParameter(object, "setVrsCodVersamentoEnte", VistaRendicontazione.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"vrsCodVersamentoEnte"));
				setParameter(object, "setVrsNome", VistaRendicontazione.model().VRS_NOME.getFieldType(),
					this.getObjectFromMap(map,"vrsNome"));
				setParameter(object, "setVrsImportoTotale", VistaRendicontazione.model().VRS_IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoTotale"));
				setParameter(object, "setVrsStatoVersamento", VistaRendicontazione.model().VRS_STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsStatoVersamento"));
				setParameter(object, "setVrsDescrizioneStato", VistaRendicontazione.model().VRS_DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"vrsDescrizioneStato"));
				setParameter(object, "setVrsAggiornabile", VistaRendicontazione.model().VRS_AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"vrsAggiornabile"));
				setParameter(object, "setVrsDataCreazione", VistaRendicontazione.model().VRS_DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDataCreazione"));
				setParameter(object, "setVrsDataValidita", VistaRendicontazione.model().VRS_DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"vrsDataValidita"));
				setParameter(object, "setVrsDataScadenza", VistaRendicontazione.model().VRS_DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"vrsDataScadenza"));
				setParameter(object, "setVrsDataOraUltimoAgg", VistaRendicontazione.model().VRS_DATA_ORA_ULTIMO_AGG.getFieldType(),
					this.getObjectFromMap(map,"vrsDataOraUltimoAgg"));
				setParameter(object, "setVrsCausaleVersamento", VistaRendicontazione.model().VRS_CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCausaleVersamento"));
				setParameter(object, "setVrsDebitoreTipo", VistaRendicontazione.model().VRS_DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreTipo"));
				setParameter(object, "setVrsDebitoreIdentificativo", VistaRendicontazione.model().VRS_DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreIdentificativo"));
				setParameter(object, "setVrsDebitoreAnagrafica", VistaRendicontazione.model().VRS_DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreAnagrafica"));
				setParameter(object, "setVrsDebitoreIndirizzo", VistaRendicontazione.model().VRS_DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreIndirizzo"));
				setParameter(object, "setVrsDebitoreCivico", VistaRendicontazione.model().VRS_DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCivico"));
				setParameter(object, "setVrsDebitoreCap", VistaRendicontazione.model().VRS_DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCap"));
				setParameter(object, "setVrsDebitoreLocalita", VistaRendicontazione.model().VRS_DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreLocalita"));
				setParameter(object, "setVrsDebitoreProvincia", VistaRendicontazione.model().VRS_DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreProvincia"));
				setParameter(object, "setVrsDebitoreNazione", VistaRendicontazione.model().VRS_DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreNazione"));
				setParameter(object, "setVrsDebitoreEmail", VistaRendicontazione.model().VRS_DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreEmail"));
				setParameter(object, "setVrsDebitoreTelefono", VistaRendicontazione.model().VRS_DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreTelefono"));
				setParameter(object, "setVrsDebitoreCellulare", VistaRendicontazione.model().VRS_DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreCellulare"));
				setParameter(object, "setVrsDebitoreFax", VistaRendicontazione.model().VRS_DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"vrsDebitoreFax"));
				setParameter(object, "setVrsTassonomiaAvviso", VistaRendicontazione.model().VRS_TASSONOMIA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"vrsTassonomiaAvviso"));
				setParameter(object, "setVrsTassonomia", VistaRendicontazione.model().VRS_TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"vrsTassonomia"));
				setParameter(object, "setVrsCodLotto", VistaRendicontazione.model().VRS_COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodLotto"));
				setParameter(object, "setVrsCodVersamentoLotto", VistaRendicontazione.model().VRS_COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodVersamentoLotto"));
				setParameter(object, "setVrsCodAnnoTributario", VistaRendicontazione.model().VRS_COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"vrsCodAnnoTributario"));
				setParameter(object, "setVrsCodBundlekey", VistaRendicontazione.model().VRS_COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"vrsCodBundlekey"));
				setParameter(object, "setVrsDatiAllegati", VistaRendicontazione.model().VRS_DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"vrsDatiAllegati"));
				setParameter(object, "setVrsIncasso", VistaRendicontazione.model().VRS_INCASSO.getFieldType(),
					this.getObjectFromMap(map,"vrsIncasso"));
				setParameter(object, "setVrsAnomalie", VistaRendicontazione.model().VRS_ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"vrsAnomalie"));
				setParameter(object, "setVrsIuvVersamento", VistaRendicontazione.model().VRS_IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsIuvVersamento"));
				setParameter(object, "setVrsNumeroAvviso", VistaRendicontazione.model().VRS_NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"vrsNumeroAvviso"));
				setParameter(object, "setVrsAck", VistaRendicontazione.model().VRS_ACK.getFieldType(),
					this.getObjectFromMap(map,"vrsAck"));
				setParameter(object, "setVrsAnomalo", VistaRendicontazione.model().VRS_ANOMALO.getFieldType(),
					this.getObjectFromMap(map,"vrsAnomalo"));
				setParameter(object, "setVrsDivisione", VistaRendicontazione.model().VRS_DIVISIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDivisione"));
				setParameter(object, "setVrsDirezione", VistaRendicontazione.model().VRS_DIREZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDirezione"));
				setParameter(object, "setVrsIdSessione", VistaRendicontazione.model().VRS_ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsIdSessione"));
				setParameter(object, "setVrsDataPagamento", VistaRendicontazione.model().VRS_DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsDataPagamento"));
				setParameter(object, "setVrsImportoPagato", VistaRendicontazione.model().VRS_IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoPagato"));
				setParameter(object, "setVrsImportoIncassato", VistaRendicontazione.model().VRS_IMPORTO_INCASSATO.getFieldType(),
					this.getObjectFromMap(map,"vrsImportoIncassato"));
				setParameter(object, "setVrsStatoPagamento", VistaRendicontazione.model().VRS_STATO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsStatoPagamento"));
				setParameter(object, "setVrsIuvPagamento", VistaRendicontazione.model().VRS_IUV_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"vrsIuvPagamento"));
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

			if(model.equals(VistaRendicontazione.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_rendicontazioni_ext","id","seq_v_rendicontazioni_ext","v_rendicontazioni_ext_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
