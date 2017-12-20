package algorithm;

import model.AvailableZone;

import java.util.Comparator;

/**
 * Created on 2017-12-18 11:03 PM
 * Author: Bowei Chan
 * E-mail: bowei_chan@163.com
 * Project: memory-manage
 * Desc:
 */
public class StartAddressComparator implements Comparator<AvailableZone> {
    @Override
    public int compare(AvailableZone o1, AvailableZone o2) {
        return o1.getStartAddr() - o2.getStartAddr();
    }
}
