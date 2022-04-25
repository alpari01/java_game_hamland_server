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
            Enemy newMob = new Zombie(800, 1000, 100, 100, 0.25, 5);
            allMobsSpawned.put(newMob.getId(), newMob);
        }
    }

    public void spawnOctopus(int amount) {
        for (int i = 0; i < amount; i++) {
            Enemy newMob = new Octopus(1200, 1000, 100, 100, 0.2, 5);
            allMobsSpawned.put(newMob.getId(), newMob);
        }
    }

    public void mobsFollowPlayers() {
        for (Enemy mob : allMobsSpawned.values()) {
            Player nearestPlayer = getNearestPlayer(mob);  // Player to follow.

            // The mob will follow this player who is the nearest one.
            if (nearestPlayer != null) {
                double distance = Math.sqrt(Math.pow(mob.getX() - nearestPlayer.getX(), 2)
                                          + Math.pow(mob.getX() - nearestPlayer.getX(), 2));

                double gradientX = (mob.getX() - nearestPlayer.getX()) / distance;
                double gradientY = (mob.getY() - nearestPlayer.getY()) / distance;

                double vectorLength = Math.sqrt(Math.pow(gradientX, 2) + Math.pow(gradientY, 2));
                double unitVectorX = gradientX / vectorLength;
                double unitVectorY = gradientY / vectorLength;

                mob.setX((float) (mob.getX() - (unitVectorX * mob.getSpeed())));
                mob.setY((float) (mob.getY() - (unitVectorY * mob.getSpeed())));
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
