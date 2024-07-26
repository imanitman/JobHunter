package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import vn.hoidanit.jobhunter.domain.response.file.ResUploadFileDto;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.StorageException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1")
public class FileController {
    
    private final FileService fileService;

    @Value("${hoidanit.upload-file.base-uri}")
    private String baseUri;
    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload Single file")
    public ResponseEntity<ResUploadFileDto> uploadFile(@RequestParam (name = "file", required = false) MultipartFile file,
        @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        if (file.isEmpty() && file != null){
            throw new StorageException("File can't be empty");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid){
            throw new StorageException("Invalid file extension only allows" + allowedExtensions.toString());
        }
        this.fileService.createUploadFolder(baseUri + folder);
        String uploadFile = this.fileService.store(file, folder);
        ResUploadFileDto resUploadFileDto = new ResUploadFileDto(uploadFile, Instant.now());
        return ResponseEntity.status(HttpStatus.OK).body(resUploadFileDto);
    }

    @GetMapping("/files")
    @ApiMessage("Download single file")
    public ResponseEntity<Resource> downloadSingleFile(
        @RequestParam(name = "fileName", required = false) String fileName,
        @RequestParam(name = "folder", required = false) String folder
    ) throws StorageException, URISyntaxException, FileNotFoundException{
        if (fileName == null || folder == null){
            throw new StorageException("I can't find your file");
        }
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0){
            throw new StorageException("File name: " + fileName + "not found");
        }
        InputStreamResource resource = this.fileService.getResource(fileName, folder);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
