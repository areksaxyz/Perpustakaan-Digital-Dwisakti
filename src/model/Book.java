package model;

public class Book {
    private String id;
    private String title;
    private String author;
    private String publicationYear;
    private String type;
    private String url;
    private boolean isBorrowed;
    private String subject;

    public Book(String id, String title, String author, String publicationYear, String type, String url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.type = type;
        this.url = url != null ? url : "";
        this.isBorrowed = false;
        this.subject = "";
    }

    // Konstruktor baru untuk menerima int publicationYear
    public Book(String id, String title, String author, int publicationYear, String type, String url, String subject) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = String.valueOf(publicationYear);
        this.type = type;
        this.url = url != null ? url : "";
        this.isBorrowed = false;
        this.subject = subject != null ? subject : "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublicationYear() { return publicationYear; }
    public void setPublicationYear(String publicationYear) { this.publicationYear = publicationYear; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public boolean isDigital() { return "Digital".equals(type); }
}