package com.russellhowell.concordance;

import org.junit.jupiter.api.Test;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConcordanceTest {

    private Concordance concordance = new Concordance();
    private final String s1 = "This is a test sentence that has the word sentence twice. A second sentence? Another one!";
    private final String s2 = "Bounty-hunter Rick Deckard signs on to a new police mission in order to earn enough money to buy a live animal to replace his electric sheep, " +
            "seeking greater existential fulfillment for himself and his depressed wife, Iran. The mission involves hunting down (\"retiring\") six Nexus-6 androids that violently went " +
            "rogue after their creation by the Rosen Association, and fled Mars for Earth. Deckard visits the Rosen headquarters in Seattle to confirm the validity of a question-and-answer empathy " +
            "test: a method for identifying any androids posing as humans. Deckard is greeted by Rachael Rosen, who quickly fails his test. " +
            "Rachael attempts to bribe Deckard into silence, but he verifies that she is indeed a Nexus-6 model used by Rosen to attempt to discredit the test.";
    private final String s3 = "\"Do Androids Dream of Electric Sheep?\" is a science fiction novel by American writer Philip K. Dick, first published in 1968. The novel is set in a post-apocalyptic San Francisco, where Earth's life has been greatly damaged by nuclear global war.";

    //change filepath for test text file
    private final String FILE_PATH = "/home/russell/IdeaProjects/Concordance/src/com/russellhowell/concordance/test.txt";


    @Test
    void concordanceCounterTest() throws Exception
    {
        Scanner input = new Scanner(s1);
        concordance.concordanceCounter(input);

        assertSame(null, concordance.getWordCounter("hello"));
        assertTrue(concordance.getWordCounter("that") != null);

        assertSame(1, concordance.getWordCounter("test").getCount());
        assertSame(3, concordance.getWordCounter("sentence").getCount());
        assertSame(1, concordance.getWordCounter("this").getCount());
        assertSame(1, concordance.getWordCounter("twice").getCount());
        assertSame(1, concordance.getWordCounter("this").getCountInSentence(0));
        concordance.printConcordanceResults();

        input = new Scanner(s2);
        concordance.concordanceCounter(input);
        assertSame(4, concordance.getWordCounter("Rosen").getCount());
        assertSame(1, concordance.getWordCounter("Question-and-answer").getCount());
        concordance.printConcordanceResults();

        input = new Scanner(s3);
        concordance.concordanceCounter(input);
        assertSame(1, concordance.getWordCounter("Novel").getCountInSentence(0));
        assertSame(1, concordance.getWordCounter("post-apocalyptic").getCountInSentence(1));
        concordance.printConcordanceResults();

    }

    @Test
    void wordIncrementTest() throws Exception
    {
        Concordance.WordCounter aWordCounter = concordance.wordCounterFactory(0);
        aWordCounter.incrementWordCount(0);
        aWordCounter.incrementWordCount(2);
        aWordCounter.incrementWordCount(2);
        aWordCounter.incrementWordCount(2);

        assertSame(2, aWordCounter.getCountInSentence(0));
        assertSame(-1, aWordCounter.getCountInSentence(1));
        assertSame(3, aWordCounter.getCountInSentence(2));
        assertSame(5, aWordCounter.getCount());
    }

    @Test
    void  concordanceTextFileTest() throws Exception
    {
        concordance.generateConcordance(FILE_PATH);
        concordance.printConcordanceResults();
    }

}