package cs499.parkin.linkscan.data;

import java.util.List;

/**
 * Created by parkin on 2/6/2016.
 */
public class TextOverlay {

    private List<Lines> lines;
    private boolean hasOverlay;
    private String Message;

    public List<Lines> getLines() {
        return lines;
    }

    public void setLines(List<Lines> lines) {
        this.lines = lines;
    }

    public boolean isHasOverlay() {
        return hasOverlay;
    }

    public void setHasOverlay(boolean hasOverlay) {
        this.hasOverlay = hasOverlay;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
