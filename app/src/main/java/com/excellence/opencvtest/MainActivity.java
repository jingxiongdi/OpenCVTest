package com.excellence.opencvtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends Activity {
    private ImageView imageView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.img);

        /**
         * JAVA层处理灰度图
         */
        OpenCVLoader.initDebug();
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.c);
        Bitmap grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        imageView.setImageBitmap(grayBitmap);

        /**
         * jni 处理灰度图
         */
//        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
//                R.mipmap.c)).getBitmap();
//        int w = bitmap.getWidth(), h = bitmap.getHeight();
//        int[] pix = new int[w * h];
//        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
//        int [] resultPixes=OpenCVHelper.gray(pix,w,h);
//        Bitmap result = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);
//        result.setPixels(resultPixes, 0, w, 0, 0,w, h);
//        imageView.setImageBitmap(result);
    }
}
