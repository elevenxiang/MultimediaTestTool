package com.htc.eleven.multimediatesttool;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.w3c.dom.Element;

import java.io.IOException;

public class PlaybackTestActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final static String mMusicFIle = "test.mp3";
    private AssetFileDescriptor mFd = null;

    private Button mStart = null;
    private Button mStop = null;
    private MediaPlayer player = null;
    private Boolean mRunning = false;
    private Boolean mPaused = false;


    private Button commitAnswer;
    private CheckBox cb1,cb2,cb3;
    private static final String TAG = "PlaybackTestActivity";

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


        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        cb3 = (CheckBox) findViewById(R.id.cb3);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);

        commitAnswer = (Button) findViewById(R.id.commitBtn);
        commitAnswer.setOnClickListener(this);
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

            case R.id.commitBtn:
                commitResult();
                mStop.callOnClick();

                /**
                 * shall we just exit test UI when after committing result ?
                 * */
                finish();
                break;
        }
    }

    private void commitResult() {

        Element e = App.getApp().getmData().get(CellData.Playback_Test_Id).getmElement();
        String[] subItems = App.getApp().getmData().get(CellData.Playback_Test_Id).getSubItems();

        String data = "Failed";
        for (int i=0; i<subItems.length; i++) {
            switch (i) {
                case 0:
                    data = cb1.isChecked()?"Passed":"Failed";
                    break;
                case 1:
                    data = cb2.isChecked()?"Passed":"Failed";
                    break;
                case 2:
                    data = cb3.isChecked()?"Passed":"Failed";
                    break;
            }
            e.getElementsByTagName(subItems[i]).item(0).setTextContent(data);

            Toast.makeText(PlaybackTestActivity.this,"提交测试成功 !", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        System.out.println(TAG+compoundButton.isChecked());
    }
}