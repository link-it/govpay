package it.govpay.core.business;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.RicevutaPagamento;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.stampe.model.RicevutaTelematicaInput;
import it.govpay.stampe.model.RicevutaTelematicaInput.ElencoVoci;
import it.govpay.stampe.model.VoceRicevutaTelematicaInput;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaCostanti;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaPdf;
import it.govpay.stampe.pdf.rt.utils.RicevutaTelematicaProperties;

public class RicevutaTelematica  extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(RicevutaTelematica.class);
	private SimpleDateFormat sdfDataPagamento = new SimpleDateFormat("dd/MM/yyyy");

	public RicevutaTelematica(BasicBD basicBD) {
		super(basicBD);
	}


	public LeggiRicevutaDTOResponse creaPdfRicevuta(LeggiRicevutaDTO leggiRicevutaDTO,it.govpay.bd.model.Rpt rpt) throws ServiceException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();

		try {
			RicevutaPagamento ricevuta = new RicevutaPagamento();
			ricevuta.setCodDominio(leggiRicevutaDTO.getIdDominio());
			ricevuta.setIuv(leggiRicevutaDTO.getIuv());
			ricevuta.setCcp(leggiRicevutaDTO.getCcp());

			RicevutaTelematicaProperties rtProperties = RicevutaTelematicaProperties.getInstance();
			RicevutaTelematicaInput input = this.fromRpt(rpt);
			ricevuta = RicevutaTelematicaPdf.getInstance().creaRicevuta(log, input, ricevuta, rtProperties );
			response.setPdf(ricevuta.getPdf()); 
		}catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		return response;
	}

	public RicevutaTelematicaInput fromRpt(it.govpay.bd.model.Rpt rpt) throws Exception{
		RicevutaTelematicaInput input = new RicevutaTelematicaInput();

		this.impostaAnagraficaEnteCreditore(rpt, input);
		Versamento versamento = rpt.getVersamento(this);

		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt());
		
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		
		List<CtDatiSingoloPagamentoRT> datiSingoloPagamento = datiPagamento.getDatiSingoloPagamento();
		
		input.setCCP(datiPagamento.getCodiceContestoPagamento());
		input.setIUV(datiPagamento.getIdentificativoUnivocoVersamento());

		StringBuilder sbIstitutoAttestante = new StringBuilder();
		if(rt.getIstitutoAttestante() != null){
			CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();

			sbIstitutoAttestante.append(istitutoAttestante.getDenominazioneAttestante());
			sbIstitutoAttestante.append(", ");
			sbIstitutoAttestante.append(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
		}
		input.setIstituto(sbIstitutoAttestante.toString());
		
		
		input.setElencoVoci(this.getElencoVoci(rt,datiSingoloPagamento,input));
		input.setImporto(datiPagamento.getImportoTotalePagato().doubleValue());
		input.setOggettoDelPagamento(versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().getSimple() : "");
		
		EsitoPagamento esitoPagamento = EsitoPagamento.toEnum(datiPagamento.getCodiceEsitoPagamento());
		
		String stato = "";
		switch(esitoPagamento) {
		case DECORRENZA_TERMINI:
			stato = RicevutaTelematicaCostanti.DECORRENZA_TERMINI;
			break;
		case DECORRENZA_TERMINI_PARZIALE:
			stato = RicevutaTelematicaCostanti.DECORRENZA_TERMINI_PARZIALE;
			break;
		case PAGAMENTO_ESEGUITO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO;
			break;
		case PAGAMENTO_NON_ESEGUITO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO;
			break;
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_PARZIALMENTE_ESEGUITO;
			break;
		default:
			break;
		
		}
		
		input.setStato(stato);



		return input;
	}

	private ElencoVoci getElencoVoci(CtRicevutaTelematica rt, List<CtDatiSingoloPagamentoRT> datiSingoloPagamento, RicevutaTelematicaInput input) {
		ElencoVoci elencoVoci = new ElencoVoci();
		
		String dataPagamento = null;
		
		for (CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT : datiSingoloPagamento) {
			VoceRicevutaTelematicaInput voce = new VoceRicevutaTelematicaInput();
			
			voce.setDescrizione(ctDatiSingoloPagamentoRT.getCausaleVersamento());
			voce.setIdRiscossione(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
			voce.setImporto(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().doubleValue());
			voce.setStato(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0 ? RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO : RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);
						
			elencoVoci.getVoce().add(voce);
			
			
			// data pagamento
			if(dataPagamento == null && ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento() != null) {
				dataPagamento = this.sdfDataPagamento.format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
			}
		}
		input.setData(dataPagamento);
		
		return elencoVoci;
	}


	private it.govpay.bd.model.Dominio impostaAnagraficaEnteCreditore(it.govpay.bd.model.Rpt rpt, RicevutaTelematicaInput input) throws ServiceException {
		it.govpay.bd.model.Dominio dominio = rpt.getDominio(this);
		String codDominio = dominio.getCodDominio();

		input.setEnteDenominazione(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));

		this.impostaIndirizzoEnteCreditore(dominio, input);
		return dominio;
	}

	private void impostaIndirizzoEnteCreditore(it.govpay.bd.model.Dominio dominio, RicevutaTelematicaInput input) throws ServiceException {
		Anagrafica anagraficaEnteCreditore = dominio.getAnagrafica();
		if(anagraficaEnteCreditore != null) {
			String indirizzo = StringUtils.isNotEmpty(anagraficaEnteCreditore.getIndirizzo()) ? anagraficaEnteCreditore.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(anagraficaEnteCreditore.getCivico()) ? anagraficaEnteCreditore.getCivico() : "";
			String cap = StringUtils.isNotEmpty(anagraficaEnteCreditore.getCap()) ? anagraficaEnteCreditore.getCap() : "";
			String localita = StringUtils.isNotEmpty(anagraficaEnteCreditore.getLocalita()) ? anagraficaEnteCreditore.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(anagraficaEnteCreditore.getProvincia()) ? (" (" +anagraficaEnteCreditore.getProvincia() +")" ) : "";
			String indirizzoCivico = indirizzo + " " + civico;
			String capCitta = cap + " " + localita + provincia;

			String indirizzoEnte = indirizzoCivico + ",";

			if(indirizzoEnte.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoEnte(indirizzoEnte);
			}else {
				input.setIndirizzoEnte(indirizzoEnte);
			}

			if(capCitta.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setLuogoEnte(capCitta);
			}else {
				input.setLuogoEnte(capCitta);
			}
		}
	}
}
