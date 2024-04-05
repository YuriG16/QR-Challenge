package it.twentyfive.demoqrcode.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomQrRequest {
    
    private String requestUrl;
    private int width;
    private int height;
    private CustomBord customBord;
    private CustomText customText;
    private CustomColor customColor;
    private LogoUrl logoUrl;

}

