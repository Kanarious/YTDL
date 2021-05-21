/**************************************************************************************************
* This is a hidden activity that will start when application is called with a SEND intent with
* plain/text for data. The Activity will run the downloader with the link it received.
***************************************************************************************************/
package com.example.ytdl;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.messagesutil.UIMessages;

public class IntentDLActivity extends Activity {
    //--Class Objects--//
    YouTubeDownloader Downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if application was launched with share
        if (savedInstanceState == null && Intent.ACTION_SEND.equals(getIntent().getAction())
                && getIntent().getType() != null && "text/plain".equals(getIntent().getType())) {
            //Create and Set Youtube Downloader
            Downloader = new YouTubeDownloader(this);
            Downloader.setDownload_manager((DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE));

            //Get link from Intent
            String yt_link = getIntent().getStringExtra(Intent.EXTRA_TEXT);

            //Check Link
            if ( Downloader.checkLink(yt_link) ){
                //Attempt Download
                if ( !Downloader.linkDownload(yt_link) ){
                    UIMessages.showToast(this,"Failed to Download");
                };
            }
            else{
                UIMessages.showToast(this,"Invalid Link");
            }
            //Terminate Activity
            finish();
        }
    }
}
