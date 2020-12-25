package com.joshua.variable.table;

import lombok.Data;

/**
 * @Author:Joshua
 * @Date:2020/12/23
 */
@Data
public class VariableTableEntry {

    private Integer number;

    private Integer size;

    private Integer startAddress;

    private String status;

    private Integer jobNumber;

    @Override
    public String toString() {
        return "分区表项{" +
                "编号=" + number +
                ", 起始地址=" + startAddress +
                ", 大小=" + size +
                ", 状态='" + status + '\'' +
                ", 分配程序编号=" + jobNumber +
                '}';
    }
}
