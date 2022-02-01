package com.example.GroupF9_HW05;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Comment {
    String name, cText, uId;
    Date date;

    @DocumentId
    String docId;

    public Comment() {
    }

    public Comment(String name, String cText, String uId, Date date, String docId) {
        this.name = name;
        this.cText = cText;
        this.uId = uId;
        this.date = date;
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcText() {
        return cText;
    }

    public void setcText(String cText) {
        this.cText = cText;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}