package com.example.filtering;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;

public class GHCNMapper extends Mapper<LongWritable, Text, Text, Text> {
    private static final Log LOG = LogFactory.getLog(GHCNMapper.class);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] parts = line.split(",");
        
        if (parts.length < 4) {
            LOG.warn("Skipping malformed line: " + line);
            return;
        }

        String stationID = parts[0];
        // Check for US, CA, and MX stations only
        if (!(stationID.startsWith("US") || stationID.startsWith("CA") || stationID.startsWith("MX"))) {
            return;
        }

        try {
            int year = Integer.parseInt(parts[1].substring(0, 4));
            if ((parts[2].equals("TMAX") || parts[2].equals("TMIN") || parts[2].equals("TAVG") || parts[2].equals("PRCP"))) {
                context.write(new Text(stationID), new Text(parts[1] + "," + parts[2] + "," + parts[3]));
            }
        } catch (NumberFormatException e) {
            LOG.error("Error parsing year: " + parts[1], e);
        }
    }
}

