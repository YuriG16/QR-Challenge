package it.twentyfive.demoqrcode.model;

import lombok.Data;





@Data
public class CustomQrRequest {

    private String requestUrl;
    private String borderColor;
    private CustomColor customColor;
    //private CustomDBord dBord;
    // private String logoPath;
    // private String customText;

}

