
import java.util.*;
import java.io.IOException;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            WordSearch wordSearch = new WordSearch();

            double matches = wordSearch.solvePuzzle();

            // Output the number of matches found
            System.out.println("Number of matches found: " + matches);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}