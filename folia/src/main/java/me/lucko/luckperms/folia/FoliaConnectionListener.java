/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.folia;

import me.lucko.luckperms.bukkit.inject.permissible.PermissibleInjector;
import me.lucko.luckperms.bukkit.listeners.BukkitConnectionListener;
import me.lucko.luckperms.common.config.ConfigKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Connection listener for Folia.
 */
public class FoliaConnectionListener extends BukkitConnectionListener {
    private final LPFoliaPlugin plugin;

    public FoliaConnectionListener(LPFoliaPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        handleDisconnect(player.getUniqueId());

        // perform unhooking from bukkit objects 1 tick later.
        // In Folia, we use GlobalRegionScheduler.
        this.plugin.getBootstrap().getServer().getGlobalRegionScheduler().runDelayed(this.plugin.getLoader(), task -> {
            // Remove the custom permissible
            try {
                PermissibleInjector.uninject(player, true);
            } catch (Exception ex) {
                this.plugin.getLogger().severe("Exception thrown when unloading permissions from " +
                        player.getUniqueId() + " - " + player.getName(), ex);
            }

            // Handle auto op
            if (this.plugin.getConfiguration().get(ConfigKeys.AUTO_OP)) {
                player.setOp(false);
            }
        }, 1L);
    }
}
