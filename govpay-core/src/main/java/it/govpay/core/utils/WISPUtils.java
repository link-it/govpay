package it.govpay.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.model.IbanAccredito;

public class WISPUtils {

	public class HiddenParameter  {
		private String name = null;
		private Object value = null;
		private String type = null;

		public HiddenParameter(String name, Object value) {
			this.name = name;
			this.value = value;
			this.type = "hidden";
		}

		public String toHtml() {
			StringBuilder sb = new StringBuilder();

			sb.append("<input type=\"").append(this.type).append("\" ")
			.append("name=\"").append(this.name).append("\" ")
			.append("value=\"").append(this.value).append("\" ")
			.append("/>"); 

			return sb.toString();
		}
	}

	public static HiddenParameter createHiddenParameter(String name, Object value) {
		return new WISPUtils().new HiddenParameter(name, value);
	}

	public static final String KEY_PARAMETRO_ID_DOMINIO = "idDominio";
	public static final String KEY_PARAMETRO_ENTE_CREDITORE = "enteCreditore";
	public static final String KEY_PARAMETRO_KEYPA = "keyPA";
	public static final String KEY_PARAMETRO_URLRETURN = "urlReturn";
	public static final String KEY_PARAMETRO_URLBACK = "urlBack";
	public static final String KEY_PARAMETRO_PRIMITIVA = "primitiva";
	public static final String KEY_PARAMETRO_NUMPAGAMENTI_RPT = "numPagamentiRPT";
	public static final String KEY_PARAMETRO_STORNO_PAGAMENTO = "stornoPagamento";
	public static final String KEY_PARAMETRO_BOLLO_DIGITALE = "bolloDigitale";
	public static final String KEY_PARAMETRO_ID_PSP = "idPsp";
	public static final String KEY_PARAMETRO_TIPO_VERSAMENTO = "tipoVersamento";
	public static final String KEY_PARAMETRO_IMPORTO_TRANSAZIONE = "importoTransazione";
	public static final String KEY_PARAMETRO_VERSIONE_INTERFACCIA_WISP = "versioneInterfacciaWISP";
	public static final String KEY_PARAMETRO_IBAN_ACCREDITO = "ibanAccredito";
	public static final String KEY_PARAMETRO_CONTO_POSTE = "contoPoste";
	public static final String KEY_PARAMETRO_PAGAMENTIMODELLO2 = "pagamentiModello2";
	public static final String KEY_PARAMETRO_CODICE_LINGUA = "codiceLingua";
	public static final String KEY_PARAMETRO_TERZOMODELLO_PAGAMENTO = "terzoModelloPagamento";
	public static final String KEY_PARAMETRI = "@LISTA_INPUT@";

	public static final String TEMPLATE_WISP_HTML = "/wisp.html";

	public static List<HiddenParameter> creaListaParametriPagamentoPortale(PagamentoPortale pagamentoPortale, String urlReturn, String urlBack,String enteCreditore,
			int numeroPagamenti, IbanAccredito ibanAccredito, boolean contoPostale, boolean bollodigitale, double importoTotale, boolean pagamentiModello2, String codiceLingua) {
		List<HiddenParameter> listaParametri = new ArrayList<HiddenParameter>();

		// id Dominio 
		if(pagamentoPortale.getWispIdDominio() != null)
			listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_ID_DOMINIO, pagamentoPortale.getWispIdDominio()));
		// Entecreditore		
		if(enteCreditore != null)
			listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_ENTE_CREDITORE, enteCreditore));
		// keypa
		if(pagamentoPortale.getIdSessione() != null)
			listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_KEYPA, pagamentoPortale.getIdSessione()));
		// urlreturn
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_URLRETURN, urlReturn));
		// urlBack
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_URLBACK, urlBack));
		// primitiva RPT
		int size = pagamentoPortale.getIdVersamento().size();
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_PRIMITIVA, size > 1 ? "nodoInviaCarrelloRPT" : "nodoInviaRPT"));
		// numero pagamenti
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_NUMPAGAMENTI_RPT, numeroPagamenti));
		// storno pagamento
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_STORNO_PAGAMENTO, "SI"));
		// bollo digitale
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_BOLLO_DIGITALE, bollodigitale ? "SI" : "NO"));
		// id_psp		
		if(pagamentoPortale.getIdPsp() != null)
			listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_ID_PSP, pagamentoPortale.getIdPsp()));
		// tipo_versamento
		if(pagamentoPortale.getTipoVersamento() != null)
			listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_TIPO_VERSAMENTO, pagamentoPortale.getTipoVersamento()));
		// importo
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_IMPORTO_TRANSAZIONE, importoTotale));
		// versione interfaccia wisp
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_VERSIONE_INTERFACCIA_WISP, "1.3"));
		// iban accredito
		if(ibanAccredito != null)
			listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_IBAN_ACCREDITO, ibanAccredito.getCodIban()));
		// contoposte		
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_CONTO_POSTE, contoPostale ? "SI" : "NO"));
		// pagamenti modello 2
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_PAGAMENTIMODELLO2, pagamentiModello2  ? "SI" : "NO"));
		// lingua
		if(codiceLingua != null)
			listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_CODICE_LINGUA, codiceLingua));

		// terzo modello pagamento (ignorato per ora)
		listaParametri.add(WISPUtils.createHiddenParameter(KEY_PARAMETRO_TERZOMODELLO_PAGAMENTO, "SI"));

		return listaParametri;
	}

	public static String creaListaParametriPagamentoPortaleAsString(PagamentoPortale pagamentoPortale, String urlReturn, String urlBack,String enteCreditore,
			int numeroPagamenti, IbanAccredito ibanAccredito, boolean contoPostale, boolean bollodigitale, double importoTotale, boolean pagamentiModello2, String codiceLingua) {
		List<HiddenParameter> listaParametri = creaListaParametriPagamentoPortale(pagamentoPortale, urlReturn, urlBack, enteCreditore, numeroPagamenti, ibanAccredito, contoPostale, bollodigitale, importoTotale, pagamentiModello2, codiceLingua);
		StringBuilder sb = new StringBuilder();
		for (HiddenParameter hiddenParameter : listaParametri) {
			sb.append(" ");
			sb.append(hiddenParameter.toHtml());
		}
		return sb.toString();
	}

	public static String getWispHtml(String template, PagamentoPortale pagamentoPortale, String urlReturn, String urlBack, String enteCreditore,
			int numeroPagamenti, IbanAccredito ibanAccredito, boolean contoPostale, boolean bollodigitale, double importoTotale, boolean pagamentiModello2, String codiceLingua) {
		String parametri = creaListaParametriPagamentoPortaleAsString(pagamentoPortale, urlReturn, urlBack, enteCreditore, numeroPagamenti, ibanAccredito, contoPostale, bollodigitale, importoTotale, pagamentiModello2, codiceLingua);
		return template.replace(KEY_PARAMETRI, parametri);
	}

	public static String readTemplate() throws ServiceException{
		// Recupero il property all'interno dell'EAR
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		String template = null;
		try {
			is = WISPUtils.class.getResourceAsStream(TEMPLATE_WISP_HTML);
			baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);
			template = baos.toString();
		}catch(Exception e) {
			throw new ServiceException(e);
		} finally {
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {}
			
			if(baos != null)
				try {
					baos.close();
				} catch (IOException e) {
				}
		}

		return template;

	}
}
