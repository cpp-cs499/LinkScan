package ocr.data;

import java.util.List;

public class PublicOcrDataHolder {
	List<ParsedResult> ParsedResults;
	int OCRExitCode;
	boolean IsErroredOnProcessing;
	String ErrorMessage;
	String ErrorDetails;
	long ProcessingTimeInMilliseconds;
	
	public List<ParsedResult> getParsedResults(){
		return ParsedResults;
	}
	public void setParsedResults(List<ParsedResult> ParsedResults){
		this.ParsedResults = ParsedResults;
	}
	public long getProcessingTimeInMilliseconds() {
		return ProcessingTimeInMilliseconds;
	}
	public void setProcessingTimeInMilliseconds(long ProcessingTimeInMilliseconds) {
		this.ProcessingTimeInMilliseconds = ProcessingTimeInMilliseconds;
	}
	public String getErrorDetails(){
		return ErrorDetails;
	}
	public void setErrorDetails(String ErrorDetails){
		this.ErrorDetails = ErrorDetails;
	}
	public String getErrorMessage(){
		return ErrorMessage;
	}
	
	public void setErrorMessage(String ErrorMessage){
		this.ErrorMessage = ErrorMessage;
	}
	public boolean getIsErroredOnProcessing(){
		return IsErroredOnProcessing;
	}
	
	public void setIsErroredOnProcessing(boolean IsErroredOnProcessing){
		this.IsErroredOnProcessing = IsErroredOnProcessing;
	}
	public int getOCRExitCode(){
		return OCRExitCode;
	}
	
	public void setOCRExitCode(int OCRExitCode){
		this.OCRExitCode = OCRExitCode;
	}
}
