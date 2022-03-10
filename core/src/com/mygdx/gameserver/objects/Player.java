package com.mygdx.gameserver.objects;

public class Player extends GameObject {

    private Character character;
    private float x;
    private float y;
    private float width;
    private float height;
    private float rotation;

    /**
     * Constructor for all objects on the screen.
     *
     * @param x         X-coordinate.
     * @param y         Y-coordinate.
     * @param width     object width.
     * @param height    object height.
     * @param character character (hero) player has chosen to play with
     */
    public Player(float x, float y, float width, float height, Character character) {
        super(x, y, width, height);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.character = character;
        this.rotation = 0;
    }

    public float getX() {
        return x;
    }

    public void setX(float newX) {
        x = newX;
    }

    public float getY() {
        return y;
    }

    public void setY(float newY) {
        y = newY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float newRotation) {
        rotation = newRotation;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character newCharacter) {
        character = newCharacter;
    }
}
