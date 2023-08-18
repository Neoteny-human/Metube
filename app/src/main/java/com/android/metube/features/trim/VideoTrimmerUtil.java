package com.android.metube.features.trim;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.ReturnCode;
import com.android.metube.interfaces.VideoTrimListener;
import com.android.metube.utils.DeviceUtil;
import com.android.metube.utils.UnitConverter;
import com.android.metube.utils.callback.SingleCallback;
import com.android.metube.utils.thread.BackgroundExecutor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoTrimmerUtil {

    private static final String TAG = "비디오 트림";
    public static final long MIN_SHOOT_DURATION = 3000L;// 최소 편집시간 = 3초
    public static final int VIDEO_MAX_TIME = 10;// 10초
    public static final long MAX_SHOOT_DURATION = VIDEO_MAX_TIME * 1000L;//동영상을 자를 수 있는 길이 = 10초

    public static final int MAX_COUNT_RANGE = 10;  //seekBar영역에 사진이 몇 장 있습니까?
    private static final int SCREEN_WIDTH_FULL = DeviceUtil.getDeviceWidth();
    public static final int RECYCLER_VIEW_PADDING = UnitConverter.dpToPx(35);
    public static final int VIDEO_FRAMES_WIDTH = SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2;
    public static final int THUMB_WIDTH = (SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2) / VIDEO_MAX_TIME;
    private static final int THUMB_HEIGHT = UnitConverter.dpToPx(50);

    public static void trim(Context context, String inputFile, String outputFile, long startMs, long endMs, final VideoTrimListener callback) {
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

//    outputFile = outputFile + "/" + outputName;

        String start = convertSecondsToTime(startMs / 1000);
        String duration = convertSecondsToTime((endMs - startMs) / 1000);
        //String start = String.valueOf(startMs);
        //String duration = String.valueOf(endMs - startMs);



        /* ffmpeg 비디오 자르기 설명:
    ffmpeg -ss START -t DURATION -i INPUT -codec copy -avoid_negative_ts 1 OUTPUT
    -ss 시작시간 (예: 00:00.20)은 20초에 시작함을 의미합니다.
    -t 기간 (예: 00:00:10) 10초에 비디오를 가로채는 것을 의미합니다.
    -i 입력, 공백, 입력 비디오 파일
    -codec copy -avoid_negative_ts 1 사용할 비디오 및 오디오의 인코딩 형식을 나타냅니다.
    여기서 cpoy로 지정된 것은 원본을 의미합니다.
    INPUT, 입력 비디오 파일
    OUTPUT, 비디오 파일 출력*/


        //TODO: Here are some instructions
        //https://trac.ffmpeg.org/wiki/Seeking
        //https://superuser.com/questions/138331/using-ffmpeg-to-cut-up-video

//    String cmd = "-ss " + start + " -t " + duration + " -accurate_seek" + " -i " + inputFile + " -codec copy -avoid_negative_ts 1 " + outputFile;
//    Log.d(TAG, "trim: "+inputFile + outputFile);
        //String cmd = "-ss " + start + " -i " + inputFile + " -ss " + start + " -t " + duration + " -vcodec copy " + outputFile;
        //{"ffmpeg", "-ss", "" + startTime, "-y", "-i", inputFile, "-t", "" + induration, "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputFile}
        //String cmd = "-ss " + start + " -y " + "-i " + inputFile + " -t " + duration + " -vcodec " + "mpeg4 " + "-b:v " + "2097152 " + "-b:a " + "48000 " + "-ac " + "2 " + "-ar " + "22050 "+ outputFile;
//    String[] command = cmd.split(" ");

        //        for(int i = 0; i<command.length; i++) {
//        Log.d(TAG, "trim: 커맨드: " + command[i]);
//    }

        final String outputName = "VID_" + timeStamp + ".mp4";
//    Log.d(TAG, "trim: 시작시간:"+startMs+"끝시간:"+endMs);
//    new Mp4Composer(inputFile, "/storage/emulated/0/Movies/"+outputName)
//            .trim(startMs, endMs)
//            .listener(new Mp4Composer.Listener() {
//              @Override
//              public void onProgress(double progress) {
//                Log.d(TAG, "onProgress: 진행 중"+progress);
//              }
//
//              @Override
//              public void onCurrentWrittenVideoTime(long timeUs) {
//                Log.d(TAG, "onCurrentWrittenVideoTime: "+timeUs);
//              }
//
//              @Override
//              public void onCompleted() {
//
//                callback.onFinishTrim(outputName);
//                Log.d(TAG, "onCompleted: 완료");
//              }
//
//              @Override
//              public void onCanceled() {
//                Log.d(TAG, "onCanceled: 취소");
//              }
//
//              @Override
//              public void onFailed(Exception exception) {
//                Log.d(TAG, "onFailed: 실패");
//              }
//            })
//            .start();




//    try {
//      final String tempOutFile = outputFile;
//      FFmpeg.getInstance(context).execute(command, new ExecuteBinaryResponseHandler() {
//
//        @Override public void onSuccess(String s) {
//          Log.d(TAG, "onSuccess: 성공에 들어오긴함.");
//          callback.onFinishTrim(tempOutFile);
//        }
//
//        @Override public void onStart() {
//          callback.onStartTrim();
//        }
//
//        @Override public void onFailure(String s){
//          Log.d(TAG, "onFailure: 실패입니다.");
//        }
//      });
//    } catch (Exception e) {
//      e.printStackTrace();
//    }



        FFmpegSession session = FFmpegKit.execute("-ss "+start+" -t "+duration+" -i /storage/emulated/0/Movies/VID_20230125_012657.mp4 -c:v copy /storage/emulated/0/Movies/"+outputName);
        if (ReturnCode.isSuccess(session.getReturnCode())) {
            callback.onFinishTrim(outputName);
            Log.d(TAG, "trim: 성공입니다.");
        } else if (ReturnCode.isCancel(session.getReturnCode())) {
            Log.d(TAG, "trim: 취소입니다.");

        } else {

            // FAILURE
            Log.d(TAG, String.format("Command failed with state %s and rc %s.%s", session.getState(), session.getReturnCode(), session.getFailStackTrace()));

        }


//    int rc = FFmpeg.execute("-ss 10 -t 10 -i /storage/emulated/0/Movies/VID_20230125_012657.mp4 -c copy /storage/emulated/0/Movies/VID_123.mp4");
//
//    if(rc == RETURN_CODE_SUCCESS){
//      Log.d(TAG, "trim: 성공입니다.");
//    } else if(rc == RETURN_CODE_CANCEL){
//      Log.d(TAG, "trim: 취소되었습니다.");
//    } else {
//      Log.d(TAG, "trim: 실패!");
//    }



    }

    public static void uploadWithTransferUtility(Context context, String fileName, File file) {

        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIA2N3JELS7ZWGK46UY", "PgWxVU8ona51jGiIyknHjY5LC2aGnGFO/WUgp0Yz");    // IAM 생성하며 받은 것 입력
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));


        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(context).build();
        TransferNetworkLossHandler.getInstance(context);

        TransferObserver uploadObserver = transferUtility.upload("metube-bucket", fileName, file);    // (bucket api, file이름, file객체)

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    // Handle a completed upload
                }
            }

            @Override
            public void onProgressChanged(int id, long current, long total) {
                int done = (int) (((double) current / total) * 100.0);
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX:" + ex.toString());
            }
        });

    }




    public static void shootVideoThumbInBackground(final Context context, final Uri videoUri, final int totalThumbsCount, final long startPosition,
                                                   final long endPosition, final SingleCallback<Bitmap, Integer> callback) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0L, "") {
            @Override public void execute() {
                try {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(context, videoUri);
                    // Retrieve media data use microsecond
                    long interval = (endPosition - startPosition) / (totalThumbsCount - 1);
                    for (long i = 0; i < totalThumbsCount; ++i) {
                        long frameTime = startPosition + interval * i;
                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        if(bitmap == null) continue;
                        try {
                            bitmap = Bitmap.createScaledBitmap(bitmap, THUMB_WIDTH, THUMB_HEIGHT, false);
                        } catch (final Throwable t) {
                            t.printStackTrace();
                        }
                        callback.onSingleCallback(bitmap, (int) interval);
                    }
                    mediaMetadataRetriever.release();
                } catch (final Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    public static String getVideoFilePath(String url) {
        if (TextUtils.isEmpty(url) || url.length() < 5) return "";
        if (url.substring(0, 4).equalsIgnoreCase("http")) {

        } else {
            url = "file://" + url;
        }

        return url;
    }

    private static String convertSecondsToTime(long seconds) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (seconds <= 0) {
            return "00:00";
        } else {
            minute = (int) seconds / 60;
            if (minute < 60) {
                second = (int) seconds % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) return "99:59:59";
                minute = minute % 60;
                second = (int) (seconds - hour * 3600 - minute * 60);
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }
}
