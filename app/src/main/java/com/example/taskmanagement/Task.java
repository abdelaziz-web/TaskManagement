package com.example.taskmanagement;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Task {
    private String id;
    private String title;
    private String desc;
    private Boolean done;
    private String img;
    private Timestamp date;
    private String email;



    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getId(){ return  id;}

    public void setId(String id) {this.id=id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "task{" +
                "title='" + title + '\'' +
                "email='" + email + '\'' +
                ", desc='" + desc + '\'' +
                ", done='" + done + '\'' +
                ", img='" + img + '\'' +
                ", date='" + date + '\'' +
                ", id='"+ id +'\''+
                '}';
    }
    public Task() {
        // Default constructor required for Firestore
    }

    public Task(String title,String email, String desc, boolean done, String img, Timestamp date,String id) {
        this.title = title;
        this.email = email;
        this.desc = desc;
        this.done = done;
        this.img = img;
        this.date = date;
        this.id = id;
    }
    public String getFormattedDate() {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US);
            return sdf.format(date.toDate());
        }
        return null;
    }
}
