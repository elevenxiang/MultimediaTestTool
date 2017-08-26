package com.htc.eleven.multimediatesttool;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class VideoTestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    /**
     * Uri for access media database.
     * */
    //private Uri mMusicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    /**
     * String[] for query column.
     * */
    private String[] mColumns = new String[] {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Audio.Media.DURATION};

    PullToRefreshListView video_list;

    List<String>  mData ;
    ArrayAdapter mAdapter;

    private Button commitBtn;
    private void refresh() {

//        Cursor list = getContentResolver().query(mVideoUri,mColumns,"duration>10000", null, null);
        Cursor list = getContentResolver().query(mVideoUri,mColumns,null, null, null);

        mData.add("======== Pull to Refresh ! =======");
        if(list.moveToFirst()) {
            int index;
            String path; // store the video file path.
            int list_id = 1; // used to list items from 1 to .... N.

            do {
                index = list.getColumnIndex(MediaStore.Video.Media.DATA);
                path = list.getString(index);

//                index = list.getColumnIndex(MediaStore.Video.Media._ID);
//                id = list.getString(index);

                mData.add(list_id+". " +path);
                list_id++;

            } while (list.moveToNext());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);

        commitBtn = (Button) findViewById(R.id.commitBtn);
        commitBtn.setOnClickListener(this);
        video_list = (PullToRefreshListView) findViewById(R.id.video_list);

        mData = new ArrayList<String>();
        refresh();
        mAdapter = new ArrayAdapter<String>(VideoTestActivity.this,android.R.layout.simple_list_item_1, mData);

        video_list.setAdapter(mAdapter);
        video_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new AsyncTask<Void,Void,Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        mData.clear();
                        refresh();
                        mAdapter.notifyDataSetChanged();
                        video_list.onRefreshComplete();

                        if(mData.size() > 2)
                            Toast.makeText(VideoTestActivity.this,"Scan and Refresh Completed!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(VideoTestActivity.this,"不好意思，没搜到任何视频!", Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            }
        });

        video_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        /**
         * i is beginning with 1, so we need i-1 to access array data, or it will be over flow.
         * here, substring(3), we use it to filter the id and "." to get full and right file path.
         * */
        String data = mData.get(i-1).substring(3);

//        System.out.println("====" + data);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(data), "video/mp4");

        startActivity(intent);
    }

    private AlertDialog result;

    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case AlertDialog.BUTTON_POSITIVE:
                    commitResult(true);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    commitResult(false);
                    break;
            }
        }
    };

    private void commitResult( boolean result) {

        Element e = App.getApp().getmData().get(CellData.Video_Test_Id).getmElement();
        String[] subItems = App.getApp().getmData().get(CellData.Video_Test_Id).getSubItems();

        String data = (result == true)?"Passed":"Failed";
        for (int i=0; i<subItems.length; i++) {
            e.getElementsByTagName(subItems[i]).item(0).setTextContent(data);
        }

        Toast.makeText(VideoTestActivity.this,"提交测试成功 !", Toast.LENGTH_SHORT).show();

        finish();
    }
    private void showResult() {
        result = new AlertDialog.Builder(this).create();
        result.setTitle(getResources().getString(R.string.video_test_dialog));
        result.setMessage(getResources().getString(R.string.video_test_message));
        result.setButton(AlertDialog.BUTTON_NEGATIVE,getResources().getString(R.string.video_no), listener);
        result.setButton(AlertDialog.BUTTON_POSITIVE,getResources().getString(R.string.video_yes),listener);
        result.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commitBtn:
                showResult();
                break;
        }
    }
}
