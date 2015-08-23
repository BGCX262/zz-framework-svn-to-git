package com.youthor.zz.common;

import java.util.HashMap;
import java.util.Map;

import com.youthor.zz.common.util.StringUtil;

public class ZzBenchmark {
    private static final String bmKey = "Zz_Benchmark";
    private static final Runtime runtime = Runtime.getRuntime();

    @SuppressWarnings("unchecked")
    private static Map<String, ZzObject> init() {
        ZzContext context = ZzContext.getContext();
        Map<String, ZzObject> benchmarkMap = (Map<String, ZzObject>)context.getObject(ZzBenchmark.bmKey);
        if (benchmarkMap == null) {
            benchmarkMap = new HashMap<String, ZzObject>();
            context.addObject(ZzBenchmark.bmKey, benchmarkMap);
        }
        return benchmarkMap;
    }

    public static void start(String name) {
        if (StringUtil.isBlank(name)) return ;
        Map<String, ZzObject> benchmarkMap = ZzBenchmark.init();
        if (!benchmarkMap.containsKey(name)) {
            long startTime = System.currentTimeMillis();
            ZzObject zzObject = new ZzObject();
            zzObject.addData("start_time", startTime);
            zzObject.addData("start_free_memory", ZzBenchmark.runtime.freeMemory());
            zzObject.addData("start_total_memory", ZzBenchmark.runtime.totalMemory());
            benchmarkMap.put(name, zzObject);
        }
    }

    public static void stop(String name) {
        if (StringUtil.isBlank(name)) return ;
        Map<String, ZzObject> benchmarkMap = ZzBenchmark.init();
        if (benchmarkMap != null && benchmarkMap.containsKey(name)) {
            ZzObject zzObject = benchmarkMap.get(name);
            if (zzObject != null) {

                long endTime = System.currentTimeMillis();
                long startTime = (Long)zzObject.getData("start_time");
                zzObject.addData("resume_time", endTime - startTime);
                zzObject.removeData("start_time");

                long startTotalMemory = (Long)zzObject.getData("start_total_memory");
                long startFreeMemory = (Long)zzObject.getData("start_free_memory");
                long totalMemory = ZzBenchmark.runtime.totalMemory();
                long resumeMemory = 0;
                if (startTotalMemory == totalMemory) {
                    long endFreeMemory = ZzBenchmark.runtime.freeMemory();
                    resumeMemory = (startFreeMemory - endFreeMemory)/1024;
                }
                else {
                    long freeMemory = ZzBenchmark.runtime.freeMemory();
                    resumeMemory = totalMemory - startTotalMemory + startFreeMemory - freeMemory;
                }
                zzObject.addData("resume_memory", resumeMemory);
                zzObject.removeData("start_free_memory");
                zzObject.removeData("start_total_memory");
            }
        }
    }

    public static long fetchTime(String name) {
        if (StringUtil.isBlank(name)) return 0;
        Map<String, ZzObject> benchmarkMap = ZzBenchmark.init();
        if (benchmarkMap != null && benchmarkMap.containsKey(name)) {
            ZzObject zzObject = benchmarkMap.get(name);
            Long resumeTime = (Long)zzObject.getData("resume_time");
            return resumeTime;
        }
        return 0;
    }

    public static long fetchMemory(String name) {
        if (StringUtil.isBlank(name)) return 0;
        Map<String, ZzObject> benchmarkMap = ZzBenchmark.init();
        if (benchmarkMap != null && benchmarkMap.containsKey(name)) {
            ZzObject zzObject = benchmarkMap.get(name);
            Long resumeMemory = (Long)zzObject.getData("resume_memory");
            return resumeMemory;
        }
        return 0;
    }

    public static long getMaxMemory() {
        return ZzBenchmark.runtime.maxMemory();
    }

    public static long getFreeMemory() {
        return ZzBenchmark.runtime.freeMemory();
    }

    public static long getTotalMemory() {
        return ZzBenchmark.runtime.totalMemory();
    }
}
