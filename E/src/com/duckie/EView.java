package com.duckie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.neuroph.contrib.imgrec.ImageRecognitionPlugin;
import org.neuroph.contrib.imgrec.image.Image;
import org.neuroph.contrib.imgrec.image.ImageFactory;
import org.neuroph.core.NeuralNetwork;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;



class EView extends EViewBase {

	private static final String TAG = "YYY";
	private final int LOADING_DATA_DIALOG = 2;			// added
	private final int RECOGNIZING_IMAGE_DIALOG = 3;


	private Bitmap bitmap;
	private Image image;


	private NeuralNetwork nnet;
	//	private NeuralNetwork nnet1;
	private ImageRecognitionPlugin imageRecognition;
	//	private ImageRecognitionPlugin imageRecognition1;

	public static final String APP_PATH_SD = "/duckie";
	public static final String IMAGE_NAME = "A.jpg";


	public static final int     VIEW_MODE_RGBA  = 0;
	public static final int     VIEW_MODE_GRAY  = 1;
	public static final int     VIEW_MODE_CANNY = 2;

	private Mat mYuv;
	private Mat mRgba;
	private Mat mGraySubmat;
	private Mat mIntermediateMat;
	private Mat mHand;
	//    private Point p;
	private Bitmap mBitmap;
	private int mViewMode;

	public EView(Context context) {
		super(context);
		//        mViewMode = VIEW_MODE_GRAY;
		mViewMode = VIEW_MODE_RGBA;
	}

	@Override
	protected void onPreviewStarted(int previewWidth, int previewHeight) {
		synchronized (this) {
			// initialize Mats before usage
			mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);	//what is the purpose of creating Mat object of height more than needed?
			mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());

			mRgba = new Mat();
			mIntermediateMat = new Mat();
			mHand = new Mat();

			mBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888); 		//each pixel is stored on 4 bytes(R, G, B, alpha). hence, 4 channels
		}
	}

	@Override
	protected void onPreviewStopped() {
		if(mBitmap != null) {
			mBitmap.recycle();
		}

		synchronized (this) {
			// Explicitly deallocate Mats
			if (mYuv != null)
				mYuv.release();
			if (mRgba != null)
				mRgba.release();
			if (mGraySubmat != null)
				mGraySubmat.release();
			if (mIntermediateMat != null)
				mIntermediateMat.release();
			if (mHand != null)
				mHand.release();
			mYuv = null;
			mRgba = null;
			mGraySubmat = null;
			mIntermediateMat = null;
			mHand = null;
		}
	}

	@Override
	protected Bitmap processFrame(byte[] data) {
		mYuv.put(0, 0, data);

		final int viewMode = mViewMode;

		switch (viewMode) {
		case VIEW_MODE_GRAY:
			// Subtract external background from hand
			mHand = Filter.subtractBG(mYuv, mRgba);
			// Classify hand signal
			int group = Classification.classify(mRgba, mHand);
			if(group == 0)
				printTextUL(mRgba, "Input Hand");
			else if(group == 1){
				printTextUL(mRgba, "Motion");
				Motion.getASLMotionLetter();
			}
			else if(group == 2){
        		printTextUL(mRgba, "Height");
//        		Log.i("size", ""+mHand.rows());
        		printTextBL(mRgba, HeightClass.findTips(mHand));
			}
			else if(group == 3)
				printTextUL(mRgba, "Mid");
			else if(group == 4){		// Centroid
				// these Centroid codes came from the D project
				
				storeImage(mHand);
				String textLetter = identifyStoredImage();
				printTextUL(mRgba, "Cen " + textLetter);
			}
			else if(group == 5)
				printTextUL(mRgba, "Width");
			break;
		case VIEW_MODE_RGBA:
			Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
			Core.putText(mRgba, "loadNNet", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
//			Core.putText(mRgba, "OpenCV + Android", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
			break;
		case VIEW_MODE_CANNY:
			Imgproc.Canny(mGraySubmat, mIntermediateMat, 80, 100);
			Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);
			break;
		}

		Bitmap bmp = mBitmap;

		try {
			Utils.matToBitmap(mRgba, bmp);
		} catch(Exception e) {
			Log.e("com.duckie", "Utils.matToBitmap() throws an exception: " + e.getMessage());
			bmp.recycle();
			bmp = null;
		}
		return bmp;
	}

	public void setViewMode(int viewMode) {
		mViewMode = viewMode;
	}

	public void printTextUL(Mat img, String text){
		final float top = 35.0f;
		final float left = 5.0f;
		// Get the screen's density scale
		final float scale = getFrameHeight()/320.0f;
		// Convert the dps to pixels, based on density scale
		int iTop = (int) (top * scale + 0.5f);
		int iLeft =  (int) (left * scale + 0.5f);
		Core.putText(img, text, new Point(iLeft, iTop), 4, 1.0f*scale, new Scalar(255, 0, 0, 255), 1);
	}

	public void printTextBL(Mat img, String text){
		final float bottom = 305.0f;		//320
		final float left = 5.0f;
		// Get the screen's density scale
		final float scale = getFrameHeight()/320.0f;
		// Convert the dps to pixels, based on density scale
		int iBottom = (int) (bottom * scale + 0.5f);
		int iLeft =  (int) (left * scale + 0.5f);
		Core.putText(img, text, new Point(iLeft, iBottom), 4, 1.0f*scale, new Scalar(255, 0, 0, 255), 1);
	}

















	/**
	 *  NEURAL NET FUNCTIONS BELOW
	 * 
	 */

	private String identifyStoredImage() {
		String pathName = Environment.getExternalStorageDirectory().getPath() + APP_PATH_SD +"/"+ IMAGE_NAME;
		image = ImageFactory.getImage(pathName);
		return recognize(image);
	}

	private void storeImage(Mat mHand) {
		bitmap = Bitmap.createBitmap(mHand.width(), mHand.height(), Bitmap.Config.ARGB_8888);
		Bitmap bmp = bitmap;

		try {
			Utils.matToBitmap(mHand, bmp);
		} catch (Exception e) {
			Log.e("VIEWMODEGREEN","_MATOBITMAP");
		}

		String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD;

		try {
			File dir = new File(fullPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			OutputStream fOut = null;
			File file = new File(fullPath, IMAGE_NAME);
			file.createNewFile();
			fOut = new FileOutputStream(file);

			// 100 means no compression, the lower you go, the stronger the compression
			//               bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e){
			Log.e("IO",e.toString());
		}
		
	}
	
	
	

	public String recognize(Image image) {
		//		showDialog(RECOGNIZING_IMAGE_DIALOG);
		// recognize image
		HashMap<String, Double> output = imageRecognition.recognizeImage(image);

		Log.e(TAG,output.toString());
		//		dismissDialog(RECOGNIZING_IMAGE_DIALOG);
		return getAnswer(output);
		//		return "A";
	}

	//	public String recognize1(Image image) {
	//		//		showDialog(RECOGNIZING_IMAGE_DIALOG);
	//		// recognize image
	//		HashMap<String, Double> output = imageRecognition1.recognizeImage(image);
	//
	//		Log.e(TAG,output.toString());
	//		//		dismissDialog(RECOGNIZING_IMAGE_DIALOG);
	//		return getAnswer(output);
	//		//		return "A";
	//	}

	public void loadData() {
		//		showDialog(LOADING_DATA_DIALOG);
		// load neural network in separate thread with stack size = 32000
		new Thread(null, loadDataRunnable, "dataLoader", 256000).start();


	}

	public Runnable loadDataRunnable = new Runnable() {
		public void run() {
			// open neural network
			//			InputStream is = getResources().openRawResource(R.raw.animals_net);
			InputStream is = getResources().openRawResource(R.raw.cen01);
			// load neural network
			nnet = NeuralNetwork.load(is);
			imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);

			//			InputStream is1 = getResources().openRawResource(R.raw.oc526cnl);
			//			nnet1 = NeuralNetwork.load(is1);
			//			imageRecognition1 = (ImageRecognitionPlugin) nnet1.getPlugin(ImageRecognitionPlugin.class);

			Log.i(TAG, "NeuralNet Loading Done");
			mViewMode = VIEW_MODE_GRAY;
		}
	};


	public String getAnswer(HashMap<String, Double> output) {
		double highest = 0;
		String answer = "";
		for (Map.Entry<String, Double> entry : output.entrySet()) {
			if (entry.getValue() > highest) {
				highest = entry.getValue();
				answer = entry.getKey();
			}
		}
		return answer;
	}

}
