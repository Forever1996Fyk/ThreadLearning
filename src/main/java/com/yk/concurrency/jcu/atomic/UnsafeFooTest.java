package com.yk.concurrency.jcu.atomic;

import com.google.common.collect.Sets;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-19 20:45
 **/
public class UnsafeFooTest {

    public static void main(String[] args) throws Exception {
       /* Simple simple = new Simple();
        System.out.println(simple.get());*/


        // 这种反射方式不会绕过初始化
//        Simple simple = Simple.class.newInstance();

        // 这种反射会绕过初始化
//        Class.forName("com.yk.concurrency.jcu.atomic.UnsafeFooTest$Simple");

        // 这种方式利用unsafe直接开辟内存, 给Simple对象 也会绕过对象的初始化
//        Unsafe unsafe = getUnsafe();
//        Simple simple = (Simple) unsafe.allocateInstance(Simple.class);
//        System.out.println(simple.get());
//        System.out.println(simple.getClass());
//        System.out.println(simple.getClass().getClassLoader());

        // 很明显这种方式是不会工作的
//        Guard guard = new Guard();
//        guard.work();

//        Guard guard = new Guard();
//        Field f = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
//        Unsafe unsafe = getUnsafe();
//        unsafe.putInt(guard, unsafe.objectFieldOffset(f), 42);
//        guard.work();

//        byte[] bytes = loadClassContent();
//        Unsafe unsafe = getUnsafe();
//        Class aClass = unsafe.defineClass(null, bytes, 0, bytes.length, null, null);
//        int v = (int) aClass.getMethod("get").invoke(aClass.newInstance(), null);
//        System.out.println(v);

        Simple simple = new Simple();
        System.out.println(sizeOf(simple));


    }

    private static long sizeOf(Object obj) {
        Unsafe unsafe = getUnsafe();
        Set<Field> fields = Sets.newHashSet();
        Class c = obj.getClass();
        while (c != Object.class) {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field field : declaredFields) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(field);
                }
            }
            c = c.getSuperclass();
        }

        long maxOffSet = 0;
        for (Field field : fields) {
            long offSet = unsafe.objectFieldOffset(field);
            if (offSet > maxOffSet) {
                maxOffSet = offSet;
            }
        }

        return ((maxOffSet / 8) + 1) * 8;
    }

    public static byte[] loadClassContent() throws IOException {
        File f = new File("/Users/yukaifan/IdeaProjects/ThreadLearning/target/classes/A.class");
        FileInputStream fis = new FileInputStream(f);
        byte[] content = new byte[(int) f.length()];
        fis.read(content);
        fis.close();
        return content;
    }

    static class Guard {
        private int ACCESS_ALLOWED = 1;

        private boolean allow() {
            return 42 == ACCESS_ALLOWED;
        }

        public void work() {
            if (allow()) {
                System.out.println("I am work by allowed");
            }
        }
    }

    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            //允许访问私有属性
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class Simple {
        private long l = 0;
        private int i = 10;
        private byte b = (byte) 0x01;

        public Simple() {
            this.l = 1;
            System.out.println("=============");
        }

        public long get() {
            return l;
        }
    }
}