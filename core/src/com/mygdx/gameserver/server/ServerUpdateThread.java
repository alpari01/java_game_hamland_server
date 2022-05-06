package com.mygdx.gameserver.server;

import java.util.Map;

import static java.lang.Thread.sleep;

public class ServerUpdateThread implements Runnable {

    // When all players are ready the game starts after this amount of seconds.
    private static final byte TIMER_GAME_BEGIN = 3;

    private KryoServer kryoServer;
    private Map<String, Boolean> playersReady;
    private boolean allPlayersReady;
    private boolean startTheGame;
    private long timerStartTime;
    long delta;

    public void setKryoServer(KryoServer server) {
        this.kryoServer = server;
        this.playersReady = this.kryoServer.getPlayersReady();
        this.allPlayersReady = false;
        this.timerStartTime = System.currentTimeMillis();
        this.startTheGame = false;
    }

    @Override
    public void run() {
        while (true) {

            // Update the data about players' readiness.
            this.playersReady = this.kryoServer.getPlayersReady();
            this.allPlayersReady = this.checkAllPlayersReady();

            if (this.kryoServer.getConnectedPlayers().size() == 0) {
                // Stop the game when all players have disconnected.
//                System.out.println("game not start");
                this.startTheGame = false;
                delta = 0;
                this.kryoServer.broadcastPacketGameBeginTimer((int) delta, TIMER_GAME_BEGIN);
                this.playersReady = this.kryoServer.getPlayersReady();  // Refresh the local hashmap.
            }

            if (this.allPlayersReady && !this.startTheGame) {
                // Start the timer.
                delta = -(timerStartTime - System.currentTimeMillis()) / 1000;
                this.kryoServer.broadcastPacketGameBeginTimer((int) delta, TIMER_GAME_BEGIN);
                if (delta >= TIMER_GAME_BEGIN + 1) {
                    this.startTheGame = true;
                    System.out.println("game start");
                }
            }
            else {
                // If any of the players is not ready again -> reset the timer.
                this.timerStartTime = System.currentTimeMillis();
                delta = 0;
                this.kryoServer.broadcastPacketGameBeginTimer((int) delta, TIMER_GAME_BEGIN);
            }

            if (this.startTheGame) {
                // When timer is up -> start the game (make mobs move).
                this.kryoServer.broadcastUpdateMobPacket();
                this.kryoServer.mobsFollowPlayer();
            }

            try {
                sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkAllPlayersReady() {

        if (this.playersReady.size() == 0 || this.kryoServer.getConnectedPlayers().size() == 0) return false;

        for (boolean isPlayerReady : this.playersReady.values()) {
            if (!isPlayerReady) {
                // If any of the players is not ready.
                return false;
            }
        }
        // Else if all players are ready.
        return true;
    }
}
