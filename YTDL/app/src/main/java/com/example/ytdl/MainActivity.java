package com.example.ytdl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;

import com.example.messagesutil.UIMessages;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //--Class Widgets--//
    EditText EDIT_link;

    //--Class Objects--//
    YouTubeDownloader Downloader;

    /**
     * Activity OnCreate Event Procedure
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create and Set Youtube Downloader
        Downloader = new YouTubeDownloader(this);
        Downloader.setDownload_manager((DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE));

        //Assign Widgets
        EDIT_link = findViewById(R.id.EDIT_link);
    }


    public void downloadClick(View v){
        //get link from edit box
        String link_ = EDIT_link.getText().toString();

        if(link_.isEmpty()){
            UIMessages.showToast();
        }
        else {
            Downloader.linkDownload(link_);
        }
        Log.d("UriExtactor","Starting Extractor");
    }

}
