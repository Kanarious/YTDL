/**************************************************************************************************
* This is a hidden activity that will start when application is called with a SEND intent with
* plain/text for data. The Activity will run the downloader with the link it received.
***************************************************************************************************/
package com.example.ytdl;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.messagesutil.UIMessages;

public class IntentDLActivity extends Activity {
    //--Class Objects--//
    YouTubeDownloader Downloader;

    private final String TAG = "IntentDLActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_d_l);

        //Check if application was launched with share
        if (savedInstanceState == null && Intent.ACTION_SEND.equals(getIntent().getAction())
                && getIntent().getType() != null && "text/plain".equals(getIntent().getType())) {
            //Check if necessary permissions are granted
            if ( isStoragePermissionGranted() ) {

                //Create and Set Youtube Downloader
                Downloader = new YouTubeDownloader(this, true);
                Downloader.setDownload_manager((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE));

                //Get link from Intent
                String yt_link = getIntent().getStringExtra(Intent.EXTRA_TEXT);

                //Check Link
                if (Downloader.checkLink(yt_link)) {
                    //Attempt Download
                    if (!Downloader.linkDownload(yt_link)) {
                        UIMessages.showToast(this, "Failed to Download");
                    }
                    else{
                        UIMessages.showToast(this,"Getting Data...");
                    }
                } else {
                    UIMessages.showToast(this, "Invalid Link");
                }
            }
            //Terminate Activity
//            finish();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}
