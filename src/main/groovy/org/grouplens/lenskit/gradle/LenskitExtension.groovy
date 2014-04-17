package org.grouplens.lenskit.gradle
/**
 * Extension for configuring LensKit.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class LenskitExtension {
    def Integer threadCount = 0
    def String maxMemory

    public void threadCount(int tc) {
        setThreadCount(tc);
    }

    public void maxMemory(String mm) {
        maxMemory = mm
    }
}
