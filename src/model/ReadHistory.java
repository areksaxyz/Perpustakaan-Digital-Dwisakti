package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadHistory {
    private String readId;
    private Book book;
    private Date readDate;

    public ReadHistory(String readId, Book book) {
        this.readId = readId;
        this.book = book;
        this.readDate = new Date();
    }

    public String getReadId() { return readId; }
    public Book getBook() { return book; }
    public Date getReadDate() { return readDate; }
    public String getFormattedReadDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(readDate);
    }
}