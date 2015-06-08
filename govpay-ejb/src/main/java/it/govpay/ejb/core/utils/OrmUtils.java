/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.core.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;

import it.govpay.ejb.core.model.CondizionePagamentoModel;
import it.govpay.ejb.core.model.DestinatarioPendenzaModel;
import it.govpay.ejb.core.model.PendenzaModel;
import it.govpay.orm.posizionedebitoria.Condizione;
import it.govpay.orm.posizionedebitoria.DestinatarioPendenza;
import it.govpay.orm.posizionedebitoria.Pendenza;
import it.govpay.orm.profilazione.CategoriaTributo;
import it.govpay.orm.profilazione.Ente;
import it.govpay.orm.profilazione.TributoEnte;
import it.govpay.orm.uuid.GeneratoreIdUnivoci;

public class OrmUtils {

	public static Pendenza buildPendenza(PendenzaModel pendenzaModel, Ente ente, TributoEnte tributoEnte, CategoriaTributo categoriaTributo, boolean hidden) {
		Timestamp adesso = new Timestamp(new Date().getTime());
		
		Pendenza pendenza = new Pendenza();

		pendenza.setIdPendenza(GeneratoreIdUnivoci.getCurrent().generaId(null));
		pendenza.setTsDecorrenza(DataTypeUtils.dateToSQLTimestamp(pendenzaModel.getDataDecorrenza()));
		pendenza.setTributoEnte(tributoEnte);
		pendenza.setCategoriaTributo(categoriaTributo);
		pendenza.setIdPendenzaente(pendenzaModel.getIdDebitoEnte());
		pendenza.setIdSystem(tributoEnte.getSIL());
		pendenza.setTsCreazioneente(DataTypeUtils.dateToSQLTimestamp(pendenzaModel.getDataCreazione()));
		pendenza.setTsEmissioneente(DataTypeUtils.dateToSQLTimestamp(pendenzaModel.getDataEmissione()));
		pendenza.setTsPrescrizione(DataTypeUtils.dateToSQLTimestamp(pendenzaModel.getDataPrescrizione()));
		pendenza.setTsModificaente(null); // è un inserimento
		pendenza.setCoRiscossore(null);
		pendenza.setRiferimento(null);
		pendenza.setDvRiferimento(GovPayConstants.DIVISA);
		pendenza.setAnnoRiferimento(pendenzaModel.getAnnoRiferimento());
		pendenza.setNote(null);
		pendenza.setImTotale(pendenzaModel.getImportoTotale());
		pendenza.setCoAbi(null);
		pendenza.setIdMittente(GovPayConstants.GOVPAY_NAME);
		pendenza.setDeMittente(null);
		pendenza.setDeCausale(pendenzaModel.getCausale());
		pendenza.setStPendenza(pendenzaModel.getStatoPendenza().name());
		pendenza.setFlModPagamento(pendenzaModel.getModalitaPagamento().name());
		pendenza.setStRiga(hidden ? GovPayConstants.ST_RIGA_NASCOSTO : GovPayConstants.ST_RIGA_VISIBILE);
		pendenza.setPrVersione(0);
		pendenza.setOpInserimento(GovPayConstants.OP_INSERIMENTO);
		pendenza.setTsInserimento(adesso);
		pendenza.setCartellaPagamento(null);
		
		//
		// Destinatari
		//
		if (pendenzaModel.getDebitore() != null) {
			pendenza.setDestinatari(new HashSet<DestinatarioPendenza>());
			// creo il destinatario
			DestinatarioPendenza destinatarioPendenza = new DestinatarioPendenza();

			destinatarioPendenza.setIdDestinatario(GeneratoreIdUnivoci.getCurrent().generaId(null));
			destinatarioPendenza.setPendenza(pendenza);
			destinatarioPendenza.setTsDecorrenza(DataTypeUtils.dateToSQLTimestamp(pendenzaModel.getDataDecorrenza()));
			destinatarioPendenza.setCoDestinatario(pendenzaModel.getDebitore().getIdFiscale());
			destinatarioPendenza.setDeDestinatario(pendenzaModel.getDebitore().getAnagrafica());
			destinatarioPendenza.setTiDestinatario(DestinatarioPendenzaModel.EnumTipoDestinatario.CI.name());
			destinatarioPendenza.setStRiga(hidden ? GovPayConstants.ST_RIGA_NASCOSTO : GovPayConstants.ST_RIGA_VISIBILE);
			destinatarioPendenza.setPrVersione(0);
			destinatarioPendenza.setOpInserimento(GovPayConstants.OP_INSERIMENTO);
			destinatarioPendenza.setTsInserimento(adesso);
			// lo aggiungo al set
			pendenza.getDestinatari().add(destinatarioPendenza);
		}

		//
		// Condizioni
		//
		if (pendenzaModel.getCondizioniPagamento() != null) {
			pendenza.setCondizioni(new HashSet<Condizione>());
			for (CondizionePagamentoModel condizionePojo : pendenzaModel.getCondizioniPagamento()) {
				// creo la condizione
				Condizione condizione = new Condizione();
				
				condizione.setIdCondizione(GeneratoreIdUnivoci.getCurrent().generaId(null));
				condizione.setPendenza(pendenza);
				condizione.setTsDecorrenza(DataTypeUtils.dateToSQLTimestamp(condizionePojo.getDataDecorrenza()));
				condizione.setTiPagamento(condizionePojo.getTipoPagamento().name());
				condizione.setEnte(ente);
				condizione.setCdTrbEnte(tributoEnte.getCdTrbEnte());
				condizione.setIdPagamento(condizionePojo.getIdPagamentoEnte());
				condizione.setTiCip(null);
				condizione.setCoCip(null);
				condizione.setDtScadenza(DataTypeUtils.dateToSQLTimestamp(condizionePojo.getDataScadenza()));
				condizione.setDtIniziovalidita(DataTypeUtils.dateToSQLTimestamp(condizionePojo.getDataInizioValidita()));
				condizione.setDtFinevalidita(DataTypeUtils.dateToSQLTimestamp(condizionePojo.getDataFineValidita()));
				condizione.setImTotale(condizionePojo.getImportoTotale());
				condizione.setStPagamento(CondizionePagamentoModel.EnumStatoPagamento.N.name());
				condizione.setDtPagamento(null);
				condizione.setDeCanalepag(null);
				condizione.setDeMezzoPagamento(null);
				condizione.setDeNotePagamento(null);
				condizione.setImPagamento(null);
				condizione.setIbanBeneficiario(condizionePojo.getIbanBeneficiario());
				condizione.setRagioneSocaleBeneficiario(condizionePojo.getRagioneSocaleBeneficiario());
				condizione.setCausalePagamento(condizionePojo.getCausale());
				condizione.setStRiga(hidden ? GovPayConstants.ST_RIGA_NASCOSTO : GovPayConstants.ST_RIGA_VISIBILE);
				condizione.setPrVersione(0);
				condizione.setOpInserimento(GovPayConstants.OP_INSERIMENTO);
				condizione.setTsInserimento(adesso);
				// la aggiungo al set
				pendenza.getCondizioni().add(condizione);
				// setto l'id condizione creata nel pojo
				condizionePojo.setIdCondizione(condizione.getIdCondizione());
			}
		}
		return pendenza;
	}

}
