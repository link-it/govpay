package it.govpay.core.business;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.AvvisiPagamentoBD;
import it.govpay.core.business.model.InserisciAvvisoDTO;
import it.govpay.core.business.model.InserisciAvvisoDTOResponse;
import it.govpay.core.business.model.LeggiAvvisoDTO;
import it.govpay.core.business.model.LeggiAvvisoDTOResponse;
import it.govpay.core.business.model.ListaAvvisiDTO;
import it.govpay.core.business.model.ListaAvvisiDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Intermediario;
import it.govpay.model.avvisi.AvvisoPagamentoInput;

public class AvvisoPagamento extends BasicBD {

	
	private SimpleDateFormat sdfDataScadenza = new SimpleDateFormat("dd/MM/yyyy");
	private static Logger log = LogManager.getLogger();

	public AvvisoPagamento(BasicBD basicBD) {
		super(basicBD);
	}

	public InserisciAvvisoDTOResponse inserisciAvviso(InserisciAvvisoDTO inserisciAvviso) {
		InserisciAvvisoDTOResponse response = new InserisciAvvisoDTOResponse();
		GpContext ctx = GpThreadLocal.get();
		return response;
	}
	
	
	public ListaAvvisiDTOResponse getAvvisi(ListaAvvisiDTO listaAvvisi) {
		ListaAvvisiDTOResponse response = new ListaAvvisiDTOResponse();
		GpContext ctx = GpThreadLocal.get();
		ctx.log("avvisiPagamento.listaAvvisi");
		
		AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);
//		avvisi
		
		
		return response;
	}
	
	
	public LeggiAvvisoDTOResponse getAvviso(LeggiAvvisoDTO leggiAvviso) {
		LeggiAvvisoDTOResponse response = new LeggiAvvisoDTOResponse();
		GpContext ctx = GpThreadLocal.get();
		return response;
	}
		
	
	public PrintAvvisoDTOResponse printAvviso(PrintAvvisoDTO printAvviso) {
		PrintAvvisoDTOResponse response = new PrintAvvisoDTOResponse();
		GpContext ctx = GpThreadLocal.get();
		return response;
	}

	public AvvisoPagamentoInput fromVersamento(it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento, it.govpay.bd.model.Versamento versamento) throws ServiceException, UnsupportedEncodingException {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		
		Dominio dominio = versamento.getUo(this).getDominio(this);
		String codDominio = dominio.getCodDominio();
		Anagrafica anagraficaDebitore = versamento.getAnagraficaDebitore();
		
		String indirizzoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getIndirizzo()) ? anagraficaDebitore.getIndirizzo() : "";
		String civicoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCivico()) ? anagraficaDebitore.getCivico() : "";
		String capDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCap()) ? anagraficaDebitore.getCap() : "";
		String localitaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getLocalita()) ? anagraficaDebitore.getLocalita() : "";
		String provinciaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getProvincia()) ? (" (" +anagraficaDebitore.getProvincia() +")" ) : "";
		String indirizzoCivicoDebitore = indirizzoDebitore + " " + civicoDebitore;
		String capCittaDebitore = capDebitore + " " + localitaDebitore + provinciaDebitore;
		
		input.setEnteDenominazione(dominio.getRagioneSociale());
		input.setEnteArea("Area di sviluppo per le politiche agricole e forestali");
		input.setEnteIdentificativo(codDominio);
		input.setEnteCbill("AAAAAAA");
		input.setEnteUrl("www.comune.sanciprianopicentino.sa.it/");
		input.setEntePeo("info@comune.sancipriano.sa.it");
		input.setEntePec("protocollo@pec.comune.sanciprianopicentino.sa.it");
		
		Intermediario intermediario = dominio.getStazione(this).getIntermediario(this);
		input.setEntePartner(intermediario.getDenominazione());
		
		input.setIntestatarioDenominazione(anagraficaDebitore.getRagioneSociale());
		input.setIntestatarioIdentificativo(anagraficaDebitore.getCodUnivoco());
		input.setIntestatarioIndirizzo1(indirizzoCivicoDebitore);
		input.setIntestatarioIndirizzo2(capCittaDebitore);
		
		it.govpay.core.business.model.Iuv iuvGenerato = 
				IuvUtils.toIuv(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), 
						versamento.getIuv(this), versamento.getImportoTotale());
		
		input.setAvvisoCausale(versamento.getCausaleVersamento().getSimple());
		
		input.setAvvisoImporto(versamento.getImportoTotale().doubleValue());
		if(versamento.getDataScadenza() != null)
			input.setAvvisoScadenza(this.sdfDataScadenza.format(versamento.getDataScadenza()));
		input.setAvvisoNumero(iuvGenerato.getNumeroAvviso());
		input.setAvvisoIuv(iuvGenerato.getIuv());
		input.setAvvisoBarcode(new String(iuvGenerato.getBarCode()));
		input.setAvvisoQrcode(new String(iuvGenerato.getQrCode()));
		
		return input;
	}
}
