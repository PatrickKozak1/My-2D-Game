package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoreManager {
    GamePanel gp;

    public List<LoreNote> allNotes = new ArrayList<>();
    public List <LoreNote> collectedNotes = new ArrayList<>();

    public boolean reading = false;
    private LoreNote currentNote = null;
    private int currentPage = 0;

    public boolean journalOpen = false;
    public int journalIndex = 0;

    public LoreManager(GamePanel gp){
        this.gp = gp;
        setupNotes();
    }

    private void setupNotes() {
        allNotes.add(new LoreNote(
                "island_history",
                "The Island's History",
                "Unknown Historian",
                "This island was once home to a great and prosperous " +
                        "civilization. They mastered the arts of magic and alchemy, " +
                        "building magnificent structures that still stand today.",
                "But one fateful night, a darkness crept up from beneath the " +
                        "earth. Strange creatures emerged and drove the people away. " +
                        "Only ruins and whispers remain of what once was."
        ));

        allNotes.add(new LoreNote(
                "old_wizard_diary",
                "A Wizard's Diary",
                "Aldric the Old",
                "Day 1 of my exile. I was once the greatest wizard this island " +
                        "had ever seen. Now I am nothing but an old man with fading " +
                        "memories and a walking stick.",
                "The monsters... they are not natural. Something summoned them " +
                        "here. I have spent years searching for the answer but my body " +
                        "grows weaker. Perhaps a young adventurer will finish what I " +
                        "could not."
        ));

        allNotes.add(new LoreNote(
                "slime_research",
                "Monster Research Notes",
                "Dr. Fenwick",
                "Subject: Red Slime. These creatures appear to be drawn to heat " +
                        "and light. They are aggressive but not intelligent. Their red " +
                        "coloration suggests a diet rich in minerals from deep caves.",
                "Most alarming discovery: the Red Slimes appear to be capable of " +
                        "producing and projecting fire. Recommend extreme caution when " +
                        "engaging at close range. Do NOT corner them."
        ));

        allNotes.add(new LoreNote(
                "treasure_clue",
                "Torn Map Fragment",
                "???",
                "...the treasure lies beyond the door that no key can open. " +
                        "Only one who has proven their worth may pass. Seek the three " +
                        "seals hidden across the island...",
                "...the first seal is guarded by water. The second by fire. " +
                        "The third... I dare not write it here. They are watching."
        ));

        allNotes.add(new LoreNote(
                "merchant_note",
                "A Merchant's Warning",
                "Old Gerald",
                "To whoever finds this: DO NOT trust the prices in this " +
                        "land. I have been swindled by three different merchants " +
                        "before finding an honest one. Always check your coins twice.",
                "The hooded merchant near the old ruins sells genuine goods. " +
                        "He is odd, yes. But his wares have saved my life more than once. " +
                        "Tell him Gerald sent you. He'll give you a fair price."
        ));
    }

    public LoreNote getNote(String id) {
        for (LoreNote n : allNotes) if (n.id.equals(id)) return n;
        return null;
    }

    public void openNote(String id){
        LoreNote note = getNote(id);
        if (note == null)return;

        if (!note.collected){
            note.collected = true;
            collectedNotes.add(note);
            gp.ui.addMessage("New lore found: " + note.title + "!");
        }

        currentNote = note;
        currentPage = 0;
        reading = true;
        journalOpen = false;
        gp.gameState = gp.loreState;
    }

    public void nextPage() {
        if (currentNote == null) return;
        if (currentPage < currentNote.pages.length -1) {
            currentPage++;
        }else {
            closeReading();
        }
    }

    public void prevPage(){
        if (currentPage > 0) currentPage--;
    }

    public void openSelectedJournalNote(){
        if (collectedNotes.isEmpty()) return;
        currentNote = collectedNotes.get(journalIndex);
        currentPage = 0;
        reading = true;
        journalOpen = false;
    }

    void closeReading() {
        reading = false;
        journalOpen = false;
        currentNote = null;
        currentPage = 0;
        gp.gameState = gp.playState;
    }

    public void draw(Graphics2D g2){
        g2.setColor(new Color(0,0,0,180));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        if (journalOpen && !reading){
            drawJournal(g2);
        }else if (reading && currentNote != null){
            drawReadingScreen(g2);
        }
    }

    private void drawReadingScreen(Graphics2D g2) {
        int pW = gp.tileSize * 11;
        int pH = gp.tileSize * 9;
        int pX = gp.screenWidth / 2 - pW / 2;
        int pY = gp.screenHeight / 2 - pH / 2;

        drawParchment(g2,pX,pY,pW,pH);

        g2.setColor(new Color(139,90,43));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(pX + 24, pY + 48, pX + pW - 24, pY + 48);
        g2.drawLine(pX + 24, pY + 50, pX + pW - 24, pY + 50);

        // Title
        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 24f));
        g2.setColor(new Color(80,40,10));
        int tW = g2.getFontMetrics().stringWidth(currentNote.title);
        g2.drawString(currentNote.title, pX + pW / 2 - tW / 2, pY +40);

        // Body Text
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 19f));
        g2.setColor(new Color(60,30,10));
        String pageText = currentNote.pages[currentPage];
        drawWrappedText(g2,pageText,pX + 36, pY+ 80,pW- 72,30);

        // Author
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 19f));
        g2.setColor(new Color(120,70,30));
        g2.drawString("~ " + currentNote.author,pX + 36, pY + pH -44);

        String pageInfo = (currentPage +1) + "/" + currentNote.pages.length;
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 16f));
        g2.setColor(new Color(100,60,20));
        int piW = g2.getFontMetrics().stringWidth(pageInfo);
        g2.drawString(pageInfo, pX + pW / 2 - piW / 2, pY + pH - 16);

        g2.setColor(new Color(139,90,43));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(pX + 24, pY + pH -56, pX + pW - 24,pY + pH -56);
        g2.drawLine(pX + 24, pY + pH -58, pX + pW - 24,pY + pH -58);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 16f));
        g2.setColor(new Color(140,90,40));
        boolean isLastPage = currentPage >= currentNote.pages.length -1;
        g2.drawString(isLastPage ? "[ ENTER ] Close" : "[ ENTER ] Next Page", pX + 36, pY + pH - 16);

        if (currentPage > 0){
            g2.drawString("[ BACKSPACE ] Previous", pX + pW - 200, pY + pH - 16);
        }
    }

    private void drawJournal(Graphics2D g2){

        int listX = gp.tileSize;
        int listY = gp.tileSize;
        int listW = gp.tileSize * 7;
        int listH = gp.screenHeight - gp.tileSize * 2;
        drawParchment(g2,listX,listY,listW,listH);

        // Journal Title
        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 22f));
        g2.setColor(new Color(80,40,10));
        g2.drawString("Journal", listX + 20, listY + 36 );
        g2.setColor(new Color(139,90,43));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(listX + 16, listY + 40, listX + listW - 16, listY + 44);

        int entryY = listY + 70;
        for (int i = 0; i < collectedNotes.size(); i++) {
            LoreNote note = collectedNotes.get(i);
            boolean selected = (i == journalIndex );

            if (selected) {
                g2.setColor(new Color(180, 120,60,100));
                g2.fillRoundRect(listX + 12, entryY -18, listW - 24,28,6,6);
                g2.setColor(new Color(139,90,43));
                g2.drawString(">", listX +16, entryY);
            }

            g2.setFont(gp.ui.purisaB.deriveFont(selected ? Font.BOLD : Font.PLAIN, 18f));
            g2.setColor(selected ? new Color(80, 40, 10) : new Color(120, 75, 30));
            g2.drawString(note.title, listX + 36, entryY);

            entryY += 36;
        }
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 15f));
        g2.setColor(new Color(140, 90, 40));
        g2.drawString("[ ENTER ] Read   [ ESC ] Close", listX + 16, listY + listH - 14);

        LoreNote selected = collectedNotes.get(journalIndex);
        int prevX = listX + listW + gp.tileSize /2;
        int prevY = listY;
        int prevW = gp.screenWidth - prevX - gp.tileSize;
        int prevH = listH;
        drawParchment(g2, prevX, prevY, prevW, prevH);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 20f));
        g2.setColor(new Color(80,40,10));
        g2.drawString(selected.title, prevX + 20, prevY + 36);

        g2.setColor(new Color(139,90,43));
        g2.drawLine(prevX + 16, prevY + 44, prevX + prevW - 16, prevY + 44);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 17f));
        g2.setColor(new Color(60,30,10));
        drawWrappedText(g2,selected.pages[0], prevX + 20, prevY + 72, prevW - 40,28);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 15f));
        g2.setColor(new Color(120,70,30));
        g2.drawString("~ " + selected.author, prevX + 20, prevY + prevH - 24);

    }

    private void drawParchment(Graphics2D g2, int x, int y, int w, int h){

        g2.setColor(new Color(0,0,0,80));
        g2.fillRoundRect(x + 6, y +6 ,w,h,16,16);

        g2.setColor(new Color(245,225,170));
        g2.fillRoundRect(x,y,w,h,16,16);

        g2.setColor(new Color(230,205,145,80));
        g2.fillRoundRect(x + 6, y + 6, w - 12, h / 2, 10, 10);

        g2.setColor(new Color(139,90,43));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(x,y,w,h,16,16);

        g2.setColor(new Color(160,110,60,150));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x + 8, y + 8, w - 16, h - 16, 10, 10);

        int cs = 12;
        g2.setColor(new Color(139,90,43));
        g2.fillOval(x + 6, y+ 6, cs, cs);
        g2.fillOval(x + w - 18, y + 6, cs, cs);
        g2.fillOval(x + 6, y + h - 18, cs, cs);
        g2.fillOval(x + w - 18, y + h - 18, cs, cs);
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int maxWidth, int lineHeight) {
        FontMetrics fm = g2.getFontMetrics();
        String[] paragraphs = text.split("\n");
        int currentY = y;

        for (String paragraph : paragraphs) {
            String[] words = paragraph.split(" ");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                String test = line.length() > 0 ? line + " " + word : word;
                if (fm.stringWidth(test) > maxWidth) {
                    g2.drawString(line.toString(),x,currentY);
                    currentY += lineHeight;
                    line = new StringBuilder(word);
                }else {
                    line = new StringBuilder(test);
                }
            }
            if (line.length() > 0){
                g2.drawString(line.toString(), x, currentY);
                currentY += lineHeight;
            }
            currentY += 8;
        }
    }



}
