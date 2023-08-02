package com.example.bookingfly.restapi;

import com.example.bookingfly.dto.request.UploadImageRequest;
import com.example.bookingfly.service.CloudflareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    final CloudflareService cloudflareService;

    @PostMapping(path = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<String> imageUpload(@ModelAttribute UploadImageRequest request) throws IOException {
        return cloudflareService.imageUpload(request.getFile());
    }
}

