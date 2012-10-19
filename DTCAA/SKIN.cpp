/*
 * SKIN.cpp
 *
 *  Created on: Oct 19, 2012
 *      Author: Vaen
 */

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

using namespace cv;
using namespace std;

int findBiggestContour(vector<vector<Point> >);
Point2f findCentroidPoint(vector<Point>  contours);

int main(){
	Mat src = imread("qq.jpg");
	if (src.empty())
		return -1;
	blur( src, src, Size(3,3) );

	Mat hsv;
	cvtColor(src, hsv, CV_BGR2HSV);

	Mat bw;
	inRange(hsv, Scalar(0, 10, 60), Scalar(20, 150, 255), bw);	// SkinDetection
	imshow("src", src);
	imshow("dst", bw);

	cv::dilate(bw, bw, cv::Mat(), cv::Point(-1,-1));

	Mat canny_output;
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;

	findContours( bw, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );
	int s = findBiggestContour(contours);

	Mat drawing = Mat::zeros( src.size(), CV_8UC1 );

	Point2f mc = findCentroidPoint(contours[s]);
	cout << "x=" << mc.x << " y=" << mc.y << endl;	// Print x and y of centroid point

	drawContours( drawing, contours, s, Scalar(255), -1, 8, hierarchy, 0, Point() );
	circle( drawing, mc, 4, Scalar(80), -1, 8, 0 );

	imshow("Drawing", drawing);
	waitKey(0);
	return 0;
}

int findBiggestContour(vector<vector<Point> > contours){
	int indexOfBiggestContour = -1;
	int sizeOfBiggestContour = 0;
	for (int i = 0; i < contours.size(); i++){
		if(contours[i].size() > sizeOfBiggestContour){
			sizeOfBiggestContour = contours[i].size();
			indexOfBiggestContour = i;
		}
	}
	return indexOfBiggestContour;
}

Point2f findCentroidPoint(vector<Point>  contours){
	Moments mu;
	mu = moments( contours, false );
	Point2f mc;
	mc = Point2f( mu.m10/mu.m00 , mu.m01/mu.m00 );
	return mc;
}
