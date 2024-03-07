package com.example.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.example.mapper.TemperatureAnalysisMapper;
import com.example.reducer.TemperatureAnalysisReducer;

public class TemperatureAnalysisDriver {

    public static void main(String[] args) throws Exception {
        System.out.println(args.length);
        if (args.length != 5) {
            System.err.println("Usage: TemperatureAnalysisDriver <countryCode> <startDate> <endDate> <inputPath> <outputPath>");
            System.exit(1);
        }

        Configuration conf = new Configuration();
        conf.setStrings("args", args);

        Job job = Job.getInstance(conf, "Temperature Analysis");
        job.setJarByClass(TemperatureAnalysisDriver.class);
        job.setMapperClass(TemperatureAnalysisMapper.class);
        job.setReducerClass(TemperatureAnalysisReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[3]));
        FileOutputFormat.setOutputPath(job, new Path(args[4]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

