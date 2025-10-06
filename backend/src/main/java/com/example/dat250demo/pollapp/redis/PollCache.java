//package com.example.dat250demo.pollapp.redis;
//
//import redis.clients.jedis.UnifiedJedis;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class PollCache {
//    private final UnifiedJedis jedis;
//    private final int ttlSeconds;
//
//    public PollCache(UnifiedJedis jedis, int ttlSeconds) {
//        this.jedis = jedis;
//        this.ttlSeconds = ttlSeconds;
//    }
//
//    private String cacheKey(String pollId) {
//        return "cache:poll:" + pollId + ":votes";
//    }
//
//    /** Try cache → else DB aggregate → populate cache. */
//    public Map<Integer, Long> getAggregatedVotes(String pollId, DBGateway db) {
//        String key = cacheKey(pollId);
//        Map<String, String> cached = jedis.hgetAll(key);
//        if (cached != null && !cached.isEmpty()) {
//            return cached.entrySet().stream()
//                    .collect(Collectors.toMap(
//                            e -> Integer.parseInt(e.getKey()),
//                            e -> Long.parseLong(e.getValue())
//                    ));
//        }
//        // Miss: query DB
//        Map<Integer, Long> aggregated = db.aggregateVotesByPresentationOrder(pollId);
//        if (!aggregated.isEmpty()) {
//            // HSET expects Map<String,String>
//            Map<String, String> asStrings = new HashMap<>();
//            for (var e : aggregated.entrySet()) {
//                asStrings.put(Integer.toString(e.getKey()), Long.toString(e.getValue()));
//            }
//            jedis.hset(key, asStrings);
//            jedis.expire(key, ttlSeconds);
//        }
//        return aggregated;
//    }
//
//    /** Call this after successfully writing a vote to the DB. */
//    public void onVoteRecorded(String pollId, int optionOrder) {
//        String key = cacheKey(pollId);
//        if (Boolean.TRUE.equals(jedis.exists(key))) {
//            jedis.hincrBy(key, Integer.toString(optionOrder), 1);
//            // Optionally refresh TTL
//            jedis.expire(key, ttlSeconds);
//        }
//        // Alternative: uncomment to hard-invalidate instead of incrementing:
//        // jedis.del(key);
//    }
//
//    /** Optional: prewarm cache for a poll. */
//    public void prewarm(String pollId, DBGateway db) {
//        getAggregatedVotes(pollId, db);
//    }
//
//    // Mockable DB gateway
//    public interface DBGateway {
//        Map<Integer, Long> aggregateVotesByPresentationOrder(String pollId);
//    }
//}
