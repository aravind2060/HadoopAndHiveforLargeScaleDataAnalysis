package com.example.reducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TemperatureComparisonReducer extends Reducer<Text, Text, Text, Text> {
    private String region1;
    private String region2;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        // Retrieve country code, start date, and end date from command-line arguments
        String[] args = context.getConfiguration().getStrings("args");
//        if (args.length != 5) {
//            throw new IllegalArgumentException("Usage: <countryCode> <startDate> <endDate>");
//        }
        this.region1 = args[0];
        this.region2 = args[1];
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Collect values from both sources
        Map<String, String> valueMap = new TreeMap<>();
        List<String> region1Stations = new ArrayList<>();
        List<String> region2Stations = new ArrayList<>();
        for (Text value : values) {
            String[] parts = value.toString().split(",");
            String stationId = parts[0];
            if (stationId.startsWith(region1)) {
                region1Stations.add(value.toString());
            } else if (stationId.startsWith(region2)) {
                region2Stations.add(value.toString());
            }
        }
        String region1Averages = calculateAverage(region1Stations);
        String region2Averages = calculateAverage(region2Stations);

        context.write(key, new Text(this.region1 + "," + this.region2 + "," +
                calculateDifference(region1Averages, region2Averages)));
    }

    private String calculateDifference(String region1Averages, String region2Averages) {
        String[] region1Parts = region1Averages.split(",");
        String[] region2Parts = region2Averages.split(",");
        int tminDiff = Integer.parseInt(region1Parts[0]) - Integer.parseInt(region2Parts[0]);
        int tmaxDiff = Integer.parseInt(region1Parts[1]) - Integer.parseInt(region2Parts[1]);
        int tavgDiff = Integer.parseInt(region1Parts[2]) - Integer.parseInt(region2Parts[2]);
        return tminDiff + "," + tmaxDiff + "," + tavgDiff;
    }

    private static String calculateAverage(List<String> regionList) {
        try {
            int tminSum = 0;
            int tmaxSum = 0;
            int tavgSum = 0;

            for (String temp : regionList) {
                String[] parts = temp.split(",");
                tminSum += Integer.parseInt(parts[2]);
                tmaxSum += Integer.parseInt(parts[3]);
                tavgSum += Integer.parseInt(parts[4]);
            }

            int size = regionList.size();
            if (size == 0) {
                throw new ArithmeticException("Division by zero error: regionList size is zero.");
            }

            int tminAvg = tminSum / size;
            int tmaxAvg = tmaxSum / size;
            int tavgAvg = tavgSum / size;

            return tminAvg + "," + tmaxAvg + "," + tavgAvg;
        } catch (NumberFormatException e) {
            // Handle NumberFormatException
            return "1,1,1";
        } catch (ArithmeticException e) {
            // Handle DivisionByZeroException
            return "1,1,1";
        }
    }

}
