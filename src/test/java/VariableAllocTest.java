import com.joshua.Job;
import com.joshua.variable.table.VariableAllocation;
import com.joshua.variable.table.VariableTableEntry;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Joshua
 * @Date:2020/12/23
 */
public class VariableAllocTest {


    @Test
    public void FirstFitTest() {
        List<VariableTableEntry> table = new ArrayList<>();
        VariableTableEntry entry1 = new VariableTableEntry();
        VariableTableEntry entry2 = new VariableTableEntry();
        VariableTableEntry entry3 = new VariableTableEntry();
        VariableTableEntry entry4 = new VariableTableEntry();

        entry1.setNumber(1);
        entry1.setStartAddress(20);
        entry1.setStatus("Null");
        entry1.setSize(12);
        entry2.setNumber(2);
        entry2.setStartAddress(32);
        entry2.setStatus("Null");
        entry2.setSize(32);
        entry3.setNumber(3);
        entry3.setStartAddress(64);
        entry3.setStatus("Null");
        entry3.setSize(64);
        entry4.setNumber(4);
        entry4.setStartAddress(128);
        entry4.setStatus("Null");
        entry4.setSize(128);

        table.add(entry2);
        table.add(entry1);
        table.add(entry3);
        table.add(entry4);

        System.out.println("table = " + table);

        Job job = new Job();
        job.setSize(12);
        job.setNumber(1);

        VariableAllocation.allocMemByFirstFit(table, job);

        System.out.println("table = " + table);

        Job job2 = new Job();
        job2.setSize(13);
        job2.setNumber(2);
        VariableAllocation.allocMemByFirstFit(table, job2);

        System.out.println("table = " + table);
    }

    @Test
    public void NextFitTest() {
        List<VariableTableEntry> table = new ArrayList<>();
        VariableTableEntry entry1 = new VariableTableEntry();
        VariableTableEntry entry2 = new VariableTableEntry();
        VariableTableEntry entry3 = new VariableTableEntry();
        VariableTableEntry entry4 = new VariableTableEntry();

        entry1.setNumber(1);
        entry1.setStartAddress(20);
        entry1.setStatus("Null");
        entry1.setSize(12);
        entry2.setNumber(2);
        entry2.setStartAddress(32);
        entry2.setStatus("Null");
        entry2.setSize(32);
        entry3.setNumber(3);
        entry3.setStartAddress(64);
        entry3.setStatus("Null");
        entry3.setSize(64);
        entry4.setNumber(4);
        entry4.setStartAddress(128);
        entry4.setStatus("Null");
        entry4.setSize(128);

        table.add(entry2);
        table.add(entry1);
        table.add(entry3);
        table.add(entry4);

        System.out.println("table = " + table);

        Job job = new Job();
        job.setSize(13);
        job.setNumber(1);

        int startPosition = 0;
        startPosition = VariableAllocation.allocMemByNextFit(table, job, startPosition);

        System.out.println("table = " + table);
        System.out.println("startPosition = " + startPosition);
        Job job2 = new Job();
        job2.setSize(130);
        job2.setNumber(2);
        startPosition = VariableAllocation.allocMemByNextFit(table, job2, startPosition);
        System.out.println("table = " + table);
        System.out.println("startPosition = " + startPosition);
    }


    @Test
    public void BestFitTest() {
        List<VariableTableEntry> table = new ArrayList<>();
        VariableTableEntry entry1 = new VariableTableEntry();
        VariableTableEntry entry2 = new VariableTableEntry();
        VariableTableEntry entry3 = new VariableTableEntry();
        VariableTableEntry entry4 = new VariableTableEntry();

        entry1.setNumber(1);
        entry1.setStartAddress(20);
        entry1.setStatus("Null");
        entry1.setSize(12);
        entry2.setNumber(2);
        entry2.setStartAddress(32);
        entry2.setStatus("Null");
        entry2.setSize(32);
        entry3.setNumber(3);
        entry3.setStartAddress(64);
        entry3.setStatus("Null");
        entry3.setSize(64);
        entry4.setNumber(4);
        entry4.setStartAddress(128);
        entry4.setStatus("Null");
        entry4.setSize(128);

        table.add(entry2);
        table.add(entry1);
        table.add(entry3);
        table.add(entry4);

        System.out.println("table = " + table);

        Job job = new Job();
        job.setSize(130);
        job.setNumber(1);

        VariableAllocation.allocMemByBestFit(table, job);

        System.out.println("table = " + table);
    }

    @Test
    public void recycleTest() {
        List<VariableTableEntry> table = new ArrayList<>();
        VariableTableEntry entry1 = new VariableTableEntry();
        VariableTableEntry entry2 = new VariableTableEntry();
        VariableTableEntry entry3 = new VariableTableEntry();
        VariableTableEntry entry4 = new VariableTableEntry();

        entry1.setNumber(1);
        entry1.setStartAddress(20);
        entry1.setStatus("Null");
        entry1.setSize(12);
        entry2.setNumber(2);
        entry2.setStartAddress(32);
        entry2.setStatus("Null");
        entry2.setSize(32);
        entry3.setNumber(3);
        entry3.setStartAddress(64);
        entry3.setStatus("Null");
        entry3.setSize(64);
        entry4.setNumber(4);
        entry4.setStartAddress(128);
        entry4.setStatus("Null");
        entry4.setSize(128);

        table.add(entry2);
        table.add(entry1);
        table.add(entry3);
        table.add(entry4);

        System.out.println("table = " + table);

        Job job = new Job();
        job.setSize(12);
        job.setNumber(1);

        VariableAllocation.allocMemByFirstFit(table, job);

        System.out.println("table = " + table);

        Job job2 = new Job();
        job2.setSize(13);
        job2.setNumber(2);
        VariableAllocation.allocMemByFirstFit(table, job2);
        System.out.println("table = " + table);

        VariableAllocation.recycleMem(table, job2);

        System.out.println("after recycle table = " + table);
    }
}
