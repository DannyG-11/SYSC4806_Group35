package org.example;

import org.example.models.Document;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DocumentTest {

    private Document testDocument;

    @BeforeAll
    public void setUp() {
        testDocument = new Document("Title", "link");
    }

    @AfterAll
    public void tearDown() {
        testDocument = null;
    }

    @Test
    public void setAndGetId() {
        testDocument.setId(8L);
        assertEquals(8L, testDocument.getId());
    }

    @Test
    public void setAndGetLink() {
        testDocument.setLink("link");
        assertEquals("link", testDocument.getLink());
    }

    @Test
    public void setAndGetTitle() {
        testDocument.setTitle("title");
        assertEquals("title", testDocument.getTitle());
    }
}
