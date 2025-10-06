# DAT250 – Experiment Assignment 5: Redis/Valkey

The goal was to experiment with NoSQL setup using Redis

[Link to Redis package](https://github.com/599061/dat250springproject/tree/main/backend/src/main/java/com/example/dat250demo/pollapp/redis)

### What was done
- Installed **Redis** locally on **Linux Mint**
- Used **redis-cli** to:
  - set/get keys with TTL
  - Track logged-in users with a **SET** (`logged-in-users`)
  - Model a **Poll** with **Hashes**
  - Increment vote counts with `HINCRBY`
- Implemented Java code using **Jedis** to repeat the experiments.
- Tested Java implementation using **redis-cli**:
  - **SMEMBERS** `logged-in-users` to list users added with Java.

### Pending
- The Redis implementation is standalone for now, and not integrated with the existing **Spring application**.
- Cache not implemented.