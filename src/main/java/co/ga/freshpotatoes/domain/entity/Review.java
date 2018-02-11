package co.ga.freshpotatoes.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"id", "author", "content"})
public class Review {
    private long id;
    private String author;
    private String content;
    private int rating;
    
    public Review() {}
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{id:").append(id)
        .append(",author:").append(author)
        .append(",content:").append(content)
        .append(",rating:").append(rating)
        .append("}");
        return s.toString();
    }
}
