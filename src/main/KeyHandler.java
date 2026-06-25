package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed,
            enterPressed, shotKeyPressed, spacePressed;
    boolean showDebugTex = false;
    public boolean godModeOn = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState == gp.titleState) {
            titleState(code);
        } else if (gp.gameState == gp.playState) {
            playState(code);
        } else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        } else if (gp.gameState == gp.dialogState || gp.gameState == gp.cutsceneState) {
            dialogState(code);
        } else if (gp.gameState == gp.characterState) {
            characterState(code);
        } else if (gp.gameState == gp.optionsSate) {
            optionsState(code);
        } else if (gp.gameState == gp.gameOverState) {
            gameOverState(code);
        } else if (gp.gameState == gp.tradeState) {
            tradeState(code);
        } else if (gp.gameState == gp.mapState) {
            mapState(code);
        } else if (gp.gameState == gp.tutorialState) {
            tutorialState(code);
        } else if (gp.gameState == gp.loreState) {
            loreState(code);
        } else if (gp.gameState == gp.cipherState) {
            cipherState(code);
        }
    }

    private void cipherState(int code) {
        if (gp.cipherManager.decoderOpen) {
            if (code == KeyEvent.VK_1) {
                gp.cipherManager.decoderTab = 0;
                gp.playSE(5);
            }
            if (code == KeyEvent.VK_2) {
                gp.cipherManager.decoderTab = 1;
                gp.playSE(5);
            }
            if (code == KeyEvent.VK_UP) {
                gp.cipherManager.selectedScrollIndex--;
                if (gp.cipherManager.selectedScrollIndex < 0)
                    gp.cipherManager.selectedScrollIndex = 0;
                gp.playSE(5);
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.cipherManager.selectedScrollIndex++;
                int max = gp.cipherManager.getCollectedCount() - 1;
                if (gp.cipherManager.selectedScrollIndex > max)
                    gp.cipherManager.selectedScrollIndex = max;
                gp.playSE(5);
            }
            // R = Scroll nochmal lesen
            if (code == KeyEvent.VK_R && gp.cipherManager.decoderTab == 1) {
                java.util.List<CipherScroll> col = new java.util.ArrayList<>();
                for (CipherScroll s : gp.cipherManager.scrolls)
                    if (s.collected) col.add(s);
                if (!col.isEmpty() &&
                        gp.cipherManager.selectedScrollIndex < col.size()) {
                    gp.cipherManager.readAgain(
                            col.get(gp.cipherManager.selectedScrollIndex));
                }
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.cipherManager.close();
            }
        } else {
            if (code == KeyEvent.VK_ENTER) {
                gp.cipherManager.nextPage();
            }
            if (code == KeyEvent.VK_T) {
                gp.cipherManager.toggleDecode();
            }
            if (code == KeyEvent.VK_N) {
                gp.cipherManager.openDecoder();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.cipherManager.close();
            }
        }
    }

    public void loreState(int code) {
        if (gp.loreManager.journalOpen) {
            if (code == KeyEvent.VK_UP) {
                gp.loreManager.journalIndex--;
                if (gp.loreManager.journalIndex < 0)
                    gp.loreManager.journalIndex = gp.loreManager.collectedNotes.size() - 1;
                gp.playSE(5);
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.loreManager.journalIndex++;
                if (gp.loreManager.journalIndex >= gp.loreManager.collectedNotes.size())
                    gp.loreManager.journalIndex = 0;
                gp.playSE(5);
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.loreManager.openSelectedJournalNote();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.loreManager.closeReading();
            }
        } else {
            if (code == KeyEvent.VK_ENTER) {
                gp.loreManager.nextPage();
            }
            if (code == KeyEvent.VK_BACK_SPACE) {
                gp.loreManager.prevPage();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.loreManager.closeReading();
            }
        }
    }

    public void tutorialState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            if (gp.tManager.itemTutorialActive) {
                gp.tManager.dismissItemTutorial();
            } else {
                gp.tManager.nextMainPhase();
            }
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.tManager.skippAll();
        }
    }

    private void mapState(int code) {
        if (code == KeyEvent.VK_M) {
            gp.gameState = gp.playState;
        }
    }

    private void tradeState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (gp.ui.subState == 0) {
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 3;
                gp.playSE(5);
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3) gp.ui.commandNum = 0;
                gp.playSE(5);
            }
        }
        if (gp.ui.subState == 1) {
            npcInventory(code);
            if (code == KeyEvent.VK_ESCAPE) gp.ui.subState = 0;
        }
        if (gp.ui.subState == 2) {
            playerInventory(code);
            if (code == KeyEvent.VK_ESCAPE) gp.ui.subState = 0;
        }
        if (gp.ui.subState == 3) {
            playerInventory(code);
            if (code == KeyEvent.VK_ESCAPE) gp.ui.subState = 0;
            if (code == KeyEvent.VK_ENTER) gp.ui.tryUpgrade();
        }
    }

    public void playerInventory(int code) {
        if (code == KeyEvent.VK_UP) {
            if (gp.ui.playerSlotRow != 0) {
                gp.ui.playerSlotRow--;
                gp.playSE(5);
            }
        }
        if (code == KeyEvent.VK_LEFT) {
            if (gp.ui.playerSlotCol != 0) {
                gp.ui.playerSlotCol--;
                gp.playSE(5);
            }
        }
        if (code == KeyEvent.VK_DOWN) {
            if (gp.ui.playerSlotRow != 3) {
                gp.ui.playerSlotRow++;
                gp.playSE(5);
            }
        }
        if (code == KeyEvent.VK_RIGHT) {
            if (gp.ui.playerSlotCol != 4) {
                gp.ui.playerSlotCol++;
                gp.playSE(5);
            }
        }
    }

    public void npcInventory(int code) {
        if (code == KeyEvent.VK_UP) {
            if (gp.ui.npcSlotRow != 0) {
                gp.ui.npcSlotRow--;
                gp.playSE(5);
            }
        }
        if (code == KeyEvent.VK_LEFT) {
            if (gp.ui.npcSlotCol != 0) {
                gp.ui.npcSlotCol--;
                gp.playSE(5);
            }
        }
        if (code == KeyEvent.VK_DOWN) {
            if (gp.ui.npcSlotRow != 3) {
                gp.ui.npcSlotRow++;
                gp.playSE(5);
            }
        }
        if (code == KeyEvent.VK_RIGHT) {
            if (gp.ui.npcSlotCol != 4) {
                gp.ui.npcSlotCol++;
                gp.playSE(5);
            }
        }
    }

    public void titleState(int code) {
        if (gp.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                gp.playSE(5);
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                gp.playSE(5);
                if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.tutorialState;
                    gp.tManager.startTutorial();
                    gp.playMusic(1);
                }
                if (gp.ui.commandNum == 1) {
                    gp.saveLoad.load();
                    if (gp.player.life <= 0) gp.player.life = gp.player.maxLife;
                    gp.gameState = gp.playState;
                    gp.playMusic(1);
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        } else if (gp.ui.titleScreenState == 1) {
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 3;
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3) gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1) {
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2) {
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 3) {
                    gp.ui.titleScreenState = 0;
                }
            }
        }
    }

    public void playState(int code) {
        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_P) gp.gameState = gp.pauseState;
        if (code == KeyEvent.VK_ENTER) enterPressed = true;
        if (code == KeyEvent.VK_F) shotKeyPressed = true;
        if (code == KeyEvent.VK_SPACE) spacePressed = true;
        if (code == KeyEvent.VK_ESCAPE) gp.gameState = gp.optionsSate;
        if (code == KeyEvent.VK_M) gp.gameState = gp.mapState;
        if (code == KeyEvent.VK_F5) gp.weatherManager.cycleWeather();

        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
            gp.tManager.triggerInventoryTutorial();
        }
        if (code == KeyEvent.VK_X) {
            gp.map.miniMapOn = !gp.map.miniMapOn;
        }
        if (code == KeyEvent.VK_J) {
            gp.loreManager.openJournal();
        }
        // N = Decoder öffnen (D war Kollision mit Bewegung)
        if (code == KeyEvent.VK_N) {
            gp.cipherManager.openDecoder();
        }

        // DEBUG
        if (code == KeyEvent.VK_T) {
            showDebugTex = !showDebugTex;
        }
        if (code == KeyEvent.VK_I) {
            switch (gp.currentMap) {
                case 0:
                    gp.tileM.loadMap("/maps/worldV2.txt", 0);
                    break;
                case 1:
                    gp.tileM.loadMap("/maps/indoor01.txt", 1);
                    break;
            }
        }
        if (code == KeyEvent.VK_G) {
            godModeOn = !godModeOn;
        }
    }

    public void pauseState(int code) {
        if (code == KeyEvent.VK_P) gp.gameState = gp.playState;
    }

    public void dialogState(int code) {
        if (code == KeyEvent.VK_ENTER) enterPressed = true;
    }

    public void characterState(int code) {
        if (gp.tManager.inventoryTutorialActive) {
            if (code == KeyEvent.VK_ENTER) gp.tManager.nextInvPhase();
            if (code == KeyEvent.VK_ESCAPE) gp.tManager.skippAll();
            return;
        }
        if (code == KeyEvent.VK_C) gp.gameState = gp.playState;
        if (code == KeyEvent.VK_ENTER) gp.player.selectItem();
        playerInventory(code);
    }

    public void gameOverState(int code) {
        if (code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) gp.ui.commandNum = 1;
            gp.playSE(5);
        }
        if (code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 1) gp.ui.commandNum = 0;
            gp.playSE(5);
        }
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.resetGame(false);
                gp.playMusic(1);
            } else if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.resetGame(true);
            }
        }
    }

    public void optionsState(int code) {
        if (code == KeyEvent.VK_ESCAPE) gp.gameState = gp.playState;
        if (code == KeyEvent.VK_ENTER) enterPressed = true;

        int maxCommandNum = 0;
        switch (gp.ui.subState) {
            case 0:
                maxCommandNum = 5;
                break;
            case 3:
                maxCommandNum = 1;
                break;
        }
        if (code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            gp.playSE(5);
            if (gp.ui.commandNum < 0) gp.ui.commandNum = maxCommandNum;
        }
        if (code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            gp.playSE(5);
            if (gp.ui.commandNum > maxCommandNum) gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_LEFT && gp.ui.subState == 0) {
            if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                gp.music.volumeScale--;
                gp.music.chackVolume();
                gp.playSE(5);
            }
            if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                gp.se.volumeScale--;
                gp.playSE(5);
            }
        }
        if (code == KeyEvent.VK_RIGHT && gp.ui.subState == 0) {
            if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                gp.music.volumeScale++;
                gp.music.chackVolume();
                gp.playSE(5);
            }
            if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                gp.se.volumeScale++;
                gp.playSE(5);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_F) shotKeyPressed = false;
        if (code == KeyEvent.VK_ENTER) enterPressed = false;
        if (code == KeyEvent.VK_SPACE) spacePressed = false;
    }
}