package com.demo.facerecognition;/*
 * Captures the camera stream with OpenCV
 * Search for the faces
 * Display a circle around the faces using Java
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import javax.swing.*;

import org.opencv.core.*;
import org.opencv.core.Point;

import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import static org.opencv.imgproc.Imgproc.boundingRect;

class My_Panel extends JPanel{
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	// Create a constructor method
	public My_Panel(){
		super();
	}
	/**
	 * Converts/writes a Mat into a BufferedImage.
	 *
	 * @paramjjj Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */
	public boolean MatToBufferedImage(Mat matBGR){
		int width = matBGR.width(), height = matBGR.height(), channels = matBGR.channels() ;
		byte[] sourcePixels = new byte[width * height * channels];
		matBGR.get(0, 0, sourcePixels);
		// create new image and get reference to backing data
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
		return true;
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (this.image==null) return;
		g.drawImage(this.image,10,10,this.image.getWidth(),this.image.getHeight(), null);
		//g.drawString("This is my custom Panel!",10,20);
	}
}
class processor {
	int i=0;
	static final int SENSITIVITY_VALUE = 30;
	Mat previous = new Mat();
	boolean first = true;
	private CascadeClassifier face_cascade;
	// Create a constructor method
	public processor(){
		face_cascade=new CascadeClassifier("D:/Projects/Java/ShootIt/target.xml");
		if(face_cascade.empty())
		{
			System.out.println("--(!)Error loading A\n");
			return;
		}
		else
		{
			System.out.println("Face classifier loooaaaaaded up");
		}
	}
	public Mat detect(Mat inputframe){

		Mat mDiff=new Mat();
		Mat mGray=new Mat();
		Mat mCont = new Mat();


		//inputframe.copyTo(mGray);
		Imgproc.cvtColor(inputframe, mGray, Imgproc.COLOR_BGR2GRAY);
		//Imgproc.equalizeHist( mGray, mGray );

		MatOfRect faces = new MatOfRect();
		//inputframe.copyTo(mRgba);
		//inputframe.copyTo(mGrey);
		// Imgproc.cvtColor( mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
		//Imgproc.equalizeHist( mGrey, mGrey );
/*        face_cascade.detectMultiScale(mGrey, faces);
        System.out.println(String.format("Detected %s faces ---------  %s", faces.toArray().length,i));
        i++;
        for(Rect rect:faces.toArray())
        {
            Point center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5 );
            Core.ellipse( mRgba, center, new Size( rect.width*0.5, rect.height*0.5), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );
        }*/
		if(first){
			mGray.copyTo(previous);
			first = false;
		}

		Core.absdiff(mGray, previous, mDiff);
		Imgproc.threshold(mDiff,mDiff,SENSITIVITY_VALUE,255,Imgproc.THRESH_BINARY);
		Imgproc.blur(mDiff,mDiff, new Size(5,5));
		Imgproc.threshold(mDiff,mDiff,SENSITIVITY_VALUE,255,Imgproc.THRESH_BINARY);
		mGray.copyTo(previous);



		mDiff.copyTo(mCont);
		java.util.List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(mCont, contours,new Mat(), Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_TC89_L1);

		Imgproc.cvtColor(mDiff, mDiff, Imgproc.COLOR_GRAY2BGR);
		System.out.println("Contours " + Integer.toString(contours.size()));
		if (contours.size()>0)
		{
			Rect objectBoundingRectangle = boundingRect(contours.get(contours.size()-1));
			int x = objectBoundingRectangle.x;//+objectBoundingRectangle.width/2;
			int y = objectBoundingRectangle.y;//+objectBoundingRectangle.height/2;

			//Core.rectangle(mDiff, new Point(x,y), new Point(x+objectBoundingRectangle.width, y+objectBoundingRectangle.height), new Scalar(0,255,0), 1);
			//Core.line(mDiff, new Point(x,y-25), new Point(x, y+25), new Scalar(0,255,0), 1);
		}

		return mDiff;
	}
}
