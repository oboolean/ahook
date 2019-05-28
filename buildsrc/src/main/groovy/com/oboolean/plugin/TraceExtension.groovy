package com.oboolean.plugin

import org.gradle.api.Project

import java.util.regex.Pattern
/**
 */
class TraceExtension {
    static final DEFAULT_EXCLUDE = [
            '.*/R(\\\$)?.*'
            , '.*/BuildConfig$'
    ]
    ArrayList<String> include = []
    ArrayList<String> exclude = []
    ArrayList<Pattern> includePatterns = []
    ArrayList<Pattern> excludePatterns = []

    public TraceExtension(){}
    public TraceExtension(Project project) {
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder('mtrace {')
        sb.append('\n\t').append('include').append(' = ').append('[')
        include.each { i ->
            sb.append('\n\t\t\'').append(i).append('\'')
        }
        sb.append('\n\t]')
        sb.append('\n\t').append('exclude').append(' = ').append('[')
        exclude.each { i ->
            sb.append('\n\t\t\'').append(i).append('\'')
        }
        sb.append('\n\t]\n}')
        return sb.toString()
    }
}