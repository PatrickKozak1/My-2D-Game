package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.GlyphMetrics;
import java.awt.image.BufferedImage;

public class OBJ_CipherScroll extends Entity {

    GamePanel gp;
    public String scrollId;
    private int glowCounter;

    public OBJ_CipherScroll(GamePanel gp, String scrollId) {
        super(gp);
        this.gp = gp;
        this.scrollId = scrollId;
        name = "Ancient Scroll";
        type = type_obstacle;
        collision = true;

        down1 = generateSprite();
        down2 = down1; up1 = down1; up2 = down1;
        left1 = down1; left2 = down1;
        right1 = down1; right2 = down1;

        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = gp.tileSize - 8;
        solidArea.height = gp.tileSize - 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void draw(Graphics2D g2){

        glowCounter++;
        int sx = getScreenX();
        int sy = getScreenY();

        float pulse = 0.25f + 0.2f * (float)(Math.sin(glowCounter * 0.06));
        pulse = Math.max(0f, Math.min(1f, pulse));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, pulse));
        g2.setColor(new Color(130,60,255));
        g2.fillOval(sx - 6, sy - 6, gp.tileSize + 12, gp.tileSize + 12);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawImage(down1,sx,sy,null);

        g2.setFont(new Font("Arial", Font.BOLD, 10));
        float labelA = 0.6f + 0.4f * (float) (Math.sin(glowCounter * 0.05));
        labelA = Math.max(0f, Math.min(1f, labelA));
        g2.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, labelA));
        g2.setColor(new Color(200,150,255));
        g2.drawString("ANCIENT", sx,sy - 4);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    }

    private BufferedImage generateSprite() {
        int s = gp.tileSize;
        BufferedImage img = new BufferedImage(s,s,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(220,200,245));
        g.fillRoundRect(s / 2 - 10, 6, 20, s - 12, 4,4);

        g.setColor(new Color(100,60,160));
        g.fillRoundRect(s / 2 - 13, 4, 26, 9, 8, 8);
        g.fillRoundRect(s / 2 - 13, s - 13, 26,9,8,8);

        g.setColor(new Color(70,30, 120));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(s / 2 - 13, 4,26,9,8,8);
        g.drawRoundRect(s / 2 - 13, s - 13, 26, 9, 8, 8);
        g.drawRoundRect(s / 2 - 10, 6, 20, s - 12, 4, 4);

        g.setColor(new Color(100,50,180,180));
        g.setStroke(new BasicStroke(0.8f));
        for (int y = 15; y < s- 14 ; y+= 5) {
            g.drawLine(s / 2 - 7, y, s / 2 + 7,y);
        }

        g.setColor(new Color(150,80,255));
        g.fillOval(s / 2 - 5, s / 2 - 5, 10,10);
        g.setColor(new Color(200,150,255));
        g.fillOval(s / 2 - 2, s / 2 - 3,4,3);
        g.setColor(new Color(80,30,150));
        g.setStroke(new BasicStroke(1f));
        g.drawOval(s / 2 - 5, s / 2 -5,10,10);

        g.dispose();
        return img;
    }
    @Override
    public void interact() {
        gp.keyH.enterPressed = false;
        gp.cipherManager.openScroll(scrollId);
        // Scroll von der Map entfernen
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] == this) {
                gp.obj[gp.currentMap][i] = null;
                break;
            }
        }
    }
}
