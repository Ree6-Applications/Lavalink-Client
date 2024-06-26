/*
 * Copyright (c) Freya Arbjerg. Licensed under the MIT license
 */

package de.ree6.lavalink.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import de.ree6.lavalink.player.event.IPlayerEventListener;

public class LavaplayerPlayerWrapper implements IPlayer {

    private final AudioPlayer player;

    public LavaplayerPlayerWrapper(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }

    @Override
    public void playTrack(AudioTrack track) {
        player.playTrack(track);
    }

    @Override
    public boolean playTrack(AudioTrack track, boolean noInterrupt) {
        return player.startTrack(track, noInterrupt);
    }

    @Override
    public void stopTrack() {
        player.stopTrack();
    }

    @Override
    public void setPaused(boolean b) {
        player.setPaused(b);
    }

    @Override
    public boolean isPaused() {
        return player.isPaused();
    }

    @Override
    public long getTrackPosition() {
        if (player.getPlayingTrack() == null) return 0L;

        return player.getPlayingTrack().getPosition();
    }

    @Override
    public long getTrackDuration() {
        if (player.getPlayingTrack() == null) return 0L;

        return player.getPlayingTrack().getDuration();
    }

    @Override
    public void seekTo(long position) {
        if (player.getPlayingTrack() == null) throw new IllegalStateException("Not playing anything");

        player.getPlayingTrack().setPosition(position);
    }

    @Override
    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    @Override
    public int getVolume() {
        return player.getVolume();
    }

    @Override
    public void addListener(IPlayerEventListener listener) {
        player.addListener((AudioEventListener) listener);
    }

    @Override
    public void removeListener(IPlayerEventListener listener) {
        player.removeListener((AudioEventListener) listener);
    }

    public AudioFrame provide() {
        return player.provide();
    }

    public boolean provide(MutableAudioFrame targetFrame) {
        return player.provide(targetFrame);
    }

}
