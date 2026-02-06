package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("/objects/potion_red",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nA Heals your life by " + value +".";
    }

    public void use (Entity entity){

        gp.gameState = gp.dialogState;
        gp.ui.currentDialogue = "You drink the " + name + "!\n"
                + "Your life has been recoverd by " + value + ".";
        entity.life += value;
        gp.playSE(16);
    }
}
