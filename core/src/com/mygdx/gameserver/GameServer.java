package com.mygdx.gameserver;

import com.badlogic.gdx.Game;
import com.mygdx.gameserver.server.KryoServer;


public class GameServer extends Game {

    @Override
    public void create() {
        // Start up the server.
        KryoServer server = new KryoServer();
    }
}
