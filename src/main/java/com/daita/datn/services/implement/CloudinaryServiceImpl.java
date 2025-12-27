package com.daita.datn.services.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.daita.datn.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public void deleteFile(String fileId) throws IOException {
        cloudinary.uploader().destroy(fileId, ObjectUtils.emptyMap());
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        Map<?,?> filePath = cloudinary.uploader().upload(file.getBytes(),ObjectUtils.emptyMap());
        return filePath.get("url").toString();
    }
}
