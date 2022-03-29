package com.mygdx.gameserver.server;

import static java.lang.Thread.sleep;

public class ServerUpdateThread implements Runnable {

    private KryoServer kryoServer;

    public void setKryoServer(KryoServer server) {
        this.kryoServer = server;
    }

    @Override
    public void run() {
        while (true) {

            this.kryoServer.broadcastUpdateMobPacket();
            System.out.println("broadcadsting");

            try {
                sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
