package com.example.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TemperatureOutlierMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split(",");

        // Check for correct number of parts to avoid index out of bounds error
        if (parts.length != 9) {
            // Possibly log error or increment a counter
            return;
        }

        // Construct composite key (StationID,Date) and value part with relevant data
        String compositeKey = parts[0] + "," + parts[1]; // StationID,Date as key
        // Emit with full record as value for now; adjustments can be made later if needed
        context.write(new Text(compositeKey), new Text(String.join(",", parts)));
    }
}


