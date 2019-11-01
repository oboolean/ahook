package com.oboolean.ahook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.TextureView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class MainActivity extends Activity {


    public static final int PROC_SPACE_TERM = ' ';
    public static final int PROC_COMBINE = 0x100;
    public static final int PROC_OUT_LONG = 0x2000;
    public static final int PROC_PARENS = 0x200;

    static final int[] SYSTEM_CPU_FORMAT = new int[]{
            PROC_SPACE_TERM | PROC_COMBINE,
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 1: user time
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 2: nice time
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 3: sys time
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 4: idle time
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 5: iowait time
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 6: irq time
            PROC_SPACE_TERM | PROC_OUT_LONG                   // 7: softirq time
    };


    static final int[] PROCESS_STATS_FORMAT = new int[]{
            PROC_SPACE_TERM,
            PROC_SPACE_TERM | PROC_PARENS,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 10: minor faults
            PROC_SPACE_TERM,
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 12: major faults
            PROC_SPACE_TERM,
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 14: utime
            PROC_SPACE_TERM | PROC_OUT_LONG,                  // 15: stime
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM,
            PROC_SPACE_TERM | PROC_OUT_LONG,                  //20:ThreadCount
            PROC_SPACE_TERM,
            PROC_SPACE_TERM | PROC_OUT_LONG,                        //22:start_time
            PROC_SPACE_TERM                 // 23: vsize
    };

    static final int PROCESS_STAT_MINOR_FAULTS = 0;
    static final int PROCESS_STAT_MAJOR_FAULTS = 1;
    static final int PROCESS_STAT_UTIME = 2;
    static final int PROCESS_STAT_STIME = 3;
    static final int PROCESS_STAT_THREADCOUNT = 4;
    static final int PROCESS_STAT_STARTTIME = 5;

    static long mProcessBaseUserTime;
    static long mProcessBaseSystemTime;
    static long mThreadCount;
    public static long mPidStartTime = 0;

    static long mBaseUserTime;
    static long mBaseSystemTime;
    static long mBaseIoWaitTime;
    static long mBaseIrqTime;
    static long mBaseSoftIrqTime;
    static long mBaseIdleTime;

    static int mJiffyMillis;

    static String mPidStatFile;
    static Method mReadProcFile;

    MessageQueueProxy messageQueueProxy = new MessageQueueProxy();


    private void setText(String msg){
        ((TextView)findViewById(R.id.info)).setText(msg);
    }




    public class WorkHandler implements InvocationHandler {

        //代理类中的真实对象
        private Object obj;

        public WorkHandler() {
            // TODO Auto-generated constructor stub
        }
        //构造函数，给我们的真实对象赋值
        public WorkHandler(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //在真实的对象执行之前我们可以添加自己的操作
            System.out.println("before invoke。。。");
            Object invoke = method.invoke(obj, args);
            //在真实的对象执行之后我们可以添加自己的操作
            System.out.println("after invoke。。。");
            return invoke;
        }

    }




    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        InvocationHandler handler = new WorkHandler(Runtime.getRuntime());




        long t = System.currentTimeMillis();
        boolean tag = true;
        for (int i = 0; i < 30 * 10000; i++){
//            System.currentTimeMillis();
            String s = i+""+"MapHomePresenter.onActivityPause()V";
//            SystemClock.uptimeMillis();
        }
        Log.d("oboolean", "onCreate: "+(System.currentTimeMillis()-t));






        Animation animation = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        animation.setDuration(3000);
        animation.setRepeatCount(10000);
        findViewById(R.id.info).setAnimation(animation);









        new Thread(){
            @Override
            public void run() {
                while (true){
                    Message msg = messageQueueProxy.getCurrentMessage();
                    if (msg == null){
                        Log.d("oboolean", "runrunrun: null ");
                    }else {
                        Log.d("oboolean", "runrunrun: "+msg.hashCode()+", "+msg.getWhen());
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


//        for (int i = 0;i< 100; i++){
//            Log.d("oboolean", "xxxxxonCreate: "+Message.obtain().hashCode());
//        }
//
//        Message msg = new Message();
//        msg.arg1 = 1;
//        Log.d("oboolean", "onCreate: "+msg.hashCode());
//
//        msg.arg1 = 2;
//        msg.obj = null;
//        Log.d("oboolean", "onCreate: "+msg.hashCode());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
//
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() {
            @Override
            public void onNmeaReceived(long timestamp, String nmea) {
                Log.d("oboolean", "onNmeaReceived: "+timestamp+":::"+nmea);
            }
        };

        locationManager.addNmeaListener(nmeaListener);




    }


    private void set1(){

    }

    private void get2(){

    }

    private void xxxsetbb(){

    }


    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate2(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);

        String s1 = getCpuRateOfApp();
        String s2 = getCpuRate();

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("getCpuRateOfApp ").append(s1).append("\n");
        Log.d("oboolean", "onCreate:getCpuRateOfApp "+s1);
        Log.d("oboolean", "onCreate:getCpuRate "+s2);
        stringBuffer.append("getCpuRate ").append(s2).append("\n");



        try {
            int pid = android.os.Process.myPid();
            mPidStatFile = "/proc/" + pid + "/stat";
            mReadProcFile = android.os.Process.class.getMethod("readProcFile", String.class, int[].class, String[].class, long[].class, float[].class);
            mReadProcFile.setAccessible(true);
            Log.d("oboolean", "onCreate: method "+mReadProcFile);
            stringBuffer.append(" getmethod ").append(mReadProcFile).append("\n");
        } catch (Exception e) {
            // Log.e(TAG,Log.getStackTraceString(e));
            e.printStackTrace();
            stringBuffer.append(" getmethod ").append(" exception"+e).append("\n");
        }


        final long[] mSysCpu = new long[7];
        final long[] mStatsData = new long[6];
        boolean invokeRet;
        try {
            invokeRet = (boolean) mReadProcFile.invoke(null, "/proc/stat", SYSTEM_CPU_FORMAT, null, mSysCpu, null);
            Log.d("oboolean", "onCreate: reflect /proc/stat "+invokeRet);
            stringBuffer.append(" reflect /proc/stat ").append(invokeRet).append("\n");
            if (invokeRet) {
                invokeRet = (boolean) mReadProcFile.invoke(null, mPidStatFile, PROCESS_STATS_FORMAT, null, mStatsData, null);
                Log.d("oboolean", "onCreate: reflect /proc/self/stat "+invokeRet);
                stringBuffer.append(" reflect /proc/self/stat ").append(invokeRet).append("\n");
            }
            Log.d("oboolean", "onCreate: "+invokeRet);
        } catch (Exception e) {
            e.printStackTrace();
            stringBuffer.append(" invoke ").append(" exception"+e).append("\n");
        }

        setText(stringBuffer.toString());


        for (int i = 0;i < 10; i++){
            new Thread("myapp-"+i){
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        new Thread("uuuuuuuuuuu"){
            @Override
            public void run() {
                android.os.Process.sendSignal(android.os.Process.myPid(), android.os.Process.SIGNAL_QUIT);
            }
        }.start();

//        try {
//            File file = new File("/data/anr");
//            File[] files = file.listFiles();
//            for (File f : files){
//                FileInputStream fileInputStream = new FileInputStream(f);
//                byte[] bytes = new byte[fileInputStream.available()];
//                fileInputStream.read(bytes);
//                fileInputStream.close();
//                Log.d("oboolean", "onCreate: "+new String(bytes));
//            }
//        }catch (Exception ex){
//            Log.e("oboolean", "onCreate: ",ex);
//        }


        extractThumbnail("file://sdcard/p.png","file://sdcard/psmall.png",100,100);
//        extractThumbnail("/sdcard/j.jpeg","/sdcard/jsmall.jpeg",100,100);

        Toast.makeText(this, "uuu "+isConnected(this), Toast.LENGTH_SHORT).show();


        Thread t = new Thread(){
            @Override
            public void run() {
                while (true){
                    isConnected(MainActivity.this);
                }
            }
        };
        t.setName("hello-world");
        t.start();
    }


    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        for (int i = 0;i < Integer.MAX_VALUE; i++){
            int a = i + i /5;
        }
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
    }



    private static String getCpuRate() {
        BufferedReader cpuReader = null;
        try {
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1024);
            String cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                return "";
            }
            return cpuRate;
        } catch (Throwable e) {
            return "";
        } finally {
        }
    }


    private  String getCpuRateOfApp() {
        BufferedReader pidReader = null;
        try {
            int pid = android.os.Process.myPid();
            pidReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), 1024);
            String pidCpuRate = pidReader.readLine();
            if (pidCpuRate == null) {
                return "";
            }
            return pidCpuRate;
        } catch (Throwable throwable) {
            return "";
        } finally {
        }
    }



    public void extractThumbnail(final String filePath,final String thumbPath,final int w,final int h) {
        if (filePath.startsWith("file://")){
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ret = false;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = getImageThumbnail(filePath,w,h,options);
                    File file = new File(thumbPath);
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    String type = options.outMimeType;
                    // format see https://android.googlesource.com/platform/frameworks/base/+/7b2f8b8/core/jni/android/graphics/BitmapFactory.cpp#53
                    if (type != null){
                        if (type.endsWith("jpeg")){
                            ret = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        }else if (type.endsWith("png")){
                            ret = bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        }
                    }
                    os.close();
                }catch (Exception ex){
                    Log.e("oboolean", "run: ",ex);
                } finally {
                }
            }
        }).start();
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath
     *            图像的路径
     * @param width
     *            指定输出图像的宽度
     * @param height
     *            指定输出图像的高度
     * @param options
     *            options
     * @return 生成的缩略图
     */
    private Bitmap getImageThumbnail(String imagePath, int width, int height,BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比

        int w = options.outWidth;
        int h = options.outHeight;

        int beWidth = w / width;
        int beHeight = h / height;
        int be = Math.min(beWidth,beHeight);
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

}
