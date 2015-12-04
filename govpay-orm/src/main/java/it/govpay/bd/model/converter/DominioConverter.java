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
package it.govpay.bd.model.converter;

import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Periodo;
import it.govpay.bd.model.Disponibilita.TipoDisponibilita;
import it.govpay.bd.model.Disponibilita.TipoPeriodo;
import it.govpay.bd.model.Dominio;
import it.govpay.orm.IdStazione;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class DominioConverter {

	private static final String FASCIA_SEPARATOR = "\\|";
	private static final String FASCIA_FIELD_SEPARATOR = ",";

	private static final SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");


	public static List<Dominio> toDTOList(List<it.govpay.orm.Dominio> anagraficaLst) throws ServiceException {
		List<Dominio> lstDTO = new ArrayList<Dominio>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Dominio anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Dominio toDTO(it.govpay.orm.Dominio vo) throws ServiceException {
		Dominio dto = new Dominio();
		dto.setId(vo.getId());
		dto.setCodDominio(vo.getCodDominio());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setGln(vo.getGln());
		dto.setPluginClass(vo.getPluginClass());
		dto.setAbilitato(vo.isAbilitato());
		dto.setIdStazione(vo.getIdStazione().getId());
		dto.setDisponibilita(toDTOListDisponibilita(vo.getDisponibilitaList()));

		return dto;
	}

	public static it.govpay.orm.Dominio toVO(Dominio dto) throws ServiceException {
		it.govpay.orm.Dominio vo = new it.govpay.orm.Dominio();
		vo.setId(dto.getId());
		vo.setCodDominio(dto.getCodDominio());
		vo.setRagioneSociale(dto.getRagioneSociale());
		vo.setGln(dto.getGln());
		vo.setPluginClass(dto.getPluginClass());
		vo.setAbilitato(dto.isAbilitato());

		IdStazione idStazione = new IdStazione();
		idStazione.setId(dto.getIdStazione());
		vo.setIdStazione(idStazione);
		vo.setDisponibilitaList(toVOList(dto.getDisponibilita()));
		return vo;
	}

	public static List<it.govpay.orm.Disponibilita> toVOList(List<Disponibilita> lstVO) throws ServiceException {
		List<it.govpay.orm.Disponibilita> lstDTO = new ArrayList<it.govpay.orm.Disponibilita>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(Disponibilita anagrafica: lstVO){
				lstDTO.add(toVO(anagrafica));
			}
		}
		return lstDTO;

	}

	public static it.govpay.orm.Disponibilita toVO(Disponibilita dto) throws ServiceException {
		it.govpay.orm.Disponibilita vo = new it.govpay.orm.Disponibilita();
		vo.setId(dto.getId());
		vo.setTipoPeriodo(dto.getTipoPeriodo().toString());
		vo.setGiorno(dto.getGiorno());
		if(dto.getFasceOrarieLst() != null && !dto.getFasceOrarieLst().isEmpty()) {
			StringBuffer sb = new StringBuffer();
			for(Periodo fascia: dto.getFasceOrarieLst()) {
				if(sb.length() > 0) {
					sb.append(FASCIA_SEPARATOR);
				}
				sb.append(fromFasciaOraria(fascia));
			}
			vo.setFasceOrarie(sb.toString());
		}
		vo.setTipoDisponibilita(dto.getTipoDisponibilita().toString());

		return vo;
	}

	public static List<Disponibilita> toDTOListDisponibilita(List<it.govpay.orm.Disponibilita> lst) throws ServiceException {
		List<Disponibilita> lstDTO = new ArrayList<Disponibilita>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.Disponibilita anagrafica: lst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Disponibilita toDTO(it.govpay.orm.Disponibilita vo) throws ServiceException {
		Disponibilita dto = new Disponibilita();

		dto.setId(vo.getId());
		dto.setTipoPeriodo(TipoPeriodo.valueOf(vo.getTipoPeriodo()));
		dto.setGiorno(vo.getGiorno());

		String[] fasce = vo.getFasceOrarie().split(FASCIA_SEPARATOR);
		List<Periodo> periodoLst = new ArrayList<Periodo>();
		for (int i = 0; i < fasce.length; i++) {
			String[] fascia = fasce[i].split(FASCIA_FIELD_SEPARATOR);
			Periodo periodo = toFasciaOraria(fascia);
			periodoLst.add(periodo);
		}
		dto.setFasceOrarieLst(periodoLst);

		dto.setTipoDisponibilita(TipoDisponibilita.valueOf(vo.getTipoDisponibilita()));

		return dto;

	}
	
	private static String fromFasciaOraria(Periodo periodo) throws ServiceException {
		
		if(periodo == null) throw new ServiceException("Fascia oraria nulla"); 
		if(periodo.getDa()== null) throw new ServiceException("Fascia oraria ["+periodo+"] non corretta. Parametro Da nullo"); 
		if(periodo.getA()== null) throw new ServiceException("Fascia oraria ["+periodo+"] non corretta. Parametro A nullo"); 
		Date da = null;
		try{
			da = hourFormat.parse(periodo.getDa());
		} catch(ParseException e) {
			throw new ServiceException("Fascia oraria ["+periodo+"] non corretta. Parametro Da ["+periodo.getDa()+"] errato");
		}
		Date a = null;
		try{
			a = hourFormat.parse(periodo.getA());
		} catch(ParseException e) {
			throw new ServiceException("Fascia oraria ["+periodo+"] non corretta. Parametro A ["+periodo.getA()+"] errato");
		}
		
		if(da.after(a)) throw new ServiceException("Periodo non corretto: il parametro Da ["+periodo.getDa()+"] deve essere antecedente al parametro A ["+periodo.getA()+"]");

		return periodo.getDa() + FASCIA_FIELD_SEPARATOR + periodo.getA();
	}

	private static Periodo toFasciaOraria(String[] fascia) throws ServiceException {
		if(fascia.length != 2) {
			throw new ServiceException("Periodo ["+fascia+"] non correttamente salvato");
		}
		Periodo periodo = new Periodo();
		
		Date da = null;
		try{
			da = hourFormat.parse(fascia[0]);
			periodo.setDa(fascia[0]);
		} catch(ParseException e) {
			throw new ServiceException("Fascia oraria ["+fascia[0]+"] non correttamente salvata. Dovrebbe essere nel formato ["+hourFormat.toPattern()+"]");
		}
		
		Date a = null;
		try{
			a = hourFormat.parse(fascia[1]);
			periodo.setA(fascia[1]);
		} catch(ParseException e) {
			throw new ServiceException("Fascia oraria ["+fascia[1]+"] non correttamente salvata. Dovrebbe essere nel formato ["+hourFormat.toPattern()+"]");
		}
		
		if(da.after(a)) throw new ServiceException("Periodo non corretto: il parametro Da ["+fascia[0]+"] deve essere antecedente al parametro A ["+fascia[1]+"]");
		
		return periodo;
	}


}
