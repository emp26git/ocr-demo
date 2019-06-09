package demo.data.contact;

/**
 * Implementation of the {@link ContactInfoBuilderFactory}
 */
public class ContactInfoBuilderFactoryImpl implements ContactInfoBuilderFactory {

	/**
	 * @see demo.data.contact.ContactInfoBuilderFactory#getBuilder()
	 */
	@Override
	public ContactInfoBuilder getBuilder() {
		return new ContactInfoImpl.Builder();
	}

}
