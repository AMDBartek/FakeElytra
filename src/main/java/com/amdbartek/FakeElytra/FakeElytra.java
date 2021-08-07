package com.amdbartek.FakeElytra;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class FakeElytra extends JavaPlugin implements Listener, CommandExecutor {

    public List<Player> glidingPlayers = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent e) {
        if (glidingPlayers.contains((Player)e.getEntity())) {
            e.setCancelled(true);
            if (this.getConfig().getBoolean("cancel-when-on-ground")) {
                if (e.getEntity().isOnGround()) {
                    glidingPlayers.remove((Player)e.getEntity());
                    ((Player)e.getEntity()).getPlayer().setGliding(false);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (glidingPlayers.contains((Player)sender)) {
                glidingPlayers.remove((Player)sender);
                player.getPlayer().setGliding(false);
                player.getPlayer().sendMessage("Stopped gliding.");
            } else {
                glidingPlayers.add((Player)sender);
                player.getPlayer().setGliding(true);
                player.getPlayer().sendMessage("Started gliding.");
            }
        } else if (!(sender instanceof Player)) {
            sender.sendMessage("Error: You have to be a player to use this command!");
        }
        return true;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        getLogger().info("FakeElytra enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("FakeElytra disabled");
    }
}
