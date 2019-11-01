package com.oboolean.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class TraceRecorder {

    static HashMap<String,Integer> methodMap = new HashMap();
    static int methodId = 1;
    private static File buildDir;

    private static File recorder;

    private static FileOutputStream fileOutputStream;


    public static void initDir(File dir){
        buildDir = dir;
        recorder = new File(buildDir,"trace_recorder.txt")
        if (!recorder.exists()){
            recorder.createNewFile()
        }
        fileOutputStream = new FileOutputStream(recorder,false)
    }


    static void writeConfiguration(String configuration){
        fileOutputStream.write("mtrace configuration:\n".getBytes())
        fileOutputStream.write(configuration.getBytes())
        fileOutputStream.write("\n".getBytes())
    }

    public static void write(String className,String methodName,String desc,String sectionName){
        fileOutputStream.write(className.getBytes())
        fileOutputStream.write(".".getBytes())
        fileOutputStream.write(methodName.getBytes())
        fileOutputStream.write(desc.getBytes())
        fileOutputStream.write(" -> ".getBytes())
        fileOutputStream.write(sectionName.getBytes())
        fileOutputStream.write("\n".getBytes())
    }

    static void writeTimeCost(long cost){
        fileOutputStream.write(("mtrace cost: "+cost+"s").getBytes())
    }

    static void close(){
        if (fileOutputStream != null){
            fileOutputStream.close()

        }
    }
}
