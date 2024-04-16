package it.twentyfive.demoqrcode.model;
import java.awt.Color;

import lombok.Data;

@Data
public class CustomColor {

    private String onColor;
    private String offColor;
    
    public int getOnColorInt() {
        return Color.decode(onColor).getRGB();
    }
    public int getOffColorInt() {
        return Color.decode(offColor).getRGB();
    }
}
