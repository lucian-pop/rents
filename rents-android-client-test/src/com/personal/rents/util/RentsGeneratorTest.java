package com.personal.rents.util;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.personal.rents.model.Rent;

import android.test.AndroidTestCase;

public class RentsGeneratorTest extends AndroidTestCase {
	
	public void testGenerateRents() {
		LatLng southwest = new LatLng(46.74574029016937, 23.572274073958397);
		LatLng northeast = new LatLng(46.75952613288869, 23.58546920120716);
		
		List<LatLng> positions = RentsGenerator.generatePositions(southwest, northeast);
		assertTrue(positions.size() > 0);
		
		List<Rent> rents = RentsGenerator.generateRents(positions, getContext());
		assertTrue(rents.size() > 0);
	}
	
}
