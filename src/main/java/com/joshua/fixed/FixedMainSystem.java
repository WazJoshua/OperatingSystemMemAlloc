package com.joshua.fixed;

import com.joshua.Job;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * 固定式分区的主程序
 *
 * @Author:Joshua
 * @Date:2020/12/23
 */
public class FixedMainSystem {

    public static void initTable(List<FixedTableEntry> table) {
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
            FixedTableEntry entry = new FixedTableEntry();
            entry.setSize(size);
            entry.setStartAddress(startAddress);
            entry.setNumber(j);
            entry.setStatus("Null");
            table.add(entry);
        }
    }

    public static void outTable(List<FixedTableEntry> table) {
        System.out.println("***************************当前分区表信息****************************");
        for (FixedTableEntry e :
                table) {
            System.out.println(e);
        }
        System.out.println("******************************************************************");
    }

    public static void main(String[] args) {

        //按分区大小从小到大排序
        Comparator<FixedTableEntry> comparatorSize = new Comparator<FixedTableEntry>() {
            @Override
            public int compare(FixedTableEntry o1, FixedTableEntry o2) {
                return o1.getSize() - o2.getSize(); //将表项按地址高低排列
            }

        };

        Scanner scanner = new Scanner(System.in);

        System.out.println("请先初始化分区表:");
        List<FixedTableEntry> table = new ArrayList<>();
        initTable(table);
        table.sort(comparatorSize);
        System.out.println("初始化成功!分区表如下:");
        outTable(table);
        int option;
        do {
            System.out.println("功能:(输入数字进入)");
            System.out.println("1.分配存储  2.回收存储  3.查看分区表信息  4.退出");
            option = scanner.nextInt();
            if (option == 1) {
                System.out.println("输入作业信息:(编号 大小)");
                int jobNumber = scanner.nextInt();
                int jobSize = scanner.nextInt();
                Job job = new Job();
                job.setNumber(jobNumber);
                job.setSize(jobSize);
                FixedAllocation.allocMem(job,table);
                outTable(table);
            } else if (option == 2) {
                System.out.println("输入需要回收的作业信息:(编号)");
                int jobNumber = scanner.nextInt();
                Job job = new Job();
                job.setNumber(jobNumber);
                job.setSize(null);
                FixedAllocation.recycleMem(job,table);
                outTable(table);
            } else if (option == 3) {
                outTable(table);
            } else if (option == 4) {
                break;
            } else {
                System.out.println("请输入正确的编号:");
                System.out.println("1.分配存储  2.回收存储  3.查看分区表信息  4.退出");
            }
        } while (true);
    }

}
