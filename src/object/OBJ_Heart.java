package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Set;

public class OBJ_Heart extends Entity {

    public OBJ_Heart(GamePanel gp) {

        super(gp);

        name = "Heart";
        image = setup("/objects/heart_full.png");
        image2 = setup("/objects/heart_half.png");
        image3 = setup("/objects/heart_blank.png");

    }
}
