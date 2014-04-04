package org.grouplens.lenskit.gradle;

import org.gradle.api.Project;

/**
 * Extension for configuring LensKit.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class LenskitExtension {
    def int threadCount
    def String maxMemory

    public void threadCount(int tc) {
        setThreadCount(tc);
    }

    public void maxMemory(String mm) {
        maxMemory = mm
    }
}
