/**************************************************************************************************
* YouTube Downloader class that implements HaarigerHarald Youtube Extractor processes to download
* YouTube videos from their links.
* GitHub: https://github.com/HaarigerHarald/android-youtubeExtractor.git
***************************************************************************************************/
package com.example.ytdl;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.example.messagesutil.UIMessages;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YouTubeDownloader {

    private Context mContext;
    private DownloadManager download_manager;
    private final String TAG = "YouYubeDownloader";

    /**
     * Class Constructor
     */
    public YouTubeDownloader(Context activity_context){
        mContext = activity_context;
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
                        if (ytFiles != null) {
                            String title = "";
                            String filename = "";
                            String download_url = "";

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
                                        title = vMeta.getTitle();
                                        filename = title + ".mp3";

                                        Log.i(TAG,"Parsing and attempting download");
                                        Uri youtubeUri = Uri.parse(download_url);
                                        DownloadManager.Request request = new DownloadManager.Request(youtubeUri);
                                        request.setTitle(title);
                                        request.allowScanningByMediaScanner();
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                                        download_manager.enqueue(request);
                                        UIMessages.showToast(mContext,"Download Started");
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
