package com.mygdx.gameserver.objects;

import com.mygdx.gameserver.server.KryoServer;

import java.util.HashMap;
import java.util.Map;

public class MobController {

    private Map<Integer, Enemy> allMobsSpawned = new HashMap<>();
    private KryoServer server;

    public MobController(KryoServer server) {
        this.server = server;
    }

    public void spawnZombies(int amount) {
        for (int i = 0; i < amount; i++) {
            Enemy newMob = new Zombie(0, 0, 100, 100, 0.05, 5);
            allMobsSpawned.put(newMob.getId(), newMob);
        }
    }

    public void spawnOctopus(int amount) {
        for (int i = 0; i < amount; i++) {
            Enemy newMob = new Octopus(0, 0, 100, 100, 0.1, 5);
            allMobsSpawned.put(newMob.getId(), newMob);
        }
    }

    public void mobsFollowPlayers() {
        for (Enemy mob : allMobsSpawned.values()) {
            Player nearestPlayer = getNearestPlayer(mob);  // Player to follow.

            // The mob will follow this player who is the nearest one.
            if (nearestPlayer != null) {
                if (mob.getX() > nearestPlayer.getX()) {
                    mob.setX((float) (mob.getX() - mob.getSpeed()));
                }

                if (mob.getX() < nearestPlayer.getX()) {
                    mob.setX((float) (mob.getX() + mob.getSpeed()));
                }

                if (mob.getY() > nearestPlayer.getY()) {
                    mob.setY((float) (mob.getY() - mob.getSpeed()));
                }

                if (mob.getY() < nearestPlayer.getY()) {
                    mob.setY((float) (mob.getY() + mob.getSpeed()));
                }
            }
        }
    }

    public Player getNearestPlayer(Enemy mob) {
        Player nearestPlayer = null;
        float distanceToPlayer;
        float distanceToPlayerPrev = 99999;

        for (Player player : server.getConnectedPlayers().values()) {
            distanceToPlayer = (float) Math.sqrt(Math.pow(player.getX() - mob.getX(), 2) +
                    Math.pow(player.getY() - mob.getY(), 2));

            if (distanceToPlayer < distanceToPlayerPrev) {
                distanceToPlayerPrev = distanceToPlayer;
                nearestPlayer = player;
            }
        }

        return nearestPlayer;
    }

    public Map<Integer, Enemy> getAllMobsSpawned() {
        return this.allMobsSpawned;
    }
}
