/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.ragioneria.v3.beans.converter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.exceptions.IOException;
import it.govpay.ragioneria.v3.beans.NuovaRiconciliazione;
import it.govpay.ragioneria.v3.beans.Riconciliazione;
import it.govpay.ragioneria.v3.beans.RiconciliazioneIndex;
import it.govpay.ragioneria.v3.beans.Riscossione;
import it.govpay.ragioneria.v3.beans.StatoRiconciliazione;

public class RiconciliazioniConverter {


	public static RichiestaIncassoDTO toRichiestaIncassoDTO(NuovaRiconciliazione incassoPost, String idDominio, String idRiconciliazione, Authentication user) {
		RichiestaIncassoDTO dto = new RichiestaIncassoDTO(user);
		dto.setCausale(incassoPost.getCausale());
		dto.setDataValuta(incassoPost.getDataValuta());
		dto.setDataContabile(incassoPost.getDataContabile());
		dto.setImporto(incassoPost.getImporto());
		dto.setCodDominio(idDominio);
		GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
		dto.setApplicazione(authenticationDetails.getApplicazione());
		dto.setOperatore(authenticationDetails.getOperatore());
		dto.setSct(incassoPost.getSct());
		dto.setIuv(incassoPost.getIuv());
		dto.setIdFlusso(incassoPost.getIdFlussoRendicontazione());
		dto.setIbanAccredito(incassoPost.getContoAccredito());
		dto.setIdRiconciliazione(idRiconciliazione);
		return dto;
	}

	public static Riconciliazione toRsModel(it.govpay.bd.model.Incasso i) throws ServiceException, NotFoundException, IOException, UnsupportedEncodingException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Riconciliazione rsModel = new Riconciliazione();

		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setData(i.getDataIncasso());
		rsModel.setImporto(i.getImporto());
		rsModel.setId(i.getIdRiconciliazione());
		rsModel.setDominio(DominiConverter.toRsModelIndex(i.getDominio(configWrapper)));
		rsModel.setSct(i.getSct());
		rsModel.setTrn(i.getTrn());
		rsModel.setContoAccredito(i.getIbanAccredito());
		if(i.getPagamenti()!= null) {
			List<Riscossione> riscossioni = new ArrayList<>();
			for (Pagamento pagamento : i.getPagamenti()) {
				SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento();
				Versamento versamento = singoloVersamento.getVersamento(null);
				riscossioni.add(RiscossioniConverter.toRsModel(pagamento, singoloVersamento, versamento));
			}

			rsModel.setRiscossioni(riscossioni);
		}
		rsModel.setIuv(i.getIuv());
		rsModel.setIdFlussoRendicontazione(i.getIdFlussoRendicontazione());

		switch (i.getStato()) {
		case ACQUISITO:
			rsModel.setStato(StatoRiconciliazione.ACQUISITA);
			break;
		case ERRORE:
			rsModel.setStato(StatoRiconciliazione.ERRORE);
			break;
		case NUOVO:
			rsModel.setStato(StatoRiconciliazione.IN_ELABORAZIONE);
			break;
		}
		rsModel.setDescrizioneStato(i.getDescrizioneStato());

		return rsModel;
	}

	public static RiconciliazioneIndex toRsIndexModel(it.govpay.bd.model.Incasso i) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiconciliazioneIndex rsModel = new RiconciliazioneIndex();

		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setData(i.getDataIncasso());
		rsModel.setImporto(i.getImporto());
		rsModel.setId(i.getIdRiconciliazione());
		rsModel.setDominio(DominiConverter.toRsModelIndex(i.getDominio(configWrapper)));
		rsModel.setSct(i.getSct());
		rsModel.setTrn(i.getTrn());
		rsModel.setContoAccredito(i.getIbanAccredito());
		rsModel.setIuv(i.getIuv());
		rsModel.setIdFlussoRendicontazione(i.getIdFlussoRendicontazione());
		switch (i.getStato()) {
		case ACQUISITO:
			rsModel.setStato(StatoRiconciliazione.ACQUISITA);
			break;
		case ERRORE:
			rsModel.setStato(StatoRiconciliazione.ERRORE);
			break;
		case NUOVO:
			rsModel.setStato(StatoRiconciliazione.IN_ELABORAZIONE);
			break;
		}
		rsModel.setDescrizioneStato(i.getDescrizioneStato());

		return rsModel;
	}
}
