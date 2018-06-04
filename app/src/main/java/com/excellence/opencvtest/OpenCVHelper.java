package com.excellence.opencvtest;

/**
 * Created by Administrator on 2018/6/4.
 */

public class OpenCVHelper {
    static {
        System.loadLibrary("OpenCV");
    }
    public static native int[] gray(int[] buf, int w, int h);
}
