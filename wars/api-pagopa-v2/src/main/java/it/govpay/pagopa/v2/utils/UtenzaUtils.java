/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2022 Link.it srl (http://www.link.it).
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
package it.govpay.pagopa.v2.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.govpay.pagopa.v2.beans.IdUnitaOperativa;
import it.govpay.pagopa.v2.entity.UtenzaDominioEntity;
import it.govpay.pagopa.v2.entity.UtenzaEntity;


public class UtenzaUtils {

	/**
	 * Controllo che l'idDominio passato come parametro sia autorizzato, cioe' stia all'interno dell'elenco di quelli associati all'utenza 
	 * @param utenza
	 * @param idDominio
	 * @return
	 */
	public static boolean isIdDominioAutorizzato(UtenzaEntity utenza, Long idDominio) {
		List<IdUnitaOperativa> dominiUo = getIdUnitaOperativeFromEntity(utenza.getUtenzaDomini());
		return isIdDominioAutorizzato(dominiUo, idDominio); 
	}
	
	/**
	 * Controllo che l'idDominio passato come parametro sia autorizzato, cioe' stia all'interno dell'elenco di quelli associati all'utenza
	 * @param dominiUo
	 * @param idDominio
	 * @return
	 */
	private static boolean isIdDominioAutorizzato(List<IdUnitaOperativa> dominiUo, Long idDominio) {
		if(dominiUo != null) {
			for(IdUnitaOperativa id: dominiUo) {
				if(id.getIdDominio() != null) {
					if(id.getIdDominio().longValue() == idDominio.longValue())
						return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Converte la lista di entity UtenzaDominio in una lista di IdUnitaOperative.
	 * @param findAll
	 * @return
	 */
	public static List<IdUnitaOperativa> getIdUnitaOperativeFromEntity(Set<UtenzaDominioEntity> findAll){
		List<IdUnitaOperativa> toRet = new ArrayList<IdUnitaOperativa>();
		
		for (UtenzaDominioEntity utenzaDominio : findAll) {
			IdUnitaOperativa idUnita = new IdUnitaOperativa();
			
			// gestione dei null
			if(utenzaDominio.getDominio() != null) {
				idUnita.setIdDominio(utenzaDominio.getDominio().getId());
			}
			
			if(utenzaDominio.getUo() != null) {
				idUnita.setIdUnita(utenzaDominio.getUo().getId());
			}
			
			toRet.add(idUnita);
		}
		
		return toRet;
	}


}
