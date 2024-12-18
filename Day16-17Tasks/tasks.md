## Day 16 & 17 Tasks
Case Study 1: Genre-Specific Data Aggregation Pipeline
Objective: Aggregate movie ratings by genre and store the results in a Parquet format for analytics.

Scenario: The Movielens dataset is stored in GCP Cloud Storage as CSV files. You need to calculate the average ratings per genre for analytics. Some genre information requires custom transformations due to inconsistent formats.
Steps:

Ingestion: Load the movies.csv and ratings.csv files as DataFrames from GCP Cloud Storage.

movies.csv contains columns: movieId, title, genres.
ratings.csv contains columns: userId, movieId, rating, timestamp.
Transformation:

Use DataFrames to parse and explode the genres column into individual genre rows (e.g., split Action|Comedy into two rows: Action and Comedy).
Convert to an RDD for custom transformations to handle inconsistent genre names (e.g., mapping Sci-Fi to Science Fiction).
Aggregation:

Perform the join between movies and ratings on movieId using a DataFrame.
Use RDD transformations to calculate the average rating for each genre using a combination of reduceByKey and custom key-value mapping.
Storage:

Convert the RDD back to a DataFrame and save the aggregated results in Parquet format in HDFS.
Case Study 2: User Rating History Partitioning
Objective: Partition the Movielens dataset by user for faster query processing.

Scenario: Movielens user ratings data (CSV format) needs to be partitioned into separate folders for each user in HDFS.
Steps:

Ingestion: Load the ratings.csv file as a DataFrame from GCP Cloud Storage.


Transformation:

Use a DataFrame to filter out invalid or incomplete records.
Convert the DataFrame into an RDD to dynamically create key-value pairs of userId and their corresponding ratings.
Partitioning:

Use RDD transformations like groupByKey to partition ratings data by userId.
Write each user's data to a separate folder in HDFS using the saveAsTextFile method.
Verification:

Validate that the HDFS structure follows the format /user-data/{userId}/ratings.csv.

Observation:
-------------
Try to solve the problem only by using DataFrame and partitionBy Function
just perform write operation check how the folder structure is created

Case Study 3: Handling Incomplete Metadata
Objective: Enrich incomplete movie metadata using additional JSON files.

Scenario: Movielens metadata (e.g., movies.csv) is missing releaseYear for some movies. Supplementary metadata in JSON format is available for enrichment.
Steps:

Ingestion:

Load movies.csv from GCP Cloud Storage as a DataFrame.
Load metadata.json from GCP Cloud Storage into an RDD for custom parsing.
Transformation:

Use RDD operations to parse the JSON file and extract movieId and releaseYear.
Perform an RDD join with the movies DataFrame to fill in missing releaseYear.
Validation:

Convert the enriched RDD back into a DataFrame.
Validate that all movies have a releaseYear field.
Storage:

Save the enriched DataFrame in Parquet format in HDFS.
Case Study 4: Duplicate Record Removal Pipeline
Objective: Identify and remove duplicate movie records based on movieId and title, saving clean data in Avro format.

Scenario: The movies.csv file in HDFS contains duplicate records that need to be cleaned.
Steps:

Ingestion: Load movies.csv into a Spark DataFrame from HDFS.

Transformation:

Use DataFrames to identify duplicates based on movieId and title.
Convert the DataFrame to an RDD to perform custom filtering operations using distinct() on a composite key (movieId, title).
Validation:

Count the number of duplicates removed by comparing the record counts before and after transformation.
Storage:

Save the cleaned data as Avro files in GCP Cloud Storage.
Case Study 5: Time-Based Data Partitioning for Ratings
Objective: Partition user ratings data by year and save in Parquet format.

Scenario: The ratings.csv file includes a timestamp field that needs to be converted into human-readable years, and the data needs to be stored year-wise.
Steps:

Ingestion: Load ratings.csv as a DataFrame from GCP Cloud Storage.

Transformation:

Use DataFrames to convert the timestamp field into a year column.
Convert the DataFrame to an RDD to partition records by year using a key-value pair transformation.
Partitioning:

Save RDD partitions as separate Parquet files in HDFS, with the structure /ratings/{year}/ratings.parquet.
Verification:

Ensure that each year folder in HDFS contains only the records for that year.