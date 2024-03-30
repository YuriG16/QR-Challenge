package it.twentyfive.demoqrcode.model;

import java.awt.Color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBord {

    private String borderColor;
    private int bordSizeTop;
    private int bordSizeRight;
    private int bordSizeBottom;
    private int bordSizeLeft;
    private LogoUrl iconUrl;
    
    public Color getBorderColor(){
        return Color.decode(borderColor);
    }
    
}
