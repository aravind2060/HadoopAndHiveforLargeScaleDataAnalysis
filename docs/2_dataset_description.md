## GHCN-Daily Dataset Format

The Global Historical Climatology Network-Daily (GHCN-Daily) dataset comprises daily climate observations from land-based stations worldwide. Each entry in the dataset represents a daily observation for a specific meteorological element from a station. The data are organized in a comma-separated values (CSV) format, where each row corresponds to a single observation and is structured as follows:

```
ID,DATE,ELEMENT,DATA VALUE,M-FLAG,Q-FLAG,S-FLAG,OBS-TIME
```

### Fields Description

- **ID**: An 11-character alphanumeric station identification code. The first two characters represent the country code according to the FIPS standard, and the remaining characters uniquely identify the station within that country.
- **DATE**: The observation date in `YYYYMMDD` format.
- **ELEMENT**: A four-character code indicating the type of observation (e.g., `PRCP` for precipitation, `SNOW` for snowfall, `TMAX` for maximum temperature).
- **DATA VALUE**: The recorded value for the observation. The unit of measurement varies by element type (e.g., tenths of millimeters for precipitation, tenths of degrees Celsius for temperature).
- **M-FLAG**: A single-character measurement flag that provides additional information about the observation (e.g., measurement techniques or conditions).
- **Q-FLAG**: A single-character quality flag indicating the quality assurance status of the observation (`blank` means the observation passed all quality checks).
- **S-FLAG**: A single-character source flag identifying the data source.
- **OBS-TIME**: The time of observation in `HHMM` format. This field is primarily available for U.S. Cooperative Observers and is based on the station's history.

### Element Codes

Key element codes include:
- `PRCP`: Precipitation
- `SNOW`: Snowfall
- `SNWD`: Snow depth
- `TMAX`: Maximum temperature
- `TMIN`: Minimum temperature

Other elements are available and documented in the dataset's full metadata.

### Flags

- **M-FLAG** provides details like measurement methods or specific conditions affecting the observation.
- **Q-FLAG** denotes the quality of the data, with codes indicating various checks the data has undergone.
- **S-FLAG** indicates the source of the data, with numerous potential sources including national meteorological services, international datasets, and automated instruments.

---

## Example of GHCN-Daily Data Entry

Below is an example of a single data entry from the GHCN-Daily dataset:

```
US1MIKZ0006,19990101,SNWD,76,,,N,2359
```

This entry can be broken down as follows:

- **ID**: `US1MIKZ0006` - This is the station ID, where `US` indicates the country code for the United States, and `1MIKZ0006` uniquely identifies the station within the US.
- **DATE**: `19990101` - The observation was made on January 1, 1999.
- **ELEMENT**: `SNWD` - The observed meteorological element is snow depth.
- **DATA VALUE**: `76` - The recorded snow depth is 7.6 mm (since the unit for snow depth is tenths of millimeters).
- **M-FLAG**: (Blank) - No additional measurement information is applicable.
- **Q-FLAG**: (Blank) - The observation did not fail any quality assurance check.
- **S-FLAG**: `N` - Indicates that there is no specific source information for this data point.
- **OBS-TIME**: `2359` - The observation was recorded at 11:59 PM.

### Interpretation

This observation tells us that at station `US1MIKZ0006` in the United States, on January 1, 1999, a snow depth of 7.6 mm was recorded at the very end of the day. No specific measurement flags were noted, suggesting standard measurement conditions, and the observation passed quality checks without any issues noted.

