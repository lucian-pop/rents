package com.personal.rents.rest.api;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.POST;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentSearch;

public interface ISearchRents {

	@POST("/rents/search")
	public RentsCounter searchRents(@Body RentSearch rentSearch);
	
	@POST("/rents/search/page")
	public List<Rent> searchRentsNextPage(@Body RentSearch rentSearch);
}
