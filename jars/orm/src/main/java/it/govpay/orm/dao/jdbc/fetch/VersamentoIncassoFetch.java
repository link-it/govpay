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

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;
import org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType;

import it.govpay.orm.VersamentoIncasso;


/**     
 * VersamentoIncassoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoIncassoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VersamentoIncasso.model())){
				VersamentoIncasso object = new VersamentoIncasso();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodVersamentoEnte", VersamentoIncasso.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", VersamentoIncasso.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setNome", VersamentoIncasso.model().NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome", VersamentoIncasso.model().NOME.getFieldType()));
				setParameter(object, "setImportoTotale", VersamentoIncasso.model().IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", VersamentoIncasso.model().IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setStatoVersamento", VersamentoIncasso.model().STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", VersamentoIncasso.model().STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizioneStato", VersamentoIncasso.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", VersamentoIncasso.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setAggiornabile", VersamentoIncasso.model().AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornabile", VersamentoIncasso.model().AGGIORNABILE.getFieldType()));
				setParameter(object, "setDataCreazione", VersamentoIncasso.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", VersamentoIncasso.model().DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setDataValidita", VersamentoIncasso.model().DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_validita", VersamentoIncasso.model().DATA_VALIDITA.getFieldType()));
				setParameter(object, "setDataScadenza", VersamentoIncasso.model().DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", VersamentoIncasso.model().DATA_SCADENZA.getFieldType()));
				setParameter(object, "setDataOraUltimoAggiornamento", VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultimo_aggiornamento", VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				setParameter(object, "setCausaleVersamento", VersamentoIncasso.model().CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", VersamentoIncasso.model().CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setDebitoreTipo", VersamentoIncasso.model().DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_tipo", VersamentoIncasso.model().DEBITORE_TIPO.getFieldType()));
				setParameter(object, "setDebitoreIdentificativo", VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setDebitoreAnagrafica", VersamentoIncasso.model().DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", VersamentoIncasso.model().DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setDebitoreIndirizzo", VersamentoIncasso.model().DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_indirizzo", VersamentoIncasso.model().DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setDebitoreCivico", VersamentoIncasso.model().DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_civico", VersamentoIncasso.model().DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setDebitoreCap", VersamentoIncasso.model().DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cap", VersamentoIncasso.model().DEBITORE_CAP.getFieldType()));
				setParameter(object, "setDebitoreLocalita", VersamentoIncasso.model().DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_localita", VersamentoIncasso.model().DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setDebitoreProvincia", VersamentoIncasso.model().DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_provincia", VersamentoIncasso.model().DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setDebitoreNazione", VersamentoIncasso.model().DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_nazione", VersamentoIncasso.model().DEBITORE_NAZIONE.getFieldType()));
				setParameter(object, "setDebitoreEmail", VersamentoIncasso.model().DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_email", VersamentoIncasso.model().DEBITORE_EMAIL.getFieldType()));
				setParameter(object, "setDebitoreTelefono", VersamentoIncasso.model().DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_telefono", VersamentoIncasso.model().DEBITORE_TELEFONO.getFieldType()));
				setParameter(object, "setDebitoreCellulare", VersamentoIncasso.model().DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cellulare", VersamentoIncasso.model().DEBITORE_CELLULARE.getFieldType()));
				setParameter(object, "setDebitoreFax", VersamentoIncasso.model().DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_fax", VersamentoIncasso.model().DEBITORE_FAX.getFieldType()));
				setParameter(object, "setTassonomiaAvviso", VersamentoIncasso.model().TASSONOMIA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia_avviso", VersamentoIncasso.model().TASSONOMIA_AVVISO.getFieldType()));
				setParameter(object, "setTassonomia", VersamentoIncasso.model().TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia", VersamentoIncasso.model().TASSONOMIA.getFieldType()));
				setParameter(object, "setCodLotto", VersamentoIncasso.model().COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_lotto", VersamentoIncasso.model().COD_LOTTO.getFieldType()));
				setParameter(object, "setCodVersamentoLotto", VersamentoIncasso.model().COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_lotto", VersamentoIncasso.model().COD_VERSAMENTO_LOTTO.getFieldType()));
				setParameter(object, "setCodAnnoTributario", VersamentoIncasso.model().COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_anno_tributario", VersamentoIncasso.model().COD_ANNO_TRIBUTARIO.getFieldType()));
				setParameter(object, "setCodBundlekey", VersamentoIncasso.model().COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bundlekey", VersamentoIncasso.model().COD_BUNDLEKEY.getFieldType()));
				setParameter(object, "setDatiAllegati", VersamentoIncasso.model().DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", VersamentoIncasso.model().DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setIncasso", VersamentoIncasso.model().INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "incasso", VersamentoIncasso.model().INCASSO.getFieldType()));
				setParameter(object, "setAnomalie", VersamentoIncasso.model().ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalie", VersamentoIncasso.model().ANOMALIE.getFieldType()));
				setParameter(object, "setIuvVersamento", VersamentoIncasso.model().IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_versamento", VersamentoIncasso.model().IUV_VERSAMENTO.getFieldType()));
				setParameter(object, "setNumeroAvviso", VersamentoIncasso.model().NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_avviso", VersamentoIncasso.model().NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setAvvisaturaAbilitata", VersamentoIncasso.model().AVVISATURA_ABILITATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_abilitata", VersamentoIncasso.model().AVVISATURA_ABILITATA.getFieldType()));
				setParameter(object, "setAvvisaturaDaInviare", VersamentoIncasso.model().AVVISATURA_DA_INVIARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_da_inviare", VersamentoIncasso.model().AVVISATURA_DA_INVIARE.getFieldType()));
				setParameter(object, "setAvvisaturaOperazione", VersamentoIncasso.model().AVVISATURA_OPERAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_operazione", VersamentoIncasso.model().AVVISATURA_OPERAZIONE.getFieldType()));
				setParameter(object, "setAvvisaturaModalita", VersamentoIncasso.model().AVVISATURA_MODALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_modalita", VersamentoIncasso.model().AVVISATURA_MODALITA.getFieldType()));
				setParameter(object, "setAvvisaturaTipoPagamento", VersamentoIncasso.model().AVVISATURA_TIPO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_tipo_pagamento", VersamentoIncasso.model().AVVISATURA_TIPO_PAGAMENTO.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setAvvisaturaCodAvvisatura", VersamentoIncasso.model().AVVISATURA_COD_AVVISATURA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_cod_avvisatura", VersamentoIncasso.model().AVVISATURA_COD_AVVISATURA.getFieldType()));
				setParameter(object, "setAck", VersamentoIncasso.model().ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ack", VersamentoIncasso.model().ACK.getFieldType()));
				setParameter(object, "setAnomalo", VersamentoIncasso.model().ANOMALO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalo", VersamentoIncasso.model().ANOMALO.getFieldType()));
				setParameter(object, "setDataPagamento", VersamentoIncasso.model().DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", VersamentoIncasso.model().DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setImportoPagato", VersamentoIncasso.model().IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", VersamentoIncasso.model().IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setImportoIncassato", VersamentoIncasso.model().IMPORTO_INCASSATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_incassato", VersamentoIncasso.model().IMPORTO_INCASSATO.getFieldType()));
				setParameter(object, "setStatoPagamento", VersamentoIncasso.model().STATO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_pagamento", VersamentoIncasso.model().STATO_PAGAMENTO.getFieldType()));
				setParameter(object, "setIuvPagamento", VersamentoIncasso.model().IUV_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_pagamento", VersamentoIncasso.model().IUV_PAGAMENTO.getFieldType()));
				setParameter(object, "setDivisione", VersamentoIncasso.model().DIVISIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "divisione", VersamentoIncasso.model().DIVISIONE.getFieldType()));
				setParameter(object, "setDirezione", VersamentoIncasso.model().DIREZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "direzione", VersamentoIncasso.model().DIREZIONE.getFieldType()));
				setParameter(object, "setSmartOrderDate", VersamentoIncasso.model().SMART_ORDER_DATE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "smart_order_date", VersamentoIncasso.model().SMART_ORDER_DATE.getFieldType()));
				setParameter(object, "setSmartOrderRank", VersamentoIncasso.model().SMART_ORDER_RANK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "smart_order_rank", VersamentoIncasso.model().SMART_ORDER_RANK.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setIdSessione", VersamentoIncasso.model().ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione", VersamentoIncasso.model().ID_SESSIONE.getFieldType()));
				setParameter(object, "setSrcIuv", VersamentoIncasso.model().SRC_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "src_iuv", VersamentoIncasso.model().SRC_IUV.getFieldType()));
				setParameter(object, "setSrcDebitoreIdentificativo", VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "src_debitore_identificativo", VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setCodRata", VersamentoIncasso.model().COD_RATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_rata", VersamentoIncasso.model().COD_RATA.getFieldType()));
				setParameter(object, "setCodDocumento", VersamentoIncasso.model().COD_DOCUMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_documento", VersamentoIncasso.model().COD_DOCUMENTO.getFieldType()));
				setParameter(object, "setTipo", VersamentoIncasso.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", VersamentoIncasso.model().TIPO.getFieldType()));
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

			if(model.equals(VersamentoIncasso.model())){
				VersamentoIncasso object = new VersamentoIncasso();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodVersamentoEnte", VersamentoIncasso.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setNome", VersamentoIncasso.model().NOME.getFieldType(),
					this.getObjectFromMap(map,"nome"));
				setParameter(object, "setImportoTotale", VersamentoIncasso.model().IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"importoTotale"));
				setParameter(object, "setStatoVersamento", VersamentoIncasso.model().STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoVersamento"));
				setParameter(object, "setDescrizioneStato", VersamentoIncasso.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setAggiornabile", VersamentoIncasso.model().AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"aggiornabile"));
				setParameter(object, "setDataCreazione", VersamentoIncasso.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				setParameter(object, "setDataValidita", VersamentoIncasso.model().DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"dataValidita"));
				setParameter(object, "setDataScadenza", VersamentoIncasso.model().DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"dataScadenza"));
				setParameter(object, "setDataOraUltimoAggiornamento", VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataOraUltimoAggiornamento"));
				setParameter(object, "setCausaleVersamento", VersamentoIncasso.model().CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"causaleVersamento"));
				setParameter(object, "setDebitoreTipo", VersamentoIncasso.model().DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"debitoreTipo"));
				setParameter(object, "setDebitoreIdentificativo", VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIdentificativo"));
				setParameter(object, "setDebitoreAnagrafica", VersamentoIncasso.model().DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"debitoreAnagrafica"));
				setParameter(object, "setDebitoreIndirizzo", VersamentoIncasso.model().DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIndirizzo"));
				setParameter(object, "setDebitoreCivico", VersamentoIncasso.model().DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"debitoreCivico"));
				setParameter(object, "setDebitoreCap", VersamentoIncasso.model().DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"debitoreCap"));
				setParameter(object, "setDebitoreLocalita", VersamentoIncasso.model().DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"debitoreLocalita"));
				setParameter(object, "setDebitoreProvincia", VersamentoIncasso.model().DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"debitoreProvincia"));
				setParameter(object, "setDebitoreNazione", VersamentoIncasso.model().DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"debitoreNazione"));
				setParameter(object, "setDebitoreEmail", VersamentoIncasso.model().DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"debitoreEmail"));
				setParameter(object, "setDebitoreTelefono", VersamentoIncasso.model().DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"debitoreTelefono"));
				setParameter(object, "setDebitoreCellulare", VersamentoIncasso.model().DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"debitoreCellulare"));
				setParameter(object, "setDebitoreFax", VersamentoIncasso.model().DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"debitoreFax"));
				setParameter(object, "setTassonomiaAvviso", VersamentoIncasso.model().TASSONOMIA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"tassonomiaAvviso"));
				setParameter(object, "setTassonomia", VersamentoIncasso.model().TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"tassonomia"));
				setParameter(object, "setCodLotto", VersamentoIncasso.model().COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"codLotto"));
				setParameter(object, "setCodVersamentoLotto", VersamentoIncasso.model().COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoLotto"));
				setParameter(object, "setCodAnnoTributario", VersamentoIncasso.model().COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"codAnnoTributario"));
				setParameter(object, "setCodBundlekey", VersamentoIncasso.model().COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"codBundlekey"));
				setParameter(object, "setDatiAllegati", VersamentoIncasso.model().DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"datiAllegati"));
				setParameter(object, "setIncasso", VersamentoIncasso.model().INCASSO.getFieldType(),
					this.getObjectFromMap(map,"incasso"));
				setParameter(object, "setAnomalie", VersamentoIncasso.model().ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"anomalie"));
				setParameter(object, "setIuvVersamento", VersamentoIncasso.model().IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvVersamento"));
				setParameter(object, "setNumeroAvviso", VersamentoIncasso.model().NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"numeroAvviso"));
				setParameter(object, "setAvvisaturaAbilitata", VersamentoIncasso.model().AVVISATURA_ABILITATA.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaAbilitata"));
				setParameter(object, "setAvvisaturaDaInviare", VersamentoIncasso.model().AVVISATURA_DA_INVIARE.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaDaInviare"));
				setParameter(object, "setAvvisaturaOperazione", VersamentoIncasso.model().AVVISATURA_OPERAZIONE.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaOperazione"));
				setParameter(object, "setAvvisaturaModalita", VersamentoIncasso.model().AVVISATURA_MODALITA.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaModalita"));
				setParameter(object, "setAvvisaturaTipoPagamento", VersamentoIncasso.model().AVVISATURA_TIPO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaTipoPagamento"));
				setParameter(object, "setAvvisaturaCodAvvisatura", VersamentoIncasso.model().AVVISATURA_COD_AVVISATURA.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaCodAvvisatura"));
				setParameter(object, "setAck", VersamentoIncasso.model().ACK.getFieldType(),
					this.getObjectFromMap(map,"ack"));
				setParameter(object, "setAnomalo", VersamentoIncasso.model().ANOMALO.getFieldType(),
					this.getObjectFromMap(map,"anomalo"));
				setParameter(object, "setDataPagamento", VersamentoIncasso.model().DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataPagamento"));
				setParameter(object, "setImportoPagato", VersamentoIncasso.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				setParameter(object, "setImportoIncassato", VersamentoIncasso.model().IMPORTO_INCASSATO.getFieldType(),
					this.getObjectFromMap(map,"importoIncassato"));
				setParameter(object, "setStatoPagamento", VersamentoIncasso.model().STATO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoPagamento"));
				setParameter(object, "setIuvPagamento", VersamentoIncasso.model().IUV_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvPagamento"));
				setParameter(object, "setDivisione", VersamentoIncasso.model().DIVISIONE.getFieldType(),
					this.getObjectFromMap(map,"divisione"));
				setParameter(object, "setDirezione", VersamentoIncasso.model().DIREZIONE.getFieldType(),
					this.getObjectFromMap(map,"direzione"));
				setParameter(object, "setSmartOrderDate", VersamentoIncasso.model().SMART_ORDER_DATE.getFieldType(),
					this.getObjectFromMap(map,"smartOrderDate"));
				setParameter(object, "setSmartOrderRank", VersamentoIncasso.model().SMART_ORDER_RANK.getFieldType(),
					this.getObjectFromMap(map,"smartOrderRank"));
				setParameter(object, "setIdSessione", VersamentoIncasso.model().ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"idSessione"));
				setParameter(object, "setSrcIuv", VersamentoIncasso.model().SRC_IUV.getFieldType(),
					this.getObjectFromMap(map,"srcIuv"));
				setParameter(object, "setSrcDebitoreIdentificativo", VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"srcDebitoreIdentificativo"));
				setParameter(object, "setCodRata", VersamentoIncasso.model().COD_RATA.getFieldType(),
					this.getObjectFromMap(map,"codRata"));
				setParameter(object, "setCodDocumento", VersamentoIncasso.model().COD_DOCUMENTO.getFieldType(),
					this.getObjectFromMap(map,"codDocumento"));
				setParameter(object, "setTipo", VersamentoIncasso.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
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

			if(model.equals(VersamentoIncasso.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("versamenti_incassi","id","seq_versamenti_incassi","versamenti_incassi_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
