//#include "opencv2/video/tracking.hpp"
//#include "opencv2/imgproc/imgproc.hpp"
//#include "opencv2/highgui/highgui.hpp"
//#include "DetectColors.h"
//#include <iostream>
//
//using namespace cv;
//using namespace std;
//
//void help()
//{
//  cout <<
//			"\nThis program demonstrates dense, Farnback, optical flow\n"
//			"Mainly the function: calcOpticalFlowFarneback()\n"
//			"Call:\n"
//			"./fback\n"
//			"This reads from video camera 0\n" << endl;
//}
//void drawOptFlowMap(const Mat& flow, Mat& cflowmap, int step,
//                    double, const Scalar& color){
//	int motionx, motiony;
//    for(int y = 0; y < cflowmap.rows; y += step)
//        for(int x = 0; x < cflowmap.cols; x += step)
//        {
//            const Point2f& fxy = flow.at<Point2f>(y, x);
////            line(cflowmap, Point(x,y), Point(cvRound(x+fxy.x), cvRound(y+fxy.y)),
////                 color);
//            motionx = cvRound(x+fxy.x);
//            motiony = cvRound(y+fxy.y);
//
//            circle(cflowmap, Point(cflowmap, 50), 5, cvScalar(255,255,255), -1);
//            if(motionx<(cflowmap.rows/2) && motionx!=x){
//                circle(cflowmap, Point(motionx, motiony), 2, cvScalar(0,0,255), -1);
//            	putText(cflowmap, "LEFT", Point(0,100),
//            	            							FONT_HERSHEY_SIMPLEX, 1,
//            	            							Scalar(255,255,255), 1, 8, false);
//            	cout<<"LEFT"<<endl;
//            }
//
//            else if(motionx>(cflowmap.rows/2) && motionx!=x){
//                circle(cflowmap, Point(motionx, motiony), 2, cvScalar(0,0,255), -1);
//            	putText(cflowmap, "RIGHT", Point(0,100),
//            							FONT_HERSHEY_SIMPLEX, 1,
//            							Scalar(255,255,255), 1, 8, false);
//            	cout<<"RIGHT"<<endl;
//            }
//
//            //circle(cflowmap, Point(x,y), 2, color, -1);
//        }
//}
//
//int main(int, char**)
//{
//    VideoCapture cap(0);
//    help();
//    if( !cap.isOpened() )
//        return -1;
//
//    Mat prevgray, gray, flow, cflow, frame, newframe, bgr, frameThresh, img;
//    namedWindow("flow", 1);
//
//    for(;;)
//    {
//        cap >> frame;
//        cv::flip(frame, frame ,1);
//    	//51 56 56
//    	cvtColor(frame,frameThresh,CV_BGR2HSV);
//
//    	inRange(frameThresh, Scalar(3, 138, 120), Scalar(255, 255, 255), img);
////   	img = getWhiteArea(frameThresh);
////    	gray = img.clone();
//    	cvtColor(img, bgr, CV_GRAY2BGR);
//    	cvtColor(frame, gray, CV_BGR2GRAY);
//        if( prevgray.data )
//        {
//            calcOpticalFlowFarneback(prevgray, gray, flow, 0.5, 3, 15, 3, 5, 1.2, 0);
//            cvtColor(prevgray, cflow, CV_GRAY2BGR);
//            drawOptFlowMap(flow, cflow, 36, 1.5, CV_RGB(0, 255, 0));
//            imshow("flow", cflow);
//        }
//        if(waitKey(30)>=0)
//            break;
//        std::swap(prevgray, gray);
//        //imshow("flow", bgr);
//        frame.release();
//
//    }
//    cvDestroyAllWindows() ;
//    cap.release();
//    return 0;
//}
