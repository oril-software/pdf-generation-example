## PDF Generation
This repository is an example of how to generate PDF files in different ways
### Prerequisites
* Java 8+
* Maven ^3.6.0
### How to Run
* From IDEA: run the **__PdfGenerationApplication.class__**
* From CLI: run command `mvn spring-boot:run`
### How to Use
There are a few different ways to generate pdf files:
* Using fillable PDF template. Endpoint: `/v1/generate/pdf-template`
* Using HTML with variable data. Endpoint: `/v1/generate/html-template`
* Using iText PDF Library for creating PDF files. Endpoint: `/v1/generate/pdf`
### Community
* Please send us your suggestions on how we make this code even more useful for the development community or contribute to this repo!
* Check out our [blog](https://oril.co/blog) with more articles!
