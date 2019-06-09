package demo.parser;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import demo.data.contact.ContactInfo;
import demo.data.contact.ContactInfoBuilder;
import demo.data.contact.ContactInfoBuilderFactory;
import demo.data.contact.ContactInfoBuilderFactoryImpl;
import demo.scanner.ui.ScannerUI;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * Implementation of {@link BusinessCardParser}<p>
 *
 * Stumbled upon the Apache OpenNLP library during search for parsing names from text. Then
 * found a solution to this exercise located at https://github.com/orangepips/business-card-parser.
 * I used my own implementation of the OpenNLP classes based on the posted solution.
 */
public class BusinessCardParserImpl implements BusinessCardParser {
    private static final Logger log = LogManager.getLogger(ScannerUI.class);

    private static BusinessCardParserImpl instance;
    private static NameFinderME nameFinder;

    static {
        try {
            nameFinder =  new NameFinderME(new TokenNameFinderModel(ClassLoader.getSystemResourceAsStream("en-ner-person.bin")));
        }
        catch (final IOException ex) {
            log.error("Error loading name library: " + ex.getMessage(), ex);
        }
    }

    /**
     * @return the {@link BusinessCardParser} instance
     */
    public static BusinessCardParser getInstance() {
        if (instance == null) {
            synchronized(BusinessCardParserImpl.class) {
                if (instance == null) {
                    instance = new BusinessCardParserImpl();
                }
            }
        }

        return instance;
    }


    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final ContactInfoBuilderFactory contactInfoBuilderFactory = new ContactInfoBuilderFactoryImpl();

    /**
     * @see demo.parser.BusinessCardParser#getContactInfo(java.lang.String)
     */
    @Override
    public ContactInfo getContactInfo(final String document) {
        if (StringUtils.isBlank(document)) {
            return null;
        }

        // separate lines of the document
        final ContactInfoBuilder contactInfoBuilder = contactInfoBuilderFactory.getBuilder();
        final String[] lines = StringUtils.splitByWholeSeparator(document, "\n");
        double currentHighProb = 0;
        String name = null;
        boolean foundEmail = false;
        boolean foundPhoneNumber = false;

        // iterate over each line, try to find the phone number, email, and name
        for (final String line : lines) {
            final double currentNameProb = calculateNameProbability(line);

            if (currentNameProb > currentHighProb) {
                name = line;
                currentHighProb = currentNameProb;
            }

            if ((!foundEmail) && emailValidator.isValid(line)) {
                contactInfoBuilder.emailAddress(line);
                foundEmail = true;

                if (log.isDebugEnabled()) {
                    log.debug("found email=" + line);
                }
            }
            else if (!foundPhoneNumber) {
                final String digits = line.replaceAll("[^\\d]", "");

                // a phone number can be 10 to 15 digits, depending on the country code; ignore potential "Fax" number
                if ((digits.length() >= 10 && digits.length() <= 15) && (!StringUtils.startsWithIgnoreCase(line, "f"))) {
                    contactInfoBuilder.phoneNumber(digits);
                    foundPhoneNumber = true;

                    if (log.isDebugEnabled()) {
                        log.debug("found phone number=" + digits);
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(name)) {
            contactInfoBuilder.name(name);

            if (log.isDebugEnabled()) {
                log.debug("found name=" + name);
            }
        }
        else if (!foundEmail && (!foundPhoneNumber)) {
            log.warn("No ContactInfo generated: data contained no name, email, or phone number!");
            return null;
        }

        // generate the ContactInfo and return it
        return contactInfoBuilder.build();
    }

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
}
