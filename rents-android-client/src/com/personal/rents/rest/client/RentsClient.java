package com.personal.rents.rest.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

import com.google.android.gms.maps.model.LatLng;
import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.api.IAddRent;
import com.personal.rents.rest.api.IAuthorization;
import com.personal.rents.rest.api.IChangePassword;
import com.personal.rents.rest.api.ILogin;
import com.personal.rents.rest.api.IMyResource;
import com.personal.rents.rest.api.ISignup;
import com.personal.rents.rest.api.IUploadImage;
import com.personal.rents.rest.util.WebserviceResponseStatus;

public class RentsClient {
	
	private static final int minPrice = 100;
	
	private static final int maxPrice = 20000;
	
	private static final String BASE_URL = "http://192.168.1.5:8080/rents-server/ws";

	private static final RestAdapter restAdapter;

	static {
		restAdapter = new RestAdapter.Builder().setServer(BASE_URL).build();
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
	
	public static String getResource() {
		String resource = restAdapter.create(IMyResource.class).getIt();
		
		return resource;
	}
	
	public static List<Rent> getRentsPositions(LatLng position0, LatLng position1) {
		double x0 = position0.longitude;
		double x1 = position1.longitude;
		double y0 = position0.latitude;
		double y1 = position1.latitude;
		
		double temp = x0;
		x0 = Math.min(x0, x1);
		x1 = Math.max(temp, x1);
		temp = y0;
		y0 = Math.min(y0, y1);
		y1 = Math.max(temp, y1);
		
		int size = 5;
		double x = 0;
		double y = 0;
		int price = 0;
		List<Rent> rents = new ArrayList<Rent>(size);
		Random random = new Random();
		Rent rent = null;
		for(int i = 0; i < size; i++) {
			x = x0 + (x1 - x0) * random.nextDouble();
			y = y0 + (y1 - y0) * random.nextDouble();
			price = minPrice + random.nextInt(maxPrice - minPrice);

			rent = new Rent();
			rent.address = new Address();
			rent.address.latitude = y;
			rent.address.longitude = x;
			rent.price = price;
			rents.add(rent);
		}
		
		return rents;
	}
}
