package com.mygdx.gameserver.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

public abstract class GameObject {

    public Polygon polygon;
    public Sprite sprite;

    /**
     * Constructor for all objects on the screen.
     * @param x X-coordinate.
     * @param y Y-coordinate.
     * @param width object width.
     * @param height object height.
     */
    public GameObject(float x, float y, float width, float height) {

        // Create a Polygon with given vertices
        polygon = new Polygon(new float[]{0f, 0f, width, 0f, width, height, 0f, height});
        polygon.setOrigin(width / 2f, height / 2f); // set polygon center
        polygon.setPosition(x - width / 2f, y - height / 2f); // set polygon position

    }
}
