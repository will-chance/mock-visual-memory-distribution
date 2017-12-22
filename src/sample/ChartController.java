package sample;

import algorithm.*;
import com.alibaba.fastjson.JSON;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import model.AvailableZone;
import model.Job;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created on 2017-12-19 2:35 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc:
 */
public class ChartController {

    @FXML
    private TableView memoryTableView;

    @FXML
    private TableView jobTableView;

    @FXML
    private RadioButton firstFit;
    @FXML
    private RadioButton nextFit;
    @FXML
    private RadioButton bestFit;
    @FXML
    private RadioButton worstFit;

    @FXML
    private ScrollPane chartPane;

    private ScatterChart memoryDistributionChart;

    private List<AvailableZone> availableZones;

    private List<AvailableZone> oldAvailableZones;

    private List<Job> jobs;

    private Job selectedJob;

    /**
     * 分配算法
     * 取值及代表的算法如下
     * FF:First Fit 首次适应算法(DEFAULT)
     * NF:Next Fit 循环首次适应
     * BF:Best Fit 最佳适应
     * WF:Worst Fit 最坏适应
     *
     */
    private String algorithm;

    @FXML
    private void initialize() {
        initAlgorithm();
        initRadioButton();
        initMemoryDistribution();
        initJobTableView();
    }

    private void initAlgorithm(){
        this.algorithm = "FF";
    }

    private void initRadioButton(){
        final ToggleGroup algorithmGroup = new ToggleGroup();
        firstFit.setToggleGroup(algorithmGroup);
        nextFit.setToggleGroup(algorithmGroup);
        bestFit.setToggleGroup(algorithmGroup);
        worstFit.setToggleGroup(algorithmGroup);

        algorithmGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (algorithmGroup.getSelectedToggle() != null){
                        this.algorithm = algorithmGroup.getSelectedToggle().getUserData().toString();
                    }
                }
        );
    }

    private void initMemoryDistribution(){
        final NumberAxis xAxis = new NumberAxis(0,8,1);
        final NumberAxis yAxis = new NumberAxis(0,256,8);
        xAxis.setLabel("Memory Address/k");
        yAxis.setLabel("Memory Address/k");
        this.memoryDistributionChart = new ScatterChart(xAxis,yAxis);
        memoryDistributionChart.setTitle("Memory Distribution");
        memoryDistributionChart.setPrefHeight(730);
        memoryDistributionChart.setPrefWidth(360);
        chartPane.setContent(memoryDistributionChart);
    }

    private void initJobTableView(){
        jobTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           selectedJob = (Job) newValue;
        });
    }

    @FXML
    private void randomMemory(){
        memoryTableView.getItems().clear();
        memoryDistributionChart.getData().clear();
        availableZones = RandomUtils.randomMemory();
        if (oldAvailableZones == null) {
            oldAvailableZones = new ArrayList<>();
        }
        oldAvailableZones.clear();
        oldAvailableZones.addAll(availableZones);
        memoryTableView.getItems().addAll(availableZones);
        showUnavailableMemory(availableZones);
    }

    @FXML
    private void memoryCompact(){
        //todo
        int totalFree = 0;
        for (AvailableZone zone :
                availableZones) {
            totalFree = totalFree + zone.getSize();
        }
    }

    @FXML
    private void recycleJobResources(){
        recycle(selectedJob);
        memoryDistributionChart.getData().clear();
        showUnavailableMemory(oldAvailableZones);
        showAllocatedJobMemoryDistribution(jobs);

        updateJobTableView(this.jobs);
        updateMemoryTableView(availableZones);
    }

    private void recycle(Job job){
        //从队列中获取到最新的空闲分区表
        if (job.getZone() !=null){
            AvailableZone newAvailableZone = new AvailableZone(job.getZone().getSize(),job.getZone().getStartAddr());
            availableZones.add(newAvailableZone);
            Collections.sort(availableZones,new StartAddressComparator());
            for (int i = 0; i < availableZones.size() - 2; i++) {
                AvailableZone currentZone = availableZones.get(i);
                AvailableZone nextZone = availableZones.get(i + 1);

                if (currentZone.getSize() + currentZone.getStartAddr() == nextZone.getStartAddr()) {
                    //合并
                    currentZone.setSize(currentZone.getSize() + nextZone.getSize());
                    //移除下一个分区
                    availableZones.remove(nextZone);
                    i--;
                }
            }
            job.setZone(null);
        }
    }

    @FXML
    private void randomJob(){
        jobTableView.getItems().clear();
        jobs = RandomUtils.randomJob();
        jobTableView.getItems().addAll(jobs);
    }

    @FXML
    private void resetJob(){
        if (oldAvailableZones != null){
            availableZones.clear();
            availableZones.addAll(oldAvailableZones);
        }
        for (Job job :
                jobs) {
            job.setZone(null);
        }
        memoryDistributionChart.getData().clear();
        showUnavailableMemory(availableZones);
        updateJobTableView(jobs);
        updateMemoryTableView(availableZones);
    }

    private void updateMemoryTableView(List<AvailableZone> memories){
        Collections.sort(memories,new StartAddressComparator());
        int i = 0;
        for (AvailableZone memory :memories) {
            memory.setId(i++);
        }
        //update memory table view
        memoryTableView.getItems().clear();
        memoryTableView.getItems().addAll(memories);
    }

    @FXML
    private void memoryAllocate() {
        ObservableList memories = memoryTableView.getItems();
        availableZones.clear();
        availableZones.addAll(memories);
        if (availableZones == null || availableZones.size() ==0){
//todo null exception
        }

        //todo delete
        for (AvailableZone zone:availableZones){
            System.out.println(JSON.toJSONString(zone));
        }

        ObservableList<Job> jobs = jobTableView.getItems();
        this.jobs.clear();
        this.jobs.addAll(jobs);
        if (jobs == null || jobs.size() == 0) {
//todo null exception
        }
        Algorithm alg = selectAlgorithm(availableZones,jobs);
        alg.execute();

        Collections.sort(availableZones,new StartAddressComparator());
        int i = 0;
        for (AvailableZone zone :
                availableZones) {
            zone.setId(i++);
        }

        //todo delete
        for (Job job:jobs){
            System.out.println(JSON.toJSONString(job));
        }
        for (AvailableZone zone:availableZones){
            System.out.println(JSON.toJSONString(zone));
        }
        showAllocatedJobMemoryDistribution(jobs);
        updateJobTableView(this.jobs);
        updateMemoryTableView(availableZones);
    }

    private Algorithm selectAlgorithm(List<AvailableZone> availableZones,List<Job> jobs){
        Algorithm alg = null;
        switch (algorithm){
            case "FF":{
                alg = new FirstFitAlgorithm(availableZones,jobs);
                break;
            }
            case "NF":{
                alg = new NextFitAlgorithm(availableZones,jobs);
                break;
            }
            case "BF":{
                alg = new BestFitAlgorithm(availableZones,jobs);
                break;
            }
            case "WF":{
                alg = new WorstFitAlgorithm(availableZones,jobs);
                break;
            }
        }
        return alg;
    }

    private void showUnavailableMemory(List<AvailableZone> memories){
        XYChart.Series unavailableSeries = new XYChart.Series();
        unavailableSeries.setName("已用空间");
        for (int i = 0; i < 256; i++) {
            unavailableSeries.getData().add(new XYChart.Data(
                    0.5 + (i%8),4 + 8 * (i/8)
            ));
        }
        Collections.sort(memories,new StartAddressComparator());//从低地址到高地址排序
        for (int k = memories.size() - 1;k>=0;k--) {//从高地址开始遍历，去掉空闲空间
            AvailableZone availableZone = memories.get(k);
            int startAddr = availableZone.getStartAddr();
            int size = availableZone.getSize();
            unavailableSeries.getData().remove(startAddr,startAddr + size);
        }
        this.memoryDistributionChart.getData().add(unavailableSeries);
    }

    /**
     * 显示已分配作业在内存中的分布状况
     * @param jobs
     */
    private void showAllocatedJobMemoryDistribution(List<Job> jobs){
        List<XYChart.Series> jobSeries = new ArrayList<>();
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            if (job.getZone() != null) {//已分配
                int startAddr = job.getZone().getStartAddr();
                int size = job.getZone().getSize();
                XYChart.Series series = new XYChart.Series();
                series.setName("作业-"+i);
                for (int j = startAddr; j < startAddr + size; j++) {
                    series.getData().add(
                            new XYChart.Data(0.5 + (j%8),4+8*(j/8))
                    );
                }
                jobSeries.add(series);
            }
        }
        memoryDistributionChart.getData().addAll(jobSeries);
    }

    private void updateJobTableView(List<Job> jobs) {
        jobTableView.getItems().clear();
        jobTableView.getItems().addAll(jobs);
    }
}
