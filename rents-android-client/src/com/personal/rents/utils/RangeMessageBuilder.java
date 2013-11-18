package com.personal.rents.utils;

public final class RangeMessageBuilder {

	private RangeMessageBuilder() {
	}
	
	public static String priceRangeMessageBuilder(int lowerValue, int upperValue, int minValue, 
			int maxValue) {
		return lowerValue + GeneralConstants.SPACE + GeneralConstants.EURO + GeneralConstants.SPACE + GeneralConstants.MINUS
				+ GeneralConstants.SPACE + upperValue + GeneralConstants.SPACE +  GeneralConstants.EURO;
	}
	
	public static String surfaceRangeMessageBuilder(int lowerValue, int upperValue, int minValue,
			int maxValue) {
		return lowerValue + GeneralConstants.SPACE + GeneralConstants.SQUARE_METERS + GeneralConstants.SPACE + GeneralConstants.MINUS
				+ GeneralConstants.SPACE + upperValue + GeneralConstants.SPACE +  GeneralConstants.SQUARE_METERS;
	}
}
