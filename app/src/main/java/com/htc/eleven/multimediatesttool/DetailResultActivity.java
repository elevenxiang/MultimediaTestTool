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
    private String mFilePath;
    private File file;

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
        /**
         * get file location, and judge if it exist.
         * */
        mFilePath = Environment.getExternalStorageDirectory() + File.separator + App.getApp().getTestResultFile();
        file = new File(mFilePath);
        Log.i(TAG,String.format("Create %s !", file.getAbsolutePath()));
        if(!file.exists()) {
            try {
                if(!file.createNewFile()) {
                    Toast.makeText(DetailResultActivity.this, "创建测试结果文件失败 !", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.i(TAG, "Create file: "+file.getAbsolutePath()+" Successfully !");
                    Toast.makeText(DetailResultActivity.this, "创建测试结果文件成功 !", Toast.LENGTH_LONG).show();
                    InputStream assertFile = getAssets().open(App.getApp().getTestResultFile());
                    FileOutputStream outputStream = new FileOutputStream(file);

                    copyResultFileToExternalStorage(assertFile,outputStream);

                    outputStream.flush();
                    assertFile.close();
                    outputStream.close();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            Element element = document.getDocumentElement();

            NodeList test_list = element.getElementsByTagName("Item");
            for (int i=0; i<test_list.getLength(); i++) {
                Element e = (Element) test_list.item(i);
                String id = e.getAttribute("id");
                Log.i(TAG, id);

                String itemClass = e.getAttribute("class");
                switch (itemClass) {
                    case "Playback":
                        resultTextView.setText(id + ".    " + itemClass + lineBreak);
                        resultTextView.append("       " + "Normal_Playback" + ": " + e.getElementsByTagName("Normal_Playback").item(0).getTextContent()+lineBreak);
                        resultTextView.append("       " + "Headset_Playback" + ": " + e.getElementsByTagName("Headset_Playback").item(0).getTextContent()+lineBreak);
                        resultTextView.append("       " + "A2DP_Playback" + ": " + e.getElementsByTagName("A2DP_Playback").item(0).getTextContent()+lineBreak);
                        e.getElementsByTagName("A2DP_Playback").item(0).setTextContent("true");
                        //TODO move below variable to App to set as Global variables.
                        break;
                    case "VoiceCall":
                        break;
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * copy assert file into external storage when Application was launched first time.
     * */
    private void copyResultFileToExternalStorage(InputStream assertFile, FileOutputStream outputStream) {

        byte[] b = new byte[1024];
        try {
            while(assertFile.read(b) !=-1){
                outputStream.write(b, 0, b.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearDataBtn:
                file.delete();
                break;
        }
    }
}
