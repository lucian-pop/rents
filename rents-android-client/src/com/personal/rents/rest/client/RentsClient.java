package com.personal.rents.rest.client;

import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

import com.personal.rents.dto.RentFavoriteViewsCounter;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentSearch;
import com.personal.rents.model.view.RentFavoriteView;
import com.personal.rents.rest.api.IAddRent;
import com.personal.rents.rest.api.IAuthorization;
import com.personal.rents.rest.api.IChangePassword;
import com.personal.rents.rest.api.IGetRent;
import com.personal.rents.rest.api.IGetRents;
import com.personal.rents.rest.api.IUserAddedRents;
import com.personal.rents.rest.api.ILogin;
import com.personal.rents.rest.api.IMyResource;
import com.personal.rents.rest.api.ISearchRents;
import com.personal.rents.rest.api.ISignup;
import com.personal.rents.rest.api.IUploadImage;
import com.personal.rents.rest.api.IUserFavorites;
import com.personal.rents.rest.error.ResponseErrorHandler;
import com.personal.rents.rest.error.UnauthorizedException;
import com.personal.rents.util.DateUtil;
import com.personal.rents.util.GeneralConstants;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

public class RentsClient {
	
	private static final String BASE_URL = GeneralConstants.BASE_URL + "/ws";

	private static final RestAdapter restAdapter;

	static {
		restAdapter = new RestAdapter.Builder().setServer(BASE_URL)
				.setErrorHandler(new ResponseErrorHandler()).build();
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
	
	public static boolean isAuthorized(String tokenKey)	throws UnauthorizedException {
		boolean authorized = true;
		Response response = restAdapter.create(IAuthorization.class).authorize(tokenKey);

		if(response.getStatus() != WebserviceResponseStatus.OK.getCode()) {
			authorized = false;
		}

		return authorized;
	}
	
	public static String uploadImage(byte[] image, String filename,	String datetime,
			String tokenKey) throws UnauthorizedException {
		TypedByteArray imagePart = new TypedByteArray("application/octet-stream", image);
		String imageURI = restAdapter.create(IUploadImage.class).uploadImage(imagePart, 
				new TypedString(filename), new TypedString(datetime),
				tokenKey);
		
		return imageURI;
	}
	
	public static Rent addRent(Rent rent, String tokenKey) throws UnauthorizedException {
		return restAdapter.create(IAddRent.class).addRent(rent, tokenKey);
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
	
	public static Rent getDetailedRent(int rentId) {
		return restAdapter.create(IGetRent.class).getDetailedRent(rentId);
	}
	
	public static RentsCounter getUserAddedRents(int pageSize, String tokenKey)
			throws UnauthorizedException {
		return restAdapter.create(IUserAddedRents.class).getUserAddedRents(pageSize, tokenKey);
	}
	
	public static List<Rent> getUserAddedRentsNextPage(Date lastRentDate, int lastRentId,
			int pageSize, String tokenKey) throws UnauthorizedException {
		return restAdapter.create(IUserAddedRents.class).getUserAddedRentsNextPage(
				DateUtil.standardFormat(lastRentDate), lastRentId, pageSize, tokenKey);
	}
	
	public static int deleteUserAddedRents(List<Integer> rentIds, String tokenKey) 
			throws UnauthorizedException {
		return restAdapter.create(IUserAddedRents.class).deleteUserAddedRents(rentIds, tokenKey);
	}
	
	public static boolean addRentToFavorites(int rentId, String tokenKey) 
			throws UnauthorizedException {
		return restAdapter.create(IUserFavorites.class).addRentToFavorites(rentId, tokenKey);
	}

	public static RentFavoriteViewsCounter getUserFavoriteRents(int pageSize, String tokenKey)
			throws UnauthorizedException {
		return restAdapter.create(IUserFavorites.class).getUserFavoriteRents(pageSize, tokenKey);
	}
	
	public static List<RentFavoriteView> getUserFavoriteRentsNextPage(Date lastDate, int pageSize,
			String tokenKey) throws UnauthorizedException {
		return restAdapter.create(IUserFavorites.class).getUserFavoriteRentsNextPage(
				DateUtil.standardFormat(lastDate), pageSize, tokenKey);
	}

	public static int deleteUserFavoriteRents(List<Integer> rentIds, String tokenKey) 
			throws UnauthorizedException {
		return restAdapter.create(IUserFavorites.class).deleteUserFavoriteRents(rentIds, tokenKey);
	}
}
