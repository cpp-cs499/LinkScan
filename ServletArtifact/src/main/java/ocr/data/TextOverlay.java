package ocr.data;

import java.util.List;

public class TextOverlay {
	List<Line> Lines;
	boolean HasOverlay;
	String Message;
	
	public List<Line> getLines() {
		return Lines;
	}
	public String getMessage() {
		return Message;
	}
	public boolean getHasOverlay(){
		return HasOverlay;
	}
	public void setHasOverlay(boolean hasOverlay) {
		HasOverlay = hasOverlay;
	}
	public void setLines(List<Line> lines) {
		Lines = lines;
	}
	public void setMessage(String message) {
		Message = message;
	}
}
