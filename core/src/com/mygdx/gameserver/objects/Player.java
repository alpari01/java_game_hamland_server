package com.mygdx.gameserver.objects;

public class Player extends GameObject {

    private Character character;

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
        this.character = character;
    }
}
