package com.mygdx.gameserver.objects;

import com.mygdx.gameserver.server.KryoServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MobController {

    private final static int MOB_SPAWN_RADIUS = 100;

    private final static double ZOMBIE_SPEED = 0.3;
    private final static int ZOMBIE_HP = 3;

    private final static double OCTOPUS_SPEED = 0.2;
    private final static int OCTOPUS_HP = 5;

    private Map<Integer, Enemy> allMobsSpawned = new HashMap<>();
    private KryoServer server;

    public MobController(KryoServer server) {
        this.server = server;
    }

    /**
     * Spawn specified amount of mobs at specified location.
     *
     * @param mobType      mob type to spawn (e.g. "zombie", "octopus")
     * @param amount       amount of mobs to spawn
     * @param spawnPointX  initial spawn point coordinate x
     * @param spawnPointY  initial spawn point coordinate y
     */
    public void spawnMob(String mobType, int amount, float spawnPointX, float spawnPointY) {

        for (int i = 0; i < amount; i++) {

            Enemy newMob = null;

            float randomNum = ThreadLocalRandom.current().nextInt(-MOB_SPAWN_RADIUS, MOB_SPAWN_RADIUS + 1);

            float randomX = spawnPointX + randomNum;
            float randomY = spawnPointY + randomNum;

            switch (mobType) {

                case "zombie": newMob = new Zombie(randomX, randomY,
                        100, 100, ZOMBIE_SPEED, ZOMBIE_HP);
                    break;

                case "octopus": newMob = new Octopus(randomX, randomY,
                        100, 100, OCTOPUS_SPEED, OCTOPUS_HP);
                    break;
            }

            allMobsSpawned.put(newMob.getId(), newMob);
        }
    }

    public void killMob(int mobId) {
        this.allMobsSpawned.remove(mobId);
    }

    public void mobsFollowPlayers() {

        double gradientX = 0;
        double gradientY = 0;

        for (Enemy mob : allMobsSpawned.values()) {
            Player nearestPlayer = getNearestPlayer(mob);  // Player to follow.

            // The mob will follow this player who is the nearest one.
            if (nearestPlayer != null) {
                double distance = Math.sqrt(Math.pow(mob.getX() - nearestPlayer.getX(), 2)
                                          + Math.pow(mob.getX() - nearestPlayer.getX(), 2)) + 0.00001;

                gradientX = (mob.getX() - nearestPlayer.getX()) / distance;
                gradientY = (mob.getY() - nearestPlayer.getY()) / distance;

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
