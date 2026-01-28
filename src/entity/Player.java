package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    int  standCounter = 0;
    boolean moving =   false;
    int pixelCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 1;
        solidArea.y = 1;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 46;
        solidArea.height = 46;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage(){
        up1 = setup("/player/boy_up_1.png");
        up2 = setup("/player/boy_up_2.png");
        down1 = setup("/player/boy_down_1.png");
        down2 = setup("/player/boy_down_2.png");
        left1 = setup("/player/boy_left_1.png");
        left2 = setup("/player/boy_left_2.png");
        right1 = setup("/player/boy_right_1.png");
        right2 = setup("/player/boy_right_2.png");
    }

    public void update(){
        if (life <= 0) {
            if (gp.gameState != gp.gameOverState) {
                gp.gameState = gp.gameOverState;
                gp.ui.commandNum = 0;
            }
            return;
        }


        if (moving == false){
            if (keyH.upPressed == true || keyH.downPressed == true
                    || keyH.leftPressed == true || keyH.rightPressed == true) {

                if (keyH.upPressed == true) {
                    direction = "up";
                } else if (keyH.downPressed == true) {
                    direction = "down";
                } else if (keyH.leftPressed == true) {
                    direction = "left";
                } else if (keyH.rightPressed == true) {
                    direction = "right";
                }

                // CHECK TILE COLLISION VOR der Bewegung
                collisionON = false;
                gp.cChecker.checkTile(this);

                // Check OBJECT COLLISION
                int objindex = gp.cChecker.checkObject(this, true);
                pickupObject(objindex);

                // CHECK NPC COLLISION
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);

                int mosterIndex = gp.cChecker.checkEntity(this, gp.monster);

                // CHECK EVENT
                gp.eHandler.checkEvent();
                gp.keyH.enterPressed = false;

                // Wenn KEINE Kollision, dann darf der Spieler sich bewegen
                if (collisionON == false){
                    moving = true;
                }
            }
            else {
                standCounter++;
                if (standCounter == 20){
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }

        if (moving == true){
            // Nochmal vor der Bewegung prüfen (für den Fall dass NPC sich bewegt hat)
            collisionON = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkEntity(this, gp.npc);

            // Nur bewegen wenn keine Kollision
            if (collisionON == false){
                switch (direction){
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            } else {
                // Wenn Kollision während Bewegung -> sofort stoppen
                moving = false;
                pixelCounter = 0;
            }

            spriteCounter++;
            if (spriteCounter > 12){
                if (spriteNum == 1){
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

            if (collisionON == false){
                pixelCounter += speed;
            }

            if (pixelCounter == 48){
                moving = false;
                pixelCounter = 0;
            }
        }
    }

    public void pickupObject(int i){
        if (i != 999){
            // TODO: Object pickup logic
        }
    }

    public void interactNPC(int i){
        if (i != 999){
            if (gp.keyH.enterPressed == true){
                gp.gameState = gp.dialogState;
                gp.npc[i].speak();
            }
        }

    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        switch (direction){
            case "up":
                if (spriteNum == 1){
                    image = up1;
                }
                if (spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1){
                    image = down1;
                }
                if (spriteNum == 2){
                    image = down2;
                }
                break;
            case  "left":
                if (spriteNum == 1){
                    image = left1;
                }
                if (spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1){
                    image = right1;
                }
                if (spriteNum == 2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, null);
        // g2.setColor(Color.red);
        // g2.drawRect(screenX + solidArea.x, screenY +solidArea.y,solidArea.width, solidArea.height);
    }


}