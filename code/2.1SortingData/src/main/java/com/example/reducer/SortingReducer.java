package com.example.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.example.mapper.CompositeKey;

import java.io.IOException;

public class SortingReducer extends Reducer<CompositeKey, Text, Text, Text> {
    @Override
    protected void reduce(CompositeKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            context.write(null, value); // Emit the sorted record
        }
    }
}
