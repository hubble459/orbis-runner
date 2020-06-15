package nl.saxion.playground.orbisrunner.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;

import nl.saxion.playground.orbisrunner.R;

/**
 * Can be used instead of MusicService but is more poo-poo
 */
@Deprecated
public class Music extends AsyncTask<Context, Void, Void> {
    private ArrayList<MediaPlayer> playlist;

    private void playlist(Context context) {
        playlist = new ArrayList<>();

        playlist.add(make(context, R.raw.eternal_champions_character_bios));
        playlist.add(make(context, R.raw.ddk2_stickerbush_symphony));
        playlist.add(make(context, R.raw.smk_koopa_beach));

        Collections.shuffle(playlist);
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        playlist(contexts[0]);
        for (int i = 0; i < playlist.size() - 1; i++) {
            playlist.get(i).setNextMediaPlayer(playlist.get(i + 1));
        }
        playlist.get(0).start();
        return null;
    }

    private MediaPlayer make(Context context, int resId) {
        MediaPlayer player = MediaPlayer.create(context, resId);
        player.setVolume(100, 100);
        return player;
    }
}
