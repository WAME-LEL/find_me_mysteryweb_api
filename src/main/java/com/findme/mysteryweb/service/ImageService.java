package com.findme.mysteryweb.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public class ImageService {

    public Resource loadImage(String fileUrl) throws MalformedURLException {
        return new UrlResource(fileUrl);
    }
}