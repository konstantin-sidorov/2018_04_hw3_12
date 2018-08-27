package ru.otus.cache;


import org.springframework.stereotype.Component;
import ru.otus.dataSets.DataSet;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

@Component
public class CacheEngineImpl<T extends DataSet> implements CacheEngine<T> {
    private final Map<Long, SoftReference<CacheElement<T>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private static final int TIME_THRESHOLD_MS = 5;
    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;
    private int hit = 0;
    private int miss = 0;

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    private TimerTask getTimerTask(final Long key, Function<CacheElement<T>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                SoftReference<CacheElement<T>> reference = elements.get(key);
                if (reference != null) {
                    CacheElement<T> element = reference.get();
                    if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                        elements.remove(key);
                        this.cancel();
                    }
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }

    @Override
    public void put(CacheElement<T> element) {
        if (elements.size() == maxElements) {
            Long firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }
        Long key = element.getKey();
        if (elements.get(key) == null) {
            elements.put(key, new SoftReference<>(element));
            if (!isEternal) {
                if (lifeTimeMs != 0) {
                    TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                    timer.schedule(lifeTimerTask, lifeTimeMs);
                }
                if (idleTimeMs != 0) {
                    TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                    timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
                }
            }
        }

    }

    @Override
    public CacheElement<T> get(long key) {
        CacheElement<T> element = null;
        SoftReference<CacheElement<T>> reference = elements.get(key);
        if (reference != null) {
            hit++;
            element = reference.get();
            element.setAccessed();
        } else {
            miss++;
        }
        return element;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public int getMaxElements() {
        return maxElements;
    }

    @Override
    public int getСurrElements() {
        return elements.size();
    }


}
