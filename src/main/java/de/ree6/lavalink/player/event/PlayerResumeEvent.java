/*
 * Copyright (c) Freya Arbjerg. Licensed under the MIT license
 */

package de.ree6.lavalink.player.event;

import de.ree6.lavalink.player.IPlayer;

public class PlayerResumeEvent extends PlayerEvent {
    public PlayerResumeEvent(IPlayer player) {
        super(player);
    }
}
