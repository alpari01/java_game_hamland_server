package com.mygdx.gameserver.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.gameserver.objects.Player;
import com.mygdx.gameserver.packets.PacketCheckPlayerNicknameUnique;
import com.mygdx.gameserver.packets.PacketMessage;
import com.mygdx.gameserver.packets.PacketSendPlayerMovement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class KryoServer extends Listener {

    static Server server;  // Server object.
    private Map<String, Player> connectedPlayers = new HashMap<>();

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
                addPlayer(packet.playerNickname);
                System.out.println("Client nickname is " + packet.playerNickname);
                System.out.println(connectedPlayers);

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
    }

    // Run this method when a client disconnects.
    public void disconnected(Connection c) {
        System.out.println("A client with IP " + c.getRemoteAddressTCP() + " has disconnected.");
    }

    /**
     * Create and add new player object to connected players list.
     *
     * @param playerNickname nickname of the player
     */
    public void addPlayer(String playerNickname) {
        Player newPlayer = new Player(0, 0, 100, 100, null);
        connectedPlayers.put(playerNickname, newPlayer);
    }

    /**
     * Remove player from connected players list.
     *
     * @param playerNickname nickname of the player to remove.
     */
    public void removePlayer(String playerNickname) {
        connectedPlayers.remove(playerNickname);
    }
}
