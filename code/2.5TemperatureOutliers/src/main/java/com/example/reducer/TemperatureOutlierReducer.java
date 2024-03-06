package com.example.reducer;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Current Functionality:
//
//Gathers records for a station and date: It iterates through values for a given key (station and date).
//Calculates averages for TMIN, TMAX, and TAVG: It uses the calculateAverage function to get the mean for each temperature column within a window of ±3 days, excluding the current day.
//Applies averages to all records (without outlier detection): It currently replaces all temperature values with the calculated averages, even if they're not outliers. This is a placeholder for actual outlier detection.

public class TemperatureOutlierReducer extends Reducer<Text, Text, Text, NullWritable> {

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		List<String[]> records = new ArrayList<>();
		for (Text value : values) {
			records.add(value.toString().split(","));
		}

		for (int i = 0; i < records.size(); i++) {
			String[] currentRecord = records.get(i);
			double tminAvg = calculateAverage(records, i, 2); // Assuming TMIN is at index 2
			double tmaxAvg = calculateAverage(records, i, 3); // Assuming TMAX is at index 3
			double tavgAvg = calculateAverage(records, i, 4); // Assuming TAVG is at index 4

			//todo outlier detection by setting a threshold and using Inter quartile range and standard deviation
			// Adjust current record values based on outlier detection
			// we are adjusting every value without actually checking outlier
			// .001 is for visual indication
			currentRecord[2] = String.valueOf(tminAvg); // TMIN
			currentRecord[3] = String.valueOf(tmaxAvg); // TMAX
			currentRecord[4] = String.valueOf(tavgAvg); // TAVG

			String adjustedRecord = String.join(",", currentRecord);
			context.write(new Text(adjustedRecord), NullWritable.get());
		}
	}

	private double calculateAverage(List<String[]> records, int currentIndex, int tempIndex) {
	    double sum = 0;
	    int validCount = 0; // Counts only valid (numeric) temperature records

	    // Ensure the calculation only considers valid numeric entries for the given temperature index.
	    try {
	        for (int i = Math.max(0, currentIndex - 3); i <= Math.min(currentIndex + 3, records.size() - 1); i++) {
	            if (i != currentIndex) { // Skipping the current record for average calculation
	                String tempValue = records.get(i)[tempIndex].trim();
	                if (!tempValue.isEmpty()) {
	                    double value = Double.parseDouble(tempValue);
	                    sum += value;
	                    validCount++;
	                }
	            }
	        }
	    } catch (NumberFormatException nfe) {
	        // Log or handle the error
	        System.err.println("Error parsing temperature value at index: " + tempIndex + " - " + nfe.getMessage());
	    }

	    // Return the current record's value if no valid surrounding records were found; otherwise, calculate the average.
	    return validCount > 0 ? sum / validCount : Double.parseDouble(records.get(currentIndex)[tempIndex]);
	}



}
