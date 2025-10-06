package com.example.dat250demo.pollapp.redis;

import redis.clients.jedis.UnifiedJedis;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try (UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379")) {

            String keyLoggedIn = "logged-in-users";
            jedis.del(keyLoggedIn);
            jedis.sadd(keyLoggedIn, "alice");
            jedis.sadd(keyLoggedIn, "bob");
            jedis.srem(keyLoggedIn, "alice");
            jedis.sadd(keyLoggedIn, "eve");
            jedis.sadd(keyLoggedIn, "Jerry");
            System.out.println("Logged in: " + jedis.smembers(keyLoggedIn));

            String pollId = "03ebcb7b-bd69-440b-924e-f5b7d664af7b";
            String metaKey = "poll:" + pollId + ":meta";
            String optionsKey = "poll:" + pollId + ":options";
            String votesKey = "poll:" + pollId + ":votes";

            jedis.hset(metaKey, Map.of("title", "Pineapple on Pizza?"));
            jedis.hset(optionsKey, Map.of(
                    "0", "Yes, yammy!",
                    "1", "Mamma mia, nooooo!",
                    "2", "I do not really care ..."
            ));
            jedis.hset(votesKey, Map.of("0", "269", "1", "268", "2", "42"));

            long newCount = jedis.hincrBy(votesKey, "1", 1);
            System.out.println("Option 1 new count = " + newCount);

            System.out.println("Votes: " + jedis.hgetAll(votesKey));
        }
    }
}
