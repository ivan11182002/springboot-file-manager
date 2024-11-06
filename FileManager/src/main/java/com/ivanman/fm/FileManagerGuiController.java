package com.ivanman.fm;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileManagerGuiController {
	
	@Autowired
	FileStorageService fileStorageService;
	
	@GetMapping("/uploader")
	public String uploader() {
		return "uploader";
	}
	
	@GetMapping("/listFiles")
	public String listFiles(Model model) throws IOException {
	    // Assume we get file names from some directory or storage
	    List<String> fileNames = fileStorageService.getAllFileNames(); // Fetch files from directory
	    model.addAttribute("files", fileNames);
	    return "list-files"; // Returns the Thymeleaf template
	}
}
