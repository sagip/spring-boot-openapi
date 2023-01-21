package io.reflectoring.service;

import java.util.UUID;
import java.util.regex.Pattern;

public class InputValidator {
	
	private InputValidator() {}
	
	static Pattern regexUuid = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
	
	public static boolean isValidLenghtString(String str, int lenMin, int lenMax) {
		if (str==null) 
			return (lenMin==0);
		else
			return (str.length()>=lenMin && str.length()<=lenMax);
	}
	
	public static UUID getValidatedUuid(String uuid) {

		try {
			if (!regexUuid.matcher(uuid).matches()) {
				return null;
			}
			return UUID.fromString(uuid);
		}catch (Exception e) {
			return null;
		}
	}
	
}
