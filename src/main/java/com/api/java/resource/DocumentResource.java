package com.api.java.resource;

public interface DocumentResource extends AutoCloseable{
    String read();

    @Override
    void close();
}
