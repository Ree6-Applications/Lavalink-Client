/*
 * Copyright (c) Freya Arbjerg. Licensed under the MIT license
 */

package de.ree6.lavalink.player.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.ree6.lavalink.player.IPlayer;

public class TrackStartEvent extends PlayerEvent {

    private AudioTrack track;

    public TrackStartEvent(IPlayer player, AudioTrack track) {
        super(player);
        this.track = track;
    }

    public AudioTrack getTrack() {
        return track;
    }
}
