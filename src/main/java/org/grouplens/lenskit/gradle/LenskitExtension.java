package org.grouplens.lenskit.gradle;

/**
 * Extension for configuring LensKit.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class LenskitExtension {
    private int threadCount = 0;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int tc) {
        threadCount = tc;
    }

    public void threadCount(int tc) {
        setThreadCount(tc);
    }
}
