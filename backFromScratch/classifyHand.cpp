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
void findTips(Mat image);

/**
 * @function main
 * @brief Main function
 */
int main() {
//  if( argc != 3 )
//  { readme(); return -1; }

	String samples[] = {"K0.jpg", "K1.jpg", "V0.jpg", "V1.jpg"};
	for (int i = 0; i < 4; i++) {
		Mat src = imread(samples[i]);
		int imgHeight = src.rows;

		if (!src.data) {
			cout << " --(!) Error reading images " << endl;
			return -1;
		}

		// Subtract external background from hand
		src = subtractBG(src);
//		furtherClean(src);
		int handHeight = src.rows;
//		cout<<imgHeight;
		if (isHeightClass(imgHeight, handHeight)) {
			cout << samples[i] << ": height classification" << endl;
			findTips(src);
//			cout << samples[i] << "\t" << src.cols << "\t" << handHeight << "\t"
//					<< centroid.x << "\t" << centroid.y << "\tvertical" << "\t"<<handHeight<<endl;
		} else {
//			cout << samples[i] << ": horizontal classification" << endl;
			if (isCentroidClass(src)){
				cout << samples[i] << ": centroid classification" << endl;
//				findTips(src);
			}
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
	inRange(temp, Scalar(100, 60, 60), Scalar(125, 255, 255), temp); //(90, 50, 50), Scalar(160, 255, 255)
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
	threshold(image, binary, 0, 255, THRESH_BINARY);

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

void findTips(Mat image){
	int left = 0, right = 0;
	vector<vector<Point> > hull(1);
	vector<Point> hullList;
	Point hullArray[2];
	Point KV_PtsArray[3];
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	Mat temp = image.clone();

	threshold(temp, temp, 0, 255, THRESH_BINARY);
	cvtColor(temp, temp, CV_BGR2GRAY);
	Mat binary = temp.clone();
	findContours(temp, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);

//	cout << indexOfBiggestContour << endl;
//	approxPolyDP(contours[0], contours[0], 1, true);
	convexHull(Mat(contours[0]), hull[0], false);

	int pointCount = 0;

	for(int j = 0; j < hull[0].size(); j++){
//		cout<<image.cols;
		if(hull[0][j].y < image.rows*0.25){
//			if(j != hull[0].size()-1){
			if (sqrt(pow((float)(hull[0][j].x - hull[0][j + 1].x), 2) + pow((float)(hull[0][j].y - hull[0][j + 1].y), 2))/image.cols > 0.08){
				if(pointCount < 2){
					hullArray[pointCount] = hull[0][j];
					pointCount++;
				}
				circle(image, hull[0][j], 5, Scalar(0, 0, 255), -1);
				cout<<hull[0][j]<<endl;
				if(hull[0][j].x > image.cols/2)
					right += 1;
				else
					left += 1;
			}
//			}
		}
	}

	// Arrange.
	if(hullArray[0].x > hullArray[1].x){
		Point tempCont = hullArray[0];
		hullArray[0] = hullArray[1];
		hullArray[1] = tempCont;
	}

	if(left == 0){
		if(right == 1) cout<<"L!!!"<<endl;
		else{
			if(hullArray[0].y > hullArray[1].y) cout<<"R!!!"<< endl;
			else cout<<"U!!!"<< endl;
		}
	}
	else if(left == 1){
		if(right == 0) cout<<"D!!!"<<endl;
		else if (right > 2) cout<<"W!!!"<<endl;
		else{
//			Point center((hullArray[0].x + hullArray[1].x)/2, (hullArray[0].y + hullArray[1].y)/2);
//			circle(image, Point(center.x, center.y), 5, Scalar(255, 0, 0), -1);
			int startX = hullArray[0].x;
			int startY = hullArray[0].y;
			int lastDir = 0;	// 0 up; 1 down
			int currDir;
			int ctr = 0;
			while(startX != hullArray[1].x){
				for (int k = 0; k < binary.rows; ++k){
					uchar * pixel = binary.ptr<uchar>(k);
					cout<<"x: "<<startX<<endl;
					cout<<"y: "<<startY<<endl;
					cout<<"k: "<<k<<endl;
					if(pixel[startX+1] != 0){
						// Plots the edges from hullPoint[0] to hullPoint[1]
//						circle(image, Point(startX+1, k), 5, Scalar(0, 255, 0), -1);
						if(k > startY)	currDir = 1;
						else if(k < startY) currDir = 0;
						if(currDir != lastDir && ctr < 3){
							circle(image, Point(startX+1, k), 5, Scalar(255, 0, 0), -1);
							KV_PtsArray[ctr] = Point(startX+1, k);
							ctr++;
						}
						lastDir = currDir;
						cout<<"in2"<<endl;
//						circle(image, Point(startX+1, k), 5, Scalar(0, 255, 0), -1);
						startX = startX + 1;
						startY = k;
						break;
					}
				}
//				if(startX == hullArray[1].x && ctr!=3){
//					ctr++;
//					KV_PtsArray[ctr] = Point(startX, startY);
//				}
			}
//			cout<<ctr<<endl;
//			cout<<hullArray[0]<<endl;
//			cout<<hullArray[1]<<endl;
//			cout<<KV_PtsArray[0]<<endl;
//			cout<<KV_PtsArray[1]<<endl;
//			cout<<KV_PtsArray[2]<<endl;
			float d1 = sqrt(pow((float)(KV_PtsArray[0].x - KV_PtsArray[1].x), 2) + pow((float)(KV_PtsArray[0].y - KV_PtsArray[1].y), 2));
			float d2 = sqrt(pow((float)(KV_PtsArray[2].x - KV_PtsArray[1].x), 2) + pow((float)(KV_PtsArray[2].y - KV_PtsArray[1].y), 2));
			if(d2 < d1/2)	cout<<"K!!!"<<endl;
			else cout<<"V!!!"<<endl;
		}
	}
	else if(left > 1 && right < 2) cout<<"F!!!"<<endl;
	else if(left > 1 && right > 1) cout<<"B!!!"<<endl;

//	cout<<"left\t"<<left<<endl;
//	cout<<"right\t"<<right<<endl;
	imshow("hull", image);
	waitKey(0);
}
