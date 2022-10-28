#include <bits/stdc++.h>
using namespace std;

int getCountOfAbbreviation(string abbreviation, vector<string> filteredWords, int maxWordLen) {

//    cout << abbreviation << endl;
//    for(auto word: filteredWords) cout << word << " ";
//    cout << endl << maxWordLen << endl;

    vector<vector<vector<int> > > dp(
                                        abbreviation.length() + 1,
                                        vector<vector<int> > (
                                            filteredWords.size() + 1,
                                            vector<int> (
                                                maxWordLen + 1,
                                                0) ));
    dp[0][0][0] = 1;
    for(int i=0; i<=abbreviation.length(); i++) {
        for(int j=1; j<=filteredWords.size(); j++) {
            for(int k=1; k<=filteredWords[j-1].length(); k++) {
                dp[i][j][k] += dp[i][j][k-1];
                if(abbreviation[i-1] == filteredWords[j-1][k-1]) {
                    dp[i][j][k] += dp[i-1][j][k-1];
                    if(j>1)
                        dp[i][j][k] += dp[i-1][j-1][filteredWords[j-2].length()];
                    else
                        dp[i][j][k] += dp[i-1][j-1][0];
                }
            }
        }
    }

    return dp[abbreviation.length()][filteredWords.size()][filteredWords.back().length()];
}

void processTestCase(string abbreviation, vector<string> filteredWords, int maxWordLen) {
    int possibleWays = getCountOfAbbreviation(abbreviation, filteredWords, maxWordLen);
    if(possibleWays > 0) {
        cout << abbreviation << " can be formed in " << possibleWays << " ways" << endl;
    } else {
        cout << abbreviation << " is not a valid abbreviation" << endl;
    }
}

int main() {
    while(true) {
        int insignificantWordsCount;
        cin >> insignificantWordsCount;

        if(insignificantWordsCount == 0) {
            break;
        }

        unordered_set<string> insignificantWords;

        for(int i=0; i<insignificantWordsCount; i++) {
            string insignificantWord = "";
            cin >> insignificantWord;

            insignificantWords.insert(insignificantWord);
        }
        string empty = "";
        getline(cin, empty);
        while(true) {
            string line = "";
            getline(cin, line);

            if(line == "LAST CASE") {
                break;
            }
            line += " ";
            string word= "";

            string abbreviation;
            vector<string> filteredWords;
            bool isFirstWordFound = false;

            int maxWordLen = 0;

            for(auto ch : line) {
                if(ch == ' ') {
                    if(insignificantWords.find(word) == insignificantWords.end()) {
                        if(isFirstWordFound == false) {
                            isFirstWordFound = true;
                            abbreviation = word;
                        } else {
                            for(auto& chW : word) {
                                chW ^= 32;
                            }
                            filteredWords.push_back(word);
                            maxWordLen = max(maxWordLen, (int)word.length());
                        }
                    }
                    word = "";
                } else {
                    word += ch;
                }
            }

            processTestCase(abbreviation, filteredWords, maxWordLen);

        }

    }
}