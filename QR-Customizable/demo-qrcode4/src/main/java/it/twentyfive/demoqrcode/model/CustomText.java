package it.twentyfive.demoqrcode.model;

import java.awt.Color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomText {
    private String text;
    private int fontSize;
    private String fontColor;
    private String position;
    private int offset;

    public Color getFontColorS(){
        return Color.decode(fontColor);
    }
}


