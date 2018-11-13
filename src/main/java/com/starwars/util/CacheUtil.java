package com.starwars.util;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtil {

    private static final CacheManager manager = CacheManager.create();

    public static int getNumAparicoesPlaneta (String nomePlaneta) {
        Element cache = manager.getCache("aparicoesPlanetas").get(nomePlaneta);
        return cache == null? 0: (Integer)cache.getObjectValue();
    }

}
