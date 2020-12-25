package com.joshua.variable.linkedlist;

import lombok.Data;

import java.util.LinkedList;

/**
 * @Author:Joshua
 * @Date:2020/12/24
 */
@Data
public class AllocNode {
    private Integer size;

    private Integer startAddress;

    private String status;

    private Integer jobNumber;

    @Override
    public String toString() {
        String s = String.format("\n -----------AllocNode---------\n" +
                " | startAddress |% -12d|\n" +
                " |     size     |% -12d|\n" +
                " |    status    |%-12s|\n" +
                " |   jobNumber  |% -12d|\n" +
                " -----------------------------", startAddress, size, status, jobNumber);
        return s;

    }
}
