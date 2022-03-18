package com.mygdx.gameserver.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.gameserver.objects.Player;
import com.mygdx.gameserver.packets.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class KryoServer extends Listener {

    static Server server;  // Server object.
    private Map<String, Player> connectedPlayers = new HashMap<>();
    private Map<String, Connection> connections = new HashMap<>();

    // Ports to listen on.
    static int udpPort = 27960;
    static int tcpPort = 27960;

    public static void main(String[] args) throws IOException {
        System.out.println("Creating the server...");
        server = new Server();

        // Register packet classes. Server can only handle packets that are registered.
        server.getKryo().register(PacketMessage.class);
        server.getKryo().register(PacketCheckPlayerNicknameUnique.class);
        server.getKryo().register(PacketSendPlayerMovement.class);
        server.getKryo().register(PacketUpdatePlayers.class);
        server.getKryo().register(PacketRequestConnectedPlayers.class);
        server.getKryo().register(java.util.ArrayList.class);
        server.getKryo().register(PacketPlayerConnected.class);

        // Bind to the ports.
        server.bind(tcpPort, udpPort);

        server.start();

        // Add the listener.
        server.addListener(new KryoServer());
        System.out.println("Server is up!");
    }

    // Run this method when a client connects.
    public void connected(Connection c) {
        System.out.println("Received a connection from " + c.getRemoteAddressTCP().getHostString());
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PacketMessage packetMessage = new PacketMessage();
        packetMessage.message = "Privet kak dela";
        c.sendTCP(packetMessage);
    }

    // Run this method when server receives any packet from ANY client.
    public void received(Connection c, Object p) {

        // Packet check nickname unique.
        if (p instanceof PacketCheckPlayerNicknameUnique) {
            PacketCheckPlayerNicknameUnique packet = (PacketCheckPlayerNicknameUnique) p;

            // Check if this nickname is not already taken by other player.
            if (!connectedPlayers.containsKey(packet.playerNickname)) {
                addPlayer(packet.playerNickname, c);
                System.out.println("Client nickname is " + packet.playerNickname);

                // DEBUG
                System.out.println(connectedPlayers);
                System.out.println(connections);

                // Notify user that his nickname is OK.
                packet.isNicknameUnique = true;
                c.sendTCP(packet);
            }

            // If this nickname already exists.
            else {
                System.out.println("Nickname already taken.");
                packet.isNicknameUnique = false;
                c.sendTCP(packet);
            }
        }

        // If received this packet -> player has moved -> need to broadcast update packet.
        if (p instanceof PacketSendPlayerMovement) {
            PacketSendPlayerMovement packet = (PacketSendPlayerMovement) p;

            // Prepare a new update packet.
            PacketUpdatePlayers updatePacket = new PacketUpdatePlayers();
            updatePacket.playerNickname = packet.playerNickname;
            Player playerToUpdate = connectedPlayers.get(packet.playerNickname);

            updatePacket.playerPositionX = packet.playerCurrentPositionX;
            updatePacket.playerPositionY = packet.playerCurrentPositionY;
            updatePacket.playerRotation = packet.playerCurrentRotation;

            // Broadcast update packet (so everyone knows this player's new position).
            for (String nickname : connections.keySet()) {
                Connection playerConnection = connections.get(nickname);
                playerConnection.sendTCP(updatePacket);
            }

            // Update server players' data.
            playerToUpdate.setX(packet.playerCurrentPositionX);
            playerToUpdate.setY(packet.playerCurrentPositionY);
            playerToUpdate.setRotation(packet.playerCurrentRotation);
        }

        // Packet respond with all currently connected players.
        if (p instanceof PacketRequestConnectedPlayers) {
            PacketRequestConnectedPlayers packet = (PacketRequestConnectedPlayers) p;

            // Get all connected players.
            packet.allPlayers.addAll(connectedPlayers.keySet());
            c.sendTCP(packet);
        }
    }

    // Run this method when a client disconnects.
    public void disconnected(Connection c) {
        String playerNicknameToRemove = null;
        for (String nickname : connections.keySet()) {
            // Search which player has disconnected.
            if (connections.get(nickname).equals(c)) {
                // Avoid modifying hashmap while iterating -> remove from player hashmaps after loop.
                playerNicknameToRemove = nickname;  // Save player (his nickname) to be removed.
                break;
            }
        } if (playerNicknameToRemove != null) {
            // Remove the player from hashmaps.
            connections.remove(playerNicknameToRemove);
            connectedPlayers.remove(playerNicknameToRemove);
            System.out.println("Player " + playerNicknameToRemove + " removed.");
        }
    }

    /**
     * Create and add new player object to connected players list.
     *
     * @param playerNickname nickname of the player
     */
    public void addPlayer(String playerNickname, Connection playerConnection) {
        Player newPlayer = new Player(0, 0, 100, 100, null);
        connectedPlayers.put(playerNickname, newPlayer);
        connections.put(playerNickname, playerConnection);
        broadcastPlayerConnected(playerNickname);
    }

    /**
     * Broadcast packet with new connected players so other are notified of it.
     */
    public void broadcastPlayerConnected(String newConnectedPlayer) {
        for (String connectedPlayer : connections.keySet()) {
            if (!connectedPlayer.equals(newConnectedPlayer)) {
                Connection connection = connections.get(connectedPlayer);
                PacketPlayerConnected packetPlayerConnected = new PacketPlayerConnected();
                packetPlayerConnected.teammateNickname = newConnectedPlayer;
                connection.sendTCP(packetPlayerConnected);
            }
        }
    }
}
