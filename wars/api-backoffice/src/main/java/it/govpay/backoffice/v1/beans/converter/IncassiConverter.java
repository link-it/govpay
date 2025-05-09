/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Incasso;
import it.govpay.backoffice.v1.beans.IncassoIndex;
import it.govpay.backoffice.v1.beans.IncassoPost;
import it.govpay.backoffice.v1.beans.Riscossione;
import it.govpay.backoffice.v1.beans.StatoIncasso;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.exceptions.IOException;

public class IncassiConverter {

	private IncassiConverter() {}

	public static RichiestaIncassoDTO toRichiestaIncassoDTO(IncassoPost incassoPost, String idDominio, Authentication user) {
		RichiestaIncassoDTO dto = new RichiestaIncassoDTO(user);
		dto.setCausale(incassoPost.getCausale());
		dto.setIuv(incassoPost.getIuv());
		dto.setIdFlusso(incassoPost.getIdFlusso());
		dto.setDataValuta(incassoPost.getDataValuta());
		dto.setDataContabile(incassoPost.getDataContabile());
		dto.setImporto(incassoPost.getImporto());
		dto.setCodDominio(idDominio);
		dto.setSct(incassoPost.getSct());
		return dto;
	}


	public static Incasso toRsModel(it.govpay.bd.model.Incasso i) throws ServiceException, IOException {
		Incasso rsModel = new Incasso();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto());
		rsModel.setIdIncasso(i.getIdRiconciliazione());
		rsModel.setDominio(DominiConverter.toRsModelIndex(i.getDominio(configWrapper)));
		rsModel.setData(i.getDataIncasso());

		rsModel.setIbanAccredito(i.getIbanAccredito());
		if(i.getPagamenti()!= null) {
			List<Riscossione> riscossioni = new ArrayList<>();
			for (Pagamento pagamento : i.getPagamenti()) {
				SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(null);
				Versamento versamento = singoloVersamento.getVersamentoBD(null);
				Rpt rpt = pagamento.getRpt(null);
				riscossioni.add(RiscossioniConverter.toRsModel(pagamento, singoloVersamento, versamento, rpt, i));
			}

			rsModel.setRiscossioni(riscossioni);
		}

		rsModel.setSct(i.getSct());
		rsModel.setIuv(i.getIuv());
		rsModel.setIdFlusso(i.getIdFlussoRendicontazione());
		if(i.getStato() != null) {
			switch (i.getStato()) {
			case ACQUISITO:
				rsModel.setStato(StatoIncasso.ACQUISITO);
				break;
			case ERRORE:
				rsModel.setStato(StatoIncasso.ERRORE);
				break;
			case NUOVO:
				rsModel.setStato(StatoIncasso.IN_ELABORAZIONE);
				break;
			}
		}
		rsModel.setDescrizioneStato(i.getDescrizioneStato());

		return rsModel;
	}

	public static IncassoIndex toRsIndexModel(it.govpay.bd.model.Incasso i) throws ServiceException {
		IncassoIndex rsModel = new IncassoIndex();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto());
		rsModel.setIdIncasso(i.getIdRiconciliazione());
		rsModel.setDominio(DominiConverter.toRsModelIndex(i.getDominio(configWrapper)));
		rsModel.setData(i.getDataIncasso());
		rsModel.setIbanAccredito(i.getIbanAccredito());
		rsModel.setSct(i.getSct());
		rsModel.setIuv(i.getIuv());
		rsModel.setIdFlusso(i.getIdFlussoRendicontazione());
		if(i.getStato() != null) {
			switch (i.getStato()) {
			case ACQUISITO:
				rsModel.setStato(StatoIncasso.ACQUISITO);
				break;
			case ERRORE:
				rsModel.setStato(StatoIncasso.ERRORE);
				break;
			case NUOVO:
				rsModel.setStato(StatoIncasso.IN_ELABORAZIONE);
				break;
			}
		}
		rsModel.setDescrizioneStato(i.getDescrizioneStato());

		return rsModel;
	}
}
