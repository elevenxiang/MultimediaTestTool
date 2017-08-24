package com.htc.eleven.multimediatesttool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NDKDemoActivity extends AppCompatActivity {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndkdemo);

        tv = (TextView) findViewById(R.id.ndk_tv);

        tv.setText(getNativeServiceName());
    }

    public native String getNativeServiceName();
}
