package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.desktop.SystemEventListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements  Runnable {

    // SCREEN SETTINGS
    final  int originalTileSize = 16; // 16*16 tile
    final int scale = 3;

    public final int tileSize  = originalTileSize * scale; //48*48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixles
    public final int screenHeight = tileSize * maxScreenRow; // 575 pixles

    //WORLD SETTINGD
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;


    //FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player( this,keyH);
    public Entity obj[] = new Entity[25];
    public Entity npc[] = new Entity[10];
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int gameOverState = 6;

    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){

        aSetter.setObject();
        aSetter.setNpc();
        playMusic(1);
        gameState = titleState;
    }


    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
//    public void run() {
//
//        double drawInterval = 1000000000 / FPS; // 0.0166666 seconds
//        double nextDrawTime =  System.nanoTime() +drawInterval;
//
//        while(gameThread != null){
//
//            update();
//
//            repaint();
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime/1000000;
//
//                if (remainingTime  < 0){
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long)remainingTime);
//
//                nextDrawTime += drawInterval;
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
    public void run(){

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime -lastTime);
            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000){
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    // In GamePanel.java
    public void retry() {
        player.setDefaultValues(); // Setzt Leben, Position etc. des Spielers zurück

         aSetter.setObject();
         aSetter.setNpc();

        gameState = playState;
        ui.commandNum = 0;
    }


    public  void update(){

        if (gameState == playState){
            // PLAYER
            player.update();
            // NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null){
                    npc[i].update();
                }
            }
            
        }
        if (gameState == pauseState){
            // nothing
        }

        if (gameState == gameOverState) {
            if (keyH.upPressed == true) {
                ui.commandNum--;
                if (ui.commandNum < 0) {
                    ui.commandNum = 1;
                }
                keyH.upPressed = false;
            }
            if (keyH.downPressed == true) {
                ui.commandNum++;
                if (ui.commandNum > 1) {
                    ui.commandNum = 0;
                }
                keyH.downPressed = false;
            }
            if (gameState == gameOverState) {
                if (keyH.upPressed == true) {
                    ui.commandNum--;
                    if (ui.commandNum < 0) {
                        ui.commandNum = 1;
                    }
                    keyH.upPressed = false;
                }
                if (keyH.downPressed == true) {
                    ui.commandNum++;
                    if (ui.commandNum > 1) {
                        ui.commandNum = 0;
                    }
                    keyH.downPressed = false;
                }
                if (keyH.enterPressed == true) {
                    if (ui.commandNum == 0) {
                        retry();
                    } else if (ui.commandNum == 1) {
                        System.exit(0);
                    }
                    keyH.enterPressed = false;
                }
            }
        }

    }
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime == true){
            drawStart = System.nanoTime();
        }

        // Title SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        }
        // OTHERS
        else {
            // Title
            tileM.draw(g2);

            //ADD ENTITIES TO THE LIST
            entityList.add(player);

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null){
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null){
                    entityList.add(obj[i]);
                }
            }

            // SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            // DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            // EMPTY ENTITY LIST
            for (int i = 0; i < entityList.size(); i++) {
                entityList.remove(i);
            }

            // UI
            ui.draw(g2);
        }



        if (keyH.checkDrawTime == true){
            long drawEnd =  System.nanoTime();
            long passed = drawEnd  - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: "+ passed, 10,400);
            System.out.println("Draw Time: " + passed);;
        }


        g2.dispose();
    }
    public void playMusic(int i){
//        music.setFile(i);
//        music.play();
//        music.loop();
    }

    public void stopMusic(){
//        music.stop();
    }
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
