package cs499.parkin.linkscan.data;

/**
 * Created by parkin on 2/6/2016.
 */
public class ParsedResults {
    private TextOverlay TextOverlay;
    private int FileParseExitCode;
    private String ParsedText;
    private String ErrorMessage;
    private String ErrorDetails;

    public cs499.parkin.linkscan.data.TextOverlay getTextOverlay() {
        return TextOverlay;
    }

    public void setTextOverlay(cs499.parkin.linkscan.data.TextOverlay textOverlay) {
        TextOverlay = textOverlay;
    }

    public int getFileParseExitCode() {
        return FileParseExitCode;
    }

    public void setFileParseExitCode(int fileParseExitCode) {
        FileParseExitCode = fileParseExitCode;
    }

    public String getParsedText() {
        return ParsedText;
    }

    public void setParsedText(String parsedText) {
        ParsedText = parsedText;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return ErrorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        ErrorDetails = errorDetails;
    }
}
