package com.ivanman.fm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/* 參考來源：https://www.youtube.com/watch?v=wW0nVc2NlhA */

@RestController
public class FileManageController {
	
	@Autowired
	FileStorageService fileStorageService;
	private static final Logger logger = Logger.getLogger(FileManageController.class.getName());

	@PostMapping("/upload-file")
	public boolean fileUpload(@RequestParam("file") MultipartFile file) {
		
		try {
			fileStorageService.storageFile(file);
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "upload failed.");
		}
		return false;
	}
	
	@GetMapping("/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
		
		try {
			File file = fileStorageService.getDownloadFile(filename);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename +"\"")
					.contentLength(file.length())
					.body(new InputStreamResource(Files.newInputStream(file.toPath())))
					;
		} catch (IOException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/download-faster")
	public ResponseEntity<Resource> downloadFileFaster(@RequestParam("filename") String filename) {
		
		try {
			File file = fileStorageService.getDownloadFile(filename);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename +"\"")
					.contentLength(file.length())
					.body(new FileSystemResource(file))
					;
		} catch (IOException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
