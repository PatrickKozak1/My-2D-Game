package main;

import java.awt.*;
import java.util.Random;

public class WeatherManager {

    GamePanel gp;
    private final Random rand = new Random();


    // Weather types
    public static final int CLEAR = 0;
    public static final int RAIN = 1;
    public static final int STORM = 2;
    public static final int FOG = 3;

    // Default weather
    public int currentWeather = CLEAR;

    // Timer
    private int weatherTimer = 0;
    private int weatherDuration = 3600;

    private final int RAIN_COUNT = 200;
    private int[][] rainDrops;

    private final int SNOW_COUNT = 150;
    private float[][] snowFlakes;

    private int lightningAlpha = 0;
    private int lightningTimer = 0;
    private int nextLightning = 0;

    private float fogAlpha = 0f;
    private float fogTargetAlpha = 0f;

    private float darknessAlpha = 0f;
    private float darknessTargetAlpha = 0f;

    public WeatherManager(GamePanel gp) {
        this.gp = gp;
        initParticles();
        scheduleNextLightning();
        weatherDuration = rand.nextInt(3600) +1800;
        int[] possible = {CLEAR, CLEAR, RAIN, FOG, STORM};
        currentWeather = possible[rand.nextInt(possible.length)];
    }

    private void initParticles() {
        rainDrops = new int[RAIN_COUNT][2];
        for (int i = 0; i < RAIN_COUNT; i++) {
            rainDrops[i][0] = rand.nextInt(gp.screenWidth);
            rainDrops[i][1] = rand.nextInt(gp.screenHeight);
        }
        snowFlakes = new float[SNOW_COUNT][4];
        for (int i = 0; i <SNOW_COUNT; i++) {
            snowFlakes[i][0] = rand.nextFloat() * gp.screenWidth;
            snowFlakes[i][1] = rand.nextFloat() * gp.screenHeight;
            snowFlakes[i][2] = 0.5f + rand.nextFloat() * 1.5f;
            snowFlakes[i][3] = rand.nextFloat() * 100f;
        }
    }

    private void scheduleNextLightning() {
        nextLightning = rand.nextInt(300) + 120;
    }

    public void setWeather(int weather){
        if (weather == currentWeather)return;
        currentWeather = weather;
        weatherTimer = 0;
        gp.ui.addMessage(getWeatherName(weather));
    }

    public void cycleWeather() {
        setWeather((currentWeather +1) % 5);
    }

    private String getWeatherName(int W) {
        switch (W){
            case CLEAR: return "The sky clears up.";
            case RAIN: return "It starts to rain.";
            case STORM: return "A storm is approaching!";
            case FOG: return "Fog rolls in...";

        };
        return "";
    }

    public void update(){
        weatherTimer++;
        if (weatherTimer >= weatherDuration) {
            weatherTimer = 0;
            weatherDuration = rand.nextInt(3600) +1800;
            int[] possible = {CLEAR, CLEAR, RAIN, FOG, STORM};
            int next;
            do {
                next = possible[rand.nextInt(possible.length)];
            }while (next == currentWeather);
            setWeather(next);
        }

        updateRain();
        updateLightning();
        updateFog();
        updateDarkness();
    }



    private void updateRain(){
        if (currentWeather != RAIN && currentWeather != STORM) return;
        int speed = (currentWeather == STORM) ? 18 : 12;
        int drift = (currentWeather == STORM) ? 4 : 2;
        for (int i = 0; i < RAIN_COUNT; i++) {
            rainDrops[i][0] += drift;
            rainDrops[i][1] += speed;
            if (rainDrops[i][1] > gp.screenHeight) {
                rainDrops[i][1] = -10;
                rainDrops[i][0] = rand.nextInt(gp.screenWidth);
            }
            if (rainDrops[i][0] > gp.screenWidth) {
                rainDrops[i][0] = 0;
            }

        }
    }



    private void updateLightning() {
        if (currentWeather != STORM){lightningAlpha = 0; return;}
        lightningTimer++;
        if (lightningTimer >= nextLightning) {
            lightningAlpha = 200;
            lightningTimer = 0;
            scheduleNextLightning();
            gp.playSE(3);
        }
        if (lightningAlpha > 0) lightningAlpha -= 12;
        if (lightningAlpha < 0) lightningAlpha = 0;
    }

    private void updateFog() {
        fogTargetAlpha = (currentWeather == FOG) ? 0.45f
                : (currentWeather == STORM) ? 0.2f : 0f;

        if (fogAlpha < fogTargetAlpha) {
            fogAlpha += 0.004f;
            if (fogAlpha > fogTargetAlpha) fogAlpha = fogTargetAlpha;
        } else if (fogAlpha > fogTargetAlpha) {
            fogAlpha -= 0.004f;
            if (fogAlpha < fogTargetAlpha) fogAlpha = fogTargetAlpha;
        }
        fogAlpha = Math.max(0f, Math.min(1f, fogAlpha)); // ← harter Clamp als Sicherheitsnetz
    }

    private void updateDarkness() {
        darknessTargetAlpha = (currentWeather == STORM) ? 0.3f
                : (currentWeather == RAIN)  ? 0.15f : 0f;

        if (darknessAlpha < darknessTargetAlpha) {
            darknessAlpha += 0.004f;
            if (darknessAlpha > darknessTargetAlpha) darknessAlpha = darknessTargetAlpha;
        } else if (darknessAlpha > darknessTargetAlpha) {
            darknessAlpha -= 0.004f;
            if (darknessAlpha < darknessTargetAlpha) darknessAlpha = darknessTargetAlpha;
        }
        darknessAlpha = Math.max(0f, Math.min(1f, darknessAlpha)); // ← harter Clamp
    }

    public void draw(Graphics2D g2){
        if (currentWeather == CLEAR && fogAlpha <= 0 && darknessAlpha <= 0) return;

        if (darknessAlpha > 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, darknessAlpha));
            g2.setColor(new Color(0,0,30));
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        }

        if (currentWeather == RAIN || currentWeather == STORM) {
            drawRain(g2);
        }

        if (fogAlpha > 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fogAlpha));
            g2.setColor(new Color(200,200,210));
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        }

        if (lightningAlpha > 0) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, lightningAlpha / 255f));
            g2.setColor(Color.WHITE);
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawRain(Graphics2D g2) {
        float a = (currentWeather == STORM) ? 0.75f : 0.5f;
        int len = (currentWeather == STORM) ? 14 : 10;
        int dx = (currentWeather == STORM) ? 4 : 2;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,a));
        g2.setColor(new Color(174,194,224));
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i < RAIN_COUNT; i++) {
            g2.drawLine(rainDrops[i][0],rainDrops[i][1],
                    rainDrops[i][0] - dx, rainDrops[i][1] - len);
        }
    }

    private void drawSnow(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        g2.setColor(Color.WHITE);
        for (int i = 0; i < SNOW_COUNT; i++) {
            int size = 2 + (int)(snowFlakes[i][2] * 2);
            g2.fillOval((int)snowFlakes[i][0], (int)snowFlakes[i][1], size, size);
        }
    }


}
