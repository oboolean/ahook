import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.oboolean.plugin.TraceProcessor
import com.oboolean.plugin.TraceTransform
import com.oboolean.plugin.TraceExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.regex.Pattern

public class TracePlugin implements Plugin<Project> {
    public static final String EXT_NAME = 'mtrace'

    @Override
    public void apply(Project project) {
        /**
         * 注册transform接口
         */
        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            println 'project(' + project.name + ') apply mtrace plugin'
            project.extensions.create(EXT_NAME, TraceExtension)
            def android = project.extensions.getByType(AppExtension)
            def transformImpl = new TraceTransform(project)
            android.registerTransform(transformImpl)
            project.afterEvaluate {
                init(project)//此处要先于transformImpl.transform方法执行
            }
        }
    }

    void init(Project project) {
        TraceExtension extension = project.extensions.findByName(EXT_NAME) as TraceExtension
        project.logger.error EXT_NAME + ' configuration is:\n' + extension.toString()
        if (extension == null) extension = new TraceExtension(project)
        if (extension.include == null) extension.include = new ArrayList<>()
        if (extension.exclude == null) extension.exclude = new ArrayList<>()

        //添加默认的排除项
        TraceExtension.DEFAULT_EXCLUDE.each { exclude ->
            if (!extension.exclude.contains(exclude))
                extension.exclude.add(exclude)
        }
        initPattern(extension.include, extension.includePatterns)
        initPattern(extension.exclude, extension.excludePatterns)
        TraceProcessor.extension = extension
    }

    private void initPattern(ArrayList<String> list, ArrayList<Pattern> patterns) {
        list.each { s ->
            patterns.add(Pattern.compile(s))
        }
    }
}