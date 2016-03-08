package ocr.data;

import java.util.List;

public class Line {
	List<Word> Words;
	int MaxHeight;
	int MinTop;
	
	public int getMaxHeight() {
		return MaxHeight;
	}
	public int getMinTop() {
		return MinTop;
	}
	public List<Word> getWords() {
		return Words;
	}
	public void setMaxHeight(int maxHeight) {
		MaxHeight = maxHeight;
	}
	public void setMinTop(int minTop) {
		MinTop = minTop;
	}
	public void setWords(List<Word> words) {
		Words = words;
	}
}
