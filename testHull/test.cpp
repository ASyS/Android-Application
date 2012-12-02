/*
 * test.cpp
 *
 *  Created on: Dec 2, 2012
 *      Author: admin
 */
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>

using namespace cv;
using namespace std;

Mat subtractBG(Mat image);
int findObjectHull(Mat src, Mat image);

int main(){
	Mat binary;
	Mat src = imread("Box.png");
	imshow("Box", src);
	Mat processMe = src.clone();
	binary = subtractBG(processMe);
	findObjectHull(src, binary);
	waitKey(0);
	return 0;
}

Mat subtractBG(Mat image){
	cvtColor(image, image, CV_BGR2HSV);
	imshow("HSV", image);
	inRange(image, Scalar(100, 80, 60), Scalar(125, 255, 255), image);
	threshold(image, image, 0, 255, THRESH_BINARY_INV);
	imshow("Blue Filtered", image);
	return image;
}

int findObjectHull(Mat src, Mat image){
	vector<vector<Point> > hull(1);
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	int indexOfBiggestContour = 0;
	double sizeOfBiggestContour = 0;
	Mat temp = image.clone();

	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);

	cout << "in" << endl;
	for (int i = 0; i < contours.size(); i++){
		// Get index of contour that has biggest size
		if(contourArea(contours[i]) > sizeOfBiggestContour){
			sizeOfBiggestContour = contourArea(contours[i]);
			indexOfBiggestContour = i;
		}
	}

	convexHull(Mat(contours[indexOfBiggestContour]), hull[0], false);
	drawContours(src, hull, indexOfBiggestContour, Scalar(0,255,0), 1, 8, vector<Vec4i>(), 0, Point() );
	imshow("Hull", src);
	return indexOfBiggestContour;
}
