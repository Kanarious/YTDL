/**************************************************************************************************
* YouTube Downloader class that implements HaarigerHarald Youtube Extractor processes to download
* YouTube videos from their links.
* GitHub: https://github.com/HaarigerHarald/android-youtubeExtractor.git
***************************************************************************************************/
package com.example.ytdl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.widget.EditText;

import com.example.messagesutil.UIMessages;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YouTubeDownloader {

    private Context mContext;
    private DownloadManager download_manager;
    private final String TAG = "YouTubeDownloader";
    private Boolean shutdown;

    private String download_title;
    private String download_url;
    private String download_filename;

    /**
     * Class Constructor
     */
    public YouTubeDownloader(Context activity_context, Boolean terminate_after_download){
        mContext = activity_context;
        shutdown = terminate_after_download;
    }

    /**
     * Sets download manager for YouTube Downloader
     * @param download_manager
     */
    public void setDownload_manager(DownloadManager download_manager) {
        this.download_manager = download_manager;
    }

    public Boolean checkLink(String link_){
        if (link_.isEmpty()){
            return false;
        }
        else{
            return (link_.contains("://youtu.be/") || link_.contains("youtube.com/watch?v="));
        }

    }

    /**
     * Procedure to download YouTube video from video link with YouTubeExtractor Class
     * @param link_ = YouTube Video Link
     */
    @SuppressLint("StaticFieldLeak")
    public Boolean linkDownload(String link_){
        //Check if manager is initialized
        if (download_manager == null){
            return false;
        }
        else {
            Boolean return_value = true;
            //Attempt Youtube Video Audio Download
            try {
                new YouTubeExtractor(mContext) {
                    @Override
                    public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                        //Ensure the files are not empty
                        if (ytFiles != null) {

                            //initialize Class Variables
                            download_title = "";
                            download_filename = "";
                            download_url = "";
                            Log.i(TAG,"Searching for audio ITag");

                            //Loop through all video iTags
                            for (int i = 0, itag; i < ytFiles.size(); i++) {
                                itag = ytFiles.keyAt(i);
                                YtFile ytFile = ytFiles.get(itag);

                                //Check if Youtube File is Assigned and is audio file
                                if (ytFile != null && ytFile.getFormat().getHeight() == -1) {
                                    Log.i(TAG,"ITag found: "+String.valueOf(itag));
                                    download_url = ytFile.getUrl();

                                    //Check if download url got assigned properly
                                    if (download_url != null && !download_url.isEmpty()) {
                                        download_title = vMeta.getTitle();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle("Input Title");

                                        // Set up the input
                                        final EditText input = new EditText(mContext);

                                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                        input.setInputType(InputType.TYPE_CLASS_TEXT /**| InputType.TYPE_TEXT_VARIATION_PASSWORD**/);
                                        input.setText(download_title);
                                        builder.setView(input);
                                        builder.setCancelable(false);

                                        // Set up the buttons
                                        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //set filename
                                                download_title = input.getText().toString();
                                                download_filename = download_title + ".mp3";

                                                Log.i(TAG,"Parsing and attempting download");
                                                Uri youtubeUri = Uri.parse(download_url);

                                                Log.i(TAG, "Creating download manager request");
                                                DownloadManager.Request request = new DownloadManager.Request(youtubeUri);

                                                Log.i(TAG, "Setting download manager request");
                                                request.setTitle(download_title);
                                                request.allowScanningByMediaScanner();
                                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, download_filename);
                                                Log.i(TAG, "Sending download manager request");

                                                Boolean download_started = true;
                                                try {
                                                    download_manager.enqueue(request);
                                                }
                                                catch (Exception e){
                                                    Log.e(TAG, "onExtractionComplete: ", e);
                                                    download_started = false;
                                                }
                                                if (download_started) {
                                                    UIMessages.showToast(mContext, "Download Started");
                                                }
                                                else{
                                                    UIMessages.showToast(mContext, "DOWNLOAD FAILED");
                                                }

                                                //Check if Activity must close after attempt
                                                if (shutdown){
                                                    ((Activity)mContext).finish();
                                                }
                                            }
                                        });

                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                                //Check if Activity must close after cancel
                                                if (shutdown){
                                                    ((Activity)mContext).finish();
                                                }
                                            }
                                        });

                                        builder.show();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }.extract(link_, true, true);
            }
            catch(Exception e){
                Log.e(TAG, "linkDownload FAILED: ", e);
                return_value = false;
            }
            return return_value;
        }
    }
}
