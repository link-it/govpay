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

package it.govpay.web.rs.gpv1;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;

import java.security.Principal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/iuv")
public class GestioneIuvRsService extends BaseRsService {

	Logger log = LogManager.getLogger();

	@GET
	@Path("/generaIuv")
	public String nuovoIuv(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario) throws GovPayException {
		initLogger("generaIuv");
		log.info("Ricevuta richiesta di generazione IUV iniziativa Ente");
		return generaIuv(identificativoBeneficiario, TipoIUV.ISO11694);
	}

	@GET
	@Path("/generaIuvAvviso")
	public String nuovoIuvAvviso(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario) throws GovPayException {
		initLogger("generaIuvAvviso");
		log.info("Ricevuta richiesta di generazione IUV iniziativa PSP");
		return generaIuv(identificativoBeneficiario, TipoIUV.NUMERICO);
	}

	private String generaIuv(String identificativoBeneficiario, TipoIUV tipoIuv) throws GovPayException {
		Principal principal = request.getUserPrincipal();

		if(principal == null) {
			log.error("Credenziali non fornite. Errore Autrneticazione.");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTENTICAZIONE);
		}

		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			Applicazione applicazione = null;

			try {
				applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, principal.getName());
			} catch (Exception e){
				log.error("Applicazione [principal: " + principal.getName() + "] non censita in Anagrafica Applicazioni.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, identificativoBeneficiario);
			} catch (Exception e){
				log.error("Dominio [codDominio: " + identificativoBeneficiario + "] non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 

			Pagamenti pagamentiInAttesa = new Pagamenti(bd);
			String iuv = pagamentiInAttesa.generaIuv(applicazione.getId(), dominio.getCodDominio(), tipoIuv, Iuv.AUX_DIGIT);

			log.info("Generato IUV [" + iuv + "]");
			return iuv;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
}

