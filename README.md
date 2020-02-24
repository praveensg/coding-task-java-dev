# coding-task-java-dev

- A SpringbootApplication which starts the program with CommandLineRunner.
- Read the CSV file provided as input line by line using NIO reader for utilizing memory properly.
- Creates Multithreaded enviornment and calls API asynchronously and process the large input data.
- Handles Nasty Exceptions thrown by the Server and executes the program
- Handles the result and provides the output of the number of Successful and failed executions.

# How to run

- Clone the project and Build it using Maven.
- Start the application using Springboot

# More Notes on the application implementation

- I have utilised the dependencies of Springboot and created a Springboot app using CommandLineRunner to run the application.

- Bean creation, dependency injection, property and logger implementations from spring.

- Multithreaded Environment to quickly process and manage system resources properly and for Application Efficiency.

- ExecutorService is used to create Thread pool and run CompletableFuture asynchronous tasks.
For Example: if there are 1 Million records, based on the system capacity and we can divide the number of threads and chunks to process the million records.(all this values are handled in the property file)

- Handled Exceptions from Server using CompletableFuture to make sure the process completes for all the records.

- Validation checks on empty line, skipping header, generating random Id for Order and conversion of amount, currency and values where necessary as been taken care.

- Added one extra dependency Mockito for testing purpose. (Due to time constraints, Could not implement tests completely and I have not worked with TDD approach)


# Tasks that can be done to improve the application

- Refactor and Write more unit or TDD tests to completely cover the code to provide more quality and Integration tests.

- Retry logic based on the Exceptions and perform API calls ’K’ number of items(Which can again handle in property file).

- Validations can be improved based on our requirement and avoid calling API’s if the input data is wrong(again based on the input and contract between client and server).


