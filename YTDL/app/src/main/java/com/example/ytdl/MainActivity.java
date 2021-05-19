package com.example.ytdl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.messagesutil.UIMessages;



public class MainActivity extends AppCompatActivity {

    //--Class Widgets--//
    EditText EDIT_link;

    //--Class Objects--//
    YouTubeDownloader Downloader;

    //Class Variables
    private final String TAG = "MainActivity";

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

    /**
     * Button Click procedure to initiate Downloader with YouTube link contained in 'EDIT_link' Edit Box
     * @param v
     */
    public void downloadClick(View v){
        //get link from edit box
        String link_ = EDIT_link.getText().toString();

        //Check for empty link
        if(link_.isEmpty()){
            UIMessages.showToast(this,"Please Input Youtube Link");
        }
        //check for invalid link
        else if (!Downloader.checkLink(link_)){
            UIMessages.showToast(this,"Invalid Link");
        }
        //Send link to Downloader
        else {
            Log.d(TAG,"Starting Downloader");
            Downloader.linkDownload(link_);
        }

    }

}
