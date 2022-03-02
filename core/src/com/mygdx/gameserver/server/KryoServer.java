package com.mygdx.gameserver.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.gameserver.objects.Player;
import com.mygdx.gameserver.packets.PacketCheckPlayerNicknameUnique;
import com.mygdx.gameserver.packets.PacketMessage;

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
        PacketMessage packetMessage = new PacketMessage();
        packetMessage.message = "Privet kak dela";
        c.sendTCP(packetMessage);
    }

    // Run this method when a client disconnects.
    public void disconnected(Connection c) {
        System.out.println("A client with IP " + c.getRemoteAddressTCP() + " has disconnected.");
    }
}
