package main;

import entity.Entity;

public class Quest {

    public static final int TYPE_KILL = 0;
    public static final int TYPE_COLLECT = 1;

    public String id;
    public String title;
    public String description;
    public int type;
    public String targetName;
    public int targetCount;
    public int currentCount;
    public boolean accepted = false;
    public boolean completed = false;
    public boolean rewarded = false;

    // Belohnung
    public int rewardCoins = 0;
    public Entity rewardItem = null;

    public Quest(String id, String title, String description, int type, String targetName, int targetCount, int rewardCoins) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.targetName = targetName;
        this.targetCount = targetCount;
        this.rewardCoins = rewardCoins;
    }

    public String getProgressText() {
        return targetName + ": " + currentCount + "/" + targetCount;
    }

}
