package com.example.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class AugmentReducer extends Reducer<Text, Text, Text, Text> {
    

	@Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
    	
    	 for (Text value : values) {
    	        // Assuming 'key' is the station ID and 'value' contains the state, location, and country, separated by commas.
    	        // Ensure to add a comma after the station ID before writing it out.
    	        String outputValue = key.toString() + "," + value.toString();

    	        // Write out the station ID and the concatenated string.
    	        context.write(null, new Text(outputValue));
    	    }
    }
}
