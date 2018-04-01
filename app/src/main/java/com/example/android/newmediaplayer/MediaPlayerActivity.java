package com.example.android.newmediaplayer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.MediaPlayer;

public class MediaPlayerActivity extends AppCompatActivity {
    private Button playButton;
    private SeekBar positionBar;
    private SeekBar volumeBar;
    private TextView elapsedTimeLabel;
    private TextView remainingTimeLabel;
    private MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        playButton = (Button) findViewById(R.id.play_button);
        positionBar = (SeekBar) findViewById(R.id.progress_seek_bar);
        volumeBar = (SeekBar) findViewById(R.id.volume_seek_bar);
        elapsedTimeLabel = (TextView) findViewById(R.id.time_start);
        remainingTimeLabel = (TextView) findViewById(R.id.time_finish);


//        Meida Player
        mp = MediaPlayer.create(this, R.raw.fonarik);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

//        Position Bar

        positionBar = (SeekBar) findViewById(R.id.progress_seek_bar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        Volume Bar

        volumeBar = (SeekBar) findViewById(R.id.volume_seek_bar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNume = progress / 100f;
                mp.setVolume(volumeNume, volumeNume);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


// Thread (Update postitionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
//             Update positionBar.
            positionBar.setProgress(currentPosition);

//            Update Labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);

        }
    };


    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;


    }

    public void playBtnClick(View view) {
        if (!mp.isPlaying()) {
//        Stopping
            mp.start();
            playButton.setBackgroundResource(R.drawable.ic_pause);
        } else {
//        Playing
            mp.pause();
            playButton.setBackgroundResource(R.drawable.ic_play_circle_outline_48px);
        }

    }
}

