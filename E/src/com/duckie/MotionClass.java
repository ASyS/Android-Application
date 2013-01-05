package com.duckie;


import java.util.ArrayList;
import java.util.List;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import android.app.Activity;



public class MotionClass extends Activity{
//	public Motion(long millisInFuture, long countDownInterval) {
//		super(millisInFuture, countDownInterval);
//		// TODO Auto-generated constructor stub
//	}

	private static final int STATIC = 0;
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private static final int DOWN = 3;
	private static final int UP = 4;
	
	private static int lastX = -1;
	private static int lastY = -1;
	private static double LAST_STEPx = -1;
	private static double LAST_STEPy = -1;
	private static int STEP_X = 100;
	private static int STEP_Y = 50;
	private static int current_orientation = 0 ;
	private static int direction_counter = 0;
	private static int current_direction = 0;
							//	2 right
							//	1 left
							//	3 down
							//	4 up
	private static int prev_direction = 0;
	
	private static Point center, peak, track_point;

	private static char direction_pattern = '?';
	private static String letter = null;
	private static String direction = "Static";
	//private static final String TAG = "CD";
	private static Mat mThresh, mHierarchy;
//	private static Timer timer = new Timer();	
//    private static RefreshHandler 		mRedrawHandler;// = new RefreshHandler();
    static int flag =0;	
//    class RefreshHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//          //Motion.this.updateUI();
//        }
//
//        public void sleep(long delayMillis) {
//          this.removeMessages(0);
//          sendMessageDelayed(obtainMessage(0), delayMillis);
//        }
//      };
    

      
	public static Point getCenter(){
		return center;
	}
	
	public static int getOrientation(){
		return current_orientation;
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
					//current_direction = 0;
					//no movement				   
//					timer.schedule(ResetDirection(), 2000);
//					counter.start();
//				    if(prev_direction == current_direction){
//				    	current_direction = 0;
//				    }
//					 new CountDownTimer(2000, 1000) {
//					     public void onTick(long millisUntilFinished) {
//					        System.out.println("seconds remaining: " + millisUntilFinished / 1000);
//					     }
//
//					     public void onFinish() {
//					    	 System.out.println("done!");
//					     }
//					  }.start();
				    //break;
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
					//current_direction = 0;
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

	
//	private static TimerTask ResetDirection() {
//		// TODO Auto-generated method stub
//		System.out.println("Timer's UP!");
//		return null;
//	}
	
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
		List<Point> hullPointList  = new ArrayList<Point>(mContours.size());
	    
	    
	    MatOfPoint hullPointMat = new MatOfPoint();
		MatOfInt hullInt = new MatOfInt();
		
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
				//if(prev_direction != current_direction){
				if(current_direction == DOWN){
					//moving down : possible motion for J
					direction_pattern = 'J';
					direction_counter++;
					//motion_letter = " ";
				}
				else{
					direction_counter = 0;
					//motion_letter = "?";
				}
				prev_direction = current_direction;
				//}
				break;
			case 1:
				//if(prev_direction != current_direction){
				if(current_direction == DOWN){
					//moving up : possible motion for J
					direction_counter++;
					motion_letter = "J";
					//direction_counter = 0;
					//current_direction = 0;
					break;
				}
				else{
					direction_counter = 0;
					//motion_letter = "?";
				}
				prev_direction = current_direction;
				//}
				break;
			case 2:
				//if(prev_direction != current_direction){
				if(current_direction == UP){
					//moving down : possible motion for J
					//direction_counter++;
					motion_letter = "J";
					direction_counter = 0;
					current_direction = 0;
				}
				else{
					direction_counter = 0;
					//motion_letter = "?";
				}
				prev_direction = current_direction;
				//}
				break;
			default:
				direction_counter = 0;
				break;
		}	
		System.out.println("J: Counter "+direction_counter+" "+motion_letter);
		return motion_letter;
	}
	
	public static String checkZMotionPath(){
		String motion_letter = null;
		switch(direction_counter){
		case 0:
			//if(prev_direction != current_direction){
			if(current_direction == RIGHT){
				direction_counter++;
				//motion_letter = " ";
			}
			else{
				direction_counter = 0;
			}
			prev_direction = current_direction;
			//}
			break;
		case 1:
			//if(prev_direction != current_direction){
			if(current_direction == LEFT){
				direction_counter++;
				//direction_counter = 0;
				motion_letter = "Z";
			}
			else{
				direction_counter = 0;
			}
			prev_direction = current_direction;
			//}
			break;
		case 2:
			//if(prev_direction != current_direction){
			if(current_direction == RIGHT){
				motion_letter = "Z";
				direction_counter = 0;
				current_direction = 0;
				break;
			}
			else{
				direction_counter = 0;
			}
			prev_direction = current_direction;
			//}
			break;
		default:
			direction_counter = 0;
			break;
		}
		System.out.println("Z: Counter "+direction_counter+" "+motion_letter);

		return motion_letter;
	}
	
	public static String getASLMotionLetter(){
//		letter = checkZMotionPath();
//		letter = Character.toString(direction_pattern);
//		switch(direction_pattern){
//			case 'J' :
//				letter = "J";
//				break;
//			case 'Z' :
//				letter = "Z";
//				break;
//			default:
//				letter = "?";
//				break;
//		}
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
	
	public static void detectMotion(Mat object, Mat dst){
		//Point center, peak;
		//int flag = 0;
		center = trackCenter(object);
		peak = detectHull(object);	
//		current_orientation = 0;
		Core.circle(dst, center, 5, new Scalar(255,0,0));
		Core.circle(dst, peak, 5, new Scalar(255,0,0));
		track_point = center;
//		getDirection(center, current_orientation);
//		//new JavaTimer(5000, current_direction, prev_direction);
		if(peak.x != 0){
			if(peak.x<center.x){
				//pinky finger
				direction_pattern = 'J';
				current_orientation = 1;
				track_point = peak;
				//getDirection(center, 1);
				flag = 1;
			}
			else if(peak.x>center.x){	
				//index finger
				if(flag == 1){
					track_point = peak;
					direction_pattern = 'J';
					current_orientation = 1;
					//getDirection(center, 1);

					flag = 0;
				}
				else{
					track_point = center;				
					direction_pattern = 'Z';
					current_orientation = 0;
					//getDirection(center, 0);
				}
				
			}
		}
		else{
			direction_pattern = '?';
		}
		getDirection(track_point, current_orientation);
		AndroidTimer timer = new AndroidTimer() {			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				getDirection(track_point, current_orientation);
				if(current_direction == prev_direction){
				current_direction = 0;
				}
			
				System.out.println("Time's up!");
			}
		};
		if(current_direction != 0){
			prev_direction = current_direction;
			timer.start();
		}else{
			timer.cancel();
			timer.setTimer(10);
		}
	}
	
	public static String getCurrentDirection(){
		switch(current_direction){
			case STATIC:
				direction = "Static";
				break;
			case LEFT:
				direction = "Left";
				break;
			case RIGHT:
				direction = "Right";
				break;
			case DOWN:
				direction = "Down";
				break;
			case UP:
				direction = "Up";
				break;
			default:
				direction = "Static";
				break;
		}
		return direction;
	}

	public static int getDirectionCounter(){
		return direction_counter;
	}
}
