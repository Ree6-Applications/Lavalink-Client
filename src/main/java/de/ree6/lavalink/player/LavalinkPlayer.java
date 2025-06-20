/*
 * Copyright (c) Freya Arbjerg. Licensed under the MIT license
 */

package de.ree6.lavalink.player;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.ree6.lavalink.player.event.IPlayerEventListener;
import de.ree6.lavalink.player.event.PlayerEvent;
import de.ree6.lavalink.player.event.PlayerPauseEvent;
import de.ree6.lavalink.player.event.PlayerResumeEvent;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.protocol.v4.Filters;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class LavalinkPlayer implements IPlayer {

    private AudioTrack track = null;
    private boolean paused = false;
    private int volume = 100;
    private long updateTime = -1;
    private long position = -1;
    /** Lazily initialized */
    private Filters filters = null;
    private boolean connected = false;

    private final Link link;
    private List<IPlayerEventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Constructor only for internal use
     *
     * @param link the parent link
     */
    public LavalinkPlayer(Link link) {
        this.link = link;
        addListener(new LavalinkInternalPlayerEventHandler());
    }

    /**
     * Invoked by {@link Link} to make sure we keep playing music on the new node
     * <p>
     * Used when we are moved to a new socket
     */
    public void onNodeChange() {
        AudioTrack track = getPlayingTrack();
        if (track != null) {
            track.setPosition(getTrackPosition());
            playTrack(track);
        }

    }

    @Override
    public AudioTrack getPlayingTrack() {
        return track;
    }

    @Override
    public void playTrack(AudioTrack track) {
        playTrack(track, false);
    }

    @Override
    public boolean playTrack(AudioTrack track, boolean noReplace) {
        try {
            position = track.getPosition();
            TrackData trackData = track.getUserData(TrackData.class);

            // TODO:: play track.

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopTrack() {
        track = null;

        link.createOrUpdatePlayer().subscribe((playerUpdateBuilder) ->
                playerUpdateBuilder.setTrack(null).setPaused(false));
    }

    @Override
    public void setPaused(boolean pause) {
        if (pause == paused) return;

        link.createOrUpdatePlayer().flatMap((player) -> player.setPaused(pause)).subscribe(x -> {
            if (pause) {
                long timeDiff = System.currentTimeMillis() - updateTime;
                position += timeDiff;
                emitEvent(new PlayerPauseEvent(this));
            } else {
                updateTime = System.currentTimeMillis();
                emitEvent(new PlayerResumeEvent(this));
            }
        });
        paused = pause;

    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public long getTrackPosition() {
        if (getPlayingTrack() == null) return 0L;

        if (!paused) {
            // Account for the time since our last update
            long timeDiff = System.currentTimeMillis() - updateTime;
            return Math.min(position + timeDiff, track.getDuration());
        } else {
            return Math.min(position, track.getDuration());
        }

    }

    @Override
    public long getTrackDuration() {
        if (getPlayingTrack() == null) return 0L;

        if (!paused) {
            // Account for the time since our last update
            long timeDiff = System.currentTimeMillis() - updateTime;
            return Math.max(position + timeDiff, track.getDuration());
        } else {
            return Math.max(position, track.getDuration());
        }
    }


    @Override
    public void seekTo(long position) {
        if (getPlayingTrack() == null) throw new IllegalStateException("Not currently playing anything");
        if (!getPlayingTrack().isSeekable()) throw new IllegalStateException("Track cannot be seeked");

        link.createOrUpdatePlayer().subscribe(lavalinkPlayer -> {
            lavalinkPlayer.setPosition(position);
        });
        
        this.position = position;
    }

    /**
     * @deprecated Please use the new filters system to specify volume
     * @see LavalinkPlayer#getFilters()
     */
    @Override
    @Deprecated
    public void setVolume(int volume) {
        this.volume = Math.min(1000, Math.max(0, volume)); // Lavaplayer bounds

        link.createOrUpdatePlayer().subscribe(player -> player.setVolume(this.volume));
    }

    @Override
    public int getVolume() {
        return Objects.requireNonNull(link.createOrUpdatePlayer().block()).getVolume();
    }

    /**
     * @return a builder that allows setting filters such as volume, an equalizer, etc.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Filters getFilters() {
        if (filters == null) {
            filters = new Filters();
            link.createOrUpdatePlayer().setFilters(filters).subscribe();
        }
        return filters;
    }

    @Override
    public void addListener(IPlayerEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(IPlayerEventListener listener) {
        listeners.remove(listener);
    }

    public void emitEvent(PlayerEvent event) {
        listeners.forEach(listener -> listener.onEvent(event));
    }

    void clearTrack() {
        track = null;
    }

    @SuppressWarnings({"unused"})
    public Link getLink() {
        return link;
    }

    /**
     * @return Whether the Lavalink player is connected to the gateway
     */
    public boolean isConnected() {
        return connected;
    }
}
