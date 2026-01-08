package main;

import object.OBJ_Key;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font purisaB;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";


    public UI(GamePanel gp){
        this.gp = gp;


        try {
            InputStream is = getClass().getResourceAsStream("/font/GomePixel-DYJX1.otf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public  void draw (Graphics2D g2){

        this.g2 = g2;

        g2.setFont(purisaB);
        g2.setColor(Color.WHITE);

        // PLAY STATE
        if (gp.gameState == gp.playState){
            // Do playState stuff later

        }
        // PAUSE STATE
        if (gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
        // DIALOGUE STATA
        if (gp.gameState == gp.dialogState){
            drawDialogueScreen();
        }
    }

    public void drawPauseScreen() {


        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text,x,y );
    }

    public void drawDialogueScreen(){

        // WINDOW
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,26F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line:currentDialogue.split("\n")){
            g2.drawString(line, x,y);
            y += 40;

        }

    }

    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(0,0,0,190);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c=new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5,y +5, width-10, height-10,25,25);
    }

    public int getXForCenteredText(String text){

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}
