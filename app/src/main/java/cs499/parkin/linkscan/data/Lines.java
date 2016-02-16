package cs499.parkin.linkscan.data;

import java.util.List;

/**
 * Created by parkin on 2/6/2016.
 */
public class Lines {

    private List<ImageWords> Words;
    private int MaxHeight;
    private int MinTop;

    public List<ImageWords> getWords() {
        return Words;
    }

    public void setWords(List<ImageWords> words) {
        Words = words;
    }

    public int getMaxHeight() {
        return MaxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        MaxHeight = maxHeight;
    }

    public int getMinTop() {
        return MinTop;
    }

    public void setMinTop(int minTop) {
        MinTop = minTop;
    }
}
