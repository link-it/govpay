/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

package it.govpay.core.business.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.model.Applicazione;

public class CaricaIuvDTO {
	
	private Applicazione applicazioneAutenticata;
    protected String codApplicazione;
    protected String codDominio;
    protected List<Iuv> iuvDaCaricare;

    public String getCodApplicazione() {
        return codApplicazione;
    }

    public void setCodApplicazione(String value) {
        this.codApplicazione = value;
    }

    public String getCodDominio() {
        return codDominio;
    }

    public void setCodDominio(String value) {
        this.codDominio = value;
    }

    public List<Iuv> getIuvDaCaricare() {
        if (iuvDaCaricare == null) {
        	iuvDaCaricare = new ArrayList<Iuv>();
        }
        return this.iuvDaCaricare;
    }

    public Applicazione getApplicazioneAutenticata() {
		return applicazioneAutenticata;
	}

	public void setApplicazioneAutenticata(Applicazione applicazioneAutenticata) {
		this.applicazioneAutenticata = applicazioneAutenticata;
	}


	public static class Iuv {

        protected String iuv;
        protected String codVersamentoEnte;
        protected BigDecimal importoTotale;

        public String getIuv() {
            return iuv;
        }

        public void setIuv(String value) {
            this.iuv = value;
        }

        public String getCodVersamentoEnte() {
            return codVersamentoEnte;
        }

        public void setCodVersamentoEnte(String value) {
            this.codVersamentoEnte = value;
        }

        public BigDecimal getImportoTotale() {
            return importoTotale;
        }

        public void setImportoTotale(BigDecimal value) {
            this.importoTotale = value;
        }

    }
}
