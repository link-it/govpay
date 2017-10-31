package it.govpay.stampe.pdf.avvisoPagamento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import it.govpay.model.avvisi.AvvisoPagamentoInput;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class AvvisoPagamentoDatasource implements JRDataSource{

	private List<Map<String, Object>> parameters = new ArrayList<Map<String, Object>>();
	private int index = -1;
//	private Logger log = null;

	public AvvisoPagamentoDatasource(List<AvvisoPagamentoInput> dataSource, Logger log) {
		for (AvvisoPagamentoInput avvisoPagamentoInput : dataSource) {
			this.parameters.add(this.toMap(avvisoPagamentoInput)); 	
		}
		
		
//		this.log = log;
	}

	@Override
	public boolean next() throws JRException {
		this.index++;
		return this.index < this.parameters.size();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Map<String, Object> map = this.parameters.get(index);
		System.out.println("Aggiungo field ["+jrField.getName()+"] con valore ["+map.get(jrField.getName())+"]. ");
//		this.log.debug("Aggiungo field ["+jrField.getName()+"] con valore ["+map.get(jrField.getName())+"]. ");
		return map.get(jrField.getName());
	}
	
	public Map<String, Object> toMap(AvvisoPagamentoInput input){
		Map<String, Object> map = new HashMap<String, Object>();

		/*
     	<ente_denominazione>Comune di San Valentino in Abruzzo Citeriore</ente_denominazione>
        <ente_area>Area di sviluppo per le politiche agricole e forestali</ente_area>
        <ente_identificativo>83000390019</ente_identificativo>
        <ente_cbill>AAAAAAA</ente_cbill>
        <ente_url>www.comune.sanciprianopicentino.sa.it/</ente_url>
        <ente_peo>info@comune.sancipriano.sa.it</ente_peo>
        <ente_pec>protocollo@pec.comune.sanciprianopicentino.sa.it</ente_pec>
        <ente_partner>Link.it Srl</ente_partner>
        <intestatario_denominazione>Lorenzo Nardi</intestatario_denominazione>
        <intestatario_identificativo>NRDLNA80P19D612M</intestatario_identificativo>
        <intestatario_indirizzo_1>Via di Corniola 119A</intestatario_indirizzo_1>
        <intestatario_indirizzo_2>50053 Empoli (FI)</intestatario_indirizzo_2>
        <avviso_causale>Pagamento diritti di segreteria per il rilascio in duplice copia della documentazione richiesta.</avviso_causale>
        <avviso_importo>9999999.99</avviso_importo>
        <avviso_scadenza>31/12/2020</avviso_scadenza>
        <avviso_numero>399000012345678900</avviso_numero>
        <avviso_iuv>99000012345678900</avviso_iuv>
        <avviso_barcode>415808888880094580203990000123456789003902222250</avviso_barcode>
        <avviso_qrcode>PAGOPA|002|399000012345678900|83000390019|222250</avviso_qrcode>

		 * */

		if(input.getEnteLogo() != null)
			map.put("ente_logo", input.getEnteLogo());
		if(input.getAgidLogo() != null)
			map.put("agid_logo", input.getAgidLogo());
		if(input.getPagopaLogo() != null)
			map.put("pagopa_logo", input.getPagopaLogo());
		if(input.getPagopaLogo() != null)
			map.put("pagopa90_logo", input.getPagopa90Logo());
		if(input.getAppLogo() != null)
			map.put("app_logo", input.getAppLogo());
		if(input.getPlaceLogo() != null)
			map.put("place_logo", input.getPlaceLogo());
		if(input.getImportoLogo() != null)
			map.put("importo_logo", input.getImportoLogo());
		if(input.getScadenzaLogo() != null)
			map.put("scadenza_logo", input.getScadenzaLogo());

		if(input.getEnteDenominazione() != null)
			map.put("ente_denominazione", input.getEnteDenominazione());
		if(input.getEnteArea() != null)
			map.put("ente_area", input.getEnteArea());
		if(input.getEnteIdentificativo() != null)
			map.put("ente_identificativo", input.getEnteIdentificativo());
		if(input.getEnteCbill() != null)
			map.put("ente_cbill", input.getEnteCbill());
		if(input.getEnteUrl() != null)
			map.put("ente_url", input.getEnteUrl());
		if(input.getEntePeo() != null)
			map.put("ente_peo", input.getEntePeo());
		if(input.getEntePec() != null)
			map.put("ente_pec", input.getEntePec());
		if(input.getEntePartner() != null)
			map.put("ente_partner", input.getEntePartner());
		if(input.getIntestatarioDenominazione() != null)
			map.put("intestatario_denominazione", input.getIntestatarioDenominazione());
		if(input.getIntestatarioIdentificativo() != null)
			map.put("intestatario_identificativo", input.getIntestatarioIdentificativo());
		if(input.getIntestatarioIndirizzo1() != null)
			map.put("intestatario_indirizzo_1", input.getIntestatarioIndirizzo1());
		if(input.getIntestatarioIndirizzo2() != null)
			map.put("intestatario_indirizzo_2", input.getIntestatarioIndirizzo2());
		if(input.getAvvisoCausale() != null)
			map.put("avviso_causale", input.getAvvisoCausale());
		//if(input.getAvvisoImporto() != null)
		map.put("avviso_importo", input.getAvvisoImporto());
		if(input.getAvvisoScadenza() != null)
			map.put("avviso_scadenza", input.getAvvisoScadenza());
		if(input.getAvvisoNumero() != null)
			map.put("avviso_numero", input.getAvvisoNumero());
		if(input.getAvvisoIuv() != null)
			map.put("avviso_iuv", input.getAvvisoIuv());
		if(input.getAvvisoBarcode() != null)
			map.put("avviso_barcode", input.getAvvisoBarcode());
		if(input.getAvvisoQrcode() != null)
			map.put("avviso_qrcode", input.getAvvisoQrcode());

		return map;
	}
}
