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
import it.twentyfive.demoqrcode.model.CustomQrRequest;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
public class MethodUtils {


    public static byte[] generateQrCodeImage(CustomQrRequest qrCode) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCode.getRequestUrl(), BarcodeFormat.QR_CODE, qrCode.getWidth(), qrCode.getHeight());
        Color onColor = Color.decode(qrCode.getCustomColor().getOnColor());
        Color offColor = Color.decode(qrCode.getCustomColor().getOffColor());
        

        //MatrixToImageConfig con = new MatrixToImageConfig(0xFFFFFFFF, 0xFF000000);
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor.getRGB(),offColor.getRGB());
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);

        if (qrCode.getCustomDBord() != null) {
            int top= qrCode.getCustomDBord().getBordSizeTop();
            int bottom= qrCode.getCustomDBord().getBordSizeBottom();
            int left= qrCode.getCustomDBord().getBordSizeLeft();
            int right =qrCode.getCustomDBord().getBordSizeRight();
            qrImage=addBorder(qrImage, left,right,top, bottom, qrCode.getCustomDBord().getBorderColor());
        }
        if(qrCode.getLogoUrl()!=null){

        }


        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public static BufferedImage addBorder(BufferedImage img, int bordSizeLeft, int bordSizeRight, int bordSizeTop, int bordSizeBottom, Color borderColor) {
        int newWidth=img.getWidth() + bordSizeLeft+bordSizeRight;
        int newHeight=img.getHeight() + bordSizeTop+bordSizeBottom;
        BufferedImage imageWithBorder = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = imageWithBorder.createGraphics();
            g.setColor(borderColor);
            g.fillRect(0, 0, imageWithBorder.getWidth(), imageWithBorder.getHeight());
            int qrX = bordSizeLeft;
            int qrY = bordSizeBottom;
            g.drawImage(img, qrX, qrY, null);
            g.dispose();

            return imageWithBorder;
    }
    public static BufferedImage addLogoToCenter(BufferedImage baseImage, BufferedImage logo, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize) {
        // Calcola le dimensioni effettive dell'immagine senza i bordi
        int effectiveWidth = baseImage.getWidth() - leftBorderSize - rightBorderSize;
        int effectiveHeight = baseImage.getHeight() - topBorderSize - bottomBorderSize;
    
        // Calcola le coordinate del logo
        int logoX = (effectiveWidth - logo.getWidth()) / 2 + leftBorderSize;
        int logoY = (effectiveHeight - logo.getHeight()) / 2 + topBorderSize;
    
        // Viene creata una nuova immagine con le stesse dimensioni dell'immagine di base, in cui verrà disegnato il logo.
        BufferedImage imageWithLogo = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageWithLogo.createGraphics();
        g.drawImage(baseImage, 0, 0, null);
        g.drawImage(logo, logoX, logoY, null);
        g.dispose();
    
        return imageWithLogo;
    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        //L'immagine originale viene ridimensionata alle dimensioni desiderate utilizzando il metodo getScaledInstance, che ritorna un oggetto Image ridimensionato in base alle dimensioni specificate. Il parametro Image.SCALE_SMOOTH indica di utilizzare un algoritmo di ridimensionamento liscio.
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        //Viene creata una nuova immagine di tipo BufferedImage con le dimensioni desiderate e il tipo di immagine ARGB (Alfa, Rosso, Verde, Blu) che supporta la trasparenza.
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        //Viene ottenuto il contesto grafico (Graphics2D) per l'immagine di output appena creata.
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }
    
}
