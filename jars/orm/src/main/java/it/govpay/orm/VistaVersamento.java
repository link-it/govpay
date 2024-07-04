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


package it.govpay.orm;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for VistaVersamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VistaVersamento"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="nome" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idTipoVersamentoDominio" type="{http://www.govpay.it/orm}id-tipo-versamento-dominio" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idTipoVersamento" type="{http://www.govpay.it/orm}id-tipo-versamento" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idUo" type="{http://www.govpay.it/orm}id-uo" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idPagamentoPortale" type="{http://www.govpay.it/orm}id-pagamento-portale" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}iuv-search" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="importoTotale" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="statoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="aggiornabile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataValidita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataOraUltimoAggiornamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="causaleVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreAnagrafica" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreIndirizzo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreCivico" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreCap" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreLocalita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreProvincia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreNazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreEmail" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreTelefono" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreCellulare" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreFax" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="tassonomiaAvviso" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="tassonomia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codLotto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codVersamentoLotto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codAnnoTributario" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codBundlekey" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="datiAllegati" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="incasso" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="anomalie" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="iuvVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="numeroAvviso" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="ack" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="anomalo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="divisione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="direzione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idSessione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="importoIncassato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="statoPagamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="iuvPagamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="srcIuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="srcDebitoreIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codRata" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idDocumento" type="{http://www.govpay.it/orm}id-documento" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataNotificaAvviso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="avvisoNotificato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="avvMailDataPromScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="avvMailPromScadNotificato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="avvAppIoDataPromScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="avvAppIoPromScadNotificato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idOperazione" type="{http://www.govpay.it/orm}id-operazione" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codDocumento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="docDescrizione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="proprieta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 		&lt;/sequence&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Rev$, $Date$
 * 
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VistaVersamento", 
  propOrder = {
  	"codVersamentoEnte",
  	"nome",
  	"idTipoVersamentoDominio",
  	"idTipoVersamento",
  	"idDominio",
  	"idUo",
  	"idApplicazione",
  	"idPagamentoPortale",
  	"iuv",
  	"importoTotale",
  	"statoVersamento",
  	"descrizioneStato",
  	"aggiornabile",
  	"dataCreazione",
  	"dataValidita",
  	"dataScadenza",
  	"dataOraUltimoAggiornamento",
  	"causaleVersamento",
  	"debitoreTipo",
  	"debitoreIdentificativo",
  	"debitoreAnagrafica",
  	"debitoreIndirizzo",
  	"debitoreCivico",
  	"debitoreCap",
  	"debitoreLocalita",
  	"debitoreProvincia",
  	"debitoreNazione",
  	"debitoreEmail",
  	"debitoreTelefono",
  	"debitoreCellulare",
  	"debitoreFax",
  	"tassonomiaAvviso",
  	"tassonomia",
  	"codLotto",
  	"codVersamentoLotto",
  	"codAnnoTributario",
  	"codBundlekey",
  	"datiAllegati",
  	"incasso",
  	"anomalie",
  	"iuvVersamento",
  	"numeroAvviso",
  	"ack",
  	"anomalo",
  	"divisione",
  	"direzione",
  	"idSessione",
  	"dataPagamento",
  	"importoPagato",
  	"importoIncassato",
  	"statoPagamento",
  	"iuvPagamento",
  	"srcIuv",
  	"srcDebitoreIdentificativo",
  	"codRata",
  	"idDocumento",
  	"tipo",
  	"dataNotificaAvviso",
  	"avvisoNotificato",
  	"avvMailDataPromScadenza",
  	"avvMailPromScadNotificato",
  	"avvAppIoDataPromScadenza",
  	"avvAppIoPromScadNotificato",
  	"idOperazione",
  	"codDocumento",
  	"docDescrizione",
  	"proprieta"
  }
)

@XmlRootElement(name = "VistaVersamento")

public class VistaVersamento extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public VistaVersamento() {
    super();
  }

  public java.lang.String getCodVersamentoEnte() {
    return this.codVersamentoEnte;
  }

  public void setCodVersamentoEnte(java.lang.String codVersamentoEnte) {
    this.codVersamentoEnte = codVersamentoEnte;
  }

  public java.lang.String getNome() {
    return this.nome;
  }

  public void setNome(java.lang.String nome) {
    this.nome = nome;
  }

  public IdTipoVersamentoDominio getIdTipoVersamentoDominio() {
    return this.idTipoVersamentoDominio;
  }

  public void setIdTipoVersamentoDominio(IdTipoVersamentoDominio idTipoVersamentoDominio) {
    this.idTipoVersamentoDominio = idTipoVersamentoDominio;
  }

  public IdTipoVersamento getIdTipoVersamento() {
    return this.idTipoVersamento;
  }

  public void setIdTipoVersamento(IdTipoVersamento idTipoVersamento) {
    this.idTipoVersamento = idTipoVersamento;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public IdUo getIdUo() {
    return this.idUo;
  }

  public void setIdUo(IdUo idUo) {
    this.idUo = idUo;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public IdPagamentoPortale getIdPagamentoPortale() {
    return this.idPagamentoPortale;
  }

  public void setIdPagamentoPortale(IdPagamentoPortale idPagamentoPortale) {
    this.idPagamentoPortale = idPagamentoPortale;
  }

  public IuvSearch getIuv() {
    return this.iuv;
  }

  public void setIuv(IuvSearch iuv) {
    this.iuv = iuv;
  }

  public double getImportoTotale() {
    return this.importoTotale;
  }

  public void setImportoTotale(double importoTotale) {
    this.importoTotale = importoTotale;
  }

  public java.lang.String getStatoVersamento() {
    return this.statoVersamento;
  }

  public void setStatoVersamento(java.lang.String statoVersamento) {
    this.statoVersamento = statoVersamento;
  }

  public java.lang.String getDescrizioneStato() {
    return this.descrizioneStato;
  }

  public void setDescrizioneStato(java.lang.String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public boolean isAggiornabile() {
    return this.aggiornabile;
  }

  public boolean getAggiornabile() {
    return this.aggiornabile;
  }

  public void setAggiornabile(boolean aggiornabile) {
    this.aggiornabile = aggiornabile;
  }

  public java.util.Date getDataCreazione() {
    return this.dataCreazione;
  }

  public void setDataCreazione(java.util.Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  public java.util.Date getDataValidita() {
    return this.dataValidita;
  }

  public void setDataValidita(java.util.Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  public java.util.Date getDataScadenza() {
    return this.dataScadenza;
  }

  public void setDataScadenza(java.util.Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public java.util.Date getDataOraUltimoAggiornamento() {
    return this.dataOraUltimoAggiornamento;
  }

  public void setDataOraUltimoAggiornamento(java.util.Date dataOraUltimoAggiornamento) {
    this.dataOraUltimoAggiornamento = dataOraUltimoAggiornamento;
  }

  public java.lang.String getCausaleVersamento() {
    return this.causaleVersamento;
  }

  public void setCausaleVersamento(java.lang.String causaleVersamento) {
    this.causaleVersamento = causaleVersamento;
  }

  public java.lang.String getDebitoreTipo() {
    return this.debitoreTipo;
  }

  public void setDebitoreTipo(java.lang.String debitoreTipo) {
    this.debitoreTipo = debitoreTipo;
  }

  public java.lang.String getDebitoreIdentificativo() {
    return this.debitoreIdentificativo;
  }

  public void setDebitoreIdentificativo(java.lang.String debitoreIdentificativo) {
    this.debitoreIdentificativo = debitoreIdentificativo;
  }

  public java.lang.String getDebitoreAnagrafica() {
    return this.debitoreAnagrafica;
  }

  public void setDebitoreAnagrafica(java.lang.String debitoreAnagrafica) {
    this.debitoreAnagrafica = debitoreAnagrafica;
  }

  public java.lang.String getDebitoreIndirizzo() {
    return this.debitoreIndirizzo;
  }

  public void setDebitoreIndirizzo(java.lang.String debitoreIndirizzo) {
    this.debitoreIndirizzo = debitoreIndirizzo;
  }

  public java.lang.String getDebitoreCivico() {
    return this.debitoreCivico;
  }

  public void setDebitoreCivico(java.lang.String debitoreCivico) {
    this.debitoreCivico = debitoreCivico;
  }

  public java.lang.String getDebitoreCap() {
    return this.debitoreCap;
  }

  public void setDebitoreCap(java.lang.String debitoreCap) {
    this.debitoreCap = debitoreCap;
  }

  public java.lang.String getDebitoreLocalita() {
    return this.debitoreLocalita;
  }

  public void setDebitoreLocalita(java.lang.String debitoreLocalita) {
    this.debitoreLocalita = debitoreLocalita;
  }

  public java.lang.String getDebitoreProvincia() {
    return this.debitoreProvincia;
  }

  public void setDebitoreProvincia(java.lang.String debitoreProvincia) {
    this.debitoreProvincia = debitoreProvincia;
  }

  public java.lang.String getDebitoreNazione() {
    return this.debitoreNazione;
  }

  public void setDebitoreNazione(java.lang.String debitoreNazione) {
    this.debitoreNazione = debitoreNazione;
  }

  public java.lang.String getDebitoreEmail() {
    return this.debitoreEmail;
  }

  public void setDebitoreEmail(java.lang.String debitoreEmail) {
    this.debitoreEmail = debitoreEmail;
  }

  public java.lang.String getDebitoreTelefono() {
    return this.debitoreTelefono;
  }

  public void setDebitoreTelefono(java.lang.String debitoreTelefono) {
    this.debitoreTelefono = debitoreTelefono;
  }

  public java.lang.String getDebitoreCellulare() {
    return this.debitoreCellulare;
  }

  public void setDebitoreCellulare(java.lang.String debitoreCellulare) {
    this.debitoreCellulare = debitoreCellulare;
  }

  public java.lang.String getDebitoreFax() {
    return this.debitoreFax;
  }

  public void setDebitoreFax(java.lang.String debitoreFax) {
    this.debitoreFax = debitoreFax;
  }

  public java.lang.String getTassonomiaAvviso() {
    return this.tassonomiaAvviso;
  }

  public void setTassonomiaAvviso(java.lang.String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  public java.lang.String getTassonomia() {
    return this.tassonomia;
  }

  public void setTassonomia(java.lang.String tassonomia) {
    this.tassonomia = tassonomia;
  }

  public java.lang.String getCodLotto() {
    return this.codLotto;
  }

  public void setCodLotto(java.lang.String codLotto) {
    this.codLotto = codLotto;
  }

  public java.lang.String getCodVersamentoLotto() {
    return this.codVersamentoLotto;
  }

  public void setCodVersamentoLotto(java.lang.String codVersamentoLotto) {
    this.codVersamentoLotto = codVersamentoLotto;
  }

  public java.lang.String getCodAnnoTributario() {
    return this.codAnnoTributario;
  }

  public void setCodAnnoTributario(java.lang.String codAnnoTributario) {
    this.codAnnoTributario = codAnnoTributario;
  }

  public java.lang.String getCodBundlekey() {
    return this.codBundlekey;
  }

  public void setCodBundlekey(java.lang.String codBundlekey) {
    this.codBundlekey = codBundlekey;
  }

  public java.lang.String getDatiAllegati() {
    return this.datiAllegati;
  }

  public void setDatiAllegati(java.lang.String datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  public java.lang.String getIncasso() {
    return this.incasso;
  }

  public void setIncasso(java.lang.String incasso) {
    this.incasso = incasso;
  }

  public java.lang.String getAnomalie() {
    return this.anomalie;
  }

  public void setAnomalie(java.lang.String anomalie) {
    this.anomalie = anomalie;
  }

  public java.lang.String getIuvVersamento() {
    return this.iuvVersamento;
  }

  public void setIuvVersamento(java.lang.String iuvVersamento) {
    this.iuvVersamento = iuvVersamento;
  }

  public java.lang.String getNumeroAvviso() {
    return this.numeroAvviso;
  }

  public void setNumeroAvviso(java.lang.String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public boolean isAck() {
    return this.ack;
  }

  public boolean getAck() {
    return this.ack;
  }

  public void setAck(boolean ack) {
    this.ack = ack;
  }

  public boolean isAnomalo() {
    return this.anomalo;
  }

  public boolean getAnomalo() {
    return this.anomalo;
  }

  public void setAnomalo(boolean anomalo) {
    this.anomalo = anomalo;
  }

  public java.lang.String getDivisione() {
    return this.divisione;
  }

  public void setDivisione(java.lang.String divisione) {
    this.divisione = divisione;
  }

  public java.lang.String getDirezione() {
    return this.direzione;
  }

  public void setDirezione(java.lang.String direzione) {
    this.direzione = direzione;
  }

  public java.lang.String getIdSessione() {
    return this.idSessione;
  }

  public void setIdSessione(java.lang.String idSessione) {
    this.idSessione = idSessione;
  }

  public java.util.Date getDataPagamento() {
    return this.dataPagamento;
  }

  public void setDataPagamento(java.util.Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public java.math.BigDecimal getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(java.math.BigDecimal importoPagato) {
    this.importoPagato = importoPagato;
  }

  public java.math.BigDecimal getImportoIncassato() {
    return this.importoIncassato;
  }

  public void setImportoIncassato(java.math.BigDecimal importoIncassato) {
    this.importoIncassato = importoIncassato;
  }

  public java.lang.String getStatoPagamento() {
    return this.statoPagamento;
  }

  public void setStatoPagamento(java.lang.String statoPagamento) {
    this.statoPagamento = statoPagamento;
  }

  public java.lang.String getIuvPagamento() {
    return this.iuvPagamento;
  }

  public void setIuvPagamento(java.lang.String iuvPagamento) {
    this.iuvPagamento = iuvPagamento;
  }

  public java.lang.String getSrcIuv() {
    return this.srcIuv;
  }

  public void setSrcIuv(java.lang.String srcIuv) {
    this.srcIuv = srcIuv;
  }

  public java.lang.String getSrcDebitoreIdentificativo() {
    return this.srcDebitoreIdentificativo;
  }

  public void setSrcDebitoreIdentificativo(java.lang.String srcDebitoreIdentificativo) {
    this.srcDebitoreIdentificativo = srcDebitoreIdentificativo;
  }

  public java.lang.String getCodRata() {
    return this.codRata;
  }

  public void setCodRata(java.lang.String codRata) {
    this.codRata = codRata;
  }

  public IdDocumento getIdDocumento() {
    return this.idDocumento;
  }

  public void setIdDocumento(IdDocumento idDocumento) {
    this.idDocumento = idDocumento;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  public java.util.Date getDataNotificaAvviso() {
    return this.dataNotificaAvviso;
  }

  public void setDataNotificaAvviso(java.util.Date dataNotificaAvviso) {
    this.dataNotificaAvviso = dataNotificaAvviso;
  }

  public Boolean getAvvisoNotificato() {
    return this.avvisoNotificato;
  }

  public void setAvvisoNotificato(Boolean avvisoNotificato) {
    this.avvisoNotificato = avvisoNotificato;
  }

  public java.util.Date getAvvMailDataPromScadenza() {
    return this.avvMailDataPromScadenza;
  }

  public void setAvvMailDataPromScadenza(java.util.Date avvMailDataPromScadenza) {
    this.avvMailDataPromScadenza = avvMailDataPromScadenza;
  }

  public Boolean getAvvMailPromScadNotificato() {
    return this.avvMailPromScadNotificato;
  }

  public void setAvvMailPromScadNotificato(Boolean avvMailPromScadNotificato) {
    this.avvMailPromScadNotificato = avvMailPromScadNotificato;
  }

  public java.util.Date getAvvAppIoDataPromScadenza() {
    return this.avvAppIoDataPromScadenza;
  }

  public void setAvvAppIoDataPromScadenza(java.util.Date avvAppIoDataPromScadenza) {
    this.avvAppIoDataPromScadenza = avvAppIoDataPromScadenza;
  }

  public Boolean getAvvAppIoPromScadNotificato() {
    return this.avvAppIoPromScadNotificato;
  }

  public void setAvvAppIoPromScadNotificato(Boolean avvAppIoPromScadNotificato) {
    this.avvAppIoPromScadNotificato = avvAppIoPromScadNotificato;
  }

  public IdOperazione getIdOperazione() {
    return this.idOperazione;
  }

  public void setIdOperazione(IdOperazione idOperazione) {
    this.idOperazione = idOperazione;
  }

  public java.lang.String getCodDocumento() {
    return this.codDocumento;
  }

  public void setCodDocumento(java.lang.String codDocumento) {
    this.codDocumento = codDocumento;
  }

  public java.lang.String getDocDescrizione() {
    return this.docDescrizione;
  }

  public void setDocDescrizione(java.lang.String docDescrizione) {
    this.docDescrizione = docDescrizione;
  }

  public java.lang.String getProprieta() {
    return this.proprieta;
  }

  public void setProprieta(java.lang.String proprieta) {
    this.proprieta = proprieta;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.VistaVersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.VistaVersamento.modelStaticInstance==null){
  			it.govpay.orm.VistaVersamento.modelStaticInstance = new it.govpay.orm.model.VistaVersamentoModel();
	  }
  }
  public static it.govpay.orm.model.VistaVersamentoModel model(){
	  if(it.govpay.orm.VistaVersamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.VistaVersamento.modelStaticInstance;
  }


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nome",required=false,nillable=false)
  protected java.lang.String nome;

  @XmlElement(name="idTipoVersamentoDominio",required=true,nillable=false)
  protected IdTipoVersamentoDominio idTipoVersamentoDominio;

  @XmlElement(name="idTipoVersamento",required=true,nillable=false)
  protected IdTipoVersamento idTipoVersamento;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @XmlElement(name="idUo",required=false,nillable=false)
  protected IdUo idUo;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @XmlElement(name="idPagamentoPortale",required=true,nillable=false)
  protected IdPagamentoPortale idPagamentoPortale;

  @XmlElement(name="iuv",required=false,nillable=false)
  protected IuvSearch iuv;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoTotale",required=true,nillable=false)
  protected double importoTotale;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoVersamento",required=true,nillable=false)
  protected java.lang.String statoVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="aggiornabile",required=true,nillable=false)
  protected boolean aggiornabile;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCreazione;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataValidita",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataValidita;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataScadenza",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataScadenza;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraUltimoAggiornamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraUltimoAggiornamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleVersamento",required=false,nillable=false)
  protected java.lang.String causaleVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreTipo",required=false,nillable=false)
  protected java.lang.String debitoreTipo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreIdentificativo",required=true,nillable=false)
  protected java.lang.String debitoreIdentificativo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreAnagrafica",required=true,nillable=false)
  protected java.lang.String debitoreAnagrafica;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreIndirizzo",required=false,nillable=false)
  protected java.lang.String debitoreIndirizzo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreCivico",required=false,nillable=false)
  protected java.lang.String debitoreCivico;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreCap",required=false,nillable=false)
  protected java.lang.String debitoreCap;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreLocalita",required=false,nillable=false)
  protected java.lang.String debitoreLocalita;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreProvincia",required=false,nillable=false)
  protected java.lang.String debitoreProvincia;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreNazione",required=false,nillable=false)
  protected java.lang.String debitoreNazione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreEmail",required=false,nillable=false)
  protected java.lang.String debitoreEmail;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreTelefono",required=false,nillable=false)
  protected java.lang.String debitoreTelefono;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreCellulare",required=false,nillable=false)
  protected java.lang.String debitoreCellulare;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreFax",required=false,nillable=false)
  protected java.lang.String debitoreFax;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tassonomiaAvviso",required=false,nillable=false)
  protected java.lang.String tassonomiaAvviso;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tassonomia",required=false,nillable=false)
  protected java.lang.String tassonomia;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codLotto",required=false,nillable=false)
  protected java.lang.String codLotto;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoLotto",required=false,nillable=false)
  protected java.lang.String codVersamentoLotto;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codAnnoTributario",required=false,nillable=false)
  protected java.lang.String codAnnoTributario;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codBundlekey",required=false,nillable=false)
  protected java.lang.String codBundlekey;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiAllegati",required=false,nillable=false)
  protected java.lang.String datiAllegati;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="incasso",required=false,nillable=false)
  protected java.lang.String incasso;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="anomalie",required=false,nillable=false)
  protected java.lang.String anomalie;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuvVersamento",required=false,nillable=false)
  protected java.lang.String iuvVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="numeroAvviso",required=false,nillable=false)
  protected java.lang.String numeroAvviso;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="ack",required=true,nillable=false)
  protected boolean ack;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="anomalo",required=true,nillable=false)
  protected boolean anomalo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="divisione",required=false,nillable=false)
  protected java.lang.String divisione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="direzione",required=false,nillable=false)
  protected java.lang.String direzione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessione",required=false,nillable=false)
  protected java.lang.String idSessione;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataPagamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataPagamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoPagato",required=true,nillable=false)
  protected java.math.BigDecimal importoPagato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoIncassato",required=true,nillable=false)
  protected java.math.BigDecimal importoIncassato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoPagamento",required=true,nillable=false)
  protected java.lang.String statoPagamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuvPagamento",required=false,nillable=false)
  protected java.lang.String iuvPagamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="srcIuv",required=false,nillable=false)
  protected java.lang.String srcIuv;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="srcDebitoreIdentificativo",required=true,nillable=false)
  protected java.lang.String srcDebitoreIdentificativo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codRata",required=false,nillable=false)
  protected java.lang.String codRata;

  @XmlElement(name="idDocumento",required=false,nillable=false)
  protected IdDocumento idDocumento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=true,nillable=false)
  protected java.lang.String tipo;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataNotificaAvviso",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataNotificaAvviso;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvisoNotificato",required=false,nillable=false)
  protected Boolean avvisoNotificato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="avvMailDataPromScadenza",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date avvMailDataPromScadenza;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromScadNotificato",required=false,nillable=false)
  protected Boolean avvMailPromScadNotificato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="avvAppIoDataPromScadenza",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date avvAppIoDataPromScadenza;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromScadNotificato",required=false,nillable=false)
  protected Boolean avvAppIoPromScadNotificato;

  @XmlElement(name="idOperazione",required=true,nillable=false)
  protected IdOperazione idOperazione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDocumento",required=false,nillable=false)
  protected java.lang.String codDocumento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="docDescrizione",required=false,nillable=false)
  protected java.lang.String docDescrizione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="proprieta",required=false,nillable=false)
  protected java.lang.String proprieta;

}
