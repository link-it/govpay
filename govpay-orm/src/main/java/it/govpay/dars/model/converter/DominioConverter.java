/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.dars.model.converter;

import it.govpay.bd.model.ContoAccredito;
import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Disponibilita.TipoDisponibilita;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.TabellaControparti;
import it.govpay.dars.model.DominioExt;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class DominioConverter {

	public static List<Dominio> toDTOList(List<DominioExt> anagraficaLst) throws ServiceException {
		List<Dominio> lstDTO = new ArrayList<Dominio>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(DominioExt anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Dominio toDTO(DominioExt vo) throws ServiceException {
		Dominio dto = new Dominio();
		dto.setId(vo.getId());
		dto.setCodDominio(vo.getCodDominio());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setGln(vo.getGln());
		dto.setPluginClass(vo.getPluginClass());
		dto.setAbilitato(vo.isAbilitato());
		dto.setIdStazione(vo.getIdStazione());
		dto.setDisponibilita(vo.getDisponibilita());
		dto.getDisponibilita().addAll(vo.getIndisponibilita());

		return dto;
	}

	public static DominioExt toDominioExt(Dominio dominioDTO, Stazione stazione, List<TabellaControparti> tabelleControparti, List<ContoAccredito> contiAccredito, List<IbanAccredito> ibanAccredito) {
		DominioExt dominio = new DominioExt();
		
		dominio.setId(dominioDTO.getId());
		dominio.setCodDominio(dominioDTO.getCodDominio());
		dominio.setGln(dominioDTO.getGln());
		dominio.setIdStazione(dominioDTO.getIdStazione());
		dominio.setRagioneSociale(dominioDTO.getRagioneSociale());
		List<Disponibilita> indisponibilitaLst = new ArrayList<Disponibilita>();
		List<Disponibilita> disponibilitaLst = new ArrayList<Disponibilita>();
		for(Disponibilita disponibilita: dominioDTO.getDisponibilita()) {
			if(TipoDisponibilita.DISPONIBILE.equals(disponibilita.getTipoDisponibilita())) {
				disponibilitaLst.add(disponibilita);
			} else {
				indisponibilitaLst.add(disponibilita);
			}
		}
		dominio.setIndisponibilita(indisponibilitaLst);
		dominio.setDisponibilita(disponibilitaLst);
		dominio.setPluginClass(dominioDTO.getPluginClass());
		dominio.setAbilitato(dominioDTO.isAbilitato());
		
		dominio.setStazione(stazione);
		dominio.setControparti(tabelleControparti);
		dominio.setIbanAccredito(ibanAccredito);
		dominio.setContiAccredito(contiAccredito);
		
		return dominio;
	}


}
