package com.wipro.Utility;

/**
 * ApplicationConstants provides the constants required for in application
 * @author Pravin Madavi on 7/16/2016.
 * @version 1.0
 */
public class ApplicationConstants {
    //holds the server url to hit to get JSON data
    public static String SERVER_URL="https://dl.dropboxusercontent.com/u/746330/facts.json";

    //To show not available in list item if title,description is null or empty
    public static String NOT_AVAILABLE="Not Available";

    //To set Title to Progress Dialog
    public static String ALERT="ALERT";

    //message to show when internet is not available
    public static String INTERNET_NOT_AVAILABLE="Internet not available, Cross check your internet connectivity and try again";

    //message to show while updating/refreshing list
    public static String PLEASE_WAIT= "Please wait, updating list";

    //if Error occurs while fetching data from server
    public static String ERROR_OCCURED= "Error occured while fetching data from server";

    //OK button text
    public static String OK= "OK";
}
