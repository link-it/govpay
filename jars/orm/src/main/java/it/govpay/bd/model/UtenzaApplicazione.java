/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtenzaApplicazione extends Utenza {
	
	public UtenzaApplicazione() {
		super();
	}
	
	public UtenzaApplicazione(Utenza utenzaBase, String codApplicazione) {
		this(utenzaBase, codApplicazione, new HashMap<>());
	}
	public UtenzaApplicazione(Utenza utenzaBase, String codApplicazione, Map<String, List<String>> headers) {
		// dati bd.utenza
		this.aclPrincipal = utenzaBase.aclPrincipal;
		this.aclRuoliEsterni = utenzaBase.aclRuoliEsterni;
		this.aclRuoliUtenza = utenzaBase.aclRuoliUtenza;
		this.codApplicazione = codApplicazione;
		this.dominiUo = utenzaBase.dominiUo;
		this.ruoli = utenzaBase.getRuoli();
		this.tipiVersamento = utenzaBase.tipiVersamento;
		// dati model
		this.id = utenzaBase.getId();
		this.principal = utenzaBase.getPrincipal();
		this.principalOriginale = utenzaBase.getPrincipalOriginale();
		this.abilitato = utenzaBase.isAbilitato();
		this.idDominiUo = utenzaBase.getIdDominiUo();
		this.idTipiVersamento = utenzaBase.getIdTipiVersamento();
		this.checkSubject = utenzaBase.isCheckSubject();
		this.autorizzazioneDominiStar = utenzaBase.isAutorizzazioneDominiStar();
		this.autorizzazioneTipiVersamentoStar = utenzaBase.isAutorizzazioneTipiVersamentoStar();
		this.headers = headers;
		this.password = utenzaBase.getPassword();
	}

	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.APPLICAZIONE;
	}

	private static final long serialVersionUID = 1L;
	
	private transient String codApplicazione;

	public String getCodApplicazione() {
		return codApplicazione;
	}
	
	@Override
	public String getMessaggioUtenzaDisabilitata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Applicazione [").append(this.getCodApplicazione()).append("] disabilitata");
		return sb.toString();
	}
	
	@Override
	public String getMessaggioUtenzaNonAutorizzata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Applicazione [").append(this.getCodApplicazione()).append("] non autorizzata ad accedere alla risorsa richiesta");
		return sb.toString();
	}

}
