package it.twentyfive.demoqrcode.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import it.twentyfive.demoqrcode.model.CustomColor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
public class MethodUtils {


    public static byte[] generateQrCodeImage(String text, int width, int height, Color borderColor, CustomColor cc) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Color onColor = Color.decode(cc.getOnColor());
        Color offColor = Color.decode(cc.getOffColor());
        

        //MatrixToImageConfig con = new MatrixToImageConfig(0xFFFFFFFF, 0xFF000000);
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor.getRGB(),offColor.getRGB());
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);

        if (borderColor != null) {
            qrImage = addBorder(qrImage, 25, borderColor);
        }


        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public static BufferedImage addBorder(BufferedImage img, int borderSize, Color borderColor) {
        BufferedImage imageWithBorder = new BufferedImage(
                img.getWidth() + 2 * borderSize,
                img.getHeight() + 2 * borderSize,
                BufferedImage.TYPE_INT_ARGB);

                Graphics2D g = imageWithBorder.createGraphics();
                g.setColor(borderColor);
                g.fillRect(0, 0, imageWithBorder.getWidth(), imageWithBorder.getHeight());
                g.drawImage(img, borderSize, borderSize, null);
                g.dispose();

                return imageWithBorder;
    }
}
