package com.yk.concurrency.jcu.utils.countdownlatch;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-20 20:35
 **/
public class CountDownLatchExample4 {
    private final static Random random = new Random(System.currentTimeMillis());

    static class Event {
        int id = 0;

        public Event(int id) {
            this.id = id;
        }
    }

    interface Watcher {
        void startWatch();

        void done(Table table);
    }

    static class TaskBatch implements Watcher {

        private CountDownLatch latch;

        private TaskGroup taskGroup;

        public TaskBatch(int size, TaskGroup taskGroup) {
            this.latch = new CountDownLatch(size);
            this.taskGroup = taskGroup;
        }

        @Override
        public void startWatch() {
        }

        @Override
        public void done(Table table) {
            latch.countDown();
            if (latch.getCount() == 0) {
                System.out.println("The table  " + table.tableName + "finished work, [" + table + "]");
                taskGroup.done(table);
            }
        }
    }

    static class TaskGroup implements Watcher {

        private CountDownLatch latch;

        private Event event;

        public TaskGroup(int size, Event event) {
            this.latch = new CountDownLatch(size);
            this.event = event;
        }

        @Override
        public void startWatch() {
        }

        @Override
        public void done(Table table) {
            latch.countDown();
            if (latch.getCount() == 0) {
                System.out.println("========All of table done in event:" + event.id);
            }
        }
    }

    static class Table {
        String tableName;
        long sourceRecordCount = 10;
        long targetCount;
        String sourceColumnSchema = "<table name='a'><column name='col1' type='varchar2'/></table>";
        String targetColumnSchema = "";

        public Table(String tableName, long sourceRecordCount) {
            this.tableName = tableName;
            this.sourceRecordCount = sourceRecordCount;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "tableName='" + tableName + '\'' +
                    ", sourceRecordCount=" + sourceRecordCount +
                    ", targetCount=" + targetCount +
                    ", sourceColumnSchema='" + sourceColumnSchema + '\'' +
                    ", targetColumnSchema='" + targetColumnSchema + '\'' +
                    '}';
        }
    }

    private static List<Table> capture(Event event) {
        List<Table> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(new Table("table-"+ event.id + "-" + i, i*1000));
        }
        return list;
    }

    public static void main(String[] args) {
        Event[] events =  {new Event(1), new Event(2)};
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (Event event : events) {
            List<Table> tables = capture(event);
            TaskGroup taskGroup = new TaskGroup(tables.size(), event);
            for (Table table : tables) {
                TaskBatch taskBatch = new TaskBatch(2, taskGroup);
                TrustSourceRecordColumns columnsRunnable = new TrustSourceRecordColumns(table, taskBatch);
                TrustSourceRecordCount countRunnable = new TrustSourceRecordCount(table, taskBatch);
                executor.submit(columnsRunnable);
                executor.submit(countRunnable);
            }
        }
    }

    static class TrustSourceRecordCount implements Runnable {
        private final Table table;

        private final TaskBatch taskBatch;

        public TrustSourceRecordCount(Table table, TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10000));
                table.targetCount = table.sourceRecordCount;
               // System.out.println("The table " + table.tableName + " target count capture done and update.");
                taskBatch.done(table);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TrustSourceRecordColumns implements Runnable {
        private final Table table;

        private final TaskBatch taskBatch;

        public TrustSourceRecordColumns(Table table, TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            table.targetColumnSchema = table.sourceColumnSchema;
//            System.out.println("The table " + table.tableName + " target columns capture done and update.");
            taskBatch.done(table);
        }
    }
}