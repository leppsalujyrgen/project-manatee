# Manatee API

The following API is made for educational purposes only and does not provide any meaningful functionalities.

## Getting started

This project requires Java 21 to be installed.
For developers, Amazon Coretta or Eclipse Termium are recommended JDKs.

For development purposes only, the relational H2 database is initialized in the local runtime.
On the shutdown, the database is torn down. There is no other option to set a persistent database.

### For Linux users (bash)

```bash
./gradlew build # Generates OpenAPI models, builds the application and runs tests.
./gradlew bootRun # Starts the application on a local network. 
```

### For Windows users

```bash
gradlew build # Generates OpenAPI models, builds the application and runs tests.
gradlew bootRun # Starts the application on a local network. 
```


# Summary
| Question                                 | Answer |
|------------------------------------------|--------|
| Time  spent (h)                          | 12h     |
| Hardest task, (with reasoning)           | Task 5. I could not include Interview field in Application class in reasonable time. Different solutions ran into errors thrown by either mappers or Hibernate. Had to implement a less eloquent solution. |
| Uncompleted tasks, if any                | -      |
| Additional dependencies (with reasoning) | -      | 


In summary, describe your overall experience with the topic, what you learned,
and any technical challenges you encountered. Your answer should be
between 50-100 words.

SUMMARY:
Seemingly simple aspects of the practice task: 
1) Creating endpoints and DTOs using the YAML file.
1) Seeding the database by using a configuration file.

Difficult aspects of the practice task:
1) Task 5 as described above (due to issues creating the best association between Interview and Application entities)
2) Keeping good separation of concerns between classes.
1) Achieving good test coverage in a limited time. 
