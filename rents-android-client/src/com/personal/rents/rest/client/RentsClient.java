package com.personal.rents.rest.client;

import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentSearch;
import com.personal.rents.rest.api.IAddRent;
import com.personal.rents.rest.api.IAuthorization;
import com.personal.rents.rest.api.IChangePassword;
import com.personal.rents.rest.api.IGetRents;
import com.personal.rents.rest.api.ILogin;
import com.personal.rents.rest.api.IMyResource;
import com.personal.rents.rest.api.ISearchRents;
import com.personal.rents.rest.api.ISignup;
import com.personal.rents.rest.api.IUploadImage;
import com.personal.rents.rest.util.WebserviceResponseStatus;
import com.personal.rents.util.DateUtil;
import com.personal.rents.util.GeneralConstants;

public class RentsClient {
	
	private static final String BASE_URL = GeneralConstants.BASE_URL + "/ws";

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
			double minLongitude, double maxLongitude, int pageSize) {
		return restAdapter.create(IGetRents.class).getRentsByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude, pageSize);
	}
	
	public static List<Rent> getRentsNextPageByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, Date lastRentDate, int lastRentId,
			int pageSize) {
		return restAdapter.create(IGetRents.class).getRentsNextPageByMapBoundaries(minLatitude,
				maxLatitude, minLongitude, maxLongitude, DateUtil.standardFormat(lastRentDate),
				lastRentId, pageSize);
	}
	
	public static RentsCounter searchRents(RentSearch rentSearch) {
		return restAdapter.create(ISearchRents.class).searchRents(rentSearch);
	}

	public static List<Rent> searchRentsNextPage(RentSearch rentSearch) {
		return restAdapter.create(ISearchRents.class).searchRentsNextPage(rentSearch);
	}
}
