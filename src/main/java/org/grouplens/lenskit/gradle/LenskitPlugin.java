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
        project.getExtensions().create("lenskit", LenskitExtension.class);
        for (Map.Entry<String,?> prop: project.getProperties().entrySet()) {
            if (prop.getKey().startsWith("lenskit.")) {
                try {
                    InvokerHelper.setProperty(project.getExtensions().getByName("lenskit"),
                                              prop.getKey().substring(8),
                                              prop.getValue());
                    logger.info("set property {}", prop.getKey());
                } catch (MissingPropertyException e) {
                    logger.debug("no property {}", prop.getKey());
                }
            }
        }
    }
}
