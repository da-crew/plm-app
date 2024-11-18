package com.studentgroup.app.service;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.studentgroup.app.model.ImageFile;
import com.studentgroup.app.model.repositories.ImageFileRepository;


@Service
public class FileStorageService {
    
    @Autowired
    private ImageFileRepository imgfileRepo;

    public ImageFile store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        BufferedImage img = ImageIO.read(file.getInputStream());
        if (img == null) {
            throw new IOException("Failed to read image file");
        }
        ImageFile imgFile = new ImageFile(fileName, file.getContentType(), file.getBytes());
        return imgfileRepo.save(imgFile);
    }

    public Optional<ImageFile> getFile(String id) {
        return imgfileRepo.findById(id);
    }

    public void deleteFile(String id) {
        imgfileRepo.deleteById(id);
    }
}