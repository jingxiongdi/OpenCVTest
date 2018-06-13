package com.excellence.opencvtest;

import org.opencv.core.Mat;

/**
 * Created by Administrator on 2018/6/4.
 */

public class OpenCVHelper {
    static {
        System.loadLibrary("OpenCV");
    }

    public static native int[] gray(int[] buf, int w, int h);

    public static native void saveMat(long addrmat1, long addrmat2, String path);

    public static native double[] readMat1(String path);

    public static native double[] readMat2(String path);
}
