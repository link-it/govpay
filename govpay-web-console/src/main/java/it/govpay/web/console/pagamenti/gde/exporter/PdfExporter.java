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
package it.govpay.web.console.pagamenti.gde.exporter;

import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.web.console.pagamenti.bean.EventoBean;
import it.govpay.web.console.pagamenti.iservice.IEventiService;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.openspcoop2.utils.UtilsException;

import com.lowagie.text.Anchor;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfExporter {

	private static Font catFont = new Font(Font. TIMES_ROMAN, 18,
			Font.BOLD);
	//	private static Font redFont = new Font(Font. TIMES_ROMAN, 12,
	//			Font.NORMAL, Color.RED);
	private static Font subFont = new Font(Font. TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font smallBold = new Font(Font. TIMES_ROMAN, 12,
			Font.BOLD);

	public static void exportAsPdf(List<EventoBean> eventi, ByteArrayOutputStream baos, IEventiService eventiService) throws DocumentException, UtilsException{


		Document document=new Document();

		PdfWriter.getInstance(document , baos);

		document.open(); 

		addMetaData(document);
		addTitlePage(document);

		// Start a new page
		document.newPage();

		addContent(document, eventi,eventiService);

		document.close(); 


	}


	private static void addContent(Document document, List<EventoBean> eventi, IEventiService eventiService) throws UtilsException, DocumentException {
		Anchor anchor = new Anchor("Eventi Selezionati", catFont);
		anchor.setName("Eventi Selezionati");

		// Second parameter is the number of the chapter
		Chapter catPart = new Chapter(new Paragraph(anchor), 1);

		for (EventoBean eventoBean : eventi) {


			Evento evento = eventoBean.getDTO();
			Paragraph subPara = new Paragraph("Evento Id["+evento.getId()+"]", subFont);

			Section subCatPart = catPart.addSection(subPara);

			addEmptyLine(subCatPart, 1);

			createEventoTable(subCatPart, evento);

			addEmptyLine(subCatPart, 1);

			Infospcoop infospcoop = eventoBean.getInfospcoop() != null ? eventoBean.getInfospcoop().getDTO() : null;
			if(infospcoop!= null){
				//			if(evento.getCategoria().equals(Categoria.INTERFACCIA) && evento.getIdEgov()!= null){

				//				Infospcoop infospcoop = eventiService.getInfospcoopByIdEgov(evento.getIdEgov());

				Paragraph infoParg = new Paragraph("Infospcoop", subFont);

				subCatPart.addSection(infoParg);

				addEmptyLine(subCatPart, 1);

				createInfospcoopTable(subCatPart, infospcoop);

				//subCatPart.add(new Paragraph(infospcoop.toXml()));
			}

			addEmptyLine(subCatPart, 1);

			catPart.newPage();
		}

		// now add all this to the document
		document.add(catPart);

	}


	private static void createInfospcoopTable(Section subCatPart, Infospcoop infospcoop)
			throws BadElementException {

		PdfPTable table = new PdfPTable(2);

		PdfPCell c1 = new PdfPCell(new Phrase("Infospcoop"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setColspan(2);
		table.addCell(c1);
		table.setHeaderRows(1);

		//riga 1
		table.addCell(new Phrase("IdEgov"));
		table.addCell(new Phrase(infospcoop.getIdEgov()));
		// riga 2
		table.addCell(new Phrase("Soggetto Erogatore"));
		table.addCell(new Phrase(infospcoop.getTipoSoggettoErogatore() + "/" + infospcoop.getSoggettoErogatore()));
		// riga 3
		table.addCell(new Phrase("Soggetto Fruitore"));
		table.addCell(new Phrase(infospcoop.getTipoSoggettoFruitore() + "/" + infospcoop.getSoggettoFruitore()));
		// riga 4
		table.addCell(new Phrase("Servizio"));
		table.addCell(new Phrase(infospcoop.getTipoServizio() + "/" + infospcoop.getServizio()));
		// riga 5
		table.addCell(new Phrase("Azione"));
		table.addCell(new Phrase(infospcoop.getAzione()));

		subCatPart.add(table);

	}

	private static void createEventoTable(Section subCatPart, Evento evento)
			throws BadElementException {

		PdfPTable table = new PdfPTable(2);

		PdfPCell c1 = new PdfPCell(new Phrase("Evento Id["+evento.getId()+"]"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setColspan(2);
		table.addCell(c1);
		table.setHeaderRows(1);

		//riga 1
		if(evento.getData()  != null){
			table.addCell(new Phrase("Data registrazione"));
			table.addCell(new Phrase(""+evento.getData()));
		}
		//riga 1
		if(evento.getDominio()  != null){
			table.addCell(new Phrase("Id Dominio"));
			table.addCell(new Phrase(evento.getDominio()));
		}
		//riga 1
		if(evento.getIuv()  != null){
			table.addCell(new Phrase("IUV"));
			table.addCell(new Phrase(evento.getIuv()));
		}
		//riga 1
		if(evento.getCcp()  != null){
			table.addCell(new Phrase("CCP"));
			table.addCell(new Phrase(evento.getCcp()));
		}
		//riga 1
		if(evento.getPsp()  != null){
			table.addCell(new Phrase("Id PSP"));
			table.addCell(new Phrase(evento.getPsp()));
		}
		//riga 1
		if(evento.getTipoVersamento()  != null){
			table.addCell(new Phrase("Tipo Versamento"));
			table.addCell(new Phrase(evento.getTipoVersamento()));
		}
		//riga 1
		if(evento.getComponente()  != null){
			table.addCell(new Phrase("Componente"));
			table.addCell(new Phrase(evento.getComponente().toString()));
		}
		//riga 1
		if(evento.getCategoria()  != null){
			table.addCell(new Phrase("Categoria Evento"));
			table.addCell(new Phrase(evento.getCategoria().toString()));
		}
		//riga 1
		if(evento.getTipo() != null){
			table.addCell(new Phrase("Tipo Evento"));
			table.addCell(new Phrase(evento.getTipo()));
		}
		//riga 1
		if(evento.getSottoTipo()  != null){
			table.addCell(new Phrase("Sottotipo Evento"));
			table.addCell(new Phrase(evento.getSottoTipo().toString()));
		}
		//riga 1
		if(evento.getFruitore()  != null){
			table.addCell(new Phrase("Id Fruitore"));
			table.addCell(new Phrase(evento.getFruitore()));
		}
		//riga 1
		if(evento.getErogatore()  != null){
			table.addCell(new Phrase("Id Erogatore"));
			table.addCell(new Phrase(evento.getErogatore()));
		}
		//riga 1
		if(evento.getStazioneIntermediarioPA() != null){
			table.addCell(new Phrase("Id Stazione Intermediario PA"));
			table.addCell(new Phrase(evento.getStazioneIntermediarioPA()));
		}
		//riga 1
		if(evento.getCanalePagamento()  != null){
			table.addCell(new Phrase("Canale Pagamento"));
			table.addCell(new Phrase(evento.getCanalePagamento()));
		}
		//riga 1
		if(evento.getParametri()  != null){
			table.addCell(new Phrase("Parametri Specifici Interfaccia"));
			table.addCell(new Phrase(evento.getParametri()));
		}

		if(evento.getEsito() != null){
			table.addCell(new Phrase("Esito"));
			table.addCell(new Phrase(evento.getEsito()));
		}

		subCatPart.add(table);

	}


	private static void addMetaData(Document document) {
		document.addTitle("Report Eventi");
		document.addSubject("Report Eventi selezionati nell'interfaccia del ProxyNDP");
		document.addKeywords("Report, Eventi, ProxyNdP");
		document.addAuthor("ProxyNdP");
		document.addCreator("ProxyNdP");
	}

	private static void addTitlePage(Document document)
			throws DocumentException {
		Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("Eventi selezionati", catFont));

		addEmptyLine(preface, 1);
		// Will create: Report generated by: _name, _date
		preface.add(new Paragraph("Report generated by: ProxyNdP, " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				smallBold));
		addEmptyLine(preface, 3);
		preface.add(new Paragraph("Il report contiene gli eventi selezionati per l'export nella console grafica del ProxyNdP.",
				smallBold));

		document.add(preface);
	}


	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private static void addEmptyLine(Section section, int number) {
		for (int i = 0; i < number; i++) {
			section.add(new Paragraph(" "));
		}
	}

}
