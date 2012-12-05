///*
// * test.cpp
// *
// *  Created on: Dec 2, 2012
// *      Author: admin
// */
//#include <opencv2/imgproc/imgproc.hpp>
//#include <opencv2/highgui/highgui.hpp>
//#include <opencv2/core/core.hpp>
//#include <opencv2/ml/ml.hpp>
//#include <iostream>
//
//using namespace cv;
//using namespace std;
//
//Mat processImage(Mat image);
//bool HW_Classify(Mat image);
//Mat subtractBG(Mat image);
//Mat findObjectContours(Mat image);
//Mat resizeSubImage(Mat subImage);
//vector<Point> findObjectHull(Mat src, Mat image);
//vector<Point> findObjectHull2(Mat src, Mat image);
//void matchToTemplate(vector<vector<Point> > templateContours, vector<Point> objectContours);
//
//int main(){
//	Mat src, processed;
//	vector<vector<Point> > templateContours(24);
//	vector<Point> objectContours;
//	String trainImages[24] = {"A5.jpg", "B3.jpg", "C4.jpg", "E4.jpg", "F4.jpg", "G3.jpg", "H1.jpg"
//			, "I4.jpg", "K1.jpg", "L5.jpg", "M1.jpg", "N4.jpg", "O5.jpg", "P1.jpg", "Q1.jpg"
//			, "R4.jpg", "S6.jpg", "T6.jpg", "U5.jpg", "V3.jpg", "W4.jpg", "Y3.jpg"};
//	for(int i = 0; i < 22; i++){
//		src = imread(trainImages[i]);
//		processed = processImage(src);
//		templateContours[i] = findObjectHull(src, processed);
//	}
//
//	src = imread("Y1.jpg");
//	processed = processImage(src);
//	objectContours = findObjectHull2(src, processed);
//
//	matchToTemplate(templateContours, objectContours);
//
//	src = imread("Y4.jpg");
//	processed = processImage(src);
//	objectContours = findObjectHull2(src, processed);
//
//	matchToTemplate(templateContours, objectContours);
//
//	src = imread("Y5.jpg");
//	processed = processImage(src);
//	objectContours = findObjectHull2(src, processed);
//
//	matchToTemplate(templateContours, objectContours);
//
//	src = imread("Y6.jpg");
//	processed = processImage(src);
//	objectContours = findObjectHull2(src, processed);
//
//	matchToTemplate(templateContours, objectContours);
//
//	waitKey(0);
//	return 0;
//}
//
//Mat processImage(Mat image){
//	Mat processMe, binary, sub, resized;
//	imshow("Source", image);
//	processMe = image.clone();
//	binary = subtractBG(processMe);
////	sub = findObjectContours(binary);
////	HW_Classify(sub);
//	imshow("Sub-image", sub);
//	//	resized = resizeSubImage(sub);
//	return binary;
//}
//
//bool HW_Classify(Mat image)
//{
//	/**
//	 * returns true if height > width
//	 */
//	int height = image.rows, width = image.cols;
//	if(height > width){
////		cout << "height-width classification" << endl;
//		return true;
//	}
//	else{
////		cout << "not height-width classification" << endl;
//		return false;
//	}
//}
//
//
///**
// * subtracts blue background from image
// * and
// * returns binary image of object
// */
//Mat subtractBG(Mat image){
//	cvtColor(image, image, CV_BGR2HSV);
////	imshow("HSV", image);
//	inRange(image, Scalar(100, 80, 60), Scalar(125, 255, 255), image);
//	threshold(image, image, 0, 255, THRESH_BINARY_INV);
////	imshow("Blue Filtered", image);
//	return image;
//}
//
//Mat findObjectContours(Mat image){
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
//	int indexOfBiggestContour = 0;
//	double sizeOfBiggestContour = 0;
//	Mat temp = image.clone();
//
//	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);
//
//	for (int i = 0; i < contours.size(); i++){
//		// Get index of contour that has biggest size
//		if(contourArea(contours[i]) > sizeOfBiggestContour){
//			sizeOfBiggestContour = contourArea(contours[i]);
//			indexOfBiggestContour = i;
//		}
//	}
//
//	// Get bounding box for contour
//	Rect roi = boundingRect(contours[indexOfBiggestContour]); // This is a OpenCV function
//
//	// Create a mask for each contour to mask out that region from image.
//	Mat mask = Mat::zeros(image.size(), CV_8UC1);
//	drawContours(mask, contours, indexOfBiggestContour, Scalar(255), CV_FILLED); // This is a OpenCV function
//
//	// At this point, mask has value of 255 for pixels within the contour and value of 0 for those not in contour.
//
//	// Extract region using mask for region
//
//	Mat contourRegion;
//	contourRegion = mask(roi);
//
//	return contourRegion;
//}
//
//Mat resizeSubImage(Mat subImage)
//{
//	Mat resizedImage;
//	float fixedWidth = 200;
//	float factor = fixedWidth/subImage.cols;
////	cout << "old cols: " << subImage.cols << endl;
////	cout << "old rows: " << subImage.rows << endl;
////	cout << factor << endl;
//	float newWidth = subImage.cols * factor;
//	float newHeight = subImage.rows * factor;
//	resize(subImage, resizedImage, Size(newWidth,newHeight), 0, 0, INTER_NEAREST);
//	return resizedImage;
////	cout << "new cols: " << resizedImage.cols << endl;
////	cout << "new rows: " << resizedImage.rows << endl;
//}
//
//
///** This is used when the sub-image is resized.
// * finds contour of the object,
// * and finds convexhull of the contour
// */
//vector<Point> findObjectHull(Mat src, Mat image){
//	vector<vector<Point> > hull(1);
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
////	int indexOfBiggestContour = 0;
////	double sizeOfBiggestContour = 0;
//	Mat temp = image.clone();
//
//	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);
//
////	for (int i = 0; i < contours.size(); i++){
////		// Get index of contour that has biggest size
////		if(contourArea(contours[i]) > sizeOfBiggestContour){
////			sizeOfBiggestContour = contourArea(contours[i]);
////			indexOfBiggestContour = i;
////		}
////	}
//
//	convexHull(Mat(contours[0]), hull[0], false);
//	drawContours(src, hull, 0, Scalar(0,255,0), 1, 8, vector<Vec4i>(), 0, Point() );
//	imshow("Hull", src);
//	waitKey(0);
//	return hull[0];
//}
//
///** This is used when the sub-image is not resized.
// * finds all contours within the image,
// * selects contour with the biggest area to be the object,
// * and finds convexhull of object
// */
//vector<Point> findObjectHull2(Mat src, Mat image){
//	vector<vector<Point> > hull(1);
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
//	int indexOfBiggestContour = 0;
//	double sizeOfBiggestContour = 0;
//	Mat temp = image.clone();
//
//	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);
//
//	for (int i = 0; i < contours.size(); i++){
//		// Get index of contour that has biggest size
//		if(contourArea(contours[i]) > sizeOfBiggestContour){
//			sizeOfBiggestContour = contourArea(contours[i]);
//			indexOfBiggestContour = i;
//		}
//	}
////	cout << indexOfBiggestContour << endl;
//	convexHull(Mat(contours[indexOfBiggestContour]), hull[0], false);
////	approxPolyDP(contours[indexOfBiggestContour], hull[0], 1, true);
//	drawContours(src, hull, 0, Scalar(0,255,0), 1, 8, vector<Vec4i>(), 0, Point() );
//	imshow("Hull", src);
//	waitKey(0);
//	return hull[0];
//}
//
///**
// * finds best match of test image from all the templates created beforehand
// * Note: still does not work. perhaps, resize first the sub-image containing the object
// * 		 before getting the convex hull? <for both template and test image>
// */
//void matchToTemplate(vector<vector<Point> > templateContours, vector<Point> objectContours){
//	double scores[22], bestScore = 10;
//	char letters[22] = {'A', 'B', 'C', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Y'};
//	int indxOfMatch = 2;
//	for(int i = 0; i < 22; i++){
//		scores[i] = matchShapes(templateContours[i], objectContours, CV_CONTOURS_MATCH_I3, 0);
////		cout<<i<<": "<<scores[i]<<endl;
//		if(scores[i] < bestScore){
//			bestScore = scores[i];
//			indxOfMatch = i;
//		}
//	}
//	cout<<"best match: "<<letters[indxOfMatch]<<endl;
//}
