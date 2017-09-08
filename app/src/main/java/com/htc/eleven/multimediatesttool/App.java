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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

    public void restoreToFile() {

//        File testFile = new File(Environment.getExternalStorageDirectory()+File.separator+"test_output.xml");
//        try {
//            testFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }

        Element root = document.createElement("Result");

        for (int i=0; i<mData.size(); i++) {

            Element e = document.createElement("Item");
            e.setAttribute("id", mData.get(i).getmId());
            e.setAttribute("class", mData.get(i).getmClass());

            String[] subItems = mData.get(i).getSubItems();
            for (int j=0; j<subItems.length; j++) {
                String key = subItems[j];
                String value = mData.get(i).getmElement().getElementsByTagName((subItems[j])).item(0).getTextContent();

                Element sub = document.createElement(key);
                sub.setTextContent(value);

                e.appendChild(sub);
            }
            root.appendChild(e);
        }

        document.appendChild(root);

        /**
         * create another XML file to store new added content and previous content.
         * */
        // transfer and create xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;

        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            /**
             * truncate file to size 0.
             * */
            new FileOutputStream(file).getChannel().truncate(0).close();

            transformer.transform(new DOMSource(document), new StreamResult(file));

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * clear mData.
         * */

        mData.clear();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.i(TAG, "onTerminate()");

        if(mFileLoaded)
            restoreToFile();
    }

    /**
     * copy assert file into external storage when Application was launched first time.
     * */
    private void copyResultFileToExternalStorage(InputStream assertFile, FileOutputStream outputStream) {

        /**
         * stupid method to use byte to handle string text data !!!
         * */
//        byte[] b = new byte[2048];
//        try {
//            while(assertFile.read(b) !=-1){
//                outputStream.write(b, 0, b.length);
//                System.out.println("==================" + b.toString());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(assertFile);
            char[] data = new char[assertFile.available()];
            inputStreamReader.read(data);
            inputStreamReader.close();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            outputStreamWriter.close();

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
                    InputStream assertFile = getAssets().open(testResultFile);
                    Toast.makeText(getApplicationContext(), "创建测试结果文件成功 ! " + assertFile.toString(), Toast.LENGTH_LONG).show();
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
//        mData.clear();
        mFileLoaded = false;
    }
}
