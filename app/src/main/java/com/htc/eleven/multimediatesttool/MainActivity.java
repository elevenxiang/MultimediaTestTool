package com.htc.eleven.multimediatesttool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "eleven-MainActivity";
    public static final String KEY = "Category";

    /**
     * permission strings for read/write external storage.
     * */
    private static final String[] mPermissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"};

    /**
     * request code & result.
     * */
    private static final int mRequestCode = 1;
    private static final int mRequestSuccessfully = 0;

    /**
     * for show string from native call.
     * */
//    private TextView ndkStringTextView;

    /**
     * Common button listener for each test item detail button.
     * */
    public class DetailButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,DetailResultActivity.class);
            switch (view.getId()) {
                case R.id.PlaybackDetail:
                    intent.putExtra(KEY, CellData.testItems[CellData.Playback_Test_Id]);
                    break;
                case R.id.RecordingDetail:
                    intent.putExtra(KEY, CellData.testItems[CellData.Recording_Test_Id]);
                    break;
                case R.id.VoiceCallDetail:
                    intent.putExtra(KEY, CellData.testItems[CellData.VoiceCall_Test_Id]);
                    break;
                case R.id.VoipCallDetail:
                    intent.putExtra(KEY, CellData.testItems[CellData.VoipCall_Test_Id]);
                    break;

            }

            startActivity(intent);
        }
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i(TAG, MainActivity.this + ", Here we go !");

        /**
         * request permission.
         * */
        requestPermissions(mPermissions,mRequestCode);

        Log.i(TAG, "we have already request permissions.");

        findViewById(R.id.PlaybackDetail).setOnClickListener(new DetailButtonClickListener());
        findViewById(R.id.RecordingDetail).setOnClickListener(new DetailButtonClickListener());
        findViewById(R.id.VoiceCallDetail).setOnClickListener(new DetailButtonClickListener());
        findViewById(R.id.VoipCallDetail).setOnClickListener(new DetailButtonClickListener());
        findViewById(R.id.Playback_TestTitle).setOnClickListener(this);
        findViewById(R.id.RecordingTestTitle).setOnClickListener(this);
        findViewById(R.id.VoiceCallTitle).setOnClickListener(this);
        findViewById(R.id.VoipCallTitle).setOnClickListener(this);

        /**
         *  add for ndk support.
         * */
//        ndkStringTextView = (TextView) findViewById(R.id.ndk_tv);
//        ndkStringTextView.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == mRequestCode) {
            for (int i=0; i<grantResults.length; i++) {
                if(grantResults[i] != mRequestSuccessfully)
                    finish(); // if user reject allow permissions, just exit directly.
            }
        }

        Log.i(TAG, "onRequestPermissionsResult Successfully !");

        if(!App.getApp().loadResultXMLFile()) {
            finish();
        }

        App.getApp().parserXMLFile();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.Playback_TestTitle:
                intent = new Intent(MainActivity.this,PlaybackTestActivity.class);
                startActivity(intent);
                break;
            case R.id.RecordingTestTitle:
                intent = new Intent(MainActivity.this,RecordingTestActivity.class);
                startActivity(intent);
                break;
            case R.id.VoiceCallTitle:
                intent = new Intent(MainActivity.this,VoiceCallTestActivity.class);
                startActivity(intent);
                break;
            case R.id.VoipCallTitle:
                intent = new Intent(MainActivity.this,VoipCallActivity.class);
                startActivity(intent);
                break;

//            case R.id.ndk_tv:
//                intent = new Intent(MainActivity.this,NDKDemoActivity.class);
//                startActivity(intent);
//                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        App.getApp().restoreToFile();
        Log.i(TAG, "onDestroy()");
    }
}
