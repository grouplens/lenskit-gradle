package org.grouplens.lenskit.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
                if (prop.type != String) {
                    val = prop.type.metaClass.invokeConstructor(val)
                }
                prop.setProperty(lenskit, val)
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
