package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed, spacePressed;

    boolean showDebugTex = false;

    public boolean godModeOn = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // TITLE STATE
        if (gp.gameState == gp.titleState){
            titleState(code);
        }

        // PLAY STATE
        else if (gp.gameState == gp.playState){
            playState(code);
        }
        // PAUSE STATE
        else if (gp.gameState == gp.pauseState){
            pauseState(code);
        }

        // DIALOGUE STATE
        else if (gp.gameState == gp.dialogState ||gp.gameState == gp.cutsceneState){
            dialogState(code);
        }

        //  CHARACTER STATE
        else if (gp.gameState == gp.characterState){
            characterState(code);
        }
        // OPTIONS STATE
        else if (gp.gameState == gp.optionsSate){
            optionsState(code);
        }
        // GAME  OVER STATE
        else if (gp.gameState == gp.gameOverState){
            gameOverState(code);
        }
        // TRADE STATE
        else if (gp.gameState == gp.tradeState){
            tradeState(code);
        }

        else if (gp.gameState == gp.mapState){
            mapState(code);
        }


    }

    private void mapState(int code) {
        if (code == KeyEvent.VK_M){
            gp.gameState = gp.playState;
        }
    }

    private void tradeState(int code) {

        if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        if (gp.ui.subState == 0){
            if (code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
                gp.playSE(5);
            }
            if (code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
                gp.playSE(5);
            }
        }
        if (gp.ui.subState == 1){
            npcInventory(code);
            if (code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }

        if (gp.ui.subState == 2){
            playerInventory(code);
            if (code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }
    }

    public void playerInventory(int code){
        if (code  ==  KeyEvent.VK_UP){
            if (gp.ui.playerSlotRow != 0){
                gp.ui.playerSlotRow--;
                gp.playSE(5);
            }
        }

        if (code  ==  KeyEvent.VK_LEFT){
            if (gp.ui.playerSlotCol  != 0){
                gp.ui.playerSlotCol--;
                gp.playSE(5);
            }
        }
        if (code  ==  KeyEvent.VK_DOWN){
            if (gp.ui.playerSlotRow != 3){
                gp.ui.playerSlotRow++;
                gp.playSE(5);
            }

        }
        if (code  ==  KeyEvent.VK_RIGHT){
            if (gp.ui.playerSlotCol != 4){
                gp.ui.playerSlotCol++;
                gp.playSE(5);
            }

        }
    }
    public void npcInventory(int code){
        if (code  ==  KeyEvent.VK_UP){
            if (gp.ui.npcSlotRow != 0){
                gp.ui.npcSlotRow--;
                gp.playSE(5);
            }
        }
        if (code  ==  KeyEvent.VK_LEFT){
            if (gp.ui.npcSlotCol  != 0){
                gp.ui.npcSlotCol--;
                gp.playSE(5);
            }
        }
        if (code  ==  KeyEvent.VK_DOWN){
            if (gp.ui.npcSlotRow != 3){
                gp.ui.npcSlotRow++;
                gp.playSE(5);
            }

        }
        if (code  ==  KeyEvent.VK_RIGHT){
            if (gp.ui.npcSlotCol != 4){
                gp.ui.npcSlotCol++;
                gp.playSE(5);
            }

        }
    }

    public void titleState(int code){
        if (gp.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                gp.playSE(5);
                if (gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;

                }
            }
            if (code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                gp.playSE(5 );
                if (gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER){
                if (gp.ui.commandNum == 0){
                    gp.gameState = gp.playState;
                    gp.playMusic(1);
                }
                if (gp.ui.commandNum == 1){
                    gp.saveLoad.load();
                    // Sicherstellen dass Leben nicht 0 ist nach dem Laden
                    if (gp.player.life <= 0) {
                        gp.player.life = gp.player.maxLife;
                    }
                    gp.gameState = gp.playState;
                    gp.playMusic(1);
                }
                if (gp.ui.commandNum == 2){
                    System.exit(0);
                }
            }
        }      else if (gp.ui.titleScreenState == 1) {
            if (code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0){
                    gp.ui.commandNum = 3;
                }
            }
            if (code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3){
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER){
                if (gp.ui.commandNum == 0){
                    System.out.println("Do some fighter specific stuff");
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1){
                    System.out.println("Do some thief specific stuff");
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2){
                    System.out.println("Do some sorcerer specific stuff");
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 3){
                    gp.ui.titleScreenState  = 0;
                }
            }
        }
    }

    public void playState(int code){
        if (code == KeyEvent.VK_W){
            upPressed = true;
        }
        if (code == KeyEvent.VK_S){
            downPressed = true;
        }

        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }

        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }

        if (code == KeyEvent.VK_P){
            gp.gameState = gp.pauseState;
        }

        if (code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
        }

        if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        if (code == KeyEvent.VK_F){
            shotKeyPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.optionsSate;
        }
        if (code == KeyEvent.VK_M){
            gp.gameState = gp.mapState;
        }

        if (code == KeyEvent.VK_X){
            if (gp.map.miniMapOn == false){
                gp.map.miniMapOn = true;
            }
            else {
                gp.map.miniMapOn = false;
            }
        }
        if (code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }

        // DEBUG
        if (code == KeyEvent.VK_T){
            if (showDebugTex == false){
                showDebugTex = true;
            } else if (showDebugTex == true) {
                 showDebugTex = false;
            }
        }
        if (code == KeyEvent.VK_I){
            switch (gp.currentMap){
                case 0:  gp.tileM.loadMap("/maps/worldV2.txt",0); break;
                case 1:  gp.tileM.loadMap("/maps/indoor01.txt",1); break;
            }

        }
        if (code == KeyEvent.VK_G){
            if (godModeOn == false){
                godModeOn = true;
            } else if (godModeOn == true) {
                godModeOn = false;
            }
        }
    }

    public void pauseState(int code){
        if (code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
        }
    }

    public void dialogState(int code){
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
    }

    public void characterState(int code){
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }

        playerInventory(code);
    }

    public void gameOverState(int code){
      if (code == KeyEvent.VK_UP) {
          gp.ui.commandNum--;
          if (gp.ui.commandNum <0){
              gp.ui.commandNum = 1;
          }
          gp.playSE(5);
      }
      if (code == KeyEvent.VK_DOWN) {
          gp.ui.commandNum++;
          if (gp.ui.commandNum > 1){
               gp.ui.commandNum = 0;
          }
          gp.playSE(5);
      }
      if (code == KeyEvent.VK_ENTER){
          if (gp.ui.commandNum == 0){
              gp.gameState =  gp.playState;
              gp.resetGame(false);
              gp.playMusic(1);
          } else if (gp.ui.commandNum == 1) {
              gp.gameState = gp.titleState;
              gp.resetGame(true);

          }
      }
    }

    public void optionsState(int code ){
        if (code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch (gp.ui.subState) {
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }

        if (code == KeyEvent.VK_UP){
            gp.ui.commandNum--;
            gp.playSE(5);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if (code == KeyEvent.VK_DOWN){
            gp.ui.commandNum++;
            gp.playSE(5);
            if (gp.ui.commandNum > maxCommandNum){
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_LEFT){
            if (gp.ui.subState == 0){
                if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0){
                    gp.music.volumeScale--;
                    gp.music.chackVolume();
                    gp.playSE(5);
                }
                if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0){
                    gp.se.volumeScale--;
                    gp.playSE(5);
                }
            }


        }
        if (code == KeyEvent.VK_RIGHT){
            if (gp.ui.subState == 0){
                if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5){
                    gp.music.volumeScale++;
                    gp.music.chackVolume();
                    gp.playSE(5);
                }
                if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5){
                    gp.se.volumeScale++;
                    gp.playSE(5);
                }


            }
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }

        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F){
            shotKeyPressed = false;
        }
        if (code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
        if (code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }
    }
}
