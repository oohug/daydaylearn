package com.hug.ali.test3;

import java.util.*;

public class ReaderUtil {

    public static TreeSet<MyData> extract(final String rawDate) {
        // parse the file and extract items.
        TreeSet<MyData> myDataSet = new TreeSet<>();
        List<Integer> indexList = getAllIndex(rawDate, ".");
        Collections.sort(indexList, Collections.reverseOrder());
        StringBuffer sb = new StringBuffer(rawDate);
        indexList.forEach(i -> sb.insert(i + 2, ";"));
        String result = new String(sb);
        String dataSet[] = result.split(";");
        Arrays.stream(dataSet).map(temp -> temp.split(","))
                .forEach(data -> myDataSet.add(new MyData(data[0], data[1], Float.parseFloat(data[2]))));
        return myDataSet;
    }

    private static ArrayList<Integer> getAllIndex(String s, String find) {
        ArrayList list = new ArrayList();
        int i = 0;
        do {
            i = s.indexOf(find, i + 1);
            if (i >= 0) list.add(i);
        } while (i > 0);
        return list;
    }
}
