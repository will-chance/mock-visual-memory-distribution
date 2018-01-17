package algorithm;

import model.AvailableZone;
import model.Job;

import java.util.List;

/**
 * Created on 2017-12-18 7:12 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc: 循环首次适应算法
 */
public class NextFitAlgorithm implements Algorithm {
    private static final int MIN_ZONE = 2;

    /**
     * 空闲分区表
     */
    private List<AvailableZone> availableZones;

    /**
     * 作业队列
     */
    private List<Job> jobs;

    public NextFitAlgorithm(List<AvailableZone> availableZones, List<Job> jobs) {
        this.availableZones = availableZones;
        this.jobs = jobs;
    }

    @Override
    public void execute() {
        int foundTimes;//查找次数
        boolean jobIsAssign;//作业是否已分配到内存中
        int leftSize;//分配完后的剩余空间
        int currentZoneId = 0;//当前所在的空闲分区的id
        AvailableZone currentZone;
        Job currentJob;

        //第一层循环，对每一个作业查找空间分配
        for (int i = 0; i < jobs.size(); i++) {
            foundTimes = 0;
            jobIsAssign = false;
            currentJob = jobs.get(i);//获取要分配的作业

            //第二层循环,在空闲分区链中寻找空闲分区
            while (true){
                //表示已经在空闲分区中查找的次数
                foundTimes ++;
                //获取当前空闲分区
                currentZone = availableZones.get(currentZoneId);
                //计算分配完后的剩余空间
                leftSize = currentZone.getSize() - currentJob.getResourceSize();

                if (leftSize < 0) {
                    //空间不足,该空闲分区不分配给当前作业
                    currentZoneId  = (currentZoneId + 1)%availableZones.size();//进入下一个空闲分区,顺序
                } else if (leftSize <= MIN_ZONE) {
                    //空间刚好足够大一点,分配完剩余空间不足以划分成另外一个空闲分区

                    //将整个分区分配该当前作业
                    AvailableZone zone = new AvailableZone(currentZone.getSize(),currentZone.getStartAddr());
                    currentJob.setZone(zone);

                    //将当前分区移出空闲分区队列中
                    availableZones.remove(currentZone);

                    //该分区移除后整个队列减少一个，所以当前id就是原下一个id
                    //currentZoneId = (currentZoneId)%availableZones.size();//进入下一空闲分区
                    if (currentZoneId == availableZones.size()){
                        currentZoneId --;
                    }
                    jobIsAssign = true;
                } else if (leftSize > MIN_ZONE) {
                    //分配完该作业后，剩余空间可以划分成一个新的空闲分区
                    int startAddr = currentZone.getStartAddr() + currentJob.getResourceSize();


                    AvailableZone jobZone = new AvailableZone(currentJob.getResourceSize(),currentZone.getStartAddr());
                    currentJob.setZone(jobZone);

                    currentZone.setSize(leftSize);
                    currentZone.setStartAddr(startAddr);
                    //进入下一个分区
                    currentZoneId = (currentZoneId + 1)%availableZones.size();
                    jobIsAssign = true;
                }

                if (foundTimes >= availableZones.size() || jobIsAssign == true) {
                    //如果完全查找完一遍空闲分区表没找到合适的空间,不为当前作业分配。
                    //或者该作业已经分配完毕
                    //不再继续查找空闲分区表
                    break;
                }
            }
        }

    }
}
