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
package it.govpay.utils;

import java.util.Date;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Esito;
import it.govpay.bd.model.Esito.StatoSpedizione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Versamento;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.gpv1.Converter;
import it.govpay.web.ws.utils.PagamentiTelematiciGPUtil;

public class EsitoFactory {

	public static Esito newEsito(BasicBD bd, Applicazione applicazione, Versamento versamento, Rpt rpt, Rt rt, byte[] ctRtByte) throws GovPayException {
		Esito esito = new Esito();
		esito.setCodDominio(versamento.getCodDominio());
		esito.setDataOraCreazione(new Date());
		esito.setIdApplicazione(applicazione.getId());
		esito.setIuv(versamento.getIuv());
		esito.setStatoSpedizione(StatoSpedizione.DA_SPEDIRE);
		esito.setTentativiSpedizione(0l);
		
		byte[] xml = null;
		
		try {
			switch (applicazione.getVersione()) {
			case GPv1:
				xml = JaxbUtils.toByte(Converter.toVerificaPagamento(versamento, rpt, ctRtByte));
				break;
			case GPv2:
				xml = PagamentiTelematiciGPUtil.toEsitoPagamento(bd, applicazione, versamento, rpt, ctRtByte);
			default:
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Non implementato");
			}
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile creare il messaggio di esito", e);
		}
		
		esito.setXml(xml);
		return esito;
	}

}
