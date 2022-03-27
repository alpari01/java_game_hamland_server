package com.mygdx.gameserver.objects;

import java.util.HashMap;
import java.util.Map;

public class MobController {

    private Map<Integer, Enemy> allMobsSpawned = new HashMap<>();

    public void spawnZombies(int amount) {
        for (int i = 0; i < amount; i++) {
            Enemy newMob = new Zombie(0, 0, 100, 100, 0.5, 5);
            allMobsSpawned.put(newMob.getId(), newMob);
        }
    }

    public void SpawnOctopus(int amount) {
        for (int i = 0; i < amount; i++) {
            Enemy newMob = new Octopus(0, 0, 100, 100, 0.5, 5);
            allMobsSpawned.put(newMob.getId(), newMob);
        }
    }

    public Map<Integer, Enemy> getAllMobsSpawned() {
        return this.allMobsSpawned;
    }
}
