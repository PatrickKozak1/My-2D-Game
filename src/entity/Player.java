package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    int  standCounter = 0;
    boolean moving =   false;
    int pixelCounter = 0;
    public boolean  attackCanceled =  false;


    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 6;
        solidArea.y = 9;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 33;
        solidArea.height = 34;



        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
//        worldX = gp.tileSize * 12;
//        worldY = gp.tileSize * 12;
        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = "down";

        // PLAYER STATUS
        level = 0;
        maxLife = 6;
        life = maxLife;
        maxMana =  4;
        mana = maxMana;
        ammo = 10;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 500;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
//        projectile = new OBJ_Rock(gp);
        attack = getAttack();
        defense = getDefense();
    }

    public void setDefaultPositions() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }

    public void restoreLifeAndMana() {
        life = maxLife;
        mana = maxMana;
        invincible = false;

    }

    public void setItems() {

        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));

    }
    private int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    private int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage(){
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize,gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize,gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize,gp.tileSize);
        left2 = setup("/player/boy_left_2",gp.tileSize,gp.tileSize);
        right1 = setup("/player/boy_right_1",gp.tileSize,gp.tileSize);
        right2 = setup("/player/boy_right_2",gp.tileSize,gp.tileSize);
    }
    public void getPlayerAttackImage(){
        if (currentWeapon.type == type_sword) {
            attackUp1 = setup("/player/boy_attack_up_1",gp.tileSize,gp.tileSize*2);
            attackUp2 = setup("/player/boy_attack_up_2",gp.tileSize,gp.tileSize*2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize,gp.tileSize*2);
            attackDown2 = setup("/player/boy_attack_down_2",gp.tileSize,gp.tileSize*2);
            attackLeft1 = setup("/player/boy_attack_left_1",gp.tileSize*2,gp.tileSize);
            attackLeft2 = setup("/player/boy_attack_left_2",gp.tileSize*2,gp.tileSize);
            attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize*2, gp.tileSize);
        }
        if (currentWeapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1",gp.tileSize,gp.tileSize*2);
            attackUp2 = setup("/player/boy_axe_up_2",gp.tileSize,gp.tileSize*2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize,gp.tileSize*2);
            attackDown2 = setup("/player/boy_axe_down_2",gp.tileSize,gp.tileSize*2);
            attackLeft1 = setup("/player/boy_axe_left_1",gp.tileSize*2,gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2",gp.tileSize*2,gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1",gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2",gp.tileSize*2, gp.tileSize);
        }

    }
    public void update(){

        if (attacking == true) {
            attacking();
        }


        if (moving == false){
            if (keyH.upPressed == true || keyH.downPressed == true
                    || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {

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

                // CHECK MONSTER COLLISION
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                contactMonster(monsterIndex);

                // CHECK INTERACTIVE TILE COLLISION
                int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);


                // CHECK EVENT
                gp.eHandler.checkEvent();

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

            collisionON = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.iTile);


            if (collisionON == false && keyH.enterPressed == false){
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

        if (keyH.enterPressed == true && attackCanceled == false){
            gp.playSE(22);
            attacking = true;
            spriteCounter = 0;
        }

        attackCanceled = false;
        gp.keyH.enterPressed = false;

        if (gp.keyH.shotKeyPressed == true && projectile.alive == false
                && shotAvailableCounter == 30 && projectile.haveResource(this) == true) {

            // SET DEFAULT COORDINATES, DIRECTION AND USER
            projectile.set(worldX,worldY,direction,true,this);

            //  SUBTRACT THE COST (MANA, AMMO ETC.)
            projectile.subtractResource(this);

            // CHECK VACANCY
            for (int i = 0; i < gp.projectile[1].length; i++) {
                if (gp.projectile[gp.currentMap][i] == null) {
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;

            gp.playSE(2);
        }

        //This needs to be outside of  key if statement!
        if (invincible == true){
            invincibleCounter++;
            if (invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        if (life > maxLife) {
            life = maxLife;
        }
        if (mana > maxMana) {
            mana = maxMana;
        }
        if (life <= 0){
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(11);
        }
    }

    public void attacking(){

        spriteCounter++;

        if (spriteCounter <= 5){
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;


            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust players worldX/Y for the attackArea
            switch (direction){
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // Check monster collision with the updated worldX, worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack,currentWeapon.knockBackPower);

            int iTileIndex = gp.cChecker.checkEntity(this,gp.iTile);
            damageInteractiveTile(iTileIndex);

            int projectTileIndex = gp.cChecker.checkEntity(this, gp.projectile);
            damageProjectTiel(projectTileIndex);

            // After checking collision, resotre the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if (spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    private void damageProjectTiel(int i) {

        if (i != 999){
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile,projectile);
        }

    }

    private void damageInteractiveTile(int i) {
        if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true
                && gp.iTile[gp.currentMap][i].isCorrectItem(this) == true && gp.iTile[gp.currentMap][i].invincible == false){
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible =  true;

            // Generate particle
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            if (gp.iTile[gp.currentMap][i].life == 0){
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyFrom();
            }

        }
    }

    public void damageMonster(int i, int attack, int knockBackPower) {
        if (i != 999){
            if (gp.monster[gp.currentMap][i].invincible == false){

                gp.playSE(12);

                if (knockBackPower > 0) {
                    knockBack(gp.monster[gp.currentMap][i], knockBackPower);
                }

                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if (damage < 0){
                    damage = 0;
                }

                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " Damage");

                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if (gp.monster[gp.currentMap][i].life <= 0){
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage(" Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.addMessage(" Exp " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void checkLevelUp(){
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSE(13);
            gp.gameState = gp.dialogState;
            gp.ui.currentDialogue = "You are level " + level + "now!\n"
                    + "You feel stronger!";
        }
    }

    public void selectItem(){

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol,gp.ui.playerSlotRow);

        if (itemIndex < inventory.size()) {

            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentWeapon = selectedItem;
                attack  = getAttack();
                getPlayerAttackImage();
            }
            if (selectedItem.type ==  type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type == type_consumable){

               selectedItem.use(this);
               inventory.remove(itemIndex);
            }
        }
    }

    public void pickupObject(int i){
        if (i != 999){

            // PICKUP ONLY ITEMS
            if (gp.obj[gp.currentMap][i].type == type_pickupOnly){

                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;

            }

            // INVENTORY ITEM
            else {

                String text;

                if (inventory.size() != maxInventorySize){

                    inventory.add(gp.obj[gp.currentMap][i]);
                    gp.playSE(4);
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                }else {
                    text = "You cannot carry any more!";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i]  = null;
            }

        }
    }

    public void interactNPC(int i) {

        if (gp.keyH.enterPressed == true) {

            if (i != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogState;
                gp.npc[gp.currentMap][i].speak();

            }
        }
    }

    public void contactMonster(int i){
        if (i != 999){

            if (invincible == false && gp.monster[gp.currentMap][i].dying == false){
                gp.playSE(17);
                int damage =  gp.monster[gp.currentMap][i].attack - defense;
                if (damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible =  true;
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction){
            case "up":
                if (attacking == false) {
                    if (spriteNum == 1){image = up1;}
                    if (spriteNum == 2){image = up2; }
                }
                if (attacking == true){
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1){image = attackUp1;}
                    if (spriteNum == 2){image = attackUp2; }
                }
                break;
            case "down":
                if (attacking == false){
                    if (spriteNum == 1){image = down1;}
                    if (spriteNum == 2){image = down2;}
                }
                if (attacking == true){
                    if (spriteNum == 1){image = attackDown1;}
                    if (spriteNum == 2){image = attackDown2;}
                }
                break;
            case  "left":
                if (attacking == false){

                    if (spriteNum == 1){image = left1;}
                    if (spriteNum == 2){image = left2;}
                }
                if (attacking == true){
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1){image = attackLeft1;}
                    if (spriteNum == 2){image = attackLeft2;}
                }

                break;
            case "right":
                if (attacking == false){
                    if (spriteNum == 1){image = right1;}
                    if (spriteNum == 2){image = right2;}
                }
                if (attacking == true){
                    if (spriteNum == 1){image = attackRight1;}
                    if (spriteNum == 2){image = attackRight2;}
                }

                break;
        }

        if (invincible  == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Reset  alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //DEBUG
//        g2.setFont(new Font("Arial", Font.PLAIN,26));
//        g2.setColor(Color.white);
//        g2.drawString("Invincible:"+invincibleCounter,10,400);
//        g2.drawRect(screenX + solidArea.x, screenY +solidArea.y,solidArea.width, solidArea.height);
//        g2.setColor(Color.RED);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }

    public void knockBack(Entity entity, int knockBackPower) {

        entity.direction = direction;
        entity.speed += knockBackPower;
        entity.knockBack = true;

    }


}