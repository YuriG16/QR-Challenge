package it.twentyfive.demoqrcode.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import it.twentyfive.demoqrcode.model.CustomBord;
import it.twentyfive.demoqrcode.model.CustomColor;
import it.twentyfive.demoqrcode.model.CustomQrRequest;
import it.twentyfive.demoqrcode.model.CustomText;
import it.twentyfive.demoqrcode.model.LogoUrl;
import it.twentyfive.demoqrcode.utils.exceptions.InvalidColorException;
import it.twentyfive.demoqrcode.utils.exceptions.InvalidInputException;
import it.twentyfive.demoqrcode.utils.exceptions.InvalidNumberException;
import it.twentyfive.demoqrcode.utils.exceptions.InvalidURLException;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
public class MethodUtils {


    public static byte[] generateQrCodeImage(CustomQrRequest qrCode) throws WriterException, IOException {
        QRCodeWriterEdited qrCodeWriter = new QRCodeWriterEdited();
        if (qrCode.getRequestUrl() == null || qrCode.getRequestUrl().isEmpty()) {
            throw new InvalidURLException("RequestURL field is empty");
        } else if (isValidUrl(qrCode.getRequestUrl()) == false) {
            throw new InvalidURLException("Invalid URL format");
        }
        if((qrCode.getWidth()==0||qrCode.getHeight()==0)||qrCode.getWidth()!=qrCode.getHeight()){
            qrCode.setWidth(350);
            qrCode.setHeight(350);
        }
        
        BitMatrix bitMatrix=null;
        if(qrCode.getPadding()!=null&&!qrCode.getPadding().isEmpty()){
            bitMatrix = qrCodeWriter.encode(qrCode.getRequestUrl(), BarcodeFormat.QR_CODE, qrCode.getWidth(), qrCode.getHeight(), qrCode.getPaddingInt());
        }
        else{
            bitMatrix = qrCodeWriter.encode(qrCode.getRequestUrl(), BarcodeFormat.QR_CODE, qrCode.getWidth(), qrCode.getHeight());
        }
        
        BufferedImage qrImage=null;
        CustomColor myColor=qrCode.getCustomColor();
        if (myColor!=null&&
        myColor.getOnColor()!=null&&!myColor.getOnColor().isEmpty()&&
        myColor.getOffColor()!=null&&!myColor.getOffColor().isEmpty()) {
            if (!isValidColor(myColor.getOnColor())||!isValidColor(myColor.getOffColor())||myColor.getOnColor().equals(myColor.getOffColor())) {
                throw new InvalidColorException("Color not valid");
            } else {
            Color onColor = Color.decode(myColor.getOnColor());
            Color offColor = Color.decode(myColor.getOffColor());
            MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor.getRGB(), offColor.getRGB());
            qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
            }
            
        } else {
            MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
            BufferedImage qr=MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            qrImage=qr;
        }
        LogoUrl logo = qrCode.getLogoUrl();
        if (logo != null && logo.getUrl() != null && !logo.getUrl().isEmpty()) {
            String logoUrl = qrCode.getLogoUrl().getUrl();
            if (isValidUrl(logoUrl)) {
                BufferedImage whiteBox = createWhiteBox(qrImage);
                addWhiteBox(qrImage, whiteBox);
                BufferedImage resizedLogo = resizeImage(logo.getImgByUrl(), whiteBox.getWidth(), whiteBox.getHeight());
                BufferedImage imgWithLogo = addLogoToCenter(qrImage, resizedLogo);
                qrImage = imgWithLogo;
            } else {
                throw new InvalidURLException("Invalid URL format" );
            }
        }
        CustomBord customBord = qrCode.getCustomBord();
        if (!isValidColor(customBord.getBorderColor())) {
            throw new InvalidColorException("Border color not valid");
        }
        else {
        
            ArrayList<Integer> listaBordi = customBord.setBordSizes(customBord.getBordSizes());
            int top = listaBordi.get(0);
            int right = listaBordi.get(1);
            int bottom = listaBordi.get(2);
            int left = listaBordi.get(3);
            qrImage = addBorder(qrImage, top, right, bottom, left, customBord.getBorderColorS());
    
            if (customBord.getIconUrl() != null &&
                customBord.getIconUrl().getUrl() != null &&
                !customBord.getIconUrl().getUrl().isEmpty()) {
                
                String iconUrl = customBord.getIconUrl().getUrl();
                
                if (isValidUrl(iconUrl)) {
                    BufferedImage iconImg = customBord.getIconUrl().getImgByUrl();
                    int targetWidth = (int)((double)iconImg.getWidth() / iconImg.getHeight() * bottom);
                    BufferedImage resizedIconImg = resizeImage(iconImg, targetWidth, bottom);
                    int iconX = left;
                    int iconY = qrImage.getHeight() - resizedIconImg.getHeight();
                    BufferedImage imageWithIcon = new BufferedImage(qrImage.getWidth(), qrImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = imageWithIcon.createGraphics();
                    g.drawImage(qrImage, 0, 0, null);
                    g.drawImage(resizedIconImg, iconX, iconY, null);
                    g.dispose();
                    qrImage = imageWithIcon;
                } else {
                    throw new InvalidURLException("Invalid URL format for iconURL field");
                }
            }
            CustomText t = qrCode.getCustomText();
            if (t != null && !t.getText().isEmpty()) {
                if (t.getText() != null && !t.getText().isEmpty()) {
                    if (t.getPosition().equals("bottom") && bottom != 0) {
                        if (bottom >= t.getFontSize()) {
                            BufferedImage b = addTextToBorderBottomWithOffset(qrImage, t, bottom, t.getOffset());
                            qrImage = b;
                        } else if (bottom < t.getFontSize()) {
                            t.setFontSize(bottom);
                            BufferedImage b = addTextToBorderBottomWithOffset(qrImage, t, bottom, t.getOffset());
                            qrImage = b;
                        }
                    }
                    if (t.getPosition().equals("top") && bottom != 0) {
                        if (top >= t.getFontSize()) {
                            BufferedImage b = addTextToBorderTopWithOffset(qrImage, t, top, t.getOffset());
                            qrImage = b;
                        } else if (top < t.getFontSize()) {
                            t.setFontSize(top);
                            BufferedImage b = addTextToBorderTopWithOffset(qrImage, t, top, t.getOffset());
                            qrImage = b;
                        }
                    }
                }
                if(!t.getPosition().equals("bottom")&&!t.getPosition().equals("top")){
                    throw new InvalidInputException("The only inputs allowed are 'bottom' and 'top'");
                }
            }
        } 
               
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }


    public static boolean isValidUrl(String url) {
        String regex = "^(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(\\/\\S*)?$";
        if (!url.matches(regex)){
            return false;
        }
        return true;
    }

    public static boolean isValidColor(String color){
        String regex = "^#[0-9a-fA-F]{6}$";
        if (!color.matches(regex)) {
            return false;
        }
        return true;
    }

    public static BufferedImage addBorder(BufferedImage img, int bordSizeTop, int bordSizeRight, int bordSizeBottom, int bordSizeLeft, Color borderColor) {
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

    public static BufferedImage addTextToBorderBottomWithOffset(BufferedImage img, CustomText t, int borderWidth, int offset) {
        Graphics2D g = img.createGraphics();
        g.setColor(t.getFontColor());
        Font font = new Font("Arial", Font.PLAIN, t.getFontSize());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(t.getText());                    
        int charHeight = metrics.getAscent() - metrics.getDescent();
        int x=0;
        if(offset>=0){
            x = (img.getWidth() - textWidth) / 2+offset;
        }else{
            x = (img.getWidth() - textWidth) / 2-Math.abs(offset);
        }                       
        int y = img.getHeight()-borderWidth/2+(charHeight/2);
        g.drawString(t.getText(), x, y);
        g.dispose();
    
        return img;
    }

    public static BufferedImage addTextToBorderTopWithOffset(BufferedImage img, CustomText t, int borderWidth, int offset) {
        Graphics2D g = img.createGraphics();
        g.setColor(t.getFontColor());
        Font font = new Font("Arial", Font.PLAIN, t.getFontSize());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(t.getText());                    
        int charHeight = metrics.getAscent() - metrics.getDescent();
        int x=0;
        if(offset>=0){
            x = (img.getWidth() - textWidth) / 2+offset;
        }else{
            x = (img.getWidth() - textWidth) / 2-Math.abs(offset);
        }                       
        int y = borderWidth/2+(charHeight/2);
        g.drawString(t.getText(), x, y);
        g.dispose();
    
        return img;
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
    
        BufferedImage overlayImage = new BufferedImage(whiteBoxSize, whiteBoxSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = overlayImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, whiteBoxSize, whiteBoxSize);
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
    public static ResponseEntity handleRuntimeException(RuntimeException e) {
        if (e instanceof InvalidInputException || e instanceof InvalidURLException || e instanceof InvalidNumberException || e instanceof InvalidColorException){ 
            String errorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    
}
