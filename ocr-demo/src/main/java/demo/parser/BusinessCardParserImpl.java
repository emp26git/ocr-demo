package demo.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import demo.data.contact.ContactInfo;
import demo.data.contact.ContactInfoBuilder;
import demo.data.contact.ContactInfoBuilderFactory;
import demo.data.contact.ContactInfoBuilderFactoryImpl;

/**
 * Implementation of {@link BusinessCardParser}<p>
 *
 * Stumbled upon the Apache OpenNLP library during search for parsing names from text. Then
 * found a solution to this exercise located at https://github.com/orangepips/business-card-parser.
 * I used my own implementation of the OpenNLP classes based on the posted solution.
 */
public class BusinessCardParserImpl implements BusinessCardParser {
    private static final Logger log = LogManager.getLogger(BusinessCardParserImpl.class);

    private static BusinessCardParserImpl instance;

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
    private final NameEvaluator nameEvaluator = new NameEvaluator();

    /**
     * @see demo.parser.BusinessCardParser#getContactInfo(java.lang.String)
     */
    @Override
    public ContactInfo getContactInfo(final String document) {
        if (StringUtils.isBlank(document)) {
            return null;
        }

        // separate lines of the document
        final String[] lines = document.split("\\r?\\n");
        final ContactInfoBuilder contactInfoBuilder = contactInfoBuilderFactory.getBuilder();
        boolean foundEmail = false;
        boolean foundPhoneNumber = false;

        // make sure we clean out any previously calculated name
        nameEvaluator.reset();

        // iterate over each line, try to find the phone number, email, and name
        for (final String line : lines) {

            if ((!foundEmail) && emailValidator.isValid(line)) {
                contactInfoBuilder.emailAddress(line);
                foundEmail = true;

                if (log.isDebugEnabled()) {
                    log.debug("found email=" + line);
                }
            }
            else if (!foundPhoneNumber) {
                // this is probably sufficient for a "business card" implementation, but a more robust regex would be better for a general case
                final String digits = line.replaceAll("[^\\d]", "");

                // a phone number can be 10 to 15 digits, depending on the country code; ignore potential "Fax" number
                if ((digits.length() >= 10 && digits.length() <= 15) && (!StringUtils.startsWithIgnoreCase(line, "f"))) {
                    contactInfoBuilder.phoneNumber(digits);
                    foundPhoneNumber = true;

                    if (log.isDebugEnabled()) {
                        log.debug("found phone number=" + digits);
                    }
                }
                else {
                    nameEvaluator.evaluate(line);
                }
            }
            else {
                nameEvaluator.evaluate(line);
            }
        }

        final String name = nameEvaluator.getName();
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
}
