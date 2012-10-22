#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

using namespace cv;
using namespace std;

Mat getYellowMat(Mat);
Mat getGreenMat(Mat);
Mat getBlueMat(Mat);

int main() {
	Mat src = imread("shapes3.png");		// image here
	if (src.empty())
		return -1;
	imshow("Source", src);

	/*
	 *  for yellow
	 */
	Mat yellow;
	yellow = getYellowMat(src);
	imshow("Yellow", yellow);

	/*
	 *  for green
	 */
	Mat green;
	green = getGreenMat(src);
	imshow("Green", green);

	/*
	 *  for blue
	 */
	Mat blue;
	blue = getBlueMat(src);
	imshow("Blue", blue);

	waitKey(0);
	return 0;
}

Mat getYellowMat(Mat m){ //yellow
	Mat hsv,colorFiltered;
	cvtColor(m, hsv, CV_BGR2HSV);
	inRange(hsv, Scalar(20, 100, 100), Scalar(30, 255, 255), colorFiltered);
	return colorFiltered;
}

Mat getGreenMat(Mat m){	//green
	Mat hsv,colorFiltered;
	cvtColor(m, hsv, CV_BGR2HSV);
	inRange(hsv, Scalar(49, 109, 61), Scalar(70, 255, 255), colorFiltered);
	return colorFiltered;
}

Mat getBlueMat(Mat m){ //red
	Mat hsv,colorFiltered;
	cvtColor(m, hsv, CV_BGR2HSV);
	inRange(hsv, Scalar(100, 100, 100), Scalar(120, 255, 255), colorFiltered);
	return colorFiltered;
}
