package com.htc.eleven.multimediatesttool;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class PlaybackTestActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String mMusicFIle = "test.mp3";
    private AssetFileDescriptor mFd = null;

    private Button mStart = null;
    private Button mStop = null;
    private MediaPlayer player = null;
    private Boolean mRunning = false;
    private Boolean mPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_test);

        // use AssertFileDescriptor to access music file.
        try {
            mFd = getAssets().openFd(mMusicFIle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mStart = (Button) findViewById(R.id.btnPlay);
        mStop = (Button) findViewById(R.id.btnStop);
        mStart.setOnClickListener(this);
        mStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPlay:
                if(!mRunning || mPaused) {
                    if(!mPaused) {
                        preparePlay();
                    }
                    player.start();
                    mRunning = true;
                    mPaused = false;
                    mStart.setText("Playing");
                } else {
                    player.pause();
                    mStart.setText("Paused");
                    mPaused = true;
                }
                break;
            case R.id.btnStop:
                player.stop();
                player.release();
                player = null;
                mRunning = false;
                mPaused = false;
                break;
        }
    }

    public void preparePlay(){

        String path = Environment.getExternalStorageDirectory() + "/Music/test.mp3";

        if(player==null) {
            player = new MediaPlayer();
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                player.stop();
                player.release();
                player = null;
                mRunning = false;
                mPaused = false;
                mStart.setText("Play");
            }
        });

        try {
//            player.setDataSource("/sdcard/Music/test.mp3");
//            System.out.println(path + "eleven ======");
//            player.setDataSource("/data/test.mp3");
//            player.setDataSource(path);

            // use AssertFileDescriptor to setDataSource().
            player.setDataSource(mFd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        /**
         * simulate click stop button to force stop, when exit playing test UI.
         * */
        mStop.callOnClick();
    }
}