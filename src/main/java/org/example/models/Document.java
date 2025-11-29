package org.example.models;

import jakarta.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // file info
    private String fileName;
    private String contentType;

    @Lob
    private byte[] data;

    public Document() { }

    public Document(String fileName, String contentType, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }

    /**
     * Gets the id of this document. The persistence provider should
     * autogenerate a unique id for new document objects.
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the id of this professor to the specified value.
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
