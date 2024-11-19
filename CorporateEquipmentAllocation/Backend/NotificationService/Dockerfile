# Use an official sbt image as a parent image
FROM hseeberger/scala-sbt:11.0.12_1.5.5_2.13.6

# Set the working directory in the container
WORKDIR /app

# Copy the project files
COPY . .

# Pre-fetch dependencies to speed up builds
RUN sbt clean compile

# Expose the application port (if necessary, e.g., for HTTP servers)
EXPOSE 8083
# Run the application
CMD ["sbt", "run"]
