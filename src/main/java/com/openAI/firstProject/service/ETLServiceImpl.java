package com.openAI.firstProject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ETLServiceImpl implements ETLService{

    private final VectorStore vectorStore;
    private final Resource pdfDocument;

    public ETLServiceImpl(VectorStore vectorStore, @Value("classpath:/document.pdf") Resource pdfDocument) {
        this.vectorStore = vectorStore;
        this.pdfDocument = pdfDocument;
    }


    @Override
    public void loadDataFromPdf() {
        // Extract
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfDocument,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        List<Document> documents = pdfReader.read();

        // Transform
        TokenTextSplitter splitter = TokenTextSplitter.builder().build();

        // Load Write
        vectorStore.add(splitter.apply(documents));

        log.info("Data putted into vector db {}", documents);
    }
}
