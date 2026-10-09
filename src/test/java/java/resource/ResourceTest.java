package java.resource;

import java.api.java.resource.DocumentResource;
import java.api.java.resource.DocumentResourceFactory;
import java.api.java.resource.DocumentResourceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceTest {

    @Test
    public void 정상처리_후에도_자원은_닫혀야한다() {
        TrackingDocumentResource resource = new TrackingDocumentResource(
                "문서 내용",
                null,
                null
        );
        DocumentResourceFactory factory = new DocumentResourceFactory() {
            @Override
            public DocumentResource open() {
                return resource;
            }
        };
        DocumentResourceService service = new DocumentResourceService(factory);
        String readContent = service.read();
        assertEquals("문서 내용", readContent);
        assertTrue(resource.isClosed());

    }

    @Test
    public void 읽기실패_후에도_자원은_닫혀야한다() {
        RuntimeException readException = new RuntimeException("읽기 실패");
        TrackingDocumentResource resource = new TrackingDocumentResource(
                null,
                readException,
                null
        );
        DocumentResourceFactory factory = new DocumentResourceFactory() {
            @Override
            public DocumentResource open() {
                return resource;
            }
        };
        DocumentResourceService service = new DocumentResourceService(factory);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            service.read();
        });

        assertSame(readException, thrownException);
        assertTrue(resource.isClosed());
    }

    @Test
    public void 읽기와_쓰기가_모두_실패했을때_읽기예외는_주예외로_처리된다(){
        RuntimeException readException = new RuntimeException("읽기 실패");
        RuntimeException closeException = new RuntimeException("종료 실패");

        TrackingDocumentResource resource = new TrackingDocumentResource(
                null,
                readException,
                closeException
        );

        DocumentResourceFactory factory = new DocumentResourceFactory() {
            @Override
            public DocumentResource open() {
                return resource;
            }
        };

        DocumentResourceService service = new DocumentResourceService(factory);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            service.read();
        });

        assertSame(readException, thrownException);
        assertTrue(resource.isClosed());

        assertEquals(1, thrownException.getSuppressed().length);
        assertSame(closeException, thrownException.getSuppressed()[0]);
    }

    @Test
    public void 읽기는_성공하고_종료만_실패(){
        RuntimeException closeException = new RuntimeException("종료 실패");
        TrackingDocumentResource resource = new TrackingDocumentResource(
                "문서 내용",
                null,
                closeException
        );

        DocumentResourceFactory factory = new DocumentResourceFactory() {
            @Override
            public DocumentResource open() {
                return resource;
            }
        };

        DocumentResourceService service = new DocumentResourceService(factory);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            service.read();
        });
        assertSame(closeException, thrownException);
        assertTrue(resource.isClosed());
    }


    private static class TrackingDocumentResource implements DocumentResource {

        private final String content;
        private final RuntimeException readException;
        private final RuntimeException closeException;

        private boolean closed;

        private TrackingDocumentResource(
                String content,
                RuntimeException readException,
                RuntimeException closeException
        ) {
            this.content = content;
            this.readException = readException;
            this.closeException = closeException;
        }

        @Override
        public String read() {
            if (readException != null) {
                throw readException;
            }
            return content;
        }

        @Override
        public void close() {
            closed = true;
            if (closeException != null) {
                throw closeException;
            }
        }

        boolean isClosed() {
            return closed;
        }
    }


}
