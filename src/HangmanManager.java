/*  Student information for assignment:
 *
 *  On my honor, <NAME>, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  Name: ian torres
 *  email address: ian_tj_04@utexas.edu
 *  UTEID: it4398
 *  Section 5 digit ID:
 *  Grader name: brad
 *  Number of slip days used on this assignment:
 */

// add imports as necessary

import jdk.jshell.spi.SPIResolutionException;

import java.util.*;

/**
 * Manages the details of EvilHangman. This class keeps
 * tracks of the possible words from a dictionary during
 * rounds of hangman, based on guesses so far.
 *
 */
public class HangmanManager {

    private static final char DASH = '-';

    // instance variables / fields
    private final Set<String> ORIGINAL_WORDS;
    private ArrayList<String> currentWords;
    private String secretPattern;
    private ArrayList<String> usedGuess;
    private boolean debug;
    private int wrongGuessesAllowed;
    private HangmanDifficulty hangmanDifficulty;
    private int timesGuessed;

    // remember to make indentation spaces not whole indentions.
    // also remember to make lines less than 100 char

    /**
     * Create a new HangmanManager from the provided set of words and phrases.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     * @param debugOn true if we should print out debugging to System.out.
     */

    //pre: words != null, words.size() > 0
    //post: copy the set (dictionary) and set debug to debugOn value.
    public HangmanManager(Set<String> words, boolean debugOn) {
        if (words == null || words.size() == 0){
            throw new IllegalArgumentException("HangmanManager: words must not be null and length" +
                    " must be greater than 0");
        }
        ORIGINAL_WORDS = words;
        debug = debugOn;
    }

    /**
     * Create a new HangmanManager from the provided set of words and phrases.
     * Debugging is off.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     */

    //pre: words != null, words.size() > 0
    //post: copy the set (dictionary) and set debug to false.
    public HangmanManager(Set<String> words) {
        if (words == null || words.size() == 0){
            throw new IllegalArgumentException("HangmanManager: words must not be null and length" +
                    " must be greater than 0");
        }
        ORIGINAL_WORDS = words;
        debug = false;
    }


    /**
     * Get the number of words in this HangmanManager of the given length.
     * pre: none
     * @param length The given length to check.
     * @return the number of words in the original Dictionary
     * with the given length
     */

    // post: return number of words in original Dictionary with given length
    public int numWords(int length) {
        int count = 0;
        for (String word: ORIGINAL_WORDS){
            if (word.length() == length){
                count++;
            }
        }
        return count;
    }


    /*
     * Get for a new round of Hangman. Think of a round as a
     * complete game of Hangman.
     * @param wordLen the length of the word to pick this time.
     * numWords(wordLen) > 0
     * @param numGuesses the number of wrong guesses before the
     * player loses the round. numGuesses >= 1
     * @param diff The difficulty for this round.
     */

    /**
     * pre: numWords(wordLeng) > 0 && numGuesses >= 1
     * post: Reset all values for HangManager
     */
    public void prepForRound(int wordLen, int numGuesses, HangmanDifficulty diff) {
        if (numGuesses < 1 || wordLen <= 0) {
            throw new IllegalArgumentException("prepForRound: numGuesses must be less than 1 and " +
                    "wordLen must be greater than 0");
        }
        wrongGuessesAllowed = numGuesses;
        hangmanDifficulty = diff;
        secretPattern = "";
        timesGuessed = 0;

        usedGuess = new ArrayList<>();
        currentWords = new ArrayList<>();

        // No issue with efficiency as words that are extremely big don't exist.
        for (int i = 0; i < wordLen; i++){
            secretPattern += DASH;
        }

        // Reset available words at start.
        for (String word : ORIGINAL_WORDS) {
            if (word.length() == wordLen) {
                currentWords.add(word);
            }
        }
    }


    /**
     * The number of words still possible (live) based on the guesses so far.
     *  Guesses will eliminate possible words.
     * @return the number of words that are still possibilities based on the
     * original dictionary and the guesses so far.
     */

    //post: return number of words that could still work.
    public int numWordsCurrent() {
        int count = 0;
        for(String i : currentWords){
            count++;
        }
        return count;
    }


    /**
     * Get the number of wrong guesses the user has left in
     * this round (game) of Hangman.
     * @return the number of wrong guesses the user has left
     * in this round (game) of Hangman.
     */

    //Return number of wrong guesses in round.
    public int getGuessesLeft() {
        return wrongGuessesAllowed;
    }


    /**
     * Return a String that contains the letters the user has guessed
     * so far during this round.
     * The characters in the String are in alphabetical order.
     * The String is in the form [let1, let2, let3, ... letN].
     * For example [a, c, e, s, t, z]
     * @return a String that contains the letters the user
     * has guessed so far during this round.
     */
    public String getGuessesMade() {
        Collections.sort(usedGuess);
        return usedGuess.toString();
    }


    /**
     * Check the status of a character.
     * @param guess The characater to check.
     * @return true if guess has been used or guessed this round of Hangman,
     * false otherwise.
     */

    //post: return whether a guess has been used or not.
    public boolean alreadyGuessed(char guess) {
        String checkGuess =  (guess + "").toLowerCase();
        for(String i: usedGuess){
            if(checkGuess.equals(i)){
                return true;
            }
        }
        return false;
    }


    /**
     * Get the current pattern. The pattern contains '-''s for
     * unrevealed (or guessed) characters and the actual character 
     * for "correctly guessed" characters.
     * @return the current pattern.
     */

    // Post: return current secret pattern.
    public String getPattern() {
        return secretPattern;
    }


    /**
     * Update the game status (pattern, wrong guesses, word list),
     * based on the give guess.
     * @param guess pre: !alreadyGuessed(ch), the current guessed character
     * @return return a tree map with the resulting patterns and the number of
     * words in each of the new patterns.
     * The return value is for testing and debugging purposes.
     */

    //pre: Guess has not been picked before
    //post: return a tree map with the resulting patterns and number of words in each pattern.
    public TreeMap<String, Integer> makeGuess(char guess) {
        if (alreadyGuessed(guess)){
            throw new IllegalArgumentException("TreeMap: cannot pick same guess as " +
                    "before.");
        }
        timesGuessed++;

        usedGuess.add(guess + "");
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        checkPattern(map, guess);
        TreeMap<String, Integer> finalMap = hardestPattern(map);
        if (!secretPattern.contains(guess + "")){
            wrongGuessesAllowed--;
        }

        return finalMap;
    }

    //Post: helper method which will add words and combinations to current treeMap.
    private void checkPattern(HashMap<String, ArrayList<String>> map, char guess){

        String currentPattern;
        for(String word: currentWords){
            StringBuilder sb = new StringBuilder(secretPattern);
            for(int i = 0; i < word.length(); i++){
                if(word.charAt(i) == guess){
                    sb.replace(i, i+ 1, guess + "");
                }
            }
            currentPattern = sb.toString();

            if(map. containsKey(currentPattern)){
                map.get(currentPattern).add(word);
            }else{
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                map.put(currentPattern, list);
            }
        }
    }

    // Post: Helper method which will update the new hardest pattern, while also creating the
    // tree map for the finalMap.
    private TreeMap<String, Integer> hardestPattern(HashMap<String, ArrayList<String>> map){
        TreeMap<String, Integer> holderMap = new TreeMap<>();
// Seems easier to understand if we equal new String to secretPattern and change it at
        // very end instead of updating the hardest secret pattern every time a new hard pattern
        // is found
        //String hardestPattern = secretPattern;
        //This also seems easier to understand if we change longestPattern at end of method.
        int longestArray = 0;
        int currentDash = checkDashes(secretPattern);
        String secondPattern = "";

        // Find the new hardest pattern, longest array, and find the new words we will be using.
        for (Map.Entry<String, ArrayList<String>> entry: map.entrySet()){
            holderMap.put(entry.getKey(), entry.getValue().size());
            if (entry.getValue().size() > longestArray){
                longestArray = entry.getValue().size();
                secondPattern = secretPattern;
                secretPattern = entry.getKey();
                currentDash = checkDashes(secretPattern);
            }else if (entry.getValue().size() == longestArray){
                int newDash = checkDashes(entry.getKey());
                if (newDash > currentDash){
                    longestArray = entry.getValue().size();
                    secondPattern = secretPattern;
                    secretPattern = entry.getKey();
                    currentDash = checkDashes(secretPattern);
                }else if (newDash == currentDash){
                    int result = entry.getKey().compareTo(secretPattern);
                    if (result > 0){
                        longestArray = entry.getValue().size();
                        secondPattern = secretPattern;
                        secretPattern = entry.getKey();
                        currentDash = checkDashes(secretPattern);
                    }
                }
            }
        }
        if(secondPattern.equals("")){
            secondPattern = secretPattern;
        }
        DifficultyPicker(map, secondPattern);
        return holderMap;
    }

    public void DifficultyPicker(HashMap<String, ArrayList<String>> map,
                                 String secondPattern){
        if (hangmanDifficulty == HangmanDifficulty.EASY){
            if (timesGuessed % 2 == 0){
                currentWords = map.get(secondPattern);
                secretPattern = secondPattern;
            }else{
                currentWords = map.get(secretPattern);
            }
        }else if (hangmanDifficulty == HangmanDifficulty.MEDIUM){
            if (timesGuessed % 4 == 0) {
                currentWords = map.get(secondPattern);
                secretPattern = secondPattern;
            }else{
                currentWords = map.get(secretPattern);
            }
        }else{
            currentWords = map.get(secretPattern);
        }
    }

    // post: return the number of dashes in word.
    private int checkDashes(String word){
        int count = 0;
        for (int i = 0; i < word.length(); i++){
            if(word.charAt(i) == DASH){
                count++;
            }
        }
        return count;
    }



    /**
     * Return the secret word this HangmanManager finally ended up
     * picking for this round.
     * If there are multiple possible words left one is selected at random.
     * <br> pre: numWordsCurrent() > 0
     * @return return the secret word the manager picked.
     */
    public String getSecretWord() {
        if(currentWords.size() == 1){
            return currentWords.get(0);
        }else{
            int randomWord = (int) (Math.random() * (currentWords.size() - 1));
            return currentWords.get(randomWord);
        }
    }
}
