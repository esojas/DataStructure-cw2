import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class WordSearch {
    public WordSearch() throws IOException
    {
        // dont forget to put inside folder not the src code
        puzzleStream = openFile("Enter puzzle file");
        wordStream = openFile("Enter dictionary name");
        System.out.println("Reading files...");
        readPuzzle();
        readWords();
    }
    public int solvePuzzle()
    {
        int matches = 0;

        for (int r=0; r<rows;r++){
            for(int c=0; c<columns;c++){
                for(int rd=-1;rd<=1;rd++){
                    for(int cd=-1;cd<=1;cd++){
                        if(rd!=0||cd!=0){
                            matches += solveDirection(c,r,rd,cd);
                        }
                    }
                }
            }
        }
        return matches;
    }
    private int rows;
    private int columns;
    private char theBoard[][];
    private String[] theWords;
    private BufferedReader puzzleStream;
    private BufferedReader wordStream;
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private static int prefixSearch(String[] a,String x)
    {
        // ensure that the array is sorted
        Arrays.sort(a);

        // perform binary search
        int index = Arrays.binarySearch(a, x);

        // if the search key is found, binarySearch returns the index of the key;
        if (index < 0) {
            index = -(index + 1);
        }
        // this count the number of words that have the given prefix
        int count = 0;
        while (index < a.length && a[index].startsWith(x)) {
            count++;
            index++;
        }
        return count;
    }

    private BufferedReader openFile(String message) {
        String fileName = "";
        FileReader theFile;
        BufferedReader fileIn = null;
        do {
            System.out.println(message + ": ");
            try {
                fileName = in.readLine();
                if (fileName == null)
                    System.exit(0);
                theFile = new FileReader(fileName);
                fileIn = new BufferedReader(theFile);
            } catch (IOException e) {
                System.err.println("Cannot open " + fileName);
            }
        }while (fileIn == null) ;
        System.out.println("Opened " + fileName);
        return fileIn;
    }

    private void readWords() throws IOException
    {
        List<String> words = new ArrayList<String>();

        String lastWord = null;
        String thisWord;

        while((thisWord = wordStream.readLine()) != null){
            if(lastWord != null && thisWord.compareTo(lastWord)<0){
                System.err.println("Dictionary is not sorted... skipping");
                continue;
            }
            words.add(thisWord);
            lastWord = thisWord;
        }
        theWords = new String[words.size()];
        theWords = words.toArray(theWords);
    }
    private void readPuzzle() throws IOException
    {

        String oneLine;
        List<String> puzzleLines = new ArrayList<String>();
        if ((oneLine = puzzleStream.readLine()) == null)
            throw new IOException("No lines in puzzle file");

        columns = oneLine.length();
        puzzleLines.add(oneLine);

        while ((oneLine = puzzleStream.readLine()) != null) {
            if (oneLine.length() != columns)
                System.err.println("Puzzle is not rectangular; skipping row");
            else
                puzzleLines.add(oneLine);
        }

        rows = puzzleLines.size();
        theBoard = new char[rows][columns];

        int r = 0;
        for (String theLine : puzzleLines)
            theBoard[r++] = theLine.toCharArray();

    }
    private int solveDirection(int baseRow, int baseCol, int rowDelta, int colDelta) {
        int matches = 0;

        // this will define the boundary conditions for row and column indices
        int maxRow = rows - 1;
        int maxCol = columns - 1;

        // initial from the base position
        int r = baseRow;
        int c = baseCol;

        // initialize the end row and column
        int endRow = baseRow;
        int endCol = baseCol;

        // search in the specified direction
        while (r >= 0 && r <= maxRow && c >= 0 && c <= maxCol) {
            // this will construct the character sequence from the current position
            StringBuilder charSequence = new StringBuilder();
            int tempRow = r;
            int tempCol = c;
            while (tempRow >= 0 && tempRow <= maxRow && tempCol >= 0 && tempCol <= maxCol) {
                charSequence.append(theBoard[tempRow][tempCol]);
                tempRow += rowDelta;
                tempCol += colDelta;
            }

            // search for the character sequence in the dictionary
            int prefixMatches = prefixSearch(theWords, charSequence.toString());
            // if there are matches, print the details
            if (prefixMatches > 0) {
                matches += prefixMatches;
                System.out.println("Found '" + charSequence.toString() + "' at (" + baseRow + "," + baseCol +
                        ") to (" + endRow + "," + endCol + ")");
            }
            // move to the next position in the specified direction
            r += rowDelta;
            c += colDelta;
            endRow = r;
            endCol = c;
        }
        return matches;
    }
}
