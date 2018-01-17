package model;

/**
 * Created on 2017-12-18 7:04 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc: 空闲分区
 */
public class AvailableZone {
    private int id;

    private int size;

    private int startAddr;

    public AvailableZone(int size, int startAddr) {
        this.size = size;
        this.startAddr = startAddr;
    }

    public AvailableZone(int id, int size, int startAddr) {
        this.id = id;
        this.size = size;
        this.startAddr = startAddr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(int startAddr) {
        this.startAddr = startAddr;
    }

    public String getDescription() {
        int end = startAddr + size -1;
        return size + "(" + startAddr + "~" + end + ")";
    }
}
