package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.example.mapper.TemperatureMapper;
import com.example.reducer.TemperatureImputationReducer;

public class TemperatureDataProcessingDriver {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: TemperatureDataProcessingDriver <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Temperature Data Processing");

        job.setJarByClass(TemperatureDataProcessingDriver.class);
        // Assuming MyMapper and MyReducer are your custom classes
        job.setMapperClass(TemperatureMapper.class);
        job.setReducerClass(TemperatureImputationReducer.class);

        job.setMapOutputKeyClass(Text.class); // Change as per your implementation
        job.setMapOutputValueClass(Text.class); // Change as per your implementation

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
