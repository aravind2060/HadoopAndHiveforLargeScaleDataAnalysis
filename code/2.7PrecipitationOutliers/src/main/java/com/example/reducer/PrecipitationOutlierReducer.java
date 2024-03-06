package com.example.reducer;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrecipitationOutlierReducer extends Reducer<Text, Text, Text, NullWritable> {

    // Assuming precipitation data is at index 5 based on the given format
    private static final int PRCP_INDEX = 5;

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<String[]> records = new ArrayList<>();
        for (Text value : values) {
            records.add(value.toString().split(","));
        }

        for (int i = 0; i < records.size(); i++) {
            String[] currentRecord = records.get(i);
            double prcpAvg = calculatePreviousThreeDaysAverage(records, i, PRCP_INDEX);

            // Adjusting precipitation value for the current record
            currentRecord[PRCP_INDEX] = String.valueOf(prcpAvg);
            
         // Remove .0 at the end of each value if present
            for (int j = 0; j < currentRecord.length; j++) {
                if (currentRecord[j].endsWith(".0")) {
                    currentRecord[j] = currentRecord[j].substring(0, currentRecord[j].length() - 2);
                }
            }


            String adjustedRecord = String.join(",", currentRecord);
            context.write(new Text(adjustedRecord), NullWritable.get());
        }
    }

    private double calculatePreviousThreeDaysAverage(List<String[]> records, int currentIndex, int prcpIndex) {
        double sum = 0;
        int validCount = 0;
        for (int i = Math.max(0, currentIndex - 3); i < currentIndex; i++) {
            String valueStr = records.get(i)[prcpIndex];
            try {
                double value = Double.parseDouble(valueStr);
                if (value != 0) { // Assuming zero is used to indicate missing data
                    sum += value;
                    validCount++;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing precipitation value at " + i + ": " + e.getMessage());
            }
        }
        
        // If there's no valid previous data, handle according to your criteria
        if (validCount == 0) {
            // Return the current value or handle as needed
            return Double.parseDouble(records.get(currentIndex)[prcpIndex]);
        }
        return sum / validCount; // Return average if there's valid previous data
    }


}
