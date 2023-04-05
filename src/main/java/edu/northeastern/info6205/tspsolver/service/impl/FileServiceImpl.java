package edu.northeastern.info6205.tspsolver.service.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.info6205.tspsolver.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVParserServiceImpl.class);

	@Override
	public File convertMultiPartToFile(MultipartFile multipartFile) {
		LOGGER.trace("Will convert the given multipartFile to file: {}", multipartFile);
		
		File file = new File(multipartFile.getOriginalFilename());
		
		try (FileOutputStream stream = new FileOutputStream(file)) {
		    stream.write(multipartFile.getBytes());
		} catch (Exception e) {
			LOGGER.error("Error in convertMultiPartToFile(): {}", e.getMessage(), e);
		}
		
	    return file;
	}

}
