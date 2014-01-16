package com.personal.rents.rest.client;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.api.IAddRent;
import com.personal.rents.rest.api.IAuthorization;
import com.personal.rents.rest.api.IChangePassword;
import com.personal.rents.rest.api.IGetRents;
import com.personal.rents.rest.api.ILogin;
import com.personal.rents.rest.api.IMyResource;
import com.personal.rents.rest.api.ISignup;
import com.personal.rents.rest.api.IUploadImage;
import com.personal.rents.rest.util.WebserviceResponseStatus;

public class RentsClient {
	
	private static final String BASE_URL = "http://192.168.1.5:8080/rents-server/ws";

	private static final RestAdapter restAdapter;

	static {
		restAdapter = new RestAdapter.Builder().setServer(BASE_URL).build();
	}
	
	public static String getResource() {
		String resource = restAdapter.create(IMyResource.class).getIt();
		
		return resource;
	}

	public static  Account signup(Account account) {
		account = restAdapter.create(ISignup.class).signup(account);
		
		return account;
	}
	
	public static Account login(String email, String password) {
		Account account = restAdapter.create(ILogin.class).login(email, password);
		
		return account;
	}
	
	public static String changePassword(String email, String password, String newPassword) {
		String tokenKey = restAdapter.create(IChangePassword.class).changePassword(email, 
				password, newPassword);
		
		return tokenKey;
	}
	
	public static boolean isAuthorized(int accountId, String tokenKey) {
		boolean authorized = true;
		Response response = restAdapter.create(IAuthorization.class).authorize(accountId, tokenKey);

		if(response.getStatus() != WebserviceResponseStatus.OK.getCode()) {
			authorized = false;
		}

		return authorized;
	}
	
	public static String uploadImage(byte[] image, String filename, String accountId, 
			String datetime) {
		TypedByteArray imagePart = new TypedByteArray("application/octet-stream", image);
		String imageURI = restAdapter.create(IUploadImage.class).uploadImage(imagePart, 
				new TypedString(filename), new TypedString(accountId), new TypedString(datetime));
		
		
		return imageURI;
	}
	
	public static Rent addRent(Rent rent) {
		rent = restAdapter.create(IAddRent.class).addRent(rent);

		return rent;
	}

	public static RentsCounter getRentsByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude) {
		return restAdapter.create(IGetRents.class).getRentsByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude);
	}

}
