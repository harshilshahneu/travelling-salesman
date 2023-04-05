package edu.northeastern.info6205.tspsolver.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service created for {@link File} related logic
 * */
public interface FileService {
	
	/**
	 * 
	 * Will convert the {@link MultipartFile} to a {@link File}
	 * */
	File convertMultiPartToFile(MultipartFile multipartFile);
	
}
