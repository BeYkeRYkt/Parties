/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bekvon.bukkit.parties;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 *
 * @author Administrator
 */
public class PartyEntityListener implements Listener {

    private Parties parent;
    
    public PartyEntityListener(Parties plugIn)
    {
        parent = plugIn;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !parent.isEnabled())
            return;
        if(!Parties.getPartyManager().isPvpEnabled() && event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            Entity sender = e.getDamager();
            Entity reciever = e.getEntity();
            if(sender instanceof Player && reciever instanceof Player)
            {
                Player attacker = (Player) sender;
                Player defender = (Player) reciever;
                if(Parties.getPartyManager().isPlayerInParty(defender.getName()) && Parties.getPartyManager().isPlayerInParty(attacker.getName()))
                {
                    if(Parties.getPartyManager().getPlayersPartyName(defender.getName()).equals(Parties.getPartyManager().getPlayersPartyName(attacker.getName())))
                    {
                        event.setCancelled(true);
                        attacker.sendMessage(Parties.getPartyManager().getErrorColor()+"Party PVP is disabled.");
                    }
                }
            }
        }
    }

}
