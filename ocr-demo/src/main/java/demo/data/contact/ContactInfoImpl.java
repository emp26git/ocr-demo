package demo.data.contact;

import java.util.Objects;

/**
 * Implementation of {@link ContactInfo}
 */
public class ContactInfoImpl implements ContactInfo {

	/** generated serial version ID */
    private static final long serialVersionUID = 5089835466226819473L;
    
    
    private final String name;
	private final String phoneNumber;
	private final String emailAddr;
	
	static final class Builder implements ContactInfoBuilder {
		private String name;
		private String phoneNumber;
		private String emailAddr;
		
		public Builder() {
			
		}

		/**
		 * @see demo.data.contact.ContactInfoBuilder#name(java.lang.String)
		 */
		@Override
		public ContactInfoBuilder name(final String name) {
			this.name = name;
			return this;
		}

		/**
		 * @see demo.data.contact.ContactInfoBuilder#phoneNumber(java.lang.String)
		 */
		@Override
		public ContactInfoBuilder phoneNumber(final String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		/**
		 * @see demo.data.contact.ContactInfoBuilder#emailAddress(java.lang.String)
		 */
		@Override
		public ContactInfoBuilder emailAddress(final String emailAddress) {
			this.emailAddr = emailAddress;
			return this;
		}

		/**
		 * @see demo.data.contact.ContactInfoBuilder#build()
		 */
		@Override
		public ContactInfo build() {
			return new ContactInfoImpl(this);
		}
		
	}
	
	
	ContactInfoImpl(final Builder builder) {
		this.name = builder.name;
		this.phoneNumber = builder.phoneNumber;
		this.emailAddr = builder.emailAddr;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContactInfoImpl [name=" + name + ", phoneNumber=" + phoneNumber + ", emailAddr=" + emailAddr + "]";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(emailAddr, name, phoneNumber);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		ContactInfoImpl other = (ContactInfoImpl) obj;
		return Objects.equals(emailAddr, other.emailAddr) && Objects.equals(name, other.name)
				&& Objects.equals(phoneNumber, other.phoneNumber);
	}

	/**
	 * @see demo.data.contact.ContactInfo#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @see demo.data.contact.ContactInfo#getPhoneNumber()
	 */
	@Override
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * @see demo.data.contact.ContactInfo#getEmailAddress()
	 */
	@Override
	public String getEmailAddress() {
		return this.emailAddr;
	}

}
