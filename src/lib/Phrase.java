package lib;

public class Phrase {
	private String phrase;
	private float time;
	
	public Phrase(String phrase, float time) {
		this.phrase = phrase;
		this.time = time;
	}
	
	public String getPhrase() {
		return this.phrase;
	}
	
	public float getTime() {
		return this.time;
	}
	
	public void setString(String phrase) {
		this.phrase = phrase;
	}
	
	public void setTime(float time) {
		this.time = time;
	}
}
