package com.studentgroup.app.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentgroup.app.model.ImageFile;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String>{
    public ImageFile findByFilename(String name);
}