package com.htc.eleven.multimediatesttool;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;

public class RecordingTestActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, MediaPlayer.OnCompletionListener {

    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;

    private Button startRecording;
    private Button startPlaying;
    private Button commitBtn;
    private TextView showStatus;
    private CheckBox cb1, cb2;

    private boolean isRecording = false;
    private boolean isPlaying = false;

    /**
     * we choose aac format as default.
     * */
    private static final String recordingFile = "recording.aac";
    private static final String filePath = Environment.getExternalStorageDirectory() + File.separator + recordingFile;

    private boolean prepareRecording() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setAudioSamplingRate(48000);
        mediaRecorder.setAudioChannels(2);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(filePath);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void startRecording() {

        prepareRecording();

        mediaRecorder.start();
        isRecording = true;

        showStatus.setText(R.string.onRecording);
    }

    private void stopRecording() {
        if(isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            isRecording = false;
        }
        showStatus.setText(R.string.onReady);
    }

    private void preparePlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            /**
             * when user Uri to load file, we need add "file:///" at the beginning of the path.
             * */
//            mediaPlayer.setDataSource(RecordingTestActivity.this, Uri.parse("file://" + filePath));

            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(this);
    }
    private void startPlaying() {
        preparePlaying();
        mediaPlayer.start();

        isPlaying = true;

        showStatus.setText(R.string.onPlaying);

    }

    private void stopPlaying() {
        if(isPlaying) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
        showStatus.setText(R.string.onReady);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_test);

        startRecording = (Button) findViewById(R.id.RecordingBtn);
        startPlaying = (Button) findViewById(R.id.PlayingBtn);
        commitBtn = (Button) findViewById(R.id.commitBtn);

        showStatus = (TextView) findViewById(R.id.showStatus);

        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);

        /**
         * set onClick Listener.
         * */

        startPlaying.setOnClickListener(this);
        startRecording.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

        /**
         * set onCheck Listener.
         * */
        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
    }

    private void commitResult() {

        Element e = App.getApp().getmData().get(CellData.Recording_Test_Id).getmElement();
        String[] subItems = App.getApp().getmData().get(CellData.Recording_Test_Id).getSubItems();

        String data = "Failed";
        for (int i=0; i<subItems.length; i++) {
            switch (i) {
                case 0:
                    data = cb1.isChecked()?"Passed":"Failed";
                    break;
                case 1:
                    data = cb2.isChecked()?"Passed":"Failed";
                    break;
            }
            e.getElementsByTagName(subItems[i]).item(0).setTextContent(data);

            Toast.makeText(RecordingTestActivity.this,"提交测试成功 !", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RecordingBtn:
                if(isRecording)
                    stopRecording();
                else
                    startRecording();
                break;

            case R.id.PlayingBtn:
                /**
                 * stop current recording behavior before we play.
                 * */
                if(isRecording)
                    stopRecording();

                if(isPlaying)
                    stopPlaying();
                else
                    startPlaying();
                break;

            case R.id.commitBtn:
                if(isPlaying)
                    stopPlaying();
                if(isRecording)
                    stopRecording();

                commitResult();

                // exit recording UI after click commit button.
                finish();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(isPlaying) {
            stopPlaying();
        }
    }
}
