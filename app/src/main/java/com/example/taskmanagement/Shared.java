package com.example.taskmanagement;

public class Shared {

    private String id;

    private String taskid;

    private String user;

    private String type;

    private String password;


    public Shared() {
        // Default constructor required for Firestore
    }
    public Shared(String id, String taskid, String user, String type, String password) {
        this.id = id;
        this.taskid = taskid;
        this.user = user;
        this.type = type;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getSharedid() {
        return taskid;
    }

    public String getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSharedid(String sharedid) {
        this.taskid = sharedid;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Shared{" +
                "id='" + id + '\'' +
                ", sharedid='" + taskid + '\'' +
                ", user='" + user + '\'' +
                ", type='" + type + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
