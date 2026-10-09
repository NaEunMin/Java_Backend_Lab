package com.api.java.document;

import java.io.IOException;

public interface DocumentReader {
    String read(String path) throws IOException;
}
