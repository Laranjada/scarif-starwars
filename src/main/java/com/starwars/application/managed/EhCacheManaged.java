package com.starwars.application.managed;

import io.dropwizard.lifecycle.Managed;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import static com.starwars.util.SWAPIPlanetHelper.getNumeroAparicoesPlaneta;
import static com.starwars.util.SWAPIPlanetHelper.recuperaTodosOsPlanetas;

public class EhCacheManaged implements Managed {

    private CacheManager manager;

    public EhCacheManaged() {
    }

    @Override
    public void start() throws Exception {
        manager = CacheManager.create();
        Cache aparicoesPlanetasCache = new Cache("aparicoesPlanetas", 2000, false, true, 0, 0);

        manager.addCache(aparicoesPlanetasCache);
        startAparicoesPlanetasCache(aparicoesPlanetasCache);
    }

    @Override
    public void stop() throws Exception {
        manager.shutdown();
    }

    private void startAparicoesPlanetasCache(Cache memoryOnlyCache) {
        recuperaTodosOsPlanetas().stream().forEach( pl ->
                memoryOnlyCache.put(new Element(pl.getName(), getNumeroAparicoesPlaneta(pl))));

    }
}

