package co.oril.pdfgeneration.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

@Component
@AllArgsConstructor
public class PdfGeneration {

	private final TemplateEngine templateEngine;

	public byte[] generatePdfUsingTemplate() {
		try {
			String rootPath = System.getProperty("user.dir");
			File originalPdf = new File("pdf_example.pdf");
			FileUtils.copyInputStreamToFile(Objects.requireNonNull(PDDocument.class.getClassLoader().getResourceAsStream("templates/pdf-example.pdf")), originalPdf);
			File targetPdf = new File(StringUtils.join(rootPath, "/", "pdf_example_" + new Date().getTime() + ".pdf"));
			PDDocument pdDocument = PDDocument.load(originalPdf);
			setPdfExampleFields(pdDocument);
			pdDocument.save(targetPdf);
			pdDocument.close();
			FileUtils.forceDelete(originalPdf);
			return FileUtils.readFileToByteArray(targetPdf);
		} catch (Exception e) {
			return null;
		}
	}

	public byte[] generatePdfUsingHtml() {
		try {
			Context context = new Context();
			context.setVariable("firstName", "Frank");
			context.setVariable("lastName", "Moreno");
			context.setVariable("address", "Green EWREWRst. 45");
			context.setVariable("city", "New York");
			context.setVariable("state", "NY");
			context.setVariable("zipCode", "10010");
			String content = templateEngine.process("hmtl-example", context);
			File file = generatePdfFileFromHtmlContent(content);
			return FileUtils.readFileToByteArray(file);
		} catch (Exception e) {
			return null;
		}
	}

	public byte[] generatePdf() {
		String rootPath = System.getProperty("user.dir");
		File file = new File(StringUtils.join(rootPath, "/", "example_" + new Date().getTime() + ".pdf"));
		try (OutputStream out = new FileOutputStream(file)) {
			Document document = new Document(new RectangleReadOnly(1050, 1200), 80, 80, 50, 50);
			PdfWriter.getInstance(document, out);
			document.open();

			getInfoSection(document);
			getTableSection(document);

			document.close();
			return FileUtils.readFileToByteArray(file);
		} catch (Exception e) {
			return null;
		}
	}

	public void getTableSection(Document document) throws DocumentException {
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
		Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
		Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 14);

		BaseColor tableHeaderBgColor = new BaseColor(250, 250, 250);
		BaseColor tableBorderColor = new BaseColor(222, 222, 222);

		Paragraph stateParagraph = new Paragraph("State: NY", headerFont);
		stateParagraph.setSpacingBefore(40);
		stateParagraph.setAlignment(Element.ALIGN_CENTER);
		document.add(stateParagraph);


		Paragraph tierNameParagraph = new Paragraph("Test table", subHeaderFont);
		tierNameParagraph.setAlignment(Element.ALIGN_LEFT);
		document.add(tierNameParagraph);

		PdfPTable table = new PdfPTable(13);
		table.setWidthPercentage(100);
		table.setSpacingBefore(5);

		table.addCell(createNewTableHeader("Column 1", regularFont, 2, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 2", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 3", regularFont, 2, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 4", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 5", regularFont, 2, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 6", regularFont, 2, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 7", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 8", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));
		table.addCell(createNewTableHeader("Column 9", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor, tableHeaderBgColor));

		table.addCell(createNewTableBorderedCell("Value 1", regularFont, 2, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 2", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 3", regularFont, 2, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 4", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 5", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 6", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 7", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 8", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 9", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 10", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));
		table.addCell(createNewTableBorderedCell("Value 11", regularFont, 1, Element.ALIGN_CENTER, tableBorderColor));

		document.add(table);
	}

	public void getInfoSection(Document document) throws DocumentException {
		Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
		Font captionFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
		Font nameFont = FontFactory.getFont(FontFactory.HELVETICA, 26);

		PdfPTable infoSection = new PdfPTable(12);
		infoSection.setWidthPercentage(100);

		PdfPCell middleCell = createNewCell("", regularFont, 8, Element.ALIGN_LEFT);

		Paragraph fullName = new Paragraph(1, "Frank Moreno", nameFont);
		fullName.setMultipliedLeading(1);
		fullName.setSpacingAfter(10);
		middleCell.addElement(fullName);

		middleCell.addElement(new Paragraph("Address: " + "Green EWREWRst. 45", regularFont));
		middleCell.addElement(new Paragraph("Phone: " + "1233211232", regularFont));

		infoSection.addCell(middleCell);

		PdfPCell rightCell = createNewCell("", regularFont, 4, Element.ALIGN_RIGHT);

		Image logo = null;
		try {
			logo = Image.getInstance("https://oril.co/wp-content/themes/oril/assets/img/creature.png");
			logo.scaleAbsolute(110, 17);
			logo.setAlignment(Element.ALIGN_RIGHT);
		} catch (IOException ignored) {
		}

		PdfPTable poweredByTable = new PdfPTable(4);
		poweredByTable.setWidthPercentage(100);
		PdfPCell poweredByCaptionCell = createNewCell("Logo", captionFont, 2, Element.ALIGN_RIGHT);
		poweredByTable.addCell(poweredByCaptionCell);

		PdfPCell poweredByLogoCell = createNewCell("", regularFont, 2, Element.ALIGN_RIGHT);
		poweredByLogoCell.addElement(logo);
		poweredByTable.addCell(poweredByLogoCell);

		rightCell.addElement(poweredByTable);

		infoSection.addCell(rightCell);

		document.add(infoSection);
	}

	private PdfPCell createNewTableBorderedCell(String text, Font font, int colspan, int alignment, BaseColor borderColor) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBorderWidth(1);
		cell.setBorderColor(borderColor);
		cell.setColspan(colspan);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setMinimumHeight(36);
		return cell;
	}

	private PdfPCell createNewCell(String text, Font font, int colspan, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setColspan(colspan);
		cell.setPaddingBottom(5);
		cell.setPaddingLeft(5);
		cell.setHorizontalAlignment(alignment);
		cell.setBorderColor(BaseColor.WHITE);
		return cell;
	}

	private PdfPCell createNewTableHeader(String text, Font font, int colspan, int alignment, BaseColor borderColor, BaseColor bgColor) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBorderWidth(1);
		cell.setBorderColor(borderColor);
		cell.setColspan(colspan);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setMinimumHeight(42);
		cell.setBackgroundColor(bgColor);
		return cell;
	}

	private File generatePdfFileFromHtmlContent(String content) {
		String rootPath = System.getProperty("user.dir");
		File file = new File(StringUtils.join(rootPath, "/", String.format("%s.pdf", new Date().getTime())));
		try (OutputStream outputStream = new FileOutputStream(file)) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(content);
			renderer.layout();
			renderer.createPDF(outputStream);
			renderer.setScaleToFit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	private void setPdfExampleFields(PDDocument pdDocument) {
		try {
			PDDocumentCatalog docCatalog = pdDocument.getDocumentCatalog();
			PDAcroForm acroForm = docCatalog.getAcroForm();

			setFieldValue(acroForm, "first-name", "Frank");
			setFieldValue(acroForm, "last-name", "Moreno");
			setFieldValue(acroForm, "address", "Green EWREWRst. 45");
			setFieldValue(acroForm, "zip", "10010");
			setFieldValue(acroForm, "country", "USA");
			setFieldValue(acroForm, "driver-license", "YES");
		} catch (Exception ignored) {
		}
	}

	private void setFieldValue(PDAcroForm acroForm, String name, String value) {
		try {
			PDTextField field = (PDTextField) acroForm.getField(name);
			field.setDefaultAppearance(acroForm.getDefaultAppearance());
			field.setValue(value);
		} catch (IOException ignored) {
		}
	}

}
