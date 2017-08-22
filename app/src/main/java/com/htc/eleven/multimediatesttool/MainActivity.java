package com.htc.eleven.multimediatesttool;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "eleven-MainActivity";

    /**
     * permission strings for read/write external storage.
     * */
    private static final String[] mPermissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    /**
     * request code & result.
     * */
    private static final int mRequestCode = 1;
    private static final int mRequestSuccessfully = 0;

    /**
     * Common button listener for each test item detail button.
     * */
    public class DetailButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, DetailResultActivity.class);
            startActivity(intent);
        }
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

        findViewById(R.id.DetailBtn).setOnClickListener(new DetailButtonClickListener());
        findViewById(R.id.TestTitle).setOnClickListener(this);
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
        switch (view.getId()) {
            case R.id.TestTitle:
                Intent intent = new Intent(MainActivity.this,PlaybackTestActivity.class);
                startActivity(intent);
                break;
        }

    }
}
