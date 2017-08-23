package com.htc.eleven.multimediatesttool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Element;

public class DetailResultActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailResultActivity";
    private static final String lineBreak = System.getProperty("line.separator");

//    private TextView resultTextView;

    private String category;

    private LinearLayout textResultLayout;
    private TextView[] views = new TextView[CellData.Numbers];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_result);

        /**
         * TextView to show playback result.
         * */
//        resulttTextView = (TextView) findViewById(R.id.result_detail);

        findViewById(R.id.clearDataBtn).setOnClickListener(this);

        category = getIntent().getStringExtra(MainActivity.KEY);
        Log.i(TAG, "onCreate() !" + category);

        textResultLayout = (LinearLayout) findViewById(R.id.show_result_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * clear content to avoid duplicated showing.
         * */
//        resultTextView.setText("");

        for (int i=0; i<App.getApp().getmData().size(); i++) {

            views[i] = new TextView(DetailResultActivity.this);
            views[i].setText("");

            // get id.
            String id = App.getApp().getmData().get(i).getmId();
            // get mClass.
            String strClass = App.getApp().getmData().get(i).getmClass();
            // get mElement
            Element e = App.getApp().getmData().get(i).getmElement();

            // get subItems.
            String[] subItems = App.getApp().getmData().get(i).getSubItems();

            views[i].append(id + ".    " + strClass + lineBreak);

            for (int j=0; j<subItems.length; j++) {
                views[i].append("       " + subItems[j] + ": ==================>[" + e.getElementsByTagName(subItems[j]).item(0).getTextContent() + "]" + lineBreak);
            }

            views[i].append(lineBreak);

            if(category != null) {
                if (category.equals(CellData.testItems[i]))
                    views[i].setTextColor(getResources().getColor(R.color.red, null));
            }
            textResultLayout.addView(views[i]);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * clear content to avoid duplicated showing.
         * */
//        resultTextView.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        /**
         * clear content to avoid duplicated showing.
         * */
//        resultTextView.setText("");
    }
}
