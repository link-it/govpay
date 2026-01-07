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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.certificate.CertificateUtils;
import org.openspcoop2.utils.certificate.PrincipalType;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Utenza;
import it.govpay.model.IdUnitaOperativa;

public class UtenzaConverter {
	
	private UtenzaConverter() {}
	
	public static Utenza toDTO(it.govpay.orm.Utenza vo, List<IdUnitaOperativa> utenzaDominioLst, List<Long> utenzaTipiVersamentoLst, BDConfigWrapper configWrapper) throws ServiceException {
		Utenza dto = new Utenza();
		dto.setPrincipal(vo.getPrincipal());
		dto.setPrincipalOriginale(vo.getPrincipalOriginale());
		dto.setAutorizzazioneDominiStar(vo.isAutorizzazioneDominiStar());
		dto.setAutorizzazioneTipiVersamentoStar(vo.isAutorizzazioneTipiVersStar());
		dto.setId(vo.getId());
		dto.setAbilitato(vo.isAbilitato());
		dto.setIdTipiVersamento(utenzaTipiVersamentoLst);
		dto.setIdDominiUo(utenzaDominioLst);
		dto.getDominiUo(configWrapper);
		dto.getTipiVersamento(configWrapper);
		if(StringUtils.isNotBlank(vo.getRuoli())){ 
			dto.setRuoli(Arrays.asList(vo.getRuoli().split(",")));
		}
		dto.setPassword(vo.getPassword());

		return dto;
	}

	public static Utenza toDTO(it.govpay.orm.Utenza vo, List<IdUnitaOperativa> utenzaDominioLst, List<Long> utenzaTipiVersamentoLst, BasicBD bd) {
		Utenza dto = new Utenza();
		dto.setPrincipal(vo.getPrincipal());
		dto.setPrincipalOriginale(vo.getPrincipalOriginale());
		dto.setAutorizzazioneDominiStar(vo.isAutorizzazioneDominiStar());
		dto.setAutorizzazioneTipiVersamentoStar(vo.isAutorizzazioneTipiVersStar());
		dto.setId(vo.getId());
		dto.setAbilitato(vo.isAbilitato());
		dto.setIdTipiVersamento(utenzaTipiVersamentoLst);
		dto.setIdDominiUo(utenzaDominioLst);
//		dto.getDominiUo(bd);
//		dto.getTipiVersamento(bd);
		if(StringUtils.isNotBlank(vo.getRuoli())){ 
			dto.setRuoli(Arrays.asList(vo.getRuoli().split(",")));
		}
		dto.setPassword(vo.getPassword());

		return dto;
	}

	public static it.govpay.orm.Utenza toVO(it.govpay.model.Utenza dto)  {
		it.govpay.orm.Utenza vo = new it.govpay.orm.Utenza();
		vo.setId(dto.getId());
		try {
			vo.setPrincipal(CertificateUtils.formatPrincipal(dto.getPrincipal(), PrincipalType.SUBJECT));
		} catch (Exception e) {
			vo.setPrincipal(dto.getPrincipal());
		}
		vo.setPrincipalOriginale(dto.getPrincipalOriginale());
		vo.setAbilitato(dto.isAbilitato());
		vo.setAutorizzazioneDominiStar(dto.isAutorizzazioneDominiStar());
		vo.setAutorizzazioneTipiVersStar(dto.isAutorizzazioneTipiVersamentoStar());
		if(dto.getRuoli() != null && dto.getRuoli().size() > 0) {
			vo.setRuoli(String.join(",", dto.getRuoli().toArray(new String [dto.getRuoli().size()])));
		}
		vo.setPassword(dto.getPassword());
		return vo;
	}

}
