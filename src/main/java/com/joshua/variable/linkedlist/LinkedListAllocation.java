package com.joshua.variable.linkedlist;

import com.joshua.Job;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:Joshua
 * @Date:2020/12/24
 */
public class LinkedListAllocation {

    static Integer minimumSize = 8;

    /**
     * 动态分配
     *
     * @param list 要操作的分区表
     * @param job   程序
     * @param node 要操作的表项
     */
    private static void dynamicAlloc(LinkedList<AllocNode> list, Job job, AllocNode node) {

        Comparator<AllocNode> comparator = new Comparator<AllocNode>() {

            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将链表按地址高低排列
            }

        };

        ///划分程序所在的分区
        AllocNode jobNode = new AllocNode();
        jobNode.setSize(job.getSize());
        jobNode.setStartAddress(node.getStartAddress());
        jobNode.setStatus("allocated");
        jobNode.setJobNumber(job.getNumber());

        list.set(list.indexOf(node), jobNode);


        AllocNode newNode = new AllocNode();
        newNode.setSize(node.getSize() - job.getSize());
        newNode.setStartAddress(node.getStartAddress() + job.getSize());
        newNode.setStatus("Null");

        list.add(list.indexOf(jobNode) + 1, newNode);

        System.out.println("将程序所在的分区设为链表的第" + list.indexOf(jobNode) + "项");

        //将分区按地址排序
        list.sort(comparator);

    }


    /**
     * 通过首次适应算法进行分配
     *
     * @param list 分区表
     * @param job   程序
     */
    public static void allocMemByFirstFit(LinkedList<AllocNode> list, Job job) {

        Comparator<AllocNode> comparator = new Comparator<AllocNode>() {

            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }

        };

        list.sort(comparator);
        int flag = 0;
        for (AllocNode node : list) {
            if (node.getSize() >= job.getSize() && "Null".equals(node.getStatus())) {
                if (node.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    node.setStatus("allocated");
                    node.setJobNumber(job.getNumber());
                    flag = 1;
                    System.out.println("因为程序大小适合,将第" + list.indexOf(node) + "项分区全部分配");
                    break;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区链
                    dynamicAlloc(list, job, node);
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
     * @param list 分区表
     * @param job   程序
     * @return index 返回当前检测到哪一项了
     */
    public static Integer allocMemByNextFit(LinkedList<AllocNode> list, Job job, Integer startPosition) {
        Comparator<AllocNode> comparator = new Comparator<AllocNode>() {

            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getStartAddress() - o2.getStartAddress();
            }

        };
        //将表项按地址高低排列
        list.sort(comparator);
        int flag = 0;
        for (int i = startPosition; i < list.size(); i++) {
            //从上次的开始check
            AllocNode node = list.get(i);
            if (node.getSize() >= job.getSize() && "Null".equals(node.getStatus())) {

                if (node.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    node.setStatus("allocated");
                    node.setJobNumber(job.getNumber());
                    System.out.println("因为程序大小适合,将第" + list.indexOf(node) + "号分区全部分配");
                    return i + 1;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区表
                    dynamicAlloc(list, job, node);
                    return i + 1;
                }

            }


        }
        //如果到最后了还没找到合适的分区,则从头开始找
        for (int i = 0; i < startPosition; i++) {
            AllocNode node = list.get(i);
            if (node.getSize() >= job.getSize() && "Null".equals(node.getStatus())) {
                if (node.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    node.setStatus("allocated");
                    node.setJobNumber(job.getNumber());
                    System.out.println("因为程序大小适合,将第" + list.indexOf(node) + "号分区全部分配");
                    return i + 1;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区表
                    dynamicAlloc(list, job, node);
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
     * @param list 分区表
     * @param job   程序
     */
    public static void allocMemByBestFit(LinkedList<AllocNode> list, Job job) {
        Comparator<AllocNode> comparator = new Comparator<AllocNode>() {
            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getSize() - o2.getSize(); //将表项按大小排列
            }

        };

        list.sort(comparator);     //将分区表按分区大小排列

        int flag = 0;
        for (AllocNode node : list) {
            if (node.getSize() >= job.getSize() && node.getStatus().equals("Null")) {
                if (node.getSize() - job.getSize() <= minimumSize) {
                    //如果分区的大小减去程序的大小 <= 设定的值,则直接将整个分区分配出去
                    node.setStatus("allocated");
                    node.setJobNumber(job.getNumber());
                    flag = 1;
                    System.out.println("因为程序大小适合,将第" + list.indexOf(node) + "号分区全部分配");
                    break;
                } else {
                    //否则,将分区划分为程序的大小,然后分配给程序,同时修改分区表
                    dynamicAlloc(list, job, node);
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
     * @param list 分区表
     * @param job   需要回收的程序
     */
    public static void recycleMem(LinkedList<AllocNode> list, Job job) {

        Comparator<AllocNode> comparator = new Comparator<AllocNode>() {

            @Override
            public int compare(AllocNode o1, AllocNode o2) {
                return o1.getStartAddress() - o2.getStartAddress(); //将表项按地址高低排列
            }

        };
        list.sort(comparator);

        for (int i = 0; i < list.size(); i++) {
            //找到需要回收的分区
            AllocNode node = list.get(i);
            //判断待回收分区的前后分区状态
            if (node.getJobNumber() != null && node.getJobNumber().equals(job.getNumber())) {

                //System.out.println("进入了判断分区");

                //判断是否为第一和最后一项
                if (i != 0 && i != list.size() - 1) {
                    //前后都为空,合并
                    if (list.get(i + 1).getStatus().equals("Null") && list.get(i - 1).getStatus().equals("Null")) {
                        System.out.println("前后都空,合并");
                        AllocNode before = list.get(i - 1);
                        AllocNode behind = list.get(i + 1);
                        before.setSize(node.getSize() + behind.getSize() + before.getSize());      //大小为三个分区合并大小
                        //删除后面俩分区
                        list.remove(i);
                        list.remove(i);            //LinkedList,移除一个后,后一个的index就变为原来这个的index 所以再次remove(i)
                        System.out.println("将第" + i + " ,第" + (i - 1) + " ,第" + (i + 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else if (list.get(i - 1).getStatus().equals("Null")) {
                        System.out.println("前空,合并");
                        AllocNode before = list.get(i - 1);
                        before.setSize(node.getSize() + before.getSize());         //大小合并
                        //删除回收项
                        list.remove(i);
                        System.out.println("将第" + i + " ,第" + (i - 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    } else if (list.get(i + 1).getStatus().equals("Null")) {
                        //若为空,则合并
                        System.out.println("后空,合并");
                        AllocNode behind = list.get(i + 1);
                        node.setSize(node.getSize() + behind.getSize());        //合并大小
                        node.setStatus("Null");
                        //删除后一项
                        list.remove(i + 1);
                        System.out.println("将第" + i + " ,第" + (i + 1) + "号分区合并为一个分区" + i);
                        break;
                    } else {
                        //前后都不空,则回收当前即可
                        System.out.println("前后都不空,只回收此项");
                        node.setStatus("Null");
                        node.setJobNumber(null);
                    }
                } else if (i != 0) {
                    //若不是第一项,则判断前面一项是否为空
                    System.out.println("前空,合并");
                    if (list.get(i - 1).getStatus().equals("Null")) {
                        AllocNode before = list.get(i - 1);
                        before.setSize(node.getSize() + before.getSize());         //大小合并
                        //删除回收项
                        list.remove(i);
                        System.out.println("将第" + i + " ,第" + (i - 1) + "号分区合并为一个分区" + (i - 1));
                        break;
                    }else {
                        //前后都不空,则回收当前即可
                        System.out.println("前后都不空,只回收此项");
                        node.setStatus("Null");
                        node.setJobNumber(null);
                    }
                } else if (i != list.size() - 1) {
                    //不是最后一项,则判断后一项是否为空
                    if (list.get(i + 1).getStatus().equals("Null")) {
                        System.out.println("后空,合并");
                        //若为空,则合并
                        AllocNode behind = list.get(i + 1);
                        node.setSize(node.getSize() + behind.getSize());  //合并大小
                        node.setStatus("Null");
                        //删除后一项
                        list.remove(i + 1);
                        System.out.println("将第" + i + " ,第" + (i + 1) + "号分区合并为一个分区" + i);
                        break;
                    }else {
                        //前后都不空,则回收当前即可
                        System.out.println("前后都不空,只回收此项");
                        node.setStatus("Null");
                        node.setJobNumber(null);
                    }
                }
            }
        }
    }

}
