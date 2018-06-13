package com.excellence.opencvtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!OpenCVLoader.initDebug()) {
              Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
              Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        imageView = findViewById(R.id.img);

        step1();
     //   getGray();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //openCV4Android 需要加载用到
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
//                    mOpenCvCameraView.enableView();
//                    mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public void getGray() {
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

    /**
     * 鱼眼矫正
     */
    public void step1() {

//        /****    图像数量     ****/int image_count = 15;
//        /****    定标板上每行、列的角点数       ****/
//        Size board_size = new Size(12, 9);
//        /****    缓存每幅图像上检测到的角点       ****/
//        MatOfPoint2f corners = new MatOfPoint2f();
//        /****  保存检测到的所有角点       ****/
//        List<MatOfPoint2f> corners_Seq = new ArrayList<>();
//        List<Mat> image_Seq = new ArrayList<>();
//        /****   成功提取角点的棋盘图数量    ****/
//        int successImageNum = 0;
//
//        int count = 0;
//        String path = "mnt/sdcard/pic/";
//        String imageFileName = null;
//        for (int i = 0; i != image_count; i++) {
//
//            imageFileName = path + i + ".jpg";
//            Log.e(TAG, "step1: " + imageFileName);
//            Mat image = Imgcodecs.imread(imageFileName);
//            /* 提取角点 */
//            Mat imageGray = new Mat();
//            Imgproc.cvtColor(image, imageGray, Imgproc.COLOR_RGB2GRAY);
//            boolean patternfound = Calib3d.findChessboardCorners(image, board_size, corners, CALIB_CB_ADAPTIVE_THRESH + CALIB_CB_NORMALIZE_IMAGE +
//                    CALIB_CB_FAST_CHECK);
//            if (!patternfound) {
//                Log.e(TAG, "onCreate: 找不到角点，需删除图片文件" + imageFileName + "重新排列文件名，再次标定");
//                continue;
//            } else {
//                /* 亚像素精确化 */
//                Imgproc.cornerSubPix(imageGray, corners, new Size(11, 11), new Size(-1, -1), new TermCriteria(3, 30, 0.1));
//                /* 绘制检测到的角点并保存 */
//                Mat imageTemp = image.clone();
//                Point[] points = corners.toArray();
//                for (int j = 0; j < points.length; j++) {
//                    Imgproc.circle(imageTemp, points[j], 10, new Scalar(0, 0, 255), 2, 8, 0);
//                }
//
////                imageFileName += "_corner.jpg";
//                Imgcodecs.imwrite(path + i + "_corner.jpg", imageTemp);
//                Log.e(TAG, "onCreate: Frame corner#" + i + 1 + "...end");
//                count = count + points.length;
//                successImageNum = successImageNum + 1;
//                corners_Seq.add(corners);
//            }
//            image_Seq.add(image);
//        }
//        Log.e(TAG, "onCreate: 角点提取完成！");
//
//
///************************************************************************
// 摄像机定标
// *************************************************************************/
//        Log.e(TAG, "step2: 开始定标………………");
//        Size square_size = new Size(20, 20);
//        Log.e(TAG, "step1: 1");
//        List<MatOfPoint3f> object_Points = new ArrayList<>();        /****  保存定标板上角点的三维坐标   ****/
//
//        Mat image_points = new Mat(1, count, CV_32FC2, Scalar.all(0));  /*****   保存提取的所有角点   *****/
//        MatOfInt point_counts = new MatOfInt();
//        /* 初始化定标板上角点的三维坐标 */
//        Log.e(TAG, "step1: 2");
//        for (int t = 0; t < successImageNum; t++) {
//            MatOfPoint3f tempPointSet = new MatOfPoint3f();
//            List<Point3> point3s = new ArrayList<>();
//            for (int i = 0; i < board_size.height; i++) {
//                for (int j = 0; j < board_size.width; j++) {
//                    /* 假设定标板放在世界坐标系中z=0的平面上 */
//                    Point3 tempPoint = new Point3();
//                    tempPoint.x = i * square_size.width;
//                    tempPoint.y = j * square_size.height;
//                    tempPoint.z = 0;
//                    point3s.add(tempPoint);
//                }
//            }
//            tempPointSet.fromList(point3s);
//            object_Points.add(tempPointSet);
//        }
//        Log.e(TAG, "step1: 3");
////        for (int i = 0; i < successImageNum; i++) {
////            point_counts.push_back((board_size.width * board_size.height));
////        }
//        Log.e(TAG, "step1: 4");
//        /* 开始定标 */
//        Size image_size = image_Seq.get(0).size();
//        Log.e(TAG, "step1: 5");
//        /*****    摄像机内参数矩阵    ****/
//        Mat intrinsic_matrix = new Mat(3, 3, CvType.CV_64F);
//        MatOfDouble distortion_coeffs = new MatOfDouble();     /* 摄像机的4个畸变系数：k1,k2,k3,k4*/
//        List<Mat> rotation_vectors = new ArrayList<>();                           /* 每幅图像的旋转向量 */
//        List<Mat> translation_vectors = new ArrayList<>();                        /* 每幅图像的平移向量 */
//        int flags = 0;
//        Log.e(TAG, "step1: 6");
//        flags |= CALIB_RECOMPUTE_EXTRINSIC;
//        flags |= CALIB_CHECK_COND;
//        flags |= CALIB_FIX_SKEW;
//        Calib3d.calibrate(object_Points, corners_Seq, image_size, intrinsic_matrix, distortion_coeffs, rotation_vectors, translation_vectors, flags, new TermCriteria(3, 20, 1e-6));
//        Log.e(TAG, "step2: 定标完成！");
//
//
//        /************************************************************************
//         对定标结果进行评价
//         *************************************************************************/
//        Log.e(TAG, "step3: 开始评价定标结果………………");
//        double total_err = 0.0;                   /* 所有图像的平均误差的总和 */
//        double err = 0.0;                        /* 每幅图像的平均误差 */
//        MatOfPoint2f image_points2 = new MatOfPoint2f();             /****   保存重新计算得到的投影点    ****/
//
//        Log.e(TAG, "step3: 每幅图像的定标误差：");
//        for (int i = 0; i < image_count; i++) {
//            MatOfPoint3f tempPointSet = object_Points.get(i);
//            /****    通过得到的摄像机内外参数，对空间的三维点进行重新投影计算，得到新的投影点     ****/
//            Calib3d.projectPoints(tempPointSet, image_points2, rotation_vectors.get(i), translation_vectors.get(i), intrinsic_matrix, distortion_coeffs)
//            ;
//            /* 计算新的投影点和旧的投影点之间的误差*/
//            MatOfPoint2f tempImagePoint = corners_Seq.get(i);
//
//            Mat tempImagePointMat = new Mat(tempImagePoint.size(), CV_32FC2);
//            Mat image_points2Mat = new Mat(image_points2.size(), CV_32FC2);
//            Point[] tempImagepoints = tempImagePoint.toArray();
//            Point[] imagepoints2 = image_points2.toArray();
//
//
//            float[] buff1 = new float[(int) (tempImagePointMat.total() * tempImagePointMat.channels())];
//            image_points2Mat.get(0, 0, buff1);
//
//            float[] buff2 = new float[(int) (image_points2Mat.total() * image_points2Mat.channels())];
//            tempImagePointMat.get(0, 0, buff2);
//
//
//            for (int j = 0; j != tempImagepoints.length; j++) {
//                buff1[0] = (float) imagepoints2[j].x;
//                buff1[j] = (float) imagepoints2[j].y;
//
//                buff2[0] = (float) tempImagepoints[j].x;
//                buff2[j] = (float) tempImagepoints[j].y;
//            }
//            image_points2Mat.put(0, 0, buff1);
//            tempImagePointMat.put(0, 0, buff2);
//
//            err = Core.norm(image_points2Mat, tempImagePointMat, NORM_L2);
////            total_err += err /= point_counts.toArray()[i];
////            Log.e(TAG, "step3: " + "第" + i + 1 + "幅图像的平均误差：" + err + "像素");
//        }
////        Log.e(TAG, "step3: " + "总体平均误差：" + total_err / image_count + "像素");
//        Log.e(TAG, "step3: " + "评价完成！");
//
//
//        /************************************************************************
//         保存定标结果
//         *************************************************************************/
//        Log.e(TAG, "step4: 开始保存定标结果………………");
//        Mat rotation_matrix = new Mat(3, 3, CV_32FC1, Scalar.all(0)); /* 保存每幅图像的旋转矩阵 */
//
////        fout << "相机内参数矩阵：" << endl;
////        fout << intrinsic_matrix << endl;
////        fout << "畸变系数：\n";
////        fout << distortion_coeffs << endl;
//        for (int i = 0; i < image_count; i++) {
////            fout << "第" << i + 1 << "幅图像的旋转向量：" << endl;
////            fout << rotation_vectors[i] << endl;
//
//            /* 将旋转向量转换为相对应的旋转矩阵 */
//            Calib3d.Rodrigues(rotation_vectors.get(i), rotation_matrix);
////            fout << "第" << i + 1 << "幅图像的旋转矩阵：" << endl;
////            fout << rotation_matrix << endl;
////            fout << "第" << i + 1 << "幅图像的平移向量：" << endl;
////            fout << translation_vectors[i] << endl;
//        }
////        cout << "完成保存" << endl;
////        fout << endl;
//
//
///************************************************************************
// 显示定标结果
// *************************************************************************/
//        Mat mapx = new Mat(image_size, CV_32FC1);
//        Mat mapy = new Mat(image_size, CV_32FC1);
//        Mat R = Mat.eye(3, 3, CV_32F);
//
//        Log.e(TAG, "step5: 保存矫正图像");
//        for (int i = 0; i != image_count; i++) {
//            Log.e(TAG, "step5: " + "Frame #" + i + 1 + "...");
//            //fisheye::initUndistortRectifyMap(intrinsic_matrix,distortion_coeffs,R,intrinsic_matrix,image_size,CV_32FC1,mapx,mapy);
//            Calib3d.initUndistortRectifyMap(intrinsic_matrix, distortion_coeffs, R,
//                    Calib3d.getOptimalNewCameraMatrix(intrinsic_matrix, distortion_coeffs, image_size, 1), image_size, CV_32FC1, mapx, mapy)
//            ;
//            Mat t = image_Seq.get(i).clone();
//            Imgproc.remap(image_Seq.get(i), t, mapx, mapy, INTER_LINEAR);
//
//            Imgcodecs.imwrite(path + i + "_d.jpg", t);
//        }
//        Log.e(TAG, "step5: 保存结束");

        /************************************************************************
         测试一张图片
         *************************************************************************/
        //cout<<"TestImage ..."<<endl;
        //Mat testImage = imread("a.jpg",1);
        //fisheye::initUndistortRectifyMap(intrinsic_matrix,distortion_coeffs,R,
        //    getOptimalNewCameraMatrix(intrinsic_matrix, distortion_coeffs, image_size, 1, image_size, 0),image_size,CV_32FC1,mapx,mapy);
        //Mat t = testImage.clone();
        //cv::remap(testImage,t,mapx, mapy, INTER_LINEAR);

        //imwrite("TestOutput.jpg",t);
        //cout<<"保存结束"<<endl;
//        OpenCVHelper.saveMat(intrinsic_matrix.getNativeObjAddr(), distortion_coeffs.getNativeObjAddr(), path + "data.xml");
        String path = "mnt/sdcard/pic/";
        Mat intrinsic_matrix = new Mat(3,3,6);
        Mat distortion_coeffs = new Mat(4,1,6);     /* 摄像机的4个畸变系数：k1,k2,k3,k4*/

        double[] buff1 = OpenCVHelper.readMat1(path + "data.xml");
        Log.e(TAG, "step1: " + buff1.length);
        double[] buff2 = OpenCVHelper.readMat2(path + "data.xml");
        Log.e(TAG, "step1: " + buff2.length);
        intrinsic_matrix.put(0, 0, buff1);
        distortion_coeffs.put(0, 0, buff2);
        Log.e(TAG, "step1: "+intrinsic_matrix.toString() );
        Log.e(TAG, "step1: "+distortion_coeffs.toString() );

        Log.e(TAG, "step6: TestImage ...");
        Mat distort_img = Imgcodecs.imread(path + "c.jpg", 1);
        Mat undistort_img = new Mat();
//        Mat intrinsic_mat = new Mat();
        Mat new_intrinsic_mat = new Mat();

        intrinsic_matrix.copyTo(new_intrinsic_mat);
        //调节视场大小,乘的系数越小视场越大
        new_intrinsic_mat.get(0, 0)[0] *= 0.5;
        new_intrinsic_mat.get(1, 1)[0] *= 0.5;
        //调节校正图中心，建议置于校正图中心
        new_intrinsic_mat.get(0, 2)[0] = 0.5 * distort_img.cols();
        new_intrinsic_mat.get(1, 2)[0] = 0.5 * distort_img.rows();

        Calib3d.undistortImage(distort_img, undistort_img, intrinsic_matrix, distortion_coeffs, new_intrinsic_mat, distort_img.size());
        Imgcodecs.imwrite(path + "output.jpg", undistort_img);
        Log.e(TAG, "step6: 保存结束");

    }
}
