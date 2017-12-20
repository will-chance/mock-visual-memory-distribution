package model;

import model.AvailableZone;

/**
 * Created on 2017-12-18 7:06 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc: 作业
 */
public class Job {
    private int id;

    private int resourceSize;

    private boolean isAssigned;

    /**
     * 该作业所被分配的空间
     */
    private AvailableZone zone;

    public Job(int id, int resourceSize) {
        this.id = id;
        this.resourceSize = resourceSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public int getResourceSize() {
        return resourceSize;
    }

    public void setResourceSize(int resourceSize) {
        this.resourceSize = resourceSize;
    }

    public AvailableZone getZone() {
        return zone;
    }

    public void setZone(AvailableZone zone) {
        this.zone = zone;
    }
}
