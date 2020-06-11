package com.demo.facedetect;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import com.demo.utils.Utils;

public class FaceDetection {

	public static String basePath=System.getProperty("user.dir");
	public static String classifierPath1=basePath+"\\src\\resources\\FaceDetection\\parojos.xml";
	public static String inpImgFilename=basePath+"\\src\\resources\\FaceDetection\\input.jpg";
	public static String opImgFilename=basePath+"\\src\\resources\\FaceDetection\\output.jpg";
    private CascadeClassifier faceCascade = new CascadeClassifier();

    public FaceDetection() {
        faceCascade.load(classifierPath1);
    }

    public void detectAndDisplay(Mat frame) throws IOException
	{
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();
		int absoluteFaceSize=0;

		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		// compute minimum face size (1% of the frame height, in our case)
		
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				absoluteFaceSize = Math.round(height * 0.01f);
			}
				
		// detect faces
		faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(absoluteFaceSize, absoluteFaceSize), new Size(height,height));
				
		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();
		if (facesArray.length == 1) {
			Point center = new Point(facesArray[0].x + facesArray[0].width/2, facesArray[0].y - 30 );
			Imgproc.circle(frame, center,  10, new Scalar(0, 255, 255));
			Imgproc.circle(frame, center,  20, new Scalar(0, 255, 255));
			Imgproc.circle(frame, center,  30, new Scalar(0, 255, 255));
			Imgproc.line(frame, new Point(center.x -40, center.y), new Point(center.x + 40, center.y),  new Scalar(0, 255, 255));
			Imgproc.line(frame, new Point(center.x , center.y - 40), new Point(center.x, center.y + 40),  new Scalar(0, 255, 255));

		}
		//for (int i = 0; i < facesArray.length; i++)
			//Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 2);
			
	}

}
