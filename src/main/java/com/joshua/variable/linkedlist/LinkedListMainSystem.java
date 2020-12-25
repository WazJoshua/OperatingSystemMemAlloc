package com.joshua.variable.linkedlist;

import com.joshua.Job;
import com.joshua.variable.table.VariableAllocation;


import java.util.*;

/**
 * @Author:Joshua
 * @Date:2020/12/24
 */
public class LinkedListMainSystem {

    public static void initList(LinkedList<AllocNode> list) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;

        do {
            System.out.println("输入链表长度:(不为0)");
            i = scanner.nextInt();
        } while (i == 0);

        for (int j = 0; j < i; j++) {
            System.out.println("输入第" + j + "项的信息:(起始地址 大小)");
            int startAddress = scanner.nextInt();
            int size = scanner.nextInt();
            AllocNode entry = new AllocNode();
            entry.setSize(size);
            entry.setStartAddress(startAddress);
            entry.setStatus("Null");
            list.add(entry);
        }
    }

    public static void outList(LinkedList<AllocNode> list, Integer method) {
        //按起始地址从小到大排序
        Comparator<AllocNode> comparatorAddress = new Comparator<AllocNode>() {
            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }
        };
        //按分区大小从小到大排序
        Comparator<AllocNode> comparatorSize = new Comparator<AllocNode>() {
            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getSize() - o2.getSize(); //将表项按地址高低排列
            }

        };
        if (method == 3) {
            list.sort(comparatorSize);
        } else {
            list.sort(comparatorAddress);
        }

        System.out.println("**********************当前分区链信息*********************");
        for (AllocNode e :
                list) {
            if (list.indexOf(e) != list.size() - 1) {

                /*if ((list.indexOf(e)+1) % 2 == 0) {
                    System.out.println(e + " <==> ");
                }else {*/
                System.out.print(e + "\n              ︿" +
                        "\n              ||              " +
                        "\n              ﹀              ");
                //}

            } else {
                System.out.println(e);
            }
        }
        System.out.println("******************************************************");
    }

    public static void alloc(int method, LinkedList<AllocNode> list, Job job) {
        if (method == 1) {
            LinkedListAllocation.allocMemByFirstFit(list, job);
        } else if (method == 3) {
            LinkedListAllocation.allocMemByBestFit(list, job);
        }

    }

    public static Integer alloc(LinkedList<AllocNode> list, Job job, Integer startPosition) {
        return LinkedListAllocation.allocMemByNextFit(list, job, startPosition);
    }

    public static void main(String[] args) {
        //按起始地址从小到大排序
        Comparator<AllocNode> comparatorAddress = new Comparator<AllocNode>() {
            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }
        };
        //按分区大小从小到大排序
        Comparator<AllocNode> comparatorSize = new Comparator<AllocNode>() {
            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getSize() - o2.getSize(); //将表项按地址高低排列
            }
        };
        Scanner scanner = new Scanner(System.in);
        Integer startPosition = 0;
        LinkedList<AllocNode> list = new LinkedList<>();
        LinkedList<AllocNode> freeTable = new LinkedList<>();
        System.out.println("请先初始化分区链信息:");
        initList(list);
        System.out.println("选择分配算法:(输入编号)");
        System.out.println("1.FirstFit  2.NextFit  3.BestFit");
        int method = scanner.nextInt();
        if (method == 3) {
            //如果为BestFit,就按分区大小从小到大排序
            list.sort(comparatorSize);
        } else if (method == 2 || method == 1) {
            //其他两种都是按起始地址从小到大排序
            list.sort(comparatorAddress);
        }
        System.out.println("初始化成功!分区链如下:");
        outList(list, method);
        int option;
        do {
            freeTable.clear();
            for (AllocNode e :
                    list) {
                if ("Null".equals(e.getStatus())) {
                    freeTable.add(e);
                }
            }
            System.out.println("功能:(输入数字进入)");
            System.out.println("1.分配存储  2.回收存储  3.查看分区链信息  4.查看空闲分区链  5.退出");
            option = scanner.nextInt();
            if (option == 1) {
                System.out.println("输入作业信息:(编号 大小)");
                int jobNumber = scanner.nextInt();
                int jobSize = scanner.nextInt();
                Job job = new Job();
                job.setNumber(jobNumber);
                job.setSize(jobSize);
                if (method == 2) {
                    startPosition = alloc(list, job, startPosition);
                    list.sort(comparatorAddress);
                    outList(list, method);
                } else {
                    alloc(method, list, job);
                    outList(list, method);
                }
            } else if (option == 2) {
                System.out.println("输入需要回收的作业信息:(编号)");
                int jobNumber = scanner.nextInt();
                Job job = new Job();
                job.setNumber(jobNumber);
                job.setSize(null);
                LinkedListAllocation.recycleMem(list, job);
                outList(list, method);
            } else if (option == 3) {
                outList(list, method);
            } else if (option == 4) {
                outList(freeTable, method);
            } else if (option == 5) {
                break;
            } else {
                System.out.println("请输入正确的编号:");
                System.out.println("1.分配存储  2.回收存储  3.查看分区链信息  4.查看空闲分区链  5.退出");
            }
        } while (true);
    }

}
