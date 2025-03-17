package main;

class Book extends Item {
    private String author;
    
    public Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
    }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author;
    }
}