package org.junstory.ex3.upload.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junstory.ex3.upload.exception.UploadNotSupportedException;
import org.junstory.ex3.upload.util.UploadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final UploadUtil uploadUtil;

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> delete(@PathVariable(name="fileName") String fileName) {
        log.info("delete file: " + fileName);

        uploadUtil.deleteFile(fileName);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("files")MultipartFile[] files){
        log.info("Uploading files.....");

        if(files == null || files.length == 0){
            throw new UploadNotSupportedException("No files to upload");
        }
        for (MultipartFile file : files) {
            log.info("---------------------------------");
            log.info("name: " + file.getOriginalFilename());
            checkFileType(file.getOriginalFilename());
        }

        List<String> result = uploadUtil.upload(files);

        return ResponseEntity.ok(result);
    }

    private void checkFileType(String fileName) throws UploadNotSupportedException {
        //jpg, gif, png, bmp
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        // ^은 시작을 의미 []안에 있으면 부정.
        String regExp = "^(jpg|jpeg|JPG|JPEG|png|PNG|gif|GIF|bm|BMP)";

        if(!suffix.matches(regExp)){
            throw new UploadNotSupportedException("File type not supported: " + suffix);
        }
    }
}
