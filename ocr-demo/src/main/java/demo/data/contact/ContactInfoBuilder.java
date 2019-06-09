package demo.data.contact;

/**
 * Contains methods used for constructing an instance of {@link ContactInfo}
 */
public interface ContactInfoBuilder {

	/**
	 * @param name
	 *         the person's name part of the contact information
	 * @return the {@link ContactInfoBuilder}
	 */
	public ContactInfoBuilder name(String name);
	
	/**
	 * @param phoneNumber
	 *         the contact information phone number
	 * @return the {@link ContactInfoBuilder}
	 */
	public ContactInfoBuilder phoneNumber(String phoneNumber);
	
	/**
	 * @param emailAddress
	 *         the contact information email address
	 * @return the {@link ContactInfoBuilder}
	 */
	public ContactInfoBuilder emailAddress(String emailAddress);
	
	/**
	 * @return an instance of {@link ContactInfo}
	 */
	public ContactInfo build();
}
