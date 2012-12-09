///*
// * convexHullPts.cpp
// *
// *  Created on: Dec 5, 2012
// *      Author: admin
// */
//
//#include <stdio.h>
//#include <iostream>
//#include "opencv2/core/core.hpp"
//#include "opencv2/features2d/features2d.hpp"
//#include "opencv2/highgui/highgui.hpp"
//#include "opencv2/imgproc/imgproc.hpp"
//#include "opencv2/nonfree/features2d.hpp"
//
//using namespace cv;
//using namespace std;
//
//Mat subtractBG(Mat image);
//Mat resizeSubImage(Mat subImage);
//Mat furtherClean(Mat image);
//void get_fingertips(Mat image, vector<Point> center);
//vector<Point> calculate_centroid(Mat image);
//Mat drawing1, drawing2;
///**
// * @function main
// * @brief Main function
// */
//int main()
//{
//  Mat img_1 = imread( "D3.jpg");
//  if(!img_1.data)
//  { std::cout<< " --(!) Error reading images " << std::endl; return -1; }
//
//  //  subtract background from objects
//  img_1 = subtractBG(img_1);
////  img_2 = subtractBG(img_2);
//
//  drawing1 = Mat::zeros( img_1.size(), CV_8UC3);
////  calculate_centroid(img_1);
//  get_fingertips(img_1, calculate_centroid(img_1));
//  imshow("result", drawing1);
//
//  // resize images
////  img_1 = resizeSubImage(img_1);
////  img_2 = resizeSubImage(img_2);
//
//  // clean more
////  img_1 = furtherClean(img_1);
////  img_2 = furtherClean(img_2);
//
//  waitKey(0);
//
//  return 0;
//}
//
//Mat subtractBG(Mat image){
//	Mat temp = image.clone();
//	cvtColor(temp, temp, CV_BGR2HSV);
////	imshow("HSV", temp);
//	inRange(temp, Scalar(100, 80, 60), Scalar(125, 255, 255), temp); //(90, 50, 50), Scalar(160, 255, 255)
//	threshold(temp, temp, 0, 255, THRESH_BINARY_INV);
//
////	imshow("Blue Filtered", image);
//
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
//	int indexOfBiggestContour = 0;
//	double sizeOfBiggestContour = 0;
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
//	// At this point, mask has value of 255 for pixels within the contour and value of 0 for those not in contour.
//
//	// Extract region using mask for region
//	Mat imageROI, contourRegion;
//	image.copyTo(imageROI, mask);
//
//	Mat contourRegion2 = mask(roi);
//	contourRegion = imageROI(roi);
//	imshow("t", contourRegion2);
//	waitKey(0);
//	return contourRegion2;
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
//	resize(subImage, resizedImage, Size(100,100), 0, 0, INTER_NEAREST);
////	imshow("t", resizedImage);
//	return resizedImage;
////	cout << "new cols: " << resizedImage.cols << endl;
////	cout << "new rows: " << resizedImage.rows << endl;
//}
//
//Mat furtherClean(Mat image){
//	Mat mask = image.clone();
//	Mat cleaned = Mat::zeros(image.size(), CV_8UC3);
//	cvtColor(mask, mask, CV_BGR2HSV);
//	inRange(mask, Scalar(90, 50, 50), Scalar(160, 255, 255), mask);
//	threshold(mask, mask, 0, 255, THRESH_BINARY_INV);
//	image.copyTo(cleaned, mask);
//	imshow("t", cleaned);
//	waitKey(0);
//	return cleaned;
//}
//
//void get_fingertips(Mat image, vector<Point> center){
//	//Mat src_copy = src.clone();
//	RNG rng(12345);
////	Mat src_gray;
////	Mat threshold_output;
//	//Mat canny_output;
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
//	Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255) );
//
//	/// Convert image to gray and blur it
////	cvtColor( image, src_gray, CV_BGR2GRAY );
////	blur( src_gray, src_gray, Size(3,3) );
//
//	/// Detect edges using Threshold
////	threshold( src_gray, threshold_output, thresh, 255, THRESH_BINARY );
//
//	/// Detect edges using canny
//	//Canny( src_gray, canny_output, thresh, thresh*2, 3 );
//
//	/// Find contours
//	//findContours( canny_output, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );
//
//	/// Find contours
//	findContours(image, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );
//
//	/// Find the convex hull object for each contour
//	vector<vector<Point> >hull1( contours.size() );
//
//	for( int i = 0; i < contours.size(); i++ ){
//		approxPolyDP(contours[i], contours[i], 1, true);
//		convexHull( Mat(contours[i]), hull1[i], false );
//	}
//
//	/// Draw contours + hull results
//	drawing1 = Mat::zeros(image.size(), CV_8UC3 );
//	//drawing = Mat::zeros( canny_output.size(), CV_8UC3 );
//	for( int i = 0; i< contours.size(); i++ ){
//		drawContours( drawing1, contours, i, color, 1, 8, vector<Vec4i>(), 0, Point() );
//        //drawContours( drawing, hull1, i, color, 1, 8, vector<Vec4i>(), 0, Point() );'
//        cout<<hull1[0].size()<<endl;
//	}
//
//	//plot centroid
//	cv::circle(drawing1, center[0], 5, color, -1, 8, 0);
//	//plot hull points
//	for (int i = 0; i<hull1[0].size(); i++){
//		if(hull1[0][i].y<center[0].y){
//			cv::circle(drawing1, hull1[0][i], 5, color, -1,8,0);
//		}
//	}
//
//	//draw lines from center to hull points
//	for (int i = 0; i<hull1[0].size(); i++){
//		if(hull1[0][i].y<center[0].y){
//			cv::line(drawing1, center[0], hull1[0][i], color, 1,8,0);
//			cout<<hull1[0][i]<<endl;
//		}
//	}
//}
//
//vector<Point> calculate_centroid(Mat image)
//{
//	RNG rng(12345);
////	Mat src_gray;
////	Mat canny_output;
//	vector<vector<Point> > contours;
//	vector<Vec4i> hierarchy;
//
//	/// Convert image to gray and blur it
////	cvtColor( image, src_gray, CV_BGR2GRAY );
////	blur( src_gray, src_gray, Size(3,3) );
//
//	/// Detect edges using canny
////	Canny( src_gray, canny_output, thresh, thresh*2, 3 );
//	/// Find contours
//	findContours(image, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );
//
//	/// Get the moments
//	vector<Moments> mu(contours.size() );
//	for( int i = 0; i < contours.size(); i++ )
//    { mu[i] = moments( contours[i], false ); }
//
//	///  Get the mass centers:
//	vector<Point> mc( contours.size() );
//
//   	for( int i = 0; i < contours.size(); i++ )
//    { mc[i] = Point( mu[i].m10/mu[i].m00 , mu[i].m01/mu[i].m00 ); }
//
//   	/// Calculate the area with the moments 00 and compare with the result of the OpenCV function
//   	printf("\t Info: Area and Contour Length \n");
//   	for( int i = 0; i< contours.size(); i++ ){
//   		printf(" * Contour[%d] - Area (M_00) = %.2f - Area OpenCV: %.2f - Length: %.2f \n", i, mu[i].m00, contourArea(contours[i]), arcLength( contours[i], true ) );
//        Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255) );
//        drawContours(drawing1, contours, i, color, 2, 8, hierarchy, 0, Point() );
//        circle(drawing1, mc[i], 4, color, -1, 8, 0 );
//      }
//   	 return mc;
//}
