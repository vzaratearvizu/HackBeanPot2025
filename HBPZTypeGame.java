import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color; // general colors (as triples of red,green,blue values)
						// and predefined colors (Red, Green, Yellow, Blue, Black, White)
import java.util.Random; // can create random

class ZTypeWorld extends World {
	ILoWord words;
	boolean isStarted;
	int level;
	int wordCount;
	int SCREEN_HEIGHT = 800;
	int SCREEN_WIDTH = 600;
	int WORD_COUNT = 5; // vary word count for different levels?
	int FALLING_SPEED = 25;

	Util u = new Util();

	ZTypeWorld(ILoWord words, boolean isStarted, int wordCount, int level) {

		this.words = words;
		this.isStarted = isStarted;
		this.wordCount = wordCount;
		this.level = level;

	}

	// draws the words onto the background
	public WorldScene makeScene() {
		if (!this.isStarted) {
			return this.startScene();
		} else {
			if (this.level == 1) {
				WorldImage imported = new FrozenImage(
						new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\2.png"));
				WorldScene background = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported,
						SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
				return this.words.draw(background);
				// return this.words.draw(new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT));
			} else if (this.level == 2) {
				WorldImage imported = new FrozenImage(
						new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\3.png"));
				WorldScene background = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported,
						SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
				return this.words.draw(background);
			} else if (this.level == 3) {
				WorldImage imported = new FrozenImage(
						new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\4.png"));
				WorldScene background = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported,
						SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
				return this.words.draw(background);
			} else if (this.level == 4) {
				WorldImage imported = new FrozenImage(
						new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\5.png"));
				WorldScene background = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported,
						SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
				return this.words.draw(background);
			} else {
				WorldImage imported = new FrozenImage(
						new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\6.png"));
				WorldScene background = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported,
						SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
				return this.words.draw(background);
			} 
		}
	}

	// move the words on the scene
	public World onTick() {
		// every time onTick runs it generates another 6 words
		// we want it to generate 6 words at once
		// or, more ideally, generate 6 words total, but not at once (but we dk how to
		// do that)
		if (this.isStarted) {
			if (this.wordCount <= this.level+2) {
				IWord newWord = new InactiveWord(u.wordGenerate(), u.randXPos(), -this.wordCount * FALLING_SPEED);
				return (new ZTypeWorld(this.words.addToEnd(newWord).updatePosition().filterOutEmpties(), this.isStarted,
						this.wordCount + 1, this.level)).levelCompleted();
			}
			else {
				return (new ZTypeWorld(this.words.updatePosition().filterOutEmpties(), this.isStarted, this.wordCount,
						this.level)).levelCompleted();
			}
		} else {
			return this;
		}
	}

	// if the level has been cleared, add one to the field level
	public ZTypeWorld levelCompleted() {
		if (this.words.levelCompleted()) {
			return new ZTypeWorld(this.words, this.isStarted, 0, (this.level + 1));
		} else {
			return this;
		}
	}

	// why does the test return ZTypeWorld but the regular onTick() return just
	// World
	public ZTypeWorld onTickForTesting() {
		return new ZTypeWorld(new MtLoWord(), this.isStarted, 0, this.level);
	}

	// if space bar clicked, isStarted becomes true
	public ZTypeWorld onKeyEvent(String key) {
		if (key.equals("enter")) {
			return new ZTypeWorld(this.words, true, 0, this.level);
		} else {
			return this;
		}
	}

	// removes first matching letter of typed letter
	public ZTypeWorld onKeyReleased(String key) {
		return new ZTypeWorld(this.words.activate(key).checkAndReduce(key), this.isStarted, this.wordCount, this.level);
	}

	/*
	 * // if mouse is clicked, sets isStarted to true public ZTypeWorld
	 * onMouseClicked(@ Posn(0, 0)) { return new ZTypeWorld(this.words, true); }
	 */

	// ends game when it hits the bottom
	public WorldEnd worldEnds() {
		if (this.youLost() || this.level > 5) {
			if (this.youLost()) {
				return new WorldEnd(true, this.losingScene()); }
			else {
				return new WorldEnd(true, this.winningScene()); 
			}
		} else {
			return new WorldEnd(false, this.makeScene());
		}
	}

	// returns true if word hits the bottom
	boolean youLost() {
		return this.words.wordsAtBottom();
	}

	// scene when you lost
	WorldScene losingScene() {
		WorldImage imported = new FrozenImage(
				new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\lose.png"));
		WorldScene background = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported,
				SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
		return this.words.draw(background);
	}
	
	// scene when you win
	WorldScene winningScene() {
		WorldImage imported = new FrozenImage(
				new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\win.png"));
		WorldScene background = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported,
				SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
		return this.words.draw(background);
	}

	/*
	 * // returns true when someone has typed "start" boolean doneTypingStart() {
	 * return this.words.doneTypingStart(); }
	 * 
	 */

	// scene when starting
	WorldScene startScene() {
		WorldImage imported = new FrozenImage(
				new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\1.png"));
		return new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(imported, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
	}

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
			return wordGenerateAcc(acc + letter, letters - 1);
		} else {
			return acc;
		}
	}

	int randXPos() {
		return r.nextInt(35, 565);
	}

}

class ExamplesGame {

	ILoWord mt = new MtLoWord();
	ILoWord sixWords = this.mt.addWord();
	ILoWord startList = new ConsLoWord(new ActiveWord("start", 50, 50), this.mt);
	ILoWord listOne = this.mt.addWord();
	ILoWord listTwo = this.mt.addWord();
	ILoWord listThree = this.mt.addWord();

	boolean testBigBang(Tester t) {
		ZTypeWorld world = new ZTypeWorld(this.mt, false, 1, 1);
		int worldWidth = 600;
		int worldHeight = 800;
		double tickRate = 1;
		return world.bigBang(worldWidth, worldHeight, tickRate);
	}

	boolean testWordGenerate(Tester t) {

		return false;
	}

}