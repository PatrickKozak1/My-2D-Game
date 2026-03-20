package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;

    public static final String objName = "Red Potion";

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_consumable;
        name = objName;
        value = 5;
        down1 = setup("/objects/potion_red",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nA Heals your life by " + value +".";
        price = 25;
        stackable = true;
    }

    public void setDialogue() {

        dialogues[0][0] ="You drink the " + name + "!\n"
                + "Your life has been recoverd by " + value + ".";

    }

    public boolean use (Entity entity){

       startDialogue(this,0);
        entity.life += value;
        gp.playSE(16);
        return true;
    }
}
