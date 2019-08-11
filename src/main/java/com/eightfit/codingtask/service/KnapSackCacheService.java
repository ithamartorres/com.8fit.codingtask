package com.eightfit.codingtask.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Simple in memory cache. Real implementation would use redis. Here is implemented
 * a simple version just to display the performance improvement required in the workout generator.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class KnapSackCacheService {

    /**
     * Simple in memory cache. Not really optimal in deployments where there is multiple instance of
     * this service running. But also the overhead of having a shared cache for this particular problem
     * which is O(n*W) needs to be balanced with the space/complexity that it could add.
     */
    private HashMap<String, int[][]> memoryCache = new HashMap<>();

    /**
     * Gets the cached version of a particular key if available.
     *
     * @return the data structure if it is available null if there is nothing in cache.
     */

    public int[][] getCachedVersion(String hashKey) {
        return memoryCache.get(hashKey);
    }

    /**
     * Puts in cache for the given key the knapsack structure.
     */
    public void putInCache(String hashkey, int[][] knapsackResult) {
        memoryCache.put(hashkey, knapsackResult);
    }

}
