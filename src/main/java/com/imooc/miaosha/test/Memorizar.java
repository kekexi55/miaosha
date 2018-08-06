package com.imooc.miaosha.test;

import java.util.concurrent.*;

public class Memorizar  implements Computable {
    private ConcurrentHashMap map = null;

    public Memorizar(ConcurrentHashMap map) {
        this.map = map;
    }

    @Override
    public Object compute(Object o) {
        while (true){
            FutureTask task = (FutureTask)map.get(o);
            final Object no = o;
            if(task == null){
                Callable callable = new Callable() {
                    @Override
                    public Object call() throws Exception {
                        return doSomething(no);
                    }
                };
                FutureTask futureTask = new FutureTask(callable);
                FutureTask f1 = (FutureTask)map.putIfAbsent(o,futureTask);
                if(f1 == null){//返回null表明put成功
                    task = futureTask;
                    futureTask.run();
                }else{//返回原值
                    task = f1;
                }
            }
            try {
                return task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                map.remove(no);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    //模拟复杂的计算
    private Object doSomething(Object o){
        return null;
    }
}
