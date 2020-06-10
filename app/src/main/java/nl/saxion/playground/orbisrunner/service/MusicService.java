package nl.saxion.playground.orbisrunner.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

import nl.saxion.playground.orbisrunner.R;

public class MusicService extends Service {
    private ArrayList<MediaPlayer> playlist;

    private void playlist() {
        playlist = new ArrayList<>();

        playlist.add(make(R.raw.eternal_champions_character_bios));
        playlist.add(make(R.raw.ddk2_stickerbush_symphony));
        playlist.add(make(R.raw.smk_koopa_beach));

        Collections.shuffle(playlist);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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

    private MediaPlayer make(int resId) {
        return MediaPlayer.create(this, resId);
    }

    @Override
    public void onDestroy() {
        if (playlist != null) {
            for (MediaPlayer mediaPlayer : playlist) {
                mediaPlayer.stop();
            }
        }
        super.onDestroy();
    }
}
