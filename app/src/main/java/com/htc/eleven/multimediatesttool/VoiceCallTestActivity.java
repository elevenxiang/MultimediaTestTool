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

public class VoiceCallTestActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText phoneNumber;
    private Button dialOut;

    private CheckBox cb1, cb2, cb3, cb4;
    private Button commitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call_test);

        /**
         * configure EditTest to only accept number.
         * add property: android:inputType="phone"
         * */
        phoneNumber = (EditText) findViewById(R.id.phone_number_edit);

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
        Uri uri = Uri.parse("tel:"+phoneNumber.getText());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    private void commitResult() {

        Element e = App.getApp().getmData().get(CellData.VoiceCall_Test_Id).getmElement();
        String[] subItems = App.getApp().getmData().get(CellData.VoiceCall_Test_Id).getSubItems();

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

            Toast.makeText(VoiceCallTestActivity.this,"提交测试成功 !", Toast.LENGTH_SHORT).show();
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
