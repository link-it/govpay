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

import it.govpay.orm.Versamento;


/**     
 * VersamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Versamento.model())){
				Versamento object = new Versamento();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodVersamentoEnte", Versamento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", Versamento.model().COD_VERSAMENTO_ENTE.getFieldType()));
				this.setParameter(object, "setNome", Versamento.model().NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome", Versamento.model().NOME.getFieldType()));
				this.setParameter(object, "setImportoTotale", Versamento.model().IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", Versamento.model().IMPORTO_TOTALE.getFieldType()));
				this.setParameter(object, "setStatoVersamento", Versamento.model().STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", Versamento.model().STATO_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", Versamento.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", Versamento.model().DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setAggiornabile", Versamento.model().AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornabile", Versamento.model().AGGIORNABILE.getFieldType()));
				this.setParameter(object, "setDataCreazione", Versamento.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", Versamento.model().DATA_CREAZIONE.getFieldType()));
				this.setParameter(object, "setDataValidita", Versamento.model().DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_validita", Versamento.model().DATA_VALIDITA.getFieldType()));
				this.setParameter(object, "setDataScadenza", Versamento.model().DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", Versamento.model().DATA_SCADENZA.getFieldType()));
				this.setParameter(object, "setDataOraUltimoAggiornamento", Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultimo_aggiornamento", Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				this.setParameter(object, "setCausaleVersamento", Versamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", Versamento.model().CAUSALE_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setDebitoreTipo", Versamento.model().DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_tipo", Versamento.model().DEBITORE_TIPO.getFieldType()));
				this.setParameter(object, "setDebitoreIdentificativo", Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
				this.setParameter(object, "setDebitoreAnagrafica", Versamento.model().DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", Versamento.model().DEBITORE_ANAGRAFICA.getFieldType()));
				this.setParameter(object, "setDebitoreIndirizzo", Versamento.model().DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_indirizzo", Versamento.model().DEBITORE_INDIRIZZO.getFieldType()));
				this.setParameter(object, "setDebitoreCivico", Versamento.model().DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_civico", Versamento.model().DEBITORE_CIVICO.getFieldType()));
				this.setParameter(object, "setDebitoreCap", Versamento.model().DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cap", Versamento.model().DEBITORE_CAP.getFieldType()));
				this.setParameter(object, "setDebitoreLocalita", Versamento.model().DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_localita", Versamento.model().DEBITORE_LOCALITA.getFieldType()));
				this.setParameter(object, "setDebitoreProvincia", Versamento.model().DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_provincia", Versamento.model().DEBITORE_PROVINCIA.getFieldType()));
				this.setParameter(object, "setDebitoreNazione", Versamento.model().DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_nazione", Versamento.model().DEBITORE_NAZIONE.getFieldType()));
				this.setParameter(object, "setDebitoreEmail", Versamento.model().DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_email", Versamento.model().DEBITORE_EMAIL.getFieldType()));
				this.setParameter(object, "setDebitoreTelefono", Versamento.model().DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_telefono", Versamento.model().DEBITORE_TELEFONO.getFieldType()));
				this.setParameter(object, "setDebitoreCellulare", Versamento.model().DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cellulare", Versamento.model().DEBITORE_CELLULARE.getFieldType()));
				this.setParameter(object, "setDebitoreFax", Versamento.model().DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_fax", Versamento.model().DEBITORE_FAX.getFieldType()));
				this.setParameter(object, "setTassonomiaAvviso", Versamento.model().TASSONOMIA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia_avviso", Versamento.model().TASSONOMIA_AVVISO.getFieldType()));
				this.setParameter(object, "setTassonomia", Versamento.model().TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia", Versamento.model().TASSONOMIA.getFieldType()));
				this.setParameter(object, "setCodLotto", Versamento.model().COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_lotto", Versamento.model().COD_LOTTO.getFieldType()));
				this.setParameter(object, "setCodVersamentoLotto", Versamento.model().COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_lotto", Versamento.model().COD_VERSAMENTO_LOTTO.getFieldType()));
				this.setParameter(object, "setCodAnnoTributario", Versamento.model().COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_anno_tributario", Versamento.model().COD_ANNO_TRIBUTARIO.getFieldType()));
				this.setParameter(object, "setCodBundlekey", Versamento.model().COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bundlekey", Versamento.model().COD_BUNDLEKEY.getFieldType()));
				this.setParameter(object, "setDatiAllegati", Versamento.model().DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", Versamento.model().DATI_ALLEGATI.getFieldType()));
				this.setParameter(object, "setIncasso", Versamento.model().INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "incasso", Versamento.model().INCASSO.getFieldType()));
				this.setParameter(object, "setAnomalie", Versamento.model().ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalie", Versamento.model().ANOMALIE.getFieldType()));
				this.setParameter(object, "setIuvVersamento", Versamento.model().IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_versamento", Versamento.model().IUV_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setNumeroAvviso", Versamento.model().NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_avviso", Versamento.model().NUMERO_AVVISO.getFieldType()));
				this.setParameter(object, "setAvvisaturaAbilitata", Versamento.model().AVVISATURA_ABILITATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_abilitata", Versamento.model().AVVISATURA_ABILITATA.getFieldType()));
				this.setParameter(object, "setAvvisaturaDaInviare", Versamento.model().AVVISATURA_DA_INVIARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_da_inviare", Versamento.model().AVVISATURA_DA_INVIARE.getFieldType()));
				this.setParameter(object, "setAvvisaturaOperazione", Versamento.model().AVVISATURA_OPERAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_operazione", Versamento.model().AVVISATURA_OPERAZIONE.getFieldType()));
				this.setParameter(object, "setAvvisaturaModalita", Versamento.model().AVVISATURA_MODALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_modalita", Versamento.model().AVVISATURA_MODALITA.getFieldType()));
				this.setParameter(object, "setAvvisaturaTipoPagamento", Versamento.model().AVVISATURA_TIPO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_tipo_pagamento", Versamento.model().AVVISATURA_TIPO_PAGAMENTO.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				this.setParameter(object, "setAvvisaturaCodAvvisatura", Versamento.model().AVVISATURA_COD_AVVISATURA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avvisatura_cod_avvisatura", Versamento.model().AVVISATURA_COD_AVVISATURA.getFieldType()));
				this.setParameter(object, "setAck", Versamento.model().ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ack", Versamento.model().ACK.getFieldType()));
				this.setParameter(object, "setAnomalo", Versamento.model().ANOMALO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalo", Versamento.model().ANOMALO.getFieldType()));
				setParameter(object, "setDivisione", Versamento.model().DIVISIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "divisione", Versamento.model().DIVISIONE.getFieldType()));
				setParameter(object, "setDirezione", Versamento.model().DIREZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "direzione", Versamento.model().DIREZIONE.getFieldType()));
				setParameter(object, "setIdSessione", Versamento.model().ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione", Versamento.model().ID_SESSIONE.getFieldType()));
				setParameter(object, "setDataPagamento", Versamento.model().DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", Versamento.model().DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setImportoPagato", Versamento.model().IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", Versamento.model().IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setImportoIncassato", Versamento.model().IMPORTO_INCASSATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_incassato", Versamento.model().IMPORTO_INCASSATO.getFieldType()));
				setParameter(object, "setStatoPagamento", Versamento.model().STATO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_pagamento", Versamento.model().STATO_PAGAMENTO.getFieldType()));
				setParameter(object, "setIuvPagamento", Versamento.model().IUV_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_pagamento", Versamento.model().IUV_PAGAMENTO.getFieldType()));
				setParameter(object, "setSrcIuv", Versamento.model().SRC_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "src_iuv", Versamento.model().SRC_IUV.getFieldType()));
				setParameter(object, "setSrcDebitoreIdentificativo", Versamento.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "src_debitore_identificativo", Versamento.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setCodRata", Versamento.model().COD_RATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_rata", Versamento.model().COD_RATA.getFieldType()));
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

			if(model.equals(Versamento.model())){
				Versamento object = new Versamento();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodVersamentoEnte", Versamento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				this.setParameter(object, "setNome", Versamento.model().NOME.getFieldType(),
					this.getObjectFromMap(map,"nome"));
				this.setParameter(object, "setImportoTotale", Versamento.model().IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"importoTotale"));
				this.setParameter(object, "setStatoVersamento", Versamento.model().STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoVersamento"));
				this.setParameter(object, "setDescrizioneStato", Versamento.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				this.setParameter(object, "setAggiornabile", Versamento.model().AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"aggiornabile"));
				this.setParameter(object, "setDataCreazione", Versamento.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				this.setParameter(object, "setDataValidita", Versamento.model().DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"dataValidita"));
				this.setParameter(object, "setDataScadenza", Versamento.model().DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"dataScadenza"));
				this.setParameter(object, "setDataOraUltimoAggiornamento", Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataOraUltimoAggiornamento"));
				this.setParameter(object, "setCausaleVersamento", Versamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"causaleVersamento"));
				this.setParameter(object, "setDebitoreTipo", Versamento.model().DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"debitoreTipo"));
				this.setParameter(object, "setDebitoreIdentificativo", Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIdentificativo"));
				this.setParameter(object, "setDebitoreAnagrafica", Versamento.model().DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"debitoreAnagrafica"));
				this.setParameter(object, "setDebitoreIndirizzo", Versamento.model().DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIndirizzo"));
				this.setParameter(object, "setDebitoreCivico", Versamento.model().DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"debitoreCivico"));
				this.setParameter(object, "setDebitoreCap", Versamento.model().DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"debitoreCap"));
				this.setParameter(object, "setDebitoreLocalita", Versamento.model().DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"debitoreLocalita"));
				this.setParameter(object, "setDebitoreProvincia", Versamento.model().DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"debitoreProvincia"));
				this.setParameter(object, "setDebitoreNazione", Versamento.model().DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"debitoreNazione"));
				this.setParameter(object, "setDebitoreEmail", Versamento.model().DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"debitoreEmail"));
				this.setParameter(object, "setDebitoreTelefono", Versamento.model().DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"debitoreTelefono"));
				this.setParameter(object, "setDebitoreCellulare", Versamento.model().DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"debitoreCellulare"));
				this.setParameter(object, "setDebitoreFax", Versamento.model().DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"debitoreFax"));
				this.setParameter(object, "setTassonomiaAvviso", Versamento.model().TASSONOMIA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"tassonomiaAvviso"));
				this.setParameter(object, "setTassonomia", Versamento.model().TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"tassonomia"));
				this.setParameter(object, "setCodLotto", Versamento.model().COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"codLotto"));
				this.setParameter(object, "setCodVersamentoLotto", Versamento.model().COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoLotto"));
				this.setParameter(object, "setCodAnnoTributario", Versamento.model().COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"codAnnoTributario"));
				this.setParameter(object, "setCodBundlekey", Versamento.model().COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"codBundlekey"));
				this.setParameter(object, "setDatiAllegati", Versamento.model().DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"datiAllegati"));
				this.setParameter(object, "setIncasso", Versamento.model().INCASSO.getFieldType(),
					this.getObjectFromMap(map,"incasso"));
				this.setParameter(object, "setAnomalie", Versamento.model().ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"anomalie"));
				this.setParameter(object, "setIuvVersamento", Versamento.model().IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvVersamento"));
				this.setParameter(object, "setNumeroAvviso", Versamento.model().NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"numeroAvviso"));
				this.setParameter(object, "setAvvisaturaAbilitata", Versamento.model().AVVISATURA_ABILITATA.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaAbilitata"));
				this.setParameter(object, "setAvvisaturaDaInviare", Versamento.model().AVVISATURA_DA_INVIARE.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaDaInviare"));
				this.setParameter(object, "setAvvisaturaOperazione", Versamento.model().AVVISATURA_OPERAZIONE.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaOperazione"));
				this.setParameter(object, "setAvvisaturaModalita", Versamento.model().AVVISATURA_MODALITA.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaModalita"));
				this.setParameter(object, "setAvvisaturaTipoPagamento", Versamento.model().AVVISATURA_TIPO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaTipoPagamento"));
				this.setParameter(object, "setAvvisaturaCodAvvisatura", Versamento.model().AVVISATURA_COD_AVVISATURA.getFieldType(),
					this.getObjectFromMap(map,"avvisaturaCodAvvisatura"));
				this.setParameter(object, "setAck", Versamento.model().ACK.getFieldType(),
					this.getObjectFromMap(map,"ack"));
				this.setParameter(object, "setAnomalo", Versamento.model().ANOMALO.getFieldType(),
					this.getObjectFromMap(map,"anomalo"));
				setParameter(object, "setDivisione", Versamento.model().DIVISIONE.getFieldType(),
					this.getObjectFromMap(map,"divisione"));
				setParameter(object, "setDirezione", Versamento.model().DIREZIONE.getFieldType(),
					this.getObjectFromMap(map,"direzione"));
				setParameter(object, "setIdSessione", Versamento.model().ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"idSessione"));
				setParameter(object, "setDataPagamento", Versamento.model().DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataPagamento"));
				setParameter(object, "setImportoPagato", Versamento.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				setParameter(object, "setImportoIncassato", Versamento.model().IMPORTO_INCASSATO.getFieldType(),
					this.getObjectFromMap(map,"importoIncassato"));
				setParameter(object, "setStatoPagamento", Versamento.model().STATO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoPagamento"));
				setParameter(object, "setIuvPagamento", Versamento.model().IUV_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvPagamento"));
				setParameter(object, "setSrcIuv", Versamento.model().SRC_IUV.getFieldType(),
					this.getObjectFromMap(map,"srcIuv"));
				setParameter(object, "setSrcDebitoreIdentificativo", Versamento.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"srcDebitoreIdentificativo"));
				setParameter(object, "setCodRata", Versamento.model().COD_RATA.getFieldType(),
					this.getObjectFromMap(map,"codRata"));
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

			if(model.equals(Versamento.model())){
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
