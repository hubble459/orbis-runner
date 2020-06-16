package nl.saxion.playground.orbisrunner.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

import nl.saxion.playground.orbisrunner.R;

/**
 * Music Service to play music while g A m i n G
 * <p>
 * Has a playlist of three songs
 * <p>
 * Music stops working after a while, so don't use this
 */
@Deprecated
public class MusicService extends Service {
    private ArrayList<MediaPlayer> playlist;

    /**
     * Make playlist
     */
    private void playlist() {
        playlist = new ArrayList<>();

        playlist.add(make(R.raw.eternal_champions_character_bios));
        playlist.add(make(R.raw.ddk2_stickerbush_symphony));
        playlist.add(make(R.raw.smk_koopa_beach));

        Collections.shuffle(playlist);
    }

    /**
     * Called when service gets started
     * First song in playlist will start
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playlist();
        for (int i = 0; i < playlist.size(); i++) {
            if (i < playlist.size() - 1) {
                playlist.get(i).setNextMediaPlayer(playlist.get(i + 1));
            } else {
                playlist.get(i).setNextMediaPlayer(playlist.get(0));
            }
        }
        playlist.get(0).start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Make making a MediaPlayer easier
     *
     * @param resId Resource ID
     * @return MediaPlayer
     */
    private MediaPlayer make(int resId) {
        return MediaPlayer.create(this, resId);
    }

    /**
     * Stop music and service
     */
    @Override
    public void onDestroy() {
        if (playlist != null) {
            for (MediaPlayer mediaPlayer : playlist) {
                mediaPlayer.stop();
            }
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
