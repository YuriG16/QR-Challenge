package it.twentyfive.demoqrcode.model;

import java.awt.Color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomQrRequest {
    private int width;
    private int height;
    private String requestUrl;
    private CustomDBord customDBord;
    private CustomColor customColor;
    private LogoUrl logoUrl;
    //private CustomDBord dBord;
    // private String logoPath;
    // private String customText;

    

}

