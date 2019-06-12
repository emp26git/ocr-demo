package demo.parser;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * Evaluates the likelihood that a specified text string contains a person's name, and keeps
 * a reference to the string having the highest probability. The probability starts at 0.
 * Both the current probability and saved name can be reset using the {@link #reset()} method.
 */
public class NameEvaluator {
    private static final Logger log = LogManager.getLogger(NameEvaluator.class);
    private static NameFinderME nameFinder;

    static {
        try {
            nameFinder =  new NameFinderME(new TokenNameFinderModel(ClassLoader.getSystemResourceAsStream("en-ner-person.bin")));
        }
        catch (final IOException ex) {
            log.error("Error loading name library: " + ex.getMessage(), ex);
        }
    }


    private double nameProbability = 0;
    private String name = null;


    /**
     * Calculates the probability that the specified line contains a person's name. Returns the calculated
     * probability.
     *
     * @param line
     *          the current line of data being parsed/examined
     * @return the probability that the line contains a person's name
     */
    private double calculateNameProbability(final String line) {
        final String[] lineParts = line.split(" ");
        final Span[] spans = nameFinder.find(lineParts);
        double totalProb = 0;

        if (spans != null && spans.length > 0) {
            final double[] probs = nameFinder.probs();

            for (final double prob : probs) {
                totalProb += prob;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("line=" + line + ", totalProb=" + totalProb);
        }

        return totalProb;
    }

    /**
     * Evaluate the specified text to see what the probability is that the text
     * is a person's name.
     *
     * @param text
     */
    public void evaluate(final String text) {
        if (text != null) {
            final double currentNameProb = calculateNameProbability(text);
            if (currentNameProb > this.nameProbability) {
                this.name = text;
                this.nameProbability = currentNameProb;
            }
        }
    }

    /**
     * @return the probability that the currently saved text is a person's name
     */
    public double getCurrentNameProbability() {
        return this.nameProbability;
    }

    /**
     * @return the text currently believed to be a person's name, or null if no name has been found
     */
    public String getName() {
        return this.name;
    }

    /**
     * Reset the probability to 0 and the name to null.
     */
    public void reset() {
        this.name = null;
        this.nameProbability = 0;
    }
}
