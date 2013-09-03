package com.personal.rents.rest.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.android.gms.maps.model.LatLng;

public class RentsRESTClient {
	
	public static List<LatLng> getRentsPositions(LatLng position0, LatLng position1) {
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
		double x[] = new double[size];
		double y[] = new double[size];
		List<LatLng> rentsPositions = new ArrayList<LatLng>(size);
		Random random = new Random();
		for(int i = 0; i < size; i++) {
			x[i] = x0 + (x1 - x0) * random.nextDouble();
			y[i] = y0 + (y1 - y0) * random.nextDouble();
			rentsPositions.add(new LatLng(y[i], x[i]));
		}
		
		return rentsPositions;
	}
}
