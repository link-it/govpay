package it.govpay.stampe.pdf.rt;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
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

	public static String getPdfRicevutaPagamento(String pathLoghi,  CtRicevutaTelematica rt,String  causale,OutputStream os ,Logger log) throws Exception {
		String msg = null;
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(os);
		JasperReportBuilder report = report();

		List<ComponentBuilder<?, ?>> cl = new ArrayList<ComponentBuilder<?,?>>();

		List<String> errList = new ArrayList<String>();
		cl.add(TemplateRt.createTitleComponent(pathLoghi,rt,errList,log));
		
		if(errList.size() > 0)
			msg = errList.get(0);

		cl.add(createRicevutaPagamentoList(Costanti.LABEL_RICEVUTA_PAGAMENTO, rt,causale)); 

		cl.add(cmp.verticalGap(20));
		

		ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];

		//configure report
		report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
		.setTemplate(TemplateBase.reportTemplate)
		.title(cl.toArray(ca))
		.toPdf(pdfExporter); 

		return msg;
	}

	private static ComponentBuilder<?, ?> createRicevutaPagamentoList(String label , CtRicevutaTelematica rt, String causale) {
		HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10));
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		TemplateBase.creaElementoListaNomeValore(list, Costanti.LABEL_CAUSALE, causale);
		TemplateBase.creaElementoListaNomeValore(list, Costanti.LABEL_IMPORTO_PAGATO, (datiPagamento.getImportoTotalePagato().doubleValue()+ "‎€"));
		TemplateBase.creaElementoListaNomeValore(list, Costanti.LABEL_CF_PIVA_DEBITORE, rt.getSoggettoPagatore().getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		TemplateBase.creaElementoListaNomeValore(list, Costanti.LABEL_IUV, datiPagamento.getIdentificativoUnivocoVersamento());

		List<CtDatiSingoloPagamentoRT> datiSingoloPagamento = datiPagamento.getDatiSingoloPagamento();
		if(datiSingoloPagamento != null && datiSingoloPagamento.size() > 0) {
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiSingoloPagamento.get(0);

			Date dataEsitoSingoloPagamento = ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento();
			TemplateBase.creaElementoListaNomeValore(list, Costanti.LABEL_DATA_PAGAMENTO, TemplateBase.sdf_ddMMyyyy.format(dataEsitoSingoloPagamento));
			TemplateBase.creaElementoListaNomeValore(list, Costanti.LABEL_IUR, ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
		}
		String labelIstitutoAttestante = rt.getIstitutoAttestante().getDenominazioneAttestante() + " (C.F. " + rt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco()+ ")";
		TemplateBase.creaElementoListaNomeValore(list, Costanti.LABEL_ISTITUTO_ATTESTANTE, labelIstitutoAttestante );

		return cmp.verticalList(
				cmp.text(label).setStyle(TemplateBase.bold16CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
				cmp.verticalGap(10),
				list).setStyle(TemplateBase.centeredStyle);
	}
}
