package cs499.parkin.linkscan.data;

import java.util.List;

/**
 * Created by parkin on 2/6/2016.
 */
public class TextOverlay {

    private List<Lines> Lines;
    private boolean HasOverlay;
    private String Message;

    public List<cs499.parkin.linkscan.data.Lines> getLines() {
        return Lines;
    }

    public void setLines(List<cs499.parkin.linkscan.data.Lines> lines) {
        Lines = lines;
    }

    public boolean isHasOverlay() {
        return HasOverlay;
    }

    public void setHasOverlay(boolean hasOverlay) {
        HasOverlay = hasOverlay;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
