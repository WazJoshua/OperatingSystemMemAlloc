import com.joshua.Job;
import com.joshua.fixed.FixedAllocation;
import com.joshua.fixed.FixedTableEntry;
import org.junit.Test;

import java.util.*;

/**
 * @Author:Joshua
 * @Date:2020/12/23
 */
public class FixedAllocTest {


    /*Comparator<FixedTableEntry> comparator = new Comparator<FixedTableEntry>() {

            @Override
            public int compare(FixedTableEntry o1, FixedTableEntry o2) {
                return o1.getSize() - o2.getSize(); //将表项按大小排列
            }
        };
        Collections.sort(tables, comparator);*/

    Comparator<FixedTableEntry> comparator = new Comparator<FixedTableEntry>() {

        @Override
        public int compare(FixedTableEntry o1, FixedTableEntry o2) {
            return o1.getSize() - o2.getSize(); //将表项按大小排列
        }
    };


    @Test
    @Deprecated
    public void testCompare() {
        Job job = null;
        List<FixedTableEntry> table = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            FixedTableEntry entry = new FixedTableEntry();
            entry.setNumber(i + 1);
            entry.setSize(random.nextInt(100));
            entry.setStartAddress(111);
            entry.setStatus("null");
            System.out.println("entry = " + entry);
            table.add(entry);
        }

        FixedAllocation.allocMem(job, table);
    }

    @Test
    public void testFixed() {


        List<FixedTableEntry> table = new ArrayList<>();
        Random random = new Random();
    /*    for (int i = 0; i < 10; i++) {
            FixedTableEntry entry = new FixedTableEntry();
            entry.setSize(random.nextInt(100));
            entry.setStartAddress(111);
            entry.setStatus("null");
            System.out.println("entry = " + entry);
            table.add(entry);
        }
        table.sort(comparator);

        for (int i = 0; i < table.size(); i++) {

        }*/
        FixedTableEntry entry1 = new FixedTableEntry();
        FixedTableEntry entry2 = new FixedTableEntry();
        FixedTableEntry entry3 = new FixedTableEntry();
        FixedTableEntry entry4 = new FixedTableEntry();

        entry1.setNumber(1);
        entry1.setStartAddress(20);
        entry1.setStatus("null");
        entry1.setSize(12);
        entry2.setNumber(2);
        entry2.setStartAddress(32);
        entry2.setStatus("null");
        entry2.setSize(32);
        entry3.setNumber(3);
        entry3.setStartAddress(64);
        entry3.setStatus("null");
        entry3.setSize(64);
        entry4.setNumber(4);
        entry4.setStartAddress(128);
        entry4.setStatus("null");
        entry4.setSize(128);

        table.add(entry1);
        table.add(entry2);
        table.add(entry3);
        table.add(entry4);

        Job job = new Job();
        job.setNumber(1);
        job.setSize(30);


        FixedAllocation.allocMem(job, table);
        System.out.println("table = " + table);


        System.out.println("回收内存...");

        FixedAllocation.recycleMem(job,table);
        System.out.println("table = " + table);
    }
}
