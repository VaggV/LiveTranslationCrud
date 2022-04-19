package com.vaggelis.livetranslationcrud.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "translations")
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="timestamp")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Europe/Athens")
    private Timestamp timestamp;
    @Column(name="location")
    private String location;
    @Column(name="userid")
    private String userid;
    @Column(name="originaltext")
    private String originaltext;
    @Column(name="textlang")
    private String textlang;
    @Column(name="translatedtext")
    private String translatedtext;
    @Column(name="translatedtextlang")
    private String translatedtextlang;

    public Translation(){}

    public Translation(Timestamp timestamp, String location, String userid, String originaltext, String textlang, String translatedtext, String translatedtextlang) {
        this.timestamp = timestamp;
        this.location = location;
        this.userid = userid;
        this.originaltext = originaltext;
        this.textlang = textlang;
        this.translatedtext = translatedtext;
        this.translatedtextlang = translatedtextlang;
    }

    public long getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOriginaltext() {
        return originaltext;
    }

    public void setOriginaltext(String originaltext) {
        this.originaltext = originaltext;
    }

    public String getTextlang() {
        return textlang;
    }

    public void setTextlang(String textlang) {
        this.textlang = textlang;
    }

    public String getTranslatedtext() {
        return translatedtext;
    }

    public void setTranslatedtext(String translatedtext) {
        this.translatedtext = translatedtext;
    }

    public String getTranslatedtextlang() {
        return translatedtextlang;
    }

    public void setTranslatedtextlang(String translatedtextlang) {
        this.translatedtextlang = translatedtextlang;
    }

    @Override
    public String toString() {
        return originaltext;
    }
}
