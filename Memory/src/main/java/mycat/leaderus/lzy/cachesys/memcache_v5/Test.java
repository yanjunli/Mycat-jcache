package mycat.leaderus.lzy.cachesys.memcache_v5;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by 行知道人 on 2016/11/10.
 */
public class Test {
    public static void main(String[] aaa){



        Random r = new Random();
        int tmp = 500000;
        System.out.println(tmp);
        int rand[] = new int[tmp];
        String[] tmpUuid = new String[tmp];
        byte[][] tmpStrings=new byte[tmp][];
        long l=System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<FutureTask<Thread>> futureTasks = new ArrayList<FutureTask<Thread>>();
        for (int i = 0; i < tmp ; i++) {
            final int j = i;
            executorService.execute(new Thread(()->tmpStrings[j]=(radomeString(r.nextInt(1000)).getBytes()/*radomeString(r.nextInt(100)*/)));
            rand[i] = r.nextInt(tmp);
            tmpUuid[i]= UUID.randomUUID().toString();
        }
        executorService.shutdown();
        do {
            //System.out.print("初始化中。。。。。。");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (executorService.isTerminated()) {
                break;
            }
            //System.out.print("\r");
        } while (true);





        System.out.println("初始化："+(System.currentTimeMillis()-l)+"ms");
        l=System.currentTimeMillis();
        for (int i = 0; i < tmp; i++) {
            // ReadWritePool.set(tmpUuid[i].toString(), tmpStrings[i], System.currentTimeMillis() + 20000);
        }

        System.out.println("写入花费："+(System.currentTimeMillis()-l)+"ms");


        byte[][] tmpStringsRes=new byte[tmp][];
        l=System.currentTimeMillis();
        for (int i = 0; i < tmp; i++) {
            //    tmpStringsRes[i]=ReadWritePool.get(tmpUuid[rand[i]]);
        }
        System.out.println("随机读取花费"+(System.currentTimeMillis()-l)+"ms");
//        for (int i = 0; i < tmp; i++) {
//            System.out.println(new String(tmpStringsRes[i]));
//        }

    }

    public static String radomeString(int size){
        String tmp = "";
        for (int i = 0; i < size; i++) {
            tmp += 'a'+(i%25);
        }
        return tmp;
    }
}
