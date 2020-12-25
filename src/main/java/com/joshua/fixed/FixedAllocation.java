package com.joshua.fixed;

import com.joshua.Job;

import java.util.*;

/**
 * @Author:Joshua
 * @Date:2020/12/23
 */
public class FixedAllocation {

    /**
     * 分配空间
     *
     * @param job
     * @param tables
     */
    public static void allocMem(Job job, List<FixedTableEntry> tables) {

        int flag = 0;
        for (FixedTableEntry tableEntry : tables) {
            //遍历分区表
            if (tableEntry.getSize() >= job.getSize() && (tableEntry.getStatus().equals("Null"))) {
                //如果分区的大小 >= 作业的大小,且分区未被分配,则将其分配给作业
                tableEntry.setJobNumber(job.getNumber());
                tableEntry.setStatus("allocated");
                flag = 1;
                System.out.println("第" + tableEntry.getNumber() + "号分区分配给了程序");
                break;
            }
        }
        if (flag == 0) {
            System.out.println("未找到合适的分区,分配失败.");
        }
    }


    /**
     * 回收空间
     *
     * @param job   回收的程序
     * @param table 分区表
     */
    public static void recycleMem(Job job, List<FixedTableEntry> table) {
        for (FixedTableEntry entry : table) {
            if (entry.getJobNumber() != null && entry.getJobNumber().equals(job.getNumber())) {
                entry.setStatus("Null");
                entry.setJobNumber(null);
                System.out.println("第" + entry.getNumber() + "号分区已回收");
            }
        }

    }
}
