package com.russellhowell.concordance;

import java.io.FileReader;
import java.util.*;

public class Concordance
{
    private TreeMap<String, WordCounter> wordCountMap;

    /*
        Helper class that keeps track of total number of
            occurrences of a word, as well as the number of
            occurrences of the word in each sentence
     */
    protected class WordCounter
    {
        private int count;
        private Map<Integer, Integer> countPerSentence;

        WordCounter(int sentenceNum)
        {
            //first occurrence word, input current sentence number and initial count into map
            count = 1;
            countPerSentence = new HashMap<>();
            countPerSentence.put(sentenceNum, 1);
        }

        /*
            Increments both the total count and the count
               for the passed sentence number
         */
        protected void incrementWordCount(int sentenceNum)
        {
            count++;
            Integer currentCount = countPerSentence.get(sentenceNum);
            if(currentCount == null)
            {
                //first occurrence in sentence
                countPerSentence.put(sentenceNum, 1);
            }
            else
            {
                //otherwise increment current count
                countPerSentence.put(sentenceNum, ++currentCount);
            }
        }

        /*
        Returns the number of occurrences in given sentence,
            returns -1 if no occurrences in requested sentence
         */
        protected int getCountInSentence(int sentenceNum)
        {
            Integer countInSentence = countPerSentence.get(sentenceNum);

            return ((countInSentence != null) ? countInSentence : -1);
        }

        /*
            Retrieve total occurrences of word
         */
        protected int getCount()
        {
            return count;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            Set sentenceCountSet = this.countPerSentence.entrySet();

            result.append(count);
            result.append("       ");

            for (Iterator setIterator = sentenceCountSet.iterator(); setIterator.hasNext();)
            {
                Map.Entry sentenceEntry = (Map.Entry) setIterator.next();
                result.append('{');
                result.append(sentenceEntry.getKey());
                result.append(',');
                result.append(sentenceEntry.getValue());
                result.append("} ");
            }

            return result.toString();
        }
    }

    Concordance()
    {
        //default
    }

    /*
        Main entry point for generating concordance
            from a passed text file
            (must be absolute filepath)
     */
    public void generateConcordance(String fileName)
    {
            try
            {
                Scanner sentenceScanner = new Scanner(new FileReader(fileName));
                //begin iterating through file one sentence at a time
                this.concordanceCounter(sentenceScanner);
            }
            catch (Exception e)
            {
                System.err.println("Issue reading from " + fileName + ", please check that file exists");
                e.printStackTrace();
            }
    }

    /*
        Helper method that keeps track of the current
        sentence number and counts each word's occurrences
     */
    protected void concordanceCounter(Scanner sentenceScanner)
    {
        String word, sentence;
        int currentSentence = 0;
        this.wordCountMap = new TreeMap<String, WordCounter>();

        //iterate through sentences
        sentenceScanner.useDelimiter(".(?<!\\w\\.\\w.)(?<![A-Z]\\.)(?<=\\.|\\?)\\s");
        while(sentenceScanner.hasNext())
        {
            sentence = sentenceScanner.next().toLowerCase();

            //iterate through each word in each sentence
            Scanner wordScanner =  new Scanner(sentence).useDelimiter(" ");
            while(wordScanner.hasNext())
            {
                word = wordScanner.next();
                //remove all punctuation from word for simplicity
                word = word.replaceAll("[^a-zA-Z\\d\\s:]|:", "");

                //insert word into map if not already otherwise increment
                WordCounter wordCounterObj = this.wordCountMap.get(word);
                if(wordCounterObj == null)
                {
                    //first occurrence of word, insert into map
                    this.wordCountMap.put(word, new WordCounter(currentSentence));
                }
                else
                {
                    //word counter exists in map, increment
                    wordCounterObj.incrementWordCount(currentSentence);
                }
            }

            currentSentence++;
        }
    }

    /*
        Returns and prints nicely formatted concordance report
     */
    protected String printConcordanceResults()
    {
        StringBuilder result = new StringBuilder();
        Set wordSet = this.wordCountMap.entrySet();

        //table heading
        result.append("Word");
        for (int i = 0; i < 21 ; i++)
        {
          result.append(' ');
        }
        result.append("Total | {Sentence #, Count in sentence}\n\n");

        //iterate through each word count object, appending results
        for (Object aWordSet : wordSet)
        {
            Map.Entry wordEntry = (Map.Entry) aWordSet;
            WordCounter wordCounterObj = (WordCounter) wordEntry.getValue();

            String currentWord = (String) wordEntry.getKey();
            result.append(currentWord);

            //nicely format output
            for (int i = currentWord.length(); i < 25 ; i++)
            {
               result.append(' ');
            }

            result.append(wordCounterObj.toString());
            result.append("\n");
        }

        System.out.println(result.toString());
        return result.toString();
    }

    /*
        Helper method for testing that generates a new WordCounter object
     */
    protected WordCounter wordCounterFactory(int sentenceNum)
    {
        return new WordCounter(sentenceNum);
    }

    /*
        Helper method that returns word counter for passed word
     */
    protected WordCounter getWordCounter(String word)
    {
        return this.wordCountMap.get(word.toLowerCase().replaceAll("[^a-zA-Z\\d\\s:]|:", ""));
    }
}
