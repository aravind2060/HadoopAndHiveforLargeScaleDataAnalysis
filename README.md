# Project: Analyzing Climate Trends in North America with Cloud Computing

## Learning Outcomes

- **Master Hadoop and Hive:** Hands-on experience with big data tools for large-scale data analysis.
- **Data Wrangling Fundamentals:** Acquire crucial skills in data cleaning and preparation.
- **Advanced Data Analysis:** Conduct temperature and precipitation analysis, identify extreme weather events, and evaluate long-term climate trends.
- **Critical Thinking and Problem-Solving:** Enhance abilities to make data-driven decisions and address challenges.
- **Collaboration and Communication:** Improve teamwork skills and effectively communicate complex findings.

## Overview of Taskss

1. **Data Quality (MapReduce):** Preparation and cleaning of data for analysis. This includes filtering, handling missing values, and addressing outliers.
2. **Temperature Analysis (MapReduce):** Analyze temperature data to calculate average minimum, maximum, and average temperatures for specified locations and periods.
3. **Extreme Weather Events (Hive):** Utilize HiveQL for identifying and analyzing extreme weather events based on predefined criteria.
4. **Precipitation Analysis (MapReduce):** Analyze precipitation data, calculate totals, and identify droughts or heavy rainfall periods.
5. **Climate Change Trends (MapReduce or Hive):** Examine long-term trends in temperature and precipitation, identifying patterns and variations over time.

## Problem 1: Data Preparation and Quality Assurance

### Objective
Ensure the GHCNd dataset is ready for analysis, focusing on accuracy and completeness.

### Task 1: Environment Setup and Data Acquisition

1. **Install Hadoop and Hive**
   - Download Hadoop on docker
   
2. **Prepare GHCNd Dataset**
   - Download dataset: `https://www.ncei.noaa.gov/pub/data/ghcn/daily/by_year/`
   - Unzip dataset: `unzip ghcnd-data.zip`
   - Upload dataset to HDFS: `hdfs dfs -put local_path /hdfs_path`

### Task 2: Data Filtering and Augmentation

#### MapReduce Option for Filtering
- **Mapper (Filtering):**
  - Input: Raw GHCNd data.
  - Process: Parse each record; if `date` within range and `location` matches, emit to reducer.
  - Pseudocode:
    ```
    def map(key, value):
        record = parse(value)
        if record.date in range and record.location in locations:
            emit(record.location, record)
    ```
- **Reducer (Aggregation):**
  - Input: Filtered records from Mapper.
  - Process: Aggregate records by location; combine data with metadata.
  - Pseudocode:
    ```
    def reduce(key, values):
        aggregated_data = aggregate(values)
        emit(key, aggregated_data)
    ```

#### HiveQL Option for Filtering and Augmentation
- **Create Tables and Query:**
  - Create table for GHCNd data and metadata.
    ```sql
    CREATE TABLE ghcnd_data (station_id STRING, date STRING, metric STRING, value DOUBLE)
    STORED AS TEXTFILE;
    ```
  - Load data into table: `LOAD DATA INPATH '/hdfs_path/ghcnd_data' INTO TABLE ghcnd_data;`
  - Filtering and joining data with metadata:
    ```sql
    SELECT d.*, m.location
    FROM ghcnd_data d
    JOIN metadata m ON (d.station_id = m.station_id)
    WHERE d.date BETWEEN '1974-01-01' AND '2024-12-31';
    ```

### Task 3: Handling Missing or Incomplete Data

#### MapReduce Option for Missing Data
- **Mapper (Identifying Missing Data):**
  - Process: Check each record for missing fields; emit indicators for missingness.
- **Reducer (Handling Missing Data):**
  - Process: Apply imputation strategy based on the field and context (mean, median, etc.).

#### HiveQL Option for Missing Data
- **Imputation Query:**
  - Identify missing data: `SELECT station_id, COUNT(*) FROM ghcnd_data WHERE value IS NULL GROUP BY station_id;`
  - Impute missing data (example for temperature): 
    ```sql
    CREATE TABLE ghcnd_imputed AS
    SELECT station_id, date, metric,
    CASE
        WHEN value IS NULL THEN (SELECT AVG(value) FROM ghcnd_data)
        ELSE value
    END as value
    FROM ghcnd_data;
    ```

### Task 4: Outlier Detection and Correction

#### MapReduce Option for Outliers
- **Mapper (Detecting Outliers):**
  - Process: Calculate local statistics and identify outliers based on standard deviation.
- **Reducer (Correcting Outliers):**
  - Process: Adjust outliers using global statistics or predefined thresholds.

#### HiveQL Option for Outliers
- **Outlier Handling Query:**
  - Calculate statistics and flag outliers: 
    ```sql
    SELECT station_id, value,
    CASE
        WHEN value > (SELECT AVG(value) + 3 * STDDEV(value) FROM ghcnd_data) THEN 'outlier'
        ELSE 'normal'
    END
    FROM ghcnd_data;
    ```

### Task 5: Final Data Quality Assurance

- **HiveQL for Data Quality Checks:**
  - Perform consistency checks: `SELECT COUNT(*) FROM ghcnd_imputed WHERE value IS NULL;`
  - Data distribution analysis to identify anomalies post-cleaning.



## Problem 2: Temperature Analysis

### Overview
Conduct an in-depth analysis of temperature trends within the GHCNd dataset focusing on average temperature calculations, trend analysis, and comparative temperature analysis across different regions.

### Task 1: Temperature Data Extraction and Preprocessing

- **MapReduce Approach:**
  - **Map Phase:** Extract relevant temperature data based on criteria (date, location).
  - **Reduce Phase:** Clean and preprocess the data, normalizing formats and resolving inconsistencies.

- **HiveQL Approach:**
  - Create a Hive table for temperature data.
  - Execute a HiveQL query to select and preprocess relevant records.

### Task 2: Average Temperature Calculation

- **MapReduce Approach:**
  - **Map Phase:** Map each temperature record to its corresponding time unit (day, month).
  - **Reduce Phase:** Calculate the average temperature for each time unit.

- **HiveQL Approach:**
  - Use HiveQL to directly calculate average temperatures by time unit with a single query.

### Task 3: Temperature Trend Analysis

- **MapReduce Approach:**
  - **Map Phase:** Tag temperature data with year for trend analysis.
  - **Reduce Phase:** Analyze changes in temperature over years to identify trends.

- **HiveQL Approach:**
  - Apply HiveQL window functions to calculate moving averages and identify trends.

### Task 4: Comparative Temperature Analysis

- **MapReduce Approach:**
  - **Map Phase:** Identify temperature records for specific regions to be compared.
  - **Reduce Phase:** Calculate statistical measures (e.g., average, max) for comparison.

- **HiveQL Approach:**
  - Use HiveQL queries to compare temperature data across different geographic regions.


## Problem 3: Extreme Weather Events Analysis

### Overview
Utilize Hive to identify and analyze extreme weather events within the GHCNd dataset, focusing on heatwaves and cold spells across different geographic locations and time frames.

### Task 1: Setup Hive Tables for Weather Data

- **Create Hive Tables:**
  - Design a Hive table schema that includes daily temperature and precipitation data, along with geographic and temporal identifiers.
  - Load the GHCNd dataset into the Hive table, ensuring data is partitioned appropriately for efficient querying.

### Task 2: Define Criteria for Extreme Weather Events

- **Identify Heatwaves:**
  - Establish criteria for what constitutes a heatwave in your dataset (e.g., temperatures exceeding a certain percentile or threshold for a consecutive number of days).
  
- **Identify Cold Spells:**
  - Define criteria for cold spells (e.g., temperatures below a certain percentile or threshold for a consecutive number of days).

### Task 3: Querying for Extreme Weather Events

- **HiveQL for Heatwaves:**
  - Craft a HiveQL query that identifies periods and locations experiencing heatwaves based on the defined criteria.
    ```sql
    SELECT location, COUNT(*) as consecutive_days, MIN(date), MAX(date)
    FROM weather_data
    WHERE temperature > threshold
    GROUP BY location, date_sub(date, row_number() over(partition by location order by date))
    HAVING consecutive_days >= min_days_for_heatwave;
    ```
  
- **HiveQL for Cold Spells:**
  - Develop a similar HiveQL query to identify periods and locations of cold spells.
    ```sql
    SELECT location, COUNT(*) as consecutive_days, MIN(date), MAX(date)
    FROM weather_data
    WHERE temperature < threshold
    GROUP BY location, date_sub(date, row_number() over(partition by location order by date))
    HAVING consecutive_days >= min_days_for_cold_spell;
    ```

### Task 4: Analyzing the Frequency and Intensity of Extreme Weather Events

- **Frequency Analysis:**
  - Use HiveQL to calculate the frequency of heatwaves and cold spells across different regions and over time.
  
- **Intensity Analysis:**
  - Analyze the intensity of identified extreme weather events by examining temperature extremes, duration, and any associated precipitation data.

### Task 5: Comparative Analysis Across Locations

- **Comparative HiveQL Queries:**
  - Compare the occurrence, frequency, and intensity of extreme weather events between different geographic regions.
  - Identify patterns or anomalies in the data that may indicate changing climate trends.



## Problem 4: Precipitation Analysis

### Overview
Conduct a comprehensive analysis of precipitation data to identify significant rainfall events, drought periods, and compare precipitation patterns across various geographic locations.

### Task 1: Data Preparation

- **Prepare GHCNd Dataset:**
 - Ensure the GHCNd dataset, specifically precipitation records, is accessible in HDFS for MapReduce tasks and in Hive tables for querying.

### Task 2: Precipitation Data Analysis

#### MapReduce Approach for Total Precipitation

- **Map Phase (Data Extraction):**
 - Extract precipitation data from the GHCNd dataset, focusing on daily precipitation amounts.
  
- **Reduce Phase (Aggregation):**
 - Aggregate total precipitation by time unit (e.g., monthly, annually) and by geographic location.

#### HiveQL Approach for Aggregation

- **Total Precipitation Calculation:**
 - Use HiveQL to calculate total precipitation by location and time unit directly from the Hive table.
  ```sql
  SELECT location, year, month, SUM(precipitation) as total_precipitation
  FROM precipitation_data
  GROUP BY location, year, month;
  ```

### Task 3: Identifying Extreme Precipitation Events

#### MapReduce Approach for Extreme Events

- **Map Phase (Event Identification):**
 - Identify days with precipitation amounts exceeding a defined threshold indicating heavy rainfall or potential drought conditions.
  
- **Reduce Phase (Event Analysis):**
 - Analyze the frequency and duration of these extreme precipitation events by location.

#### HiveQL Approach for Extreme Events

- **Extreme Event Query:**
 - Craft HiveQL queries to identify periods of heavy rainfall and drought based on precipitation thresholds.
  ```sql
  SELECT location, COUNT(*) as days_exceeding_threshold
  FROM precipitation_data
  WHERE precipitation > heavy_rainfall_threshold OR precipitation < drought_threshold
  GROUP BY location;
  ```

### Task 4: Comparative Precipitation Analysis Across Locations

- **MapReduce and HiveQL:**
 - Compare precipitation data between different geographic regions to identify areas with significant differences in rainfall patterns.
 - Utilize both MapReduce and HiveQL to perform comparative analysis, focusing on variations in total precipitation, frequency of extreme events, and seasonal differences.


## Problem 5: Climate Change Trends Analysis

### Overview
Perform a comprehensive analysis of long-term climate change trends by examining temperature and precipitation data. This analysis aims to uncover patterns that indicate climate change, such as increasing average temperatures, variations in precipitation, and changes in the occurrence of extreme weather events.

### Task 1: Long-term Temperature and Precipitation Data Aggregation

- **Prepare Data:**
 - Ensure temperature and precipitation data from the GHCNd dataset is readily available in both HDFS for MapReduce operations and Hive for direct querying.

#### MapReduce Approach for Data Aggregation

- **Map Phase (Data Extraction):**
 - Extract yearly temperature and precipitation data, focusing on long-term trends.
  
- **Reduce Phase (Aggregation):**
 - Aggregate data to calculate average annual temperature and total annual precipitation, along with other relevant statistics.

#### HiveQL Approach for Aggregation

- **Annual Aggregation Query:**
 - Use HiveQL to perform annual aggregation of temperature and precipitation data.
  ```sql
  SELECT year, AVG(temperature) as avg_temperature, SUM(precipitation) as total_precipitation
  FROM climate_data
  GROUP BY year;
  ```

### Task 2: Identifying Climate Change Indicators

- **Temperature Trends:**
 - Analyze long-term temperature data to identify warming or cooling trends across different geographic regions.

- **Precipitation Trends:**
 - Assess changes in annual precipitation levels, identifying regions with increasing drought or wetter conditions.

- **Extreme Weather Event Frequency:**
 - Evaluate data for trends in the frequency and intensity of extreme weather events, such as heat waves and heavy rainfall.

### Task 3: Comparative Analysis Across Regions

- **MapReduce and HiveQL:**
 - Compare climate trends between different geographic regions to identify disparities in climate change impacts.
 - Utilize both MapReduce and HiveQL to facilitate this comparative analysis, focusing on differences in temperature rises, precipitation changes, and extreme weather occurrences. 


