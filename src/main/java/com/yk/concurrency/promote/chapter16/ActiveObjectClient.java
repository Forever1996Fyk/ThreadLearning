package com.yk.concurrency.promote.chapter16;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-12 21:46
 **/
public class ActiveObjectClient {

    public static void main(String[] args) {
        ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
        new MakerClientThread(activeObject, "Alice").start();
        new MakerClientThread(activeObject, "Bobby").start();

        new DisplayClientThread("YK", activeObject).start();
    }
}