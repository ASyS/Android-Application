package org.opencv.samples.tutorial1;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/*#include "opencv2/video/background_segm.hpp"
#include "opencv2/features2d/features2d.hpp"*/

public class ASLMotionID{

	private static double lastX = -1;
	private static double lastY = -1;
	private static double LAST_STEPx = -1;
	private static double LAST_STEPy = -1;
	private static int STEP_X = 20;
	private static int STEP_Y = 20;
	private static int direction_counter = 0;
	private static int current_direction = 0;
	private static int prev_direction = 0;

	private static char direction_pattern = '?';
	public static String motion_letter = " ";
	private static char color = '?';	
	
	private static Scalar s_red = new Scalar(255,0,0,255);
	//private static Scalar s_green = new Scalar(0,255,0);
	private static Scalar s_blue = new Scalar(0,0,255,255);
	private static Scalar s_white = new Scalar(255,255,255,255);
	private static Point p_color1 = new Point(0,0), p_color2 = new Point(0,0);
	 
	public static void printHelloWorld(){
		System.out.println("HELLO WORLD.");
	}
	
	public static void putToCameraScreen(Mat frame, String text){
		/*putText(frame, text, Point(0,100),
				FONT_HERSHEY_SIMPLEX, 3,
				Scalar(255,255,255), 2, 8, false);*/
		Core.putText(frame, text,new Point(0,100), 0/*font*/, 1, s_white, 2,8,false);
	}
	
/*	ColorDetection.getBlueMat(mHsv,mBlue);
 *   ColorDetection.detectSingleBlob(mYuv, mBlue, "B", mResult);
*/
	private static Mat getBlueColoredObjects(Mat image){
		Mat imgThresh = new Mat();//, c1 = new Mat(), c2 = new Mat();
		//Imgproc.cvtColor(image,imgThresh, Imgproc.COLOR_YUV420sp2RGB, 4);
		Imgproc.cvtColor(image,imgThresh,Imgproc.COLOR_RGB2HSV);
		Core.inRange(imgThresh, new Scalar(100, 100, 100), new Scalar(120, 255, 255), imgThresh);
		return imgThresh;
	}

/*	ColorDetection.getRedMat(mHsv,mRed);
    ColorDetection.detectSingleBlob(mResult, mRed, "R", mResult);*/	

	private static Mat getRedColoredObjects(Mat image){
		Mat imgThresh = new Mat(), c1 = new Mat(), c2 = new Mat();
		Imgproc.cvtColor(image, imgThresh, Imgproc.COLOR_YUV420sp2RGB, 4);
		/*Imgproc.cvtColor(image,imgThresh,Imgproc.COLOR_BGR2HSV);
		Core.inRange(imgThresh, new Scalar(100, 100, 100), new Scalar(120, 255, 255), imgThresh);
		return imgThresh;*/
		Imgproc.cvtColor(image,imgThresh,Imgproc.COLOR_RGB2HSV);
		Core.inRange(imgThresh, new Scalar(0, 100, 100), new Scalar(10, 255, 255), c1);
		Core.inRange(imgThresh, new Scalar(170, 100, 100), new Scalar(180, 255, 255), c2);
		Core.bitwise_or(c1, c2, imgThresh);
		return imgThresh;
	}

	
	/**FOR BACKGROUND SUBSTRACTION**/
	public static void applyMask(Mat src, Mat mask){
		Mat tmpFrame = Mat.zeros(src.size(), src.type());
		src.copyTo(tmpFrame, mask);
		tmpFrame.copyTo(src);
	}

	public static void cleanMask(Mat src, int iter){
		// Clean things up a bit
		//medianBlur(src,src,9);
	    //erode(src,  src, Mat(), Point(-1,-1), iter);
	  Imgproc.dilate(src, src, new Mat(), new Point(-1,-1), iter);
		//morphologyEx(src,src,MORPH_OPEN,getStructuringElement(0, Size(3,3), Point(3,3)));
	}

	public static void findDifferenceGray(Mat src1, Mat src2, Mat dst){
		// This function thresholds the difference between the background model image
		//	and the current frame by first converting both to grayscale.

		Mat src1_gray = new Mat(), src2_gray = new Mat();
		Imgproc.cvtColor(src1, src1_gray, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(src2, src2_gray, Imgproc.COLOR_RGB2GRAY);

		//dst = src2_gray - src1_gray 
		Core.absdiff(src2_gray, src1_gray, dst);
	}

	
	/**FOR MOTION TRACKING**/
	public static void getDirection(Point current_pos){
		if(current_pos.x > 0 && current_pos.y > 0){
			if(LAST_STEPx <0 && LAST_STEPy <0){
		 	   LAST_STEPx = current_pos.x;
		 	   LAST_STEPy = current_pos.y;
		    }

			//get horizontal direction
			if((current_pos.x - LAST_STEPx)>STEP_X){
				LAST_STEPx = current_pos.x;
				current_direction = 1;
				//cout<<"MOVING TO THE RIGHT\n"<<endl;
			}
			else if((current_pos.x - LAST_STEPx)<-(STEP_X)){
				LAST_STEPx = current_pos.x;
				current_direction = 2;
				//cout<<"MOVING TO THE LEFT\n"<<endl;
			}

			//get vertical direction
			else if((current_pos.y - LAST_STEPy)>STEP_X){
				LAST_STEPy = current_pos.y;
				current_direction = 3;
				//cout<<"MOVING DOWN\n"<<endl;
			}
			else if((current_pos.y - LAST_STEPy)<-(STEP_X)){
				LAST_STEPy = current_pos.y;
				current_direction = 4;
				//cout<<"MOVING UP\n"<<endl;
			}
			//cout<<"LAST POSITION: "<<LAST_STEPx<<" CURRENT POSITION: "<<current_pos.x<<endl;
			//cout<<"DIFFERENCE: "<<current_pos.x - LAST_STEPx<<"\n"<<endl;
		}
		else{
			LAST_STEPx = -1;
		 	LAST_STEPy = -1;
		 	current_direction = 0;
			//cout<<"NO OBJECT PRESENT"<<endl;
		}
		//return direction;
	}
	
	//ColorDetection.detectSinglePoint(Mat src, Point center)
	//@SuppressWarnings("null")
	static Point trackObject(Mat Object){
	    // Calculate the moments of 'imgThresh'
		Point current_pos= new Point(0,0);
		
		//CvMoments *moments = (CvMoments*)malloc(sizeof(CvMoments));
	    //Moments(Object, moments, 1);
		Moments moments;
		moments = Imgproc.moments(Object, true);
		
		//double moment10 = getSpatialMoment(moments, 1, 0);
		double moment10 = moments.get_m10();
		//double moment01 = GetSpatialMoment(moments, 0, 1);
		double moment01 = moments.get_m01();
		//double area = GetCentralMoment(moments, 0, 0);
		double area = moments.get_m00();

	     // if the area<1000, I consider that the there are no object in the image and it's because of the noise, the area is not zero
	    if(area>1000){
	        // calculate the position of the ball	    	
	        int posX = (int)(moment10/area);
	        int posY = (int)(moment01/area);
	        current_pos.x = posX;
	        current_pos.y = posY;
	       if(lastX>=0 && lastY>=0 && posX>=0 && posY>=0)
	       {
	            // Draw lines from the previous point to the current point
	        	//cvLine(imgTracking1, cvPoint(posX, posY), cvPoint(lastX, lastY), cvScalar(255,0,0), 4);
	       }
	        lastX = posX;
	        lastY = posY;
	    }
	    else{
	        current_pos.x = 0;
	        current_pos.y = 0;
	        lastX = -1;
	        lastY = -1;
	    }
	     
	     return current_pos;
	}
	
	/**FOR ASL LETTER IDENTIFICATION**/
	public static void checkASLletter(Mat img, int color){
		//0 - blue : J; 1 - red : Z
		//0 - blue; 1 - red
		switch(color){
			case 0 :
				switch(direction_counter){
						case 0:
							if(prev_direction != current_direction){
								if(current_direction == 3){
									//moving down : possible motion for J
									direction_pattern = 'J';
									direction_counter++;
									motion_letter = " ";
								}
								else if(current_direction == 0){
									direction_counter = 0;
									//motion_letter = ' ';
								}
								else{
									direction_counter = 0;
									motion_letter = "?";
									/*putText(img, motion_letter, Point(0,100),
											FONT_HERSHEY_SIMPLEX, 3,
											Scalar(255,255,255), 2, 8, false);*/
								}
								prev_direction = current_direction;
							}
							break;
						case 1:
							if(prev_direction != current_direction){
								if(current_direction == 4 && direction_pattern == 'J'){
									//moving up : possible motion for J
									motion_letter = "J";
									/*putText(img, motion_letter, Point(0,100),
											FONT_HERSHEY_SIMPLEX, 3,
											Scalar(255,255,255), 2, 8, false);*/
									//direction_counter = 0;
									break;
								}
								else if(current_direction == 2 && direction_pattern == 'J'){
									//moving left : possible motion for J
									direction_pattern = 'J';
									direction_counter++;
									//motion_letter = ' ';
								}
								else{
									direction_counter = 0;
									//motion_letter = '?';
								}
								prev_direction = current_direction;
							}
							break;
						case 2:
							if(prev_direction != current_direction){
								if(current_direction == 4 && direction_pattern == 'J'){
									//moving up : possible motion for J
									motion_letter = "J";
									/*putText(img, motion_letter, Point(0,100),
											FONT_HERSHEY_SIMPLEX, 3,
											Scalar(255,255,255), 2, 8, false);*/
									//direction_counter = 0;
									break;
								}
								else{
									direction_counter = 0;
									//motion_letter = '?';
								}
								prev_direction = current_direction;
							}
							break;
						default:
							direction_counter = 0;
							break;
					}
					break;
			case 1 :
				switch(direction_counter){
						case 0:
							if(prev_direction != current_direction){
								if(current_direction == 1){
									//moving right : possible motion for Z
									direction_pattern = 'Z';
									direction_counter++;
									//motion_letter = " ";
								}
								else if(current_direction == 0){
									direction_counter = 0;
									//motion_letter = ' ';
								}
								else{
									direction_counter = 0;
									motion_letter = "?";
									/*putText(img, motion_letter, Point(0,100),
											FONT_HERSHEY_SIMPLEX, 3,
											Scalar(255,255,255), 2, 8, false);*/
								}
								prev_direction = current_direction;
							}
							break;
						case 1:
							if(prev_direction != current_direction){
								if(current_direction == 2 && direction_pattern == 'Z'){
									//moving left : possible motion for Z
									direction_pattern = 'Z';
									direction_counter++;
									//motion_letter = ' ';
								}
								else{
									direction_counter = 0;
									//motion_letter = '?';
								}
								prev_direction = current_direction;
							}
							break;
						case 2:
							if(prev_direction != current_direction){
								if(current_direction == 1 && direction_pattern == 'Z'){
									//moving left down : possible motion for Z
									motion_letter = "Z";
									/*putText(img, motion_letter, Point(0,100),
											FONT_HERSHEY_SIMPLEX, 3,
											Scalar(255,255,255), 2, 8, false);*/
									direction_counter = 0;
									break;
								}
								else{
									direction_counter = 0;
									//motion_letter = '?';
								}
								prev_direction = current_direction;
							}
							break;
						default:
							direction_counter = 0;
							break;
					}
					break;
			default :
					break;
		}
		Core.putText(img, motion_letter, new Point(0,100),
				0, 3,
				s_white, 2, 8, false);
		//cout<<motion_letter<<endl;
		//return motion_letter;
	}
	
	static boolean isObjectPresent(Point imgPosition){
		if(imgPosition.x == 0 && imgPosition.y==0)
			return false;
		else
			return true;
	}
	
	public static String getASLmotionLetter(Mat frame, Mat mcoloredframe1, Mat mcoloredframe2){		
      	//Core.putText(frame, "TEST", new Point(10, 100), 3, 2, new Scalar(255, 255, 255, 255), 3);
		/**1 identify what color is present**/
		//if only color1 is present - do letter J motion tracking , 0
		//if only color2 is present - do letter Z motion tracking , 1
		//if both return "?"
		
		//Mat mcoloredframe1 = new Mat(), mcoloredframe2 = new Mat();
		boolean color1 = false;
		boolean color2 = false;
		//0 - blue, 1 - red
		//newimg = frame.clone();
		mcoloredframe1 = getBlueColoredObjects(frame);
		mcoloredframe2 = getRedColoredObjects(frame);

		//track color blue object
		p_color1 = trackObject(mcoloredframe1);

		// track color red object
		p_color2 = trackObject(mcoloredframe2);

		color1 = isObjectPresent(p_color1);
		color2 = isObjectPresent(p_color2);
		/*if(color1)
			Core.putText(frame, "BLUE", new Point(0,100),
					0, 3,
					new Scalar(255,255,255), 2, 8, false);
		else
			Core.putText(frame, "NONE", new Point(0,100),
					0, 3,
					new Scalar(255,255,255), 2, 8, false);*/		
		
		if(color1 && !color2){
			//only blue object is present
			getDirection(p_color1);
			checkASLletter(frame, 0);
			Core.circle(frame, p_color1,
					10, s_blue,
					1, 8, 0);
		}
		else if(!color1 && color2){
			//only red object is present
			getDirection(p_color2);
			checkASLletter(frame, 1);
			Core.circle(frame, p_color2,
							10, s_red,
							1, 8, 0);
		}
		else{
			//no object or both blue and red are present
			Core.putText(frame, " ", new Point(0,100),
						0, 3,
						s_white, 2, 8, false);
			//motion_letter = "NONE";
		}
		return motion_letter;
	}
	
}