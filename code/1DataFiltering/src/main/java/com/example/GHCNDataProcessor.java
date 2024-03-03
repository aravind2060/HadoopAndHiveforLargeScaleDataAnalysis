package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.example.filtering.GHCNMapper;
import com.example.reducing.GHCNReducer;

public class GHCNDataProcessor {

    public static void main(String[] args) throws Exception {
        // Check for correct usage
        if (args.length != 2) {
            System.err.println("Usage: GHCNDataProcessor <input path> <output path>");
            System.exit(-1);
        }

        // Create a configuration and a job instance
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "GHCN Data Processor");

        // Specify the jar file that contains the driver, mapper, and reducer classes
        job.setJarByClass(GHCNDataProcessor.class);

        // Set mapper and reducer classes
        job.setMapperClass(GHCNMapper.class);
        job.setReducerClass(GHCNReducer.class);

        // Set output key and value types for the reducer
        job.setOutputKeyClass(Text.class); // Use Text.class if you're writing a null key in reducer
        job.setOutputValueClass(Text.class);

        // Set paths for input and output directories in HDFS
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Exit after job completion with a success or failure message
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

