package com.example;
import java.util.HashMap;
import java.util.Map;

public class DataProcessor {

	private static String processForMexico(String line) {
	    String[] parts = line.split("\\s+");
	    if (parts.length < 5) return ""; // Basic validation

	    String stationId = parts[0];
	    StringBuilder locationBuilder = new StringBuilder(parts[4]);
	    for (int i = 5; i < parts.length; i++) {
	        // Adjust the regex to break on any sequence of digits or the tag "GSN"
	        if (parts[i].matches("GSN|\\d+")) break;
	        locationBuilder.append(" ").append(parts[i]);
	    }
	    String location = locationBuilder.toString().trim(); // Ensure to trim the location
	    String country = "MEXICO"; // Since this method is specifically for Mexico
	    String state = "MX"; // Placeholder for state, as explicit state codes are not present

	    return String.join(",", stationId, state, location, country);
	}

    
    private boolean isRequiredCountry(String stationId) {
    	return stationId.startsWith("US") || stationId.startsWith("CA") || stationId.startsWith("MX");
    }
    private static String countryNameOnCountryCode(String countryCode) {
    	
    	if(countryCode.contentEquals("US"))
    		return "United States";
    	else if(countryCode.contentEquals("CA"))
    		return "CANADA";
    	else if(countryCode.contentEquals("MX"))
    		return "MEXICO";
    	else
    		return "Unknown";
    }

    public static void main(String[] args) {
        String[] arr = {
                "MXM00076118  30.3667 -109.6833 1099.7    NACOZARI (SMN)                         76118",
                "MXM00076122  30.3670 -107.9500 1487.0    NUEVA CASAS GRANDES                    7612",
                "MXM00076225  28.6333 -106.0833 1428.0    CHIHUAHUA                      GSN     76225",
                "MXM00076160  29.0960 -111.0480  191.1    GENERAL IGNACIO P GARCIA INTL          76160",
                "MXM00076258  27.3670 -109.9330   40.0    CIUDAD OBREGON  SON.                   76258",
                "MXN00001003  21.8833 -102.7167 1639.8    CALVILLO (SMN)",
                "USR0000NCRN  34.7833  -76.8667    6.1 NC CROATAN NORTH CAROLINA"
        };

        for (String line : arr) {
            System.out.println(processForMexico(line));
        }
    }
}
