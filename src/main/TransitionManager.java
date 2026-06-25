package main;

import java.awt.*;

public class TransitionManager {

    GamePanel gp;

    public enum Phase {IDLE, FADE_OUT, FADE_IN}

    public Phase phase = Phase.IDLE;
    private float alpha = 0f;
    private float speed = 0.05f;
    private Runnable onMidPoint;

    public TransitionManager(GamePanel gp) {
        this.gp = gp;
    }

    public void trigger(Runnable onMidpoint) {
        trigger(0.05f, onMidpoint);
    }

    public void trigger(float speed, Runnable onMidPoint) {
        if (phase != Phase.IDLE) return;
        this.speed = speed;
        this.onMidPoint = onMidPoint;
        alpha = 0f;
        phase = Phase.FADE_OUT;
    }

    public void update() {
        if (phase == Phase.FADE_OUT) {
            alpha += speed;
            if (alpha >= 1f) {
                alpha = 1f;
                if (onMidPoint != null) {
                    onMidPoint.run();
                    onMidPoint = null;
                }
                phase = Phase.FADE_IN;
            }
        } else if (phase == Phase.FADE_IN) {
            alpha -= speed;
            if (alpha <= 0f) {
                alpha = 0f;
                phase = Phase.IDLE;
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (phase == Phase.IDLE) return;
        float a = Math.min(1f, Math.max(0f, alpha));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public boolean isActive() {
        return phase != Phase.IDLE;
    }

}
