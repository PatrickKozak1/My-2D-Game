package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class OBJ_Gravestone extends Entity {

    GamePanel gp;
    public List<Entity> storedItems = new ArrayList<>();
    private int pulseCounter = 0;

    public OBJ_Gravestone(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Gravestone";
        type = type_obstacle;
        collision = true;
        down1 = generateSprite(false);
        down2 = generateSprite(true);
        up1 = down1;
        up2 = down2;
        left1 = down1;
        left2 = down2;
        right1 = down1;
        right2 = down2;

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = gp.tileSize -8;
        solidArea.height = gp.tileSize -20;
        solidAreaDefaultX  = solidArea.x;
        solidAreaDefaultY  = solidArea.y;
    }

    @Override
    public void interact(){
        if (storedItems.isEmpty()) {
            gp.ui.addMessage("The gravestone is empty.");
            return;
        }

        List<Entity> remaining = new ArrayList<>();
        int returned = 0;

        for (Entity item : storedItems) {
            if (gp.player.inventory.size() < gp.player.maxInventorySize){
                gp.player.inventory.add(item);
                returned++;
            }else {
                remaining.add(item);
            }
        }

        storedItems = remaining;

        if (returned > 0) {
            gp.playSE(13);
            gp.ui.addMessage("Recovered " + returned + " items from grave!");
        }

        if (storedItems.isEmpty()) {
            for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                if (gp.obj[gp.currentMap][i] == this) {
                    gp.obj[gp.currentMap][i] = null;
                    break;
                }
            }
        }else {
            gp.ui.addMessage(storedItems.size() + " items left - inventory full!");
        }
    }

    @Override
    public void draw(Graphics2D g2){
        if (!inCamera()) return;

        pulseCounter++;
        int sx = getScreenX();
        int sy = getScreenY();

        float pulse = 0.3f + (float) (Math.sin(pulseCounter * 0.05) * 0.2f);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, pulse));
        g2.setColor(new Color(150, 80, 255));
        g2.fillOval(sx + gp.tileSize / 2 - 14, sy - 6, 28, 28);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

        BufferedImage img = (pulseCounter % 60 < 30) ? down1 : down2;
        g2.drawImage(img,sx,sy, null);

        if (!storedItems.isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.setColor(new Color(255,215,0));
            g2.drawString(storedItems.size() + " items", sx + 2, sy + gp.tileSize - 2);
        }
    }

    private BufferedImage generateSprite(boolean glow) {
        int s = gp.tileSize;
        BufferedImage img = new BufferedImage(s,s, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (glow) {
            g.setColor(new Color(150, 80,255,50));
        }

        g.setColor(new Color(70,70,75));
        g.fillRoundRect(s / 2 - 13, s - 11, 26,7,4,4);
        g.setColor(new Color(40, 40, 45));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(s / 2 - 13, s - 11, 26,7,4,4);

        g.setColor(new Color(110,110,120));
        g.fillRoundRect(s / 2 - 11, 10, 22, s - 20, 10, 10);

        g.setColor(new Color(150,150,165,140));
        g.fillRoundRect(s / 2 - 9, 13, 7, s - 26, 4, 4);

        g.setColor(new Color(55,55,65));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(s / 2 - 11, 10, 22, s - 20, 10, 10);

        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.setColor(new Color(45, 45, 55));
        FontMetrics fm = g.getFontMetrics();
        g.drawString("RIP", s / 2 - fm.stringWidth("RIP") / 2, 24);

        g.setColor(new Color(65,65,75));
        g.setStroke(new BasicStroke(2f));
        g.drawLine(s / 2, 28, s / 2, s - 18);
        g.drawLine(s / 2 - 7, 34, s / 2 + 7, 34);

        if (glow) {
            g.setColor(new Color(180,130,255,140));
            g.fillOval(s / 2 - 3, 2, 6, 6);
            g.fillOval(s / 2 + 7, 6, 4, 4);
            g.fillOval(s / 2 - 11, 5, 4, 4);

        }

        g.dispose();
        return img;
    }
}
