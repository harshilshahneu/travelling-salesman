package edu.northeastern.info6205.tspsolver.model;

/**
 * Will represent the state
 * for TSP Solution output
 * */
public class TSPOutput {
	private String completeFilePath;
	private String fileName;
	
	public String getCompleteFilePath() {
		return completeFilePath;
	}
	public void setCompleteFilePath(String completeFilePath) {
		this.completeFilePath = completeFilePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "TSPOutput [completeFilePath=" + completeFilePath + ", fileName=" + fileName + "]";
	}
}
