package com.example.ytdl;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseArray;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YouTubeDownloader {

    private Context mContext;
    private DownloadManager download_manager;

    /**
     * Class Constructor
     */
    public YouTubeDownloader(Context activity_context){
        mContext = activity_context;
    }

    public void setDownload_manager(DownloadManager download_manager) {
        this.download_manager = download_manager;
    }

    /**
     * Procedure to download YouTube video from video link with YouTubeExtractor Class
     * @param link_ = YouTube Video Link
     */
    public void linkDownload(String link_){
        new YouTubeExtractor(mContext) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null && download_manager != null) {
                    String title = "";
                    String filename = "";
                    String download_url = "";
                    //Loop through all video iTags
                    for (int i = 0, itag; i < ytFiles.size(); i ++){
                        itag = ytFiles.keyAt(i);
                        YtFile ytFile = ytFiles.get(itag);
                        //Check if Youtube File is Assigned and is audio file
                        if (ytFile != null && ytFile.getFormat().getHeight() == -1){
                            download_url = ytFile.getUrl();
                            //Check if download url got assigned properly
                            if (download_url != null && !download_url.isEmpty()){
                                title = vMeta.getTitle();
                                filename = title+".mp3";

                                Uri youtubeUri = Uri.parse(download_url);
                                DownloadManager.Request request = new DownloadManager.Request(youtubeUri);
                                request.setTitle(title);
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
                                download_manager.enqueue(request);
                                return;
                            }
                        }
                    }
                }
            }
        }.extract(link_, true, true);
    }
}
