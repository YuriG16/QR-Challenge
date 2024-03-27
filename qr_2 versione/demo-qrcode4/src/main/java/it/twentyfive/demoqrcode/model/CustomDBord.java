package it.twentyfive.demoqrcode.model;

import java.awt.Color;

import lombok.Data;

@Data
public class CustomDBord {

    private String borderColor;
    private int bordSizeTop;
    private int bordSizeRight;
    private int bordSizeBottom;
    private int bordSizeLeft;
    private String text;
    
    public Color getBorderColor(){
        return Color.decode(borderColor);
    }
    
}
