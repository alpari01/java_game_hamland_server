package com.mygdx.gameserver.packets;

import com.mygdx.gameserver.objects.Player;

import java.util.Map;

public class PacketRequestConnectedPlayers {
    public Map<String, Player> allPlayers;
}
