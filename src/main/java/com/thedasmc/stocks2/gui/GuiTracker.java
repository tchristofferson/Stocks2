package com.thedasmc.stocks2.gui;

import java.util.HashMap;
import java.util.Map;

public class GuiTracker<K, V> {

    protected final Map<K, V> tracker = new HashMap<>();

    public V get(K key) {
        return tracker.get(key);
    }

    public void track(K key, V value) {
        tracker.put(key, value);
    }

    public void untrack(K key) {
        tracker.remove(key);
    }

}
