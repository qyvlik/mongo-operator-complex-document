package space.qyvlik.mongo.utils;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by qyvlik on 2017/6/8.
 */
public class Collections3 {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    // 分组
    public static <T> List<List<T>> group(List<T> list, Comparator<T> comparator) {
        List<List<T>> groupList = Lists.newArrayList();
        if (isEmpty(list)) {
            return groupList;
        }
        list.removeAll(Collections.singleton(null));
        while (!list.isEmpty()) {
            List<T> group = Lists.newArrayList();
            for (T item : list) {
                if (item == null) {
                    continue;
                }
                if (group.isEmpty()) {
                    group.add(item);
                } else {
                    if (comparator.compare(group.get(0), item) == 0) {
                        group.add(item);
                    }
                }
            }
            list.removeAll(group);
            if(Collections3.isNotEmpty(group)) {
                groupList.add(group);
            }
        }
        return groupList;
    }

    public static <T> List<List<T>> split(List<T> list, int step) {
        List<List<T>> splitList = Lists.newLinkedList();

        if (isEmpty(list)) {
            return splitList;
        }

        int listSize = list.size();

        if (listSize <= step) {
            splitList.add(list);
            return splitList;
        }

        int start = 0;
        int end = step;

        while(end <= listSize) {

            List<T> item = Lists.newLinkedList();

            while( start < end) {
                // O(1) or O(n)
                item.add(list.get(start));
                start++;
            }

            if (isNotEmpty(item)) {
                splitList.add(item);
            }

            if (end == listSize) {
                break;
            }

            end += step;
            if (end > listSize) {
                end = listSize;
            }
        }

        return splitList;
    }

    // such as : `[0, 1], [2, 3], [4, 4]`
    public static List<List<Long>> splitRangeToList(long start, long end, int size) {
        List<List<Long>> itemList = Lists.newLinkedList();
        if (start < 0 || end < 0 || start >= end) {
            return Lists.newArrayList();
        }

        int indexGap = size - 1;

        if (end - start < size) {
            itemList.add(Lists.newArrayList(start, end));
            return itemList;
        }

        long startIndex = start;
        long endIndex = start + indexGap;

        while (endIndex < end) {
            itemList.add(Lists.newArrayList(startIndex, endIndex));

            startIndex = endIndex + 1;

            if (startIndex > end) {
                itemList.add(Lists.newArrayList(end, end));
                break;
            }

            endIndex = startIndex + indexGap;

            if (endIndex > end) {
                itemList.add(Lists.newArrayList(startIndex, end));
                break;
            }
        }
        return itemList;
    }

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList();
        int it = 10;
        while(it-->0) {
            list.add(it);
        }
        System.out.println("fill finished");

        List<List<Integer>> group = split(list, 3);
        System.out.println("group size:" + group.size());
        System.out.println("group : " + group);
    }

}
