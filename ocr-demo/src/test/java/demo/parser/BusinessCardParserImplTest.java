package demo.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import demo.data.contact.ContactInfo;

/**
 * Unit tests for {@link BusinessCardParserImpl}
 */
class BusinessCardParserImplTest {

    /**
     * Test method for {@link demo.parser.BusinessCardParserImpl#getContactInfo(java.lang.String)}.
     */
    @Test
    void testGetArthurWilsonContactInfo() {
        final String scannedData = readDataFile("arthur-wilson-scan-result.txt");
        final BusinessCardParser parser = new BusinessCardParserImpl();
        final ContactInfo contactInfo = parser.getContactInfo(scannedData);

        assertNotNull(contactInfo);
        assertEquals(contactInfo.getName(), "Arthur Wilson");
        assertEquals(contactInfo.getEmailAddress(), "awilson@abctech.com");
        assertEquals(contactInfo.getPhoneNumber(), "17035551259");
    }

    /**
     * Test method for {@link demo.parser.BusinessCardParserImpl#getContactInfo(java.lang.String)}.
     */
    @Test
    void testGetLisaHaungContactInfo() {
        final String scannedData = readDataFile("lisa-haung-scan-result.txt");
        final BusinessCardParser parser = new BusinessCardParserImpl();
        final ContactInfo contactInfo = parser.getContactInfo(scannedData);

        assertNotNull(contactInfo);
        assertEquals(contactInfo.getName(), "Lisa Haung");
        assertEquals(contactInfo.getEmailAddress(), "lisa.haung@foobartech.com");
        assertEquals(contactInfo.getPhoneNumber(), "4105551234");
    }

    /**
     * Test method for {@link demo.parser.BusinessCardParserImpl#getContactInfo(java.lang.String)}.
     */
    @Test
    void testGetMikeSmithContactInfo() {
        final String scannedData = readDataFile("mike-smith-scan-result.txt");
        final BusinessCardParser parser = new BusinessCardParserImpl();
        final ContactInfo contactInfo = parser.getContactInfo(scannedData);

        assertNotNull(contactInfo);
        assertEquals(contactInfo.getName(), "Mike Smith");
        assertEquals(contactInfo.getEmailAddress(), "msmith@asymmetrik.com");
        assertEquals(contactInfo.getPhoneNumber(), "4105551234");
    }

    /**
     * Test method for {@link demo.parser.BusinessCardParserImpl#getContactInfo(java.lang.String)}.
     */
    @Test
    void testNoContactInfo() {
        final String scannedData = readDataFile("no-contact-data.txt");
        final BusinessCardParser parser = new BusinessCardParserImpl();
        final ContactInfo contactInfo = parser.getContactInfo(scannedData);

        assertNull(contactInfo);
    }


    private String readDataFile(final String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }

        try {
            final String content = new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName)), "UTF-8");
            return content;
        }
        catch (final IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        return null;
    }
}
