package com.htc.eleven.multimediatesttool;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by eleven on 17-8-21.
 */

public class App extends Application {

    private static final String TAG = "eleven-App";
    private static App mApp;
    private static final String testResultFile = "results.xml";

    private String mFilePath;
    private File file;
    private boolean mFileLoaded = false;

    private ArrayList<CellData> mData;
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "App Created !");

        mApp = (App) getApplicationContext();

        mData = new ArrayList<>(CellData.Numbers);
    }

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
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
    public boolean loadResultXMLFile() {
        /**
         * get file location, and judge if it exist.
         * */
        mFilePath = Environment.getExternalStorageDirectory() + File.separator + testResultFile;
        file = new File(mFilePath);
        Log.i(TAG,String.format("Create %s !", file.getAbsolutePath()));
        if(!file.exists()) {
            try {
                if(!file.createNewFile()) {
                    Toast.makeText(getApplicationContext(), "创建测试结果文件失败 !", Toast.LENGTH_LONG).show();
                    mFileLoaded = false;
                    return false;
                } else {
                    Log.i(TAG, "Create file: "+file.getAbsolutePath()+" Successfully !");
                    Toast.makeText(getApplicationContext(), "创建测试结果文件成功 !", Toast.LENGTH_LONG).show();
                    InputStream assertFile = getAssets().open(testResultFile);
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

        mFileLoaded = true;

        return true;
    }

    public void parserXMLFile() {

        if(mFileLoaded) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);
                Element element = document.getDocumentElement();

                NodeList test_list = element.getElementsByTagName("Item");
                for (int i = 0; i < test_list.getLength(); i++) {
                    Element e = (Element) test_list.item(i);

                    CellData data = new CellData(e.getAttribute("id"), e.getAttribute("class"), e);
                    mData.add(data);
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Result file wasn't loaded yet !", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<CellData> getmData() {
        return mData;
    }

    public void deleteFile() {
        file.delete();
        mFileLoaded = false;
    }
}
