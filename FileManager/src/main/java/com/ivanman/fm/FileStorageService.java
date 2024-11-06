package com.ivanman.fm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
public class FileStorageService {
	
	private static final String STORAGE_LOCATION = "D:\\storage";
	
	public void storageFile(MultipartFile fileToSave) throws IOException {
		if(fileToSave == null) {
			throw new NullPointerException("file is not found");
		}
		var targetFile = new File(STORAGE_LOCATION + File.separator + fileToSave.getOriginalFilename());
		
		if( !StringUtils.equals(STORAGE_LOCATION, targetFile.getParent())) {
			throw new SecurityException("file not supported.");
		}
		
		Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public File getDownloadFile(String filename) throws IOException {
		if(filename == null || StringUtils.isEmpty(filename)) {
			throw new NullPointerException("filename is null");
		}
		var targetFile = new File(STORAGE_LOCATION + File.separator + filename);
		
		if( !StringUtils.equals(STORAGE_LOCATION, targetFile.getParent())) {
			throw new SecurityException("file not supported.");
		}
		
		if(targetFile.getAbsoluteFile() == null) {
			throw new FileNotFoundException("file not found");
		}
		
		return targetFile;
	}
	
	public List<String> getAllFileNames() throws IOException {
		
		// 影片中的寫法
//		try (DirectoryStream<Path> stream = Files.newDirectoryStream(new File((STORAGE_LOCATION)).toPath())) {
//			return StreamSupport.stream(stream.spliterator(), false)
//					.map(Path::getFileName) 
//					.map(Path::toString)
//					.collect(Collectors.toList());
//		}
		
		try (Stream<Path> files = Files.list(Path.of(STORAGE_LOCATION))) {
            // Collect all file names as strings and return
            return files
                    .filter(Files::isRegularFile) // Ensure only files are included, not directories
                    .map(Path::getFileName)       // Get just the file name, not the full path
                    .map(Path::toString)          // Convert Path to String
                    .collect(Collectors.toList());
        }
	}
}
