/*
 * Copyright (c) Freya Arbjerg. Licensed under the MIT license
 */

package de.ree6.lavalink.player;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.ree6.lavalink.player.event.IPlayerEventListener;

public interface IPlayer {

    AudioTrack getPlayingTrack();

    void playTrack(AudioTrack track);

    boolean playTrack(AudioTrack track, boolean noInterrupt);

    void stopTrack();

    void setPaused(boolean b);

    boolean isPaused();

    long getTrackPosition();

    long getTrackDuration();

    void seekTo(long position);

    void setVolume(int volume);

    int getVolume();

    void addListener(IPlayerEventListener listener);

    void removeListener(IPlayerEventListener listener);

}
