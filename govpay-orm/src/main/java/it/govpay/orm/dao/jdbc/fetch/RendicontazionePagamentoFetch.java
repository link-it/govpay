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
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodPsp", RendicontazionePagamento.model().FR.COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", RendicontazionePagamento.model().FR.COD_PSP.getFieldType()));
				this.setParameter(object, "setCodDominio", RendicontazionePagamento.model().FR.COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RendicontazionePagamento.model().FR.COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setCodFlusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType()));
				this.setParameter(object, "setStato", RendicontazionePagamento.model().FR.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamento.model().FR.STATO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setIur", RendicontazionePagamento.model().FR.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().FR.IUR.getFieldType()));
				this.setParameter(object, "setDataOraFlusso", RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_flusso", RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO.getFieldType()));
				this.setParameter(object, "setDataRegolamento", RendicontazionePagamento.model().FR.DATA_REGOLAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_regolamento", RendicontazionePagamento.model().FR.DATA_REGOLAMENTO.getFieldType()));
				this.setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE.getFieldType()));
				this.setParameter(object, "setNumeroPagamenti", RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI.getFieldType()));
				this.setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				this.setParameter(object, "setCodBicRiversamento", RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bic_riversamento", RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO.getFieldType()));
				this.setParameter(object, "setXml", RendicontazionePagamento.model().FR.XML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml", RendicontazionePagamento.model().FR.XML.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE)){
				Rendicontazione object = new Rendicontazione();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setIuv", RendicontazionePagamento.model().RENDICONTAZIONE.IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RendicontazionePagamento.model().RENDICONTAZIONE.IUV.getFieldType()));
				this.setParameter(object, "setIur", RendicontazionePagamento.model().RENDICONTAZIONE.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().RENDICONTAZIONE.IUR.getFieldType()));
				this.setParameter(object, "setIndiceDati", RendicontazionePagamento.model().RENDICONTAZIONE.INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_dati", RendicontazionePagamento.model().RENDICONTAZIONE.INDICE_DATI.getFieldType(), org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				this.setParameter(object, "setImportoPagato", RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO.getFieldType()));
				this.setParameter(object, "setEsito", RendicontazionePagamento.model().RENDICONTAZIONE.ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito", RendicontazionePagamento.model().RENDICONTAZIONE.ESITO.getFieldType(), org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				this.setParameter(object, "setData", RendicontazionePagamento.model().RENDICONTAZIONE.DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data", RendicontazionePagamento.model().RENDICONTAZIONE.DATA.getFieldType()));
				this.setParameter(object, "setStato", RendicontazionePagamento.model().RENDICONTAZIONE.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamento.model().RENDICONTAZIONE.STATO.getFieldType()));
				this.setParameter(object, "setAnomalie", RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalie", RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
				Pagamento object = new Pagamento();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodDominio", RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setIuv", RendicontazionePagamento.model().PAGAMENTO.IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RendicontazionePagamento.model().PAGAMENTO.IUV.getFieldType()));
				this.setParameter(object, "setIndiceDati", RendicontazionePagamento.model().PAGAMENTO.INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_dati", RendicontazionePagamento.model().PAGAMENTO.INDICE_DATI.getFieldType()));
				this.setParameter(object, "setImportoPagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType()));
				this.setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType()));
				this.setParameter(object, "setIur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType()));
				this.setParameter(object, "setDataPagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType()));
				this.setParameter(object, "setCommissioniPsp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "commissioni_psp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType()));
				this.setParameter(object, "setTipoAllegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_allegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType()));
				this.setParameter(object, "setAllegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType()));
				this.setParameter(object, "setDataAcquisizioneRevoca", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione_revoca", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA.getFieldType()));
				this.setParameter(object, "setCausaleRevoca", RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_revoca", RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA.getFieldType()));
				this.setParameter(object, "setDatiRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_revoca", RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA.getFieldType()));
				this.setParameter(object, "setImportoRevocato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_revocato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO.getFieldType()));
				this.setParameter(object, "setEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito_revoca", RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA.getFieldType()));
				this.setParameter(object, "setDatiEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_esito_revoca", RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA.getFieldType()));
				this.setParameter(object, "setStato", RendicontazionePagamento.model().PAGAMENTO.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RendicontazionePagamento.model().PAGAMENTO.STATO.getFieldType()));
				setParameter(object, "setTipo", RendicontazionePagamento.model().PAGAMENTO.TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", RendicontazionePagamento.model().PAGAMENTO.TIPO.getFieldType()));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)){
				SingoloVersamento object = new SingoloVersamento();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				this.setParameter(object, "setStatoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_singolo_versamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setImportoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_singolo_versamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType()));
				this.setParameter(object, "setTipoBollo", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_bollo", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType()));
				this.setParameter(object, "setHashDocumento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "hash_documento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType()));
				this.setParameter(object, "setProvinciaResidenza", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "provincia_residenza", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType()));
				this.setParameter(object, "setTipoContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_contabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType()));
				this.setParameter(object, "setCodiceContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_contabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType()));
				this.setParameter(object, "setDescrizione", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE.getFieldType()));
				this.setParameter(object, "setDatiAllegati", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setIndiceDati", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_dati", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.INDICE_DATI.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().VERSAMENTO)){
				Versamento object = new Versamento();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodVersamentoEnte", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType()));
				this.setParameter(object, "setNome", RendicontazionePagamento.model().VERSAMENTO.NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome", RendicontazionePagamento.model().VERSAMENTO.NOME.getFieldType()));
				this.setParameter(object, "setImportoTotale", RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType()));
				this.setParameter(object, "setStatoVersamento", RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setAggiornabile", RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornabile", RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE.getFieldType()));
				this.setParameter(object, "setDataCreazione", RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE.getFieldType()));
				this.setParameter(object, "setDataValidita", RendicontazionePagamento.model().VERSAMENTO.DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_validita", RendicontazionePagamento.model().VERSAMENTO.DATA_VALIDITA.getFieldType()));
				this.setParameter(object, "setDataScadenza", RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA.getFieldType()));
				this.setParameter(object, "setDataOraUltimoAggiornamento", RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultimo_aggiornamento", RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				this.setParameter(object, "setCausaleVersamento", RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setDebitoreTipo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_tipo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TIPO.getFieldType()));
				this.setParameter(object, "setDebitoreIdentificativo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType()));
				this.setParameter(object, "setDebitoreAnagrafica", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType()));
				this.setParameter(object, "setDebitoreIndirizzo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_indirizzo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType()));
				this.setParameter(object, "setDebitoreCivico", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_civico", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType()));
				this.setParameter(object, "setDebitoreCap", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cap", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP.getFieldType()));
				this.setParameter(object, "setDebitoreLocalita", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_localita", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType()));
				this.setParameter(object, "setDebitoreProvincia", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_provincia", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType()));
				this.setParameter(object, "setDebitoreNazione", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_nazione", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType()));
				this.setParameter(object, "setDebitoreEmail", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_email", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL.getFieldType()));
				this.setParameter(object, "setDebitoreTelefono", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_telefono", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO.getFieldType()));
				this.setParameter(object, "setDebitoreCellulare", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cellulare", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE.getFieldType()));
				this.setParameter(object, "setDebitoreFax", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_fax", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX.getFieldType()));
				this.setParameter(object, "setTassonomiaAvviso", RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia_avviso", RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA_AVVISO.getFieldType()));
				this.setParameter(object, "setTassonomia", RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia", RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA.getFieldType()));
				this.setParameter(object, "setCodLotto", RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_lotto", RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO.getFieldType()));
				this.setParameter(object, "setCodVersamentoLotto", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_lotto", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType()));
				this.setParameter(object, "setCodAnnoTributario", RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_anno_tributario", RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType()));
				this.setParameter(object, "setCodBundlekey", RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bundlekey", RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType()));
				this.setParameter(object, "setDatiAllegati", RendicontazionePagamento.model().VERSAMENTO.DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", RendicontazionePagamento.model().VERSAMENTO.DATI_ALLEGATI.getFieldType()));
				this.setParameter(object, "setIncasso", RendicontazionePagamento.model().VERSAMENTO.INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "incasso", RendicontazionePagamento.model().VERSAMENTO.INCASSO.getFieldType()));
				this.setParameter(object, "setAnomalie", RendicontazionePagamento.model().VERSAMENTO.ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalie", RendicontazionePagamento.model().VERSAMENTO.ANOMALIE.getFieldType()));
				setParameter(object, "setIuvVersamento", RendicontazionePagamento.model().VERSAMENTO.IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_versamento", RendicontazionePagamento.model().VERSAMENTO.IUV_VERSAMENTO.getFieldType()));
				setParameter(object, "setNumeroAvviso", RendicontazionePagamento.model().VERSAMENTO.NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_avviso", RendicontazionePagamento.model().VERSAMENTO.NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setAvvisatura", RendicontazionePagamento.model().VERSAMENTO.AVVISATURA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura", RendicontazionePagamento.model().VERSAMENTO.AVVISATURA.getFieldType()));
				setParameter(object, "setTipoPagamento", RendicontazionePagamento.model().VERSAMENTO.TIPO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_pagamento", RendicontazionePagamento.model().VERSAMENTO.TIPO_PAGAMENTO.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setDaAvvisare", RendicontazionePagamento.model().VERSAMENTO.DA_AVVISARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "da_avvisare", RendicontazionePagamento.model().VERSAMENTO.DA_AVVISARE.getFieldType()));
				setParameter(object, "setCodAvvisatura", RendicontazionePagamento.model().VERSAMENTO.COD_AVVISATURA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_avvisatura", RendicontazionePagamento.model().VERSAMENTO.COD_AVVISATURA.getFieldType()));
				setParameter(object, "setAck", RendicontazionePagamento.model().VERSAMENTO.ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ack", RendicontazionePagamento.model().VERSAMENTO.ACK.getFieldType()));
				setParameter(object, "setNote", RendicontazionePagamento.model().VERSAMENTO.NOTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "note", RendicontazionePagamento.model().VERSAMENTO.NOTE.getFieldType()));
				setParameter(object, "setAnomalo", RendicontazionePagamento.model().VERSAMENTO.ANOMALO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalo", RendicontazionePagamento.model().VERSAMENTO.ANOMALO.getFieldType()));
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
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"FR.id"));
				this.setParameter(object, "setCodPsp", RendicontazionePagamento.model().FR.COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"FR.codPsp"));
				this.setParameter(object, "setCodDominio", RendicontazionePagamento.model().FR.COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"FR.codDominio"));
				this.setParameter(object, "setCodFlusso", RendicontazionePagamento.model().FR.COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.codFlusso"));
				this.setParameter(object, "setStato", RendicontazionePagamento.model().FR.STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.stato"));
				this.setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().FR.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.descrizioneStato"));
				this.setParameter(object, "setIur", RendicontazionePagamento.model().FR.IUR.getFieldType(),
					this.getObjectFromMap(map,"FR.iur"));
				this.setParameter(object, "setDataOraFlusso", RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataOraFlusso"));
				this.setParameter(object, "setDataRegolamento", RendicontazionePagamento.model().FR.DATA_REGOLAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataRegolamento"));
				this.setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"FR.dataAcquisizione"));
				this.setParameter(object, "setNumeroPagamenti", RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.numeroPagamenti"));
				this.setParameter(object, "setImportoTotalePagamenti", RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.importoTotalePagamenti"));
				this.setParameter(object, "setCodBicRiversamento", RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.codBicRiversamento"));
				this.setParameter(object, "setXml", RendicontazionePagamento.model().FR.XML.getFieldType(),
					this.getObjectFromMap(map,"FR.xml"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE)){
				Rendicontazione object = new Rendicontazione();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Rendicontazione.id"));
				this.setParameter(object, "setIuv", RendicontazionePagamento.model().RENDICONTAZIONE.IUV.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.iuv"));
				this.setParameter(object, "setIur", RendicontazionePagamento.model().RENDICONTAZIONE.IUR.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.iur"));
				this.setParameter(object, "setIndiceDati", RendicontazionePagamento.model().RENDICONTAZIONE.INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.indiceDati"));
				this.setParameter(object, "setImportoPagato", RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.importoPagato"));
				this.setParameter(object, "setEsito", RendicontazionePagamento.model().RENDICONTAZIONE.ESITO.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.esito"));
				this.setParameter(object, "setData", RendicontazionePagamento.model().RENDICONTAZIONE.DATA.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.data"));
				this.setParameter(object, "setStato", RendicontazionePagamento.model().RENDICONTAZIONE.STATO.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.stato"));
				this.setParameter(object, "setAnomalie", RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"Rendicontazione.anomalie"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
				Pagamento object = new Pagamento();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Pagamento.id"));
				this.setParameter(object, "setCodDominio", RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.codDominio"));
				this.setParameter(object, "setIuv", RendicontazionePagamento.model().PAGAMENTO.IUV.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.iuv"));
				this.setParameter(object, "setIndiceDati", RendicontazionePagamento.model().PAGAMENTO.INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.indiceDati"));
				this.setParameter(object, "setImportoPagato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.importoPagato"));
				this.setParameter(object, "setDataAcquisizione", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataAcquisizione"));
				this.setParameter(object, "setIur", RendicontazionePagamento.model().PAGAMENTO.IUR.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.iur"));
				this.setParameter(object, "setDataPagamento", RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataPagamento"));
				this.setParameter(object, "setCommissioniPsp", RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.commissioniPsp"));
				this.setParameter(object, "setTipoAllegato", RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.tipoAllegato"));
				this.setParameter(object, "setAllegato", RendicontazionePagamento.model().PAGAMENTO.ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.allegato"));
				this.setParameter(object, "setDataAcquisizioneRevoca", RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.dataAcquisizioneRevoca"));
				this.setParameter(object, "setCausaleRevoca", RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.causaleRevoca"));
				this.setParameter(object, "setDatiRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.datiRevoca"));
				this.setParameter(object, "setImportoRevocato", RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.importoRevocato"));
				this.setParameter(object, "setEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.esitoRevoca"));
				this.setParameter(object, "setDatiEsitoRevoca", RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.datiEsitoRevoca"));
				this.setParameter(object, "setStato", RendicontazionePagamento.model().PAGAMENTO.STATO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.stato"));
				this.setParameter(object, "setTipo", RendicontazionePagamento.model().PAGAMENTO.TIPO.getFieldType(),
					this.getObjectFromMap(map,"Pagamento.tipo"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)){
				SingoloVersamento object = new SingoloVersamento();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"SingoloVersamento.id"));
				this.setParameter(object, "setCodSingoloVersamentoEnte", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.codSingoloVersamentoEnte"));
				this.setParameter(object, "setStatoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.statoSingoloVersamento"));
				this.setParameter(object, "setImportoSingoloVersamento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.importoSingoloVersamento"));
				this.setParameter(object, "setAnnoRiferimento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.annoRiferimento"));
				this.setParameter(object, "setTipoBollo", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.tipoBollo"));
				this.setParameter(object, "setHashDocumento", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.hashDocumento"));
				this.setParameter(object, "setProvinciaResidenza", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.provinciaResidenza"));
				this.setParameter(object, "setTipoContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.tipoContabilita"));
				this.setParameter(object, "setCodiceContabilita", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.codiceContabilita"));
				this.setParameter(object, "setDescrizione", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.descrizione"));
				this.setParameter(object, "setDatiAllegati", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.datiAllegati"));
				setParameter(object, "setIndiceDati", RendicontazionePagamento.model().SINGOLO_VERSAMENTO.INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"SingoloVersamento.indiceDati"));
				return object;
			}
			if(model.equals(RendicontazionePagamento.model().VERSAMENTO)){
				Versamento object = new Versamento();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Versamento.id"));
				this.setParameter(object, "setCodVersamentoEnte", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codVersamentoEnte"));
				this.setParameter(object, "setNome", RendicontazionePagamento.model().VERSAMENTO.NOME.getFieldType(),
					this.getObjectFromMap(map,"Versamento.nome"));
				this.setParameter(object, "setImportoTotale", RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.importoTotale"));
				this.setParameter(object, "setStatoVersamento", RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.statoVersamento"));
				this.setParameter(object, "setDescrizioneStato", RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.descrizioneStato"));
				this.setParameter(object, "setAggiornabile", RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.aggiornabile"));
				this.setParameter(object, "setDataCreazione", RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataCreazione"));
				this.setParameter(object, "setDataValidita", RendicontazionePagamento.model().VERSAMENTO.DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataValidita"));
				this.setParameter(object, "setDataScadenza", RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataScadenza"));
				this.setParameter(object, "setDataOraUltimoAggiornamento", RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.dataOraUltimoAggiornamento"));
				this.setParameter(object, "setCausaleVersamento", RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.causaleVersamento"));
				this.setParameter(object, "setDebitoreTipo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreTipo"));
				this.setParameter(object, "setDebitoreIdentificativo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreIdentificativo"));
				this.setParameter(object, "setDebitoreAnagrafica", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreAnagrafica"));
				this.setParameter(object, "setDebitoreIndirizzo", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreIndirizzo"));
				this.setParameter(object, "setDebitoreCivico", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCivico"));
				this.setParameter(object, "setDebitoreCap", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCap"));
				this.setParameter(object, "setDebitoreLocalita", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreLocalita"));
				this.setParameter(object, "setDebitoreProvincia", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreProvincia"));
				this.setParameter(object, "setDebitoreNazione", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreNazione"));
				this.setParameter(object, "setDebitoreEmail", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreEmail"));
				this.setParameter(object, "setDebitoreTelefono", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreTelefono"));
				this.setParameter(object, "setDebitoreCellulare", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreCellulare"));
				this.setParameter(object, "setDebitoreFax", RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"Versamento.debitoreFax"));
				this.setParameter(object, "setTassonomiaAvviso", RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.tassonomiaAvviso"));
				this.setParameter(object, "setTassonomia", RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.tassonomia"));
				this.setParameter(object, "setCodLotto", RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codLotto"));
				this.setParameter(object, "setCodVersamentoLotto", RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codVersamentoLotto"));
				this.setParameter(object, "setCodAnnoTributario", RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codAnnoTributario"));
				this.setParameter(object, "setCodBundlekey", RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codBundlekey"));
				this.setParameter(object, "setDatiAllegati", RendicontazionePagamento.model().VERSAMENTO.DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"Versamento.datiAllegati"));
				this.setParameter(object, "setIncasso", RendicontazionePagamento.model().VERSAMENTO.INCASSO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.incasso"));
				this.setParameter(object, "setAnomalie", RendicontazionePagamento.model().VERSAMENTO.ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.anomalie"));
				setParameter(object, "setIuvVersamento", RendicontazionePagamento.model().VERSAMENTO.IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.iuvVersamento"));
				setParameter(object, "setNumeroAvviso", RendicontazionePagamento.model().VERSAMENTO.NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.numeroAvviso"));
				setParameter(object, "setAvvisatura", RendicontazionePagamento.model().VERSAMENTO.AVVISATURA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.avvisatura"));
				setParameter(object, "setTipoPagamento", RendicontazionePagamento.model().VERSAMENTO.TIPO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.tipoPagamento"));
				setParameter(object, "setDaAvvisare", RendicontazionePagamento.model().VERSAMENTO.DA_AVVISARE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.daAvvisare"));
				setParameter(object, "setCodAvvisatura", RendicontazionePagamento.model().VERSAMENTO.COD_AVVISATURA.getFieldType(),
					this.getObjectFromMap(map,"Versamento.codAvvisatura"));
				setParameter(object, "setAck", RendicontazionePagamento.model().VERSAMENTO.ACK.getFieldType(),
					this.getObjectFromMap(map,"Versamento.ack"));
				setParameter(object, "setNote", RendicontazionePagamento.model().VERSAMENTO.NOTE.getFieldType(),
					this.getObjectFromMap(map,"Versamento.note"));
				setParameter(object, "setAnomalo", RendicontazionePagamento.model().VERSAMENTO.ANOMALO.getFieldType(),
					this.getObjectFromMap(map,"Versamento.anomalo"));
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
