package sample;

import algorithm.*;
import com.alibaba.fastjson.JSON;
import model.AvailableZone;
import model.Job;

import java.util.Collections;
import java.util.List;

import static sample.RandomUtils.randomJob;
import static sample.RandomUtils.randomMemory;

/**
 * Created on 2017-12-18 7:39 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc:
 */
public class TestAlgorithm {

    public static void main(String[] args) {


        List<AvailableZone> availableZones = randomMemory();
        List<Job> jobs = randomJob();

//        Algorithm algorithm = new NextFitAlgorithm(availableZones,jobs);
//        Algorithm algorithm = new FirstFitAlgorithm(availableZones,jobs);
//        Algorithm algorithm = new WorstFitAlgorithm(availableZones,jobs);
        Algorithm algorithm = new BestFitAlgorithm(availableZones,jobs);
        algorithm.execute();
        Collections.sort(availableZones,new StartAddressComparator());

        for (AvailableZone zone:
             availableZones) {
            System.out.println(JSON.toJSONString(zone));
        }

        for (Job job :jobs) {
            System.out.println(JSON.toJSONString(job));
        }
    }
}
