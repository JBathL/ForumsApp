package com.example.GroupF9_HW05;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;
import java.util.HashMap;

public class Forum {
    String forumTitle, name, post, uID;
    Date date;

    @DocumentId
    String docId;
    //string since it keeps track of uId

    HashMap<String, Boolean> likeHashMap = new HashMap<String, Boolean>();

    @Override
    public String toString() {
        return "Forum{" +
                "forumTitle='" + forumTitle + '\'' +
                ", name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", uID='" + uID + '\'' +
                ", date=" + date +
                ", docId='" + docId + '\'' +
                ", likeHashMap=" + likeHashMap +
                '}';
    }

    public Forum() {
    }

    public Forum(String forumTitle, String name, String post, String uID, Date date, String docId, HashMap<String, Boolean> likeHashMap) {
        this.forumTitle = forumTitle;
        this.name = name;
        this.post = post;
        this.uID = uID;
        this.date = date;
        this.docId = docId;
        this.likeHashMap = likeHashMap;
    }

    public Forum(HashMap<String, Boolean> likeHashMap) {
        this.likeHashMap = likeHashMap;
    }

    public HashMap<String, Boolean> getLikeHashMap() {
        return likeHashMap;
    }

    public void setLikeHashMap(HashMap<String, Boolean> likeHashMap) {
        this.likeHashMap = likeHashMap;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
