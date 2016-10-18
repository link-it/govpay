package it.govpay.stampe.pdf.er;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiSingoloEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtEsitoRevoca;
import it.govpay.model.Anagrafica;
import it.govpay.model.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

public class ErPdf {

	public static String getPdfEsitoRevoca(String pathLoghi, CtEsitoRevoca er,Dominio dominio, Anagrafica anagraficaDominio,String  causale,OutputStream os ,Logger log) throws Exception {
		String msg = null;
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(os);
		JasperReportBuilder report = report();

		List<ComponentBuilder<?, ?>> cl = new ArrayList<ComponentBuilder<?,?>>();

		List<String> errList = new ArrayList<String>();
		cl.add(TemplateEr.createTitleComponent(pathLoghi,er,dominio,anagraficaDominio,errList,log));
		
		if(errList.size() > 0)
			msg = errList.get(0);

		cl.add(createRicevutaPagamentoList(Costanti.LABEL_ESITO_REVOCA, er,causale)); 

		cl.add(cmp.verticalGap(20));
		

		ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];

		//configure report
		report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
		.setTemplate(TemplateBase.reportTemplate)
		.title(cl.toArray(ca))
		.toPdf(pdfExporter); 

		return msg;
	}

	private static ComponentBuilder<?, ?> createRicevutaPagamentoList(String label , CtEsitoRevoca er, String causale) {
		HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10));
		CtDatiEsitoRevoca datiPagamento = er.getDatiRevoca(); 
		addCustomerAttribute(list, Costanti.LABEL_CAUSALE, causale);
		addCustomerAttribute(list, Costanti.LABEL_IMPORTO_REVOCA, (datiPagamento.getImportoTotaleRevocato().doubleValue()+ "‎€"));
		addCustomerAttribute(list, Costanti.LABEL_CF_PIVA_DEBITORE, er.getSoggettoPagatore().getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		addCustomerAttribute(list, Costanti.LABEL_IUV, datiPagamento.getIdentificativoUnivocoVersamento());

		List<CtDatiSingoloEsitoRevoca> datiSingoloPagamento = datiPagamento.getDatiSingolaRevoca();
		if(datiSingoloPagamento != null && datiSingoloPagamento.size() > 0) {
			CtDatiSingoloEsitoRevoca ctDatiSingoloPagamentoRT = datiSingoloPagamento.get(0); 

			Date dataEsitoSingoloPagamento = er.getDataOraMessaggioEsito();
			addCustomerAttribute(list, Costanti.LABEL_DATA_REVOCA, TemplateBase.sdf_ddMMyyyy.format(dataEsitoSingoloPagamento));
			addCustomerAttribute(list, Costanti.LABEL_IUR, ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
		}
		String labelIstitutoAttestante = er.getIstitutoAttestante().getDenominazioneMittente() + " (C.F. " + er.getIstitutoAttestante().getIdentificativoUnivocoMittente().getCodiceIdentificativoUnivoco()+ ")";
		addCustomerAttribute(list, Costanti.LABEL_ISTITUTO_ATTESTANTE, labelIstitutoAttestante );

		return cmp.verticalList(
				cmp.text(label).setStyle(TemplateBase.bold16CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
				cmp.verticalGap(10),
				list).setStyle(TemplateBase.centeredStyle);
	}

	private static void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
		if (value != null) {
			list.add(cmp.text(label + ":").setFixedColumns(20).setStyle(TemplateBase.boldStyle), cmp.text(value)).newRow();
		}
	}
	
//	public static void init() throws Exception{
//		JasperReportBuilder report = report();
//		List<ComponentBuilder<?, ?>> cl = new ArrayList<ComponentBuilder<?,?>>();
//		cl.add(cmp.verticalGap(20));
//		ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];
//		report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
//		.setTemplate(TemplateRt.reportTemplate)
//		.title(cl.toArray(ca));
//	}
}
