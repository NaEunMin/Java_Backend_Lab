package com.api.java.resource;

public class DocumentResourceService {
    private final DocumentResourceFactory documentResourceFactory;

    public DocumentResourceService(DocumentResourceFactory documentResourceFactory) {
        this.documentResourceFactory = documentResourceFactory;
    }

    public String read(){
        try(DocumentResource documentResource = documentResourceFactory.open()){
            return documentResource.read();
        }
    }
}
