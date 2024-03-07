package com.example.mapper;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TemperatureAnalysisMapper extends Mapper<LongWritable, Text, Text, Text> {

    private final Text outputKey = new Text();
    private final Text outputValue = new Text();
    private String countryCode;
    private String startDate;
    private String endDate;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        // Retrieve country code, start date, and end date from command-line arguments
        String[] args = context.getConfiguration().getStrings("args");
        if (args.length != 5) {
            throw new IllegalArgumentException("Usage: <countryCode> <startDate> <endDate>");
        }
        this.countryCode = args[0];
        this.startDate = args[1];
        this.endDate = args[2];
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] parts = value.toString().split(",");
        String stationId = parts[0];
        String date = parts[1];
        if (stationId.startsWith(countryCode) && isDateInRange(date)) {
            int tmin = Integer.parseInt(parts[2]);
            int tmax = Integer.parseInt(parts[3]);
            int tavg = Integer.parseInt(parts[4]);
            outputKey.set(stationId);
            outputValue.set(date + "," + tmin + "," + tmax + "," + tavg);
            context.write(outputKey, outputValue);
        }
    }

    private boolean isDateInRange(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date parsedDate = sdf.parse(date);
            Date parsedStartDate = sdf.parse(this.startDate);
            Date parsedEndDate = sdf.parse(this.endDate);
            return !parsedDate.before(parsedStartDate) && !parsedDate.after(parsedEndDate);
        } catch (ParseException e) {
            // Handle parsing exception
            return false;
        }
    }
}