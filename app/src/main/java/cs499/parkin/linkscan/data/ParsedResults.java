package cs499.parkin.linkscan.data;

/**
 * Created by parkin on 2/6/2016.
 */
public class ParsedResults {
    private TextOverlay textOverlay;
    private int fileParseExitCode;
    private String parsedText;
    private String errorMessage;
    private String errorDetails;

    public TextOverlay getTextOverlay() {
        return textOverlay;
    }

    public void setTextOverlay(TextOverlay textOverlay) {
        this.textOverlay = textOverlay;
    }

    public int getFileParseExitCode() {
        return fileParseExitCode;
    }

    public void setFileParseExitCode(int fileParseExitCode) {
        this.fileParseExitCode = fileParseExitCode;
    }

    public String getParsedText() {
        return parsedText;
    }

    public void setParsedText(String parsedText) {
        this.parsedText = parsedText;
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
}
