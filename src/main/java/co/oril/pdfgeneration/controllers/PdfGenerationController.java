package co.oril.pdfgeneration.controllers;

import co.oril.pdfgeneration.utils.PdfGeneration;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class PdfGenerationController {

	private final PdfGeneration pdfGeneration;

	@GetMapping(value = "/generate/pdf-template")
	public ResponseEntity<Resource> generatePdfUsingTemplate() {
		byte[] content = pdfGeneration.generatePdfUsingTemplate();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, MediaType.APPLICATION_OCTET_STREAM_VALUE)
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
				.body(new ByteArrayResource(content));
	}

	@GetMapping(value = "/generate/html-template")
	public ResponseEntity<Resource> generatePdfUsingHtml() {
		byte[] content = pdfGeneration.generatePdfUsingHtml();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, MediaType.APPLICATION_OCTET_STREAM_VALUE)
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
				.body(new ByteArrayResource(content));
	}

	@GetMapping(value = "/generate/pdf")
	public ResponseEntity<Resource> generatePdf() {
		byte[] content = pdfGeneration.generatePdf();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, MediaType.APPLICATION_OCTET_STREAM_VALUE)
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
				.body(new ByteArrayResource(content));
	}

}
