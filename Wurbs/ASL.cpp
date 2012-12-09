/*
 * ASL.c
 *
 *  Created on: Dec 1, 2012
 *      Author: admin
 */

// Gesture Recognition.cpp
//
// Jeremy Wurbs
// September 2012

// includes
//#include "stdafx.h"

#include "opencv/cv.h"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/video/tracking.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/video/background_segm.hpp"
#include "opencv2/features2d/features2d.hpp"
#include <stdio.h>
#include <stdlib.h>
#include <iostream>

// namespaces
using namespace cv;
using namespace std;

// global variables
int morph_elem = 2;
int morph_size = 4;
int Hmin = 65, Hmax = 160, //  {38 187} {88, 136} {0, 30}    {55 144}
	Smin = 0,  Smax = 255, //  {0 126} {0, 166}  {45, 159}  {0 93}
	Vmin = 112,  Vmax = 255; //  {29 134} {86, 255} {102, 255} {111 255}
bool useBGS = true; //

// extra functions
void Dilation(Mat frameBinary, Mat &frameFilled);
void Erosion(Mat frameBinary, Mat &frameFilled);
void FindDifferenceColor(Mat src1, Mat src2, Mat &dst);
void FindDifferenceGray (Mat src1, Mat src2, Mat &dst);
void trainBackgroundModel(VideoCapture cap, Mat &background);
void createWindows(void);
void applyMask(Mat &src, Mat mask);
void cleanMask(Mat &src, int iter);
void displayImages(Mat image, Mat foreground, Mat frameFilled);
void findHull(vector< vector< Point > > contours, vector<vector<Point> > &hull);
void computeMoments(vector<vector<Point> > hull, vector<Moments> &mu);
void generateFLTemplates(VideoCapture cam, vector<Moments> &templateMoments, vector<vector<Point> > &templateContours, Mat background);
void printMoments(vector<Moments> templateMoments);
int  findMatch(vector<Moments> templateMoments, vector<Moments> objectMoments, vector<vector<Point> > templateContours, vector<vector<Point> > objectContours, double scores[24]);
void displayPrediction(Mat &image, int indexSign);
void plotScores(Mat &image, double scores[24]);
void addLetters(Mat &src);

int main(int argc, char* argv[])
{
	// Mats (images)
	Mat image, frame, frameHSV, frameBinary, frameEdges, frameFilled, mask, scoreImage;

	// Create our image and recording structures
	vector< vector< Point > > contours;
	vector< Vec4i > hierarchy;
	RNG rng(12345);
	VideoWriter record;
	Mat foreground, background, difference, tmpFrame;
	BackgroundSubtractorMOG2 mog;
	int bgsThresh = 30; // Background subtraction threshold
	string imageToSave = "Image.jpg";

	// Set up our video capture object
	VideoCapture cam(0);
    if(!cam.isOpened())
        return -1;

	// Train background model
	trainBackgroundModel(cam, background);

	// Generate moment templates for each letter
	vector<Moments> templateMoments(24);
	vector<vector<Point> > templateContours(24);
	generateFLTemplates(cam, templateMoments, templateContours, background);

	// Print moments
	printMoments(templateMoments);

	// Open up our windows
	createWindows();

	// Our infinite loop; hit 'escape' to get out
	while(true)
	{
		// get new frame from the camera
		cam >> image; // we keep 'image' so we have a copy of the original
		frame = image.clone(); // we will do our processing on 'frame' from here on out

		// Subtract out the background
		//	Get the difference between each pixel in the current frame and the background model
		//FindDifferenceColor(frame, background, difference);
		FindDifferenceGray(frame, background, difference);
		//	Create a 'foreground' mask based on this difference
		if (useBGS)
	        threshold(difference, foreground, bgsThresh, 255, THRESH_BINARY);
		else
		{
			foreground = image.clone();
			foreground = Scalar::all(255);
		}

		// Often individual pixels inside foreground objects will match the background;
		//	We dilate to lessen this effect.
		cleanMask(foreground, 5);
		applyMask(frame, foreground);

		// convert the frame from RGB to HSV
		cvtColor(frame, frameHSV, CV_RGB2HSV);

		// threshold the image in the range (Hmin,Smin,Vmin) to (Hmax,Smax,Vmax);
		//	anything inside the range is set to 255, anything outside the range
		//	is set to 0.
		inRange(frameHSV, Scalar(Hmin,Smin,Vmin), Scalar(Hmax,Smax,Vmax), frameBinary);

		// So what we have is ok, but we can do a little better by finding all
		//	the contours and filling them in.

		// First, we dilate to give a better connected region
		//	Note: One of the next three lines needs to be run
		//Erosion();
		//Dilation();
		frameFilled = frameBinary.clone();
		//erode(frameBinary, frameFilled, Mat(), Point(-1,-1), 8);
		//dilate(frameBinary, frameFilled, Mat(), Point(-1,-1), 8);

		// Find the contours in the image
		findContours(frameFilled, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS);
		if (contours.size() == 0)
			continue;

		// Find the hull of the contours
		//vector<vector<Point> > hull(contours.size() )
		vector<vector<Point> > hull(1);
		findHull(contours, hull);

		// Compute image moments on the hull
		vector<Moments> mu(1);
		computeMoments(hull, mu);

		// Draw contours and hull results
		/*
		for (int i = 0; i < contours.size(); i++)
		{
			Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255) );
			drawContours(frameFilled, contours, i, color, CV_FILLED, 8, hierarchy, 0, Point() );
			//drawContours(frameFilled, hull, i, color, 1, 8, vector<Vec4i>(), 0, Point() );
		}
		*/
		Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255) );
		drawContours(frameFilled, hull, 0, color, 1, 8, vector<Vec4i>(), 0, Point() );
		drawContours(image, hull, 0, Scalar(255,0,0), 3, 8, vector<Vec4i>(), 0, Point() );

		// find the best match
		vector<Moments> object;
		vector<vector<Point> > objectContours;
		double savedScores[24];
		int indexSign = findMatch(templateMoments,mu,templateContours,hull,savedScores);

		// Display scores
		scoreImage = image.clone();
 		plotScores(scoreImage, savedScores);
		imshow("Fingerletter Scores", scoreImage);

		// add the prediction to the display
		displayPrediction(image, indexSign);

		// display the images
		displayImages(image, foreground, frameFilled);

		// Use the escape key to exit the loop
		int c = cvWaitKey(5);
		if((char)c==27) // 27 is the escape key
			return 0;
		if((char)c==32) // 32 is spacebar
		{
			imwrite(imageToSave,image);
		}
	}

	return 0;
}

void Dilation(Mat frameBinary, Mat &frameFilled)
{
	int morph_type;

	if (morph_elem == 0)      {morph_type = MORPH_RECT;}
	else if (morph_elem == 1) {morph_type = MORPH_CROSS; }
	else if (morph_elem == 2) {morph_type = MORPH_ELLIPSE; }

	Mat element = getStructuringElement(morph_type,
										Size(2*morph_size + 1, 2*morph_size+1),
										Point(morph_size, morph_size) );
	dilate(frameBinary, frameFilled, element);
}

void Erosion(Mat frameBinary, Mat &frameFilled)
{
	int morph_type;

	if (morph_elem == 0)      {morph_type = MORPH_RECT;}
	else if (morph_elem == 1) {morph_type = MORPH_CROSS; }
	else if (morph_elem == 2) {morph_type = MORPH_ELLIPSE; }

	Mat element = getStructuringElement(morph_type,
										Size(2*morph_size + 1, 2*morph_size+1),
										Point(morph_size, morph_size) );
	erode(frameBinary, frameFilled, element);
}

void FindDifferenceColor(Mat src1, Mat src2, Mat &dst)
{
	// This function thresholds the difference between the background model image
	//	and the current frame by thresholding each color channel independently.

	vector<cv::Mat> difference(3);
	vector<cv::Mat> rgbChannels1(3);
	vector<cv::Mat> rgbChannels2(3);
	vector<cv::Mat> differences(3);
	cv::split(src1, rgbChannels1);
	cv::split(src2, rgbChannels2);

	for (int i = 0; i < 3; i++)
	{
		difference[i] = cv::abs(rgbChannels2[i] - rgbChannels1[i]);
	}

	merge(difference, dst);
}

void FindDifferenceGray(Mat src1, Mat src2, Mat &dst)
{
	// This function thresholds the difference between the background model image
	//	and the current frame by first converting both to grayscale.

	Mat src1_gray, src2_gray, difference;
	cvtColor(src1, src1_gray, CV_RGB2GRAY);
	cvtColor(src2, src2_gray, CV_RGB2GRAY);

	//difference = cv::abs(src2_gray - src1_gray);
	//cv::threshold(difference, dst, threshold, 255, THRESH_BINARY);

	dst = cv::abs(src2_gray - src1_gray);
}

void trainBackgroundModel(VideoCapture cap, Mat &background)
{
	Mat frame, foreground, difference,image;
	BackgroundSubtractorMOG2 mog;

	// Set up a background model (image)
	namedWindow( "Training", CV_WINDOW_AUTOSIZE);
	printf("\nHit any key to begin...");
	cvWaitKey(0);
	int numTrainingFrames = 100;
	for (int i = 0; i < numTrainingFrames; i++)
	{
		// Get a new frame
        cap >> frame;
        if( frame.empty() )
                break;

		imshow("Training", frame);
		printf("\nTraining: %d / %d", i, numTrainingFrames);

		// Update the model
		mog.operator()(frame,foreground,-1);

		waitKey(5);
	}
	destroyWindow("Training");

	// Set the background image
	mog.getBackgroundImage(background);
}

void createWindows()
{
	namedWindow("Video feed",1);
	namedWindow("BGS",1);
	namedWindow("Detected skin filled-in", 1);
	namedWindow("Fingerletter Scores",1);
}

void displayImages(Mat image, Mat foreground, Mat frameFilled)
{
	imshow("Video feed",image);
	imshow("Detected skin filled-in", frameFilled);
	imshow("BGS", foreground);
}

void applyMask(Mat &src, Mat mask)
{
	Mat tmpFrame;
	tmpFrame = Mat::zeros(src.size(), src.type());
	src.copyTo(tmpFrame, mask);
	tmpFrame.copyTo(src);
}

void cleanMask(Mat &src, int iter)
{
	// Clean things up a bit
	//medianBlur(src,src,9);
    //erode(src,  src, Mat(), Point(-1,-1), iter);
    dilate(src, src, Mat(), Point(-1,-1), iter);
	//morphologyEx(src,src,MORPH_OPEN,getStructuringElement(0, Size(3,3), Point(3,3)));
}

void findHull(vector<vector<Point> > contours, vector<vector<Point> > &hull)
{
	//vector<vector<Point> > hull(contours.size() );
	//for (int i = 0; i < contours.size(); i++)
	//{
	//	convexHull( Mat(contours[i]), hull[i], false);
	//}

	// Get the largest contour
	int largestContour = 0; int largestArea = 0;
	for (int i = 0; i < contours.size(); i++)
	{
		if (contours[i].size() > largestArea)
		{
			largestContour = i;
			largestArea = contours[i].size();
		}
	}

	// Get the convex hull for that contour
	//vector<Point> hull;
	convexHull(Mat(contours[largestContour]), hull[0], false);
}

void computeMoments(vector<vector<Point> > hull, vector<Moments> &mu)
{
	mu[0] = moments(hull[0], false);
}

void generateFLTemplates(VideoCapture cam, vector<Moments> &templateMoments, vector<vector<Point> > &templateContours, Mat background)
{
	// This function has the user go through all the finger letters and create a template for each one.
	//	The moments for the resulting hull shape are then saved and will be used later for matching.
	// Mats (images)
	Mat image, frame, frameHSV, frameBinary, frameEdges, frameFilled, mask;

	char letterList[24] = {'a','b','c','d','e','f',
						   'g','h','i','k','l','m',
						   'n','o','p','q','r','s',
						   't','u','v','w','x','y'};
	char curLetter[2];


	// Create our image and recording structures
	vector< vector< Point > > contours;
	vector< Vec4i > hierarchy;
	Mat foreground, difference, tmpFrame;
	int bgsThresh = 30; // Background subtraction threshold
	//vector<Moments> templateMoments(24);

	namedWindow("Generating ASL Templates");
	namedWindow("Mask Window");

	int i = 0;
	while (i<24)
	{
		// Get a new frame
        cam >> frame;
        if( frame.empty() )
                break;
		image = frame.clone();

		// As copied from main()
		FindDifferenceGray(frame, background, difference);
        threshold(difference, foreground, bgsThresh, 255, THRESH_BINARY);
		cleanMask(foreground, 5);
		applyMask(frame, foreground);
		cvtColor(frame, frameHSV, CV_RGB2HSV);
		inRange(frameHSV, Scalar(Hmin,Smin,Vmin), Scalar(Hmax,Smax,Vmax), frameBinary);
		frameFilled = frameBinary.clone();
		findContours(frameFilled, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS);
		if (contours.size() == 0)
			continue;
		vector<vector<Point> > hull(1);
		findHull(contours, hull);
		drawContours(frameFilled, hull, 0, Scalar(0,255,0), 1, 8, vector<Vec4i>(), 0, Point() );
		drawContours(image, hull, 0, Scalar(255,0,0), 3, 8, vector<Vec4i>(), 0, Point() );

		// Put the current letter to sign in the image
		sprintf(curLetter, "%c", letterList[i]);
		putText(image, curLetter, Point(0,20), FONT_HERSHEY_SIMPLEX, 1, Scalar(0,255,0), 2, 8, false);

		// Display image
		imshow("Generating ASL Templates", image);
		imshow("Mask Window", frameBinary);

		int c = cvWaitKey(15);
		if((char)c==27) // 27 is the escape key
			return;
		else if (c == ' ') // hit spacebar to take a snapshot and move on
		{
			//templateContours[i].reserve(hull[0].size());
			templateContours[i] = hull[0];
			//templateContours[i].assign(hull[0].begin(), hull[0].end() );

			templateMoments[i] = moments(hull[0], false);
			i++;
		}
	}

	destroyWindow("Generating ASL Templates");
	destroyWindow("Mask Window");

}

void printMoments(vector<Moments> templateMoments)
{
	char letterList[24] = {'a','b','c','d','e','f',
						   'g','h','i','k','l','m',
						   'n','o','p','q','r','s',
						   't','u','v','w','x','y'};
	char curLetter[2];

	printf("\n# M00 M01 M02 M03 M10 M11 M12 M20 M21 M30 Mu02 Mu03 Mu11 Mu12 Mu20 Mu21 Mu30 Nu02 Nu03 Nu11 Nu12 Nu20 Nu21 Nu30");
	for (int i=0; i<24; i++)
	{
		sprintf(curLetter, "%c", letterList[i]);
		printf("\n%c: %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.4f %2.4f %2.4f %2.4f %2.4f %2.4f",
									curLetter[0],
									templateMoments[i].m00,
									templateMoments[i].m01,
									templateMoments[i].m02,
									templateMoments[i].m03,
									templateMoments[i].m10,
									templateMoments[i].m11,
									templateMoments[i].m12,
									templateMoments[i].m20,
									templateMoments[i].m21,
									templateMoments[i].m30,
									templateMoments[i].mu02,
									templateMoments[i].mu03,
									templateMoments[i].mu11,
									templateMoments[i].mu12,
									templateMoments[i].mu20,
									templateMoments[i].mu21,
									templateMoments[i].mu30,
									templateMoments[i].nu02,
									templateMoments[i].nu03,
									templateMoments[i].nu11,
									templateMoments[i].nu12,
									templateMoments[i].nu20,
									templateMoments[i].nu21,
									templateMoments[i].nu30);

	}
}

int findMatch(vector<Moments> templateMoments, vector<Moments> objectMoments, vector<vector<Point> > templateContours, vector<vector<Point> > objectContours, double savedScores[24])
{
	double score = 0,
		   bestScore = 10;
	int indexSign = 0;
	printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	printf("\nMatching templates:");
	for (int i=0; i<24; i++)
	{
		score = cv::matchShapes(templateContours[i], objectContours[0], CV_CONTOURS_MATCH_I3, 0);
		savedScores[i] = score;
		printf("\nScore %d: %f", i, score);
		if (score<bestScore)
		{
			bestScore = score;
			indexSign = i;
		}
	}

	printf("\n\n\n Best Score: %f from %d", bestScore, indexSign);
	printf("\n Object Hu Moments: %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f %2.1f",
		objectMoments[0].nu02, objectMoments[0].nu03, objectMoments[0].nu11, objectMoments[0].nu12, objectMoments[0].nu20, objectMoments[0].nu21, objectMoments[0].nu30);

	return indexSign;
}

void displayPrediction(Mat &image, int indexSign)
{
	char letterList[24] = {'a','b','c','d','e','f',
						   'g','h','i','k','l','m',
						   'n','o','p','q','r','s',
						   't','u','v','w','x','y'};
	char curLetter[2];

	sprintf(curLetter, "%c", letterList[indexSign]);
	putText(image, curLetter, Point(0,20), FONT_HERSHEY_SIMPLEX, 1, Scalar(0,255,0), 2, 8, false);
}

void plotScores(Mat &src, double scores[24])
{
	src = Scalar::all(0);

	double imgWdth = src.cols;
	double imgHght = src.rows;

	int barWdth = imgWdth / 24;
	double yLim = 2; // the max score allowable

	// We need to leave space at the bottom of the window to print the letters
	double letterHght = 20;
	double usableImgHght = imgHght - letterHght;

	Point pt1 = cvPoint(0,0);
	Point pt2 = cvPoint(0,0);

	for (int i = 0; i<24; i++)
	{
		pt1 = cvPoint(barWdth*i,usableImgHght); // lower left corner
		pt2 = cvPoint(barWdth*(i+1), usableImgHght*(1-(scores[i]/yLim)) ); // upper right corner

		cv::rectangle( src, pt1, pt2, Scalar(255,0,0),CV_FILLED,8,0);
	}

	addLetters(src);
}

void addLetters(Mat &src)
{
	char letterList[24] = {'a','b','c','d','e','f',
						   'g','h','i','k','l','m',
						   'n','o','p','q','r','s',
						   't','u','v','w','x','y'};
	char curLetter[2];

	double imgWdth = src.cols;
	double imgHght = src.rows;
	double barWdth = imgWdth / 24;

	for (int i = 0; i<24; i++)
	{
		sprintf(curLetter, "%c", letterList[i]);
		putText(src, curLetter, Point(i*barWdth,imgHght), FONT_HERSHEY_SIMPLEX, 1, Scalar(0,255,0), 2, 8, false);
	}


}


