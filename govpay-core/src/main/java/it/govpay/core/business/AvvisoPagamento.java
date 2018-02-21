package it.govpay.core.business;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.AvvisiPagamentoBD;
import it.govpay.bd.pagamento.filters.AvvisoPagamentoFilter;
import it.govpay.core.business.model.InserisciAvvisoDTO;
import it.govpay.core.business.model.InserisciAvvisoDTOResponse;
import it.govpay.core.business.model.LeggiAvvisoDTO;
import it.govpay.core.business.model.LeggiAvvisoDTOResponse;
import it.govpay.core.business.model.ListaAvvisiDTO;
import it.govpay.core.business.model.ListaAvvisiDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Intermediario;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.model.avvisi.AvvisoPagamentoInput;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;

public class AvvisoPagamento extends BasicBD {


	private SimpleDateFormat sdfDataScadenza = new SimpleDateFormat("dd/MM/yyyy");
	private static Logger log = LoggerWrapperFactory.getLogger(AvvisoPagamento.class);

	public AvvisoPagamento(BasicBD basicBD) {
		super(basicBD);
	}

	public InserisciAvvisoDTOResponse inserisciAvviso(InserisciAvvisoDTO inserisciAvviso) throws InternalException {
		InserisciAvvisoDTOResponse response = new InserisciAvvisoDTOResponse();
		try {
			log.info("Inserimento Avviso Pagamento [Dominio: " + inserisciAvviso.getCodDominio() +" | IUV: " + inserisciAvviso.getIuv() + "]");

			it.govpay.model.avvisi.AvvisoPagamento avviso = new it.govpay.model.avvisi.AvvisoPagamento();
			avviso.setCodDominio(inserisciAvviso.getCodDominio());
			avviso.setDataCreazione(inserisciAvviso.getDataCreazione());
			avviso.setIuv(inserisciAvviso.getIuv());
			avviso.setStato(inserisciAvviso.getStato());

			AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);
			avvisiBD.insertAvviso(avviso);

			response.setAvviso(avviso);

			log.info("Avviso Pagamento inserito con id: " + avviso.getId());
			return response;
		} catch (ServiceException e) {
			log.error("Inserimento Avviso Pagamento fallito", e);
			throw new InternalException(e);
		}
	}


	public ListaAvvisiDTOResponse getAvvisi(ListaAvvisiDTO listaAvvisi) throws ServiceException {
		ListaAvvisiDTOResponse response = new ListaAvvisiDTOResponse();

		AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);
		AvvisoPagamentoFilter filter = avvisiBD.newFilter();
		filter.setStato(StatoAvviso.DA_STAMPARE);
		filter.setOffset(listaAvvisi.getOffset());
		filter.setLimit(listaAvvisi.getLimit());

		List<it.govpay.model.avvisi.AvvisoPagamento> avvisi = avvisiBD.findAll(filter);
		response.setAvvisi(avvisi);

		return response;
	}


	public LeggiAvvisoDTOResponse getAvviso(LeggiAvvisoDTO leggiAvviso) throws ServiceException {
		LeggiAvvisoDTOResponse response = new LeggiAvvisoDTOResponse();
		try {
			AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);

			it.govpay.model.avvisi.AvvisoPagamento avviso = avvisiBD.getAvviso(leggiAvviso.getCodDominio(), leggiAvviso.getIuv());

			response.setAvviso(avviso);
			return response;
		} catch (NotFoundException e) {
			return null;
		} 
	}

	public PrintAvvisoDTOResponse printAvviso(PrintAvvisoDTO printAvviso) {
		PrintAvvisoDTOResponse response = new PrintAvvisoDTOResponse();

		it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento = printAvviso.getAvviso();
		log.info("Creazione PDF Avviso Pagamento [Dominio: " + avvisoPagamento.getCodDominio() +" | IUV: " + avvisoPagamento.getIuv() + "]");
		AvvisoPagamentoInput input = printAvviso.getInput();
		AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
		try {
			it.govpay.model.avvisi.AvvisoPagamento avvisoPagamentoResponse  = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, avvisoPagamento, avProperties);
			
			log.info("Salvataggio PDF Avviso Pagamento [Dominio: " + avvisoPagamento.getCodDominio() +" | IUV: " + avvisoPagamento.getIuv() + "] sul db in corso...");
			// aggiornamento della entry sul db
			AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);
			avvisoPagamentoResponse.setStato(StatoAvviso.STAMPATO); 
			avvisiBD.updateAvviso(avvisoPagamentoResponse);
			log.info("Salvataggio PDF Avviso Pagamento [Dominio: " + avvisoPagamento.getCodDominio() +" | IUV: " + avvisoPagamento.getIuv() + "] sul db completato.");
			response.setAvviso(avvisoPagamentoResponse);
		} catch (Exception e) {
			log.error("Creazione Pdf Avviso Pagamento fallito", e);
		}

		return response;
	}

	public AvvisoPagamentoInput fromVersamento(it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento, it.govpay.bd.model.Versamento versamento) throws Exception {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();

		Dominio dominio = versamento.getUo(this).getDominio(this);
		String codDominio = dominio.getCodDominio();
		Anagrafica anagraficaDominio = dominio.getAnagrafica();
		
		input.setEnteDenominazione(dominio.getRagioneSociale());
		input.setEnteIdentificativo(codDominio);
		input.setEnteIdentificativoSplit(this.splitString(codDominio));
		input.setEnteCbill(dominio.getCbill());
		
		if(anagraficaDominio != null) {
			input.setEnteArea(anagraficaDominio.getArea());
			input.setEnteUrl(anagraficaDominio.getUrlSitoWeb());
			input.setEntePeo(anagraficaDominio.getEmail());
			input.setEntePec(anagraficaDominio.getPec());
		}
		

		Anagrafica anagraficaDebitore = versamento.getAnagraficaDebitore();
		if(anagraficaDebitore != null) {
			String indirizzoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getIndirizzo()) ? anagraficaDebitore.getIndirizzo() : "";
			String civicoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCivico()) ? anagraficaDebitore.getCivico() : "";
			String capDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCap()) ? anagraficaDebitore.getCap() : "";
			String localitaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getLocalita()) ? anagraficaDebitore.getLocalita() : "";
			String provinciaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getProvincia()) ? (" (" +anagraficaDebitore.getProvincia() +")" ) : "";
			String indirizzoCivicoDebitore = indirizzoDebitore + " " + civicoDebitore;
			String capCittaDebitore = capDebitore + " " + localitaDebitore + provinciaDebitore;
			
			input.setIntestatarioDenominazione(anagraficaDebitore.getRagioneSociale());
			input.setIntestatarioIdentificativo(anagraficaDebitore.getCodUnivoco());
			input.setIntestatarioIndirizzo1(indirizzoCivicoDebitore);
			input.setIntestatarioIndirizzo2(capCittaDebitore);
		}

		Intermediario intermediario = dominio.getStazione().getIntermediario(this);
		if(intermediario != null) {
			// visualizzao l'ente partner solo se non coincide con il dominio
			if(!intermediario.getCodIntermediario().equals(dominio.getCodDominio()))
				input.setEntePartner(intermediario.getDenominazione());
		}

		it.govpay.core.business.model.Iuv iuvGenerato = 
				IuvUtils.toIuv(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), 
						versamento.getIuv(this), versamento.getImportoTotale());

		if(versamento.getCausaleVersamento() != null)
			input.setAvvisoCausale(versamento.getCausaleVersamento().getSimple());
		
		// avviso_mav 
		input.setAvvisoMav(false);
		
		if(versamento.getImportoTotale() != null)
			input.setAvvisoImporto(versamento.getImportoTotale().doubleValue());
		
		if(versamento.getDataScadenza() != null)
			input.setAvvisoScadenza(this.sdfDataScadenza.format(versamento.getDataScadenza()));
		
		if(iuvGenerato.getNumeroAvviso() != null) {
			input.setAvvisoNumero(iuvGenerato.getNumeroAvviso());
			input.setAvvisoNumeroSplit(this.splitString(iuvGenerato.getNumeroAvviso()));
		}
		
		if(iuvGenerato.getIuv() != null) {
			input.setAvvisoIuv(iuvGenerato.getIuv());
			input.setAvvisoIuvSplit(this.splitString(iuvGenerato.getIuv()));
		}
		
		if(iuvGenerato.getBarCode() != null)
			input.setAvvisoBarcode(new String(iuvGenerato.getBarCode()));
		
		if(iuvGenerato.getQrCode() != null)
		input.setAvvisoQrcode(new String(iuvGenerato.getQrCode()));

		return input;
	}
	
	public String splitString(String start) {
		if(start == null || start.length() <= 4)
			return start;
		
		int length = start.length();
		int bonusSpace = length / 4;
		int charCount = 0;
		int iteration = 1;
		char [] tmp = new char[length + bonusSpace];
		
		for (int i = length -1; i >= 0; i --) {
			char c = start.charAt(i);
			tmp[charCount ++] = c;
			
			if(iteration % 4 == 0) {
				tmp[charCount ++] = ' ';
			}
			
			iteration ++;
		}
		if(length % 4 == 0)
			charCount --;
		
		String toRet = new String(tmp, 0, charCount); 
		toRet = StringUtils.reverse(toRet);
		
		return toRet;
	}
}
