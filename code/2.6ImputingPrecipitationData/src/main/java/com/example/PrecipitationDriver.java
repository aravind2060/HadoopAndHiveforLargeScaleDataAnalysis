package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.example.mapper.PrecipitationMapper;
import com.example.reducer.PrecipitationImputationReducer;

public class PrecipitationDriver {

    public static void main(String[] args) throws Exception {
        // Check if input and output paths have been passed as arguments
        if (args.length != 2) {
            System.err.println("Usage: PrecipitationDriver <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Precipitation Processing");

        // Specify the jar file that contains the driver, mapper, and reducer
        job.setJarByClass(PrecipitationDriver.class);
        
        // Set the mapper and reducer classes
        job.setMapperClass(PrecipitationMapper.class);
        job.setReducerClass(PrecipitationImputationReducer.class); // Assuming you're using the same reducer for imputation

     // For the mapper
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class); // Ensure this matches the type being emitted by the mapper.

        // Specify the types of the final output key and value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // Set the paths of input and output directories
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Exit with a success or failure notification
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
