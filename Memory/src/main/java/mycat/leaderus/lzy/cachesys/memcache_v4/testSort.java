package mycat.leaderus.lzy.cachesys.memcache_v4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by wadasiwaAcer01 on 2016/12/4.
 */
public class testSort {
    public static void main(String[] args) {
        List<AA> aa = new ArrayList<>();
        long l = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            aa.add(new AA());
        }
        System.out.println(System.nanoTime() - l);
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        l = System.nanoTime();
        Collections.sort(aa);
        System.out.println(System.nanoTime() - l);
    }


}

class AA implements Comparable<AA> {

    private int anInt = new Random().nextInt();

    @Override
    public int compareTo(AA o) {
        return o.anInt == this.anInt ? 0 : o.anInt < this.anInt ? -1 : 1;
    }
}
