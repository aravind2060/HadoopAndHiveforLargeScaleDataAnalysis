package com.example.reducer;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class PrecipitationImputationReducer extends Reducer<Text, Text, Text, NullWritable> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Queue<Integer> lastValidPrecipitations = new LinkedList<>();
        for (Text val : values) {
            String[] parts = val.toString().split(",");
            // Assume parts[5] is the precipitation value index; adjust if necessary.
            int prcp;
            try {
                prcp = Integer.parseInt(parts[5]);
            } catch (NumberFormatException e) {
                // Handle the case where precipitation data is not an integer.
                prcp = 0; // Consider setting to 0 or some other default or error value.
            }
            
            String imputedValue = parts[5]; // Use original value by default
            // Check if precipitation data is missing (represented by zero)
            if (prcp == 0) {
                // Impute missing precipitation data if possible
                if (!lastValidPrecipitations.isEmpty()) {
                    int sum = 0;
                    for (int p : lastValidPrecipitations) {
                        sum += p;
                    }
                    prcp = sum / lastValidPrecipitations.size(); // Calculate average
                    imputedValue = prcp + ".001"; // Mark the imputed value with .001
                }
                // Note: If lastValidPrecipitations is empty, you might want to handle this case differently
            } else {
                // Update the queue of last three valid precipitations
                if (lastValidPrecipitations.size() >= 3) {
                    lastValidPrecipitations.poll(); // Remove the oldest if we already have 3
                }
                lastValidPrecipitations.offer(prcp);
            }
            
            // Construct the output string with imputed or original values
            parts[5] = imputedValue; // Update with imputed or original value
            String output = String.join(",", parts);
            
            context.write(new Text(output), NullWritable.get());
        }
    }
}
