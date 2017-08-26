package com.htc.eleven.multimediatesttool;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Element;

import java.io.File;

public class VideoRecordingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startCamcor;
    private Button playbackCamcor;

    private Button commitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recording);

        startCamcor = (Button) findViewById(R.id.startCaptureBtn);
        playbackCamcor = (Button) findViewById(R.id.startPlayBtn);
        commitBtn = (Button) findViewById(R.id.commitBtn);

        startCamcor.setOnClickListener(this);
        playbackCamcor.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
    }

    private AlertDialog result;

    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case AlertDialog.BUTTON_POSITIVE:
                    commitResult(true);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    commitResult(false);
                    break;
            }
        }
    };

    private void commitResult( boolean result) {

        Element e = App.getApp().getmData().get(CellData.Video_Recording_Test_Id).getmElement();
        String[] subItems = App.getApp().getmData().get(CellData.Video_Recording_Test_Id).getSubItems();

        String data = (result == true)?"Passed":"Failed";
        for (int i=0; i<subItems.length; i++) {
            e.getElementsByTagName(subItems[i]).item(0).setTextContent(data);
        }

        Toast.makeText(VideoRecordingActivity.this,"提交测试成功 !", Toast.LENGTH_SHORT).show();

        finish();
    }
    private void showResult() {
        result = new AlertDialog.Builder(this).create();
        result.setTitle(getResources().getString(R.string.video_recording_test_dialog));
        result.setMessage(getResources().getString(R.string.video_recording_test_message));
        result.setButton(AlertDialog.BUTTON_NEGATIVE,getResources().getString(R.string.video_no), listener);
        result.setButton(AlertDialog.BUTTON_POSITIVE,getResources().getString(R.string.video_yes),listener);
        result.show();
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.startCaptureBtn:
                Intent camcorder = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivity(camcorder);
                break;
            case R.id.startPlayBtn:
                startActivity(new Intent(VideoRecordingActivity.this, VideoTestActivity.class));
                break;

            case R.id.commitBtn:
                showResult();
                break;
        }
    }
}
