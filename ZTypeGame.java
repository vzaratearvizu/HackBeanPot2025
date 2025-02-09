import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color; // general colors (as triples of red,green,blue values)
						// and predefined colors (Red, Green, Yellow, Blue, Black, White)
import java.util.Random; // can create random

class ZTypeWorld extends World {
	ILoWord words;
	int SCREEN_HEIGHT = 1000;
	int SCREEN_WIDTH = 600;
	int WORD_COUNT = 5; // vary word count for different levels?

	Util u = new Util();

	ZTypeWorld(ILoWord words) {

		this.words = words;
	}

	// draws the words onto the background
	public WorldScene makeScene() {
		return this.words.draw(new WorldScene(this.SCREEN_HEIGHT, this.SCREEN_WIDTH));
	}

	// adds a total of six words on the list
	ILoWord addWord() {
		return this.words.addWordAcc(0);
	}
	
a
	// move the words on the scene
	public World onTick() {
		// every time onTick runs it generates another 6 words
		// we want it to generate 6 words at once
		// or, more ideally, generate 6 words total, but not at once (but we dk how to do that)
		ILoWord listOfWords = this.addWord();
		return new ZTypeWorld(listOfWords.updatePosition().filterOutEmpties());

	}
	
	// why does the test return ZTypeWorld but the regular onTick() return just World
	public ZTypeWorld onTickForTesting() {
		return new ZTypeWorld(new MtLoWord());
	}
	
	// removes first matching letter of typed letter
	public ZTypeWorld onKeyEvent(String key) {
		return new ZTypeWorld(this.words.activate(key).checkAndReduce(key));
	}
	
	// 
}

class Util {
	Random r = new Random();
	int totalLetters = 6;

	String wordGenerate() {
		return wordGenerateAcc("", this.totalLetters); 
	}
	
	String wordGenerateAcc(String acc, int letters) {
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();

		char letter = abc.charAt(r.nextInt(abc.length()));
		
		if (letters > 0) {
			return wordGenerateAcc(acc + letter, letters-1); }
		else {
			return acc;
		}
	}
	
	int randXPos() {
		return r.nextInt(600);
	}
}

class ExamplesGame {
	
	ILoWord mt = new MtLoWord();

	boolean testBigBang(Tester t) {
		ZTypeWorld world = new ZTypeWorld(this.mt);
		int worldWidth = 1000;
		int worldHeight = 600;
		double tickRate = 1;
		return world.bigBang(worldWidth, worldHeight, tickRate);
	}
	
	boolean testWordGenerate(Tester t) {
		
		return false;
	}
	
}