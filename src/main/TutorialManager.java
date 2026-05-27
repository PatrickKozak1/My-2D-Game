package main;

import entity.Entity;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.HashSet;
import java.util.Set;

public class TutorialManager {

    GamePanel gp;
    Graphics2D g2;

    // ── MAIN TUTORIAL ──────────────────────────────────────
    public int phase = 0;
    private final int maxPhase = 5;

    // ── INVENTORY TUTORIAL ─────────────────────────────────
    public boolean inventoryTutorialActive = false;
    private boolean inventoryTutorialShown = false;
    private int invPhase = 0;
    private final int maxInvPhase = 3;

    // ── ITEM TUTORIAL ──────────────────────────────────────
    private final Set<String> seenItems = new HashSet<>();
    public boolean itemTutorialActive = false;
    private Entity tutorialItem = null;

    // ── SHARED VISUAL STATE ────────────────────────────────
    private String[] lines = {};
    private boolean spotlightActive = false;
    private int hlX, hlY, hlW, hlH;
    private float fadeAlpha = 0f;
    private int pulseCounter = 0;

    public TutorialManager(GamePanel gp) {
        this.gp = gp;
    }

    // ═══════════════════════════════════════════════════════
    //  MAIN TUTORIAL
    // ═══════════════════════════════════════════════════════

    public void startTutorial() {
        phase = 0;
        fadeAlpha = 0f;
        setupMainPhase();
    }

    public void nextMainPhase() {
        fadeAlpha = 0f;
        phase++;
        if (phase > maxPhase) {
            phase = 0;
            gp.gameState = gp.playState;
        } else {
            setupMainPhase();
        }
    }

    private void setupMainPhase() {
        spotlightActive = true;
        switch (phase) {
            case 0:
                spotlightActive = false;
                lines = new String[]{
                        "Welcome to Blue Boy Adventure!",
                        "This tutorial will guide you through the basics."
                };
                break;
            case 1:
                hlX = gp.player.getScreenX() - gp.tileSize;
                hlY = gp.player.getScreenY() - gp.tileSize;
                hlW = gp.tileSize * 3;
                hlH = gp.tileSize * 3;
                lines = new String[]{
                        "[ W A S D ]  Move your character",
                        "Explore the world and discover its secrets!"
                };
                break;
            case 2:
                hlX = 0;
                hlY = 0;
                hlW = gp.tileSize * 9;
                hlH = gp.tileSize * 3;
                lines = new String[]{
                        "Hearts = your Life.   Crystals = your Mana.",
                        "Lose all hearts and it's game over!"
                };
                break;
            case 3:
                hlX = gp.player.getScreenX() - gp.tileSize;
                hlY = gp.player.getScreenY() - gp.tileSize;
                hlW = gp.tileSize * 3;
                hlH = gp.tileSize * 3;
                lines = new String[]{
                        "[ ENTER ] Attack     [ SPACE ] Block / Parry",
                        "[ F ] Use ranged attack or cast a spell"
                };
                break;
            case 4:
                spotlightActive = false;
                lines = new String[]{
                        "[ C ] Character & Inventory",
                        "[ M ] World Map         [ X ] Toggle Minimap"
                };
                break;
            case 5:
                spotlightActive = false;
                lines = new String[]{
                        "[ ESC ] Options Menu      [ P ] Pause",
                        "Good luck on your adventure, Blue Boy!"
                };
                break;
        }
    }
    
    public void triggerInventoryTutorial() {
        if (!inventoryTutorialShown) {
            inventoryTutorialShown = true;
            inventoryTutorialActive = true;
            invPhase = 0;
            fadeAlpha = 0f;
            setupInvPhase();
        }
    }

    public void nextInvPhase() {
        fadeAlpha = 0f;
        invPhase++;
        if (invPhase > maxInvPhase) {
            inventoryTutorialActive = false;
            invPhase = 0;
        } else {
            setupInvPhase();
        }
    }

    private void setupInvPhase() {
        spotlightActive = true;
        switch (invPhase) {
            case 0:
                hlX = gp.tileSize * 12;
                hlY = gp.tileSize;
                hlW = gp.tileSize * 6;
                hlH = gp.tileSize * 5;
                lines = new String[]{
                        "This is your Inventory.",
                        "Navigate with the arrow keys to select items."
                };
                break;
            case 1:
                hlX = gp.tileSize * 2;
                hlY = gp.tileSize;
                hlW = gp.tileSize * 5;
                hlH = gp.tileSize * 10;
                lines = new String[]{
                        "These are your Character Stats.",
                        "Level up to improve your strength and defense!"
                };
                break;
            case 2:
                hlX = gp.tileSize * 12;
                hlY = gp.tileSize;
                hlW = gp.tileSize * 6;
                hlH = gp.tileSize * 5;
                lines = new String[]{
                        "[ ENTER ] Equip or use the selected item.",
                        "Equipped items are highlighted in gold."
                };
                break;
            case 3:
                hlX = gp.tileSize * 12;
                hlY = gp.tileSize * 6;
                hlW = gp.tileSize * 6;
                hlH = gp.tileSize * 3;
                lines = new String[]{
                        "Item descriptions appear here.",
                        "Always check what you pick up!"
                };
                break;
        }
    }

    public void drawInventoryOverlay(Graphics2D g2) {
        this.g2 = g2;
        tickFadeAndPulse();

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
        if (spotlightActive) {
            drawSpotlight();
        } else {
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }
        drawTextBox(invPhase, maxInvPhase);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void triggerItemTutorial(Entity item) {
        if (item == null || seenItems.contains(item.name)) return;
        if (gp.gameState != gp.playState) return;

        seenItems.add(item.name);
        tutorialItem = item;
        itemTutorialActive = true;
        fadeAlpha = 0f;
        gp.gameState = gp.tutorialState;
    }

    public void dismissItemTutorial() {
        itemTutorialActive = false;
        tutorialItem = null;
        gp.gameState = gp.playState;
    }

    // Skip Tutorials
   public void skippAll(){
        phase = 0;
        inventoryTutorialActive = false;
        inventoryTutorialShown = true;
        invPhase = 0;
        itemTutorialActive = false;
        tutorialItem = null;
        fadeAlpha = 0f;
        gp.gameState = gp.playState;
   }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        tickFadeAndPulse();

        if (itemTutorialActive) {
            drawItemTutorial();
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
            if (spotlightActive) {
                drawSpotlight();
            } else {
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            }
            drawTextBox(phase, maxPhase);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }


    private void tickFadeAndPulse() {
        if (fadeAlpha < 1f) {
            fadeAlpha += 0.04f;
            if (fadeAlpha > 1f) fadeAlpha = 1f;
        }
        pulseCounter++;
    }

    private void drawSpotlight() {
        Area overlay = new Area(new Rectangle(0, 0, gp.screenWidth, gp.screenHeight));

        float pulse = (float) (Math.sin(pulseCounter * 0.05) * 4);
        Area spotlight = new Area(new RoundRectangle2D.Float(
                hlX - pulse, hlY - pulse,
                hlW + pulse * 2, hlH + pulse * 2,
                20, 20
        ));
        overlay.subtract(spotlight);

        g2.setColor(new Color(0, 0, 0, 185));
        g2.fill(overlay);

        // Pulsierender Gold-Rahmen
        float borderPulse = 0.65f + (float) (Math.sin(pulseCounter * 0.05) * 0.35f);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, borderPulse * fadeAlpha));
        g2.setColor(new Color(255, 215, 0));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect((int) (hlX - pulse), (int) (hlY - pulse),
                (int) (hlW + pulse * 2), (int) (hlH + pulse * 2), 20, 20);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
    }

    private void drawTextBox(int currentPhase, int total) {
        int boxX = gp.tileSize;
        int boxY = (int) (gp.tileSize * 8.5);
        int boxW = gp.screenWidth - gp.tileSize * 2;
        int boxH = gp.tileSize * 3;

        gp.ui.drawSubWindow(boxX, boxY, boxW, boxH);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 26f));
        g2.setColor(Color.white);
        int tx = boxX + 24;
        int ty = boxY + 44;
        for (String line : lines) {
            g2.drawString(line, tx, ty);
            ty += 38;
        }

        drawStepDots(boxX, boxY, boxW, boxH, currentPhase, total);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 18f));
        g2.setColor(new Color(130, 130, 130));
        g2.drawString("[ ENTER ]  Continue", boxX + 24, boxY + boxH - 12);
    }

    private void drawStepDots(int boxX, int boxY, int boxW, int boxH, int current, int total) {
        int dotSize = 10;
        int dotGap = 18;
        int count = total + 1;
        int totalW = count * dotGap - (dotGap - dotSize);
        int startX = boxX + boxW / 2 - totalW / 2;
        int dotY = boxY + boxH - 14;

        for (int i = 0; i < count; i++) {
            g2.setColor(i == current ? new Color(255, 215, 0) : new Color(80, 80, 80));
            g2.fillOval(startX + i * dotGap, dotY - dotSize / 2, dotSize, dotSize);
        }
    }

    private void drawItemTutorial() {
        // Lokale Kopie – verhindert dass tutorialItem zwischen den Aufrufen null wird
        Entity item = tutorialItem;
        if (item == null) {
            itemTutorialActive = false;
            gp.gameState = gp.playState;
            return;
        }

        // Dunkler Hintergrund
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha * 0.85f));
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Card-Fenster
        int cardW = gp.tileSize * 9;
        int cardH = gp.tileSize * 7;
        int cardX = gp.screenWidth  / 2 - cardW / 2;
        int cardY = gp.screenHeight / 2 - cardH / 2;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
        gp.ui.drawSubWindow(cardX, cardY, cardW, cardH);

        // Typ-Badge
        String typeName = getTypeName(item);
        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 16f));
        g2.setColor(new Color(255, 215, 0));
        int tnW = g2.getFontMetrics().stringWidth(typeName);
        g2.drawString(typeName, cardX + cardW / 2 - tnW / 2, cardY + 28);

        // Trennlinie
        g2.setColor(new Color(255, 215, 0, 100));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(cardX + 20, cardY + 36, cardX + cardW - 20, cardY + 36);

        // Item-Bild
        if (item.down1 != null) {
            int imgSize = gp.tileSize + 16;
            int imgX = cardX + cardW / 2 - imgSize / 2;
            g2.drawImage(item.down1, imgX, cardY + 44, imgSize, imgSize, null);
        }

        // Item-Name
        int nameY = cardY + 44 + gp.tileSize + 28;
        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 30f));
        g2.setColor(Color.white);
        int nW = g2.getFontMetrics().stringWidth(item.name);
        g2.drawString(item.name, cardX + cardW / 2 - nW / 2, nameY);


        String desc = (item.description != null && !item.description.isEmpty())
                ? item.description : "A mysterious item.";
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 20f));
        g2.setColor(new Color(190, 190, 190));
        int descY = nameY + 34;
        for (String line : desc.split("\n")) {
            int lW = g2.getFontMetrics().stringWidth(line);
            g2.drawString(line, cardX + cardW / 2 - lW / 2, descY);
            descY += 28;
        }

        String hint = getItemHint(item);
        int hintBoxY = cardY + cardH - gp.tileSize - 14;
        g2.setColor(new Color(255, 215, 0, 40));
        g2.fillRoundRect(cardX + 16, hintBoxY - 22, cardW - 32, 32, 8, 8);
        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 19f));
        g2.setColor(new Color(255, 215, 0));
        int hW = g2.getFontMetrics().stringWidth(hint);
        g2.drawString(hint, cardX + cardW / 2 - hW / 2, hintBoxY);


        g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 17f));
        g2.setColor(new Color(110, 110, 110));
        g2.drawString("[ ENTER ]  Got it", cardX + 20, cardY + cardH - 14);
        String skipText = "[ ESC ]  Skip";
        int skW = g2.getFontMetrics().stringWidth(skipText);
        g2.drawString(skipText, cardX + cardW - skW - 20, cardY + cardH - 14);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private String getTypeName(Entity item) {
        switch (item.type) {
            case 3:
                return "- WEAPON  (Sword) -";
            case 4:
                return "- WEAPON  (Axe) -";
            case 5:
                return "- SHIELD -";
            case 6:
                return "- CONSUMABLE -";
            case 7:
                return "- PICKUP -";
            case 9:
                return "- LIGHT SOURCE -";
            case 10:
                return "- TOOL -";
            default:
                return "- ITEM -";
        }
    }

    private String getItemHint(Entity item) {
        switch (item.type) {
            case 3:
            case 4:
                return "Inventory [ C ]  ->  Equip  ->  Attack [ ENTER ]";
            case 5:
                return "Inventory [ C ]  ->  Equip  ->  Block [ SPACE ]";
            case 6:
                return "Inventory [ C ]  ->  Select  ->  Use [ ENTER ]";
            case 9:
                return "Inventory [ C ]  ->  Equip to light dark areas";
            default:
                return "Check your Inventory [ C ] for details";
        }
    }
}