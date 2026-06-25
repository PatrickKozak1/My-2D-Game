package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

    GamePanel gp;
    public static final String objName = "Chest";

    // Liste statt einzelnem loot
    private java.util.List<Entity> lootList = new java.util.ArrayList<>();

    public OBJ_Chest(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_obstacle;
        name = objName;
        image = setup("/objects/chest", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/chest_opened", gp.tileSize, gp.tileSize);
        down1 = image;
        collision = true;

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void setLoot(Entity loot) {
        this.lootList.add(loot);  // hinzufügen statt überschreiben
        setDialogue();
    }

    public void setDialogue() {
        // Namen aller Items zusammenbauen
        String names = lootList.stream()
                .map(e -> e.name)
                .collect(java.util.stream.Collectors.joining(" and "));

        dialogues[0][0] = "You opened the chest and find " + names + "!\n...But you cannot carry any more!";
        dialogues[1][0] = "You opened the chest and find " + names + "!\nYou obtain them!";
    }

    public void interact() {
        if (!opened) {
            gp.playSE(21);

            // erst nur checken ob alle Platz haben
            boolean canObtainAll = true;
            for (Entity loot : lootList) {
                if (!gp.player.canObtainItem(loot)) {
                    canObtainAll = false;
                    break;
                }
            }

            if (!canObtainAll) {
                startDialogue(this, 0);
            } else {
                startDialogue(this, 1);
                down1 = image2;
                opened = true;
            }
        } else {
            startDialogue(this, 2);
        }
    }
}