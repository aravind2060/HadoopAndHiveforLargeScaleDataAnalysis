package com.example.reducing;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class GHCNReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) 
            throws IOException, InterruptedException {
        // Directly write the values without augmentation
        for (Text value : values) {
            // Construct an output format as StationID,Date,ElementType,Value
            // Assuming value already comes in the format: Date,ElementType,Value
            String outputValue = key.toString() + "," + value.toString();
            
            // Writing the output value, using a null key to avoid having a key in the output file
            context.write(null, new Text(outputValue));
        }
    }
}

