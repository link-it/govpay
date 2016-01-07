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
package it.govpay.test;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.EntiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.mail.MailTemplateBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Applicazione.Versione;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Connettore.EnumAuthType;
import it.govpay.bd.model.Connettore.EnumSslType;
import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.Periodo;
import it.govpay.bd.model.Disponibilita.TipoDisponibilita;
import it.govpay.bd.model.Disponibilita.TipoPeriodo;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Evento.CategoriaEvento;
import it.govpay.bd.model.Evento.TipoEvento;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.MailTemplate;
import it.govpay.bd.model.MailTemplate.TipoAllegati;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Rt.EsitoPagamento;
import it.govpay.bd.model.Rt.StatoRt;
import it.govpay.bd.model.SingolaRevoca;
import it.govpay.bd.model.SingolaRevoca.Stato;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Sla;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Tributo.TipoContabilta;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.bd.registro.EventiBD;
import it.govpay.bd.registro.SlaBD;
import it.govpay.bd.revoca.RrBD;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

public class BasicTest {
	protected static final int APPLICATION_CODE = 1;
	protected static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	protected static Psp psp, pspPoste;
	protected static Intermediario intermediario;
	protected static Stazione stazione;
	protected static Dominio dominioA, dominioB;
	protected static MailTemplate mailTemplateRPT, mailTemplateRT;
	protected static Ente enteAA, enteAB, enteBA;
	protected static Tributo tributoAA1, tributoAA2, tributoAB1, tributoBB1, tributoBB2;
	protected static Applicazione applicazioneAA, applicazioneAB, applicazioneBB;
	protected static Versamento versamentoAA1_1;
	protected static Rpt rptAA1_1;
	protected static Portale portale;
	protected static Evento eventoIniziale;
	protected static Evento eventoFinale;
	protected static Sla sla;
	protected static Rt rt;
	protected static Rr rr;
	protected static IbanAccredito ibanAccredito;
	protected static Operatore operatore;

	@BeforeSuite
	public static void setUpDataSource() throws Exception {
		try {
			ConnectionManager.initialize();
		} catch (Exception e) {
			DriverAdapterCPDS cpds = new DriverAdapterCPDS();
			cpds.setDriver("org.postgresql.Driver");
			cpds.setUrl("jdbc:postgresql://127.0.0.1:5432/govpay");
			cpds.setUser("govpay");
			cpds.setPassword("govpay");

			SharedPoolDataSource dataSource = new SharedPoolDataSource();
			dataSource.setConnectionPoolDataSource(cpds);
			dataSource.setMaxActive(10);
			dataSource.setMaxWait(50);

			System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
			System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");            

			InitialContext ic = new InitialContext();

			ic.createSubcontext("java:");
			ic.bind("java:/govpay", dataSource);
			ConnectionManager.initialize();
		}
		bd = BasicBD.newInstance();
		cleanUpDataBase();
		bd.closeConnection();
	}

	@BeforeClass
	public static void setUpBusinessDelegate() throws Exception {
		bd = BasicBD.newInstance();
	}

	@AfterClass
	public static void closeConnection() throws Exception {
		if(bd.getConnection().isClosed()) {
			bd = BasicBD.newInstance();
		}
		bd.closeConnection();
	}

	protected static void cleanUpDataBase() throws Exception {
		ScriptRunner runner = new ScriptRunner(bd.getConnection(), false, false, false);
		String file = "/postgresql/delete.sql";
		runner.runScript(new InputStreamReader(GovpayConfig.class.getResourceAsStream(file)));
	}

	public void reInitDataBase() throws Exception {
		cleanUpDataBase();
		psp = null;
		pspPoste = null;
		intermediario = null;
		stazione = null;
		dominioA = null;
		dominioB = null;
		mailTemplateRPT = null;
		mailTemplateRT = null;
		enteAA = null;
		enteAB = null;
		enteBA = null;
		tributoAA1 = null;
		tributoAA2 = null;
		tributoAB1 = null;
		tributoBB1 = null;
		tributoBB2 = null;
		applicazioneAA = null;
		applicazioneAB = null;
		applicazioneBB = null;
		versamentoAA1_1 = null;
		rptAA1_1 = null;
		rt = null;
		rr = null;
		portale = null;
		eventoIniziale = null;
		eventoFinale = null;
		sla = null;
		ibanAccredito = null;
		operatore = null;
	}

	protected void setupPsp() throws Exception {
		if(psp != null) return;

		PspBD pspBD = new PspBD(bd);
		psp = new Psp();
		psp.setAttivo(true);
		psp.setBolloGestito(false);
		psp.setCodFlusso("CodFlusso");
		psp.setCodPsp("CodPsp");
		psp.setRagioneSociale("Prestatore Servizi Pagamento");
		psp.setStornoGestito(false);
		psp.setUrlInfo("http://www.govpay.it/psp");

		for(TipoVersamento tipoVersamento : TipoVersamento.values()) {
			Canale canale = psp.new Canale();
			canale.setCodCanale("CodCanale" + tipoVersamento);
			canale.setCodIntermediario("CodIntermediario");
			canale.setCondizioni("Condizioni" + tipoVersamento);
			canale.setDescrizione("Descrizione" + tipoVersamento);
			canale.setDisponibilita("Disponibilita" + tipoVersamento);
			switch (tipoVersamento) {
			case ADDEBITO_DIRETTO:
				canale.setModelloPagamento(ModelloPagamento.DIFFERITO);
				break;
			case ATTIVATO_PRESSO_PSP:
				canale.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP);
				break;
			case BOLLETTINO_POSTALE:
				canale.setModelloPagamento(ModelloPagamento.DIFFERITO);
				break;
			case BONIFICO_BANCARIO_TESORERIA:
				canale.setModelloPagamento(ModelloPagamento.DIFFERITO);
				break;
			case CARTA_PAGAMENTO:
				canale.setModelloPagamento(ModelloPagamento.IMMEDIATO);
				break;
			case MYBANK:
				canale.setModelloPagamento(ModelloPagamento.IMMEDIATO);
				break;

			default:continue;
			}
			canale.setTipoVersamento(tipoVersamento);
			canale.setUrlInfo("http://www.govpay.it/canale/" + tipoVersamento);
			canale.setPsp(psp);
			psp.getCanali().add(canale);
		}

		for(TipoVersamento tipoVersamento : TipoVersamento.values()) {
			Canale canale = psp.new Canale();
			canale.setCodCanale("CodCanaleMulti" + tipoVersamento);
			canale.setCodIntermediario("CodIntermediario");
			canale.setCondizioni("CondizioniMulti" + tipoVersamento);
			canale.setDescrizione("DescrizioneMulti" + tipoVersamento);
			canale.setDisponibilita("DisponibilitaMulti" + tipoVersamento);
			switch (tipoVersamento) {
			case CARTA_PAGAMENTO:
				canale.setModelloPagamento(ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO);
				break;
			case MYBANK:
				canale.setModelloPagamento(ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO);
				break;
			default:
				continue;
			}
			canale.setTipoVersamento(tipoVersamento);
			canale.setUrlInfo("http://www.govpay.it/canale/Multi" + tipoVersamento);
			canale.setPsp(psp);
			psp.getCanali().add(canale);
		}
		pspBD.insertPsp(psp, "<xml />".getBytes());
	}

	protected void setupIntermediario() throws Exception {
		if(intermediario != null) return;
		IntermediariBD intermediariBD = new IntermediariBD(bd);
		intermediario = new Intermediario();
		intermediario.setCodIntermediario("11111111113");
		intermediario.setDenominazione("Denominazione");
		Connettore connettorePdd = new Connettore();
		connettorePdd.setAzioneInUrl(false);
		connettorePdd.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettorePdd.setHttpPassw("HttpPassw");
		connettorePdd.setHttpUser("HttpUser");
		connettorePdd.setUrl("http://localhost:8080/govpay-ndp-sym/PagamentiTelematiciRPTservice");
		intermediario.setConnettorePdd(connettorePdd);
		intermediariBD.insertIntermediario(intermediario);
	}

	protected void setupStazione() throws Exception {
		if(stazione != null) return;
		if(intermediario == null) setupIntermediario();
		StazioniBD stazioniBD = new StazioniBD(bd);
		stazione = new Stazione();
		stazione.setCodStazione(intermediario.getCodIntermediario() +"_01");
		stazione.setIdIntermediario(intermediario.getId());
		stazione.setPassword("Password");
		stazione.setApplicationCode(APPLICATION_CODE);
		stazioniBD.insertStazione(stazione);
	}

	protected void setupIbanAccredito() throws Exception {
		if(ibanAccredito != null) return;
		ibanAccredito = new IbanAccredito();
		ibanAccredito.setCodIban("IT88ABCDEFGHIJKLMNOPQRSTUVWXYZ01");
		ibanAccredito.setIdNegozio("idNegozio");
		ibanAccredito.setIdSellerBank("idSellerBank");
		ibanAccredito.setAbilitato(true);
		ibanAccredito.setAttivatoObep(true);
		ibanAccredito.setIdDominio(dominioA.getId());
		IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
		ibanAccreditoBD.insertIbanAccredito(ibanAccredito);

	}
	protected void setupDominio() throws Exception {
		if(dominioA != null) return;
		if(stazione == null) setupStazione();
		DominiBD dominiBD = new DominiBD(bd);
		dominioA = new Dominio();
		dominioA.setCodDominio("CodDominioA");
		dominioA.setRagioneSociale("ragioneSociale");
		dominioA.setGln("GlnA");
		dominioA.setIdStazione(stazione.getId());
		List<Disponibilita> disponibilita = new ArrayList<Disponibilita>();
		Disponibilita e = new Disponibilita();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 30);

		List<Periodo> fasceOrarieLst = new ArrayList<Periodo>();
		Periodo periodo =  new Periodo();
		SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
		periodo.setA(hourFormat.format(calendar.getTime()));
		periodo.setDa(hourFormat.format(calendar.getTime()));
		fasceOrarieLst.add(periodo);
		e.setFasceOrarieLst(fasceOrarieLst);
		e.setGiorno("giorno");
		e.setTipoDisponibilita(TipoDisponibilita.DISPONIBILE);
		e.setTipoPeriodo(TipoPeriodo.ANNUALE);
		disponibilita.add(e);
		dominioA.setDisponibilita(disponibilita);

		dominiBD.insertDominio(dominioA);

		dominioB = new Dominio();
		dominioB.setCodDominio("CodDominioB");
		dominioB.setRagioneSociale("ragioneSociale");
		dominioB.setGln("GlnB");
		dominioB.setIdStazione(stazione.getId());

		dominiBD.insertDominio(dominioB);
	}

	protected void setupMailTemplate() throws Exception {
		if(mailTemplateRPT != null) return;

		MailTemplateBD mailTemplateBD = new MailTemplateBD(bd);
		mailTemplateRPT = new MailTemplate();
		mailTemplateRPT.setMittente("from@mittente.com");
		mailTemplateRPT.setTemplateOggetto("Oggetto dove si sostituiscono i valori iuv:${IUV} con placeholder");
		mailTemplateRPT.setTemplateMessaggio("Body dove si sostituiscono iuv:${IUV} e importoTotale: ${importoTotale}");

		List<TipoAllegati> allegatiRPT = new ArrayList<MailTemplate.TipoAllegati>();
		allegatiRPT.add(TipoAllegati.RPT);
		mailTemplateRPT.setAllegati(allegatiRPT);
		mailTemplateBD.insertMailTemplate(mailTemplateRPT);

		mailTemplateRT = new MailTemplate();
		mailTemplateRT.setMittente("from@mittente.com");
		mailTemplateRT.setTemplateOggetto("Oggetto dove si sostituiscono i valori iuv:${IUV} e esito: ${esito} con placeholder");
		mailTemplateRT.setTemplateMessaggio("Body dove si sostituiscono importoTotale: ${importoTotale} e importoPagato: ${importoPagato}");

		List<TipoAllegati> allegatiRT = new ArrayList<MailTemplate.TipoAllegati>();
		allegatiRT.add(TipoAllegati.RT_PDF);
		allegatiRT.add(TipoAllegati.RT_XML);
		mailTemplateRT.setAllegati(allegatiRT);
		mailTemplateBD.insertMailTemplate(mailTemplateRT);
	}

	protected void setupEnte() throws Exception {
		if(enteAA != null) return;
		if(dominioA == null) setupDominio();

		EntiBD entiBD = new EntiBD(bd);
		enteAA = new Ente();
		enteAA.setAttivo(true);
		enteAA.setIdDominio(dominioA.getId());
		Anagrafica anagraficaEnte = new Anagrafica();
		anagraficaEnte.setCodUnivoco("CodUnivocoEnteAA");
		anagraficaEnte.setRagioneSociale("RagioneSocialeEnteAA");
		enteAA.setAnagraficaEnte(anagraficaEnte);
		enteAA.setCodEnte("CodEnteAA");
		entiBD.insertEnte(enteAA);

		enteAB = new Ente();
		enteAB.setAttivo(true);
		enteAB.setIdDominio(dominioA.getId());

		anagraficaEnte = new Anagrafica();
		anagraficaEnte.setCodUnivoco("CodUnivocoEnteAB");
		anagraficaEnte.setRagioneSociale("RagioneSocialeEnteAB");
		enteAB.setAnagraficaEnte(anagraficaEnte);
		enteAB.setCodEnte("CodEnteAB");
		entiBD.insertEnte(enteAB);

		enteBA = new Ente();
		enteBA.setAttivo(true);
		enteBA.setIdDominio(dominioB.getId());
		anagraficaEnte = new Anagrafica();
		anagraficaEnte.setCodUnivoco("CodUnivocoEnteBA");
		anagraficaEnte.setRagioneSociale("RagioneSocialeEnteBA");
		enteBA.setAnagraficaEnte(anagraficaEnte);
		enteBA.setCodEnte("CodEnteBA");
		entiBD.insertEnte(enteBA);
		
	}



	protected void setupTributi() throws Exception {
		if(tributoAA1 != null) return;
		if(enteAA == null) setupEnte();
		if(ibanAccredito == null) setupIbanAccredito();
		TributiBD tributiBD = new TributiBD(bd);
		tributoAA1 = new Tributo();
		tributoAA1.setAbilitato(true);
		tributoAA1.setCodContabilita("CodContabilitaAA1");
		tributoAA1.setIdEnte(enteAA.getId());
		tributoAA1.setCodTributo("CodTributoAA1");
		tributoAA1.setDescrizione("DescrizioneAA1");
		tributoAA1.setIbanAccredito(ibanAccredito.getId());
		tributoAA1.setTipoContabilita(TipoContabilta.ALTRO);

		tributoAA2 = new Tributo();
		tributoAA2.setAbilitato(true);
		tributoAA2.setCodContabilita("CodContabilitaAA2");
		tributoAA2.setIdEnte(enteAA.getId());
		tributoAA2.setCodTributo("CodTributoAA2");
		tributoAA2.setDescrizione("DescrizioneAA2");
		tributoAA2.setIbanAccredito(ibanAccredito.getId());
		tributoAA2.setTipoContabilita(TipoContabilta.CAPITOLO);

		tributoAB1 = new Tributo();
		tributoAB1.setAbilitato(true);
		tributoAB1.setCodContabilita("CodContabilitaAB1");
		tributoAB1.setIdEnte(enteAB.getId());
		tributoAB1.setCodTributo("CodTributoAB1");
		tributoAB1.setDescrizione("DescrizioneAB1");
		tributoAB1.setIbanAccredito(ibanAccredito.getId());
		tributoAB1.setTipoContabilita(TipoContabilta.SIOPE);

		tributoBB1 = new Tributo();
		tributoBB1.setAbilitato(true);
		tributoBB1.setCodContabilita("CodContabilitaB1");
		tributoBB1.setIdEnte(enteBA.getId());
		tributoBB1.setCodTributo("CodTributoB1");
		tributoBB1.setDescrizione("DescrizioneB1");
		tributoBB1.setIbanAccredito(ibanAccredito.getId());
		tributoBB1.setTipoContabilita(TipoContabilta.SPECIALE);

		tributoBB2 = new Tributo();
		tributoBB2.setAbilitato(true);
		tributoBB2.setCodContabilita("CodContabilitaB2");
		tributoBB2.setIdEnte(enteBA.getId());
		tributoBB2.setCodTributo("CodTributoB2");
		tributoBB2.setDescrizione("DescrizioneB2");
		tributoBB2.setIbanAccredito(ibanAccredito.getId());
		tributoBB2.setTipoContabilita(TipoContabilta.ALTRO);

		tributiBD.insertTributo(tributoAA1);
		tributiBD.insertTributo(tributoAA2);
		tributiBD.insertTributo(tributoAB1);
		tributiBD.insertTributo(tributoBB1);
		tributiBD.insertTributo(tributoBB2);
	}

	protected void setupApplicazioni() throws Exception {
		if(applicazioneAA != null) return;
		if(stazione == null) setupStazione();
		if(tributoAA1 == null) setupTributi();

		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		applicazioneAA = new Applicazione();
		applicazioneAA.setVersione(Versione.GPv1);
		applicazioneAA.setAbilitato(true);
		applicazioneAA.setCodApplicazione("CodApplicazioneAA1");
		Connettore connettoreEsito = new Connettore();
		connettoreEsito.setTipoAutenticazione(EnumAuthType.NONE);
		connettoreEsito.setUrl("http://localhost:6789");
		applicazioneAA.setConnettoreEsito(connettoreEsito);
		Connettore connettoreVerifica = new Connettore();
		connettoreVerifica.setTipoAutenticazione(EnumAuthType.NONE);
		connettoreEsito.setUrl("http://localhost:6789/esito/");
		applicazioneAA.setConnettoreVerifica(connettoreVerifica);
		List<Long> idTributi = new ArrayList<Long>();
		idTributi.add(tributoAA1.getId());
		idTributi.add(tributoAA2.getId());
		applicazioneAA.setIdTributi(idTributi);
		applicazioneAA.setPrincipal("PrincipalAA1");
		applicazioneAA.setPolicyRispedizione("it.govpay.test.web.utils.PolicyRispedizioneTest");
		applicazioniBD.insertApplicazione(applicazioneAA);

		applicazioneAB = new Applicazione();
		applicazioneAB.setVersione(Versione.GPv2);
		applicazioneAB.setAbilitato(true);
		applicazioneAB.setCodApplicazione("CodapplicazioneAA2");
		connettoreEsito = new Connettore();
		connettoreEsito.setAzioneInUrl(false);
		connettoreEsito.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettoreEsito.setHttpPassw("HttpPasswAA2");
		connettoreEsito.setHttpUser("HttpUserAA2");
		applicazioneAB.setConnettoreEsito(connettoreEsito);
		connettoreVerifica = new Connettore();
		connettoreVerifica.setAzioneInUrl(true);
		connettoreVerifica.setTipoAutenticazione(EnumAuthType.SSL);
		connettoreVerifica.setTipoSsl(EnumSslType.SERVER);
		connettoreVerifica.setSslTsLocation("sslTsLocation");
		connettoreVerifica.setSslTsPasswd("sslTsPasswd");
		connettoreVerifica.setSslTsType("sslTsType");
		applicazioneAB.setConnettoreVerifica(connettoreVerifica);
		idTributi = new ArrayList<Long>();
		idTributi.add(tributoAB1.getId());
		applicazioneAB.setIdTributi(idTributi);
		applicazioneAB.setPrincipal("PrincipalAA2");
		applicazioniBD.insertApplicazione(applicazioneAB);

		applicazioneBB = new Applicazione();
		applicazioneBB.setVersione(Versione.GPv1);
		applicazioneBB.setAbilitato(true);
		applicazioneBB.setCodApplicazione("CodapplicazioneBB");
		connettoreEsito = new Connettore();
		connettoreEsito.setAzioneInUrl(false);
		connettoreEsito.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettoreEsito.setHttpPassw("HttpPasswBB");
		connettoreEsito.setHttpUser("HttpUserBB");
		applicazioneBB.setConnettoreEsito(connettoreEsito);
		connettoreVerifica = new Connettore();
		connettoreVerifica.setAzioneInUrl(true);
		connettoreVerifica.setTipoAutenticazione(EnumAuthType.SSL);
		connettoreVerifica.setTipoSsl(EnumSslType.SERVER);
		connettoreVerifica.setSslTsLocation("sslTsLocation");
		connettoreVerifica.setSslTsPasswd("sslTsPasswd");
		connettoreVerifica.setSslTsType("sslTsType");
		applicazioneBB.setConnettoreVerifica(connettoreVerifica);
		idTributi = new ArrayList<Long>();
		idTributi.add(tributoBB1.getId());
		idTributi.add(tributoBB2.getId());
		applicazioneBB.setIdTributi(idTributi);
		applicazioneBB.setPrincipal("PrincipalBB");
		applicazioniBD.insertApplicazione(applicazioneBB);

	}

	protected void setupPortali() throws Exception {
		if(portale!=null) return;
		if(applicazioneAA == null) setupApplicazioni();
		if(stazione == null) setupStazione();

		portale = new Portale();
		portale.setAbilitato(true);
		portale.setCodPortale("CodPortale");
		portale.setDefaultCallbackURL("http://defaultCallbackURL");
		List<Long> applicazioni = new ArrayList<Long>();
		applicazioni.add(applicazioneAA.getId());
		portale.setIdApplicazioni(applicazioni);
		portale.setPrincipal("Principal");
		PortaliBD portaliBD = new PortaliBD(bd);
		portaliBD.insertPortale(portale);
	}

	protected void setupEventi() throws Exception {
		if(eventoIniziale!=null) return;

		eventoIniziale = new Evento();
		eventoIniziale.setAltriParametri("AltriParametri");
		eventoIniziale.setCanalePagamento("CanalePagamento");
		eventoIniziale.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		eventoIniziale.setCcp(Rpt.CCP_NA);
		eventoIniziale.setCodDominio("CodDominio");
		eventoIniziale.setCodErogatore("CodErogatore");
		eventoIniziale.setCodFruitore("CodFruitore");
		eventoIniziale.setCodPsp("CodPsp");
		eventoIniziale.setCodStazione("CodStazione");
		eventoIniziale.setComponente("Componente");
		eventoIniziale.setDataOraEvento(new Date());
		eventoIniziale.setEsito("Esito");
		eventoIniziale.setIdApplicazione(1l);
		eventoIniziale.setIuv("Iuv");
		eventoIniziale.setSottotipoEvento("SottotipoEvento");
		eventoIniziale.setTipoEvento(TipoEvento.nodoInviaRPT);
		eventoIniziale.setTipoVersamento(TipoVersamento.ADDEBITO_DIRETTO);

		EventiBD eventiBD = new EventiBD(bd);
		eventiBD.insertEvento(eventoIniziale);
		eventoFinale = new Evento();
		eventoFinale.setAltriParametri("AltriParametri");
		eventoFinale.setCanalePagamento("CanalePagamento");
		eventoFinale.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		eventoFinale.setCcp(Rpt.CCP_NA);
		eventoFinale.setCodDominio("CodDominio");
		eventoFinale.setCodErogatore("CodErogatore");
		eventoFinale.setCodFruitore("CodFruitore");
		eventoFinale.setCodPsp("CodPsp");
		eventoFinale.setCodStazione("CodStazione");
		eventoFinale.setComponente("Componente");
		eventoFinale.setDataOraEvento(new Date());
		eventoFinale.setEsito("Esito");
		eventoFinale.setIdApplicazione(1l);
		eventoFinale.setIuv("Iuv");
		eventoFinale.setSottotipoEvento("SottotipoEvento");
		eventoFinale.setTipoEvento(TipoEvento.nodoInviaRPT);
		eventoFinale.setTipoVersamento(TipoVersamento.ADDEBITO_DIRETTO);

		eventiBD.insertEvento(eventoFinale);
	}

	protected void setupSla() throws Exception {
		if(sla!=null) return;

		sla = new Sla();
		sla.setDescrizione("Descrizione");

		sla.setTipoEventoIniziale("TipoEventoIniziale");
		sla.setSottotipoEventoIniziale("sottotipoEventoIniziale");

		sla.setTipoEventoFinale("TipoEventoFinale");
		sla.setSottotipoEventoFinale("sottotipoEventoFinale");

		sla.setTempoA(10l);
		sla.setTempoB(30l);
		sla.setTolleranzaA(0.1d);
		sla.setTolleranzaB(0.3d);
		SlaBD slaBD = new SlaBD(bd);
		slaBD.insertSLA(sla);
	}

	protected void setupVersamenti() throws Exception {
		if(versamentoAA1_1 != null) return;
		setupAnagrafica();
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Iuv iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694);
		versamentoAA1_1 = new Versamento();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco("CodUnivocoDebitore");
		anagraficaDebitore.setRagioneSociale("RagioneSocialeDebitore");
		versamentoAA1_1.setAnagraficaDebitore(anagraficaDebitore);
		versamentoAA1_1.setCodDominio(dominioA.getCodDominio());
		versamentoAA1_1.setIdEnte(enteAA.getId());
		versamentoAA1_1.setDataScadenza(sdf.parse("31/12/2020"));
		versamentoAA1_1.setIdApplicazione(applicazioneAA.getId());
		versamentoAA1_1.setIdEnte(enteAA.getId());
		versamentoAA1_1.setImportoTotale(BigDecimal.valueOf(100));
		versamentoAA1_1.setCodVersamentoEnte(iuv.getIuv());
		versamentoAA1_1.setIuv(iuv.getIuv());
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("versamentoAA1_1_1");
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(90.10));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamentoAA1_1.getSingoliVersamenti().add(singoloVersamento);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("versamentoAA1_1_2");
		singoloVersamento.setIdTributo(tributoAA2.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA2.getTipoContabilita() + "/" + tributoAA2.getCodContabilita());
		singoloVersamento.setIndice(1);
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(9.90));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamentoAA1_1.getSingoliVersamenti().add(singoloVersamento);
		versamentoAA1_1.setStato(StatoVersamento.IN_ATTESA);
		versamentiBD.insertVersamento(versamentoAA1_1);
	}

	protected void setupRpt() throws Exception {
		if(rptAA1_1 != null) return;
		if(versamentoAA1_1 == null) setupVersamenti();
		if(psp == null) setupPsp();

		rptAA1_1 = new Rpt();
		rptAA1_1.setAutenticazioneSoggetto(Autenticazione.N_A);
		rptAA1_1.setCallbackURL("http://www.govpay.it/callbackUrl");
		rptAA1_1.setCcp(Rpt.CCP_NA);
		rptAA1_1.setCodCarrello(null);
		rptAA1_1.setCodMsgRichiesta(UUID.randomUUID().toString().replaceAll("-", ""));
		rptAA1_1.setCodSessione(null);
		rptAA1_1.setCodDominio(versamentoAA1_1.getCodDominio());
		rptAA1_1.setDataOraMsgRichiesta(new Date());
		rptAA1_1.setDataOraCreazione(new Date());
		rptAA1_1.setDescrizioneStato(null);
		rptAA1_1.setFaultCode(null);
		rptAA1_1.setFirmaRichiesta(FirmaRichiesta.NESSUNA);
		rptAA1_1.setIbanAddebito(null);;
		rptAA1_1.setIdPsp(psp.getId());
		rptAA1_1.setIdPortale(portale.getId());
		rptAA1_1.setIdStazione(stazione.getId());
		rptAA1_1.setIdVersamento(versamentoAA1_1.getId());
		rptAA1_1.setIuv(versamentoAA1_1.getIuv());
		rptAA1_1.setStatoRpt(StatoRpt.RPT_INVIATA_A_PSP);
		rptAA1_1.setDescrizioneStato("Descrizione Stato");
		rptAA1_1.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);

		byte[] rptByte = "<xmlRpt />".getBytes();

		RptBD rptBD = new RptBD(bd);
		rptBD.insertRpt(rptAA1_1, rptByte);
	}

	protected void setupRt() throws Exception {
		if(rt != null) return;
		if(rptAA1_1 == null) setupRpt();

		rt = new Rt();
		rt.setCodMsgRicevuta(UUID.randomUUID().toString().replaceAll("-", ""));
		rt.setDataOraMsgRicevuta(new Date());
		rt.setDescrizioneStato("DescrizioneStato");
		rt.setEsitoPagamento(EsitoPagamento.PAGAMENTO_ESEGUITO);
		Anagrafica anagraficaAttestante = new Anagrafica();
		anagraficaAttestante.setRagioneSociale("RagioneSocialeAttestante");
		anagraficaAttestante.setCodUnivoco("CodUnivocoAttestante");
		rt.setAnagraficaAttestante(anagraficaAttestante);
		rt.setIdRpt(rptAA1_1.getId());
		rt.setStato(StatoRt.ACCETTATA);

		byte[] rtByte = "<xmlRt />".getBytes();

		RtBD rtBD = new RtBD(bd);
		rtBD.insertRt(rt, rtByte);

	}

	protected void setupRr() throws Exception {
		if(rr != null) return;
		if(rt == null) setupRt();

		rr = new Rr();
		rr.setCodMsgRevoca("codMsgRevoca");
		rr.setDataOraCreazione(new Date());
		rr.setDataOraMsgRevoca(new Date());
		rr.setDescrizioneStato("descrizioneStato");
		rr.setIdRt(rt.getId());
		rr.setImportoTotaleRevocato(2000.01);
		rr.setStato(it.govpay.bd.model.Rr.Stato.RR_DA_INVIARE_A_NODO);
		List<SingolaRevoca> singolaRevocaList = new ArrayList<SingolaRevoca>();
		SingolaRevoca singolaRevoca = new SingolaRevoca();
		singolaRevoca.setCausaleRevoca("causaleRevoca");
		singolaRevoca.setDatiAggiuntiviEsito("datiAggiuntiviEsito");
		singolaRevoca.setDatiAggiuntiviRevoca("datiAggiuntiviRevoca");
		singolaRevoca.setDescrizioneStato("descrizioneStato");
		singolaRevoca.setSingoloImporto(10.01);
		singolaRevoca.setIdSingoloVersamento(versamentoAA1_1.getSingoliVersamenti().get(0).getId());
		singolaRevoca.setIdEr(1l);
		singolaRevoca.setStato(Stato.SINGOLA_REVOCA_REVOCATA);
		singolaRevocaList.add(singolaRevoca);
		rr.setSingolaRevocaList(singolaRevocaList);
		byte[] xml = "<xml />".getBytes();
		RrBD rrBD = new RrBD(bd);
		rrBD.insertRr(rr, xml);

	}
	
	protected void setupOperatore() throws Exception {
		if(operatore != null) return;

		operatore = new Operatore();
		operatore.setAbilitato(true);
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCodUnivoco("CodUnivocoOperatore");
		anagrafica.setRagioneSociale("Operatore");
		operatore.setNome("Nome Cognome");
		operatore.setPrincipal("admin");
		operatore.setProfilo(ProfiloOperatore.ADMIN);
		OperatoriBD operatoriBD = new OperatoriBD(bd);
		operatoriBD.insertOperatore(operatore);

	}

	protected void setupAnagrafica() throws Exception {
		setupIntermediario();
		setupStazione();
		setupDominio();
		setupEnte();
		setupTributi();
		setupApplicazioni();
		setupPortali();
		setupPsp();
		setupOperatore();
	}

	protected static BasicBD bd; 
}
