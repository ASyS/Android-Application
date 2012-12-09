///**
// * @file SURF_FlannMatcher
// * @brief SURF detector + descriptor + FLANN Matcher
// * @author A. Huaman
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
//
///**
// * @function main
// * @brief Main function
// */
//int main()
//{
////  if( argc != 3 )
////  { readme(); return -1; }
//
//  Mat_<double>img_1 = imread( "A1.jpg");
////  Mat img_2 = imread( "X3.jpg");
//
////  if( !img_1.data || !img_2.data )
////  { std::cout<< " --(!) Error reading images " << std::endl; return -1; }
//
//  //  subtract background from objects
//  img_1 = subtractBG(img_1);
////  img_2 = subtractBG(img_2);
//
//  // traverse image from left to right, top to bottom
////  for(int i = 0; i < img_1.rows; i++){
////	  for(int j = 0; j < img_1.cols; j++){
////		  Vec3b bgrPixel = img_1.at<Vec3b>(i, j);
////		  cout<<(Scalar)bgrPixel.val[0]<<"\t";
////		  cout<<img_1.at<unsigned >(i, j)<<"\t";
////	  }
////	  cout<<endl;
////  }
////  double v;
////  for( int y = 0; y < img_1.rows; y++ ) {
////	  for( int x = 0; x < img_1.cols; x++ ) {
////		  v = img_1[y][x];
////		  cout<<v<<endl;
////	  }
////  }
//
//// resize images
////  img_1 = resizeSubImage(img_1);
////  img_2 = resizeSubImage(img_2);
//
//  // clean more
////  img_1 = furtherClean(img_1);
////  img_2 = furtherClean(img_2);
//
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
////	cvtColor(contourRegion, contourRegion, CV_RGB2YCrCb);
////	cout<<contourRegion<<endl;
//	imshow("t", contourRegion);
//	waitKey(0);
//	return contourRegion;
//}
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
//	resize(subImage, resizedImage, Size(subImage.cols/8,subImage.rows/8), 0, 0, INTER_NEAREST);
//	imshow("n", resizedImage);
//	cout<<format(resizedImage, "python")<<endl;
//	waitKey(0);
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
