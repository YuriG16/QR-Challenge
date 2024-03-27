package it.twentyfive.demoqrcode.controller;

import it.twentyfive.demoqrcode.model.CustomColor;
import it.twentyfive.demoqrcode.model.CustomQrRequest;
import it.twentyfive.demoqrcode.model.ResponseImage;
import it.twentyfive.demoqrcode.utils.MethodUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.awt.Color;
import java.util.Base64;

@RestController
public class QrCodeController {


    @PostMapping("/generate")
    public ResponseEntity<ResponseImage> downloadQrCodeBase64(@RequestBody CustomQrRequest request) {
        try {
            byte[] bytes = MethodUtils.generateQrCodeImage(request);
            String base64 = Base64.getEncoder().encodeToString(bytes);
            base64 = "data:image/png;base64," + base64;
            ResponseImage response = new ResponseImage();
            response.setImageBase64(base64);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
}
