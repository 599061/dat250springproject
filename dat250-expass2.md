# Software Technology Experiment 2
The goal of this assignment was to implement a simple REST API for 
Poll app using Spring Boot based on a given domain model and simple instructions.
- [Git Repo](https://github.com/599061/dat250springproject.git)

### What was done
- Created Dat250DemoTest.http to cover the test scenarios listen in the assignment.
- Used IntelliJ HTTP Client to design and run the test scenarios.
- Also used Bruno to check HTTP requests and responses.
- Added domain objects: `User`, `Poll`, `VoteOption`, `Vote`.
- Added a service class `PollManager`.
- Created controllers for endpoints: `UserController`, `PollController`, `VoteController`.
- Created DTOs to avoid JSON cycles.

### Technical problems
- The app was largely stitched together from code found in lecture notes, 
youtube videos and earlier ventures into Spring/MVC. On account of the fast 
approaching deadline, ChatGPT was heavily used in the last phase for troubleshooting and debugging.

### Future Improvements
- Persistence: No database-implementation yet.
- Github Actions: Automated testing.
- Authorizations: No password requirement at the moment.
