package model;

public class Task {
    private String title;
    private String desc;
    private String den;
    private String doc;
    private String img;

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getDen() {
        return den;
    }

    public String getDoc() {
        return doc;
    }

    public String getImg() {
        return img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDen(String den) {
        this.den = den;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", den='" + den + '\'' +
                ", doc='" + doc + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public Task(String title, String desc, String den, String doc, String img) {
        this.title = title;
        this.desc = desc;
        this.den = den;
        this.doc = doc;
        this.img = img;
    }
}
