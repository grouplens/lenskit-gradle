package org.grouplens.lenskit.gradle

import com.google.common.collect.FluentIterable
import org.gradle.api.Nullable
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaExecSpec
import org.gradle.process.internal.DefaultJavaExecAction
import org.gradle.util.ConfigureUtil

/**
 * Task to run LensKit evaluations.
 *
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class LenskitEval extends ConventionTask {
    private Object script = "eval.groovy"
    private List<String> targets = new ArrayList<String>()
    private Map<String,Object> lkProps = new HashMap<String, Object>()
    def int threadCount
    def String maxMemory
    final def JavaExecSpec invoker

    public LenskitEval() {
        invoker = new DefaultJavaExecAction(services.get(FileResolver))
    }

    /**
     * Set the evaluation script to run.
     * @param obj The script to run (interpreted by {@link Project#file(Object)}).
     */
    public void script(Object obj) {
        script = obj;
    }

    /**
     * Set the script to be run.
     * @param obj The script to run (interpreted by {@link Project#file(Object)}).
     */
    public void setScript(Object obj) {
        script = obj;
    }

    /**
     * Get the script to be run.
     * @return The script to be run.
     */
    public Object getScript() {
        return script;
    }

    /**
     * Specify targets to run.
     * @param tgts Some targets to run.
     */
    public void targets(String... tgts) {
        for (String t: tgts) {
            targets.add(t);
        }
    }

    /**
     * Set the list of targets to run.
     * @param tgts The list of targets to run.
     */
    public void setTargets(Iterable<String> tgts) {
        targets = FluentIterable.from(tgts).toList();
    }

    /**
     * Get the targets to be run.
     * @return The list of targets to be run.
     */
    public List<String> getTargets() {
        return targets;
    }

    /**
     * Get the map of properties to be passed to the script.
     * @return The map of the properties to be passed to the script.
     */
    public Map<String, Object> getLenskitProperties() {
        return lkProps;
    }

    /**
     * Set the map of properties to be passed to the script.
     * @param props The map of properties to be passed.
     */
    public void setLenskitProperties(Map<String, Object> props) {
        lkProps = new HashMap<String, Object>(props);
    }

    /**
     * Set some properties to be passed to the script.
     * @param props A map of properties to set.  These properties are set in addition to any
     *              already-set properties, overriding previously-set properties with the same value.
     */
    public void lenskitProperties(Map<String, Object> props) {
        lkProps.putAll(props);
    }

    @Nullable
    private LenskitExtension getLenskitConfig() {
        return (LenskitExtension) getProject().getExtensions().findByName("lenskit");
    }

    public void threadCount(int tc) {
        setThreadCount(tc);
    }

    public FileCollection getClasspath() {
        return invoker.classpath
    }

    public void setClasspath(FileCollection cp) {
        invoker.classpath = cp
    }

    public void classpath(FileCollection cp) {
        invoker.classpath = cp
    }

    public void maxMemory(String mm) {
        maxMemory = mm
    }

    public void invoker(Closure block) {
        ConfigureUtil.configure(block, invoker)
    }

    /**
     * Get the script file to run.
     *
     * @return The name of the script file to run.
     */
    @InputFile
    public File getScriptFile() {
        return getProject().file(script);
    }

    @TaskAction
    public void exec() {
        invoker {
            main = 'org.grouplens.lenskit.cli.Main'
            args 'eval'
            args "-j$threadCount"
            for (prop in properties) {
                args "-D$prop.key=$prop.value"
            }
            if (scriptFile != null) {
                args '-f', scriptFile
            }
            args targets
            if (maxMemory != null) {
                maxHeapSize = maxMemory
            }
        }
        invoker.execute()
    }
}
