package ocr.data;

public class ParsedResult {
	TextOverlay TextOverlay;
	int FileParseExitCode;
	String ParsedText;
	
	public int getFileParseExitCode() {
		return FileParseExitCode;
	}
	public TextOverlay getTextOverlay() {
		return TextOverlay;
	}
	public String getParsedText() {
		return ParsedText;
	}
	public void setFileParseExitCode(int fileParseExitCode) {
		FileParseExitCode = fileParseExitCode;
	}
	public void setParsedText(String parsedText) {
		ParsedText = parsedText;
	}
	public void setTextOverlay(TextOverlay textOverlay) {
		TextOverlay = textOverlay;
	}
}
