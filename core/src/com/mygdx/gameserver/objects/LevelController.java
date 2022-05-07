package com.mygdx.gameserver.objects;

import com.mygdx.gameserver.server.KryoServer;

public class LevelController {

    private KryoServer server;
    private MobController mobController;
    private int currentWave;

    public LevelController(KryoServer server) {
        this.server = server;
        this.mobController = new MobController(this.server);
        this.currentWave = 1;
    }

    public void beginNextWave() {

        if (!this.getIsWaveOngoing()) {
            // If there is no currently ongoing wave.
            switch (this.currentWave) {
                // The higher the wave the more difficult it is for players to survive.
                case 1:
                    this.mobController.spawnMob("zombie", 2, 500, 500);
                    break;

                case 2:
                    this.mobController.spawnMob("blueguy", 4, 300, 800);
                    break;

                case 3:
                    this.mobController.spawnMob("zombie", 4, 500, 1200);
                    this.mobController.spawnMob("crab", 2, 300, 800);
                    break;

                case 4:
                    this.mobController.spawnMob("zombie", 3, 1500, 700);
                    this.mobController.spawnMob("octopus", 3, 300, 400);
                    this.currentWave = 1; // FOR WAVE LOOP, REMOVE LATER.
                    break;
            }

            // Increment next wave number.
            this.currentWave++;
        }
    }

    public boolean getIsWaveOngoing() {
        // If there are no mobs alive -> there is no ongoing wave and new wave can begin.
        return this.mobController.getAllMobsSpawned().size() != 0;
    }

    public MobController getMobController() {
        return this.mobController;
    }
}
