package entity;

import main.GamePanel;
import main.Quest;

import java.awt.*;
import java.util.Random;


public class NPC_OldMan extends Entity{

    private boolean introShown = false;

    public NPC_OldMan(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 30;

        dialogueSet = -1;

        getImage();
        setDialogue();
    }
    public void getImage(){
        up1 = setup("/npc/oldman_up_1",gp.tileSize,gp.tileSize);
        up2 = setup("/npc/oldman_up_2",gp.tileSize,gp.tileSize);
        down1 = setup("/npc/oldman_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("/npc/oldman_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("/npc/oldman_left_1",gp.tileSize,gp.tileSize);
        left2 = setup("/npc/oldman_left_2",gp.tileSize,gp.tileSize);
        right1 = setup("/npc/oldman_right_1",gp.tileSize,gp.tileSize);
        right2 = setup("/npc/oldman_right_2",gp.tileSize,gp.tileSize);

    }

    public void setDialogue(){
        dialogues[0][0] = "Hello, lad.";
        dialogues[0][1] = "So you've come to this island to \nfind the treasure?";
        dialogues[0][2] = "I used to be a great wizard but now... \nI'm a bit too old for taking an adventure";
        dialogues[0][3] = "Well , good luck on you.";

        dialogues[1][0] = "If you become tire rest at the water.";
        dialogues[1][1] = "However, the monsters reappear if you rest.\nI don't know why but that's how it works";
        dialogues[1][2] = "In any case, don't push yourself too hard.";

        dialogues[2][0] = "I wonder how to open that door...";

        dialogues[3][0] = "Those Red Slimes have been causing\ntrouble around here lately...";
        dialogues[3][1] = "Would you help an old man out?\nDefeat 5 Red Slimes for me!";
        dialogues[3][2] = "I'll reward you handsomely, I promise!";

        dialogues[4][0] = "Those slimes are still out there...";
        dialogues[4][1] = "Comme back when you've defeated \n5 Red Slimes!";

        dialogues[5][0] = "You did it! The slimes are gone!";
        dialogues[5][1] = "Here, take this as your reward.\nYou've earned it, lad!";

        dialogues[6][0] = "Thank you again for your help.";
        dialogues[6][1] = "You truly are a brave adventurer!";

    }

    public void setAction(){

        if (onPath == true) {
//            int goalCol = 10;
//            int goalRow = 10;
//            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
//            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
//
//            searchPath(goalCol,goalRow);
        }else  {
            actionLockCounter ++;

            if (actionLockCounter == 120){
                Random random = new Random();
                int i = random.nextInt(100)+1; // pick up a number from 1 to 100

                if (i <= 25){
                    direction = "up";
                }
                if (i > 25 && i <= 50){
                    direction = "down";
                }
                if (i > 50 && i <= 75){
                    direction = "left";
                }
                if (i > 75 && i <= 100){
                    direction = "right";
                }
                actionLockCounter = 0;
            }
        }


    }
    public void speak(){

        facePlayer();
        Quest q = gp.questManager.getQuest("slime_hunter");

        if (q.rewarded){
            startDialogue(this,6);

        }else if (q.completed) {
            startDialogue(this,5);
            gp.questManager.rewardQuest(q);
        }else if (q.accepted) {
            startDialogue(this,4);
        }else if (introShown) {
            startDialogue(this,3);
            gp.questManager.acceptQuest("slime_hunter");
        }else {
            introShown = true;
            startDialogue(this,0);
        }
    }

}
