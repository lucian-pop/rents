package com.personal.rents.utils;

public final class RangeMessageBuilder {

	private RangeMessageBuilder() {
	}
	
	public static String priceRangeMessageBuilder(int lowerValue, int upperValue, int minValue, 
			int maxValue) {
		return lowerValue + Constants.SPACE + Constants.EURO + Constants.SPACE + Constants.MINUS
				+ Constants.SPACE + upperValue + Constants.SPACE +  Constants.EURO;
	}
	
	public static String surfaceRangeMessageBuilder(int lowerValue, int upperValue, int minValue,
			int maxValue) {
		return lowerValue + Constants.SPACE + Constants.SQUARE_METERS + Constants.SPACE + Constants.MINUS
				+ Constants.SPACE + upperValue + Constants.SPACE +  Constants.SQUARE_METERS;
	}
}
