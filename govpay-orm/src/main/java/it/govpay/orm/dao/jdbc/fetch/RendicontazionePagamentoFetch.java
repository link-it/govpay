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

import it.govpay.orm.FR;
import it.govpay.orm.Rendicontazione;
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
				setParameter(object, "setCodPsp", RendicontazionePagamento.model().FR.COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", RendicontazionePagamento.model().FR.COD_PSP.getFieldType()));
				setParameter(object, "setCodDominio", RendicontazionePagamento.model().FR.COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RendicontazionePagamento.model().FR.COD_DOMINIO.getFieldType()));
				setParameter(object, "setCodFlusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType()));
				setParameter(object, "setStato", RendicontazionePagamento.model().FR.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamento.model().FR.STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setIur", RendicontazionePagamento.model().FR.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().FR.IUR.getFieldType()));
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
			if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE)){
				Rendicontazione object = new Rendicontazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setIuv", RendicontazionePagamento.model().RENDICONTAZIONE.IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RendicontazionePagamento.model().RENDICONTAZIONE.IUV.getFieldType()));
				setParameter(object, "setIur", RendicontazionePagamento.model().RENDICONTAZIONE.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().RENDICONTAZIONE.IUR.getFieldType()));
				setParameter(object, "setImportoPagato", RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setEsito", RendicontazionePagamento.model().RENDICONTAZIONE.ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito", RendicontazionePagamento.model().RENDICONTAZIONE.ESITO.getFieldType(), org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setData", RendicontazionePagamento.model().RENDICONTAZIONE.DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data", RendicontazionePagamento.model().RENDICONTAZIONE.DATA.getFieldType()));
				setParameter(object, "setStato", RendicontazionePagamento.model().RENDICONTAZIONE.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamento.model().RENDICONTAZIONE.STATO.getFieldType()));
				setParameter(object, "setAnomalie", RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalie", RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", RendicontazionePagamento.model().PAGAMENTO.IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RendicontazionePagamento.model().PAGAMENTO.IUV.getFieldType()));
				setParameter(object, "setImportoPagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setIur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType()));
				setParameter(object, "setDataPagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setIbanAccredito", RendicontazionePagamento.model().PAGAMENTO.IBAN_ACCREDITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iban_accredito", RendicontazionePagamento.model().PAGAMENTO.IBAN_ACCREDITO.getFieldType()));
				setParameter(object, "setCommissioniPsp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "commissioni_psp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType()));
				setParameter(object, "setTipoAllegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_allegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType()));
				setParameter(object, "setAllegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType()));
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
				setParameter(object, "setDebitoreEmail", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_email", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL.getFieldType()));
				setParameter(object, "setDebitoreTelefono", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_telefono", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO.getFieldType()));
				setParameter(object, "setDebitoreCellulare", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cellulare", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE.getFieldType()));
				setParameter(object, "setDebitoreFax", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_fax", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX.getFieldType()));
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
				setParameter(object, "setCodPsp", RendicontazionePagamento.model().FR.COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"FR.codPsp"));
				setParameter(object, "setCodDominio", RendicontazionePagamento.model().FR.COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"FR.codDominio"));
				setParameter(object, "setCodFlusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.codFlusso"));
				setParameter(object, "setStato", RendicontazionePagamento.model().FR.STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.stato"));
				setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.descrizioneStato"));
				setParameter(object, "setIur", RendicontazionePagamento.model().FR.IUR.getFieldType(),
					this.getObjectFromMap(map,"FR.iur"));
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
			if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE)){
				Rendicontazione object = new Rendicontazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Rendicontazione.id"));
				setParameter(object, "setIuv", RendicontazionePagamento.model().RENDICONTAZIONE.IUV.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.iuv"));
				setParameter(object, "setIur", RendicontazionePagamento.model().RENDICONTAZIONE.IUR.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.iur"));
				setParameter(object, "setImportoPagato", RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.importoPagato"));
				setParameter(object, "setEsito", RendicontazionePagamento.model().RENDICONTAZIONE.ESITO.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.esito"));
				setParameter(object, "setData", RendicontazionePagamento.model().RENDICONTAZIONE.DATA.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.data"));
				setParameter(object, "setStato", RendicontazionePagamento.model().RENDICONTAZIONE.STATO.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.stato"));
				setParameter(object, "setAnomalie", RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.anomalie"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Pagamento.id"));
				setParameter(object, "setCodDominio", RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.codDominio"));
				setParameter(object, "setIuv", RendicontazionePagamento.model().PAGAMENTO.IUV.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.iuv"));
				setParameter(object, "setImportoPagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.importoPagato"));
				setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataAcquisizione"));
				setParameter(object, "setIur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.iur"));
				setParameter(object, "setDataPagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataPagamento"));
				setParameter(object, "setIbanAccredito", RendicontazionePagamento.model().PAGAMENTO.IBAN_ACCREDITO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.ibanAccredito"));
				setParameter(object, "setCommissioniPsp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.commissioniPsp"));
				setParameter(object, "setTipoAllegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.tipoAllegato"));
				setParameter(object, "setAllegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.allegato"));
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
				setParameter(object, "setDebitoreEmail", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreEmail"));
				setParameter(object, "setDebitoreTelefono", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreTelefono"));
				setParameter(object, "setDebitoreCellulare", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCellulare"));
				setParameter(object, "setDebitoreFax", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreFax"));
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

			if(model.equals(RendicontazionePagamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("rendicontazione_pagamento","id","seq_rendicontazione_pagamento","rendicontazione_pagamento_init_seq");
			}
			if(model.equals(RendicontazionePagamento.model().FR)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr","id","seq_fr","fr_init_seq");
			}
			if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("rendicontazioni","id","seq_rendicontazioni","rendicontazioni_init_seq");
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
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
