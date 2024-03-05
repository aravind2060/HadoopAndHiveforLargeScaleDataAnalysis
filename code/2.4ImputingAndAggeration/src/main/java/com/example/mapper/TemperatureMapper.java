package com.example.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class TemperatureMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        // Find the indexes of the first two commas to split the line into the key part and the rest
        int firstCommaIndex = line.indexOf(',');
        int secondCommaIndex = line.indexOf(',', firstCommaIndex + 1);

        if (firstCommaIndex == -1 || secondCommaIndex == -1) {
            // Skip the line if it doesn't contain enough commas to split correctly
            return;
        }

        // Extract the station ID and date as the composite key
        String compositeKey = line.substring(0, secondCommaIndex).trim();

        // The remaining data starts from the element type to the country
        String remainingData = line.substring(secondCommaIndex + 1).trim();

        // Emitting the composite key (station ID and date) and the remaining data as value
        context.write(new Text(compositeKey), new Text(remainingData));
    }
}
