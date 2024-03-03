# Step 1: Filtering data without Augmentation
### GHCN-Daily Data Processing

This project processes GHCN-Daily weather data using Hadoop MapReduce, focusing on filtering records from 1974 to 2024 for specific elements (TMAX, TMIN, PRCP) and outputting them without augmentation.

### Components

- **Mapper (`GHCNMapper.java`)**: Filters records by year and element.
- **Reducer (`GHCNReducer.java`)**: Passes filtered records directly to output.
- **Driver (`GHCNDataProcessor.java`)**: Configures and runs the MapReduce job.

### Running the Job

Compile the Java classes into a JAR and run the MapReduce job with:

```sh
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/DataFiltering-0.0.1-SNAPSHOT.jar GHCNDataProcessor user/root/input/weather output/filter
```

- `<input_path>`: HDFS path to GHCN-Daily data.
- `<output_path>`: HDFS path for job output (must not exist prior).

### output is stored
  ```sh hadoop fs -cat output/*```
### Output Format

The output is a CSV with columns: StationID, Date, ElementType, Value.
