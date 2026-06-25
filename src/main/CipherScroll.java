package main;

public class CipherScroll {

    public String id;
    public String title;
    public String[] encodedPages;
    public String[] decodedPages;
    public String[] taughtWords;
    public boolean collected = false;

    public CipherScroll(String id, String title, String[] decodedPages, String[] taughtWords) {
        this.id = id;
        this.title = title;
        this.decodedPages = decodedPages;
        this.taughtWords = taughtWords;

        this.encodedPages = new String[decodedPages.length];
        for (int i = 0; i < decodedPages.length; i++) {
            this.encodedPages[i] = AncientLanguage.encode(decodedPages[i]);

        }
    }
}
