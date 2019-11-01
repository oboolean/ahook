package android.oboolean;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ATrace {

    static Stack<MethodInfo> stack = new Stack<>();
    static Thread sMainThread = Looper.getMainLooper().getThread();

    static File traceFile = new File("/sdcard/mtrace.txt");
    static FileOutputStream fileOutputStream = null;
    static String content = null;
    static boolean run = true;
    static List<String> list = new ArrayList<>();

    static{
        if (!traceFile.exists()){
            try {
                traceFile.createNewFile();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        run = false;
                    }
                },13000);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread("dump-all-methods"){
                            @Override
                            public void run() {
                                try{
                                    fileOutputStream = new FileOutputStream(traceFile);
                                    for (String string : list){
                                        fileOutputStream.write(string.getBytes());
                                    }
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                },14000);
            } catch (IOException e) {
                e.printStackTrace();
                run = false;
            }
        }else {
            run = false;
        }


    }


    public static void beginSection(String sectionName) {
        if (Thread.currentThread() == sMainThread){
            if (run){
                stack.push(new MethodInfo(sectionName,SystemClock.uptimeMillis()));
            }

        }
    }

    public static void endSection(String s) {
        if (Thread.currentThread() == sMainThread){
            if (stack.isEmpty()){
                return;
            }
            MethodInfo methodInfo = stack.pop();
            if (run){
                content = methodInfo.signature+" "+(SystemClock.uptimeMillis()-methodInfo.startTime)+"\n";
                if (stack.size() == 0){
                    //栈顶
                    content = "+" + content+"\n";
                }
                list.add(content);
            }
        }
    }

    static class MethodInfo {
        String signature;
        long startTime;
        public MethodInfo(String n, long t){
            this.signature = n;
            this.startTime = t;
        }
    }
}
