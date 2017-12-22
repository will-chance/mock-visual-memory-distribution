package algorithm;

import model.AvailableZone;
import model.Job;

import java.util.Collections;
import java.util.List;

/**
 * Created on 2017-12-18 10:36 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc:最佳适应算法
 */
public class BestFitAlgorithm implements Algorithm{
    private static final int MIN_AREA = 2;

    private List<AvailableZone> availableZones;

    private List<Job> jobs;

    public BestFitAlgorithm(List<AvailableZone> availableZones, List<Job> jobs) {
        this.availableZones = availableZones;
        this.jobs = jobs;
    }

    @Override
    public void execute() {
        AvailableZone currentZone;

        for (Job job:jobs) {
            Collections.sort(availableZones,new SizeComparator());//从大到小排序

            for (int i = 0;i < availableZones.size();i++) {
                currentZone = availableZones.get(i);
                int leftSize = currentZone.getSize() - job.getResourceSize();
                if (leftSize < 0) {
                    continue;
                } else if (leftSize < MIN_AREA) {
                    availableZones.remove(i);
                    job.setZone(currentZone);//将当前空闲空间分配给该作业
                    break;
                } else {
                    int startAddr = currentZone.getStartAddr() + job.getResourceSize();
                    AvailableZone newAvailableZone = new AvailableZone(leftSize,startAddr);
                    //将当前空闲空间分配给该作业
                    AvailableZone zone = new AvailableZone(job.getResourceSize(),currentZone.getStartAddr());
                    job.setZone(zone);
                    availableZones.remove(i);
                    availableZones.add(i,newAvailableZone);
                    break;
                }
            }
        }
    }
}
