package ocr.data;

public class Word {
	String WordText;
	int Left;
	int Top;
	int Height;
	int Width;
	
	public int getHeight() {
		return Height;
	}
	public int getLeft() {
		return Left;
	}
	public int getTop() {
		return Top;
	}
	public int getWidth() {
		return Width;
	}
	public String getWordText() {
		return WordText;
	}
	public void setHeight(int height) {
		Height = height;
	}
	public void setLeft(int left) {
		Left = left;
	}
	public void setTop(int top) {
		Top = top;
	}
	public void setWidth(int width) {
		Width = width;
	}
	public void setWordText(String wordText) {
		WordText = wordText;
	}
}
