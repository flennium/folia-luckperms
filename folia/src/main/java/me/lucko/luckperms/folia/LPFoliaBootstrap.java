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

import me.lucko.luckperms.bukkit.LPBukkitBootstrap;
import net.luckperms.api.platform.Platform;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.logging.Logger;

/**
 * Bootstrap plugin for LuckPerms running on Folia.
 */
public class LPFoliaBootstrap extends LPBukkitBootstrap {
    private final FoliaSchedulerAdapter schedulerAdapter;

    public LPFoliaBootstrap(JavaPlugin loader) {
        super(loader);
        this.schedulerAdapter = new FoliaSchedulerAdapter(this);
        this.plugin = new LPFoliaPlugin(this);
    }

    @Override
    public FoliaSchedulerAdapter getScheduler() {
        return this.schedulerAdapter;
    }

    @Override
    public void onEnable() {
        if (this.incompatibleVersion) {
            Logger logger = this.loader.getLogger();
            logger.severe("----------------------------------------------------------------------");
            logger.severe("Your server version is not compatible with this build of LuckPerms. :(");
            logger.severe("----------------------------------------------------------------------");
            getServer().getPluginManager().disablePlugin(this.loader);
            return;
        }

        this.serverStarting = true;
        this.serverStopping = false;
        this.startTime = Instant.now();
        try {
            this.plugin.enable();

            // schedule a task to update the 'serverStarting' flag
            getServer().getGlobalRegionScheduler().run(this.loader, task -> this.serverStarting = false);
        } finally {
            this.enableLatch.countDown();
        }
    }

    @Override
    public Platform.Type getType() {
        return Platform.Type.FOLIA;
    }
}
