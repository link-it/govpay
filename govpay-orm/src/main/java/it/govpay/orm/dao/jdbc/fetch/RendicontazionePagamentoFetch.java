/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import it.govpay.orm.FrApplicazione;
import it.govpay.orm.FR;
import it.govpay.orm.RPT;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.RendicontazionePagamento;
import it.govpay.orm.Pagamento;


/**     
 * RendicontazionePagamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazionePagamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(RendicontazionePagamento.model())){
				RendicontazionePagamento object = new RendicontazionePagamento();
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().FR)){
				FR object = new FR();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodFlusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType()));
				setParameter(object, "setStato", RendicontazionePagamento.model().FR.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamento.model().FR.STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setIur", RendicontazionePagamento.model().FR.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().FR.IUR.getFieldType()));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().FR.ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", RendicontazionePagamento.model().FR.ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setDataOraFlusso", RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_flusso", RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO.getFieldType()));
				setParameter(object, "setDataRegolamento", RendicontazionePagamento.model().FR.DATA_REGOLAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_regolamento", RendicontazionePagamento.model().FR.DATA_REGOLAMENTO.getFieldType()));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				setParameter(object, "setCodBicRiversamento", RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bic_riversamento", RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO.getFieldType()));
				setParameter(object, "setXml", RendicontazionePagamento.model().FR.XML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml", RendicontazionePagamento.model().FR.XML.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().FR_APPLICAZIONE)){
				FrApplicazione object = new FrApplicazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamento.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", RendicontazionePagamento.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamento.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", RendicontazionePagamento.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamento.model().PAGAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", RendicontazionePagamento.model().PAGAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setImportoPagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setIur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType()));
				setParameter(object, "setDataPagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setCommissioniPsp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "commissioni_psp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType()));
				setParameter(object, "setTipoAllegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_allegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType()));
				setParameter(object, "setAllegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType()));
				setParameter(object, "setRendicontazioneEsito", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_esito", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_ESITO.getFieldType()));
				setParameter(object, "setRendicontazioneData", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_data", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_DATA.getFieldType()));
				setParameter(object, "setCodflussoRendicontazione", RendicontazionePagamento.model().PAGAMENTO.CODFLUSSO_RENDICONTAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codflusso_rendicontazione", RendicontazionePagamento.model().PAGAMENTO.CODFLUSSO_RENDICONTAZIONE.getFieldType()));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().PAGAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", RendicontazionePagamento.model().PAGAMENTO.ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setIndiceSingoloPagamento", RendicontazionePagamento.model().PAGAMENTO.INDICE_SINGOLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_singolo_pagamento", RendicontazionePagamento.model().PAGAMENTO.INDICE_SINGOLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setDataAcquisizioneRevoca", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione_revoca", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA.getFieldType()));
				setParameter(object, "setCausaleRevoca", RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_revoca", RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA.getFieldType()));
				setParameter(object, "setDatiRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_revoca", RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA.getFieldType()));
				setParameter(object, "setImportoRevocato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_revocato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO.getFieldType()));
				setParameter(object, "setEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito_revoca", RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA.getFieldType()));
				setParameter(object, "setDatiEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_esito_revoca", RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setRendicontazioneEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_esito_revoca", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setRendicontazioneDataRevoca", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_DATA_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_data_revoca", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_DATA_REVOCA.getFieldType()));
				setParameter(object, "setCodFlussoRendicontazioneRevoca", RendicontazionePagamento.model().PAGAMENTO.COD_FLUSSO_RENDICONTAZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso_rendicontaz_revoca", RendicontazionePagamento.model().PAGAMENTO.COD_FLUSSO_RENDICONTAZIONE_REVOCA.getFieldType()));
				setParameter(object, "setAnnoRiferimentoRevoca", RendicontazionePagamento.model().PAGAMENTO.ANNO_RIFERIMENTO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento_revoca", RendicontazionePagamento.model().PAGAMENTO.ANNO_RIFERIMENTO_REVOCA.getFieldType()));
				setParameter(object, "setIndiceSingoloPagamentoRevoca", RendicontazionePagamento.model().PAGAMENTO.INDICE_SINGOLO_PAGAMENTO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ind_singolo_pagamento_revoca", RendicontazionePagamento.model().PAGAMENTO.INDICE_SINGOLO_PAGAMENTO_REVOCA.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)){
				SingoloVersamento object = new SingoloVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setStatoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_singolo_versamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setImportoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_singolo_versamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setTipoBollo", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_bollo", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType()));
				setParameter(object, "setHashDocumento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "hash_documento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType()));
				setParameter(object, "setProvinciaResidenza", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "provincia_residenza", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType()));
				setParameter(object, "setTipoContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_contabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType()));
				setParameter(object, "setCodiceContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_contabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType()));
				setParameter(object, "setNote", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.NOTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "note", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.NOTE.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().VERSAMENTO)){
				Versamento object = new Versamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodVersamentoEnte", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setImportoTotale", RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setStatoVersamento", RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setAggiornabile", RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornabile", RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE.getFieldType()));
				setParameter(object, "setDataCreazione", RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setDataScadenza", RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA.getFieldType()));
				setParameter(object, "setDataOraUltimoAggiornamento", RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultimo_aggiornamento", RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				setParameter(object, "setCausaleVersamento", RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setDebitoreIdentificativo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setDebitoreAnagrafica", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setDebitoreIndirizzo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_indirizzo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setDebitoreCivico", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_civico", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setDebitoreCap", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cap", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP.getFieldType()));
				setParameter(object, "setDebitoreLocalita", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_localita", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setDebitoreProvincia", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_provincia", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setDebitoreNazione", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_nazione", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType()));
				setParameter(object, "setCodLotto", RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_lotto", RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO.getFieldType()));
				setParameter(object, "setCodVersamentoLotto", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_lotto", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType()));
				setParameter(object, "setCodAnnoTributario", RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_anno_tributario", RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType()));
				setParameter(object, "setCodBundlekey", RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bundlekey", RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().RPT)){
				RPT object = new RPT();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodCarrello", RendicontazionePagamento.model().RPT.COD_CARRELLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_carrello", RendicontazionePagamento.model().RPT.COD_CARRELLO.getFieldType()));
				setParameter(object, "setIuv", RendicontazionePagamento.model().RPT.IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RendicontazionePagamento.model().RPT.IUV.getFieldType()));
				setParameter(object, "setCcp", RendicontazionePagamento.model().RPT.CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", RendicontazionePagamento.model().RPT.CCP.getFieldType()));
				setParameter(object, "setCodDominio", RendicontazionePagamento.model().RPT.COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RendicontazionePagamento.model().RPT.COD_DOMINIO.getFieldType()));
				setParameter(object, "setCodMsgRichiesta", RendicontazionePagamento.model().RPT.COD_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_richiesta", RendicontazionePagamento.model().RPT.COD_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setDataMsgRichiesta", RendicontazionePagamento.model().RPT.DATA_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_richiesta", RendicontazionePagamento.model().RPT.DATA_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setStato", RendicontazionePagamento.model().RPT.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamento.model().RPT.STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().RPT.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamento.model().RPT.DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setCodSessione", RendicontazionePagamento.model().RPT.COD_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione", RendicontazionePagamento.model().RPT.COD_SESSIONE.getFieldType()));
				setParameter(object, "setPspRedirectURL", RendicontazionePagamento.model().RPT.PSP_REDIRECT_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_redirect_url", RendicontazionePagamento.model().RPT.PSP_REDIRECT_URL.getFieldType()));
				setParameter(object, "setXmlRPT", RendicontazionePagamento.model().RPT.XML_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rpt", RendicontazionePagamento.model().RPT.XML_RPT.getFieldType()));
				setParameter(object, "setDataAggiornamentoStato", RendicontazionePagamento.model().RPT.DATA_AGGIORNAMENTO_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_aggiornamento_stato", RendicontazionePagamento.model().RPT.DATA_AGGIORNAMENTO_STATO.getFieldType()));
				setParameter(object, "setCallbackURL", RendicontazionePagamento.model().RPT.CALLBACK_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "callback_url", RendicontazionePagamento.model().RPT.CALLBACK_URL.getFieldType()));
				setParameter(object, "setModelloPagamento", RendicontazionePagamento.model().RPT.MODELLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "modello_pagamento", RendicontazionePagamento.model().RPT.MODELLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setCodMsgRicevuta", RendicontazionePagamento.model().RPT.COD_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_ricevuta", RendicontazionePagamento.model().RPT.COD_MSG_RICEVUTA.getFieldType()));
				setParameter(object, "setDataMsgRicevuta", RendicontazionePagamento.model().RPT.DATA_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_ricevuta", RendicontazionePagamento.model().RPT.DATA_MSG_RICEVUTA.getFieldType()));
				setParameter(object, "setFirmaRicevuta", RendicontazionePagamento.model().RPT.FIRMA_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "firma_ricevuta", RendicontazionePagamento.model().RPT.FIRMA_RICEVUTA.getFieldType()));
				setParameter(object, "setCodEsitoPagamento", RendicontazionePagamento.model().RPT.COD_ESITO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_esito_pagamento", RendicontazionePagamento.model().RPT.COD_ESITO_PAGAMENTO.getFieldType()));
				setParameter(object, "setImportoTotalePagato", RendicontazionePagamento.model().RPT.IMPORTO_TOTALE_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagato", RendicontazionePagamento.model().RPT.IMPORTO_TOTALE_PAGATO.getFieldType()));
				setParameter(object, "setXmlRT", RendicontazionePagamento.model().RPT.XML_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rt", RendicontazionePagamento.model().RPT.XML_RT.getFieldType()));
				setParameter(object, "setCodStazione", RendicontazionePagamento.model().RPT.COD_STAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_stazione", RendicontazionePagamento.model().RPT.COD_STAZIONE.getFieldType()));
				setParameter(object, "setCodTransazioneRPT", RendicontazionePagamento.model().RPT.COD_TRANSAZIONE_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rpt", RendicontazionePagamento.model().RPT.COD_TRANSAZIONE_RPT.getFieldType()));
				setParameter(object, "setCodTransazioneRT", RendicontazionePagamento.model().RPT.COD_TRANSAZIONE_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rt", RendicontazionePagamento.model().RPT.COD_TRANSAZIONE_RT.getFieldType()));
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

			if(model.equals(RendicontazionePagamento.model())){
				RendicontazionePagamento object = new RendicontazionePagamento();
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().FR)){
				FR object = new FR();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"FR.id"));
				setParameter(object, "setCodFlusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.codFlusso"));
				setParameter(object, "setStato", RendicontazionePagamento.model().FR.STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.stato"));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.descrizioneStato"));
				setParameter(object, "setIur", RendicontazionePagamento.model().FR.IUR.getFieldType(),
					this.getObjectFromMap(map,"FR.iur"));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().FR.ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.annoRiferimento"));
				setParameter(object, "setDataOraFlusso", RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataOraFlusso"));
				setParameter(object, "setDataRegolamento", RendicontazionePagamento.model().FR.DATA_REGOLAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataRegolamento"));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"FR.dataAcquisizione"));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.numeroPagamenti"));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.importoTotalePagamenti"));
				setParameter(object, "setCodBicRiversamento", RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.codBicRiversamento"));
				setParameter(object, "setXml", RendicontazionePagamento.model().FR.XML.getFieldType(),
					this.getObjectFromMap(map,"FR.xml"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().FR_APPLICAZIONE)){
				FrApplicazione object = new FrApplicazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"FrApplicazione.id"));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamento.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FrApplicazione.numeroPagamenti"));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamento.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FrApplicazione.importoTotalePagamenti"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Pagamento.id"));
				setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamento.model().PAGAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.codSingoloVersamentoEnte"));
				setParameter(object, "setImportoPagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.importoPagato"));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataAcquisizione"));
				setParameter(object, "setIur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.iur"));
				setParameter(object, "setDataPagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataPagamento"));
				setParameter(object, "setCommissioniPsp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.commissioniPsp"));
				setParameter(object, "setTipoAllegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.tipoAllegato"));
				setParameter(object, "setAllegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.allegato"));
				setParameter(object, "setRendicontazioneEsito", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_ESITO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.rendicontazioneEsito"));
				setParameter(object, "setRendicontazioneData", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_DATA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.rendicontazioneData"));
				setParameter(object, "setCodflussoRendicontazione", RendicontazionePagamento.model().PAGAMENTO.CODFLUSSO_RENDICONTAZIONE.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.codflussoRendicontazione"));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().PAGAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.annoRiferimento"));
				setParameter(object, "setIndiceSingoloPagamento", RendicontazionePagamento.model().PAGAMENTO.INDICE_SINGOLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.indiceSingoloPagamento"));
				setParameter(object, "setDataAcquisizioneRevoca", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataAcquisizioneRevoca"));
				setParameter(object, "setCausaleRevoca", RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.causaleRevoca"));
				setParameter(object, "setDatiRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.datiRevoca"));
				setParameter(object, "setImportoRevocato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.importoRevocato"));
				setParameter(object, "setEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.esitoRevoca"));
				setParameter(object, "setDatiEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.datiEsitoRevoca"));
				setParameter(object, "setRendicontazioneEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.rendicontazioneEsitoRevoca"));
				setParameter(object, "setRendicontazioneDataRevoca", RendicontazionePagamento.model().PAGAMENTO.RENDICONTAZIONE_DATA_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.rendicontazioneDataRevoca"));
				setParameter(object, "setCodFlussoRendicontazioneRevoca", RendicontazionePagamento.model().PAGAMENTO.COD_FLUSSO_RENDICONTAZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.codFlussoRendicontazioneRevoca"));
				setParameter(object, "setAnnoRiferimentoRevoca", RendicontazionePagamento.model().PAGAMENTO.ANNO_RIFERIMENTO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.annoRiferimentoRevoca"));
				setParameter(object, "setIndiceSingoloPagamentoRevoca", RendicontazionePagamento.model().PAGAMENTO.INDICE_SINGOLO_PAGAMENTO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.indiceSingoloPagamentoRevoca"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)){
				SingoloVersamento object = new SingoloVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"SingoloVersamento.id"));
				setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.codSingoloVersamentoEnte"));
				setParameter(object, "setStatoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.statoSingoloVersamento"));
				setParameter(object, "setImportoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.importoSingoloVersamento"));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.annoRiferimento"));
				setParameter(object, "setTipoBollo", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.tipoBollo"));
				setParameter(object, "setHashDocumento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.hashDocumento"));
				setParameter(object, "setProvinciaResidenza", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.provinciaResidenza"));
				setParameter(object, "setTipoContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.tipoContabilita"));
				setParameter(object, "setCodiceContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.codiceContabilita"));
				setParameter(object, "setNote", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.NOTE.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.note"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().VERSAMENTO)){
				Versamento object = new Versamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Versamento.id"));
				setParameter(object, "setCodVersamentoEnte", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codVersamentoEnte"));
				setParameter(object, "setImportoTotale", RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.importoTotale"));
				setParameter(object, "setStatoVersamento", RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.statoVersamento"));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.descrizioneStato"));
				setParameter(object, "setAggiornabile", RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.aggiornabile"));
				setParameter(object, "setDataCreazione", RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataCreazione"));
				setParameter(object, "setDataScadenza", RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataScadenza"));
				setParameter(object, "setDataOraUltimoAggiornamento", RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataOraUltimoAggiornamento"));
				setParameter(object, "setCausaleVersamento", RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.causaleVersamento"));
				setParameter(object, "setDebitoreIdentificativo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreIdentificativo"));
				setParameter(object, "setDebitoreAnagrafica", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreAnagrafica"));
				setParameter(object, "setDebitoreIndirizzo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreIndirizzo"));
				setParameter(object, "setDebitoreCivico", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCivico"));
				setParameter(object, "setDebitoreCap", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCap"));
				setParameter(object, "setDebitoreLocalita", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreLocalita"));
				setParameter(object, "setDebitoreProvincia", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreProvincia"));
				setParameter(object, "setDebitoreNazione", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreNazione"));
				setParameter(object, "setCodLotto", RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codLotto"));
				setParameter(object, "setCodVersamentoLotto", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codVersamentoLotto"));
				setParameter(object, "setCodAnnoTributario", RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codAnnoTributario"));
				setParameter(object, "setCodBundlekey", RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codBundlekey"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().RPT)){
				RPT object = new RPT();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"RPT.id"));
				setParameter(object, "setCodCarrello", RendicontazionePagamento.model().RPT.COD_CARRELLO.getFieldType(),
					this.getObjectFromMap(map,"RPT.codCarrello"));
				setParameter(object, "setIuv", RendicontazionePagamento.model().RPT.IUV.getFieldType(),
					this.getObjectFromMap(map,"RPT.iuv"));
				setParameter(object, "setCcp", RendicontazionePagamento.model().RPT.CCP.getFieldType(),
					this.getObjectFromMap(map,"RPT.ccp"));
				setParameter(object, "setCodDominio", RendicontazionePagamento.model().RPT.COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"RPT.codDominio"));
				setParameter(object, "setCodMsgRichiesta", RendicontazionePagamento.model().RPT.COD_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"RPT.codMsgRichiesta"));
				setParameter(object, "setDataMsgRichiesta", RendicontazionePagamento.model().RPT.DATA_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"RPT.dataMsgRichiesta"));
				setParameter(object, "setStato", RendicontazionePagamento.model().RPT.STATO.getFieldType(),
					this.getObjectFromMap(map,"RPT.stato"));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().RPT.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"RPT.descrizioneStato"));
				setParameter(object, "setCodSessione", RendicontazionePagamento.model().RPT.COD_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"RPT.codSessione"));
				setParameter(object, "setPspRedirectURL", RendicontazionePagamento.model().RPT.PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"RPT.pspRedirectURL"));
				setParameter(object, "setXmlRPT", RendicontazionePagamento.model().RPT.XML_RPT.getFieldType(),
					this.getObjectFromMap(map,"RPT.xmlRPT"));
				setParameter(object, "setDataAggiornamentoStato", RendicontazionePagamento.model().RPT.DATA_AGGIORNAMENTO_STATO.getFieldType(),
					this.getObjectFromMap(map,"RPT.dataAggiornamentoStato"));
				setParameter(object, "setCallbackURL", RendicontazionePagamento.model().RPT.CALLBACK_URL.getFieldType(),
					this.getObjectFromMap(map,"RPT.callbackURL"));
				setParameter(object, "setModelloPagamento", RendicontazionePagamento.model().RPT.MODELLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"RPT.modelloPagamento"));
				setParameter(object, "setCodMsgRicevuta", RendicontazionePagamento.model().RPT.COD_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"RPT.codMsgRicevuta"));
				setParameter(object, "setDataMsgRicevuta", RendicontazionePagamento.model().RPT.DATA_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"RPT.dataMsgRicevuta"));
				setParameter(object, "setFirmaRicevuta", RendicontazionePagamento.model().RPT.FIRMA_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"RPT.firmaRicevuta"));
				setParameter(object, "setCodEsitoPagamento", RendicontazionePagamento.model().RPT.COD_ESITO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"RPT.codEsitoPagamento"));
				setParameter(object, "setImportoTotalePagato", RendicontazionePagamento.model().RPT.IMPORTO_TOTALE_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"RPT.importoTotalePagato"));
				setParameter(object, "setXmlRT", RendicontazionePagamento.model().RPT.XML_RT.getFieldType(),
					this.getObjectFromMap(map,"RPT.xmlRT"));
				setParameter(object, "setCodStazione", RendicontazionePagamento.model().RPT.COD_STAZIONE.getFieldType(),
					this.getObjectFromMap(map,"RPT.codStazione"));
				setParameter(object, "setCodTransazioneRPT", RendicontazionePagamento.model().RPT.COD_TRANSAZIONE_RPT.getFieldType(),
					this.getObjectFromMap(map,"RPT.codTransazioneRPT"));
				setParameter(object, "setCodTransazioneRT", RendicontazionePagamento.model().RPT.COD_TRANSAZIONE_RT.getFieldType(),
					this.getObjectFromMap(map,"RPT.codTransazioneRT"));
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

			if(model.equals(RendicontazionePagamento.model().FR)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr","id","seq_fr","fr_init_seq");
			}
			if(model.equals(RendicontazionePagamento.model().FR_APPLICAZIONE)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr_applicazioni","id","seq_fr_applicazioni","fr_applicazioni_init_seq");
			}
			if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("pagamenti","id","seq_pagamenti","pagamenti_init_seq");
			}
			if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("singoli_versamenti","id","seq_singoli_versamenti","singoli_versamenti_init_seq");
			}
			if(model.equals(RendicontazionePagamento.model().VERSAMENTO)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("versamenti","id","seq_versamenti","versamenti_init_seq");
			}
			if(model.equals(RendicontazionePagamento.model().RPT)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("rpt","id","seq_rpt","rpt_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
