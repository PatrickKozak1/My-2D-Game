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


    }

    @Override
    public void interact() {
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

        g2.setColor(new Color(245,225,170));
        g2.fillRoundRect(4, 6, size - 8, size - 12, 4, 4);

        g2.setColor(new Color(180,140, 80, 120));
        g2.fillRect(6,size-8, size - 10, 3);

        g2.setColor(new Color(160,100,40));
        g2.fillRoundRect(2, 3, size - 4, 6, 6, 6);

        g2.setColor(new Color(160,100,40));
        g2.fillRoundRect(2, size - 9, size - 4, 6, 6, 6);

        g2.setColor(new Color(100,60,20));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(2,3,size- 4,6,6,6);
        g2.drawRoundRect(2, size - 9, size - 4, 6, 6, 6);

        g2.setColor(new Color(139, 90, 43));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(4, 6, size - 8, size - 12, 4, 4);

        g2.setColor(new Color(180, 140, 80, 160));
        g2.setStroke(new BasicStroke(1f));
        for (int y = 12; y < size -12; y += 4) {
            g2.drawLine(8, y, size - 8, y);
        }

        g2.setColor(new Color(180, 50, 50));
        g2.fillOval(size / 2 - 4, size / 2 - 4, 8, 8);
        g2.setColor(new Color(120, 30, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawOval(size / 2 - 4, size / 2 - 4, 8, 8);

        g2.dispose();
        return img;

    }
}
