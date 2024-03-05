package com.example.reducer;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemperatureImputationReducer extends Reducer<Text, Text, Text, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Map<String, Integer> temperatureData = new HashMap<>();
        temperatureData.put("TMAX", null);
        temperatureData.put("TMIN", null);
        temperatureData.put("TAVG", null);
        String state = "", location = "", country = "";
        String prcp = "0";

        for (Text val : values) {
            String[] parts = val.toString().split(",");
            String elementType = parts[0];
            String elementValue = parts[1];
            if (elementType.equals("PRCP")) {
                prcp = elementValue; // Assume precipitation data is directly used without imputation
                continue;
            }
            if (parts.length > 2) {
                // Assumes state, location, and country are provided only once and in the last part of the input
                state = parts[2];
                location = parts[3];
                country = parts[4];
            }
            temperatureData.put(elementType, Integer.parseInt(elementValue));
        }

        // Impute missing temperature data
        Integer tmin = temperatureData.get("TMIN");
        Integer tmax = temperatureData.get("TMAX");
        Integer tavg = temperatureData.get("TAVG");

        if (tmin == null && tmax != null && tavg != null) {
            tmin = (2 * tavg) - tmax; // TMIN = 2*TAVG - TMAX
        } else if (tmax == null && tmin != null && tavg != null) {
            tmax = (2 * tavg) - tmin; // TMAX = 2*TAVG - TMIN
        } else if (tavg == null && tmin != null && tmax != null) {
            tavg = (tmin + tmax) / 2; // TAVG = (TMIN + TMAX) / 2
        }

        // Construct the output string with imputed values
        String output = String.join(",", key.toString(),
        		                         tmin == null ? "0" : tmin.toString(),
                                         tmax == null ? "0" : tmax.toString(),
                                         tavg == null ? "0" : tavg.toString(),
                                         prcp,
                                         state,
                                         location,
                                         country);

        context.write(new Text(output), NullWritable.get());
    }
}
