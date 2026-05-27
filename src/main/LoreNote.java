package main;

public class LoreNote {

    public String id;
    public String title;
    public String author;
    public String[] pages;
    public boolean collected = false;

    public LoreNote(String id, String title, String author, String...pages){
        this.id = id;
        this.title = title;
        this.author = author;
        this.pages = pages;
    }
}
