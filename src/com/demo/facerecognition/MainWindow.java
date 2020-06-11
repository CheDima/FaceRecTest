package com.demo.facerecognition;

import com.demo.facedetect.FaceDetection;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;

public class MainWindow {
    public static void main(String[] arg) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String window_name = "Capture - Face detection";
        JFrame frame = new JFrame(window_name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        processor my_processor = new processor();
        My_Panel my_panel = new My_Panel();
        frame.setContentPane(my_panel);
        frame.setVisible(true);
        Mat webcam_image = new Mat();
        VideoCapture capture = new VideoCapture();
        capture.open(0);
        com.demo.facedetect.FaceDetection detector = new FaceDetection();
        if (capture.isOpened()) {
            while (true) {
                capture.read(webcam_image);
                if (!webcam_image.empty()) {
                    frame.setSize(webcam_image.width() + 40, webcam_image.height() + 60);
                    long startTime = System.nanoTime();
                    detector.detectAndDisplay(webcam_image);
                    long endTime = System.nanoTime();
                    System.out.println(String.format("Elapsed time: %.2f ms", (float) (endTime - startTime) / 1000000));
                    my_panel.MatToBufferedImage(webcam_image);
                    my_panel.repaint();
                } else {
                    System.out.println(" --(!) No captured frame -- Break!");
                    break;
                }
            }
        }
    }
}
