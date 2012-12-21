package com.duckie;

import java.util.ArrayList;
import java.util.List;

import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

public class Motion {
	private static int lastX = -1;
	private static int lastY = -1;
	private static double LAST_STEPx = -1;
	private static double LAST_STEPy = -1;
	private static int STEP_X = 40;
	private static int STEP_Y = 30;
	private static int direction_counter = 0;
	private static int current_direction = 0;
							//	2 right
							//	1 left
							//	4 down
							//	3 up
	private static int prev_direction = 0;
	private static char direction_pattern = '?';

	//private static final String TAG = "CD";
	private static Mat mSrc, mThresh, mHierarchy;

	public static void draw_opticflow(Mat flow, Mat cflowmap, int step, Scalar color){
		int i=0,j = 0;
		for(int y=0; y<cflowmap.rows(); y+= step){
			for(int x=0; x<cflowmap.cols(); x+= step){
				
				//double[] fxy = Sample1View.flow.get(50, 50);
				
				//for testing, getting change in values sa frames at point 50,50
				//result : fxy ng.change ang value, however, kun i.add na ang 50, mura xag ma.zero?? 
				//i = (int) Math.ceil(fxy[0]);
				//j = (int) (50.00+fxy[1]);		
				
				Core.putText(cflowmap, Integer.toString(i)
						, new Point(10, 100), 
            			3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);

				//Core.line(cflowmap, new Point(x,y), new Point(i,j), new Scalar(0,0,255), 4);
				//Core.circle(cflowmap, new Point(i,j), 2, new Scalar(0,0,255), -1);
				//Core.circle(cflowmap, new Point(x,y), 2, color, -1);
				//System.out.print("fxy"+i+" "+j+"\n");
			
	            //Core.circle(cflowmap, new Point(cflowmap.width(), 50), 5, new Scalar(255,255,255), -1);
	            if(i<(cflowmap.width()/2) && i!=50){
	            	Core.circle(cflowmap, new Point(i,j), 2, new Scalar(0,0,255), -1);
//	            	Core.putText(cflowmap, "LEFT", new Point(10, 100), 
//	            			3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 255, 255, 255), 3);
	            	
	            }
	            
	            else if(i>(cflowmap.width()/2) && i!=x){
	                Core.circle(cflowmap, new Point(i, j), 2, new Scalar(0,0,255), -1);
//	                Core.putText(cflowmap, "RIGHT", new Point(10, 100), 
//	            			3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 255, 255, 255), 3);
	            }
			
			}
		}
	}
	
	public static void cvt_YUVtoGRAY(Mat src, Mat dst){
		/** convert YUV image to RGB then GRAY colorspace */
		mSrc = new Mat();
		src.copyTo(mSrc); 
		Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
		Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2GRAY);
	}
	
	public static int getCurrentDirection(){
		return current_direction;
	}
	
	public static void getDirection(Point current_pos, int orientation){
		if(current_pos.x > 0 && current_pos.y > 0){
			if(LAST_STEPx <0 && LAST_STEPy <0){
		 	   LAST_STEPx = current_pos.x;
		 	   LAST_STEPy = current_pos.y;
		    }

			switch(orientation){
			case 0:
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
				else{
					current_direction = 0;
					//no movement
				}
				break;
			case 1:
				//get vertical direction
				if((current_pos.y - LAST_STEPy)>STEP_Y){
					LAST_STEPy = current_pos.y;
					current_direction = 3;
					//cout<<"MOVING DOWN\n"<<endl;
				}
				else if((current_pos.y - LAST_STEPy)<-(STEP_Y)){
					LAST_STEPy = current_pos.y;
					current_direction = 4;
					//cout<<"MOVING UP\n"<<endl;
				}
				else{
					current_direction = 0;
					//no movement
				}
				break;
			default:
				current_direction = 0;
				break;
				
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

	
	public static Point trackCenter(Mat Object){
	    // Calculate the moments of 'imgThresh'
		Point current_pos = new Point(0,0);
		
		//CvMoments *moments = (CvMoments*)malloc(sizeof(CvMoments));
	    //Moments(Object, moments, 1);
		Moments moments;
		moments = Imgproc.moments(Object);
		
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

	
	public static Point detectHull(Mat src_gray){
		mThresh = new Mat();
		mHierarchy = new Mat();
		Point hullPeak = new Point(0,0);
		
		List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
		List<MatOfPoint> defectsPoints = new ArrayList<MatOfPoint>();
		List<Point> hullPointList  = new ArrayList<Point>(mContours.size());
	    List<Point> defectsPointList = new ArrayList<Point>(mContours.size());;
	    
	    
	    MatOfPoint hullPointMat = new MatOfPoint();
		MatOfInt hullInt = new MatOfInt();
		MatOfInt4 defectsInt = new MatOfInt4();
		MatOfPoint defectsPointMat = new MatOfPoint();
		
		/// Detect edges using Threshold
		Imgproc.GaussianBlur(src_gray, src_gray, new Size(9, 9), 2, 2);    
		Imgproc.threshold(src_gray, mThresh, 30, 255, Imgproc.THRESH_BINARY);    
		
		
		Imgproc.findContours(mThresh, mContours, mHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		 
		for (int k=0; k < mContours.size(); k++){
			Imgproc.convexHull(mContours.get(k), hullInt);			
//			Imgproc.convexityDefects(mContours.get(k), hullInt, defectsInt);
			
			for(int j=0; j < hullInt.toList().size(); j++){				
		        hullPointList.add(mContours.get(k).toList().get(hullInt.toList().get(j)));
		    }

//			for(int j=0; j < defectsInt.toList().size(); j++){					     
//				defectsPointList.add(mContours.get(k).toList().get(defectsInt.toList().get(j)));
//		    }
			
			hullPointMat.fromList(hullPointList);
		    hullPoints.add(hullPointMat);
		    	    
//			defectsPointMat.fromList(defectsPointList);
//		    defectsPoints.add(defectsPointMat);   
		}
		
		//GET THE HIGHEST PEAK
		for (int i = 0; i<hullPointList.size(); i++){
			if(hullPeak.y != 0){
				if(hullPointList.get(i).y<hullPeak.y){
					hullPeak = hullPointList.get(i);
				}
			}
			else{
				hullPeak = hullPointList.get(i);
			}
		 }
		//Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2RGBA);
		
		//hullPoints - set of HULL POINTS CALCULATED from convexHull
		//Imgproc.drawContours( dst, hullPoints, -1,  new Scalar(255,0,0, 255), 1);
		return hullPeak;
	}

	public static String checkJMotionPath(){
		String motion_letter = null;
		switch(direction_counter){
			case 0:
				if(prev_direction != current_direction){
					if(current_direction == 4){
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
						//motion_letter = "?";
						/*putText(img, motion_letter, Point(0,100),
								FONT_HERSHEY_SIMPLEX, 3,
								Scalar(255,255,255), 2, 8, false);*/
					}
					prev_direction = current_direction;
				}
				break;
			case 1:
				if(prev_direction != current_direction){
					if(current_direction == 3){
						//moving up : possible motion for J
						motion_letter = "J";
						/*putText(img, motion_letter, Point(0,100),
								FONT_HERSHEY_SIMPLEX, 3,
								Scalar(255,255,255), 2, 8, false);*/
						//direction_counter = 0;
						current_direction = 0;
						break;
					}
					else{
						direction_counter = 0;
						//motion_letter = "?";
					}
					prev_direction = current_direction;
				}
				break;
			default:
				direction_counter = 0;
				break;
		}	
		return motion_letter;
	}
	
	public static String checkZMotionPath(){
		String motion_letter = null;
		switch(direction_counter){
		case 0:
			if(prev_direction != current_direction){
				if(current_direction == 2){
					//moving right : possible motion for Z
					//direction_pattern = 'Z';
					direction_counter++;
					motion_letter = " ";
				}
				else if(current_direction == 0){
					direction_counter = 0;
					motion_letter = " ";
				}
				else{
					direction_counter = 0;
					//motion_letter = "?";
					/*putText(img, motion_letter, Point(0,100),
							FONT_HERSHEY_SIMPLEX, 3,
							Scalar(255,255,255), 2, 8, false);*/
				}
				prev_direction = current_direction;
			}
			break;
		case 1:
			if(prev_direction != current_direction){
				if(current_direction == 1){
					//moving left : possible motion for Z
					//direction_pattern = 'Z';
					direction_counter++;
					motion_letter = " ";
				}
				else{
					direction_counter = 0;
					//motion_letter = "?";
				}
				prev_direction = current_direction;
			}
			break;
		case 2:
			if(prev_direction != current_direction){
				if(current_direction == 2){
					//moving left down : possible motion for Z
					motion_letter = "Z";
					/*putText(img, motion_letter, Point(0,100),
							FONT_HERSHEY_SIMPLEX, 3,
							Scalar(255,255,255), 2, 8, false);*/
					direction_counter = 0;
					current_direction = 0;
					break;
				}
				else{
					direction_counter = 0;
					//motion_letter = "?";
				}
				prev_direction = current_direction;
			}
			break;
		default:
			direction_counter = 0;
			break;
		}
		return motion_letter;
	}
	
	public static String getASLMotionLetter(){
		String letter = null;
		if(direction_pattern == 'J'){			
			//return "J";
			//return Integer.toString(current_direction);
			letter = checkJMotionPath();
		}
		else if(direction_pattern == 'Z'){
			//return "Z";
			//return Integer.toString(current_direction);
			letter = checkZMotionPath();
		}
		else{
			letter = "?";
		}
		return letter;
	}	
	
	public static void detectMotion(Mat src, Mat dst){
		Point center, peak;
		int flag = 0;
		
//		temp = prevgray;
//		prevgray = gray;
//		gray = temp;		
//    	cvt_YUVtoGRAY(src, Sample1View.gray);
		center = trackCenter(src);
		peak = detectHull(src);
		
		if(peak.x != 0){
			if(peak.x<center.x){
				//pinky finger
				direction_pattern = 'J';
				getDirection(center, 1);
				flag = 1;
			}
			else if(peak.x>center.x){	
				//index finger
				if(flag == 1){
					direction_pattern = 'J';
					//getDirection(center, 1);
					current_direction = 3;
				}
				else{
					direction_pattern = 'Z';
					getDirection(center, 0);
				}
				flag = 0;
			}
		}
		else{
			direction_pattern = '?';
		}
	}
}
