package com.example.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PrecipitationMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        // Split the line into its components
        String[] parts = line.split(",");

        if (parts.length < 9) {
            // Skip this record if it doesn't have all required parts
            return;
        }

        // Construct the composite key as StationID,Date
        String compositeKey = parts[0] + "," + parts[1];

        // Reconstruct the original line but ensure it's formatted consistently
        // No change needed here since we're taking the entire line as is
        String outputValue = line;

        // Emit the composite key and the original line as the value
        context.write(new Text(compositeKey), new Text(outputValue));
    }
}
