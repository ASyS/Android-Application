//#include <opencv2/imgproc/imgproc.hpp>
//#include <opencv2/highgui/highgui.hpp>
//#include <iostream>
//#include <stdio.h>
//#include <stdlib.h>
//
//using namespace cv;
//using namespace std;
//
//Mat getYellowMat(Mat);
//Mat getGreenMat(Mat);
//Mat getBlueMat(Mat);
//Mat getRedMat(Mat);
//Mat getCyanMat(Mat);
//Mat getVioletMat(Mat);
//Mat detectBlob(Mat, Mat);
//Mat detectSingleBlob(Mat, Mat);
//
//Point2f getCentroidPoint(vector<Point>);
//int getBiggestContour(vector<vector<Point> >);
//
//int main() {
//	Mat src = imread("shapes3.png");		// image here
////	Mat src = imread("s.jpg");
//	if (src.empty())
//		return -1;
//	imshow("Source", src);
//
//	Mat red;
//	red = getRedMat(src);
////	imshow("Red", red);
//
//	Mat yellow;
//	yellow = getYellowMat(src);
//	imshow("Yellow", yellow);
//
//	Mat green;
//	green = getGreenMat(src);
////	imshow("Green", green);
//
//	Mat blue;
//	blue = getBlueMat(src);
////	imshow("Blue", blue);
//
//	Mat cyan;
//	cyan = getCyanMat(src);
////	imshow("Cyan", cyan);
//
//	Mat violet;
//	violet = getVioletMat(src);
//	imshow("Violet", violet);
//
////	detectBlob(src, red);
//
//	detectSingleBlob(src, red);
//	waitKey(0);
//	return 0;
//}
//
//Mat getVioletMat(Mat m){
//	Mat hsv,colorFiltered;
//	cvtColor(m, hsv, CV_BGR2HSV);
//	inRange(hsv, Scalar(130, 25, 25), Scalar(150, 255, 255), colorFiltered);
//	return colorFiltered;
//}
//
//
//Mat getCyanMat(Mat m){ //cyan
//	Mat hsv,colorFiltered;
//	cvtColor(m, hsv, CV_BGR2HSV);
//	inRange(hsv, Scalar(85, 100, 150), Scalar(105, 255, 255), colorFiltered);
//	return colorFiltered;
//}
//
//Mat getRedMat(Mat m){ //red
//	Mat hsv,c1,c2,colorFiltered;
//	cvtColor(m, hsv, CV_BGR2HSV);
//	inRange(hsv, Scalar(0, 100, 100), Scalar(10, 255, 255), c1); //0-175-179---4-255-255
//	inRange(hsv, Scalar(170, 100, 100), Scalar(180, 255, 255), c2); //177-216-165---181-255-255
//	bitwise_or(c1,c2,colorFiltered);
//	return colorFiltered;
//}
//
//Mat getYellowMat(Mat m){ //yellow
//	Mat hsv,colorFiltered;
//	cvtColor(m, hsv, CV_BGR2HSV);
//	inRange(hsv, Scalar(20, 100, 100), Scalar(30, 255, 255), colorFiltered);
//	return colorFiltered;
//}
//
//Mat getGreenMat(Mat m){	//green
//	Mat hsv,colorFiltered;
//	cvtColor(m, hsv, CV_BGR2HSV);
//	inRange(hsv, Scalar(45, 80, 100), Scalar(70, 255, 255), colorFiltered);	//49, 109, 61   ----  70, 255, 255
//	return colorFiltered;
//}
//
//Mat getBlueMat(Mat m){ //red
//	Mat hsv,colorFiltered;
//	cvtColor(m, hsv, CV_BGR2HSV);
//	inRange(hsv, Scalar(110, 100, 100), Scalar(120, 255, 255), colorFiltered);	//100,100,100/120,255,255
//	return colorFiltered;
//}
//
//Mat detectBlob(Mat orig, Mat image)
//{
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
//	Mat temp;
//	image.copyTo(temp);
//	/// Find contours
//	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );
//
//	/// Approximate contours to polygons + get bounding rects and circles
//	//vector<vector<Point> > contours_poly( contours.size() );
//	vector<Rect> boundRect( contours.size() );
//	vector<Point2f>center( contours.size() );
//	vector<float>radius( contours.size() );
//	for( int i = 0; i < contours.size(); i++ )
//	{
//		boundRect[i] = boundingRect( Mat(contours[i]) );
//	    rectangle(orig, boundRect[i].tl(), boundRect[i].br(), Scalar(255, 255, 255), 2, 8, 0 );
//	    minEnclosingCircle( contours[i], center[i], radius[i] );
//	    circle(orig, center[i], 1, Scalar(255, 255, 255), -1, 8, 0 );
//	    cout << i+1 << ": (" << center[i].x << ", " << center[i].y << ")" << endl;
//	}
//	/// Show in a window
//	namedWindow( "Border", CV_WINDOW_AUTOSIZE );
//	imshow( "Border", orig);
//
//	return orig;
//}
//
//
//Mat detectSingleBlob(Mat orig, Mat image)
//{
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
//	Mat temp;
//	image.copyTo(temp);
//	/// Find contours
//	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );
//
//	/// Approximate contours to polygons + get bounding rects and circles
//	//vector<vector<Point> > contours_poly( contours.size() );
//	vector<Rect> boundRect( contours.size() );
//	vector<Point2f>center( contours.size() );
//	vector<float>radius( contours.size() );
////	for( int i = 0; i < contours.size(); i++ )
////	{
//
//	int i = getBiggestContour(contours);
//		boundRect[i] = boundingRect( Mat(contours[i]) );
//	    rectangle(orig, boundRect[i].tl(), boundRect[i].br(), Scalar(255, 255, 255), 2, 8, 0 );
//	    minEnclosingCircle( contours[i], center[i], radius[i] );
//	    circle(orig, center[i], 1, Scalar(255, 255, 255), -1, 8, 0 );
//	    cout << i+1 << ": (" << center[i].x << ", " << center[i].y << ")" << endl;
////	}
//	/// Show in a window
//	namedWindow( "Border", CV_WINDOW_AUTOSIZE );
//	imshow( "Border", orig);
//
//	return orig;
//}
//
//Point2f getCentroidPoint(vector<Point> contours) {
//	Moments mu;
//	mu = moments(contours, false);
//	Point2f mc;
//	mc = Point2f(mu.m10 / mu.m00, mu.m01 / mu.m00);
//	return mc;
//}
//
//int getBiggestContour(vector<vector<Point> > contours) {
//	int indexOfBiggestContour = -1;
//	int sizeOfBiggestContour = 0;
//	for (int i = 0; i < contours.size(); i++) {
//		if (contours[i].size() > sizeOfBiggestContour) {
//			sizeOfBiggestContour = contours[i].size();
//			indexOfBiggestContour = i;
//		}
//	}
//	return indexOfBiggestContour;
//}
//
//
