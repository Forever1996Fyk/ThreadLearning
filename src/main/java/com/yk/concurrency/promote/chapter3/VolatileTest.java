package com.yk.concurrency.promote.chapter3;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-08 21:21
 **/
public class VolatileTest {
    /**
     * 如果不加volatile, reader就会进入死循环, 不会打印。
     * update线程修改 INIT_VALUE 之后, reader线程并没有感知到 INIT_VALUE 的变化, 所以localValue仍然为0
     *
     * 这部分内容就涉及到Java内存模型, 以及CPU相关问题
     *
     * 对于CPU内存而言, 分为主内存, 高速缓存。
     * 程序加载过程后, 数据会被加载到主内存和缓存。
     * 在多线程的环境下, 每个线程都只会在第一次在主内存中获取数据, 之后就会在缓存中拿数据。
     * 而对数据修改时, 会在缓存中修改数据后, 刷新到主内存中去。
     *
     * 例如: i = 1; i = i + 1 ===> 在CPU中操作流程是: 在主内存中读取i的数据, 然后把i放入缓存中, cpu执行i+1的指令, 然后把结果放入到缓存, 最后缓存在刷新到主内存。
     * 上面的操作在单线程中是没有问题的, 但是在多线程环境中就会出现问题, 当第二个线程执行上面的操作时, 线程操作在自己的缓存中i的值仍然是1, 这就导致了两个线程对i+1的操作结果是一致的。
     * 所以最后两个线程都操作i = i+1, 但是最终i的值仍然是2。
     *
     * i = 1;
     * i = i + 1;
     * 线程1: main memory -> i -> cache i+1 -> cache(2) -> main memory(2)
     * 线程2: main memory -> i -> cache i+1 -> cache(2) -> main memory(2)
     * i = 2;
     *
     * 那么解决上面的问题有两种方式:
     *
     * 1. 总线锁机制: 给数据总线加锁 [总线(数据总线, 地址总线, 控制总线)], 在cpu中就是LOCK#指令。一旦对数据总线加锁, 那么每次操作这个数据都只能有一个线程, 但是这就导致了多核CPU串行化了, 效率大大降低
     * 2. CPU高速缓存一致性协议: 多个高速缓存中的数据副本始终保持一致。
     * 它的核心思想就是: (1) 当CPU写入数据时, 如果发现该变量被共享(也就是说, 在其他CPU中也存在该变量的副本), 就会发出一个信号, 通知其他CPU该变量的缓存无效
     *
     * 但是READER线程为什么一直不会执行呢?
     *
     * 这就是因为Java内存的优化导致的。
     * Java内存在判断一段程序在线程中没有任何写操作时, JVM就会认为这段程序不会去主内存拿数据, 而去缓存中拿数据, 而线程READER在缓存中的数据没有任何变化,就导致了INIT_VALUE一直不会变化
     *
     * 1. 原子性
     * 对于基本数据类型的变量读取和赋值是保证了原子性, 要么都成功, 要么都失败, 这些操作不可被中断
     * i = 10;
     * cache 10, memory 10
     *
     * a = 10; 原子性
     * b = a;  不满足, 1. read a 2. assign(赋值) b
     * c++;    不满足, 1. read c 2. add c 3. assign c
     * c=c+1;  不满足, 1. read c 2. add c 3. assign c
     *
     * Object obj = obj2; 也是原子性的。这种引用类型的赋值, 实际上赋值的是对象的地址。
     *
     * 2. 可见性
     * 使用volatile关键字保证可见性; 当一个变量被volatile修饰时, 如果这个变量被修改, 它会保证这个变量立即更新到主内存中, 其他线程读取时, 就直接从主内存中读取, 而不从高速缓存中读取
     *
     * 3. 有序性
     * Java内存模型中, 允许编译器在编译过程中对变量进行重排序, 只要满足数据最终一致性即可。在多线程下, 就会导致问题, 例如 chapter2 --- DoubleCheckLazySingletonObject中的单例模式
     *
     * 3.1 代码的执行顺序, 编写在前面的代码, 结果发生在后面的代码。Object o = new Object(); int i = 0; 在这段代码中 int i = 0 先完成, 而 Object o = new Object()后完成, 只是最终结果是没错的
     * 3.2 unlock必须发生在lock之后, 意思就是只能先加锁, 然后在解锁
     * 3.3 volatile修饰的变量, 对变量的写操作, 必须发生在读操作之前
     * 3.4 传递规则, 操作A先于B, B先于C, 那么A肯定先于C
     * 3.5 线程启动规则, start方法肯定先于线程的run方法
     * 3.6 线程中断规则, interrupt这个动作, 必须发生在捕获该动作之前, 就是说如果要捕获异常, 一定是先中断, 然后再去中断这个线程
     * 3.7 线程中结规则, 所有的操作都发生在线程死亡之前
     * 3.8 对象销毁规则, 初始化必须发生在finalize之前
     *
     * volatile关键字
     * 一旦一个共享变量被volatile修饰,具备两层语义
     * 1. 保证不同线程间的可见性
     * 2. 禁止对其进行重排序, 也就是保证了有序性
     * 3. 并没有保证原子性
     *
     * volatile关键字在代码中的意义：
     *
     * 1. 保证重排序不会包后面的指令放到屏障的前面, 也不会把前面的放到后面
     * 2. 强制对缓存的修改操作立刻写入主存
     * 3. 如果是写操作, 它会导致其他cpu中的缓存失效
     *
     * volatile的使用场景:
     * 1. 状态量的标记
     * volatile boolean flag = true;
     *
     * while(flag) { // }
     *
     * void close() { flag = false; }
     *
     * 2. 屏障前后的一致性: 之前的例子: double check, chapter2----VDCLazySingletonObject
     * volatile boolean init = false;
     * -------------- Thread -1 -------------
     * //......
     * obj = new createObj()    1;
     * init = true              2;
     *
     * -------------- Thread -2 -------------
     * while(!init){
     *     sleep();
     * }
     * useTheObj(obj);
     * --------------------------------------
     */
    private volatile static int INIT_VALUE = 0;

    private final static int MAX_LIMIT = 5;

    public static void main(String[] args) {
        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (localValue < MAX_LIMIT) {
                if (localValue != INIT_VALUE) {
                    System.out.printf("The value update to [%d]\n", INIT_VALUE);
                    localValue = INIT_VALUE;
                }
            }

        }, "READER").start();

        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (INIT_VALUE < MAX_LIMIT) {
                System.out.printf("Update the value to [%d]\n", ++localValue);
                INIT_VALUE = localValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "UPDATE").start();
    }
}