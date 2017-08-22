package com.htc.eleven.multimediatesttool;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static java.lang.System.in;
import static java.lang.System.out;

public class DetailResultActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailResultActivity";
    private static final String lineBreak = System.getProperty("line.separator");

    private TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_result);

        /**
         * TextView to show playback result.
         * */
        resultTextView = (TextView) findViewById(R.id.result_detail);

        findViewById(R.id.clearDataBtn).setOnClickListener(this);

        Log.i(TAG, "onCreate() !");
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i=0; i<App.getApp().getmData().size(); i++) {

            // get id.
            String id = App.getApp().getmData().get(i).getmId();
            // get mClass.
            String strClass = App.getApp().getmData().get(i).getmClass();
            // get mElement
            Element e = App.getApp().getmData().get(i).getmElement();

            // get subItems.
            String[] subItems = App.getApp().getmData().get(i).getSubItems();

            resultTextView.append(id + ".    " + strClass + lineBreak);

            for (int j=0; j<subItems.length; j++) {
                resultTextView.append("       " + subItems[j] + ": " + e.getElementsByTagName(subItems[j]).item(0).getTextContent() + lineBreak);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearDataBtn:
                App.getApp().deleteFile();
                break;
        }
    }
}
