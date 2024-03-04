package com.example.reducer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class ElementGapReducer extends Reducer<Text, Text, Text, NullWritable> {
	
//	private MultipleOutputs<Text, NullWritable> multipleOutputs;
//
//    @Override
//    protected void setup(Context context) {
//        multipleOutputs = new MultipleOutputs<>(context);
//    }
    
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // It's crucial to have the values sorted by date at this point
        LocalDate lastDate = null;
        boolean emitRecords = true;
        List<Text> cachedRecords = new ArrayList<>();

        for (Text value : values) {
            String[] parts = value.toString().split(",");
            LocalDate currentDate = LocalDate.parse(parts[1], DateTimeFormatter.BASIC_ISO_DATE);

            if (lastDate != null) {
                long gapDays = ChronoUnit.DAYS.between(lastDate, currentDate) - 1;
                if (gapDays > 10) {
                    emitRecords = false;
                    break; // Found a gap larger than 10 days for this element type
                }
            }
            cachedRecords.add(new Text(value));
            lastDate = currentDate;
        }

        if (emitRecords) {
            for (Text record : cachedRecords) {
                context.write(record, NullWritable.get());
            }
//        } else {
//            for (Text record : cachedRecords) {
//                // Emit to a 'reject' named output file in the 'reject' directory with records having gaps larger than 10 days
//                multipleOutputs.write("reject", record, NullWritable.get(), "rejects/rejects");
//            }
//        }
    }

//    @Override
//    protected void cleanup(Context context) throws IOException, InterruptedException {
//        multipleOutputs.close();
//    }
}
    }
    

