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
package it.govpay.web.rs;

import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ejb.controller.DistintaEJB;
import it.govpay.ejb.controller.PendenzaEJB;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.filter.DistintaFilter;
import it.govpay.ejb.filter.EnteCreditoreFilter;
import it.govpay.ejb.model.ConnettoreModel;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.EnteCreditoreModel.EnumStato;
import it.govpay.ejb.model.GatewayPagamentoModel;
import it.govpay.ejb.model.OperatoreModel;
import it.govpay.ejb.model.PendenzaModel;
import it.govpay.ejb.model.ScadenzarioModel;
import it.govpay.ejb.model.ScadenzarioModelId;
import it.govpay.ejb.model.TributoModel;
import it.govpay.ejb.model.TributoModel.EnumStatoTributo;
import it.govpay.ndp.controller.FrController;
import it.govpay.ndp.controller.RptController;
import it.govpay.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ndp.ejb.EventoEJB;
import it.govpay.ndp.ejb.filter.EventoFilter;
import it.govpay.ndp.ejb.filter.IntermediarioFilter;
import it.govpay.ndp.ejb.filter.ScadenzarioFilter;
import it.govpay.ndp.model.DominioEnteModel;
import it.govpay.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ndp.model.IntermediarioModel;
import it.govpay.ndp.model.StazioneModel;
import it.govpay.web.controller.GatewayController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/* *******************************************************
 * 
 * TODO:MINO classe da cancellare. serve solo per i test
 * 
 * ******************************************************* */
@Path("/test")
public class TestRsService {

	private static final String ID_ENTE_CREDITORE_DEFAULT = "00000000000000000000";

	@Inject
	PendenzaEJB pendenzaEjb;

	@Inject
	DistintaEJB distintaEjb;

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	EventoEJB eventoEjb;

	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;

	@Inject
	GatewayController gatewayCtrl;

	@Inject
	FrController frCtrl;
	
	@Inject
	RptController rptCtrl;
	
	@Inject
	Logger log;
	
	@GET
	@Path("/distinte")
	public List<DistintaModel> getDistinte(
			@QueryParam(value = "limit") Integer limit, 
			@QueryParam(value = "offset") Integer offset, 
			@QueryParam(value = "iuv") String iuv, 
			@QueryParam(value = "importoDa") Integer importoDa, 
			@QueryParam(value = "importoA") Integer importoA) throws GovPayException {

		Calendar cal = Calendar.getInstance();

		cal.set(2014, 11, 2);
		Date dataInizio = cal.getTime();
		cal.set(2014, 11, 31);
		Date dataFine = cal.getTime();
		EnumStatoDistinta stato = EnumStatoDistinta.ESEGUITO;
		String cfEnteCreditore = null;// "12345678903";
		List<String> identificativiEnteCreditore = new ArrayList<String>();
		//		identificativiEnteCreditore.add("12345678903");
		//		identificativiEnteCreditore.add("00000000000");
		String cfVersanteODebitore = "NRDLNZ80P19D612M";


		DistintaFilter filtro = new DistintaFilter();

		filtro.setLimit(limit);
		filtro.setOffset(offset);

		filtro.setIuv(iuv);
		filtro.setImportoA(importoA != null ? new BigDecimal(importoA) : null);
		filtro.setImportoDa(importoDa != null ? new BigDecimal(importoDa) : null);


		filtro.setDataInizio(dataInizio);
		filtro.setDataFine(dataFine);
		filtro.setStato(stato);
		filtro.setCfEnteCreditore(cfEnteCreditore);
		filtro.setIdentificativiEnteCreditore(identificativiEnteCreditore);
		filtro.setCfVersanteODebitore(cfVersanteODebitore);

		System.out.println("totale: " + distintaEjb.countAllDistinte(filtro));

		return distintaEjb.findAllDistinte(filtro);
	}

	@GET
	@Path("/enti/count")
	public int getCountEnti(
			@QueryParam(value = "limit") Integer limit, 
			@QueryParam(value = "offset") Integer offset,
			@QueryParam(value = "user") String user
			) throws GovPayException {

		EnteCreditoreFilter filtro = new EnteCreditoreFilter();

		filtro.setLimit(limit);
		filtro.setOffset(offset);

		OperatoreModel op = new OperatoreModel();
		op.setUsername(user);
		filtro.setOperatore(op);

		return anagraficaEjb.countEntiCreditori(filtro);
	}

	@GET
	@Path("/enti/find")
	public List<EnteCreditoreModel> getEnti(
			@QueryParam(value = "limit") Integer limit, 
			@QueryParam(value = "offset") Integer offset,
			@QueryParam(value = "user") String user
			) throws GovPayException {

		EnteCreditoreFilter filtro = new EnteCreditoreFilter();

		filtro.setLimit(limit);
		filtro.setOffset(offset);

		OperatoreModel op = new OperatoreModel();
		op.setUsername(user);
		filtro.setOperatore(op);

		return anagraficaEjb.findAllEntiCreditori(filtro);
	}

	@GET
	@Path("/eventi")
	public List<Evento> getEventi(
			@QueryParam(value = "limit") Integer limit, 
			@QueryParam(value = "offset") Integer offset, 
			@QueryParam(value = "iuv") String iuv) throws GovPayException {

		EventoFilter filtro = new EventoFilter();

		filtro.setLimit(limit);
		filtro.setOffset(offset);
		filtro.setIuv(iuv);

		System.out.println("totale: " + eventoEjb.count(filtro));

		return eventoEjb.findAll(filtro);
	}



	@GET
	@Path("/dominio")
	public DominioEnteModel getDominioEnte() throws GovPayException {

		//		anagraficaDominioEjb.salvaIntermediarioNdp(null, buildDominoEnteMock());
		return anagraficaDominioEjb.getDominioEnte("12345678903", "01386030488", "01386030488_01");
	}	


	@GET
	@Path("/listaPSP")
	public String aggiornaListaPSP() throws GovPayException {
		try {
			gatewayCtrl.aggiornaListaPSP();
		} catch (GovPayException e) {
			e.printStackTrace();
		}
		return "finito";
	}	

	@GET
	@Path("/operatore")
	public OperatoreModel getOperatore(
			@QueryParam(value = "username") String username, 
			@QueryParam(value = "password") String password) throws GovPayException {

		OperatoreModel operatore = anagraficaEjb.getOperatore(username, password);
		return operatore;
	}	


	@GET
	@Path("/salvaEnte")
	public List<EnteCreditoreModel> salvaEnteCreditore() throws GovPayException {

		EnteCreditoreModel enteDaCancellare = anagraficaEjb.getCreditoreByIdLogico("00067530709");
		if(enteDaCancellare != null) {
			anagraficaEjb.cancellaEnteCreditore(enteDaCancellare.getIdEnteCreditore());


		}
		EnteCreditoreModel enteDaInserire = new EnteCreditoreModel();
		//		enteDaInserire.setIdEnteCreditore("00000000000000000001");
		enteDaInserire.setIdentificativoUnivoco("COMUNE_TERMOLI");
		enteDaInserire.setIdFiscale("00067530709");
		enteDaInserire.setStato(EnumStato.A);

		enteDaInserire.setDenominazione("Comune di Termoli");
		enteDaInserire.setIndirizzo("Via Sannitica, 5");
		enteDaInserire.setLocalita("Termoli");
		enteDaInserire.setProvincia("CB");

		String idEnte = anagraficaEjb.salvaEnteCreditore(null, enteDaInserire, null);

		System.out.println("======================================>  idEnte: " + idEnte);


		//
		// aggancio anche un dominio
		//
		DominioEnteModel dominioente = new DominioEnteModel();
		enteDaInserire.setIdEnteCreditore(idEnte);
		dominioente.setEnteCreditore(enteDaInserire);
		dominioente.setIdDominio("00000000001");
		IntermediarioModel intermediario = new IntermediarioModel();
		intermediario.setIdIntermediarioPA("01386030488");
		dominioente.setIntermediario(intermediario);

		anagraficaDominioEjb.salvaDominioEnte(null, dominioente);


		// carico gli enti inseriti e modificati

		EnteCreditoreModel e1 = anagraficaEjb.getCreditoreByIdFisico(ID_ENTE_CREDITORE_DEFAULT);
		EnteCreditoreModel e2 = anagraficaEjb.getCreditoreByIdLogico(enteDaInserire.getIdFiscale());


		List<EnteCreditoreModel> listaEnti = new ArrayList<EnteCreditoreModel>();
		listaEnti.add(e1);
		listaEnti.add(e2);
		return listaEnti;
	}	

	@GET
	@Path("/salvaConnettore")
	public List<ConnettoreModel> salvaConnettore() throws GovPayException {



		ConnettoreModel cnt = new ConnettoreModel();
		cnt.setHttpUser("HTTPUSER");
		cnt.setHttpPassw("HTTPPASSW");
		//		cnt.setTipoAutenticazione(EnumAuthType.SSL);
		//		cnt.setTipoSsl(EnumSslType.SERVER);
		//		cnt.setSslKsLocation("PROPR AGGIUNTA");

		anagraficaDominioEjb.salvaConnettore(7l, cnt);


		ConnettoreModel cnt2 = new ConnettoreModel();
		cnt2.setHttpUser("APPENA_INSERITO");
		cnt2.setHttpPassw("STESSA_PASSWORD");
		//		cnt2.setTipoAutenticazione(EnumAuthType.SSL);
		//		cnt2.setTipoSsl(EnumSslType.SERVER);

		anagraficaDominioEjb.salvaConnettore(null, cnt2);


		return anagraficaDominioEjb.findAllConnettori();

	}


	@GET
	@Path("/scadenzario")
	public ScadenzarioModel scadenzario() throws GovPayException {

		DistintaModel distintaModel = distintaEjb.getDistinta("12345678903", "RF40000000000000128");
		ScadenzarioModel scadenzarioModel = anagraficaEjb.getScadenzario(distintaModel);
		return scadenzarioModel;

	}

	@GET
	@Path("/scadenzari/count")
	public int scadenzariCount(	@QueryParam(value = "limit") Integer limit, 
			@QueryParam(value = "offset") Integer offset, 
			@QueryParam(value = "idEnte") String idEnte, 
			@QueryParam(value = "idIntermediarioPA") String idIntermediarioPA) throws GovPayException {

		ScadenzarioFilter filtro = new ScadenzarioFilter();
		filtro.setLimit(limit);
		filtro.setOffset(offset);
		filtro.setIdEnteCreditore(idEnte);
		filtro.setIdIntermediarioPA(idIntermediarioPA);

		return anagraficaDominioEjb.countAllScadenzari(filtro);
	}

	@GET
	@Path("/scadenzari/find")
	public List<ScadenzarioModel> scadenzariFind(
			@QueryParam(value = "limit") Integer limit, 
			@QueryParam(value = "offset") Integer offset, 
			@QueryParam(value = "idEnte") String idEnte, 
			@QueryParam(value = "idIntermediarioPA") String idIntermediarioPA) throws GovPayException {

		ScadenzarioFilter filtro = new ScadenzarioFilter();
		filtro.setLimit(limit);
		filtro.setOffset(offset);
		filtro.setIdEnteCreditore(idEnte);
		filtro.setIdIntermediarioPA(idIntermediarioPA);

		return anagraficaDominioEjb.findAllScadenzari(filtro);
	}

	@GET
	@Path("/scadenzarioStazione")
	public ScadenzarioModel scadenzarioStazione() throws GovPayException {

		ScadenzarioModel scad = anagraficaDominioEjb.getScadenzarioById(new ScadenzarioModelId(ID_ENTE_CREDITORE_DEFAULT,"SIL"));

//		ScadenzarioModel scad = mappa.keySet().iterator().next();

		//		StazioneModel staz = mappa.get(scad);
		//		staz.setIdStazioneIntermediarioPA("01386030488_ZZ");

		scad.setIdSystem("SIL2");

		StazioneModel stazione = new StazioneModel();
		stazione.setIdIntermediarioPA("01386030488");
		stazione.setIdStazioneIntermediarioPA("01386030488_02");


		anagraficaDominioEjb.salvaScadenzario(null, scad, stazione.getIdStazioneIntermediarioPA());


		// ricarico tutto
		scad = anagraficaDominioEjb.getScadenzarioById(new ScadenzarioModelId(ID_ENTE_CREDITORE_DEFAULT,"SIL2"));
		return scad;

	}

	@GET
	@Path("/intermediari/count")
	public int intermediariCount() throws GovPayException {

		IntermediarioFilter filter = new IntermediarioFilter();
		return anagraficaDominioEjb.countAllIntermediari(filter);
	}

	@GET
	@Path("/intermediari/find")
	public List<IntermediarioModel> intermediariFind(
			@QueryParam(value = "id") String id) throws GovPayException {

		IntermediarioFilter filter = new IntermediarioFilter();
		if(id==null) return anagraficaDominioEjb.findAllIntermediari(filter);
		else {
			List<IntermediarioModel> m = new ArrayList<IntermediarioModel>();
			m.add(anagraficaDominioEjb.getIntermediarioById(id));
			return m;
		}

	}

	@GET
	@Path("/intermediari/stazioni")
	public List<StazioneModel> getStazioniIntermediario(
			@QueryParam(value = "id") String id) throws GovPayException {

		return anagraficaDominioEjb.getStazioniIntermediario(id);

	}

	@GET
	@Path("/tributi/all")
	public List<TributoModel> getTributi(
			@QueryParam(value = "idEnte") String idEnte) throws GovPayException {

		return anagraficaEjb.getTributi(idEnte);

	}

	@GET
	@Path("/tributi/find")
	public TributoModel getTributo(
			@QueryParam(value = "idEnte") String idEnte, 
			@QueryParam(value = "codTributo") String codTributo) throws GovPayException {

		return anagraficaEjb.getTributoById(idEnte, codTributo);
	}

	@GET
	@Path("/tributi/new")
	public TributoModel nuovoTributo(
			@QueryParam(value = "idEnte") String idEnte, 
			@QueryParam(value = "codTributo") String codTributo) throws GovPayException {

		ScadenzarioFilter filtroScadenzario = new ScadenzarioFilter();
		filtroScadenzario.setIdEnteCreditore(idEnte);
		List<ScadenzarioModel>  lista = anagraficaDominioEjb.findAllScadenzari(filtroScadenzario);
		ScadenzarioModel scadenzario = lista.get(0);

		TributoModel m = new TributoModel();
		m.setCodiceTributo(codTributo);
		m.setDescrizione("descrizione tributo " + m.getCodiceTributo());
		m.setIbanAccredito("IBANACCREDITO");
		m.setIdEnteCreditore(idEnte);
		m.setIdSistema(scadenzario.getIdSystem());
		//m.setIdTributo(idTributo); viene settato il default (GovPayConstants.CATEGORIA_TRIBUTO_DEFAULT)
		m.setStato(EnumStatoTributo.A);

		anagraficaEjb.salvaTributo(null, null, m);

		return anagraficaEjb.getTributoById(idEnte, codTributo);
	}

	@GET
	@Path("/tributi/update")
	public TributoModel modificaTributo(
			@QueryParam(value = "idEnte") String idEnte, 
			@QueryParam(value = "codTributo") String codTributo) throws GovPayException {

		TributoModel m = new TributoModel();
		m.setCodiceTributo(codTributo);
		m.setDescrizione("M_" + m.getDescrizione());
		m.setIbanAccredito("IBANACCREDITO");
		m.setIdEnteCreditore(idEnte);
		//m.setIdSistema(scadenzario.getIdSystem());
		//m.setIdTributo(idTributo); viene settato il default (GovPayConstants.CATEGORIA_TRIBUTO_DEFAULT)
		m.setStato(EnumStatoTributo.A);

		anagraficaEjb.salvaTributo(idEnte, codTributo, m);

		return anagraficaEjb.getTributoById(idEnte, codTributo);
	}

	@GET
	@Path("/gateway/find")
	public List<GatewayPagamentoModel> findAllGatewayPagamento() throws GovPayException {
		return anagraficaEjb.findAllGatewayPagamento();
	}

	@GET
	@Path("/gateway/count")
	public int countGatewayPagamento() throws GovPayException {
		return anagraficaEjb.countGatewayPagamento();
	}

	@GET
	@Path("/gateway/update")
	public void aggiornaStatoGateway() throws GovPayException {
		GatewayPagamentoModel gateway = new GatewayPagamentoModel();
		gateway.setIdGateway(17l);
		gateway.setStato(it.govpay.ejb.model.GatewayPagamentoModel.EnumStato.ATTIVO);
		anagraficaEjb.aggiornaStatoGateway(gateway);
	}


	@GET
	@Path("/pendenze")
	public List<PendenzaModel> getPendenze(
			@QueryParam(value = "idDistinta") Long idDistinta
			) throws GovPayException {

		if(idDistinta == null)
			idDistinta = 5l;

		return pendenzaEjb.getPendenze(idDistinta);
	}
	
	
	@GET
	@Path("/fr")
	public String gestioneFlussi() throws GovPayException {
		ThreadContext.put("proc", "DownloadRendicontazioni");
    	ThreadContext.put("dom", null);
    	ThreadContext.put("iuv", null);
    	ThreadContext.put("ccp", null);
    	String html = "<html><body>";
    	html += "Attivazione del DownloadRendicontazioniTimer";

		// Per ciascun ente
		List<EnteCreditoreModel> enti = anagraficaEjb.getEntiCreditori();

		
		for(EnteCreditoreModel ente : enti) {
			
			ThreadContext.put("dom", ente.getIdFiscale());
			html += "<br>Download delle rendicontazioni " + ente.getIdFiscale();
			
			List<ScadenzarioModel> scadenzari = anagraficaEjb.getScadenzari(ente.getIdEnteCreditore());
			for(ScadenzarioModel scadenzario : scadenzari) {
				try {
					html += "<br>Download delle rendicontazioni " + scadenzario.getDescrizione();
					frCtrl.downloadRendicontazioni(ente.getIdEnteCreditore(), scadenzario);
				} catch (GovPayException e) {
					html += "<br>Download delle rendicontazioni fallita: " + e.getTipoException() + ": " + e.getDescrizione();
				}
			}
		}
		
		html += "</body></html>";
		return html;
	}
	
	
	@GET
	@Path("/pendenti")
	public void pendenti(){
		ThreadContext.put("proc", "GestionePendenti");
    	ThreadContext.put("dom", null);
    	ThreadContext.put("iuv", null);
    	ThreadContext.put("ccp", null);
		//log.info("Attivazione del ControlloPendentiTimer.");

		// Per ciascun ente
		List<EnteCreditoreModel> enti = anagraficaEjb.getEntiCreditori();

		for(EnteCreditoreModel ente : enti) {
			ThreadContext.put("dom", ente.getIdFiscale());
			log.info("Gestione pagamenti pendenti per il creditore " + ente.getDenominazione() + " ("+ ente.getIdEnteCreditore() + ")");
			
			List<ScadenzarioModel> scadenzari = anagraficaEjb.getScadenzari(ente.getIdEnteCreditore());
			for(ScadenzarioModel scadenzario : scadenzari) {
				try {
					rptCtrl.verificaPendenti(ente.getIdEnteCreditore(), scadenzario);
				} catch (GovPayException e) {
					log.error("Verifica pagamenti pendenti fallita: " + e.getTipoException() + ": " + e.getDescrizione(), e);
				}
			}
		}
	}

}

