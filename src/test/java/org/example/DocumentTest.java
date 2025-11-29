package org.example;

import org.example.models.Document;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DocumentTest {

    private Document testDocument;

    @Test
    void noArgsConstructor_shouldCreateEmptyDocument() {
        Document document = new Document();

        assertNull(document.getId());
        assertNull(document.getFileName());
        assertNull(document.getContentType());
        assertNull(document.getData());
    }

    @Test
    void emptyConstructorTest() {
        byte[] data = "hello".getBytes();

        Document document = new Document("test.pdf", "application/pdf", data);

        assertEquals("test.pdf", document.getFileName());
        assertEquals("application/pdf", document.getContentType());
        assertArrayEquals(data, document.getData());
    }

    @Test
    void FullConstructorTest() {
        Document document = new Document();

        Long id = 1L;
        String fileName = "file.txt";
        String contentType = "text/plain";
        byte[] data = "content".getBytes();

        document.setId(id);
        document.setFileName(fileName);
        document.setContentType(contentType);
        document.setData(data);

        assertEquals(id, document.getId());
        assertEquals(fileName, document.getFileName());
        assertEquals(contentType, document.getContentType());
        assertArrayEquals(data, document.getData());
    }
}
