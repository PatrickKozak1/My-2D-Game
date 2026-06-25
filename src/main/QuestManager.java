package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestManager {

    GamePanel gp;
    public List<Quest> quests = new ArrayList<>();

    public QuestManager(GamePanel gp) {
        this.gp = gp;
        setupQuests();
    }

    private void setupQuests() {
        quests.add(new Quest(
                "slime_hunter",
                "Slime Hunter",
                "Defeat 5 Red Slimes for the Old Man.",
                Quest.TYPE_KILL,
                "Red Slime",
                5,
                150
        ));
    }

    public Quest getQuest(String id) {
        for (Quest q : quests) if (q.id.equals(id)) return q;
        return null;
    }

    public void acceptQuest(String id) {
        Quest q = getQuest(id);
        if (q != null && !q.accepted) {
            q.accepted = true;
            gp.ui.addMessage("Quest accepted: " + q.title + "!");
        }
    }

    public void notifyKill(String monsterName) {
        for (Quest q : quests) {
            if (!q.accepted || q.completed) continue;
            if (q.type == Quest.TYPE_KILL && q.targetName.equals(monsterName)) {
                q.currentCount++;
                if (q.currentCount >= q.targetCount) {
                    q.completed = true;
                    gp.ui.addMessage("Quest completed: " + q.title + "!");
                    gp.ui.addMessage("Return to the Old Man for you reward.");
                } else {
                    gp.ui.addMessage(q.title + ":" + q.currentCount + "/" + q.targetCount);
                }
            }
        }
    }

    public void notifyCollect(String itemName) {
        for (Quest q : quests) {
            if (!q.accepted || q.completed) continue;
            if (q.type == Quest.TYPE_COLLECT && q.targetName.equals(itemName)) {
                q.currentCount++;
                if (q.currentCount >= q.targetCount) {
                    q.completed = true;
                    gp.ui.addMessage("Quest completed: " + q.title + "!");
                } else {
                    gp.ui.addMessage(q.title + ":" + q.currentCount + "/" + q.targetCount);
                }
            }
        }
    }

    public void rewardQuest(Quest q) {
        if (q == null || q.rewarded) return;
        q.rewarded = true;
        gp.player.coin += q.rewardCoins;
        if (q.rewardItem != null) gp.player.inventory.add(q.rewardItem);
        gp.ui.addMessage("Reward: +" + q.rewardCoins + " Coins!");
        gp.playSE(13);
    }

    public void draw(Graphics2D g2) {
        boolean hasActive = false;
        for (Quest q : quests) {
            if (q.accepted && !q.completed) {
                hasActive = true;
                break;
            }
        }
        if (!hasActive) return;

        int boxW = gp.tileSize * 7;
        int boxX = gp.screenWidth - boxW - gp.tileSize / 2;
        int boxY = gp.tileSize / 2;
        int lineH = 28;

        int lineCount = 1;
        for (Quest q : quests) if (q.accepted && !q.rewarded) lineCount += 2;
        int boxH = 20 + lineCount * lineH;

        gp.ui.drawSubWindow(boxX, boxY, boxW, boxH);

        g2.setFont(gp.ui.purisaB.deriveFont(Font.BOLD, 18f));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString("QUESTS", boxX + 20, boxY + 28);

        int ty = boxY + 28 + lineH;
        for (Quest q : quests) {
            if (!q.accepted || q.rewarded) continue;
            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 16f));
            g2.setColor(Color.WHITE);
            g2.drawString(q.title, boxX + 20, ty);
            ty += lineH;

            g2.setFont(gp.ui.purisaB.deriveFont(Font.PLAIN, 15f));
            if (q.completed) {
                g2.setColor(new Color(100, 220, 100));
                g2.drawString("Return to the Old Man", boxX + 24, ty);
            } else {
                g2.setColor(new Color(180, 180, 180));
                g2.drawString(q.getProgressText(), boxX + 24, ty);
            }
            ty += lineH + 4;
        }
    }
}
