/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

package it.govpay.core.beans;

import java.math.BigDecimal;
import java.util.Date;

public class RichiestaStorno {

    protected String codRichiesta;
    protected Date dataRichiesta;
    protected StatoRevoca stato;
    protected String descrizioneStato;
    protected byte[] rr;
    protected byte[] er;
    protected BigDecimal importoStornato;

    /**
     * Recupera il valore della proprietà codRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRichiesta() {
        return this.codRichiesta;
    }

    /**
     * Imposta il valore della proprietà codRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodRichiesta(String value) {
        this.codRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà dataRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataRichiesta() {
        return this.dataRichiesta;
    }

    /**
     * Imposta il valore della proprietà dataRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataRichiesta(Date value) {
        this.dataRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link StatoRevoca }
     *     
     */
    public StatoRevoca getStato() {
        return this.stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link StatoRevoca }
     *     
     */
    public void setStato(StatoRevoca value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneStato() {
        return this.descrizioneStato;
    }

    /**
     * Imposta il valore della proprietà descrizioneStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneStato(String value) {
        this.descrizioneStato = value;
    }

    /**
     * Recupera il valore della proprietà rr.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRr() {
        return this.rr;
    }

    /**
     * Imposta il valore della proprietà rr.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRr(byte[] value) {
        this.rr = value;
    }

    /**
     * Recupera il valore della proprietà er.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getEr() {
        return this.er;
    }

    /**
     * Imposta il valore della proprietà er.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setEr(byte[] value) {
        this.er = value;
    }

    /**
     * Recupera il valore della proprietà importoStornato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoStornato() {
        return this.importoStornato;
    }

    /**
     * Imposta il valore della proprietà importoStornato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoStornato(BigDecimal value) {
        this.importoStornato = value;
    }

}
