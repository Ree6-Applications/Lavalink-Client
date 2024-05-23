/*
 * Copyright (c) Freya Arbjerg. Licensed under the MIT license
 */

package de.ree6.lavalink.player.event;

import de.ree6.lavalink.player.IPlayer;

public abstract class PlayerEvent {

    private final IPlayer player;

    public PlayerEvent(IPlayer player) {
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }
}
