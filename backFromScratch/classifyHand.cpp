#include <stdio.h>
#include <iostream>
#include "opencv2/core/core.hpp"
//#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
//#include "opencv2/nonfree/features2d.hpp"

using namespace cv;
using namespace std;

Mat subtractBG(Mat image);
Mat resizeSubImage(Mat subImage);
Mat furtherClean(Mat image);
bool isHeightClass(int imgHeight, int handHeight);
bool isCentroidClass(Mat image);

/**
 * @function main
 * @brief Main function
 */
int main() {
//  if( argc != 3 )
//  { readme(); return -1; }

	String samples[] = { "A0_0.jpg", "A0_1.jpg", "A0_2.jpg", "A1_0.jpg",
			"A1_1.jpg", "B0.jpg", "B1.jpg", "C0.jpg", "C1.jpg", "D0.jpg",
			"D1_0.jpg", "D1_1.jpg", "E0_0.jpg", "E0_1.jpg", "E1.jpg", "F0.jpg",
			"F1.jpg", "G0.jpg", "G1_0.jpg", "G1_1.jpg", "H0_0.jpg", "H0_1.jpg",
			"H1_0.jpg", "H1_1.jpg", "H1_2.jpg", "I0.jpg", "I1.jpg", "K0.jpg",
			"K1.jpg", "L0.jpg", "L1.jpg", "M0.jpg", "M1_0.jpg", "M1_1.jpg",
			"N0.jpg", "N1.jpg", "O0.jpg", "O1_0.jpg", "O1_1.jpg", "P0.jpg",
			"P1_0.jpg", "P1_1.jpg", "P1_2.jpg", "Q0.jpg", "Q1_0.jpg",
			"Q1_1.jpg", "R0.jpg", "R1.jpg", "S0.jpg", "S1.jpg", "T0.jpg",
			"T1.jpg", "U0.jpg", "U1_0.jpg", "U1_1.jpg", "V0.jpg", "V1.jpg",
			"W0.jpg", "W1_0.jpg", "W1_1.jpg", "X0_0.jpg", "X0_1.jpg",
			"X1_0.jpg", "X1_1.jpg", "Y0.jpg", "Y1_0.jpg", "Y1_1.jpg" };
	for (int i = 0; i < 67; i++) {
		Mat src = imread(samples[i]);
		int imgHeight = src.rows;

		if (!src.data) {
			cout << " --(!) Error reading images " << endl;
			return -1;
		}

		// Subtract external background from hand
		src = subtractBG(src);
		int handHeight = src.rows;
//		cout<<imgHeight;
		if (isHeightClass(imgHeight, handHeight)) {
			cout << samples[i] << ": height classification" << endl;
//			cout << samples[i] << "\t" << src.cols << "\t" << handHeight << "\t"
//					<< centroid.x << "\t" << centroid.y << "\tvertical" << "\t"<<handHeight<<endl;
		} else {
//			cout << samples[i] << ": horizontal classification" << endl;
			if (isCentroidClass(src))
				cout << samples[i] << ": centroid classification" << endl;
			else
				cout << samples[i] << ": width classification" << endl;
//			cout << samples[i] << "\t" << src.cols << "\t" << handHeight << "\t"
//					<< centroid.x << "\t" << centroid.y << "\thorizontal" << "\t"<<handHeight<< endl;
//			cout<<"height: "<<handHeight<<endl;
//			cout<<"width: "<<src.cols<<endl;
//			cout<<mc<<endl;
		}
//		cout<<endl;
	}

//  img_2 = subtractBG(img_2);

// resize images
//  src = resizeSubImage(src);
//  img_2 = resizeSubImage(img_2);

// clean more
//  src = furtherClean(src);
//  img_2 = furtherClean(img_2);

	waitKey(0);

	return 0;
}

Mat subtractBG(Mat image) {
	Mat temp = image.clone();
	cvtColor(temp, temp, CV_BGR2HSV);
//	imshow("HSV", temp);
	inRange(temp, Scalar(100, 80, 60), Scalar(125, 255, 255), temp); //(90, 50, 50), Scalar(160, 255, 255)
	threshold(temp, temp, 0, 255, THRESH_BINARY_INV);
//	imshow("Blue Filtered", image);

	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	int indexOfBiggestContour = 0;
	double sizeOfBiggestContour = 0;

	findContours(temp, contours, hierarchy, CV_RETR_TREE,
			CV_CHAIN_APPROX_SIMPLE);

	for (int i = 0; i < contours.size(); i++) {
		// Get index of contour that has biggest size
		if (contourArea(contours[i]) > sizeOfBiggestContour) {
			sizeOfBiggestContour = contourArea(contours[i]);
			indexOfBiggestContour = i;
		}
	}

	// Get bounding box for contour
	Rect roi = boundingRect(contours[indexOfBiggestContour]); // This is a OpenCV function

	// Create a mask for each contour to mask out that region from image.
	Mat mask = Mat::zeros(image.size(), CV_8UC1);
	drawContours(mask, contours, indexOfBiggestContour, Scalar(255), CV_FILLED); // This is a OpenCV function
	// At this point, mask has value of 255 for pixels within the contour and value of 0 for those not in contour.

//	// Get moments of biggest contour
//	Moments mu;
//    mu = moments( contours[indexOfBiggestContour], false );
//
//	//  Get the mass center
//	mc = Point( mu.m10/mu.m00 , mu.m01/mu.m00 );
//
//    circle(image, mc, 4, Scalar(0, 255, 0), -1, 8, 0 );

	// Extract region using mask for region
	Mat imageROI, contourRegion;
	image.copyTo(imageROI, mask);

	Mat contourRegion2 = mask(roi);
	contourRegion = imageROI(roi);
//	cvtColor(contourRegion, contourRegion, CV_RGB2YCrCb);
//	cout<<contourRegion<<endl;
	imshow("t", contourRegion);
//	waitKey(0);
	return contourRegion;
}
Mat resizeSubImage(Mat subImage) {
	Mat resizedImage;
	float fixedWidth = 200;
	float factor = fixedWidth / subImage.cols;
//	cout << "old cols: " << subImage.cols << endl;
//	cout << "old rows: " << subImage.rows << endl;
//	cout << factor << endl;
	float newWidth = subImage.cols * factor;
	float newHeight = subImage.rows * factor;
	resize(subImage, resizedImage, Size(subImage.cols / 8, subImage.rows / 8),
			0, 0, INTER_NEAREST);
	imshow("n", resizedImage);
	cout << format(resizedImage, "python") << endl;
	waitKey(0);
	return resizedImage;
//	cout << "new cols: " << resizedImage.cols << endl;
//	cout << "new rows: " << resizedImage.rows << endl;
}

Mat furtherClean(Mat image) {
	Mat mask = image.clone();
	Mat cleaned = Mat::zeros(image.size(), CV_8UC3);
	cvtColor(mask, mask, CV_BGR2HSV);
	inRange(mask, Scalar(90, 50, 50), Scalar(160, 255, 255), mask);
	threshold(mask, mask, 0, 255, THRESH_BINARY_INV);
	image.copyTo(cleaned, mask);
	imshow("t", cleaned);
	waitKey(0);
	return cleaned;
}

bool isHeightClass(int imgHeight, int handHeight) {
	float thresh = 0.8;
	if ((float) handHeight / imgHeight > thresh)
		return true;
	else
		return false;
}

bool isCentroidClass(Mat image) {
	Mat binary;
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	int handWidth = image.cols;

	// Convert to B&W image
	threshold(image, binary, 0, 255, CV_THRESH_BINARY);

	// Convert to binary image, 1 channel
	cvtColor(binary, binary, CV_BGR2GRAY);

	// Find contour
	findContours(binary, contours, hierarchy, CV_RETR_TREE,
			CV_CHAIN_APPROX_SIMPLE);

	// Get the moment
	Moments mu;
	mu = moments(contours[0], false);

	//  Get the centroid or mass center
	Point mc;
	mc = Point(mu.m10 / mu.m00, mu.m01 / mu.m00);

	// Plot centroid
	circle(image, mc, 4, Scalar(0, 255, 0), -1, 8, 0);
	imshow("centroid", image);
	waitKey(0);

	if ((float) mc.x / handWidth < 0.55)
		return true;
	else
		return false;
}
