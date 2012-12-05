/**
 * @file SURF_FlannMatcher
 * @brief SURF detector + descriptor + FLANN Matcher
 * @author A. Huaman
 */

#include <stdio.h>
#include <iostream>
#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/nonfree/features2d.hpp"

using namespace cv;
using namespace std;

void readme();
Mat subtractBG(Mat image);
Mat resizeSubImage(Mat subImage);
/**
 * @function main
 * @brief Main function
 */
int main()
{
//  if( argc != 3 )
//  { readme(); return -1; }

  Mat img_1 = imread( "A1.jpg");
  Mat img_2 = imread( "O3.jpg");

  if( !img_1.data || !img_2.data )
  { std::cout<< " --(!) Error reading images " << std::endl; return -1; }

  //  subtract background from objects
  img_1 = subtractBG(img_1);
  img_2 = subtractBG(img_2);

  // resize images
  img_1 = resizeSubImage(img_1);
  img_2 = resizeSubImage(img_2);
  //-- Step 1: Detect the keypoints using SURF Detector
  int minHessian = 400;

  SurfFeatureDetector detector( minHessian );

  vector<vector<KeyPoint> > keypoints(2);

  detector.detect( img_1, keypoints[0]);
  detector.detect( img_2, keypoints[1] );

  //-- Step 2: Calculate descriptors (feature vectors)
  SurfDescriptorExtractor extractor;

  Mat descriptors_1, descriptors_2;

  extractor.compute( img_1, keypoints[0], descriptors_1 );
  extractor.compute( img_2, keypoints[1], descriptors_2 );

  //-- Step 3: Matching descriptor vectors using FLANN matcher
  FlannBasedMatcher matcher;
  std::vector< DMatch > matches;
  matcher.match( descriptors_1, descriptors_2, matches );

  double max_dist = 0; double min_dist = 100;

  //-- Quick calculation of max and min distances between keypoints
  for( int i = 0; i < descriptors_1.rows; i++ )
  { double dist = matches[i].distance;
    if( dist < min_dist ) min_dist = dist;
    if( dist > max_dist ) max_dist = dist;
  }

  printf("-- Max dist : %f \n", max_dist );
  printf("-- Min dist : %f \n", min_dist );

  //-- Draw only "good" matches (i.e. whose distance is less than 2*min_dist )
  //-- PS.- radiusMatch can also be used here.
  std::vector< DMatch > good_matches;

  for( int i = 0; i < descriptors_1.rows; i++ )
  { if( matches[i].distance < 2*min_dist )
    { good_matches.push_back( matches[i]); }
  }

  //-- Draw only "good" matches
  Mat img_matches;
  drawMatches( img_1, keypoints[0], img_2, keypoints[1],
               good_matches, img_matches, Scalar::all(-1), Scalar::all(-1),
               vector<char>(), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS );

  //-- Show detected matches
  imshow( "Good Matches", img_matches );
  cout << "Match Count: " << good_matches.size() << endl;
  for( int i = 0; i < (int)good_matches.size(); i++ )
  { printf( "-- Good Match [%d] Keypoint 1: %d  -- Keypoint 2: %d  \n", i, good_matches[i].queryIdx, good_matches[i].trainIdx ); }

  waitKey(0);

  return 0;
}

/**
 * @function readme
 */
void readme()
{ std::cout << " Usage: ./SURF_FlannMatcher <img1> <img2>" << std::endl; }

Mat subtractBG(Mat image){
	Mat temp = image.clone();
	cvtColor(temp, temp, CV_BGR2HSV);
	imshow("HSV", temp);
	inRange(temp, Scalar(100, 80, 60), Scalar(125, 255, 255), temp);
	threshold(temp, temp, 0, 255, THRESH_BINARY_INV);
//	imshow("Blue Filtered", image);

	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	int indexOfBiggestContour = 0;
	double sizeOfBiggestContour = 0;

	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);

	for (int i = 0; i < contours.size(); i++){
		// Get index of contour that has biggest size
		if(contourArea(contours[i]) > sizeOfBiggestContour){
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

	// Extract region using mask for region
	Mat imageROI, contourRegion;
	image.copyTo(imageROI, mask);

	contourRegion = imageROI(roi);

	imshow("check", contourRegion);
	waitKey(0);
	return contourRegion;
}
Mat resizeSubImage(Mat subImage)
{
	Mat resizedImage;
	float fixedWidth = 200;
	float factor = fixedWidth/subImage.cols;
//	cout << "old cols: " << subImage.cols << endl;
//	cout << "old rows: " << subImage.rows << endl;
//	cout << factor << endl;
	float newWidth = subImage.cols * factor;
	float newHeight = subImage.rows * factor;
	resize(subImage, resizedImage, Size(newWidth,newHeight), 0, 0, INTER_NEAREST);
	return resizedImage;
//	cout << "new cols: " << resizedImage.cols << endl;
//	cout << "new rows: " << resizedImage.rows << endl;
}

