package org.grouplens.lenskit.gradle;

import com.google.common.collect.FluentIterable;
import org.gradle.api.DefaultTask;
import org.gradle.api.Nullable;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Task to run LensKit evaluations.
 *
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class LenskitEval extends DefaultTask {
    private JavaExec exec;
    private Object script = "eval.groovy";
    private List<String> targets = new ArrayList<String>();
    private Map<String,Object> properties = new HashMap<String, Object>();
    private Integer threadCount;
    private Object classpath;

    public LenskitEval() {
        exec = new JavaExec();
    }

    @Override
    public void setProject(Project project) {
        exec.setProject(project);
        super.setProject(project);
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
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Set the map of properties to be passed to the script.
     * @param props The map of properties to be passed.
     */
    public void setProperties(Map<String, Object> props) {
        properties = new HashMap<String, Object>(props);
    }

    /**
     * Set some properties to be passed to the script.
     * @param props A map of properties to set.  These properties are set in addition to any
     *              already-set properties, overriding previously-set properties with the same value.
     */
    public void properties(Map<String, Object> props) {
        properties.putAll(props);
    }

    @Nullable
    private LenskitExtension getLenskitConfig() {
        return (LenskitExtension) getProject().getExtensions().findByName("lenskit");
    }

    public int getThreadCount() {
        if (threadCount != null) {
            return threadCount;
        } else {
            LenskitExtension ext = getLenskitConfig();
            if (ext != null) {
                return ext.getThreadCount();
            } else {
                return 0;
            }
        }
    }

    public void setThreadCount(int tc) {
        threadCount = tc;
    }

    public void threadCount(int tc) {
        setThreadCount(tc);
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
        exec.setMain("org.grouplens.lenskit.cli.Main");
        exec.args("eval");
        exec.args("-j" + getThreadCount());
        for (Map.Entry<String,?> props: properties.entrySet()) {
            exec.args("-D" + props.getKey() + "=" + props.getValue());
        }
        File f = getScriptFile();
        if (f != null) {
            exec.args("-f", f);
        }
        exec.args(getTargets());
        exec.exec();
    }

    //region Delegated Java Config Methods
    public List<String> getAllJvmArgs() {
        return exec.getAllJvmArgs();
    }

    public void setAllJvmArgs(Iterable<?> arguments) {
        exec.setAllJvmArgs(arguments);
    }

    public List<String> getJvmArgs() {
        return exec.getJvmArgs();
    }

    public void setJvmArgs(Iterable<?> arguments) {
        exec.setJvmArgs(arguments);
    }

    public JavaExec jvmArgs(Iterable<?> arguments) {
        return exec.jvmArgs(arguments);
    }

    public JavaExec jvmArgs(Object... arguments) {
        return exec.jvmArgs(arguments);
    }

    public Map<String, Object> getSystemProperties() {
        return exec.getSystemProperties();
    }

    public void setSystemProperties(Map<String, ?> properties) {
        exec.setSystemProperties(properties);
    }

    public JavaExec systemProperty(String name, Object value) {
        return exec.systemProperty(name, value);
    }

    public JavaExec systemProperties(Map<String, ?> properties) {
        return exec.systemProperties(properties);
    }

    @InputFiles
    public FileCollection getBootstrapClasspath() {
        return exec.getBootstrapClasspath();
    }

    public JavaExec bootstrapClasspath(Object... classpath) {
        return exec.bootstrapClasspath(classpath);
    }

    public void setBootstrapClasspath(FileCollection classpath) {
        exec.setBootstrapClasspath(classpath);
    }

    public String getMinHeapSize() {
        return exec.getMinHeapSize();
    }

    public void setMinHeapSize(String heapSize) {
        exec.setMinHeapSize(heapSize);
    }

    public String getMaxHeapSize() {
        return exec.getMaxHeapSize();
    }

    public void setMaxHeapSize(String heapSize) {
        exec.setMaxHeapSize(heapSize);
    }

    public String getDefaultCharacterEncoding() {
        return exec.getDefaultCharacterEncoding();
    }

    public void setDefaultCharacterEncoding(String defaultCharacterEncoding) {
        exec.setDefaultCharacterEncoding(defaultCharacterEncoding);
    }

    public boolean getEnableAssertions() {
        return exec.getEnableAssertions();
    }

    public void setEnableAssertions(boolean enabled) {
        exec.setEnableAssertions(enabled);
    }

    public JavaExec classpath(Object... paths) {
        return exec.classpath(paths);
    }

    public JavaExec setClasspath(FileCollection classpath) {
        return exec.setClasspath(classpath);
    }

    @InputFiles
    public FileCollection getClasspath() {
        return exec.getClasspath();
    }

    public File getWorkingDir() {
        return exec.getWorkingDir();
    }

    public void setWorkingDir(Object dir) {
        exec.setWorkingDir(dir);
    }

    public JavaExec workingDir(Object dir) {
        return exec.workingDir(dir);
    }

    public Map<String, Object> getEnvironment() {
        return exec.getEnvironment();
    }

    public void setEnvironment(Map<String, ?> environmentVariables) {
        exec.setEnvironment(environmentVariables);
    }

    public JavaExec environment(String name, Object value) {
        return exec.environment(name, value);
    }

    public JavaExec environment(Map<String, ?> environmentVariables) {
        return exec.environment(environmentVariables);
    }
    //endregion
}
