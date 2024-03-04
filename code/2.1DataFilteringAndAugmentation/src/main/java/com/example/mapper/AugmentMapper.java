package com.example.mapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AugmentMapper extends Mapper<LongWritable, Text, Text, Text> {
    
	private final Map<String, String> geoGraphicMetadata = new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException {
        Configuration conf = context.getConfiguration();
        String stateFilePath = conf.get("geographic.metadata.path");

        loadGeoGraphicMetadata(conf, stateFilePath);
    }

	private void loadGeoGraphicMetadata(Configuration conf, String hdfsFilePath)throws IOException {
		Path path = new Path(hdfsFilePath);
        FileSystem fs = FileSystem.get(conf);
        
    	try (BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
            	int firstCommaIndex = line.indexOf(",");
                if (firstCommaIndex == -1) {
                   
                    return;
                }

                String stationId = line.substring(0, firstCommaIndex).trim();
                String remainingData = line.substring(firstCommaIndex + 1).trim();
                
                geoGraphicMetadata.put(stationId, remainingData);
            	
            	
            }
        }
		
	}
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	    String line = value.toString();
	    // Split the input line at the first comma to separate the station ID from the rest
	    int firstCommaIndex = line.indexOf(",");
	    if (firstCommaIndex == -1) {
	        // Log or handle the error if needed
	        return;
	    }

	    String stationId = line.substring(0, firstCommaIndex).trim();
	    String remainingData = line.substring(firstCommaIndex + 1).trim();
	    String geographicInformation = geoGraphicMetadata.getOrDefault(stationId, "N/A,N/A,N/A"); // Assuming format: state,location,country

	    // Concatenate the original data with the geographic information
	    String augmentedData = remainingData + "," + geographicInformation;

	    // Emit the augmented data
	    context.write(new Text(stationId), new Text(augmentedData));
	}

    
    
    
    
    
}

