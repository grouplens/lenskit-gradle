package org.grouplens.lenskit.gradle;

import groovy.lang.MissingPropertyException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Plugin for LensKit evaluations.
 *
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class LenskitPlugin implements Plugin<Project> {
    private static final Logger logger = LoggerFactory.getLogger(LenskitPlugin.class);

    public void apply(Project project) {
        def lenskit = project.extensions.create("lenskit", LenskitExtension)

        for (prop in project.properties.entrySet()) {
            if (prop.key.startsWith("lenskit.")) {
                try {
                    logger.info("setting property {} to {}", prop.key, prop.value);
                    lenskit.setProperty(prop.key.substring(8), prop.value)
                } catch (MissingPropertyException e) {
                    logger.warning("unrecognized property {}", prop.getKey());
                }
            }
        }

        project.tasks.withType(LenskitEval) { LenskitEval task ->
            task.conventionMapping.threadCount = {
                lenskit.threadCount
            }
            task.conventionMapping.maxMemory = {
                lenskit.maxMemory
            }
        }
    }
}
