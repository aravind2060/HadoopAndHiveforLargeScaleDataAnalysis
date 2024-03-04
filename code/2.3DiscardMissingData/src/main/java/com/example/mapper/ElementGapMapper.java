package com.example.mapper;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ElementGapMapper extends Mapper<Object, Text, Text, Text> {
    private Text compositeKey = new Text();
    private Text recordValue = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split(",");
        // Assuming station ID is the first part and element type is the third part of the record
        String stationId = parts[0];
        String elementType = parts[2];
        compositeKey.set(stationId + "," + elementType);
        recordValue.set(value);
        context.write(compositeKey, recordValue);
    }
}
