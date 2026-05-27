package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OBJ_Scroll extends Entity {

    GamePanel gp;
    public String noteId;

    public OBJ_Scroll(GamePanel gp, String noteId) {
        super(gp);
        this.gp = gp;
        this.noteId = noteId;
        name = "Scroll";
        type = type_obstacle;
        collision = true;

        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = gp.tileSize -8;
        solidArea.height = gp.tileSize -8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        down1  = generateScrollSprite();
        down2  = down1;
        up1    = down1;
        up2    = down1;
        left1  = down1;
        left2  = down1;
        right1 = down1;
        right2 = down1;


    }

    @Override
    public void interact() {
        gp.keyH.enterPressed = false;
        gp.loreManager.openNote(noteId);    // ← Notiz öffnen
        // Scroll aus der Welt entfernen nach dem Lesen
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] == this) {
                gp.obj[gp.currentMap][i] = null;
                break;
            }
        }
    }

    private BufferedImage generateScrollSprite() {
        int size = gp.tileSize;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Schatten
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRoundRect(5, size - 10, size - 8, 6, 4, 4);

        // Pergament-Körper (hochkant, wie eine aufgerollte Schriftrolle)
        g2.setColor(new Color(240, 220, 160));
        g2.fillRoundRect(size / 2 - 10, 6, 20, size - 12, 4, 4);

        // Pergament-Schattierung links
        g2.setColor(new Color(200, 175, 110, 120));
        g2.fillRoundRect(size / 2 - 10, 6, 6, size - 12, 4, 4);

        // Obere Rolle (dicker Zylinder)
        g2.setColor(new Color(160, 100, 40));
        g2.fillRoundRect(size / 2 - 13, 4, 26, 9, 8, 8);

        // Untere Rolle
        g2.setColor(new Color(160, 100, 40));
        g2.fillRoundRect(size / 2 - 13, size - 13, 26, 9, 8, 8);

        // Rand Rollen
        g2.setColor(new Color(100, 55, 15));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(size / 2 - 13, 4, 26, 9, 8, 8);
        g2.drawRoundRect(size / 2 - 13, size - 13, 26, 9, 8, 8);

        // Highlight auf Rollen (3D-Effekt)
        g2.setColor(new Color(200, 140, 70, 150));
        g2.fillRoundRect(size / 2 - 10, 5, 20, 4, 4, 4);
        g2.fillRoundRect(size / 2 - 10, size - 12, 20, 4, 4, 4);

        // Rand Pergament
        g2.setColor(new Color(180, 140, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(size / 2 - 10, 6, 20, size - 12, 4, 4);

        // Linien auf dem Pergament
        g2.setColor(new Color(160, 120, 60, 140));
        g2.setStroke(new BasicStroke(0.8f));
        for (int y = 16; y < size - 14; y += 5) {
            g2.drawLine(size / 2 - 7, y, size / 2 + 7, y);
        }

        // Rotes Siegel in der Mitte
        g2.setColor(new Color(180, 40, 40));
        g2.fillOval(size / 2 - 5, size / 2 - 5, 10, 10);
        g2.setColor(new Color(220, 80, 80, 150));
        g2.fillOval(size / 2 - 3, size / 2 - 4, 4, 3);
        g2.setColor(new Color(120, 20, 20));
        g2.setStroke(new BasicStroke(1f));
        g2.drawOval(size / 2 - 5, size / 2 - 5, 10, 10);

        g2.dispose();
        return img;
    }
}
