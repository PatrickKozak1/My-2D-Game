package main;

import java.util.Random;

public class CameraShake {

    private int duration = 0;
    private float intensity = 0;
    public int offsetX = 0;
    public int offsetY = 0;
    private final Random rand = new Random();

    public void shake(int duration, float intensity) {
        if (duration > this.duration) this.duration = duration;
        if (intensity > this.intensity) this.intensity = intensity;
    }

    public void update() {
        if (duration > 0) {
            float fade = Math.min(1f, (float) duration / 20f);
            float curr = intensity * fade;
            offsetX = (int) ((rand.nextFloat() * 2f - 1f) * curr);
            offsetY = (int) ((rand.nextFloat() * 2f - 1f) * curr);
            duration--;
        } else {
            offsetX = 0;
            offsetY = 0;
        }
    }

    public boolean isActive() {
        return duration > 0;
    }

}
