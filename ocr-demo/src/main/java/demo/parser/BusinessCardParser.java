package demo.parser;

import demo.data.contact.ContactInfo;

/**
 * Defines the interface to the business card parser, which is used to parse data from a scanned business card.
 */
public interface BusinessCardParser {

    /**
     * @param document
     *          the contents of the scanned business card in a single String of text
     * @return the {@link ContactInfo} as parsed from the specified document
     */
    public ContactInfo getContactInfo(String document);
}
