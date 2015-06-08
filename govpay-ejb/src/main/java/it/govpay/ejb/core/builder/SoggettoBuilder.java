/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.core.builder;

import it.govpay.ejb.core.model.DestinatarioPendenzaModel;
import it.govpay.ejb.core.model.VersanteModel;
import it.govpay.orm.pagamenti.DatiAnagraficiVersante;
import it.govpay.orm.posizionedebitoria.DatiAnagraficiDestinatario;

public class SoggettoBuilder {

	public static VersanteModel fromAnagraficaVersante(DatiAnagraficiVersante anagraficaVersante, String idFiscale) {

		if (anagraficaVersante == null)
			return null;

		VersanteModel versanteModel = new VersanteModel();
		// passo direttamente l'id fiscale per evitare ulteriori query. ma se non c'è ...
		versanteModel.setIdFiscale(idFiscale != null ? idFiscale : anagraficaVersante.getDistintaPagamento().getUtentecreatore());
		versanteModel.setAnagrafica(anagraficaVersante.getAnagrafica()); 
		versanteModel.setCap(anagraficaVersante.getCap());
		versanteModel.seteMail(anagraficaVersante.getEmail());
		versanteModel.setIndirizzo(anagraficaVersante.getIndirizzo());
		versanteModel.setLocalita(anagraficaVersante.getLocalita());
		versanteModel.setNazione(anagraficaVersante.getNazione());
		versanteModel.setCivico(anagraficaVersante.getNumeroCivico());
		versanteModel.setProvincia(anagraficaVersante.getProvincia());

		return versanteModel;
	}

	
	
	public static DestinatarioPendenzaModel fromAnagraficaDestinatario(DatiAnagraficiDestinatario anagraficaDestinatario, String idFiscale) {

		if (anagraficaDestinatario == null)
			return null;

		DestinatarioPendenzaModel destinatarioModel = new DestinatarioPendenzaModel();
		// passo direttamente l'id fiscale per evitare ulteriori query. ma se non c'è ...
		destinatarioModel.setIdFiscale(idFiscale != null ? idFiscale : anagraficaDestinatario.getDestinatario().getCoDestinatario());
		destinatarioModel.setAnagrafica(anagraficaDestinatario.getAnagrafica()); 
		destinatarioModel.setCap(anagraficaDestinatario.getCap());
		destinatarioModel.seteMail(anagraficaDestinatario.getEmail());
		destinatarioModel.setIndirizzo(anagraficaDestinatario.getIndirizzo());
		destinatarioModel.setLocalita(anagraficaDestinatario.getLocalita());
		destinatarioModel.setNazione(anagraficaDestinatario.getNazione());
		destinatarioModel.setCivico(anagraficaDestinatario.getNumeroCivico());
		destinatarioModel.setProvincia(anagraficaDestinatario.getProvincia());

		return destinatarioModel;
	}

}
