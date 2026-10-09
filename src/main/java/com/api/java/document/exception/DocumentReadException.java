package com.api.java.document.exception;

public class DocumentReadException extends RuntimeException {
    private final String path;

    public DocumentReadException(String path, Throwable cause) {
        super("문서를 읽지 못했습니다. path=" + path, cause);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
