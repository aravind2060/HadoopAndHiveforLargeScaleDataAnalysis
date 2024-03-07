package com.example.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TemperatureComparisonMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Assuming input format is: stationId, date, value
        String[] parts = value.toString().split(",");
        // Emit key-value pairs with a common key for both files and a tag indicating the source
        context.write(new Text(parts[0]), new Text(value));
    }
}

