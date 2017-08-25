package com.htc.eleven.multimediatesttool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Element;


public class VoipCallActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText voipName;
    private Button dialOut;

    private CheckBox cb1, cb2, cb3, cb4;
    private Button commitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip_call);

        /**
         * configure EditTest to only accept number.
         * add property: android:inputType="phone"
         * */
        voipName = (EditText) findViewById(R.id.sky_contact_edit);

        dialOut = (Button) findViewById(R.id.DialOutBtn);
        commitBtn = (Button) findViewById(R.id.commitBtn);

        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        cb3 = (CheckBox) findViewById(R.id.cb3);
        cb4 = (CheckBox) findViewById(R.id.cb4);

        dialOut.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
    }

    private void dialOut() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("skype:" + voipName.getText()));


        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(VoipCallActivity.this, "Please install Skype firstly !", Toast.LENGTH_LONG).show();
            finish();

            //TODO, install apk in code, need FileProvider to share file between Applications.
            /**
             * install the Skype apk.
             *
            {
                String installFile = Environment.getExternalStorageDirectory() + File.separator + "Skype.apk";
                String command = "chmod 777 " + installFile;
                System.out.println(command);
                try {
                    Runtime.getRuntime().exec(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent_install = new Intent(Intent.ACTION_VIEW);
                intent_install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_install.setDataAndType(Uri.parse("file://"+installFile),"application/vnd.android.package-archive");
                startActivity(intent_install);
            }
             */
        } else
            startActivity(intent);


//        Toast.makeText(VoipCallActivity.this, voipName.getText(), Toast.LENGTH_LONG).show();
    }

    private void commitResult() {

        Element e = App.getApp().getmData().get(CellData.VoipCall_Test_Id).getmElement();
        String[] subItems = App.getApp().getmData().get(CellData.VoipCall_Test_Id).getSubItems();

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
                case 3:
                    data = cb4.isChecked()?"Passed":"Failed";
                    break;
            }
            e.getElementsByTagName(subItems[i]).item(0).setTextContent(data);

            Toast.makeText(VoipCallActivity.this,"提交测试成功 !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.DialOutBtn:
                dialOut();
                break;

            case R.id.commitBtn:
                commitResult();
                finish();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}