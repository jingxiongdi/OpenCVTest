#include "com_excellence_opencvtest_OpenCVHelper.h"
#include <stdio.h>
#include <stdlib.h>
#include<android/log.h>
#include <opencv2/opencv.hpp>

     #define TAG "myDemo-jni" // 这个是自定义的LOG的标识
    #define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
    #define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
    #define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
    #define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
    #define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

using namespace cv;

extern "C" {

JNIEXPORT jintArray JNICALL Java_com_excellence_opencvtest_OpenCVHelper_gray(
        JNIEnv *env, jclass obj, jintArray buf, int w, int h);

JNIEXPORT void JNICALL Java_com_excellence_opencvtest_OpenCVHelper_saveMat(
        JNIEnv *env, jclass obj, jlong addrmat1, jlong addrmat2, jstring path);

JNIEXPORT jdoubleArray JNICALL Java_com_excellence_opencvtest_OpenCVHelper_readMat1(
        JNIEnv *env, jclass obj, jstring path);

JNIEXPORT jdoubleArray JNICALL Java_com_excellence_opencvtest_OpenCVHelper_readMat2(
        JNIEnv *env, jclass obj, jstring path);




    JNIEXPORT jintArray JNICALL Java_com_excellence_opencvtest_OpenCVHelper_gray(
            JNIEnv *env, jclass obj, jintArray buf, int w, int h) {

        jint *cbuf;
        cbuf = env->GetIntArrayElements(buf, JNI_FALSE );
        if (cbuf == NULL) {
            return 0;
        }

        Mat imgData(h, w, CV_8UC4, (unsigned char *) cbuf);

        uchar* ptr = imgData.ptr(0);
        for(int i = 0; i < w*h; i ++){
            //计算公式：Y(亮度) = 0.299*R + 0.587*G + 0.114*B
            //对于一个int四字节，其彩色值存储方式为：BGRA
            int grayScale = (int)(ptr[4*i+2]*0.299 + ptr[4*i+1]*0.587 + ptr[4*i+0]*0.114);
            ptr[4*i+1] = grayScale;
            ptr[4*i+2] = grayScale;
            ptr[4*i+0] = grayScale;
        }

        int size = w * h;
        jintArray result = env->NewIntArray(size);
        env->SetIntArrayRegion(result, 0, size, cbuf);
        env->ReleaseIntArrayElements(buf, cbuf, 0);
        return result;
    }


    JNIEXPORT void JNICALL Java_com_excellence_opencvtest_OpenCVHelper_saveMat(
        JNIEnv *env, jclass obj,jlong addrmat1,jlong addrmat2, jstring path){

        const char *nativepath = env->GetStringUTFChars(path, 0);
        Mat& matintrinsic_matrix = *(Mat*)addrmat1;
        Mat& mat_distortion_coeffs = *(Mat*)addrmat2;

        FileStorage storage(nativepath, FileStorage::WRITE);
        storage << "intrinsic_matrix" << matintrinsic_matrix;
        storage << "distortion_coeffs" << mat_distortion_coeffs;
        storage.release();

        env->ReleaseStringUTFChars(path, nativepath);
    }



    JNIEXPORT jdoubleArray JNICALL Java_com_excellence_opencvtest_OpenCVHelper_readMat1(
        JNIEnv *env, jclass obj, jstring path){
        jint i = 0;
        LOGD("########## i = %d", i++);
        const char *nativepath = env->GetStringUTFChars(path, 0);
        LOGD("########## i = %d", i++);
        FileStorage storage(nativepath, FileStorage::READ);
        LOGD("########## i = %d", i++);
        Mat mat_intrinsic_matrix;
        LOGD("########## i = %d", i++);
        storage["intrinsic_matrix"] >> mat_intrinsic_matrix;
        LOGD("########## i = %d", i++);
        LOGD("########## i = %d", mat_intrinsic_matrix.type());
        LOGD("########## i = %d", mat_intrinsic_matrix.rows);
        LOGD("########## i = %d", mat_intrinsic_matrix.cols);
        storage.release();
        int size = mat_intrinsic_matrix.total() * mat_intrinsic_matrix.elemSize();
        LOGD("########## i = %d", i++);
        jdouble * bytes = new jdouble[size]; // you will have to delete[] that later
        LOGD("########## i = %d", i++);
        std::memcpy(bytes,mat_intrinsic_matrix.data,size * sizeof(jbyte));
        LOGD("########## i = %d", i++);

        jdoubleArray jarray = env->NewDoubleArray(size);
        LOGD("########## i = %d", i++);

        env->SetDoubleArrayRegion(jarray, 0, size, bytes);
        LOGD("########## i = %d", i++);

        env->ReleaseStringUTFChars(path, nativepath);
        return jarray;
    }



    JNIEXPORT jdoubleArray JNICALL Java_com_excellence_opencvtest_OpenCVHelper_readMat2(
        JNIEnv *env, jclass obj, jstring path){
        jint i = 0;
        LOGD("########## i = %d", i++);
        const char *nativepath = env->GetStringUTFChars(path, 0);
        LOGD("########## i = %d", i++);
        FileStorage storage(nativepath, FileStorage::READ);
        LOGD("########## i = %d", i++);
        Mat distortion_coeffs;
        LOGD("########## i = %d", i++);
        storage["distortion_coeffs"] >> distortion_coeffs;
        LOGD("########## i = %d", i++);
        storage.release();
        LOGD("########## i = %d", distortion_coeffs.type());
        LOGD("########## i = %d", distortion_coeffs.rows);
        LOGD("########## i = %d", distortion_coeffs.cols);
        LOGD("########## i = %d", i++);
        int size = distortion_coeffs.total() * distortion_coeffs.elemSize();
        jdouble * bytes = new jdouble[size]; // you will have to delete[] that later
        LOGD("########## i = %d", i++);
        std::memcpy(bytes,distortion_coeffs.data,size * sizeof(jdouble));
        LOGD("########## i = %d", i++);
        jdoubleArray jarray = env->NewDoubleArray(size);
        LOGD("########## i = %d", i++);
        env->SetDoubleArrayRegion(jarray, 0, size, bytes);
        LOGD("########## i = %d", i++);
        env->ReleaseStringUTFChars(path, nativepath);
        LOGD("########## i = %d", i++);
        return jarray;
    }


}