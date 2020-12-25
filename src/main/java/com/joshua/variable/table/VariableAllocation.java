package com.joshua.variable.table;

import com.joshua.Job;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Author:Joshua
 * @Date:2020/12/23
 */
public class VariableAllocation {

    static Integer minimumSize = 8;

    /**
     * 动态分配
     *
     * @param table 要操作的分区表
     * @param job   程序
     * @param entry 要操作的表项
     */
    private static void dynamicAlloc(List<VariableTableEntry> table, Job job, VariableTableEntry entry) {

        Comparator<VariableTableEntry> comparator = new Comparator<VariableTableEntry>() {

            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }

        };

        ///划分程序所在的分区
        VariableTableEntry jobEntry = new VariableTableEntry();
        jobEntry.setNumber(entry.getNumber());
        jobEntry.setSize(job.getSize());
        jobEntry.setStartAddress(entry.getStartAddress());
        jobEntry.setStatus("allocated");
        jobEntry.setJobNumber(job.getNumber());

        table.set(table.indexOf(entry), jobEntry);


        VariableTableEntry newEntry = new VariableTableEntry();
        newEntry.setNumber(table.size() + 1);
        newEntry.setSize(entry.getSize() - job.getSize());
        newEntry.setStartAddress(entry.getStartAddress() + job.getSize());
        newEntry.setStatus("Null");

        table.add(table.indexOf(jobEntry) + 1, newEntry);

        System.out.println("将程序所在的分区设为第" + jobEntry.getNumber() + "分区");
        System.out.println("将剩余部分设为第" + newEntry.getNumber() + "号分区");
        table.sort(comparator);     //将分区按地址排序

    }

    /**
     * 通过首次适应算法进行分配
     *
     * @param table 分区表
     * @param job   程序
     */
    public static void allocMemByFirstFit(List<VariableTableEntry> table, Job job) {

        Comparator<VariableTableEntry> comparator = new Comparator<VariableTableEntry>() {

            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }

        };

        table.sort(comparator);
        int flag = 0;
        for (VariableTableEntry entry : table) {
            if (entry.getSize() >= job.getSize() && entry.getStatus().equals("Null")) {
                if (entry.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    entry.setStatus("allocated");
                    entry.setJobNumber(job.getNumber());
                    flag = 1;
                    System.out.println("因为程序大小适合,将第" + entry.getNumber() + "号分区全部分配");
                    break;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区表
                    dynamicAlloc(table, job, entry);
                    flag = 1;
                    break;
                }
            }
        }
        if (flag == 0) {
            System.out.println("分配失败,没有合适的分区.");
        }
    }


    /**
     * 通过循环首次适应算法进行分配
     *
     * @param table 分区表
     * @param job   程序
     * @return index 返回当前检测到哪一项了
     */
    public static Integer allocMemByNextFit(List<VariableTableEntry> table, Job job, Integer startPosition) {
        Comparator<VariableTableEntry> comparator = new Comparator<VariableTableEntry>() {

            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getStartAddress() - o2.getStartAddress();
            }

        };

        table.sort(comparator);//将表项按地址高低排列
        int flag = 0;
        for (int i = startPosition; i < table.size(); i++) {
            VariableTableEntry entry = table.get(i);    //从上次的开始check
            if (entry.getSize() >= job.getSize() && entry.getStatus().equals("Null")) {

                if (entry.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    entry.setStatus("allocated");
                    entry.setJobNumber(job.getNumber());
                    System.out.println("因为程序大小适合,将第" + entry.getNumber() + "号分区全部分配");
                    return i + 1;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区表
                    dynamicAlloc(table, job, entry);
                    return i + 1;
                }

            }


        }
        //如果到最后了还没找到合适的分区,则从头开始找
        for (int i = 0; i < startPosition; i++) {
            VariableTableEntry entry = table.get(i);
            if (entry.getSize() >= job.getSize() && entry.getStatus().equals("Null")) {
                if (entry.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    entry.setStatus("allocated");
                    entry.setJobNumber(job.getNumber());
                    System.out.println("因为程序大小适合,将第" + entry.getNumber() + "号分区全部分配");
                    return i + 1;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区表
                    dynamicAlloc(table, job, entry);
                    return i + 1;
                }
            }
        }
        System.out.println("分配失败,没和合适的分区.");
        return startPosition;
    }

    /**
     * 通过最佳适应算法进行分配
     *
     * @param table 分区表
     * @param job   程序
     */
    public static void allocMemByBestFit(List<VariableTableEntry> table, Job job) {
        Comparator<VariableTableEntry> comparator = new Comparator<VariableTableEntry>() {
            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getSize() - o2.getSize(); //将表项按大小排列
            }

        };

        table.sort(comparator);     //将分区表按分区大小排列

        int flag = 0;
        for (VariableTableEntry entry : table) {
            if (entry.getSize() >= job.getSize() && entry.getStatus().equals("Null")) {
                if (entry.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    entry.setStatus("allocated");
                    entry.setJobNumber(job.getNumber());
                    flag = 1;
                    System.out.println("因为程序大小适合,将第" + entry.getNumber() + "号分区全部分配");
                    break;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区表
                    dynamicAlloc(table, job, entry);
                    flag = 1;
                    break;
                }
            }
        }
        if (flag == 0) {
            System.out.println("分配失败,没有合适的分区.");
        }
    }

    /**
     * 回收,修改分区表
     *
     * @param table 分区表
     * @param job   需要回收的程序
     */
    public static void recycleMem(List<VariableTableEntry> table, Job job) {

        Comparator<VariableTableEntry> comparator = new Comparator<VariableTableEntry>() {

            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }

        };
        table.sort(comparator);

        for (int i = 0; i < table.size(); i++) {
            //找到需要回收的分区
            VariableTableEntry entry = table.get(i);
            //判断待回收分区的前后分区状态
            if (entry.getJobNumber() != null && entry.getJobNumber().equals(job.getNumber())) {
                //判断是否为第一和最后一项
                if (i != 0 && i != table.size() - 1) {
                    //前后都为空,合并
                    if ("Null".equals(table.get(i + 1).getStatus()) && "Null".equals(table.get(i - 1).getStatus())) {
                        System.out.println("前后都空,合并");
                        VariableTableEntry before = table.get(i - 1);
                        VariableTableEntry behind = table.get(i + 1);
                        //大小为三个分区合并大小
                        before.setSize(entry.getSize() + behind.getSize() + before.getSize());
                        //删除后面俩分区
                        table.remove(i);
                        //ArrayList,移除一个后,后一个的index就变为原来这个的index 所以再次remove(i)
                        table.remove(i);
                        System.out.println("将第" + i + " ,第" + (i - 1) + " ,第" + (i + 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else if ("Null".equals(table.get(i - 1).getStatus())) {
                        System.out.println("前空,合并");
                        VariableTableEntry before = table.get(i - 1);
                        //大小合并
                        before.setSize(entry.getSize() + before.getSize());
                        //删除回收项
                        table.remove(i);
                        System.out.println("将第" + i + " ,第" + (i - 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else if ("Null".equals(table.get(i + 1).getStatus())) {
                        //若为空,则合并
                        System.out.println("后空,合并");
                        VariableTableEntry behind = table.get(i + 1);
                        entry.setSize(entry.getSize() + behind.getSize());
                        entry.setStatus("Null");
                        //删除后一项
                        table.remove(i + 1);
                        System.out.println("将第" + i + " ,第" + (i + 1) + "号分区合并为一个分区" + i);
                        break;
                    } else {
                        //前后都不空,则回收当前即可
                        System.out.println("前后都不空,只回收此项");
                        entry.setStatus("Null");
                        entry.setJobNumber(null);
                    }
                } else if (i != 0) {
                    //若不是第一项,则判断前面一项是否为空
                    System.out.println("前空,合并");
                    if ("Null".equals(table.get(i - 1).getStatus())) {
                        VariableTableEntry before = table.get(i - 1);
                        before.setSize(entry.getSize() + before.getSize());
                        //删除回收项
                        table.remove(i);
                        System.out.println("将第" + i + " ,第" + (i - 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else {
                        //前后都不空,则回收当前即可
                        System.out.println("前后都不空,只回收此项");
                        entry.setStatus("Null");
                        entry.setJobNumber(null);
                    }
                } else if (i != table.size() - 1) {
                    //不是最后一项,则判断后一项是否为空
                    if ("Null".equals(table.get(i + 1).getStatus())) {
                        System.out.println("后空,合并");
                        //若为空,则合并
                        VariableTableEntry behind = table.get(i + 1);
                        entry.setSize(entry.getSize() + behind.getSize());
                        entry.setStatus("Null");
                        //删除后一项
                        table.remove(i + 1);
                        System.out.println("将第" + i + " ,第" + (i + 1) + "号分区合并为一个分区" + i);
                        break;
                    } else {
                        //前后都不空,则回收当前即可
                        System.out.println("前后都不空,只回收此项");
                        entry.setStatus("Null");
                        entry.setJobNumber(null);
                    }
                }
            }
        }
    }

    public static void recycleSmall(List<VariableTableEntry> table, VariableTableEntry entry) {

        Comparator<VariableTableEntry> comparator = new Comparator<VariableTableEntry>() {

            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }

        };
        table.sort(comparator);

        for (int i = 0; i < table.size(); i++) {
            //找到需要回收的分区
            VariableTableEntry e = table.get(i);
            //判断待回收分区的前后分区状态
            if (entry.equals(e)) {

                //System.out.println("进入了判断分区");

                //判断是否为第一和最后一项
                if (i != 0 && i != table.size() - 1) {
                    //前后都为空,合并
                    if ("Null".equals(table.get(i + 1).getStatus()) && "Null".equals(table.get(i - 1).getStatus())) {
                        System.out.println("前后都空,合并");
                        VariableTableEntry before = table.get(i - 1);
                        VariableTableEntry behind = table.get(i + 1);
                        before.setSize(entry.getSize() + behind.getSize() + before.getSize());      //大小为三个分区合并大小
                        //删除后面俩分区
                        table.remove(i);
                        table.remove(i);            //ArrayList,移除一个后,后一个的index就变为原来这个的index 所以再次remove(i)
                        System.out.println("将第" + i + " ,第" + (i - 1) + " ,第" + (i + 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else if ("Null".equals(table.get(i - 1).getStatus())) {
                        System.out.println("前空,合并");
                        VariableTableEntry before = table.get(i - 1);
                        //大小合并
                        before.setSize(entry.getSize() + before.getSize());
                        //删除回收项
                        table.remove(i);
                        System.out.println("将第" + i + " ,第" + (i - 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else if ("Null".equals(table.get(i + 1).getStatus())) {
                        //若为空,则合并
                        System.out.println("后空,合并");
                        VariableTableEntry behind = table.get(i + 1);
                        //合并大小
                        entry.setSize(entry.getSize() + behind.getSize());
                        entry.setStatus("Null");
                        //删除后一项
                        table.remove(i + 1);
                        System.out.println("将第" + i + " ,第" + (i + 1) + "号分区合并为一个分区" + i);
                        break;
                    } else {
                        //前后都不空,则不能合并
                        System.out.println("前后都不空,不能合并");
                    }
                } else if (i != 0) {
                    //若不是第一项,则判断前面一项是否为空
                    System.out.println("前空,合并");
                    if ("Null".equals(table.get(i - 1).getStatus())) {
                        VariableTableEntry before = table.get(i - 1);
                        //大小合并
                        before.setSize(entry.getSize() + before.getSize());
                        //删除回收项
                        table.remove(i);
                        System.out.println("将第" + i + " ,第" + (i - 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else {
                        //前后都不空,则不能合并
                        System.out.println("前后都不空,不能合并");
                    }
                } else if (i != table.size() - 1) {
                    //不是最后一项,则判断后一项是否为空
                    if ("Null".equals(table.get(i + 1).getStatus())) {
                        System.out.println("后空,合并");
                        //若为空,则合并
                        VariableTableEntry behind = table.get(i + 1);
                        entry.setSize(entry.getSize() + behind.getSize());  //合并大小
                        entry.setStatus("Null");
                        //删除后一项
                        table.remove(i + 1);
                        System.out.println("将第" + i + " ,第" + (i + 1) + "号分区合并为一个分区" + i);
                        break;
                    } else {
                        //前后都不空,则不能合并
                        System.out.println("前后都不空,不能合并");
                    }
                }
            }
        }
    }

    public static void recyclePieces(List<VariableTableEntry> table) {
        List<VariableTableEntry> smallPieces = new ArrayList<>();
        for (VariableTableEntry entry : table) {
            if (entry.getSize() < 8) {
                System.out.println("表项"+entry+"为小碎片");
                smallPieces.add(entry);
            }
        }
    }

}
