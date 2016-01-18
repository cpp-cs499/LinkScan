package cs499.parkin.linkscan;

/**
 * Created by parkin on 1/17/2016.
 */
public class DataHolder {
    private String data;
    private boolean isProcessing = false;
    public String getData() {return data;}
    public void setData(String data){this.data = data;}
    public boolean isProcessing(){ return this.isProcessing;}
    public void setProcessing(boolean bol){this.isProcessing = bol;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

}
