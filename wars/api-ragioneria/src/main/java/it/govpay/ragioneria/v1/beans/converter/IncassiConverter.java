/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.ragioneria.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Pagamento;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.exceptions.IOException;
import it.govpay.ragioneria.v1.beans.Incasso;
import it.govpay.ragioneria.v1.beans.IncassoIndex;
import it.govpay.ragioneria.v1.beans.IncassoPost;
import it.govpay.ragioneria.v1.beans.Riscossione;

public class IncassiConverter {


	public static RichiestaIncassoDTO toRichiestaIncassoDTO(IncassoPost incassoPost, String idDominio, Authentication user) {
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
		dto.setIdFlusso(incassoPost.getIdFlusso());
		return dto;
	}


	public static Incasso toRsModel(it.govpay.bd.model.Incasso i) throws NotFoundException, IOException {
		Incasso rsModel = new Incasso();

		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto());
		rsModel.setIdIncasso(i.getTrn());
		rsModel.setIdDominio(i.getCodDominio());
		rsModel.setSct(i.getSct());
		rsModel.setIbanAccredito(i.getIbanAccredito());
		if(i.getPagamenti()!= null) {
			List<Riscossione> riscossioni = new ArrayList<>();
			for (Pagamento pagamento : i.getPagamenti()) {
				riscossioni.add(RiscossioniConverter.toRsModel(pagamento));
			}

			rsModel.setRiscossioni(riscossioni);
		}

		return rsModel;
	}

	public static IncassoIndex toRsIndexModel(it.govpay.bd.model.Incasso i) {
		IncassoIndex rsModel = new IncassoIndex();

		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto());
		rsModel.setIdIncasso(i.getTrn());
		rsModel.setIdDominio(i.getCodDominio());
		rsModel.setSct(i.getSct());
		rsModel.setIbanAccredito(i.getIbanAccredito());

		return rsModel;
	}
}
