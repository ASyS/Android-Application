package com.duckie.b;

import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class Motion {
  //private static final String TAG = "CD";
	private static Mat mSrc;
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
				i = (int) Math.round(fxy[0]);
				j = (int) (50.00+fxy[1]);		
				
				Core.putText(cflowmap, Double.toString(j)
						, new Point(10, 100), 
            			3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);

				//Core.line(cflowmap, new Point(x,y), new Point(i,j), new Scalar(0,0,255), 4);
				//Core.circle(cflowmap, new Point(i,j), 2, new Scalar(0,0,255), -1);
				//Core.circle(cflowmap, new Point(x,y), 2, color, -1);
				//System.out.print("fxy"+i+" "+j+"\n");
			
	            //Core.circle(cflowmap, new Point(cflowmap.width(), 50), 5, new Scalar(255,255,255), -1);
	            if(i<(cflowmap.width()/2) && i!=0.0){
	            	Core.circle(cflowmap, new Point(i,j), 2, new Scalar(0,0,255), -1);
	            	Core.putText(cflowmap, "LEFT", new Point(10, 100), 
	            			3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 255, 255, 255), 3);
	            	
	            }

	            else if(i>(cflowmap.width()/2) && i!=x){
	                Core.circle(cflowmap, new Point(i, j), 2, new Scalar(0,0,255), -1);
	                Core.putText(cflowmap, "RIGHT", new Point(10, 100), 
	            			3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 255, 255, 255), 3);
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
	
	public static void detectMotion(Mat src, Mat dst){
//		temp = prevgray;
//		prevgray = gray;
//		gray = temp;		
    	cvt_YUVtoGRAY(src, Sample1View.gray);
		if(!Sample1View.prevgray.empty()){
			Video.calcOpticalFlowFarneback(Sample1View.prevgray, Sample1View.gray, Sample1View.flow, 0.5, 3, 15, 3, 5, 1.2, 0);
			Imgproc.cvtColor(Sample1View.prevgray, dst, Imgproc.COLOR_GRAY2RGB);
			draw_opticflow(Sample1View.flow, dst, 36, new Scalar(0, 255, 0));
//	    	cvt_YUVtoGRAY(src, dst);
		}
//		gray.copyTo(prevgray);
		Sample1View.prevgray = Sample1View.gray;
    	try {
			Thread.sleep(20);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	cvt_YUVtoGRAY(src, dst);
	}
}
