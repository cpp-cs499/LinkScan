package cs499.parkin.linkscan.data;

/**
 * Created by parkin on 2/18/2016.
 */

import java.util.List;

public class ImageDataHolder {

    List<String> textList;
    int exitCode;

    public int getExitCode() {
        return exitCode;
    }
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
    public List<String> getTextList(){
        return textList;
    }

    public void setTextList(List<String> input){
        textList = input;
    }
}
