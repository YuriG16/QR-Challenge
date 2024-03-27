package it.twentyfive.demoqrcode.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoUrl {

    private String url;

    public URL getUrlByString() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public BufferedImage getImgByUrl() {
        try {
            URL urlObj = getUrlByString();
            if (urlObj != null) {
                return ImageIO.read(urlObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
