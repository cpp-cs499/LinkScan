package cs499.parkin.linkscan.data;

import java.util.List;

/**
 * Created by parkin on 2/6/2016.
 */
public class Lines {

    private List<ImageWords> words;
    private int maxHeight;
    private int minTop;

    public List<ImageWords> getWords() {
        return words;
    }

    public void setWords(List<ImageWords> words) {
        this.words = words;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMinTop() {
        return minTop;
    }

    public void setMinTop(int minTop) {
        this.minTop = minTop;
    }
}
