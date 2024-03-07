package com.example.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TemperatureAnalysisReducer extends Reducer<Text, Text, Text, Text> {

    private final Text outputValue = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        Map<Date, List<String>> dateMap = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        for (Text value : values) {
            String[] parts = value.toString().split(",");
            Date date = null;
            try {
                date = sdf.parse(parts[0].substring(0, 6));
            } catch (ParseException e) {
                System.out.println("Error parsing date: " + parts[0]);
            }
            String tmin = parts[1];
            String tmax = parts[2];
            String tavg = parts[3];
            List<String> tempList = dateMap.getOrDefault(date, new ArrayList<>());
            tempList.add(tmin + "," + tmax + "," + tavg);
            dateMap.put(date, tempList);
        }

        for (Map.Entry<Date, List<String>> entry : dateMap.entrySet()) {
            Date date = entry.getKey();
            List<String> tempList = entry.getValue();
            int tminSum = 0;
            int tmaxSum = 0;
            int tavgSum = 0;
            for (String temp : tempList) {
                String[] parts = temp.split(",");
                tminSum += Integer.parseInt(parts[0]);
                tmaxSum += Integer.parseInt(parts[1]);
                tavgSum += Integer.parseInt(parts[2]);
            }
            int tminAvg = tminSum / tempList.size();
            int tmaxAvg = tmaxSum / tempList.size();
            int tavgAvg = tavgSum / tempList.size();
            outputValue.set(key+","+sdf.format(date) + "," + tminAvg + "," + tmaxAvg + "," + tavgAvg);
            context.write(null,outputValue);
        }
    }
}