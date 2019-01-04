package com.kalbe.kalbecallplanaedp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.kalbe.kalbecallplanaedp.Common.VMDownloadFile;
import com.kalbe.kalbecallplanaedp.Common.mFileAttachment;
import com.kalbe.kalbecallplanaedp.Common.mUserLogin;
import com.kalbe.kalbecallplanaedp.Common.tAkuisisiDetail;
import com.kalbe.kalbecallplanaedp.Common.tRealisasiVisitPlan;
import com.kalbe.kalbecallplanaedp.Data.clsHardCode;
import com.kalbe.kalbecallplanaedp.Repo.mFileAttachmentRepo;
import com.kalbe.kalbecallplanaedp.Repo.mUserLoginRepo;
import com.kalbe.kalbecallplanaedp.Repo.tAkuisisiDetailRepo;
import com.kalbe.kalbecallplanaedp.Repo.tRealisasiVisitPlanRepo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

/**
 * Created by Dewi Oktaviani on 11/22/2018.
 */

public class LongThread implements Runnable {
    int threadNo;
    Handler handler;
    VMDownloadFile data;
    public static final String TAG = "LongThread";
    Context context;

    public LongThread(Context context, int threadNo, VMDownloadFile data, Handler handler) {
        this.threadNo = threadNo;
        this.handler = handler;
        this.data = data;
        this.context = context;
    }

    @Override
    public void run() {
        Log.i(TAG, "Starting Thread : " + threadNo);
//        getBitmap(strUrl);
        final byte[] file =  getByte(data.getLink());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (data.getGroupDownload().equals(new clsHardCode().INFO_PROGRAM)){
                        mFileAttachment datum = (mFileAttachment) new mFileAttachmentRepo(context).findById(Integer.parseInt(data.getTxtId()));
                        datum.setBlobFile(file);
                        new mFileAttachmentRepo(context).createOrUpdate(datum);
                    }else if (data.getGroupDownload().equals(new clsHardCode().AKUISISI)){
                        tAkuisisiDetail datum = new tAkuisisiDetailRepo(context).findByDetailId(data.getTxtId());
                        datum.setTxtImg(file);
                        new tAkuisisiDetailRepo(context).createOrUpdate(datum);
                    }else if (data.getGroupDownload().equals(new clsHardCode().REALISASI_SATU)){
                        tRealisasiVisitPlan datum = new tRealisasiVisitPlanRepo(context).findBytxtId(data.getTxtId());
                        datum.setBlobImg1(file);
                        new tRealisasiVisitPlanRepo(context).createOrUpdate(datum);
                    }else if (data.getGroupDownload().equals(new clsHardCode().REALISASI_DUA)){
                        tRealisasiVisitPlan datum = new tRealisasiVisitPlanRepo(context).findBytxtId(data.getTxtId());
                        datum.setBlobImg2(file);
                        new tRealisasiVisitPlanRepo(context).createOrUpdate(datum);
                    }else if (data.getGroupDownload().equals(new clsHardCode().LOGIN)){
                        mUserLogin datum = new mUserLoginRepo(context).findByTxtId(data.getTxtId());
                        datum.setBlobImg(file);
                        new mUserLoginRepo(context).createOrUpdate(datum);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(context, String.valueOf(file), Toast.LENGTH_SHORT).show();
                sendMessage(threadNo, "Thread Completed");
            }
        });
        Log.i(TAG, "Thread Completed " + threadNo);
    }

    public void sendMessage(int what, String msg) {
        Message message = handler.obtainMessage(what, msg);
        message.sendToTarget();
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(url).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            // Do extra processing with the bitmap
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private byte[] getByte(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            String contentType = ucon.getHeaderField("Content-Type");
            boolean image = contentType.startsWith("image/");
            boolean text = contentType.startsWith("application/");
            if (image||text){
                byte[] data = null;
                InputStream is = ucon.getInputStream();
                int length =  ucon.getContentLength();
                data = new byte[length];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytesRead = is.read(data)) != -1) {
                    output.write(data, 0, bytesRead);
                }
                return output.toByteArray();
            }
            else {
                return null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }
}
