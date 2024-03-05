package com.example.reducer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ElementGapReducer extends Reducer<Text, Text, Text, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<Text> segmentRecords = new ArrayList<>();
        LocalDate lastDate = null;

        for (Text value : values) {
            String[] parts = value.toString().split(",");
            LocalDate currentDate = LocalDate.parse(parts[1], DateTimeFormatter.BASIC_ISO_DATE);

            if (lastDate != null && ChronoUnit.DAYS.between(lastDate, currentDate) > 3) {
                // Process the current segment
                emitRecords(segmentRecords, context);
                segmentRecords.clear(); // Start a new segment
            }

            segmentRecords.add(new Text(value));
            lastDate = currentDate;
        }

        // Process the final segment
        emitRecords(segmentRecords, context);
    }

    private void emitRecords(List<Text> records, Context context) throws IOException, InterruptedException {
        for (Text record : records) {
            context.write(record, NullWritable.get());
        }
    }
}

    

