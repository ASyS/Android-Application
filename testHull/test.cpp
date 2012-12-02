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
vector<Point> findObjectHull(Mat src, Mat image);
void matchToTemplate(vector<vector<Point> > templateContours, vector<Point> objectContours);

int main(){
	Mat binary;
	vector<vector<Point> > templateContours(24);
	vector<Point> objectContours;

	Mat src = imread("Hand.png");
	imshow("Box", src);
	Mat processMe = src.clone();
	binary = subtractBG(processMe);
	templateContours[0] = findObjectHull(src, binary);

	Mat src2 = imread("XHand.png");
	processMe = src2.clone();
	binary = subtractBG(processMe);
	templateContours[1] = findObjectHull(src2, binary);

	Mat src3 = imread("test.png");
	processMe = src3.clone();
	binary = subtractBG(processMe);
	objectContours = findObjectHull(src3, binary);

	matchToTemplate(templateContours, objectContours);
	waitKey(0);
	return 0;
}

/**
 * subtracts blue background from image
 * and
 * returns binary image of object
 */
Mat subtractBG(Mat image){
	cvtColor(image, image, CV_BGR2HSV);
	imshow("HSV", image);
	inRange(image, Scalar(100, 80, 60), Scalar(125, 255, 255), image);
	threshold(image, image, 0, 255, THRESH_BINARY_INV);
	imshow("Blue Filtered", image);
	return image;
}

/**
 * finds all contours within the image,
 * selects contour with the biggest area to be the object,
 * and finds convexhull of object
 */
vector<Point> findObjectHull(Mat src, Mat image){
	vector<vector<Point> > hull(1);
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	int indexOfBiggestContour = 0;
	double sizeOfBiggestContour = 0;
	Mat temp = image.clone();

	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);

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
	waitKey(0);
	return hull[0];
}

/**
 * finds best match of test image from all the templates created beforehand
 * Note: still does not work. perhaps, resize first the sub-image containing the object
 * 		 before getting the convex hull? <for both template and test image>
 */
void matchToTemplate(vector<vector<Point> > templateContours, vector<Point> objectContours){
	double scores[2], bestScore = 10;
	int indxOfMatch = 2;
	for(int i = 0; i < 2; i++){
		scores[i] = matchShapes(templateContours[i], objectContours, CV_CONTOURS_MATCH_I3, 0);
		cout<<i<<": "<<scores[i]<<endl;
		if(scores[i] < bestScore){
			bestScore = scores[i];
			indxOfMatch = i;
		}
	}
	cout<<"best match: "<<indxOfMatch<<endl;
}
