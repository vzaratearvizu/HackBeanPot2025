import tester.Tester;

import java.awt.Color;
import javalib.worldimages.*;
import javalib.funworld.*;

// DON'T FORGET TO ADD THE TEMPLATES

//represents a list of words
interface ILoWord {

	Util u = new Util();
	int SCREEN_WIDTH = 600;
	int SCREEN_HEIGHT = 800;
	int MAX_COUNT = 6;
	int fallingSpeed = 50;

	// active words in the ILoWord are reduced by removing the first letter
	// if the given string matches the first letter
	ILoWord checkAndReduce(String letter);

	// add a given IWord to the end of this ILoWord
	ILoWord addToEnd(IWord word);

	// filters out IWords that are an empty string
	ILoWord filterOutEmpties();

	// draws all words
	WorldScene draw(WorldScene ws);

	// updates the positions of all the words in the list
	ILoWord updatePosition();

	// activates word that matches string
	ILoWord activate(String key);

	// adds a total of 7 words on the list
	ILoWord addWord();

	// counts words and highkey creates them too
	ILoWord addWordAcc(int acc);

	// returns true if any of the words are at the bottom of the screen
	boolean wordsAtBottom();
	
	// returns true if the list is successfully empty
	boolean levelCompleted();
}

//represents an empty list of words
class MtLoWord implements ILoWord {

	// returns an MtLoWord if the list is empty
	public ILoWord checkAndReduce(String letter) {
		return new MtLoWord();
	}

	// adds the given word to the empty list
	public ILoWord addToEnd(IWord word) {
		return new ConsLoWord(word, new MtLoWord());
	}

	// returns an empty list since there are no empties to filter
	public ILoWord filterOutEmpties() {
		return new MtLoWord();
	}

	// draws the WorldScene with no words
	public WorldScene draw(WorldScene ws) {
		return ws;
	}

	// update positions of an empty list
	public ILoWord updatePosition() {
		return new MtLoWord();
	}

	// empty list so nothing should activate
	public ILoWord activate(String key) {
		return this;
	}

	// adds a total of 7 words on the list
	public ILoWord addWord() {
		return this.addWordAcc(0);
	}

	// 7 new words
	public ILoWord addWordAcc(int acc) {
		if (acc <= MAX_COUNT) {
			IWord newWord = new InactiveWord(u.wordGenerate(), u.randXPos(), -acc * fallingSpeed);
			return this.addToEnd(newWord).addWordAcc(acc + 1);
		} else {
			return this;
		}
	}

	// no words at bottom for empty list so returns false
	public boolean wordsAtBottom() {
		return false;
	}

	// returns true since the list has been succesesfully cleared
	public boolean levelCompleted() {
		return true;
	}
}

//represents a non-empty list of words
class ConsLoWord implements ILoWord {
	IWord first;
	ILoWord rest;

	ConsLoWord(IWord first, ILoWord rest) {
		this.first = first;
		this.rest = rest;
	}

	// removes the first letter from active words that start with the given letter
	public ILoWord checkAndReduce(String letter) {
		return new ConsLoWord(this.first.activeReduce(letter), this.rest);
	}

	// Adds word to end of list
	public ILoWord addToEnd(IWord word) {
		return new ConsLoWord(this.first, this.rest.addToEnd(word));
	}

	// filters out the empty IWords
	public ILoWord filterOutEmpties() {
		if (this.first.isEmpty()) {
			return this.rest.filterOutEmpties();
		} else {
			return new ConsLoWord(this.first, this.rest.filterOutEmpties());
		}
	}

	// draws a list of words
	public WorldScene draw(WorldScene ws) {
		return this.first.draw(this.rest.draw(ws));
	}

	// updates the positions of the list of words
	public ILoWord updatePosition() {
		return new ConsLoWord(this.first.updatePosition(), this.rest.updatePosition());
	}

	// turns the desired word into an active word
	public ILoWord activate(String key) {
		if (this.first.compareFirst(key)) {
			return new ConsLoWord(this.first.activate(), this.rest);
		} else {
			return (this.rest.activate(key)).addToEnd(this.first);
		}
	}

	// adds a total of 7 words on the list
	public ILoWord addWord() {
		return this.addWordAcc(0);
	}

	// creates 7 new words
	public ILoWord addWordAcc(int acc) {
		if (acc <= MAX_COUNT) {
			IWord newWord = new InactiveWord(u.wordGenerate(), u.randXPos(), -acc * fallingSpeed);
			return this.addToEnd(newWord).addWordAcc(acc + 1);
		} else {
			return this;
		}
	}

	// returns true if any of the words are at the bottom of the screen
	public boolean wordsAtBottom() {
		return this.first.wordsAtBottom() || this.rest.wordsAtBottom();
	}

	// returns false since the list has not been successfully cleared
	public boolean levelCompleted() {
		return false;
	}
}

//represents a word in the ZType game
interface IWord {

	int SCREEN_WIDTH = 600;
	int SCREEN_HEIGHT = 800;

	// removes first letter from active words if it starts with given letter
	IWord activeReduce(String letter);

	// returns True if the word is an empty
	boolean isEmpty();

	// draws each of the words in a WorldImage
	WorldScene draw(WorldScene ws);

	// updates the position of a word
	IWord updatePosition();

	// turns inactive word into active word
	IWord activate();

	// compare first letter to given letter
	boolean compareFirst(String key);

	// returns true if the word is at the bottom of the screen
	boolean wordsAtBottom();
}

//represents an active word in the ZType game
class ActiveWord implements IWord {
	String word;
	int x;
	int y;

	ActiveWord(String word, int x, int y) {
		this.word = word;
		this.x = x;
		this.y = y;
	}

	// removes the first letter of the word if it matches the given word
	public IWord activeReduce(String letter) {
		if ((this.word.length() > 0) && (this.word.substring(0, 1).equals(letter))) {
			return new ActiveWord(this.word.substring(1), this.x, this.y);
		} else {
			return this;
		}
	}

	// returns true if the word is an empty
	public boolean isEmpty() {
		return this.word.equals("");
	}

	// draw this task as text
	public WorldScene draw(WorldScene ws) {
		WorldImage imported = new FrozenImage(
				new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\trash bag.png"));
		WorldImage trashWithText = new OverlayImage(new TextImage(this.word, 20, Color.GREEN), imported);
		return ws.placeImageXY(trashWithText, this.x, this.y);
	}

	// updates the position of a IWord (currently only moves downwards)
	public IWord updatePosition() {
		return new ActiveWord(this.word, this.x, this.y + 20);
	}

	// activates already active word into itself
	public IWord activate() {
		return this;
	}

	// returns true since already active and is compared in activeReduce
	public boolean compareFirst(String key) {
		return true;
	}

	// returns true if word is at bottom
	public boolean wordsAtBottom() {
		return this.y == SCREEN_HEIGHT;
	}
}

//represents an inactive word in the ZType game
class InactiveWord implements IWord {
	String word;
	int x;
	int y;

	InactiveWord(String word, int x, int y) {
		this.word = word;
		this.x = x;
		this.y = y;
	}

	// returns the word unchanged
	public IWord activeReduce(String letter) {
		return this;
	}

	// returns true if the word is an empty
	public boolean isEmpty() {
		return this.word.equals("");
	}

	// draw this task as text
	public WorldScene draw(WorldScene ws) {
		WorldImage imported = new FrozenImage(
				new FromFileImage("C:\\Users\\yimin\\Downloads\\Anti-Polluter Scooter\\trash bag.png"));
		WorldImage trashWithText = new OverlayImage(new TextImage(this.word, 20, Color.CYAN), imported);
		return ws.placeImageXY(trashWithText, this.x, this.y);
	}

	// updates position of inactive word (currently only moves downwards)
	public IWord updatePosition() {
		return new InactiveWord(this.word, this.x, this.y + 20);
	}

	// changes an inactive word into an active word
	public IWord activate() {
		return new ActiveWord(this.word, this.x, this.y);
	}

	// compares first letter to given key
	public boolean compareFirst(String key) {
		return (this.word.length() > 0) && (this.word.substring(0, 1).equals(key));
	}

	// returns true if word is at bottom
	public boolean wordsAtBottom() {
		return this.y == SCREEN_HEIGHT;
	}
}

class Examples {
	IWord hello = new ActiveWord("hello", 1, 1);
	IWord my = new InactiveWord("my", 0, 0);
	IWord name = new ActiveWord("name", 100, 100);
	IWord is = new InactiveWord("is", 5, 0);
	IWord jello = new ActiveWord("jello", 1, 1);
	IWord emptyWord = new ActiveWord("", 0, 0);
	IWord upperWord = new ActiveWord("APPLE", 10, 40);

	ILoWord empty = new MtLoWord();
	ILoWord sentence1 = new ConsLoWord(this.hello, this.empty);
	ILoWord sentence2 = new ConsLoWord(this.my, this.sentence1);
	ILoWord sentence3 = new ConsLoWord(this.name, this.sentence2);
	ILoWord sentence4 = new ConsLoWord(this.is, this.sentence3);
	ILoWord sentence5 = new ConsLoWord(this.jello, this.sentence4);
	ILoWord orderedList = new ConsLoWord(this.hello,
			new ConsLoWord(this.is, new ConsLoWord(this.my, new ConsLoWord(this.name, new MtLoWord()))));
	ILoWord upperIncluded = new ConsLoWord(this.upperWord, sentence5);

	IWord one = new ActiveWord("one", 10, 15);
	IWord two = new InactiveWord("two", 20, 25);
	IWord three = new ActiveWord("three", 30, 35);
	IWord four = new InactiveWord("four", 40, 45);
	IWord five = new InactiveWord("five", 40, 45);
	IWord six = new InactiveWord("six", 40, 45);
	IWord seven = new InactiveWord("seven", 40, 45);

	ILoWord listOne = new ConsLoWord(this.one, this.empty);
	ILoWord listTwo = new ConsLoWord(this.two, this.listOne);
	ILoWord listTwoTwo = new ConsLoWord(this.two, this.empty);
	ILoWord listThree = new ConsLoWord(this.three, this.listTwo);
	ILoWord listFour = new ConsLoWord(this.four, this.listThree);
	ILoWord listFive = new ConsLoWord(this.five, this.listFour);
	ILoWord listSix = new ConsLoWord(this.six, this.listFive);
	ILoWord orderedOneFour = new ConsLoWord(this.five, new ConsLoWord(this.four,
			new ConsLoWord(this.one, new ConsLoWord(this.three, new ConsLoWord(this.two, new MtLoWord())))));
	ILoWord emptiesList = new ConsLoWord(this.emptyWord, new ConsLoWord(this.one,
			new ConsLoWord(this.emptyWord, new ConsLoWord(this.emptyWord, new ConsLoWord(this.two, new MtLoWord())))));

	WorldScene ws_ex = new WorldScene(100, 100);

	// tests ILoWord.checkAndReduce(String)
	boolean testCheckAndReduce(Tester t) {
		// should only change three and not two, since three is active word
		return t.checkExpect(this.orderedOneFour.checkAndReduce("t"),
				new ConsLoWord(this.five,
						new ConsLoWord(this.four,
								new ConsLoWord(this.one,
										new ConsLoWord(new ActiveWord("hree", 30, 35),
												new ConsLoWord(this.two, new MtLoWord()))))))
				&& t.checkExpect(this.empty.checkAndReduce("a"), new MtLoWord())
				&& t.checkExpect(this.orderedList.checkAndReduce(""), this.orderedList);
	}

	// tests for ILoWord.addToEnd(IWord)
	boolean testAddToEnd(Tester t) {
		return t.checkExpect(this.orderedOneFour.addToEnd(this.one),
				new ConsLoWord(this.five,
						new ConsLoWord(this.four,
								new ConsLoWord(this.one,
										new ConsLoWord(this.three,
												new ConsLoWord(this.two, new ConsLoWord(this.one, new MtLoWord())))))))
				&& t.checkExpect(this.empty.addToEnd(this.one), new ConsLoWord(this.one, new MtLoWord()))
				&& t.checkExpect(this.orderedList.addToEnd(this.emptyWord),
						new ConsLoWord(this.hello, new ConsLoWord(this.is, new ConsLoWord(this.my,
								new ConsLoWord(this.name, new ConsLoWord(this.emptyWord, new MtLoWord()))))));
	}

	// tests for ILoWord.filterOutEmpties()
	boolean testFilterOutEmpties(Tester t) {
		return t.checkExpect(this.empty.filterOutEmpties(), new MtLoWord())
				&& t.checkExpect(this.orderedList.filterOutEmpties(), this.orderedList)
				&& t.checkExpect(this.emptiesList.filterOutEmpties(),
						new ConsLoWord(this.one, new ConsLoWord(this.two, new MtLoWord())));
	}

	// tests for ILoWord.draw(WorldScene)
	boolean testDraw(Tester t) {
		return t.checkExpect(this.empty.draw(this.ws_ex), new WorldScene(100, 100))
				&& t.checkExpect(this.listOne.draw(this.ws_ex),
						ws_ex.placeImageXY(new TextImage("one", 20, Color.GREEN), 10, 15))
				&& t.checkExpect(this.listTwoTwo.draw(this.ws_ex),
						ws_ex.placeImageXY(new TextImage("two", 20, Color.YELLOW), 20, 25));
	}

	// tests for methods of IWord

	// tests for IWord.activeReduce(String)
	boolean testActiveReduce(Tester t) {
		return t.checkExpect(this.one.activeReduce("o"), new ActiveWord("ne", 10, 15))
				&& t.checkExpect(this.one.activeReduce("O"), this.one)
				&& t.checkExpect(this.one.activeReduce(""), this.one)
				&& t.checkExpect(this.two.activeReduce("t"), this.two)
				&& t.checkExpect(this.emptyWord.activeReduce(""), this.emptyWord);
	}

	// tests for IWord.isEmpty()
	boolean testWordIsEmpty(Tester t) {
		return t.checkExpect(this.emptyWord.isEmpty(), true) && t.checkExpect(this.one.isEmpty(), false);
	}

	// tests for IWord.draw(WorldScene ws);
	boolean testWordDraw(Tester t) {
		return t.checkExpect(this.emptyWord.draw(this.ws_ex), new WorldScene(100, 100))
				&& t.checkExpect(this.one.draw(this.ws_ex),
						ws_ex.placeImageXY(new TextImage("one", 20, Color.GREEN), 10, 15))
				&& t.checkExpect(this.two.draw(this.ws_ex),
						ws_ex.placeImageXY(new TextImage("two", 20, Color.YELLOW), 20, 25));
	}
}
