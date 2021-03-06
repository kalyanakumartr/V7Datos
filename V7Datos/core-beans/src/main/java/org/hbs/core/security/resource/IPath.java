package org.hbs.core.security.resource;

import java.util.Map;

import org.hbs.core.beans.model.IUsers;
import org.hbs.core.beans.model.IUsers.EUsers;
import org.hbs.core.beans.model.Producers;
import org.hbs.core.beans.model.Users;
import org.hbs.core.util.CommonValidator;
import org.hbs.core.util.EnumInterface;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public interface IPath extends IPathBase
{
	public enum EAuth implements EnumInterface
	{
		User;

		Map<String, Object> details = null;

		public String getCountryId(Authentication auth)
		{
			if (details == null)
				init(auth);
			return (String) details.get(COUNTRY_ID);
		}

		public String getFullName(Authentication auth)
		{
			if (details == null)
				init(auth);
			return (String) details.get(USER_FULL_NAME);
		}

		public String getParentProducerId(Authentication auth)
		{
			if (details == null)
				init(auth);

			return (String) details.get(PARENT_PRODUCER_ID);
		}

		public String getParentProducerName(Authentication auth)
		{
			if (details == null)
				init(auth);

			return (String) details.get(PARENT_PRODUCER_NAME);
		}

		public Producers getParentProducer(Authentication auth)
		{
			if (details == null)
				init(auth);

			return new Producers((String) details.get(PARENT_PRODUCER_ID), (String) details.get(PARENT_PRODUCER_NAME));
		}

		public Producers getProducer(Authentication auth)
		{
			if (details == null)
				init(auth);

			return new Producers((String) details.get(PRODUCER_ID), (String) details.get(PRODUCER_NAME));
		}

		public String getProducerId(Authentication auth)
		{
			if (details == null)
				init(auth);

			return (String) details.get(PRODUCER_ID);
		}

		public String getProducerName(Authentication auth)
		{
			if (details == null)
				init(auth);
			return (String) details.get(PRODUCER_NAME);
		}

		public IUsers getUser(Authentication auth)
		{
			if (details == null)
				init(auth);
			return new Users(String.valueOf(details.get(EMPLOYEE_ID)));
		}

		public String getUserId(Authentication auth)
		{
			if (details == null)
				init(auth);
			return ((String) details.get(EMPLOYEE_ID));
		}

		@SuppressWarnings("unchecked")
		void init(Authentication auth)
		{
			OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
			this.details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		}

		public boolean isAdministrator(Authentication auth)
		{
			for (GrantedAuthority _GA : auth.getAuthorities())
			{
				if (CommonValidator.isEqual(_GA.getAuthority(), ERole.Administrator))
					return true;
			}
			return false;
		}

		public boolean isSuperAdmin(Authentication auth)
		{
			if (details == null)
				init(auth);
			if (CommonValidator.isEqual((String) details.get(USER_NAME), EUsers.SuperAdmin))
				return true;

			return false;
		}

		public boolean verifyProducer(Authentication auth, String producerId)
		{
			if (isSuperAdmin(auth))
				return true;
			return CommonValidator.isEqual(getProducerId(auth), producerId);
		}

	}

	public enum ETemplate implements EnumInterface
	{
		Default(001), //
		Create_User_Admin(100), //
		Create_User_Employee(110), //
		User_Token(120), //
		User_Password(130), //
		User_Reset_Password(140), //
		User_Blocked_Admin(150), //
		Create_User_Employee_SMS(200), //
		SMS_OTP(210), //
		WhatsApp_Create_Employee(300), //
		Test_Email_Connection(900), //
		Test_SMS_Connection(910), //
		Test_WhatsApp_Connection(920);

		long id;

		ETemplate(long id)
		{
			this.id = id;
		}

		public long getId()
		{
			return id;
		}
	}

	public static final String	USER_FULL_NAME						= "userFullName";

	public static final String	PRODUCER_NAME						= "producerName";

	public static final String	PRODUCER_ID							= "producerId";

	public static final String	EMPLOYEE_ID							= "employeeId";

	public static final String	COUNTRY_ID							= "countryId";

	public static final String	ACCEPT								= "accept";

	public static final String	AUTHORIZATION						= "authorization";

	public static final String	CONTENT_TYPE						= "Content-Type";

	public static final String	USER_NAME							= "userName";

	public static final String	MEDIA_TYPE_ZIP						= "application/zip";

	public static final String	PARENT_PRODUCER_ID					= "parentProducerId";

	public static final String	PARENT_PRODUCER_NAME				= "parentProducerName";

	public static final String	HAS_AUTHORITY_SUPERADMIN			= "hasAuthority('SUPERADMINROLE')";
	public static final String	HAS_AUTHORITY_ADMINISTRATOR			= "hasAuthority('ADMIN')";
	public static final String	HAS_AUTHORITY_EMPLOYEE				= "hasAuthority('EMPLOYEE')";
	public static final String	HAS_AUTHORITY_USER					= "hasAuthority('USER')";
	public static final String	HAS_AUTHORITY_SUPERADMIN_OR_ADMIN	= HAS_AUTHORITY_ADMINISTRATOR + " or " + HAS_AUTHORITY_SUPERADMIN;
	public static final String	HAS_AUTHORITY_BOTH					= HAS_AUTHORITY_ADMINISTRATOR + " or " + HAS_AUTHORITY_EMPLOYEE;
	public static final String	HAS_ALL_AUTHORITY					= HAS_AUTHORITY_SUPERADMIN + " or " + HAS_AUTHORITY_ADMINISTRATOR + " or " + HAS_AUTHORITY_EMPLOYEE + " or " + HAS_AUTHORITY_USER;

}
