package it.twentyfive.demoqrcode.controller;

import it.twentyfive.demoqrcode.model.CustomQrRequest;
import it.twentyfive.demoqrcode.model.ResponseImage;
import it.twentyfive.demoqrcode.utils.MethodUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.Base64;


@RestController
public class QrCodeController {

    @PostMapping("/generate")
    public ResponseEntity downloadQrCodeBase64(@RequestBody CustomQrRequest request) throws WriterException, IOException, RuntimeException {
        try {
            byte[] bytes = MethodUtils.generateQrCodeImage(request);
            String base64 = Base64.getEncoder().encodeToString(bytes);
            base64 = "data:image/png;base64," + base64;
            ResponseImage response = new ResponseImage();
            response.setImageBase64(base64);
            return ResponseEntity.status(HttpStatus.OK).body(response);
    
        } catch (RuntimeException e) {
            return MethodUtils.handleRuntimeException(e);
        }
    }
}
