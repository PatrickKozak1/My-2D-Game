package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;

    boolean showDebugTex = false;

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
        else if (gp.gameState == gp.dialogState){
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
                    //add later
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
    }

    public void pauseState(int code){
        if (code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
        }
    }

    public void dialogState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code){
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
        if (code  ==  KeyEvent.VK_UP){
            if (gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                gp.playSE(5);
            }
        }
        if (code  ==  KeyEvent.VK_LEFT){
            if (gp.ui.slotCol  != 0){
                gp.ui.slotCol--;
                gp.playSE(5);
            }
        }
        if (code  ==  KeyEvent.VK_DOWN){
            if (gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(5);
            }

        }
        if (code  ==  KeyEvent.VK_RIGHT){
            if (gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(5);
            }

        }

        if (code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }


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
              gp.retry();
              gp.playMusic(1);
          } else if (gp.ui.commandNum == 1) {
              gp.gameState = gp.titleState;
              gp.restart();

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
    }
}
