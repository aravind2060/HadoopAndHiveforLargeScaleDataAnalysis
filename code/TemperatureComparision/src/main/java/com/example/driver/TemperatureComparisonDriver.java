package com.example.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.example.mapper.TemperatureComparisonMapper;
import com.example.reducer.TemperatureComparisonReducer;

import java.io.IOException;

public class TemperatureComparisonDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        if (args.length != 4) {
            System.err.println("Usage: TemperatureAnalysisDriver <region1> <region2> <inputPath> <outputPath>");
            System.exit(1);
        }

        Configuration conf = new Configuration();
        conf.setStrings("args", args);
        Job job = Job.getInstance(conf, "Region Temperature Comparison");

        job.setJarByClass(TemperatureComparisonDriver.class);
        job.setMapperClass(TemperatureComparisonMapper.class);
        job.setReducerClass(TemperatureComparisonReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[2]));
        FileOutputFormat.setOutputPath(job, new Path(args[3]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}


