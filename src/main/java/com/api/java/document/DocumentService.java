package com.api.java.document;

import com.api.java.document.exception.DocumentReadException;

import java.io.IOException;

public class DocumentService {
    private final DocumentReader documentReader;

    public DocumentService(DocumentReader documentReader) {
        this.documentReader = documentReader;
    }

    public String read(String path) {
        try {
            return documentReader.read(path);
        } catch (IOException e) {
            throw new DocumentReadException(path, e);
        }
    }
}
