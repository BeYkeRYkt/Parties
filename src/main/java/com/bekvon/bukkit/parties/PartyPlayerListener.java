/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bekvon.bukkit.parties;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listens to player-specific events.
 */
public class PartyPlayerListener implements Listener {

    private Parties parent;

    public PartyPlayerListener(Parties plugIn)
    {
        parent = plugIn;
    }

    /**
     * Called when player chat events occur, to reroute chat messages to the
     * party when appropriate.
     * 
     * @param event
     *            Chat event containing the event details.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled() || !parent.isEnabled())
            return;
        String pname = event.getPlayer().getName();
        PartyManager pmanager = Parties.getPartyManager();
        if(pmanager.partyChatEnabled(pname) && pmanager.isPlayerInParty(pname))
        {
            Parties.getPartyManager().chatInParty(pname, event.getMessage());
            System.out.println("[PartyChat] "+pname+": " + event.getMessage());
            event.setCancelled(true);
            return;
        }
    }

    /**
     * Called when a player logs in, to initialise party chat where applicable
     * and to notify the party.
     * 
     * @param event
     *            Player login event, containing detailed information about the
     *            event.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if(!parent.isEnabled())
            return;
        String pname = event.getPlayer().getName();
        if (Parties.getPartyManager().partyChatEnabled(pname) && Parties.getPartyManager().isPlayerInParty(pname)) {
            Parties.getPartyManager().sendPartyMessage(Parties.getPartyManager().getPlayersPartyName(pname),"Party Member " + pname+" has logged in.");
            return;
        }
    }

    /**
     * Called when a player logs out, to update the party manager and notify
     * party members.
     * 
     * @param event
     *            Player quit event, containing detailed information about the
     *            event.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!parent.isEnabled())
            return;
        String pname = event.getPlayer().getName();
        if (Parties.getPartyManager().partyChatEnabled(pname) && Parties.getPartyManager().isPlayerInParty(pname)) {
            Parties.getPartyManager().sendPartyMessage(Parties.getPartyManager().getPlayersPartyName(pname), "Party Member " + pname+" has logged out.");
            return;
        }
    }
}
