package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CipherManager {

    GamePanel gp;

    public List<CipherScroll> scrolls = new ArrayList<>();
    public Set<String> knownWords = new HashSet<>();

    public boolean reading = false;
    private CipherScroll currentScroll = null;
    private int currentPage = 0;
    private boolean showDecoded = false;

    public boolean decoderOpen = false;
    public int decoderTab = 0;
    public int selectedScrollIndex = 0;

    private float fadeAlpha = 0f;
    private int glowCounter = 0;

    public CipherManager(GamePanel gp) {
        this.gp = gp;
        setupScrolls();
    }

    private void setupScrolls() {
        scrolls.add(new CipherScroll(
                "cipher_intro",
                "Fragment I  –  The Old Tongue",
                new String[]{
                        "This is the language of the ancients.\n" +
                                "Few can read these words.\n" +
                                "Seek the other fragments\n" +
                                "to understand the truth.",
                        "The key to the door\n" +
                                "is not made of iron.\n" +
                                "It is made of knowledge."
                },
                new String[]{"the", "is", "of", "this", "key", "door"}
        ));

        scrolls.add(new CipherScroll(
                "cipher_two",
                "Fragment II  –  The Warning",
                new String[]{
                        "Do not trust the merchant\n" +
                                "who smiles in the dark.\n" +
                                "He knows where the\n" +
                                "treasure is hidden.",
                        "The three seals must\n" +
                                "be broken in order.\n" +
                                "Water first. Then fire.\n" +
                                "Then what lies beneath."
                },
                new String[]{"not", "trust", "merchant", "treasure", "hidden", "three", "seals"}
        ));

        scrolls.add(new CipherScroll(
                "cipher_three",
                "Fragment III  –  The Final Truth",
                new String[]{
                        "The old wizard knew\n" +
                                "what darkness sleeps below.\n" +
                                "He chose to forget.\n" +
                                "You must remember.",
                        "When all seals are broken\n" +
                                "and the door opens wide\n" +
                                "do not step through alone.\n" +
                                "You have been warned."
                },
                new String[]{"wizard", "darkness", "below", "forget", "remember",
                        "when", "all", "broken", "alone", "warned"}
        ));
    }

    public CipherScroll getScroll(String id) {
        for (CipherScroll s : scrolls) if (s.id.equals(id)) return s;
        return null;
    }

    public void openScroll(String id) {
        CipherScroll scroll = getScroll(id);
        if (scroll == null) {
            System.out.println("Scroll nicht gefunden: " + id);
            return;
        }

        if (!scroll.collected) {
            scroll.collected = true;
            for (String word : scroll.taughtWords) {
                knownWords.add(word.toLowerCase());
            }
            gp.ui.addMessage("Ancient scroll found!");
            gp.ui.addMessage("You learned " + scroll.taughtWords.length + " new words!");
        }

        currentScroll = scroll;
        currentPage = 0;
        showDecoded = false;
        fadeAlpha = 0f;
        reading = true;
        decoderOpen = false;
        gp.gameState = gp.cipherState;
    }

    public void openDecoder() {
        decoderOpen = true;
        reading = false;
        fadeAlpha = 0f;
        gp.gameState = gp.cipherState;
    }

    public void readAgain(CipherScroll scroll) {
        currentScroll = scroll;
        currentPage = 0;
        showDecoded = false;
        fadeAlpha = 0f;
        reading = true;
        decoderOpen = false;
    }

    public void nextPage() {
        if (currentScroll == null) return;
        showDecoded = false;
        if (currentPage < currentScroll.encodedPages.length - 1) {
            currentPage++;
            fadeAlpha = 0f;
        } else {
            close();
        }
    }

    public void toggleDecode() {
        showDecoded = !showDecoded;
        fadeAlpha = 0f;
    }

    public void close() {
        reading = false;
        decoderOpen = false;
        currentScroll = null;
        currentPage = 0;
        showDecoded = false;
        decoderTab = 0;
        selectedScrollIndex = 0;
        gp.gameState = gp.playState;
    }

    public int getCollectedCount() {
        int n = 0;
        for (CipherScroll s : scrolls) if (s.collected) n++;
        return n;
    }

    public void update() {
        if (fadeAlpha < 1f) {
            fadeAlpha += 0.05f;
            if (fadeAlpha > 1f) fadeAlpha = 1f;
        }
        glowCounter++;
    }

    public void draw(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (decoderOpen) {
            drawDecoderScreen(g2);
        } else if (reading && currentScroll != null) {
            drawScrollScreen(g2);
        }
    }

    private void drawScrollScreen(Graphics2D g2) {
        int pW = gp.tileSize * 12;
        int pH = gp.tileSize * 10;
        int pX = gp.screenWidth / 2 - pW / 2;
        int pY = gp.screenHeight / 2 - pH / 2;

        drawAncientParchment(g2, pX, pY, pW, pH);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 20f));
        g2.setColor(new Color(80, 40, 10));
        int tW = g2.getFontMetrics().stringWidth(currentScroll.title);
        g2.drawString(currentScroll.title, pX + pW / 2 - tW / 2, pY + 38);

        drawRuneDecoration(g2, pX + 20, pY + 48, pW - 40);

        String pageText = showDecoded
                ? AncientLanguage.partialDecode(currentScroll.encodedPages[currentPage], knownWords)
                : currentScroll.encodedPages[currentPage];

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));

        if (!showDecoded) {
            float glow = 0.6f + 0.4f * (float) (Math.sin(glowCounter * 0.04));
            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 20f));
            g2.setColor(new Color(120, 60, 20, (int) (glow * 180)));
            drawWrappedText(g2, pageText, pX + 32, pY + 80, pW - 64, 32);
            g2.setColor(new Color(60, 25, 8));
            drawWrappedText(g2, pageText, pX + 30, pY + 78, pW - 64, 32);
        } else {
            drawPartialText(g2, currentScroll.encodedPages[currentPage],
                    pX + 30, pY + 78, pW - 64, 32);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        String badge = "Known words: " + knownWords.size();
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 14f));
        g2.setColor(new Color(120, 70, 30));
        g2.drawString(badge, pX + 20, pY + pH - 44);

        String pageInfo = (currentPage + 1) + " / " + currentScroll.encodedPages.length;
        int piW = g2.getFontMetrics().stringWidth(pageInfo);
        g2.drawString(pageInfo, pX + pW / 2 - piW / 2, pY + pH - 14);

        drawRuneDecoration(g2, pX + 20, pY + pH - 56, pW - 40);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 15f));
        g2.setColor(new Color(100, 55, 20));

        String decodeHint = showDecoded ? "[ T ] Hide Translation" : "[ T ] Translate Known";
        g2.drawString(decodeHint, pX + 20, pY + pH - 28);

        boolean isLast = currentPage >= currentScroll.encodedPages.length - 1;
        String nextHint = isLast ? "[ ENTER ] Close" : "[ ENTER ] Next Page";
        int nhW = g2.getFontMetrics().stringWidth(nextHint);
        g2.drawString(nextHint, pX + pW - nhW - 20, pY + pH - 28);

        g2.setColor(new Color(150, 100, 40));
        g2.drawString("[ D ] Open Decoder", pX + pW / 2 - 60, pY + pH - 14);
    }

    private void drawDecoderScreen(Graphics2D g2) {
        int pW = gp.tileSize * 14;
        int pH = gp.tileSize * 10;
        int pX = gp.screenWidth / 2 - pW / 2;
        int pY = gp.screenHeight / 2 - pH / 2;

        drawAncientParchment(g2, pX, pY, pW, pH);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 22f));
        g2.setColor(new Color(80, 40, 10));
        String title = "The Ancient Tongue  –  Decoder";
        int tW = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, pX + pW / 2 - tW / 2, pY + 38);

        drawRuneDecoration(g2, pX + 20, pY + 48, pW - 40);

        int tabY = pY + 68;
        String[] tabs = {"Alphabet", "My Scrolls"};
        for (int i = 0; i < tabs.length; i++) {
            boolean sel = (decoderTab == i);
            g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 15f));
            int tabX = pX + 30 + i * 140;
            if (sel) {
                g2.setColor(new Color(139, 90, 43));
                g2.fillRoundRect(tabX - 8, tabY - 16, 130, 24, 6, 6);
                g2.setColor(new Color(245, 225, 170));
            } else {
                g2.setColor(new Color(160, 110, 60));
            }
            g2.drawString(tabs[i], tabX, tabY);
        }

        if (decoderTab == 0) {
            drawAlphabetTab(g2, pX, pY, pW, pH);
        } else {
            drawMyScrollsTab(g2, pX, pY, pW, pH);
        }

        drawRuneDecoration(g2, pX + 20, pY + pH - 56, pW - 40);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 15f));
        g2.setColor(new Color(100, 55, 20));
        g2.drawString("Scrolls found: " + getCollectedCount() + " / " + scrolls.size(),
                pX + 20, pY + pH - 28);
        g2.drawString("[ 1 / 2 ] Switch Tab    [ ESC ] Close",
                pX + pW / 2 - 130, pY + pH - 28);
    }

    private void drawAlphabetTab(Graphics2D g2, int pX, int pY, int pW, int pH) {
        if (knownWords.isEmpty()) {
            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 20f));
            g2.setColor(new Color(120, 70, 30));
            g2.drawString("Find Ancient Scrolls to learn the language.", pX + 30, pY + 110);
            return;
        }

        int col1X = pX + 30;
        int col2X = pX + pW / 2 + 20;
        int startY = pY + 100;
        int lineH = 30;
        int row = 0;

        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 16f));
        g2.setColor(new Color(120, 70, 30));
        g2.drawString("Ancient → English", col1X, startY - 10);
        g2.drawString("Ancient → English", col2X, startY - 10);

        List<String> sorted = new ArrayList<>(knownWords);
        java.util.Collections.sort(sorted);

        for (String word : sorted) {
            String encoded = AncientLanguage.encode(word);
            int cx = (row % 2 == 0) ? col1X : col2X;
            int cy = startY + (row / 2) * lineH;

            float glow = 0.5f + 0.3f * (float) (Math.sin(glowCounter * 0.03 + row));
            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 18f));
            g2.setColor(new Color(120, 60, 20, (int) (glow * 200)));
            g2.drawString(encoded, cx, cy);
            g2.setColor(new Color(60, 25, 8));
            g2.drawString(encoded, cx - 1, cy - 1);
            g2.setColor(new Color(139, 90, 43));
            g2.drawString("→", cx + 110, cy);
            g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 17f));
            g2.setColor(new Color(40, 20, 5));
            g2.drawString(word, cx + 140, cy);
            row++;
            if (cy > pY + pH - 70) break;
        }
    }

    private void drawMyScrollsTab(Graphics2D g2, int pX, int pY, int pW, int pH) {
        List<CipherScroll> collected = new ArrayList<>();
        for (CipherScroll s : scrolls) if (s.collected) collected.add(s);

        if (collected.isEmpty()) {
            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 20f));
            g2.setColor(new Color(120, 70, 30));
            g2.drawString("No scrolls collected yet.", pX + 30, pY + 110);
            return;
        }

        int listX = pX + 20;
        int listY = pY + 90;
        int lineH = 36;

        for (int i = 0; i < collected.size(); i++) {
            boolean sel = (i == selectedScrollIndex);
            g2.setFont(gp.ui.purisaB.deriveFont(sel ? Font.BOLD : Font.PLAIN, 17f));
            if (sel) {
                g2.setColor(new Color(180, 120, 60, 100));
                g2.fillRoundRect(listX - 4, listY + i * lineH - 16, 200, 24, 6, 6);
                g2.setColor(new Color(80, 40, 10));
                g2.drawString(">", listX - 14, listY + i * lineH);
            } else {
                g2.setColor(new Color(120, 75, 30));
            }
            g2.drawString(collected.get(i).title, listX, listY + i * lineH);
        }

        // Vorschau rechts
        if (selectedScrollIndex >= 0 && selectedScrollIndex < collected.size()) {
            CipherScroll sel = collected.get(selectedScrollIndex);
            int prevX = pX + pW / 2 + 10;
            int prevY = pY + 85;
            int prevW = pW / 2 - 30;

            g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 16f));
            g2.setColor(new Color(80, 40, 10));
            g2.drawString(sel.title, prevX, prevY);

            g2.setColor(new Color(139, 90, 43));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(prevX, prevY + 6, prevX + prevW, prevY + 6);

            String preview = AncientLanguage.partialDecode(sel.encodedPages[0], knownWords);
            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 15f));
            g2.setColor(new Color(60, 30, 10));
            drawWrappedText(g2, preview, prevX, prevY + 28, prevW, 24);

            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 14f));
            g2.setColor(new Color(100, 55, 20));
            g2.drawString("[ R ] Read again", prevX, pY + pH - 70);
        }
    }


    private void drawPartialText(Graphics2D g2, String encoded,
                                 int x, int y, int maxW, int lineH) {
        String[] words = encoded.split(" ");
        int currX = x;
        int currY = y;
        FontMetrics fm = g2.getFontMetrics();

        for (String word : words) {
            String decoded = AncientLanguage.decode(word);
            boolean known = knownWords.contains(decoded.toLowerCase().replaceAll("[^a-z]", ""));
            String display = known ? decoded : word;

            if (currX + fm.stringWidth(display) > x + maxW) {
                currX = x;
                currY += lineH;
            }

            if (known) {
                g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 20f));
                g2.setColor(new Color(40, 20, 5));
            } else {
                float glow = 0.5f + 0.4f * (float) (Math.sin(glowCounter * 0.04));
                g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 20f));
                g2.setColor(new Color(120, 60, 20, (int) (glow * 180)));
            }
            g2.drawString(display + " ", currX, currY);
            currX += fm.stringWidth(display + " ");
        }
    }

    private void drawRuneDecoration(Graphics2D g2, int x, int y, int width) {
        g2.setColor(new Color(139, 90, 43, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(x, y, x + width, y);

        String runes = "ꋫ ꉔ ꂑ ꋊ ꂦ ꉣ";
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 13f));
        int rW = g2.getFontMetrics().stringWidth(runes);
        g2.setColor(new Color(245, 225, 170));
        g2.fillRect(x + width / 2 - rW / 2 - 4, y - 8, rW + 8, 12);
        g2.setColor(new Color(139, 90, 43));
        g2.drawString(runes, x + width / 2 - rW / 2, y + 2);
    }

    private void drawAncientParchment(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(x + 8, y + 8, w, h, 16, 16);

        g2.setColor(new Color(235, 210, 155));
        g2.fillRoundRect(x, y, w, h, 16, 16);

        g2.setColor(new Color(180, 145, 90, 80));
        g2.fillRoundRect(x, y, w, h / 3, 16, 16);
        g2.fillRoundRect(x, y + h * 2 / 3, w, h / 3, 16, 16);

        float shimmer = 0.04f + 0.02f * (float) (Math.sin(glowCounter * 0.03));
        shimmer = Math.max(0f, Math.min(1f, shimmer));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, shimmer));
        g2.setColor(new Color(100, 50, 200));
        g2.fillRoundRect(x, y, w, h, 16, 16);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g2.setColor(new Color(100, 55, 20));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(x, y, w, h, 16, 16);

        g2.setColor(new Color(139, 90, 43, 120));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x + 10, y + 10, w - 20, h - 20, 10, 10);

        drawCornerOrnament(g2, x + 10, y + 10);
        drawCornerOrnament(g2, x + w - 22, y + 10);
        drawCornerOrnament(g2, x + 10, y + h - 22);
        drawCornerOrnament(g2, x + w - 22, y + h - 22);
    }

    private void drawCornerOrnament(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(139, 90, 43));
        g2.fillOval(x, y, 12, 12);
        g2.setColor(new Color(200, 150, 80));
        g2.fillOval(x + 3, y + 3, 6, 6);
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y,
                                 int maxWidth, int lineHeight) {
        FontMetrics fm = g2.getFontMetrics();
        String[] paragraphs = text.split("\n");
        int currentY = y;

        for (String paragraph : paragraphs) {
            String[] words = paragraph.split(" ");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                String test = line.length() > 0 ? line + " " + word : word;
                if (fm.stringWidth(test) > maxWidth) {
                    g2.drawString(line.toString(), x, currentY);
                    currentY += lineHeight;
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(test);
                }
            }
            if (line.length() > 0) {
                g2.drawString(line.toString(), x, currentY);
                currentY += lineHeight;
            }
            currentY += 8;
        }
    }
}