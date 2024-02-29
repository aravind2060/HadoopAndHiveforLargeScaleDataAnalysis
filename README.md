# Project: Analyzing Climate Trends in North America with Cloud Computing

## Learning Outcomes

- **Master Hadoop and Hive:** Hands-on experience with big data tools for large-scale data analysis.
- **Data Wrangling Fundamentals:** Acquire crucial skills in data cleaning and preparation.
- **Advanced Data Analysis:** Conduct temperature and precipitation analysis, identify extreme weather events, and evaluate long-term climate trends.
- **Critical Thinking and Problem-Solving:** Enhance abilities to make data-driven decisions and address challenges.
- **Collaboration and Communication:** Improve teamwork skills and effectively communicate complex findings.

## Overview of Tasks

1. **Data Quality (MapReduce):** Preparation and cleaning of data for analysis. This includes filtering, handling missing values, and addressing outliers.
2. **Temperature Analysis (MapReduce):** Analyze temperature data to calculate average minimum, maximum, and average temperatures for specified locations and periods.
3. **Extreme Weather Events (Hive):** Utilize HiveQL for identifying and analyzing extreme weather events based on predefined criteria.
4. **Precipitation Analysis (MapReduce):** Analyze precipitation data, calculate totals, and identify droughts or heavy rainfall periods.
5. **Climate Change Trends (MapReduce or Hive):** Examine long-term trends in temperature and precipitation, identifying patterns and variations over time.

## Problem 1: Data Preparation and Quality Assurance

### Objective
Prepare the GHCNd dataset for analysis, focusing on accuracy and reliability.

### Tasks and Steps

#### Task 1: Environment Setup and Data Acquisition

- **Step 1.1:** Install Hadoop and configure it for local or cloud-based data processing.
- **Step 1.2:** Install Hive and set it up on top of the Hadoop ecosystem.
- **Step 1.3:** Prepare your development environment with necessary tools (e.g., IDE) and libraries (e.g., Python with pandas for data manipulation).
- **Step 1.4:** Download the GHCNd dataset from the NOAA website.
- **Step 1.5:** Upload the dataset to HDFS, ensuring it's properly distributed across the cluster for efficient processing.
- **Step 1.6:** Create a Hive table to map to the GHCNd dataset structure, facilitating easy data querying and manipulation.

#### Task 2: Data Filtering and Augmentation

- **Option A (MapReduce):**
    - **Step 2.1.a:** Write a MapReduce job to filter records by the desired date range and geographic locations.
    - **Step 2.1.b:** Augment filtered data with station metadata using a MapReduce join operation.
- **Option B (HiveQL):**
    - **Step 2.2.a:** Use HiveQL to perform filtering based on date and location directly on the dataset stored in Hive.
    - **Step 2.2.b:** Augment weather data with station metadata using HiveQL join clauses.

#### Task 3: Handling Missing or Incomplete Data

- **Option A (MapReduce):**
    - **Step 3.1.a:** Identify records with missing temperature or precipitation data using a MapReduce job.
    - **Step 3.1.b:** Exclude stations with a significant amount of missing data to ensure data quality.
- **Option B (HiveQL):**
    - **Step 3.2.a:** Use HiveQL functions to count missing data points and identify stations with excessive missing data.
    - **Step 3.2.b:** Exclude these stations from the dataset using HiveQL `WHERE` clause conditions.

#### Task 4: Outlier Detection and Correction

- **Option A (MapReduce):**
    - **Step 4.1.a:** Calculate statistical measures (mean, median, standard deviation) for temperature and precipitation using MapReduce.
    - **Step 4.1.b:** Identify outliers based on these statistical measures and replace them with median values.
- **Option B (HiveQL):**
    - **Step 4.2.a:** Use HiveQL to compute statistical measures and identify outliers in the dataset.
    - **Step 4.2.b:** Replace outlier values with median values using HiveQL update statements.

#### Task 5: Final Data Quality Assurance and Compilation

- **Step 5.1:** Perform a comprehensive data quality review using HiveQL queries to ensure all data transformations have been applied accurately.
- **Step 5.2:** Compile the final cleaned and processed dataset into a new Hive table, ready for further analysis.

## Problem 2: Temperature Analysis

### Task 1: Temperature Data Extraction and Preprocessing

#### Using MapReduce
- **Step 1.1:** Write MapReduce jobs to extract and preprocess temperature data based on specified criteria (location and time period).
- **Step 1.2:** Aggregate the data to the required temporal granularity (year, month, day) for analysis.

#### Using HiveQL
- **Step 1.3:** Use HiveQL to efficiently select and aggregate temperature data for analysis, streamlining the preprocessing step.

### Task 2: Average Temperature Calculation

#### Using MapReduce
- **Step 2.1:** Calculate average minimum, maximum, and mean temperatures for each time unit using MapReduce.

#### Using HiveQL
- **Step 2.2:** Simplify the calculation of average temperatures by utilizing HiveQL's aggregate functions.

### Task 3: Temperature Trend Analysis

#### Using MapReduce
- **Step 3.1:** Analyze temperature trends over the selected time period with MapReduce, identifying significant changes.

#### Using HiveQL
- **Step 3.2:** Conduct temperature trend analysis using HiveQL for a more straightforward approach to identifying year-over-year changes.

### Task 4: Comparative Temperature Analysis

#### Using MapReduce
- **Step 4.1:** Compare temperature data between two regions using MapReduce to highlight differences.

#### Using HiveQL
- **Step 4.2:** Perform comparative temperature analysis across locations using HiveQL, enabling efficient cross-region comparisons.
