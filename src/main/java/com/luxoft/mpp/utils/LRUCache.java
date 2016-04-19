package com.luxoft.mpp.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by iivaniv on 19.04.2016.
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

        private int cacheSize;

        public LRUCache(int cacheSize) {
                super(1, 1, true);
                this.cacheSize = cacheSize;
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() >= cacheSize;
        }
}
