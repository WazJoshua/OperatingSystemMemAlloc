package com.joshua.variable.table;

import com.joshua.Job;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * 可变式分区分配的主程序
 *
 * @Author:Joshua
 * @Date:2020/12/23
 */
public class VariableMainSystem {

    public static void initTable(List<VariableTableEntry> table) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;

        do {
            System.out.println("输入表项数:(不为0)");
            i = scanner.nextInt();
        } while (i == 0);

        for (int j = 0; j < i; j++) {
            System.out.println("输入第" + j + "项的信息:(起始地址 大小)");
            int startAddress = scanner.nextInt();
            int size = scanner.nextInt();
            VariableTableEntry entry = new VariableTableEntry();
            entry.setSize(size);
            entry.setStartAddress(startAddress);
            entry.setNumber(j);
            entry.setStatus("Null");
            table.add(entry);
        }
    }

    public static void alloc(int method, List<VariableTableEntry> table, Job job) {
        if (method == 1) {
            VariableAllocation.allocMemByFirstFit(table, job);
        } else if (method == 3) {
            VariableAllocation.allocMemByBestFit(table, job);
        }

    }

    public static Integer alloc(List<VariableTableEntry> table, Job job, Integer startPosition) {

        startPosition = VariableAllocation.allocMemByNextFit(table, job, startPosition);
        return startPosition;
    }

    public static void outTable(List<VariableTableEntry> table, Integer method) {
        //按起始地址从小到大排序
        Comparator<VariableTableEntry> comparatorAddress = new Comparator<VariableTableEntry>() {
            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }
        };
        //按分区大小从小到大排序
        Comparator<VariableTableEntry> comparatorSize = new Comparator<VariableTableEntry>() {
            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getSize() - o2.getSize(); //将表项按地址高低排列
            }

        };
        if (method == 3) {
            table.sort(comparatorSize);
        } else {
            table.sort(comparatorAddress);
        }

        //System.out.println("当前分区表信息:");
        System.out.println("**********************当前分区表信息*********************");
        for (VariableTableEntry e :
                table) {
            System.out.println(e);
        }
        System.out.println("******************************************************");
    }

    public static void main(String[] args) {

        //按起始地址从小到大排序
        Comparator<VariableTableEntry> comparatorAddress = new Comparator<VariableTableEntry>() {
            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }
        };
        //按分区大小从小到大排序
        Comparator<VariableTableEntry> comparatorSize = new Comparator<VariableTableEntry>() {
            @Override
            public int compare(VariableTableEntry o1, VariableTableEntry o2) {
                return o1.getSize() - o2.getSize(); //将表项按地址高低排列
            }

        };

        Scanner scanner = new Scanner(System.in);

        Integer startPosition = 0;

        System.out.println("请先初始化分区表:");
        List<VariableTableEntry> table = new ArrayList<>();
        List<VariableTableEntry> freeTable = new ArrayList<>();

        initTable(table);
        System.out.println("选择分配算法:(输入编号)");
        System.out.println("1.FirstFit  2.NextFit  3.BestFit");
        int method = scanner.nextInt();
        if (method == 3) {
            //如果为BestFit,就按分区大小从小到大排序
            table.sort(comparatorSize);
        } else if (method == 2 || method == 1) {
            //其他两种都是按起始地址从小到大排序
            table.sort(comparatorAddress);
        }
        System.out.println("初始化成功!分区表如下:");
        outTable(table, method);

        int option;
        do {
            freeTable.clear();
            for (VariableTableEntry e :
                    table) {

                if ("Null".equals(e.getStatus())) {
                    freeTable.add(e);
                }
            }
            System.out.println("功能:(输入数字进入)");
            System.out.println("1.分配存储  2.回收存储  3.查看分区表信息  4.查看空闲分区表  5.退出");
            option = scanner.nextInt();
            if (option == 1) {
                System.out.println("输入作业信息:(编号 大小)");
                int jobNumber = scanner.nextInt();
                int jobSize = scanner.nextInt();
                Job job = new Job();
                job.setNumber(jobNumber);
                job.setSize(jobSize);
                if (method == 2) {
                    startPosition = alloc(table, job, startPosition);
                    table.sort(comparatorAddress);
                    outTable(table, method);
                } else {
                    alloc(method, table, job);
                    outTable(table, method);
                }
            } else if (option == 2) {
                System.out.println("输入需要回收的作业信息:(编号)");
                int jobNumber = scanner.nextInt();
                Job job = new Job();
                job.setNumber(jobNumber);
                job.setSize(null);
                VariableAllocation.recycleMem(table, job);
                outTable(table, method);
            } else if (option == 3) {
                outTable(table, method);
            } else if (option == 4) {
                outTable(freeTable, method);
            } else if (option == 5) {
                break;
            } else {
                System.out.println("请输入正确的编号:");
                System.out.println("1.分配存储  2.回收存储  3.查看分区表信息  4.查看空闲分区表  5.退出");
            }

        } while (true);


    }

}
