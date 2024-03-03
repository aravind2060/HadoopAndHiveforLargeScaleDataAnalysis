package com.example;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.example.mapper.AugmentMapper;
import com.example.reducer.AugmentReducer;

public class AugmentJobDriver {
    public static void main(String[] args) throws Exception {
    	if (args.length < 3) {
            System.err.println("Usage: AugmentJobDriver <input path> <output path> <GeoGraphic Metadata file> ");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        conf.set("geographic.metadata.path", args[2]);
        
        
        Job job = Job.getInstance(conf, "Augment GHCN Data");
        job.setJarByClass(AugmentJobDriver.class);
        job.setMapperClass(AugmentMapper.class);
        job.setReducerClass(AugmentReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
