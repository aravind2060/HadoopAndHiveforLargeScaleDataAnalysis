package com.example.mapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class GhcndStationMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if (line.startsWith("US") || line.startsWith("CA")) {
            parseUSCAStation(line, context);
        } else if (line.startsWith("MX")) {
            parseMXStation(line, context);
        }
    }

    private void parseUSCAStation(String line, Context context) throws IOException, InterruptedException {
    	// Example Line: USP00CA0001 38.5553 -121.4183 11.6 CA SACRAMENTO 5ESE
        String[] parts = line.split("\\s+");
        if (parts.length < 6) return; // Basic validation

        String stationId = parts[0];
        String state = parts[4];
        StringBuilder locationBuilder = new StringBuilder();
        for (int i = 5; i < parts.length; i++) {
            if (parts[i].matches("GSN|\\d{5}") || parts[i].matches("HCN|\\d{5}") || parts[i].matches("CRN|\\d{5}")) break; // Stop if metadata or code is encountered
            if (locationBuilder.length() > 0) locationBuilder.append(" ");
            locationBuilder.append(parts[i]);
        }
        String location = locationBuilder.toString();
        String country="N/A";
        if(line.startsWith("US"))
        	country="UNITED STATES";
        else
        	country ="CANADA";
        context.write(new Text(stationId), new Text(String.join(",", state, location,country)));
    }
    private void parseMXStation(String line, Context context) throws IOException, InterruptedException {
        // Example Line: MXM00076118 30.3667 -109.6833 1099.7 NACOZARI (SMN) 76118
    	 String[] parts = line.split("\\s+");
 	    if (parts.length < 5) return; // Basic validation

 	    String stationId = parts[0];
 	    StringBuilder locationBuilder = new StringBuilder(parts[4]);
 	    for (int i = 5; i < parts.length; i++) {
 	        // Adjust the regex to break on any sequence of digits or the tag "GSN"
 	        if (parts[i].matches("GSN|\\d{5}") || parts[i].matches("HCN|\\d{5}") || parts[i].matches("CRN|\\d{5}")) break;
 	        locationBuilder.append(" ").append(parts[i]);
 	    }
 	    String location = locationBuilder.toString().trim(); // Ensure to trim the location
 	    String country = "MEXICO"; // Since this method is specifically for Mexico
 	    String state = "MX"; // Placeholder for state, as explicit state codes are not present

 	    
        context.write(new Text(stationId), new Text("MX," + location+","+country)); // Assuming "MX" as a placeholder for the state code
    }
}
