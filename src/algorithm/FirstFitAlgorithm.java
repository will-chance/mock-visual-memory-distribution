package algorithm;

import model.AvailableZone;
import model.Job;

import java.util.List;

/**
 * Created on 2017-12-18 7:08 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc: 首次适应算法
 */
public class FirstFitAlgorithm implements Algorithm {
    private static final int MIN_AREA = 2;
    /**
     * 空闲分区表
     */
    private List<AvailableZone> availableZones;

    /**
     * 作业队列
     */
    private List<Job> jobs;

    public FirstFitAlgorithm(List<AvailableZone> availableZones, List<Job> jobs) {
        this.availableZones = availableZones;
        this.jobs = jobs;
    }

    @Override
    public void execute() {
        int leftSize;
        AvailableZone currentZone;
        boolean jobIsAssign;

        for (Job job:
             jobs) {
            jobIsAssign = false;

            for (int i = 0;i < availableZones.size();i++){
                currentZone = availableZones.get(i);//获取当前分区

                leftSize = currentZone.getSize() - job.getResourceSize();

                if (leftSize < 0) {
                    //该分区内存空间，不分配该作业
                    continue;
                } else if (leftSize < MIN_AREA) {
                    availableZones.remove(i);
                    job.setZone(currentZone);
                    jobIsAssign = true;
                    break;
                } else if (leftSize >= MIN_AREA) {
                    int startAddr = currentZone.getStartAddr() + job.getResourceSize();
                    AvailableZone newAvailableZone = new AvailableZone(leftSize,startAddr);

                    AvailableZone jobZone = new AvailableZone(job.getResourceSize(),currentZone.getStartAddr());
                    job.setZone(jobZone);

                    availableZones.remove(i);//将原空闲分区移除
                    availableZones.add(i,newAvailableZone);//插入新的空闲分区
                    jobIsAssign = true;
                    break;
                }

            }
            job.setAssigned(jobIsAssign);
        }
    }
}
