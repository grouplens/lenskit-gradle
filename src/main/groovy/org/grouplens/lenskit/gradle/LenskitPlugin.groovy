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

        for (prop in lenskit.metaClass.properties) {
            def prjProp = "lenskit.$prop.name"
            if (project.hasProperty(prjProp)) {
                def val = project.getProperty(prjProp)
                logger.info 'setting property {} to {}', prjProp, val
                prop.setProperty(lenskit, project.getProperty(prjProp))
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
