import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Main {

    private static int MAX_LEN = 95;

    static int[][][] dp;

    private static int getCountOfAbbreviationBottomUp(String abbreviation, List<String> words) {
        dp[0][0][0] = 1;

        for(int i=1; i<=abbreviation.length(); i++) {
            for(int j=1; j<=words.size(); j++) {
                for(int k=1; k<=words.get(j-1).length(); k++) {
                    dp[i][j][k] += dp[i][j][k-1];
                    if(abbreviation.charAt(i-1) == words.get(j-1).charAt(k-1)) {
                        dp[i][j][k] += dp[i-1][j][k-1];
                        if(j>1)
                            dp[i][j][k] += dp[i-1][j-1][words.get(j-2).length()];
                        else
                            dp[i][j][k] += dp[i-1][j-1][0];
                    }
                }
            }
        }

        return dp[abbreviation.length()][words.size()][words.get(words.size() - 1).length()];

    }

    private static int getCountOfAbbreviation(int abbreviationIndex, String abbreviation, int wordIndex, int wordOffset, List<String> words) {

        if(abbreviationIndex == abbreviation.length() && wordIndex == words.size()) return 1;
        if(abbreviationIndex >= abbreviation.length() || wordIndex >= words.size()) return 0;
        if(abbreviation.length() - abbreviationIndex < words.size() - wordIndex) return 0;
        if(wordOffset == words.get(wordIndex).length()) return 0;

        if(dp[abbreviationIndex][wordIndex][wordOffset] != -1) {
            return dp[abbreviationIndex][wordIndex][wordOffset];
        }

        int count = 0;

        if(words.get(wordIndex).charAt(wordOffset) == abbreviation.charAt(abbreviationIndex)) {
            count += getCountOfAbbreviation(abbreviationIndex + 1, abbreviation, wordIndex + 1, 0, words)
                    + getCountOfAbbreviation(abbreviationIndex + 1, abbreviation, wordIndex, wordOffset + 1, words);
        }
        count += getCountOfAbbreviation(abbreviationIndex, abbreviation, wordIndex, wordOffset + 1, words);
        return dp[abbreviationIndex][wordIndex][wordOffset] = count;
    }
    private static String processTestCase(String abbreviation, List<String> filteredWords) {

//        int possibleWays = getCountOfAbbreviation(0, abbreviation, 0, 0, filteredWords);
        int possibleWays = getCountOfAbbreviationBottomUp(abbreviation, filteredWords);

//        System.out.println(abbreviation);
//        System.out.println(filteredWords.toString());
//        System.out.println(Arrays.deepToString(dp));
        if(possibleWays > 0) {
//            System.out.printf("%s can be formed in %d ways\n", abbreviation, possibleWays);
            return abbreviation + " can be formed in " + possibleWays + " ways\n";
        } else {
//            System.out.printf("%s is not a valid abbreviation\n", abbreviation);
            return abbreviation + " is not a valid abbreviation\n";
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder output  = new StringBuilder();
        int stopper = 10000;
        while(stopper > 0) {
            stopper--;
            int insignifacntWordsCount = Integer.parseInt(scanner.readLine());
            if(insignifacntWordsCount == 0) {
                break;
            }
            Set<String> insignificantWord = new HashSet<>();

            for(int i=0; i<insignifacntWordsCount; i++) {
                insignificantWord.add(scanner.readLine().toUpperCase());
            }
//            scanner.nextLine();
            while(true) {
                String line = scanner.readLine().toUpperCase();
                if(line.equals("LAST CASE")) {
                    break;
                }
                String[] words = line.split(" ");
                String abbreviation = words[0];
                List<String> filteredWords = new ArrayList<>();
                int maxWordSize = 0;
                for(int i=1; i<words.length; i++) {
                    if(!insignificantWord.contains(words[i])) {
                        filteredWords.add(words[i]);
                        maxWordSize = Math.max(maxWordSize, words[i].length());
                    }
                }
                dp = new int[abbreviation.length() + 1][filteredWords.size()+1][maxWordSize + 1];
//                for(int i=0; i <= abbreviation.length(); i++){
//                    for(int j=0; j <= filteredWords.size(); j++) {
//                        for(int k=0; k<= maxWordSize; k++)dp[i][j][k] = 0;
//                    }
//                }
                output.append(processTestCase(abbreviation, filteredWords));

            }
        }
        System.out.print(output.toString());
    }
}