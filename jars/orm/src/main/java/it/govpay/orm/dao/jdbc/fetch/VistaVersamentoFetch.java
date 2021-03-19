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

import it.govpay.orm.VistaVersamento;


/**     
 * VistaVersamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaVersamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaVersamento.model())){
				VistaVersamento object = new VistaVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodVersamentoEnte", VistaVersamento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", VistaVersamento.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setNome", VistaVersamento.model().NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome", VistaVersamento.model().NOME.getFieldType()));
				setParameter(object, "setImportoTotale", VistaVersamento.model().IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", VistaVersamento.model().IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setStatoVersamento", VistaVersamento.model().STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", VistaVersamento.model().STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizioneStato", VistaVersamento.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", VistaVersamento.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setAggiornabile", VistaVersamento.model().AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornabile", VistaVersamento.model().AGGIORNABILE.getFieldType()));
				setParameter(object, "setDataCreazione", VistaVersamento.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", VistaVersamento.model().DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setDataValidita", VistaVersamento.model().DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_validita", VistaVersamento.model().DATA_VALIDITA.getFieldType()));
				setParameter(object, "setDataScadenza", VistaVersamento.model().DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", VistaVersamento.model().DATA_SCADENZA.getFieldType()));
				setParameter(object, "setDataOraUltimoAggiornamento", VistaVersamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultimo_aggiornamento", VistaVersamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				setParameter(object, "setCausaleVersamento", VistaVersamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", VistaVersamento.model().CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setDebitoreTipo", VistaVersamento.model().DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_tipo", VistaVersamento.model().DEBITORE_TIPO.getFieldType()));
				setParameter(object, "setDebitoreIdentificativo", VistaVersamento.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", VistaVersamento.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setDebitoreAnagrafica", VistaVersamento.model().DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", VistaVersamento.model().DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setDebitoreIndirizzo", VistaVersamento.model().DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_indirizzo", VistaVersamento.model().DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setDebitoreCivico", VistaVersamento.model().DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_civico", VistaVersamento.model().DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setDebitoreCap", VistaVersamento.model().DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cap", VistaVersamento.model().DEBITORE_CAP.getFieldType()));
				setParameter(object, "setDebitoreLocalita", VistaVersamento.model().DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_localita", VistaVersamento.model().DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setDebitoreProvincia", VistaVersamento.model().DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_provincia", VistaVersamento.model().DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setDebitoreNazione", VistaVersamento.model().DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_nazione", VistaVersamento.model().DEBITORE_NAZIONE.getFieldType()));
				setParameter(object, "setDebitoreEmail", VistaVersamento.model().DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_email", VistaVersamento.model().DEBITORE_EMAIL.getFieldType()));
				setParameter(object, "setDebitoreTelefono", VistaVersamento.model().DEBITORE_TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_telefono", VistaVersamento.model().DEBITORE_TELEFONO.getFieldType()));
				setParameter(object, "setDebitoreCellulare", VistaVersamento.model().DEBITORE_CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cellulare", VistaVersamento.model().DEBITORE_CELLULARE.getFieldType()));
				setParameter(object, "setDebitoreFax", VistaVersamento.model().DEBITORE_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_fax", VistaVersamento.model().DEBITORE_FAX.getFieldType()));
				setParameter(object, "setTassonomiaAvviso", VistaVersamento.model().TASSONOMIA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia_avviso", VistaVersamento.model().TASSONOMIA_AVVISO.getFieldType()));
				setParameter(object, "setTassonomia", VistaVersamento.model().TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia", VistaVersamento.model().TASSONOMIA.getFieldType()));
				setParameter(object, "setCodLotto", VistaVersamento.model().COD_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_lotto", VistaVersamento.model().COD_LOTTO.getFieldType()));
				setParameter(object, "setCodVersamentoLotto", VistaVersamento.model().COD_VERSAMENTO_LOTTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_lotto", VistaVersamento.model().COD_VERSAMENTO_LOTTO.getFieldType()));
				setParameter(object, "setCodAnnoTributario", VistaVersamento.model().COD_ANNO_TRIBUTARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_anno_tributario", VistaVersamento.model().COD_ANNO_TRIBUTARIO.getFieldType()));
				setParameter(object, "setCodBundlekey", VistaVersamento.model().COD_BUNDLEKEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bundlekey", VistaVersamento.model().COD_BUNDLEKEY.getFieldType()));
				setParameter(object, "setDatiAllegati", VistaVersamento.model().DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", VistaVersamento.model().DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setIncasso", VistaVersamento.model().INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "incasso", VistaVersamento.model().INCASSO.getFieldType()));
				setParameter(object, "setAnomalie", VistaVersamento.model().ANOMALIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalie", VistaVersamento.model().ANOMALIE.getFieldType()));
				setParameter(object, "setIuvVersamento", VistaVersamento.model().IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_versamento", VistaVersamento.model().IUV_VERSAMENTO.getFieldType()));
				setParameter(object, "setNumeroAvviso", VistaVersamento.model().NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_avviso", VistaVersamento.model().NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setAck", VistaVersamento.model().ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ack", VistaVersamento.model().ACK.getFieldType()));
				setParameter(object, "setAnomalo", VistaVersamento.model().ANOMALO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anomalo", VistaVersamento.model().ANOMALO.getFieldType()));
				setParameter(object, "setDivisione", VistaVersamento.model().DIVISIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "divisione", VistaVersamento.model().DIVISIONE.getFieldType()));
				setParameter(object, "setDirezione", VistaVersamento.model().DIREZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "direzione", VistaVersamento.model().DIREZIONE.getFieldType()));
				setParameter(object, "setIdSessione", VistaVersamento.model().ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione", VistaVersamento.model().ID_SESSIONE.getFieldType()));
				setParameter(object, "setDataPagamento", VistaVersamento.model().DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", VistaVersamento.model().DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setImportoPagato", VistaVersamento.model().IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", VistaVersamento.model().IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setImportoIncassato", VistaVersamento.model().IMPORTO_INCASSATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_incassato", VistaVersamento.model().IMPORTO_INCASSATO.getFieldType()));
				setParameter(object, "setStatoPagamento", VistaVersamento.model().STATO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_pagamento", VistaVersamento.model().STATO_PAGAMENTO.getFieldType()));
				setParameter(object, "setIuvPagamento", VistaVersamento.model().IUV_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_pagamento", VistaVersamento.model().IUV_PAGAMENTO.getFieldType()));
				setParameter(object, "setSrcIuv", VistaVersamento.model().SRC_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "src_iuv", VistaVersamento.model().SRC_IUV.getFieldType()));
				setParameter(object, "setSrcDebitoreIdentificativo", VistaVersamento.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "src_debitore_identificativo", VistaVersamento.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setCodRata", VistaVersamento.model().COD_RATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_rata", VistaVersamento.model().COD_RATA.getFieldType()));
				setParameter(object, "setTipo", VistaVersamento.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", VistaVersamento.model().TIPO.getFieldType()));
				setParameter(object, "setDataNotificaAvviso", VistaVersamento.model().DATA_NOTIFICA_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_notifica_avviso", VistaVersamento.model().DATA_NOTIFICA_AVVISO.getFieldType()));
				setParameter(object, "setAvvisoNotificato", VistaVersamento.model().AVVISO_NOTIFICATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avviso_notificato", VistaVersamento.model().AVVISO_NOTIFICATO.getFieldType()));
				setParameter(object, "setAvvMailDataPromScadenza", VistaVersamento.model().AVV_MAIL_DATA_PROM_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_data_prom_scadenza", VistaVersamento.model().AVV_MAIL_DATA_PROM_SCADENZA.getFieldType()));
				setParameter(object, "setAvvMailPromScadNotificato", VistaVersamento.model().AVV_MAIL_PROM_SCAD_NOTIFICATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_notificato", VistaVersamento.model().AVV_MAIL_PROM_SCAD_NOTIFICATO.getFieldType()));
				setParameter(object, "setAvvAppIoDataPromScadenza", VistaVersamento.model().AVV_APP_IO_DATA_PROM_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_data_prom_scadenza", VistaVersamento.model().AVV_APP_IO_DATA_PROM_SCADENZA.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadNotificato", VistaVersamento.model().AVV_APP_IO_PROM_SCAD_NOTIFICATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_notificat", VistaVersamento.model().AVV_APP_IO_PROM_SCAD_NOTIFICATO.getFieldType()));
				setParameter(object, "setCodDocumento", VistaVersamento.model().COD_DOCUMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_documento", VistaVersamento.model().COD_DOCUMENTO.getFieldType()));
				setParameter(object, "setDocDescrizione", VistaVersamento.model().DOC_DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "doc_descrizione", VistaVersamento.model().DOC_DESCRIZIONE.getFieldType()));
				setParameter(object, "setProprieta", VistaVersamento.model().PROPRIETA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "proprieta", VistaVersamento.model().PROPRIETA.getFieldType()));
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

			if(model.equals(VistaVersamento.model())){
				VistaVersamento object = new VistaVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodVersamentoEnte", VistaVersamento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setNome", VistaVersamento.model().NOME.getFieldType(),
					this.getObjectFromMap(map,"nome"));
				setParameter(object, "setImportoTotale", VistaVersamento.model().IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"importoTotale"));
				setParameter(object, "setStatoVersamento", VistaVersamento.model().STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoVersamento"));
				setParameter(object, "setDescrizioneStato", VistaVersamento.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setAggiornabile", VistaVersamento.model().AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"aggiornabile"));
				setParameter(object, "setDataCreazione", VistaVersamento.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				setParameter(object, "setDataValidita", VistaVersamento.model().DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"dataValidita"));
				setParameter(object, "setDataScadenza", VistaVersamento.model().DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"dataScadenza"));
				setParameter(object, "setDataOraUltimoAggiornamento", VistaVersamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataOraUltimoAggiornamento"));
				setParameter(object, "setCausaleVersamento", VistaVersamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"causaleVersamento"));
				setParameter(object, "setDebitoreTipo", VistaVersamento.model().DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"debitoreTipo"));
				setParameter(object, "setDebitoreIdentificativo", VistaVersamento.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIdentificativo"));
				setParameter(object, "setDebitoreAnagrafica", VistaVersamento.model().DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"debitoreAnagrafica"));
				setParameter(object, "setDebitoreIndirizzo", VistaVersamento.model().DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIndirizzo"));
				setParameter(object, "setDebitoreCivico", VistaVersamento.model().DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"debitoreCivico"));
				setParameter(object, "setDebitoreCap", VistaVersamento.model().DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"debitoreCap"));
				setParameter(object, "setDebitoreLocalita", VistaVersamento.model().DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"debitoreLocalita"));
				setParameter(object, "setDebitoreProvincia", VistaVersamento.model().DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"debitoreProvincia"));
				setParameter(object, "setDebitoreNazione", VistaVersamento.model().DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"debitoreNazione"));
				setParameter(object, "setDebitoreEmail", VistaVersamento.model().DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"debitoreEmail"));
				setParameter(object, "setDebitoreTelefono", VistaVersamento.model().DEBITORE_TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"debitoreTelefono"));
				setParameter(object, "setDebitoreCellulare", VistaVersamento.model().DEBITORE_CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"debitoreCellulare"));
				setParameter(object, "setDebitoreFax", VistaVersamento.model().DEBITORE_FAX.getFieldType(),
					this.getObjectFromMap(map,"debitoreFax"));
				setParameter(object, "setTassonomiaAvviso", VistaVersamento.model().TASSONOMIA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"tassonomiaAvviso"));
				setParameter(object, "setTassonomia", VistaVersamento.model().TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"tassonomia"));
				setParameter(object, "setCodLotto", VistaVersamento.model().COD_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"codLotto"));
				setParameter(object, "setCodVersamentoLotto", VistaVersamento.model().COD_VERSAMENTO_LOTTO.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoLotto"));
				setParameter(object, "setCodAnnoTributario", VistaVersamento.model().COD_ANNO_TRIBUTARIO.getFieldType(),
					this.getObjectFromMap(map,"codAnnoTributario"));
				setParameter(object, "setCodBundlekey", VistaVersamento.model().COD_BUNDLEKEY.getFieldType(),
					this.getObjectFromMap(map,"codBundlekey"));
				setParameter(object, "setDatiAllegati", VistaVersamento.model().DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"datiAllegati"));
				setParameter(object, "setIncasso", VistaVersamento.model().INCASSO.getFieldType(),
					this.getObjectFromMap(map,"incasso"));
				setParameter(object, "setAnomalie", VistaVersamento.model().ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"anomalie"));
				setParameter(object, "setIuvVersamento", VistaVersamento.model().IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvVersamento"));
				setParameter(object, "setNumeroAvviso", VistaVersamento.model().NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"numeroAvviso"));
				setParameter(object, "setAck", VistaVersamento.model().ACK.getFieldType(),
					this.getObjectFromMap(map,"ack"));
				setParameter(object, "setAnomalo", VistaVersamento.model().ANOMALO.getFieldType(),
					this.getObjectFromMap(map,"anomalo"));
				setParameter(object, "setDivisione", VistaVersamento.model().DIVISIONE.getFieldType(),
					this.getObjectFromMap(map,"divisione"));
				setParameter(object, "setDirezione", VistaVersamento.model().DIREZIONE.getFieldType(),
					this.getObjectFromMap(map,"direzione"));
				setParameter(object, "setIdSessione", VistaVersamento.model().ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"idSessione"));
				setParameter(object, "setDataPagamento", VistaVersamento.model().DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataPagamento"));
				setParameter(object, "setImportoPagato", VistaVersamento.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				setParameter(object, "setImportoIncassato", VistaVersamento.model().IMPORTO_INCASSATO.getFieldType(),
					this.getObjectFromMap(map,"importoIncassato"));
				setParameter(object, "setStatoPagamento", VistaVersamento.model().STATO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoPagamento"));
				setParameter(object, "setIuvPagamento", VistaVersamento.model().IUV_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvPagamento"));
				setParameter(object, "setSrcIuv", VistaVersamento.model().SRC_IUV.getFieldType(),
					this.getObjectFromMap(map,"srcIuv"));
				setParameter(object, "setSrcDebitoreIdentificativo", VistaVersamento.model().SRC_DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"srcDebitoreIdentificativo"));
				setParameter(object, "setCodRata", VistaVersamento.model().COD_RATA.getFieldType(),
					this.getObjectFromMap(map,"codRata"));
				setParameter(object, "setTipo", VistaVersamento.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setDataNotificaAvviso", VistaVersamento.model().DATA_NOTIFICA_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"dataNotificaAvviso"));
				setParameter(object, "setAvvisoNotificato", VistaVersamento.model().AVVISO_NOTIFICATO.getFieldType(),
					this.getObjectFromMap(map,"avvisoNotificato"));
				setParameter(object, "setAvvMailDataPromScadenza", VistaVersamento.model().AVV_MAIL_DATA_PROM_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"avvMailDataPromScadenza"));
				setParameter(object, "setAvvMailPromScadNotificato", VistaVersamento.model().AVV_MAIL_PROM_SCAD_NOTIFICATO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadNotificato"));
				setParameter(object, "setAvvAppIoDataPromScadenza", VistaVersamento.model().AVV_APP_IO_DATA_PROM_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoDataPromScadenza"));
				setParameter(object, "setAvvAppIoPromScadNotificato", VistaVersamento.model().AVV_APP_IO_PROM_SCAD_NOTIFICATO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadNotificato"));
				setParameter(object, "setCodDocumento", VistaVersamento.model().COD_DOCUMENTO.getFieldType(),
					this.getObjectFromMap(map,"codDocumento"));
				setParameter(object, "setDocDescrizione", VistaVersamento.model().DOC_DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"docDescrizione"));
				setParameter(object, "setProprieta", VistaVersamento.model().PROPRIETA.getFieldType(),
					this.getObjectFromMap(map,"proprieta"));
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

			if(model.equals(VistaVersamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_versamenti","id","seq_v_versamenti","v_versamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
