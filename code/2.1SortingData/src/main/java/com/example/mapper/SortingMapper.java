package com.example.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


import java.io.IOException;

public class SortingMapper extends Mapper<LongWritable, Text, CompositeKey, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split(",");

        if (parts.length >= 7) { // Ensure the record has enough parts
            String stationId = parts[0];
            String date = parts[1];
            String elementType = parts[2];
            CompositeKey compositeKey = new CompositeKey(stationId, date, elementType);
            context.write(compositeKey, value); // Emit the entire record as the value
        }
    }
}
