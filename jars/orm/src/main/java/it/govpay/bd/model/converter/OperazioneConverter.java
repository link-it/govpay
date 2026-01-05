/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Operazione;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdStampa;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.IdVersamento;

public class OperazioneConverter {
	
	private OperazioneConverter() {}

	public static List<Operazione> toDTOList(List<it.govpay.orm.Operazione> anagraficaLst) {
		List<Operazione> lstDTO = new ArrayList<>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Operazione anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Operazione toDTO(it.govpay.orm.Operazione vo) {
		Operazione dto = new Operazione();
		
		dto.setId(vo.getId());
		dto.setTipoOperazione(TipoOperazioneType.valueOf(vo.getTipoOperazione()));
		dto.setLineaElaborazione(vo.getLineaElaborazione());
		if(vo.getStato() != null)
			dto.setStato(StatoOperazioneType.valueOf(vo.getStato()));

		if(vo.getDatiRichiesta() != null)
			dto.setDatiRichiesta(vo.getDatiRichiesta());
		else 
			dto.setDatiRichiesta("".getBytes());
		
		if(vo.getDatiRisposta() != null)
			dto.setDatiRisposta(vo.getDatiRisposta());
		else 
			dto.setDatiRisposta("".getBytes());
		
		dto.setDettaglioEsito(vo.getDettaglioEsito());
		dto.setIdTracciato(vo.getIdTracciato().getIdTracciato());
		
		if(vo.getIdApplicazione() != null)
			dto.setIdApplicazione(vo.getIdApplicazione().getId());
		
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
		
		if(vo.getIdStampa() != null)
			dto.setIdStampa(vo.getIdStampa().getId());
		
		if(vo.getIdVersamento() != null)
			dto.setIdVersamento(vo.getIdVersamento().getId());

		return dto;
	}

	public static it.govpay.orm.Operazione toVO(Operazione dto) {
		it.govpay.orm.Operazione vo = new it.govpay.orm.Operazione();
		vo.setId(dto.getId());
		vo.setTipoOperazione(dto.getTipoOperazione().name());
		vo.setLineaElaborazione(dto.getLineaElaborazione());
		if(dto.getStato() != null)
			vo.setStato(dto.getStato().name());

		
		if(dto.getDatiRichiesta().length != 0)
			vo.setDatiRichiesta(dto.getDatiRichiesta());
		
		if(dto.getDatiRisposta().length != 0)
			vo.setDatiRisposta(dto.getDatiRisposta());
		
		vo.setDettaglioEsito(dto.getDettaglioEsito());

		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(dto.getIdTracciato());
		vo.setIdTracciato(idTracciato);

		if(dto.getIdApplicazione() != null) {
			IdApplicazione idApp = new IdApplicazione();
			idApp.setId(dto.getIdApplicazione());
			vo.setIdApplicazione(idApp);
		}

		vo.setCodDominio(dto.getCodDominio());
		vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
		
		if(dto.getIdStampa() != null) {
			IdStampa idStampa = new IdStampa();
			idStampa.setId(dto.getIdStampa());
			vo.setIdStampa(idStampa);
		}
		
		if(dto.getIdVersamento() != null && dto.getIdVersamento() > 0) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento);
		}
		
		return vo;
	}
}
