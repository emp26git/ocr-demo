package demo.data.contact;

/**
 * Factory used for creating the {@link ContactInfoBuilder}
 */
public interface ContactInfoBuilderFactory {

	/**
	 * @return the {@link ContactInfoBuilder} instance
	 */
	public ContactInfoBuilder getBuilder();
}
