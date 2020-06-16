package com.demo.facedetect;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;

public class BarCodeDetector {

    private static final Map<DecodeHintType, Object> tmpHintsMap;

    static {
        tmpHintsMap = new EnumMap<>(DecodeHintType.class);
        //final List<BarcodeFormat> formats = new ArrayList<>(Arrays.asList(BarcodeFormat.values()));
        final List<BarcodeFormat> formats = List.of(BarcodeFormat.EAN_13);

        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
    }

    public String detectBarCode(Mat mat) {
        Mat grayMat = new Mat();
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY);
        String barCode;
        Mat bwMat = new Mat();
        Imgproc.threshold(grayMat, bwMat, 128, 255, Imgproc.THRESH_BINARY);
        BufferedImage img = toBufferedImage(bwMat);
        LuminanceSource source = new BufferedImageLuminanceSource(img);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap, tmpHintsMap);
            barCode = result.getText();

            drawBarCodeValue(mat, barCode);
        } catch (NotFoundException e) {
            barCode = null;
        }
        return barCode;
    }

    private void drawBarCodeValue(Mat mat, String bCode) {
        Imgproc.rectangle(mat, new Point(200, 100), new Point(1000, 300), new Scalar(255, 255, 255), Imgproc.FILLED);
        Imgproc.putText(mat, bCode, new Point(200, 200), Imgproc.FONT_HERSHEY_PLAIN, 4, new Scalar(255, 0, 0));
    }

    private BufferedImage toBufferedImage(Mat inpMat) {
        Mat mat = new Mat();

        if (inpMat.channels() == 1) {
            Imgproc.cvtColor(inpMat, mat, Imgproc.COLOR_GRAY2BGR);
        } else {
            mat = inpMat;
        }
        byte[] buffer = new byte[mat.channels() * mat.cols() * mat.rows()];
        mat.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}
