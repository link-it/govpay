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

import it.govpay.orm.FrApplicazione;
import it.govpay.orm.FR;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.RendicontazionePagamentoSenzaRPT;
import it.govpay.orm.Versamento;
import it.govpay.orm.RendicontazioneSenzaRPT;


/**     
 * RendicontazionePagamentoSenzaRPTFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazionePagamentoSenzaRPTFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(RendicontazionePagamentoSenzaRPT.model())){
				RendicontazionePagamentoSenzaRPT object = new RendicontazionePagamentoSenzaRPT();
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR)){
				FR object = new FR();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodFlusso", RendicontazionePagamentoSenzaRPT.model().FR.COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso", RendicontazionePagamentoSenzaRPT.model().FR.COD_FLUSSO.getFieldType()));
				setParameter(object, "setStato", RendicontazionePagamentoSenzaRPT.model().FR.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamentoSenzaRPT.model().FR.STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamentoSenzaRPT.model().FR.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamentoSenzaRPT.model().FR.DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setIur", RendicontazionePagamentoSenzaRPT.model().FR.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamentoSenzaRPT.model().FR.IUR.getFieldType()));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamentoSenzaRPT.model().FR.ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", RendicontazionePagamentoSenzaRPT.model().FR.ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setDataOraFlusso", RendicontazionePagamentoSenzaRPT.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_flusso", RendicontazionePagamentoSenzaRPT.model().FR.DATA_ORA_FLUSSO.getFieldType()));
				setParameter(object, "setDataRegolamento", RendicontazionePagamentoSenzaRPT.model().FR.DATA_REGOLAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_regolamento", RendicontazionePagamentoSenzaRPT.model().FR.DATA_REGOLAMENTO.getFieldType()));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamentoSenzaRPT.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", RendicontazionePagamentoSenzaRPT.model().FR.DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamentoSenzaRPT.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", RendicontazionePagamentoSenzaRPT.model().FR.NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamentoSenzaRPT.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", RendicontazionePagamentoSenzaRPT.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				setParameter(object, "setCodBicRiversamento", RendicontazionePagamentoSenzaRPT.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bic_riversamento", RendicontazionePagamentoSenzaRPT.model().FR.COD_BIC_RIVERSAMENTO.getFieldType()));
				setParameter(object, "setXml", RendicontazionePagamentoSenzaRPT.model().FR.XML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml", RendicontazionePagamentoSenzaRPT.model().FR.XML.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE)){
				FrApplicazione object = new FrApplicazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT)){
				RendicontazioneSenzaRPT object = new RendicontazioneSenzaRPT();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setImportoPagato", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setIur", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IUR.getFieldType()));
				setParameter(object, "setRendicontazioneData", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.RENDICONTAZIONE_DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_data", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.RENDICONTAZIONE_DATA.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO)){
				SingoloVersamento object = new SingoloVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setStatoSingoloVersamento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_singolo_versamento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setImportoSingoloVersamento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_singolo_versamento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setTipoBollo", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_bollo", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType()));
				setParameter(object, "setHashDocumento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "hash_documento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType()));
				setParameter(object, "setProvinciaResidenza", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "provincia_residenza", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType()));
				setParameter(object, "setTipoContabilita", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_contabilita", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType()));
				setParameter(object, "setCodiceContabilita", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_contabilita", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType()));
				setParameter(object, "setNote", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.NOTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "note", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.NOTE.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO)){
				Versamento object = new Versamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodVersamentoEnte", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setImportoTotale", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setStatoVersamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setAggiornabile", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornabile", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.AGGIORNABILE.getFieldType()));
				setParameter(object, "setDataCreazione", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setDataScadenza", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_SCADENZA.getFieldType()));
				setParameter(object, "setDataOraUltimoAggiornamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultimo_aggiornamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				setParameter(object, "setCausaleVersamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setDebitoreIdentificativo", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setDebitoreAnagrafica", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setDebitoreIndirizzo", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_indirizzo", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setDebitoreCivico", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_civico", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setDebitoreCap", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cap", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CAP.getFieldType()));
				setParameter(object, "setDebitoreLocalita", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_localita", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setDebitoreProvincia", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_provincia", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setDebitoreNazione", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_nazione", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType()));
				setParameter(object, "setCodLotto", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_lotto", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_LOTTO.getFieldType()));
				setParameter(object, "setCodVersamentoLotto", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_lotto", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType()));
				setParameter(object, "setCodAnnoTributario", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_anno_tributario", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType()));
				setParameter(object, "setCodBundlekey", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bundlekey", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType()));
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

			if(model.equals(RendicontazionePagamentoSenzaRPT.model())){
				RendicontazionePagamentoSenzaRPT object = new RendicontazionePagamentoSenzaRPT();
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR)){
				FR object = new FR();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"FR.id"));
				setParameter(object, "setCodFlusso", RendicontazionePagamentoSenzaRPT.model().FR.COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.codFlusso"));
				setParameter(object, "setStato", RendicontazionePagamentoSenzaRPT.model().FR.STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.stato"));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamentoSenzaRPT.model().FR.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.descrizioneStato"));
				setParameter(object, "setIur", RendicontazionePagamentoSenzaRPT.model().FR.IUR.getFieldType(),
					this.getObjectFromMap(map,"FR.iur"));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamentoSenzaRPT.model().FR.ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.annoRiferimento"));
				setParameter(object, "setDataOraFlusso", RendicontazionePagamentoSenzaRPT.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataOraFlusso"));
				setParameter(object, "setDataRegolamento", RendicontazionePagamentoSenzaRPT.model().FR.DATA_REGOLAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataRegolamento"));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamentoSenzaRPT.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"FR.dataAcquisizione"));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamentoSenzaRPT.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.numeroPagamenti"));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamentoSenzaRPT.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.importoTotalePagamenti"));
				setParameter(object, "setCodBicRiversamento", RendicontazionePagamentoSenzaRPT.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.codBicRiversamento"));
				setParameter(object, "setXml", RendicontazionePagamentoSenzaRPT.model().FR.XML.getFieldType(),
					this.getObjectFromMap(map,"FR.xml"));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE)){
				FrApplicazione object = new FrApplicazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"FrApplicazione.id"));
				setParameter(object, "setNumeroPagamenti", RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FrApplicazione.numeroPagamenti"));
				setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FrApplicazione.importoTotalePagamenti"));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT)){
				RendicontazioneSenzaRPT object = new RendicontazioneSenzaRPT();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"RendicontazioneSenzaRPT.id"));
				setParameter(object, "setImportoPagato", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"RendicontazioneSenzaRPT.importoPagato"));
				setParameter(object, "setIur", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IUR.getFieldType(),
					this.getObjectFromMap(map,"RendicontazioneSenzaRPT.iur"));
				setParameter(object, "setRendicontazioneData", RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.RENDICONTAZIONE_DATA.getFieldType(),
					this.getObjectFromMap(map,"RendicontazioneSenzaRPT.rendicontazioneData"));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO)){
				SingoloVersamento object = new SingoloVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"SingoloVersamento.id"));
				setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.codSingoloVersamentoEnte"));
				setParameter(object, "setStatoSingoloVersamento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.statoSingoloVersamento"));
				setParameter(object, "setImportoSingoloVersamento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.importoSingoloVersamento"));
				setParameter(object, "setAnnoRiferimento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.annoRiferimento"));
				setParameter(object, "setTipoBollo", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.tipoBollo"));
				setParameter(object, "setHashDocumento", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.hashDocumento"));
				setParameter(object, "setProvinciaResidenza", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.provinciaResidenza"));
				setParameter(object, "setTipoContabilita", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.tipoContabilita"));
				setParameter(object, "setCodiceContabilita", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.codiceContabilita"));
				setParameter(object, "setNote", RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.NOTE.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.note"));
				return object;
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO)){
				Versamento object = new Versamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Versamento.id"));
				setParameter(object, "setCodVersamentoEnte", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codVersamentoEnte"));
				setParameter(object, "setImportoTotale", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.importoTotale"));
				setParameter(object, "setStatoVersamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.statoVersamento"));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.descrizioneStato"));
				setParameter(object, "setAggiornabile", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.aggiornabile"));
				setParameter(object, "setDataCreazione", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataCreazione"));
				setParameter(object, "setDataScadenza", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataScadenza"));
				setParameter(object, "setDataOraUltimoAggiornamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataOraUltimoAggiornamento"));
				setParameter(object, "setCausaleVersamento", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.causaleVersamento"));
				setParameter(object, "setDebitoreIdentificativo", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreIdentificativo"));
				setParameter(object, "setDebitoreAnagrafica", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreAnagrafica"));
				setParameter(object, "setDebitoreIndirizzo", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreIndirizzo"));
				setParameter(object, "setDebitoreCivico", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCivico"));
				setParameter(object, "setDebitoreCap", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCap"));
				setParameter(object, "setDebitoreLocalita", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreLocalita"));
				setParameter(object, "setDebitoreProvincia", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreProvincia"));
				setParameter(object, "setDebitoreNazione", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreNazione"));
				setParameter(object, "setCodLotto", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codLotto"));
				setParameter(object, "setCodVersamentoLotto", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codVersamentoLotto"));
				setParameter(object, "setCodAnnoTributario", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codAnnoTributario"));
				setParameter(object, "setCodBundlekey", RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codBundlekey"));
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

			if(model.equals(RendicontazionePagamentoSenzaRPT.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr","id","seq_rendicontazione_pagamento","rendicontazione_pagamento_init_seq");
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr","id","seq_fr","fr_init_seq");
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr_applicazioni","id","seq_fr_applicazioni","fr_applicazioni_init_seq");
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("rendicontazioni_senza_rpt","id","seq_rendicontazioni_senza_rpt","rendicontazioni_senza_rpt_init_seq");
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("singoli_versamenti","id","seq_singoli_versamenti","singoli_versamenti_init_seq");
			}
			if(model.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("versamenti","id","seq_versamenti","versamenti_init_seq");
			}
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
