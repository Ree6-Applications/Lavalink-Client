/*
 * Copyright (c) Freya Arbjerg. Licensed under the MIT license
 */

package de.ree6.lavalink.player;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.ree6.lavalink.player.event.PlayerEventListenerAdapter;

class LavalinkInternalPlayerEventHandler extends PlayerEventListenerAdapter {

    @Override
    public void onTrackEnd(IPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason != AudioTrackEndReason.REPLACED && endReason != AudioTrackEndReason.STOPPED) {
            ((LavalinkPlayer) player).clearTrack();
        }
    }
}
