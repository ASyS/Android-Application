package com.duckie.b;

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
	private static int STEP_X = 20;
	private static int STEP_Y = 20;
	private static int direction_counter = 0;
	private static int current_direction = 0;
	private static int prev_direction = 0;

	//private static final String TAG = "CD";
	private static Mat mSrc, mThresh, mHierarchy;

	//tracking parameters (in sec)
	//number of cyclic frame buffer used for motion detection
	//depend on FPS
	
	//ring image buffer

	
	
	//temporary images
	public static void update_mhi(Mat img, Mat dst, int diff_threshold){
		
	}
	
	public static void draw_opticflow(Mat flow, Mat cflowmap, int step, Scalar color){
		int i=0,j = 0;
		for(int y=0; y<cflowmap.rows(); y+= step){
			for(int x=0; x<cflowmap.cols(); x+= step){
				
				double[] fxy = Sample1View.flow.get(50, 50);
				
				//for testing, getting change in values sa frames at point 50,50
				//result : fxy ng.change ang value, however, kun i.add na ang 50, mura xag ma.zero?? 
				i = (int) Math.ceil(fxy[0]);
				j = (int) (50.00+fxy[1]);		
				
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

	
	public static Point detectHull(Mat src_gray, Mat dst){
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
		Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2RGBA);
		
		//hullPoints - set of HULL POINTS CALCULATED from convexHull
		Imgproc.drawContours( dst, hullPoints, -1,  new Scalar(255,0,0, 255), 1);
		return hullPeak;
	}
	
	public static void detectMotion(Mat src, Mat dst){
		Point center, peak;
//		temp = prevgray;
//		prevgray = gray;
//		gray = temp;		
//    	cvt_YUVtoGRAY(src, Sample1View.gray);
		center = trackCenter(src);
		peak = detectHull(src,dst);
		
//		getDirection(peak, 0);
		
//		Core.putText(dst, Integer.toString(current_direction), 
//		new Point(10, 100), 
//		3/* CV_FONT_HERSHEY_COMPLEX */, 2,
//		new Scalar(255, 0, 0, 255), 3);
		
//		if(peak.x != 0){
//			if(peak.x<center.x){
//				//pinky finger
//				Core.putText(dst, "Pinky", 
//						new Point(10, 100), 
//						3/* CV_FONT_HERSHEY_COMPLEX */, 2,
//						new Scalar(255, 0, 0, 255), 3);
//			}
//			else if(peak.x>center.x){
//				//index finger
//				Core.putText(dst, "Index", 
//						new Point(10, 100), 
//						3/* CV_FONT_HERSHEY_COMPLEX */, 2,
//						new Scalar(255, 0, 0, 255), 3);
//			}
//		}
//		if(!Sample1View.prevgray.empty()){
//			Video.calcOpticalFlowFarneback(Sample1View.prevgray, Sample1View.gray, Sample1View.flow, 0.5, 3, 15, 3, 5, 1.2, 0);
//			Imgproc.cvtColor(Sample1View.prevgray, dst, Imgproc.COLOR_GRAY2RGB);
//			//draw_opticflow(Sample1View.flow, dst, 36, new Scalar(0, 255, 0));
////	    	cvt_YUVtoGRAY(src, dst);
//		}
////		gray.copyTo(prevgray);
//		Sample1View.prevgray = Sample1View.gray;
//    	try {
//			Thread.sleep(20);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	cvt_YUVtoGRAY(src, dst);
	}
}
