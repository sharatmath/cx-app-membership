package com.dev.prepaid;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCollection {

    @Test
    public void givenList_whenParitioningIntoNSublists_thenCorrect() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        List<List<Integer>> subSets = Lists.partition(intList, 3);

        List<Integer> lastPartition = subSets.get(2);
        List<Integer> expectedLastPartition = Lists.<Integer> newArrayList(7, 8);
        System.out.println(lastPartition);
        System.out.println(expectedLastPartition);

//        assertThat(subSets.size(), equalTo(3));
//        assertThat(lastPartition, equalTo(expectedLastPartition));
    }

    @Test
    public void givenList_whenParitioningIntoNSublists_thenCorrect2() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println(intList.subList(0, 3));

//        assertThat(subSets.size(), equalTo(3));
//        assertThat(lastPartition, equalTo(expectedLastPartition));
    }
}
