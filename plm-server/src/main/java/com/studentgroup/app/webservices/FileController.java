package com.studentgroup.app.webservices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.studentgroup.app.model.ImageFile;
import com.studentgroup.app.service.FileStorageService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class FileController {
    
    @Autowired
    private FileStorageService storageService;

    //@PostMapping("/test/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            storageService.store(file);
            return ResponseEntity.ok().body("Successfully uploaded the file");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload the file");
        }
    }

    //the files from here can be served as static resources, meaning you can access them using URLs like "http://[hostname]/files/{id}"
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        Optional<ImageFile> foundFile = storageService.getFile(id);

        if (!foundFile.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ImageFile file = foundFile.get();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file.getData());
    }
        
}
