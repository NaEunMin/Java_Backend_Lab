package com.api.java.document;

import com.api.java.document.DocumentReader;
import com.api.java.document.DocumentService;
import com.api.java.document.exception.DocumentReadException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentServiceTest {

    @Test
    public void 서비스가_같은_문자열을_반환하는지_검증() {
        DocumentReader documentReader = new DocumentReader() {
            @Override
            public String read(String path) throws IOException {
                return "주문서 내용";
            }
        };
        DocumentService documentService = new DocumentService(documentReader);

        assertEquals("주문서 내용", documentService.read("documents/order-100.txt"));
    }

    @Test
    public void 체크예외를_언체크예외로_변환하여_전파한다() {
        IOException originalException = new IOException("디스크 읽기 실패");

        DocumentReader documentReader = new DocumentReader() {
            @Override
            public String read(String path) throws IOException {
                throw originalException;
            }
        };

        DocumentService documentService = new DocumentService(documentReader);
        String path = "documents/order-200.txt";

        DocumentReadException readException =
                assertThrows(DocumentReadException.class,
                        () -> documentService.read(path));

        assertEquals(path, readException.getPath());
        assertSame(originalException, readException.getCause());
        assertTrue(readException.getMessage().contains(path));
    }
}
