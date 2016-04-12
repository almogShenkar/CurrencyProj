package com.almog.project;

/**
*this interface include the three basic methods in order to get data of requested conversions
*
*/
public interface ModelInterFace {

	/**
	*@param from the currency code
	*@param amount the requested amount
	*@param to the currency code that should convert to
	*@return the correct result
	*/
	public abstract double Convert(String from, double amuont, String to);
	
	/**
	*
	*@return the date of the xml file
	*/
	public abstract String getDate();// return the last update date
	
	/**
	*
	*@return array of string that hold the names of the country and they rate
	*/
	public abstract String[] getData();// return data from sever/xml..
}
