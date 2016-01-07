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

import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.rs.ListaPsp;
import it.govpay.web.adapter.Converter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;

@Path("/psp")
public class GestionePspRsService {

	private static Logger log = LogManager.getLogger();
	
	@GET
	@Path("/listaPsp")
	public ListaPsp listaPSP(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario) {
	
		BasicBD bd = null;	
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			PspBD pspBD = new PspBD(bd);
			List<Psp> psps = pspBD.getPsp();
			
			ListaPsp listaPsp = new ListaPsp();
			for(Psp psp : psps) {
				if(psp.isAttivo()) {
					for(Canale canale : psp.getCanali()) {
						if(canale.isAbilitato())
							listaPsp.getPsps().add(Converter.toPsp(canale));
					}
				}
			}
			return listaPsp;
		} catch (Exception e){
			log.error("Errore durante la generazione della lista PSP.", e);
			return null;
		} finally {
			if(bd!= null) bd.closeConnection();
		}
	}
}

