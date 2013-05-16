package cse.ust.twittermap;

import java.util.List;

public class WordBlock extends Object {
	public String word;
	public int width;
	public int height;
	public int x;
	public int y;
	
	private int max(int a, int b){
		return a>b?a:b;
	}
	
	private int min(int a, int b){
		return a<b?a:b;
	}
	
	public WordBlock(String word, Integer fontSize, Integer x, Integer y){
		this.word = word;
		this.height = fontSize + 2;
		this.width = word.length()*fontSize*3/5 +2;
		this.x = x-1;
		this.y = y+1;
	}
	
	public Boolean overlap(WordBlock that){
		if(max(this.x,that.x)<=min(this.x+this.width,that.x+that.width) && max(this.y-this.height,that.y-that.height)<=min(this.y,that.y))
			return true;
		else
			return false;
	}
	
	public Boolean overlap(List<WordBlock> list){
		for(WordBlock wb : list){
			if(overlap(wb))
				return true;
		}
		return false;
	}
}
