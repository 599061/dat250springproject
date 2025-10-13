# DAT250 – Experiment Assignment 6: Messaging

The goal was to experiment with message broker implementation.

[Link to application](https://github.com/599061/dat250springproject/tree/main/backend)

### What was done
- Installed RabbitMQ locally.
  - accessed via localhost:15672
- Added **RabbitConfig.java**, **PollService.java**, **VoteListener.java** and **UpdatePublisher.java**.

### What works
- Polls topic available on **RabbitMQ dashboard**.
- Managed to publish message on **RabbitMQ dashboard**.
  - Published message on a poll created in the **REST app**.

### Troubles
- Trouble running application
  - Java-files encoded with UTF-8 "with BOM" for some reason. removed BOM.
- Duplicate TopicExchange beans in RabbitConfig
  - solved with **@Qualifier** annotation

### Pending
- Creating polls does not dynamically create corresponding topics.
- Did not figure out how to capture payloads of published messages