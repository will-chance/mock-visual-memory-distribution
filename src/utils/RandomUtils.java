package utils;

import model.AvailableZone;
import model.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 2017-12-19 7:11 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc:
 */
public class RandomUtils {
    /**
     * 生成随机作业队列
     *
     * @return
     */
    public static List<Job> randomJob() {
        Random random = new Random(System.currentTimeMillis());
        int jobSize = random.nextInt(10) % 10 + 1;
        List<Job> jobs = new ArrayList<>(jobSize);
        for (int i = 0; i < jobSize; i++) {
            jobs.add(new Job(i, random.nextInt(20) + 1));
        }
        return jobs;
    }

    /**
     * 生成随机空闲空间
     *
     * @return
     */
    public static List<AvailableZone> randomMemory() {
        Random random = new Random(System.currentTimeMillis());
        int zoneSize = random.nextInt(20) % 20 + 1;
        List<AvailableZone> availableZones = new ArrayList<>();

        int startAddr;
        int minAddr = 0, maxAddr;
        int size = 0;

        for (int i = 0; i < zoneSize; i++) {
            size = random.nextInt(20) + 1;//空闲分区大小

            maxAddr = minAddr + size * 2;//最大起始地址不要太大
            maxAddr = maxAddr < 256 ? maxAddr : 256;//最大地址不超过256
            //在 min - max 范围开辟新空闲分区
            startAddr = random.nextInt(maxAddr) % (maxAddr - minAddr + 1) + minAddr;
            if ((startAddr + size)>256){
                //空间过大,将最后的不超过内存的所有空间分配为最后一个空闲区间
                size = 256 - startAddr;
            }

            availableZones.add(new AvailableZone(i,size, startAddr));
            minAddr = startAddr + size + 1;//下一分区的最小其实地址
            if (minAddr>256) break;//起始地址超过内存范围
        }
        return availableZones;
    }
}
