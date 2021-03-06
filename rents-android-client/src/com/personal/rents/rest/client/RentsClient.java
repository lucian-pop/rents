package com.personal.rents.rest.client;

import java.util.Date;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

import com.personal.rents.dto.RentFavoriteViewsCounter;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;
import com.personal.rents.model.RentSearch;
import com.personal.rents.model.view.RentFavoriteView;
import com.personal.rents.rest.api.IAccount;
import com.personal.rents.rest.api.IRent;
import com.personal.rents.rest.api.IGetRent;
import com.personal.rents.rest.api.IGetRents;
import com.personal.rents.rest.api.IUserAddedRents;
import com.personal.rents.rest.api.ISearchRents;
import com.personal.rents.rest.api.IRentImage;
import com.personal.rents.rest.api.IUserFavorites;
import com.personal.rents.util.DateUtil;
import com.personal.rents.util.GeneralConstants;

public class RentsClient {
	
	private static final String VERSION_HEADER_NAME = "version";
	
	private static final String VERSION_HEADER_VALUE = "1";
	
	private static final String BASE_URL = GeneralConstants.BASE_URL + "/ws";

	private static final RestAdapter restAdapter;

	static {
		restAdapter = new RestAdapter.Builder()
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						request.addHeader(VERSION_HEADER_NAME, VERSION_HEADER_VALUE);
					}
				})
				.setServer(BASE_URL)
				.build();
	}

	public static  Account signup(Account account) {
		account = restAdapter.create(IAccount.class).signup(account);
		
		return account;
	}
	
	public static Account login(String email, String password) {
		Account account = restAdapter.create(IAccount.class).login(email, password);
		
		return account;
	}
	
	public static String changePassword(String email, String password, String newPassword) {
		String tokenKey = restAdapter.create(IAccount.class).changePassword(email, 
				password, newPassword);
		
		return tokenKey;
	}
	
	public static RentImage uploadRentImage(byte[] image, int rentId, String tokenKey) {
		TypedByteArray imagePart = new TypedByteArray("application/octet-stream", image);
		RentImage rentImage = restAdapter.create(IRentImage.class).uploadRentImage(imagePart,
				new TypedString(Integer.toString(rentId)), tokenKey);
		
		return rentImage;
	}
	
	public static RentImage replaceRentImage(byte[] imageData, RentImage rentImage, 
			String tokenKey){
		TypedByteArray imagePart = new TypedByteArray("application/octet-stream", imageData);
		
		return restAdapter.create(IRentImage.class).replaceRentImage(imagePart,
				new TypedString(Integer.toString(rentImage.rentImageId)), rentImage.rentImageURI, 
				new TypedString(Integer.toString(rentImage.rentId)), tokenKey);
	}
	
	public static void deleteRentImage(int rentImageId, String tokenKey) {
		restAdapter.create(IRentImage.class).deleteRentImage(rentImageId, tokenKey);
	}
	
	public static Rent addRent(Rent rent, String tokenKey) {
		return restAdapter.create(IRent.class).addRent(rent, tokenKey);
	}

	public static int updateRent(Rent rent, String tokenKey) {
		return restAdapter.create(IRent.class).updateRent(rent, tokenKey);
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
	
	public static RentsCounter getUserAddedRents(int pageSize, String tokenKey) {
		return restAdapter.create(IUserAddedRents.class).getUserAddedRents(pageSize, tokenKey);
	}
	
	public static List<Rent> getUserAddedRentsNextPage(Date lastRentDate, int lastRentId,
			int pageSize, String tokenKey) {
		return restAdapter.create(IUserAddedRents.class).getUserAddedRentsNextPage(
				DateUtil.standardFormat(lastRentDate), lastRentId, pageSize, tokenKey);
	}
	
	public static int deleteUserAddedRents(List<Integer> rentIds, String tokenKey) {
		return restAdapter.create(IUserAddedRents.class).deleteUserAddedRents(rentIds, tokenKey);
	}
	
	public static boolean addRentToFavorites(int rentId, String tokenKey) {
		return restAdapter.create(IUserFavorites.class).addRentToFavorites(rentId, tokenKey);
	}

	public static RentFavoriteViewsCounter getUserFavoriteRents(int pageSize, String tokenKey) {
		return restAdapter.create(IUserFavorites.class).getUserFavoriteRents(pageSize, tokenKey);
	}
	
	public static List<RentFavoriteView> getUserFavoriteRentsNextPage(Date lastDate, int pageSize,
			String tokenKey) {
		return restAdapter.create(IUserFavorites.class).getUserFavoriteRentsNextPage(
				DateUtil.standardFormat(lastDate), pageSize, tokenKey);
	}

	public static int deleteUserFavoriteRents(List<Integer> rentIds, String tokenKey) {
		return restAdapter.create(IUserFavorites.class).deleteUserFavoriteRents(rentIds, tokenKey);
	}
}
