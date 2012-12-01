package com.duckie.a;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;

import android.util.Log;


public class ColorDetection {
	private static final String TAG = "CD";
	private static Mat mSrc;

	private static final double lowThresh = 50.0;
	private static final double highThresh = 90.0;


	public static void testIdentifyHandGesture(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv, Mat mYuv) {
		String letter = "";
		
		
		
		if ( isNotVisible(p_pinky) &
				isVisible(new Point[] {p_index, p_thumb})){
			if (isLetterQ(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "Q";}
		}
		
		else if (isNotVisible(p_thumb) &
				isVisible(new Point[] {p_pinky, p_ring, p_middle, p_index})){
			if (isLetterX(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "X";}
		}
		
		else if (isNotVisible(p_index) &
				isVisible(new Point[] {p_pinky, p_ring, p_middle, p_thumb})){
			if (isLetterC(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv, 0))	{ letter = "C";Log.i(TAG, "firstC");}	// duplicate
			else if (isLetterS(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv, 0))	{ letter = "S";}						// duplicate
			else if (isLetterO(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "O";}
		}
		
		else if (isNotVisible(p_pinky) &
				isVisible(new Point[] {p_ring, p_middle, p_index, p_thumb})) {
			if (isLetterP(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "P";}
		}
		
		else if (isVisible(new Point[] {p_pinky, p_ring, p_middle, p_index, p_thumb})) {
		
			if (isLetterA(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "A";}	
			else if (isLetterB(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "B";}	
				
			else if (isLetterC(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv, 1))	{ letter = "C";}
			else if (isLetterS(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv, 1))	{ letter = "S";}
//			else if (isLetterO(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "O";}
//			else if (isLetterP(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "P";}
//			
//			
			else if (isLetterD(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "D";}	
			else if (isLetterE(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "E";}	
//			else if (isLetterF(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "F";}		
//			else if (isLetterG(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "G";}
//			else if (isLetterH(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "H";}	
//			else if (isLetterI(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "I";}	
//			else if (isLetterK(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "K";}
//			else if (isLetterL(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "L";}
//			else if (isLetterM(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "M";}
//			else if (isLetterN(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "N";}
		
			else if (isLetterR(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "R";}
			
			else if (isLetterT(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "T";}
			else if (isLetterU(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "U";}
			else if (isLetterV(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "V";}
			else if (isLetterW(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "W";}
			
			else if (isLetterY(p_pinky, p_ring, p_middle, p_index, p_thumb, mHsv))	{ letter = "Y";}
		}
		
		Core.putText(mYuv, letter, new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
	}
	
	/** mao ni ang check para sa D. ang naa sa sulod sa if-statement kay mao ang ilisun.
	 *  paghimu ug if-statements sa letters nga assigned ninyu. naa nay statements daan
	 *  ang mga letters pero mao na ang daan. dapat di na mugamit ug aning tulo ka functions:
	 *  
	 *     X        isLowDistanceTo
	 *     X        isMidDistanceTo
	 *     X        isHighDistanceTo
	 *     
	 *  tungod kay lahi2x ang pixels sa mga phone. ang mga gamitun nga functions sud sa
	 *  if-statement kay kani, pili lang mu depende sa characteristics sa letter:
	 *  
	 *  1)	isWhiteNorthOf(Point, Mat)
	 *  		mangita ug White pixel at the given Point of a given Mat image pasaka
	 *  
	 *  2)	isWhiteNorthOf(Point[], Mat)
	 *  		same as above pero makaaccept ug array of Point
	 *  
	 *  3)	isWhiteNotNorthOf(Point, Mat)
	 *  		reverse of isWhiteNorthOf(Point, Mat)
	 *  		NOTE: use this instead of !isWhiteNorthOf(Point, Mat)
	 *  
	 *  4)	isWhiteNotNorthOf(Point[], Mat)			if array, always use this
	 *  		same as above pero makaaccept ug array of Point
	 *  		NOTE: use this instead of !isWhiteNorthOf(Point[], Mat)
	 *  
	 *  5)	isWhiteSouthOf(Point, Mat)
	 *  6)	isWhiteSouthOf(Point[], Mat)
	 *  7)	isWhiteNotSouthOf(Point, Mat)
	 *  8)	isWhiteNotSouthOf(Point[], Mat)			if array, always use this
	 *  9)	isVisible(Point)
	 *  		is the Point parameter visible?
	 *  10)	isNotVisible(Point)
	 *  		opposite of isVisible(Point)
	 *  
	 *  11) isEastOf(Point, Point)
	 *  12) isEastOf(Point[], Point)
	 *  13) isEastOf(Point, Point[])
	 *  
	 *  14) isWestOf(Point, Point)
	 *  15) isWestOf(Point[], Point)
	 *  16) isWestOf(Point, Point[])
	 *  
	 *  17) isNorthOf(Point, Point)
	 *  18) isNorthOf(Point[], Point)
	 *  19) isNorthOf(Point, Point[])
	 *  
	 *  20) isSouthOf(Point, Point)
	 *  21) isSouthOf(Point[], Point)
	 *  21) isSouthOf(Point, Point[])
	 *  
	 *  22) isNorthEastOf(Point, Point)
	 *  23) isNorthEastOf(Point[], Point)
	 *  24) isNorthEastOf(Point, Point[])
	 *  
	 *  25) isNorthWestOf(Point, Point)
	 *  26) isNorthWestOf(Point[], Point)
	 *  27) isNorthWestOf(Point, Point[])
	 *   
	 *  28) isSouthEastOf(Point, Point)
	 *  29) isSouthEastOf(Point[], Point)
	 *  30) isSouthEastOf(Point, Point[])
	 *  
	 *  31) isSouthWestOf(Point, Point)
	 *  32) isSouthWestOf(Point[], Point)
	 *  33) isSouthWestOf(Point, Point[])
	 *  
	 *  Do not use
	 *  	isLowDistanceTo
	 *  	isMidDistanceTo
	 *  	isHighDistanceTo
	 * 
	 *  
	 * */
	public static boolean isLetterD(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** ok na ni*/
		if (
				isEastOf(p_ring, p_pinky)
				&&isEastOf(p_middle, p_ring)
				&&isEastOf(p_thumb, p_pinky)
				&&isNorthOf(p_index, new Point[] {p_middle,p_thumb})
				&&isNorthEastOf(p_index, new Point[] {p_pinky, p_ring})
				&&isWhiteNorthOf(new Point[] {p_pinky, p_ring, p_middle, p_thumb}, mHsv)
				&&isWhiteNotNorthOf(p_index, mHsv)
				){
			return true;
		}
		return false;
	}

	public static boolean isLetterE(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** ok na ni*/
		if (isEastOf(p_ring, p_pinky)
				&&isEastOf(p_middle, p_ring)
				&&isEastOf(p_index, p_middle)
				&&isSouthOf(p_thumb, new Point[] {p_pinky, p_ring, p_middle, p_index})
				&&isWestOf(p_thumb,p_index)
				&&isWhiteSouthOf(new Point[] {p_pinky, p_ring, p_middle, p_index}, mHsv)
				&&isWhiteNorthOf(new Point[] {p_pinky, p_ring, p_middle, p_index}, mHsv)){
			return true;
		}
		return false;
	}

	public static boolean isLetterB(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** ok na ni*/
		if (isEastOf(p_ring, p_pinky)
				&&isEastOf(p_middle, p_ring)
				&&isEastOf(p_index, p_middle)
				&&isSouthOf(p_thumb, new Point[] {p_pinky, p_ring, p_middle, p_index})
				&&isWestOf(p_thumb,p_index)
				&&isWhiteSouthOf(new Point[] {p_pinky, p_ring, p_middle, p_index}, mHsv)
				&&isWhiteNotNorthOf(new Point[] {p_pinky, p_ring, p_middle, p_index}, mHsv)
				){
			return true;
		}
		return false;
	}

	public static boolean isLetterA(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** ok na ni*/
		if (isNorthEastOf(p_thumb, p_index)
				&&isEastOf(p_index, p_middle)
				&&isEastOf(p_middle, p_ring)
				&&isEastOf(p_ring, p_pinky)
				&&isWhiteNorthOf(new Point[] {p_pinky,p_ring,p_middle,p_index}, mHsv)
				&&isWhiteNotNorthOf(p_thumb, mHsv)
				){
			return true;
		}
		return false;
	}

	public static boolean isLetterC(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv, int flag){
	/** not working*/
		if (flag == 1){				// not visible index
			
			if (isSouthOf(p_thumb, new Point[] {p_pinky, p_ring, p_middle}) &
					
					isLowDistanceTo(p_pinky, p_ring) &
					isLowDistanceTo(p_ring, p_middle) &
//					!isLowDistanceTo(p_thumb, new Point[] {p_pinky, p_ring, p_thumb}) 
					
					isWhiteNotNorthOf(new Point[] {p_thumb,   p_ring,p_middle}, mHsv) &
					!isWhiteSouthOf(p_thumb, mHsv)
					){
				return true;
			}
		} else {					// visible index
			
			if (isSouthOf(p_thumb, new Point[] {p_pinky, p_ring, p_middle, p_index}) &
					isLowDistanceTo(p_pinky, p_ring) &
					isLowDistanceTo(p_ring, p_middle) &
					isLowDistanceTo(p_ring, p_index) &
//					!isLowDistanceTo(p_thumb, new Point[] {p_pinky, p_ring, p_thumb, p_index})
					isWhiteNotNorthOf(new Point[] {p_thumb,   p_ring,p_middle,p_index}, mHsv) &
					!isWhiteSouthOf(p_thumb, mHsv)
					){
				return true;
			}
			
			
			
		} 
		return false;
	}

	public static boolean isLetterO(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isNotVisible(p_index)){				// not visible index
			
			if (
					isLowDistanceTo(p_pinky, p_ring) &
					isLowDistanceTo(p_ring, p_middle)  &
					isLowDistanceTo(p_thumb, new Point[] {p_pinky, p_ring, p_thumb}) 
					
					){
				return true;
			}
		}
		 else if (isVisible(p_index)){					// visible index
	
			if (
					isLowDistanceTo(p_pinky, p_ring) &
					isLowDistanceTo(p_ring, p_middle) &
					isLowDistanceTo(p_ring, p_index) &
					isLowDistanceTo(p_thumb, new Point[] {p_pinky, p_ring, p_thumb, p_index}) 
					){
				return true;
			}
	
	
	
		}
		return false;
	}
	
	public static boolean isLetterQ(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** ok na ni*/
		if (isNorthEastOf(p_middle, p_index) &&
				isSouthOf(p_thumb, p_middle) &&
				isWestOf(p_index, p_thumb))
			return true;
		return false;
	}


	public static boolean isLetterY(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
//		edited 11/26/12
		if (isNorthWestOf(p_pinky, p_ring) &
				isNorthEastOf(p_thumb, p_index) &
				isEastOf(p_index,p_middle) &
				isEastOf(p_middle, p_ring) &
				isWhiteNorthOf(new Point[] {p_ring, p_middle, p_index}, mHsv) &
				!isWhiteNorthOf(p_pinky, mHsv) &
				!isWhiteNorthOf(p_thumb, mHsv)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterX(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** ok na ni*/
		if (isNorthEastOf(p_index,p_middle) &
				isEastOf(p_middle, p_ring) &
				isEastOf(p_ring, p_pinky)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterW(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		
//		if (isEastOf(p_index, p_middle) &
//				isEastOf(p_middle, p_ring)&
//				
//				isEastOf(p_thumb, p_pinky) &
//				isSouthOf(p_thumb,p_ring) &
//				isSouthOf(p_thumb,p_middle) &
//				isSouthOf(p_thumb,p_index) &
//				
//				isSouthOf(p_pinky,p_ring) &
//				isSouthOf(p_pinky,p_middle) &
//				isSouthOf(p_pinky,p_index) &
//			
//				isLowDistanceTo(p_pinky, p_thumb) ){
//			return true;
//		}
//		edited 11/26/12
		if (isEastOf(p_index, p_middle) &
				isEastOf(p_middle, p_ring) &
				
				isSouthOf(p_pinky, new Point[] {p_ring, p_middle, p_index}) &
			
				isSouthOf(p_thumb, new Point[] {p_ring, p_middle, p_index}) &
				isWhiteNorthOf(new Point[] {p_pinky, p_thumb}, mHsv) &
				!isWhiteNorthOf(p_ring, mHsv) &				// one function call per finger
				!isWhiteNorthOf(p_middle, mHsv) &			// done intentionally
				!isWhiteNorthOf(p_index, mHsv)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterV(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		
//		if (
//				//isNorthWestOf(p_middle, p_thumb) &
//				isNorthEastOf(p_index, p_thumb)&
////				isEastOf(p_thumb, p_ring) &
//				isEastOf(p_ring, p_pinky) &
//				isNorthOf(p_thumb, new Point[] {p_pinky, p_ring})&
//				
//				isNorthOf(p_middle, new Point[] {p_pinky, p_ring, p_thumb})&
//				isNorthOf(p_index, new Point[] {p_pinky, p_ring, p_thumb})&		
////				isLowDistanceTo(p_thumb, p_ring) &
//				!isLowDistanceTo(p_index,p_middle)){
//			return true;
//		}
//		edited 11/30/12
		if (isEastOf(p_ring,p_pinky) &
				isNorthOf(p_thumb, new Point[] {p_pinky, p_ring}) &
				isNorthOf(new Point[] {p_index, p_middle}, p_thumb) &
				isWestOf(p_middle,p_index) &
				isWhiteNorthOf(new Point[] {p_pinky, p_ring, p_thumb}, mHsv) &
				!isWhiteNorthOf(p_middle, mHsv) &
				!isWhiteNorthOf(p_pinky, mHsv)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterU(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		
//		if (isNorthEastOf(p_middle,p_ring) &
//				isNorthEastOf(p_middle,p_pinky) &
//				isEastOf(p_ring, p_pinky) &
//				isSouthOf(p_thumb,p_index) &
//				isEastOf(p_index,p_middle) &
//				isLowDistanceTo(p_index, p_middle) &
//				!isLowDistanceTo(p_index, p_thumb)&
//				isLowDistanceTo(p_thumb, p_ring)){
//			return true;
//		}
//		edited 11/30/12
		if (isEastOf(p_ring,p_pinky) &
				isNorthOf(p_thumb, new Point[] {p_pinky, p_ring}) &
				isNorthOf(new Point[] {p_index, p_middle}, p_thumb) &
				isWestOf(p_middle,p_index) &
				isWhiteNorthOf(new Point[] {p_pinky, p_ring, p_thumb}, mHsv) &
				!isWhiteNorthOf(p_middle, mHsv) &
				!isWhiteNorthOf(p_pinky, mHsv)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterT(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		
//		if (isNorthWestOf(p_thumb,p_index) &
//				isNorthEastOf(p_thumb,p_middle) &
//				
//				isEastOf(p_middle,p_ring) &
//				isEastOf(p_ring,p_pinky) &
//				
//				isLowDistanceTo(p_index, p_thumb) &
//				!isLowDistanceTo(p_middle, p_thumb)){
//			return true;
//		}
//		edited 11/24/12
		if(isNorthWestOf(p_thumb, p_index) &
				isNorthEastOf(p_index, p_middle) &
				isEastOf(p_middle, p_ring) & 
				isEastOf(p_ring, p_pinky) &
				isWhiteNorthOf(new Point[] {p_pinky,p_ring,p_middle,p_index}, mHsv) &
				!isWhiteNorthOf(p_thumb, mHsv))
			return true;
		return false;
	}
	
	public static boolean isLetterS(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv, int flag){
		/** The flag will help the system know which algo to execute:
		 *  0 for when only the pinky, ring, middle, and thumb fingers are visible
		 *  1 for when all are visible
		 */
//		edited 11/24/12
		if(flag == 1){
			if(isNorthEastOf(p_thumb, p_ring) &
					isEastOf(p_index, p_middle) &
					isEastOf(p_middle, p_ring) & 
					isEastOf(p_ring, p_pinky) &
					isSouthOf(new Point[] {p_pinky, p_ring, p_middle, p_index}, p_thumb) &
					isWhiteNorthOf(new Point[] {p_pinky,p_ring,p_middle,p_index, p_thumb}, mHsv))
				return true;
		}
		else{
		    if(isNorthEastOf(p_thumb, p_ring) &
		    		isEastOf(p_middle, p_ring) & 
		    		isEastOf(p_ring, p_pinky) &
		    		isSouthOf(new Point[] {p_pinky, p_ring, p_middle}, p_thumb) &
		    		isWhiteNorthOf(new Point[] {p_pinky,p_ring,p_middle, p_thumb}, mHsv))		    
			return true;
		}
		return false;
	}
	
	public static boolean isLetterR(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		
//		if (isNorthEastOf(p_index,p_ring) &
//				isNorthEastOf(p_index,p_pinky) &
//				isEastOf(p_ring, p_pinky) &
//				isSouthOf(p_thumb,p_index) &
//				isEastOf(p_middle,p_index) &
//				isLowDistanceTo(p_index, p_middle) &
//				!isLowDistanceTo(p_index, p_thumb)){
//			return true;
//		}
//		edited 11/24/12
		if (isEastOf(p_ring,p_pinky) &
				isNorthOf(p_thumb, new Point[] {p_pinky, p_ring}) &
				isNorthOf(new Point[] {p_index, p_middle}, p_thumb) &
				isEastOf(p_middle,p_index)){
			return true;
		}

		return false;
	}
	
	public static boolean isLetterP(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		
		if (isSouthEastOf(p_middle, p_index) &
				isSouthEastOf(p_thumb, p_index) &
				isSouthOf(p_middle, p_thumb) &
				!isLowDistanceTo(p_index, p_middle) &
				!isLowDistanceTo(p_thumb, p_middle)){
			return true;
		}
		return false;
	}
	
	// mu work ra man pero dapat di mu gamit ug
	//		isLowDistanceTo(Point,Point)
	//		isMidDistanceTo(Point,Point)
	//		isHighDistanceTo(Point,Point)
	// since screen size dependent ni sila
	// instead use these
	//		isWhiteNorthOf(Point, Mat)
	//		isWhiteNotNorthOf(Point, Mat)
	//		isWhiteSouthOf(Point, Mat)
	//		isWhiteNotSouthOf(Point, Mat)
	public static boolean isLetterN(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isEastOf(p_index,p_middle) &
				isEastOf(p_middle,p_ring) &
				isEastOf(p_ring,p_pinky) &
				isLowDistanceTo(p_index, p_middle) &
				isLowDistanceTo(p_middle,p_ring)&
				isLowDistanceTo(p_ring,p_pinky)&
				!isNorthOf(p_ring,p_middle)){
			return true;
		}
		return false;
	}

	// mu work ra man pero dapat di mu gamit ug
	//		isLowDistanceTo(Point,Point)
	//		isMidDistanceTo(Point,Point)
	//		isHighDistanceTo(Point,Point)
	// since screen size dependent ni sila
	// instead use these
	//		isWhiteNorthOf(Point, Mat)
	//		isWhiteNotNorthOf(Point, Mat)
	//		isWhiteSouthOf(Point, Mat)
	//		isWhiteNotSouthOf(Point, Mat)

	public static boolean isLetterM(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isEastOf(p_index,p_middle) &
				isEastOf(p_middle,p_ring) &
				isEastOf(p_ring,p_pinky) &
				isLowDistanceTo(p_index, p_middle) &
				isLowDistanceTo(p_middle,p_ring)&
				isLowDistanceTo(p_ring,p_pinky)&
				isNorthOf(p_ring,p_middle)){
			return true;
		}
		return false;
	}
	
	// mu work ra man pero dapat di mu gamit ug
	//		isLowDistanceTo(Point,Point)
	//		isMidDistanceTo(Point,Point)
	//		isHighDistanceTo(Point,Point)
	// since screen size dependent ni sila
	// instead use these
	//		isWhiteNorthOf(Point, Mat)
	//		isWhiteNotNorthOf(Point, Mat)
	//		isWhiteSouthOf(Point, Mat)
	//		isWhiteNotSouthOf(Point, Mat)
	public static boolean isLetterL(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isNorthEastOf(p_index, p_pinky) &
				isNorthEastOf(p_index, p_ring) &
				isNorthEastOf(p_index, p_middle) &
				isNorthOf(p_index, p_thumb) &
				!isLowDistanceTo(p_thumb, p_middle) &
				isWestOf(p_index,p_thumb) &
				!isLowDistanceTo(p_middle,p_index)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterK(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isNorthWestOf(p_middle, p_thumb) &
				isNorthEastOf(p_index, p_thumb)&
				isSouthWestOf(p_ring, p_thumb) &
				isSouthWestOf(p_pinky, p_thumb) &
				!isLowDistanceTo(p_thumb, p_ring)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterI(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isNorthWestOf(p_pinky, p_ring) &
				isEastOf(p_thumb, p_index)&
				isEastOf(p_index, p_middle) &
				isEastOf(p_middle, p_ring) & 
				
				!isLowDistanceTo(p_pinky, p_ring)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterH(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isSouthEastOf(p_thumb, p_middle) &
				isSouthEastOf(p_thumb, p_index) &
				isSouthEastOf(p_ring, p_middle) &
				isSouthEastOf(p_ring, p_index) &
				isSouthEastOf(p_pinky, p_middle) &
				isSouthEastOf(p_pinky, p_index) &
				isLowDistanceTo(p_index, p_middle) &
				!isLowDistanceTo(p_index, p_thumb)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterG(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isSouthEastOf(p_middle, p_index) &
				isSouthEastOf(p_thumb, p_index) &
				!isLowDistanceTo(p_index, p_middle)){
			return true;
		}
		return false;
	}
	
	public static boolean isLetterF(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mHsv){
		/** not working*/
		if (isSouthEastOf(p_index,p_pinky) &
				isSouthEastOf(p_index,p_ring) &
				isSouthEastOf(p_index,p_middle) &
				isSouthEastOf(p_thumb,p_pinky) &
				
				isLowDistanceTo(p_index,p_thumb)){
			return true;
		}
		return false;
	}	
	
	/** check the HSV value of the color at Point (0,0) */
	public static void checkHsvValue(Point p, Mat mHsv){
		double[] x = mHsv.get(0, 0);
		Log.i(TAG, "a=" + x[0] + " b=" + x[1]+ " c=" + x[2]);
	}
	
	public static void getWhiteMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(0, 0, 180), new Scalar(255, 20, 255), dst);
	}

	/** starting from Point p, is there any white pixel in the North? */
	public static boolean isWhiteNorthOf(Point p, Mat mHsv){
			int py = (int) p.y;
			int px = (int) p.x;
		
	//		double[] x = hsv.get( py, px);
	//		double[] x = hsv.get( 0, 0);
	//		Log.i(TAG, "p.x=" + px + " p.y=" + py+ " a=" + x[0] + " b=" + x[1]+ " c=" + x[2]);
	//		Core.inRange(src, new Scalar(0, 0, 220), new Scalar(255, 15, 255), dst);
			while (py != 0){
				double[] x = mHsv.get( py, px);
				//						15			220				255
				if (x[1] >= 0 & x[1] <= 20 & x[2] >= 180 & x[2] <= 255){
					return true;
				}
				py--;
			}
			return false;
		}

	/** from an array of Points p, are there any white pixels in the North? */
	public static boolean isWhiteNorthOf(Point[] p, Mat mHsv){
		for (int i = 0; i < p.length; i++){
			if (!isWhiteNorthOf(p[i], mHsv)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	

	public static boolean isWhiteNotNorthOf(Point p, Mat mHsv){
		int py = (int) p.y;
		int px = (int) p.x;

		while (py != 0){
			double[] x = mHsv.get( py, px);
			//						15			220				255
			if (x[1] >= 0 & x[1] <= 20 & x[2] >= 180 & x[2] <= 255){
				return false;
			}
			py--;
		}
		return true;
	}
	
	public static boolean isWhiteNotNorthOf(Point[] p, Mat mHsv){
		for (int i = 0; i < p.length; i++){
			if (!isWhiteNotNorthOf(p[i], mHsv)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	/** starting from Point p, is there any white pixel in the South? */
	public static boolean isWhiteSouthOf(Point p, Mat mHsv){
		int py = (int) p.y;
		int px = (int) p.x;
	
		while (py != mHsv.height()){
			double[] x = mHsv.get( py, px);
			//						15			220				255
			if (x[1] >= 0 & x[1] <= 20 & x[2] >= 180 & x[2] <= 255){
				return true;
			}
			py++;
		}
		return false;
	}

	/** from an array of Points p, are there any white pixels in the South? */
	public static boolean isWhiteSouthOf(Point[] p, Mat mHsv){
		for (int i = 0; i < p.length; i++){
			if (!isWhiteSouthOf(p[i], mHsv)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	public static boolean isWhiteNotSouthOf(Point p, Mat mHsv){
		int py = (int) p.y;
		int px = (int) p.x;
	
		while (py != mHsv.height()){
			double[] x = mHsv.get( py, px);
			//						15			220				255
			if (x[1] >= 0 & x[1] <= 20 & x[2] >= 180 & x[2] <= 255){
				return false;
			}
			py++;
		}
		return true;
	}
	
	public static boolean isWhiteNotSouthOf(Point[] p, Mat mHsv){
		for (int i = 0; i < p.length; i++){
			if (!isWhiteNotSouthOf(p[i], mHsv)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}

	/** is the Point p visible in the screen? --- parameter p is the point of the color */
	public static boolean isVisible(Point p){
		if (p.x == 0 & p.y ==0){
			return false;
		}
		return true;
	}

	/** are all the points p visible in the screen? */
	public static boolean isVisible(Point[] p){
		for (int i = 0; i < p.length; i++){
			if (!isVisible(p[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}

	/** if wala ba ang point */
	public static boolean isNotVisible(Point p){
		if (p.x == 0 & p.y ==0){
			return true;
		}
		return false;
	}
	
	/** are the array of Point p not visible in the screen?*/
	public static boolean isNotVisible(Point[] p){
		for (int i = 0; i < p.length; i++){
			if (isVisible(p[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	

	/** "of" is reference point. is "is" East of "of"? */	
	public static boolean isEastOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
	
		if (is.x > of.x){
			return true;
		}
		return false;
	}

	/** are the array of Point "is" EAST of Point "of" */
	public static boolean isEastOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isEastOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	/** is Point "is" EAST of the array of Point "of" */
	public static boolean isEastOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isEastOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}

	/** "of" is reference point. is "is" West of "of"? */	
	public static boolean isWestOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
	
		if (is.x < of.x){
			return true;
		}
		return false;
	}
	
	/** are the array of Point "is" WEST of Point "of" */
	public static boolean isWestOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isWestOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	/** is Point "is" WEST of the array of Point "of" */
	public static boolean isWestOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isWestOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}

	/** "of" is reference point. is "is" North of "of"? */	
	public static boolean isNorthOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
	
	
		if (is.y < of.y){
			return true;
		}
		return false;
	}
	
	/** are the array of Point "is" NORTH of Point "of" */
	public static boolean isNorthOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isNorthOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	/** is Point "is" NORTH of the array of Point "of" */
	public static boolean isNorthOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isNorthOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}

	/** "of" is reference point. is "is" South of "of"? */	
	public static boolean isSouthOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
	
	
		if (is.y > of.y){
			return true;
		}
		return false;
	}
	
	/** are the array of Point "is" SOUTH of Point "of" */
	public static boolean isSouthOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isSouthOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	/** is Point "is" SOUTH of the array of Point "of" */
	public static boolean isSouthOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isSouthOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}

	public static boolean isNorthEastOf(Point is, Point of){return isNorthOf(is,of) & isEastOf(is,of);}
	public static boolean isNorthEastOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isNorthEastOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	public static boolean isNorthEastOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isNorthEastOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean isNorthWestOf(Point is, Point of){return isNorthOf(is,of) & isWestOf(is,of);}
	public static boolean isNorthWestOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isNorthWestOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	public static boolean isNorthWestOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isNorthWestOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean isSouthEastOf(Point is, Point of){return isSouthOf(is,of) & isEastOf(is,of);}
	public static boolean isSouthEastOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isSouthEastOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	public static boolean isSouthEastOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isSouthEastOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean isSouthWestOf(Point is, Point of){return isSouthOf(is,of) & isWestOf(is,of);}
	public static boolean isSouthWestOf(Point[] is, Point of){
		for (int i = 0; i < is.length; i++){
			if (!isSouthWestOf(is[i],of)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	public static boolean isSouthWestOf(Point is, Point[] of){
		for (int i = 0; i < of.length; i++){
			if (!isSouthWestOf(is,of[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
	}
	
	
	/** try not to use this anymore, use the isWhiteNorthOf() or isWhiteSouthOf() function */
	public static boolean isLowDistanceTo(Point is, Point to){
		double temp = Math.hypot( (is.x - to.x) , (is.y - to.y));
		if (temp < lowThresh)
			return true;
		return false;
	}
	
	public static boolean isLowDistanceTo(Point is, Point[] to){
		for (int i = 0; i < to.length; i++){
			if (!isLowDistanceTo(is,to[i])){	//	if one is false, returns false
				return false;
			}
		}
		return true;
		
	}
	
	public static boolean isLowDistanceTo(Point[] is, Point to){
		for (int i = 0; i < is.length; i++){
			if (!isLowDistanceTo(is[i],to)){	//	if one is false, returns false
				return false;
			}
		}
		return true;
		
	}

	/** try not to use this anymore, use the isWhiteNorthOf() or isWhiteSouthOf() function */
	public static boolean isMidDistanceTo(Point is, Point to){
		double temp = Math.hypot( (is.x - to.x) , (is.y - to.y));
		if (temp >= lowThresh & temp <= highThresh)
			return true;
		return false;
	}
	
	/** try not to use this anymore, use the isWhiteNorthOf() or isWhiteSouthOf() function */
	public static boolean isHighDistanceTo(Point is, Point to){
		if ( (Math.abs(is.x - to.x) > highThresh) & (Math.abs(is.y - to.y) > highThresh) )
			return true;
		return false;
	}


	public static void detectSinglePoint(Mat src, Point center){
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
		Mat hierarchy = new Mat();
		Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		int k = getBiggestContourIndex(contours);
		Rect boundRect = setContourRect(contours, k);
		getCenterPoint(boundRect.tl(), boundRect.br(), center);
	}
	
	/** convert YUV image to RGB then HSV colorspace */
	public static void cvt_YUVtoRGBtoHSV(Mat src, Mat dst){
		mSrc = new Mat();
		src.copyTo(mSrc); 
		Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//		Imgproc.blur(dst, dst, new Size(8,8));
		Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
	}

	/** detect all the biggest blob of each color */
	public static void detectAllColorBlobs(Mat mYuv, Mat mHsv, Mat dst) {
		Mat mColor = new Mat();
		Mat mResult = new Mat();

		getBlueMat(mHsv,mColor);
		detectSingleBlob(mYuv, mColor, "B", mResult);

		getYellowMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "Y", mResult);

		getRedMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "R", mResult);

		getGreenMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "G", mResult);

		getCyanMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "C", mResult);

		getVioletMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "V", dst);		
	}
	
	public static Rect setContourRect(List<MatOfPoint> contours,int k){
		Rect boundRect = new Rect();
		Iterator<MatOfPoint> each = contours.iterator();
		int j = 0;
		while (each.hasNext()){
			MatOfPoint wrapper = each.next();
			if (j==k){
				return Imgproc.boundingRect( wrapper );
			}
			j++;
		}
		return boundRect;
	}
	
	public static Rect[] setContourRect(List<MatOfPoint> contours){
		Rect[] boundRect = new Rect[contours.size()];
		
		for (int i = 0; i < contours.size(); i++){
			boundRect[i] = Imgproc.boundingRect(contours.get(i));
		}
		
//		Iterator<MatOfPoint> each = contours.iterator();
//		int j = 0;
//		while (each.hasNext()){
//			MatOfPoint wrapper = each.next();
//				return Imgproc.boundingRect( wrapper );
//			j++;
//		}
		return boundRect;
	}

	public static void detectAllBlob(Mat src, Mat image, String text, Mat dst){
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
		Mat hierarchy = new Mat();
		src.copyTo(dst);

		Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		Rect[] boundRect = setContourRect(contours);

		for (int i = 0; i < contours.size(); i++){
			Core.rectangle(dst, boundRect[i].tl(), boundRect[i].br(), new Scalar(255, 255, 0), 2, 8, 0 );
			Core.putText(dst, text, boundRect[i].tl(), 0/*font*/, 1, new Scalar(255, 255, 255, 255), 3);
		}
	}
	

	public static void detectSingleBlob(Mat src, Mat image, String text, Mat dst){
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
		Mat hierarchy = new Mat();
		//		Mat tempMat = new Mat();
		src.copyTo(dst);

		Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		int k = getBiggestContourIndex(contours);
		Rect boundRect = setContourRect(contours, k);

//		Point center = new Point();
//		getCenterPoint(boundRect.tl(), boundRect.br(), center);
		Core.rectangle(dst, boundRect.tl(), boundRect.br(), new Scalar(255, 255, 0), 2, 8, 0 );
		//        Core.putText(dst, center.x+"|"+center.y, new Point(10, 200), 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);

		Core.putText(dst, text, boundRect.tl(), 0/*font*/, 1, new Scalar(255, 255, 255, 255), 3);
		//        Log.i(TAG, "x="+boundRect.tl().x+" y="+boundRect.tl().y);

	}

	public static void getCenterPoint(Point tl, Point br, Point dst){
		dst.x = (tl.x + br.x)/2;
		dst.y = (tl.y + br.y)/2;
	}

	public static int getBiggestContourIndex(List<MatOfPoint> contours){
		double maxArea = 0;
		Iterator<MatOfPoint> each = contours.iterator();
		int j = 0;
		int k = -1;
		while (each.hasNext())
		{
			MatOfPoint wrapper = each.next();
			double area = Imgproc.contourArea(wrapper);
			if (area > maxArea){
				maxArea = area;
				k = j;
			}
			j++;
		}
		//        Log.i(TAG, "k="+k+" area="+maxArea);
		return k;
	}

	/** not used at the moment */
	public boolean isVertical(Rect wrist){
		if(wrist.height>wrist.width) return true;
		return false;
	}
	
	
	

	public static void getOrangeMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(9, 70, 70), new Scalar(18, 255, 255), dst);
	}
	
	
	
	public static void getPinkMat(Mat src, Mat dst){
			Core.inRange(src, new Scalar(148, 50, 80), new Scalar(163, 255, 255), dst);
		}

	//ok! -mini2
	public static void getRedMat(Mat src, Mat dst){
		Mat c1 = new Mat();
		Mat c2 = new Mat();
		Core.inRange(src, new Scalar(0, 180, 70), new Scalar(6, 255, 255), c1);		// 11/10/12
		Core.inRange(src, new Scalar(175, 180, 70), new Scalar(178, 255, 255), c2);	// 11/10/12
		Core.bitwise_or(c1,c2,dst);
	}
	
	public static void getVioletMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(130, 100, 40), new Scalar(143, 255, 255), dst);		// 11/08/12
	}

	public static void getCyanMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(78, 70, 70), new Scalar(91, 255, 225), dst);		//	11/09/X2 dean
	}

	public static void getGreenMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(40, 90, 70), new Scalar(91, 255, 255), dst);
	}

	public static void getBlueMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(100, 80, 60), new Scalar(125, 255, 255), dst); //	11/08/X2 dean
	}

	public static void getYellowMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(20, 140, 80), new Scalar(33, 255, 255), dst);		//	11/09/X2 dean
		// 							 20  160  80??
	}	
	
	public static void XtestIdentifyHandGesture(Point p_pinky, Point p_ring,
			Point p_middle, Point p_index, Point p_thumb, Mat mYuv) {

		if ( (isNotVisible(p_ring)&(isNotVisible(p_pinky))) &
				isNorthEastOf(p_middle, p_index) &
				isSouthOf(p_thumb, p_middle) &
				isWestOf(p_index, p_thumb)
		){
			Core.putText(mYuv, "Q", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
		}
		
		else if ( isNotVisible(p_thumb)){
			if (isNorthEastOf(p_index,p_middle) &
					isEastOf(p_middle, p_ring) &
					isEastOf(p_ring, p_pinky)
					){
				Core.putText(mYuv, "X", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
		}
		
		else if ( isNotVisible(p_index) ){
			if (isLowDistanceTo(p_thumb,p_ring)){
				Core.putText(mYuv, "O", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			} else if (isMidDistanceTo(p_thumb,p_ring)){
				Core.putText(mYuv, "C", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
		}
		
		else if (isSouthEastOf(p_middle, p_index) &
				isSouthEastOf(p_thumb, p_index) &
				isSouthOf(p_middle, p_thumb) &
				!isLowDistanceTo(p_index, p_middle) &
				!isLowDistanceTo(p_thumb, p_middle)){
			Core.putText(mYuv, "P", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
		}

		else if (isSouthEastOf(p_middle, p_index) &
				isSouthEastOf(p_thumb, p_index) &
				!isLowDistanceTo(p_index, p_middle)){
			Core.putText(mYuv, "G", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
		}
		
		else if (isSouthEastOf(p_thumb, p_middle) &
				isSouthEastOf(p_thumb, p_index) &
				isSouthEastOf(p_ring, p_middle) &
				isSouthEastOf(p_ring, p_index) &
				isSouthEastOf(p_pinky, p_middle) &
				isSouthEastOf(p_pinky, p_index) &
				isLowDistanceTo(p_index, p_middle) &
				!isLowDistanceTo(p_index, p_thumb)){
			Core.putText(mYuv, "H", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
		}
		
		else if (isNorthEastOf(p_index,p_ring) &
				isNorthEastOf(p_index,p_pinky) &
				isEastOf(p_ring, p_pinky) &
				isSouthOf(p_thumb,p_index) &
				isEastOf(p_middle,p_index) &
				isLowDistanceTo(p_index, p_middle) &
				!isLowDistanceTo(p_index, p_thumb)){
			Core.putText(mYuv, "R", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
		}
		
		else if (isNorthEastOf(p_middle,p_ring) &
				isNorthEastOf(p_middle,p_pinky) &
				isEastOf(p_ring, p_pinky) &
				isSouthOf(p_thumb,p_index) &
				isEastOf(p_index,p_middle) &
				isLowDistanceTo(p_index, p_middle) &
				!isLowDistanceTo(p_index, p_thumb)&
				isLowDistanceTo(p_thumb, p_ring)){
			Core.putText(mYuv, "U", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
		}

		else if (isVisible(p_pinky) & isVisible(p_ring) & isVisible(p_middle) & isVisible(p_index) & isVisible(p_thumb)){
			if (isNorthWestOf(p_pinky, p_ring) &
				isNorthEastOf(p_thumb, p_index) &
				isEastOf(p_index,p_middle) &
				isEastOf(p_middle, p_ring) &
				!isLowDistanceTo(p_pinky, p_ring)){
				Core.putText(mYuv, "Y", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			else if (isNorthEastOf(p_thumb, p_index) &
					isEastOf(p_index, p_middle) &
					isEastOf(p_middle, p_ring) & 
					isEastOf(p_ring, p_pinky)&
					!isHighDistanceTo(p_thumb, p_index)){
				Core.putText(mYuv, "A", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			
			else if (isNorthWestOf(p_pinky, p_ring) &
					isEastOf(p_thumb, p_index)&
					isEastOf(p_index, p_middle) &
					isEastOf(p_middle, p_ring) & 
					
					!isLowDistanceTo(p_pinky, p_ring)){
				Core.putText(mYuv, "I", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			
			else if (isNorthWestOf(p_middle, p_thumb) &
					isNorthEastOf(p_index, p_thumb)&
					isSouthWestOf(p_ring, p_thumb) &
					isSouthWestOf(p_pinky, p_thumb) &
					!isLowDistanceTo(p_thumb, p_ring)){
				Core.putText(mYuv, "K", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			else if (isNorthWestOf(p_middle, p_thumb) &
					isNorthEastOf(p_index, p_thumb)&
					isEastOf(p_thumb, p_ring) &
					isEastOf(p_ring, p_pinky) &
					isLowDistanceTo(p_thumb, p_ring) &
					!isLowDistanceTo(p_middle,p_ring)){
				Core.putText(mYuv, "V", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			
			else if (isEastOf(p_index, p_middle) &
					isEastOf(p_middle, p_ring)&
					
					isEastOf(p_thumb, p_pinky) &
					isSouthOf(p_thumb,p_ring) &
					isSouthOf(p_thumb,p_middle) &
					isSouthOf(p_thumb,p_index) &
					
					isSouthOf(p_pinky,p_ring) &
					isSouthOf(p_pinky,p_middle) &
					isSouthOf(p_pinky,p_index) &
				
					isLowDistanceTo(p_pinky, p_thumb) 
//					isHighDistanceTo(p_yellow, p_green) &
//					!isHighDistanceTo(p_yellow,p_cyan)
					){
				Core.putText(mYuv, "W", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			else if (isNorthWestOf(p_thumb,p_index) &
					isNorthEastOf(p_thumb,p_middle) &
					
					isEastOf(p_middle,p_ring) &
					isEastOf(p_ring,p_pinky) &
					
					isLowDistanceTo(p_index, p_thumb) &
					!isLowDistanceTo(p_middle, p_thumb)){
				Core.putText(mYuv, "T", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			
			else if (isNorthEastOf(p_index, p_pinky) &
					isNorthEastOf(p_index, p_ring) &
					isNorthEastOf(p_index, p_middle) &
					isNorthOf(p_index, p_thumb)
				
					){
				
				if (isLowDistanceTo(p_thumb, p_middle)){
					Core.putText(mYuv, "D", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
				} else if (!isLowDistanceTo(p_thumb, p_middle) & isWestOf(p_index,p_thumb) & !isLowDistanceTo(p_middle,p_index)){
					Core.putText(mYuv, "L", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
				}
			}
			
			else if (isSouthOf(p_thumb,p_pinky) &
					isSouthOf(p_thumb,p_ring) &
					isSouthOf(p_thumb,p_middle) &
					isSouthOf(p_thumb,p_index) &
					isEastOf(p_thumb,p_pinky) &
					isWestOf(p_thumb,p_index) &
					!isLowDistanceTo(p_pinky,p_ring)
					
					){
				Core.putText(mYuv, "B", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			else if (isSouthOf(p_thumb,p_pinky) &
					isSouthOf(p_thumb,p_ring) &
					isSouthOf(p_thumb,p_middle) &
					isSouthWestOf(p_thumb,p_index) &
					isEastOf(p_thumb,p_pinky) &
					isLowDistanceTo(p_pinky,p_ring)
					
					){
				Core.putText(mYuv, "E", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			else if (isSouthEastOf(p_index,p_pinky) &
					isSouthEastOf(p_index,p_ring) &
					isSouthEastOf(p_index,p_middle) &
					isSouthEastOf(p_thumb,p_pinky) &
					
					isLowDistanceTo(p_index,p_thumb)
					
					){
				Core.putText(mYuv, "F", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
			}
			
			
//			else if (isEastOf(p_green,p_yellow) &
//					isEastOf(p_yellow,p_red) &
//					isEastOf(p_red,p_cyan) &
//					isLowDistanceTo(p_green, p_yellow) &
//					isLowDistanceTo(p_red, p_yellow)){
//				Core.putText(mYuv, "S", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//			}
			
			
			else if (isEastOf(p_index,p_middle) &
					isEastOf(p_middle,p_ring) &
					isEastOf(p_ring,p_pinky) &
					isLowDistanceTo(p_index, p_middle) &
					isLowDistanceTo(p_middle,p_ring)&
					isLowDistanceTo(p_ring,p_pinky)
//					&
//					!isVisible(p_yellow)
					){
				
				if (isNorthOf(p_ring,p_middle)){
					Core.putText(mYuv, "M", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
				}else {
					Core.putText(mYuv, "N", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
				}
			}
			
		}
	}
	
	
}
	
//public static void XgetVioletMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(130, 40, 40), new Scalar(150, 255, 255), dst);	//131-170-108---142-255-255
//}
//
//public static void XgetCyanMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(85, 131, 125), new Scalar(98, 255, 255), dst);
//}
//
//public static void XgetGreenMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(50, 145, 90), new Scalar(75, 255, 255), dst);	//49-109-61---70-255-255
//}
//
//public static void XgetBlueMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(100, 100, 100), new Scalar(120, 255, 255), dst);
//}
//
//
//public static void XgetYellowMat(Mat src, Mat dst){
//	mSrc = new Mat();
//	src.copyTo(mSrc);
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(20, 100, 100), new Scalar(30, 255, 255), dst);
//}
//
//public static void getRedMat_YCRCB(Mat src, Mat dst){
//	Mat bgr = new Mat();
//	Mat ycrcb = new Mat();
//	
//	Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
//	Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
//	Core.inRange(ycrcb, new Scalar(0, 230, 0), new Scalar(255, 255, 120), dst);
//}
//
//public static void getGreenMat_YCRCB(Mat src, Mat dst){
//	Mat bgr = new Mat();
//	Mat ycrcb = new Mat();
//	
//	Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
//	Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
//	Core.inRange(ycrcb, new Scalar(0, 0, 0), new Scalar(255, 120, 120), dst);
//}
//
//
//public static void XgetRedMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Mat c1 = new Mat();
//	Mat c2 = new Mat();
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(0, 100, 100), new Scalar(10, 255, 255), c1); //0-130-179---6-255-255
//	Core.inRange(dst, new Scalar(170, 100, 100), new Scalar(180, 255, 255), c2); //177-200-120---183-255-255
//	Core.bitwise_or(c1,c2,dst);
//}
//
//public static void XgetVioletMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(130, 40, 40), new Scalar(150, 255, 255), dst);	//131-170-108---142-255-255
//}
//
//public static void XgetCyanMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(85, 131, 125), new Scalar(98, 255, 255), dst);
//}
//
//public static void XgetGreenMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(50, 145, 90), new Scalar(75, 255, 255), dst);	//49-109-61---70-255-255
//}
//
//public static void XgetBlueMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(100, 100, 100), new Scalar(120, 255, 255), dst);
//}
//
//
//public static void XgetYellowMat(Mat src, Mat dst){
//	mSrc = new Mat();
//	src.copyTo(mSrc);
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(20, 100, 100), new Scalar(30, 255, 255), dst);
//}
//
//public static void getRedMat_YCRCB(Mat src, Mat dst){
//	Mat bgr = new Mat();
//	Mat ycrcb = new Mat();
//	
//	Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
//	Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
//	Core.inRange(ycrcb, new Scalar(0, 230, 0), new Scalar(255, 255, 120), dst);
//}
//
//public static void getGreenMat_YCRCB(Mat src, Mat dst){
//	Mat bgr = new Mat();
//	Mat ycrcb = new Mat();
//	
//	Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
//	Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
//	Core.inRange(ycrcb, new Scalar(0, 0, 0), new Scalar(255, 120, 120), dst);
//}
//
//
//public static void XgetRedMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Mat c1 = new Mat();
//	Mat c2 = new Mat();
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(0, 100, 100), new Scalar(10, 255, 255), c1); //0-130-179---6-255-255
//	Core.inRange(dst, new Scalar(170, 100, 100), new Scalar(180, 255, 255), c2); //177-200-120---183-255-255
//	Core.bitwise_or(c1,c2,dst);
//}
//	public static Mat getRedMat(Mat m){
//		Mat mRgb = new Mat();
//		Mat hsv = new Mat();
//		Mat c1 = new Mat();
//		Mat c2 = new Mat();
//		Mat colorFiltered = new Mat();
//		
//		Imgproc.cvtColor(m,mRgb,Imgproc.COLOR_YUV420sp2RGB);
//		Imgproc.cvtColor(mRgb, hsv, Imgproc.COLOR_RGB2HSV);
//		
//		Core.inRange(hsv, new Scalar(0, 175, 179), new Scalar(4, 255, 255), c1);
//		Core.inRange(hsv, new Scalar(177, 216, 165), new Scalar(181, 255, 255), c2);
//		Core.add(c1,c2,colorFiltered);
//		return colorFiltered;
//	}

//	MatOfPoint2f getCentroidPoint(Mat contours) {
//	Moments mu;
//	mu = Imgproc.moments(contours,false);
//	
//	MatOfPoint2f mc;
//	mc = MatOfPoint2f(mu.get_m10() / mu.get_m00(), mu.get_m01() / mu.get_m00());
//	return mc;
//}

//int getBiggestContour(List<List<Point> > contours) {
//	int indexOfBiggestContour = -1;
//	int sizeOfBiggestContour = 0;
//	for (int i = 0; i < contours.size(); i++) {
//		if (contours.get(i).size() > sizeOfBiggestContour) {
//			sizeOfBiggestContour = contours.get(i).size();
//			indexOfBiggestContour = i;
//		}
//	}
//	return indexOfBiggestContour;
//}


/*

	public static Mat detectSingleBlob(Mat src, Mat image) //, Mat dst)
	{


//		//Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method, Point offset
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
		Mat hierarchy = new Mat();
		Mat tempMat = new Mat();
		image.copyTo(tempMat);

		Imgproc.findContours(tempMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int k = getBiggestContourIndex(contours);

//        Rect boundRect = new Rect();
        Rect boundRect = setContourRect(contours, k);
//        setContourRect(boundRect,contours,k);


//        Iterator<MatOfPoint> each = contours.iterator();
//        int j = 0;
//        while (each.hasNext())
//        {
//        	MatOfPoint wrapper = each.next();
//
//        	if (j==k){
//        		 
//        		 boundRect = Imgproc.boundingRect( wrapper );
////        		 MatOfPoint2f  y = new MatOfPoint2f( wrapper.toArray() );
////        		 Imgproc.minEnclosingCircle(y, center, radius);
//        	}
//        	j++;
//        }

        Point center = new Point();
        getCenterPoint(boundRect.tl(), boundRect.br(), center);

//        Core.circle(src, center, 1, new Scalar(255, 255, 255), -1, 8, 0 );
        Core.rectangle(src, boundRect.tl(), boundRect.br(), new Scalar(255, 255, 0), 2, 8, 0 );
        Core.putText(src, center.x+"|"+center.y, new Point(10, 200), 4, 1, new Scalar(255, 0, 0, 255), 3);



        Core.putText(src, "Y", boundRect.tl(), 0, 1, new Scalar(255, 0, 0, 255), 3);
        Log.i(TAG, "x="+boundRect.tl().x+" y="+boundRect.tl().y);
//		Mat temp = new Mat();
//		image.copyTo(temp);
//		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );
//
//		Rect boundRect = new Rect();//( contours.size() );
//		Point center = new Point();
//		float[] radius = new float[1];
//
//		int i = 0;//getBiggestContour(contours);
//		boundRect = Imgproc.boundingRect( contours.get(i) );
//		Core.rectangle(orig, boundRect.tl(), boundRect.br(), new Scalar(255, 255, 255), 2, 8, 0 );
//		
//		MatOfPoint2f x = new MatOfPoint2f();
//		contours.get(i).convertTo(x,CvType.CV_32FC2);
//		Imgproc.minEnclosingCircle( x , center, radius );
//		Core.circle(orig, center, 1, new Scalar(255, 255, 255), -1, 8, 0 );
//
//		System.out.println((i+1) + ": (" + center.x + ", " + center.y + ")");

		return src;
	}



		public static Rect setContourRect(List<MatOfPoint> contours,int k){
		Rect boundRect = new Rect();
		Iterator<MatOfPoint> each = contours.iterator();
        int j = 0;
        while (each.hasNext()){
        	MatOfPoint wrapper = each.next();

        	if (j==k){

//        		 boundRect = Imgproc.boundingRect( wrapper );
        		return Imgproc.boundingRect( wrapper );
//        		 MatOfPoint2f  y = new MatOfPoint2f( wrapper.toArray() );
//        		 Imgproc.minEnclosingCircle(y, center, radius);
        	}
        	j++;
        }
        return boundRect;
	}

		public static void getYellowMat(Mat src, Mat dst){
		mSrc = new Mat();
		src.copyTo(mSrc);

//		Mat mMattemp = new Mat();
		Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(20, 100, 100), new Scalar(30, 255, 255), dst);
//		Mat mMattemp_rgb = new Mat();
//		Mat mMattemp_hsv = new Mat();
//		Mat mMattemp_ir = new Mat();
//		Imgproc.cvtColor(m,mMattemp_rgb,Imgproc.COLOR_YUV420sp2RGB);
//    	Imgproc.cvtColor(mMattemp_rgb,mMattemp_hsv, Imgproc.COLOR_RGB2HSV);
//    	Core.inRange(mMattemp_hsv, new Scalar(20, 100, 100), new Scalar(30, 255, 255), mMattemp_ir);
//		return mMattemp_ir;
	}
*/
