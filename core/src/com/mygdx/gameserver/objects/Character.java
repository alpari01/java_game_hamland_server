package com.mygdx.gameserver.objects;

import com.badlogic.gdx.graphics.Texture;

public class Character extends GameObject {

    private final String name;
    private int damage;
    private int maxHealth;
    private int currentHealth;
    private int speed;

    /**
     * Constructor for all objects on the screen.
     *
     * @param x         X-coordinate.
     * @param y         Y-coordinate.
     * @param width     object width.
     * @param height    object height.
     * @param damage    amount of damage character can deal to mobs (NPCs).
     * @param maxHealth maximum amount of HP character has.
     * @param speed     speed character moves with
     */
    public Character(String name, float x, float y, float width, float height, int damage, int maxHealth, int speed) {
        super(x, y, width, height);
        this.name = name;
        this.damage = damage;
        this.maxHealth = maxHealth;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int newDamage) {
        damage = newDamage;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int newMaxHealth) {
        maxHealth = newMaxHealth;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }
}
