package com.demo.facedetect;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class FaceDetector {

    private static final String classifierPath1 = "/Users/dmitrychernyaev/IdeaProjects/FaceRecTest/src/main/java/resources/FaceDetection/haarcascade_frontalface_alt.xml";
    private final CascadeClassifier faceCascade = new CascadeClassifier();
    private static final int MIN_FACE_SIZE = 7;

    public FaceDetector() {
        faceCascade.load(classifierPath1);
    }

    public void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        int height = grayFrame.rows();

        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(MIN_FACE_SIZE, MIN_FACE_SIZE), new Size(height, height));

        Rect[] facesArray = faces.toArray();
        if (facesArray.length == 1) {
            Point center = new Point(facesArray[0].x + facesArray[0].width / 2, facesArray[0].y + 30);
			drawTarget(frame, center);
		}
    }

	private void drawTarget(Mat frame, Point center) {
		Imgproc.circle(frame, center, 10, new Scalar(0, 0, 255), 4);
		Imgproc.circle(frame, center, 25, new Scalar(0, 0, 255), 4);
		Imgproc.circle(frame, center, 40, new Scalar(0, 0, 255), 4);
		Imgproc.line(frame, new Point(center.x - 40, center.y), new Point(center.x + 40, center.y), new Scalar(0, 0, 255), 4);
		Imgproc.line(frame, new Point(center.x, center.y - 40), new Point(center.x, center.y + 40), new Scalar(0, 0, 255), 4);
	}

}
