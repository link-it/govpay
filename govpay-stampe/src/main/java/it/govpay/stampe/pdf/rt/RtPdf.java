package it.govpay.stampe.pdf.rt;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

public class RtPdf {

	public static byte[] getPdfRicevutaPagamento(BasicBD bd, String pathLoghi,  CtRicevutaTelematica rt,String  causale) throws Exception { //, String idDominio, RefPagamento refPagamento , BigDecimal importoTotale, Anagrafica debitore, Anagrafica creditore) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(baos);
		JasperReportBuilder report = report();

		List<ComponentBuilder<?, ?>> cl = new ArrayList<ComponentBuilder<?,?>>();

		cl.add(TemplateRt.createTitleComponent(bd, pathLoghi,rt));

		cl.add(createRicevutaPagamentoList(Costanti.LABEL_RICEVUTA_PAGAMENTO, rt,causale)); 

		cl.add(cmp.verticalGap(20));
		

		ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];

		//configure report
		report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
		.setTemplate(TemplateBase.reportTemplate)
		.title(cl.toArray(ca))
		.toPdf(pdfExporter); 

		return baos.toByteArray();
	}

	private static ComponentBuilder<?, ?> createRicevutaPagamentoList(String label , CtRicevutaTelematica rt, String causale) {
		HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10));
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		addCustomerAttribute(list, Costanti.LABEL_CAUSALE, causale);
		addCustomerAttribute(list, Costanti.LABEL_IMPORTO, (datiPagamento.getImportoTotalePagato().doubleValue()+ "‎€"));
		addCustomerAttribute(list, Costanti.LABEL_CF_PIVA_DEBITORE, rt.getSoggettoPagatore().getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		addCustomerAttribute(list, Costanti.LABEL_IUV, datiPagamento.getIdentificativoUnivocoVersamento());

		List<CtDatiSingoloPagamentoRT> datiSingoloPagamento = datiPagamento.getDatiSingoloPagamento();
		if(datiSingoloPagamento != null && datiSingoloPagamento.size() > 0) {
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiSingoloPagamento.get(0);

			Date dataEsitoSingoloPagamento = ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento();
			addCustomerAttribute(list, Costanti.LABEL_DATA_PAGAMENTO, TemplateBase.sdf_ddMMyyyy.format(dataEsitoSingoloPagamento));
			addCustomerAttribute(list, Costanti.LABEL_IUR, ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
		}
		String labelIstitutoAttestante = rt.getIstitutoAttestante().getDenominazioneAttestante() + " (C.F. " + rt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco()+ ")";
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
