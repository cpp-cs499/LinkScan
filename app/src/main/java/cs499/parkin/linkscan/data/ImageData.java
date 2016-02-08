package cs499.parkin.linkscan.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by parkin on 2/6/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageData {
    private List<ParsedResults> parsedResults;
    private int ocrExitCode;
    private boolean isErroredOnProcessing;
    private String errorMessage;
    private String errorDetails;
    private int processingTimeInMilliseconds;


    public List<ParsedResults> getParsedResults() {
        return parsedResults;
    }

    public void setParsedResults(List<ParsedResults> parsedResults) {
        this.parsedResults = parsedResults;
    }

    public int getOcrExitCode() {
        return ocrExitCode;
    }

    public void setOcrExitCode(int ocrExitCode) {
        this.ocrExitCode = ocrExitCode;
    }

    public boolean isErroredOnProcessing() {
        return isErroredOnProcessing;
    }

    public void setIsErroredOnProcessing(boolean isErroredOnProcessing) {
        this.isErroredOnProcessing = isErroredOnProcessing;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public int getProcessingTimeInMilliseconds() {
        return processingTimeInMilliseconds;
    }

    public void setProcessingTimeInMilliseconds(int processingTimeInMilliseconds) {
        this.processingTimeInMilliseconds = processingTimeInMilliseconds;
    }
}
