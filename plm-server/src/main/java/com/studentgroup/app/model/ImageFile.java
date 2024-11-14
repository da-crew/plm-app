package com.studentgroup.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "IMAGE_FILE")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String filename;
    private String type;

    @Lob
    private byte[] data;

    //constructors 
    public ImageFile() {}
    public ImageFile(String name, String type, byte[] data) {
        this.filename = name;
        this.type = type;
        this.data = data;
    }
    
    //getters and setters

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String name) {
        this.filename = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
