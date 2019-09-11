package com.exam.app.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Service
public class StorageService {

    private final String tempSimpleFile = "C:\\TestFolder\\jpa.txt";
    private final String destination = "C:\\TestFolder\\final.txt";

    public void processFile(MultipartFile file) {
        log.debug("Start file processing with name: {}", file.getOriginalFilename());
        writeBytesToFile(file, tempSimpleFile, destination);
    }

    private void writeBytesToFile(MultipartFile file, String tempFile, String destination) {
        try {
            File simpleFile = new File(tempFile);
            file.transferTo(simpleFile);
            PDFParser parser = new PDFParser(new RandomAccessFile(simpleFile, "r"));
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            PDDocument pdDocument = new PDDocument(cosDoc);
            PrintWriter pw = new PrintWriter(destination);
            pw.print(pdfTextStripper.getText(pdDocument));
            pw.close();
        } catch (IOException e) {
            log.error("Something went wrong: {}", e.getMessage());
        }

    }
}
