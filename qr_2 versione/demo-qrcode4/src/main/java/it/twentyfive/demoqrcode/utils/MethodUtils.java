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
        if((qrCode.getWidth()==0||qrCode.getHeight()==0)||qrCode.getWidth()!=qrCode.getHeight()){
            qrCode.setWidth(350);
            qrCode.setHeight(350);
        }
        
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCode.getRequestUrl(), BarcodeFormat.QR_CODE, qrCode.getWidth(), qrCode.getHeight());
        Color onColor = Color.decode(qrCode.getCustomColor().getOnColor());
        Color offColor = Color.decode(qrCode.getCustomColor().getOffColor());
        

        //MatrixToImageConfig con = new MatrixToImageConfig(0xFFFFFFFF, 0xFF000000);
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor.getRGB(),offColor.getRGB());
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);

        if(qrCode.getLogoUrl()!=null){
            BufferedImage whiteBox=createWhiteBox(qrImage);
            addWhiteBox(qrImage, whiteBox);
            BufferedImage resizedLogo= resizeImage(qrCode.getLogoUrl().getImgByUrl(), whiteBox.getWidth(), whiteBox.getHeight());
            BufferedImage imgWithLogo=addLogoToCenter(qrImage, resizedLogo);
            qrImage=imgWithLogo;
        }
        if (qrCode.getCustomDBord() != null) {
            int top= qrCode.getCustomDBord().getBordSizeTop();
            int bottom= qrCode.getCustomDBord().getBordSizeBottom();
            int left= qrCode.getCustomDBord().getBordSizeLeft();
            int right =qrCode.getCustomDBord().getBordSizeRight();
            qrImage=addBorder(qrImage, left,right,top, bottom, qrCode.getCustomDBord().getBorderColor());
            if(qrCode.getCustomDBord().getIconUrl()!=null){
                BufferedImage iconImg=qrCode.getCustomDBord().getIconUrl().getImgByUrl();
                int targetWidth = (int)((double)iconImg.getWidth() / iconImg.getHeight() * bottom);
                BufferedImage resizedIconImg=resizeImage(iconImg, targetWidth, bottom);
                int iconX=left;
                int iconY=qrImage.getHeight()-resizedIconImg.getHeight();
                BufferedImage imageWithIcon = new BufferedImage(qrImage.getWidth(), qrImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = imageWithIcon.createGraphics();
                g.drawImage(qrImage, 0, 0, null);
                g.drawImage(resizedIconImg, iconX, iconY, null);
                g.dispose();
                qrImage=imageWithIcon;
            }
        }

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    /*public static BufferedImage drawImage(BufferedImage img, int x, int y, int wight, int height){
        Graphics2D g = img.createGraphics();
        g
    }*/

    public static BufferedImage addBorder(BufferedImage img, int bordSizeLeft, int bordSizeRight, int bordSizeTop, int bordSizeBottom, Color borderColor) {
        int newWidth=img.getWidth() + bordSizeLeft+bordSizeRight;
        int newHeight=img.getHeight() + bordSizeTop+bordSizeBottom;
        BufferedImage imageWithBorder = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = imageWithBorder.createGraphics();
            g.setColor(borderColor);
            g.fillRect(0, 0, imageWithBorder.getWidth(), imageWithBorder.getHeight());
            int qrX = bordSizeLeft;
            int qrY = bordSizeTop;
            g.drawImage(img, qrX, qrY, null);
            g.dispose();

            return imageWithBorder;
    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    public static BufferedImage addLogoToCenter(BufferedImage baseImage, BufferedImage logo) {
        BufferedImage whiteBox=createWhiteBox(baseImage);
        BufferedImage resizedLogo=resizeImage(logo, whiteBox.getWidth(), whiteBox.getHeight());
        int logoX = baseImage.getWidth()/2-(resizedLogo.getWidth()/2);
        int logoY = baseImage.getHeight()/2-(resizedLogo.getHeight()/2);
    
        // Viene creata una nuova immagine con le stesse dimensioni dell'immagine di base, in cui verrà disegnato il logo.
        BufferedImage imageWithLogo = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageWithLogo.createGraphics();
        g.drawImage(baseImage, 0, 0, null);
        g.drawImage(resizedLogo, logoX, logoY, null);
        g.dispose();
    
        return imageWithLogo;
    }
    
    public static BufferedImage createWhiteBox(BufferedImage img) {
        int whiteBoxSize = (int) (Math.min(img.getWidth(), img.getHeight()) * 0.135);
    
        BufferedImage overlayImage = new BufferedImage(whiteBoxSize, whiteBoxSize, BufferedImage.TYPE_INT_ARGB); // Correzione qui
        Graphics2D graphics = overlayImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, whiteBoxSize, whiteBoxSize); // Correzione qui
        graphics.dispose();
    
        return overlayImage;
    }
    
    public static void addWhiteBox(BufferedImage img, BufferedImage whiteBoxImage) {
        BufferedImage whiteBox=createWhiteBox(img);
        int whiteBoxX = (img.getWidth() / 2)-(whiteBox.getWidth()/2);
        int whiteBoxY = (img.getHeight()/2) - (whiteBox.getHeight() / 2);
    
        Graphics2D graphics = img.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(whiteBoxX, whiteBoxY, whiteBox.getWidth(), whiteBox.getHeight());
        graphics.dispose();
    }
    
    
}
