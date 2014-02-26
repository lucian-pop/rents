package com.personal.rents.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public final class NetworkUtil {

	private NetworkUtil() {
	}

	public static boolean isValidURI(String urI) {
		try {
			URL uriObj = new URL(urI);
			uriObj.toURI();
		} catch (MalformedURLException e) {
			return false;
		} catch (URISyntaxException e) {
			return false;
		}
		
		return true;
	}

}
