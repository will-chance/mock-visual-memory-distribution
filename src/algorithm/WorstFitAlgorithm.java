package algorithm;

import model.AvailableZone;
import model.Job;

import java.util.Collections;
import java.util.List;

/**
 * Created on 2017-12-18 10:37 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc:最坏适应算法
 */
public class WorstFitAlgorithm implements Algorithm {
    private static final int MIN_AREA = 2;

    private List<AvailableZone> availableZones;

    private List<Job> jobs;

    public WorstFitAlgorithm(List<AvailableZone> availableZones, List<Job> jobs) {
        this.availableZones = availableZones;
        this.jobs = jobs;
    }

    @Override
    public void execute() {
        int leftSize;

        for (Job job:jobs) {
            Collections.sort(availableZones,new SizeComparator());
            int idx = availableZones.size() - 1;//最大空闲分区的索引
            AvailableZone worstZone = availableZones.get(idx);
            leftSize = worstZone.getSize() - job.getResourceSize();

            if (leftSize < 0) {
                continue;
            } else if (leftSize < MIN_AREA) {
                availableZones.remove(idx);//把该分区分给当前作业
                job.setZone(worstZone);
            } else {
                int startAddr = worstZone.getStartAddr() + job.getResourceSize();
                AvailableZone newAvailableZone = new AvailableZone(leftSize,startAddr);

                AvailableZone jobZone = new AvailableZone(job.getResourceSize(),worstZone.getStartAddr());
                job.setZone(jobZone);

                availableZones.remove(idx);
                availableZones.add(idx,newAvailableZone);
            }
        }

    }
}
