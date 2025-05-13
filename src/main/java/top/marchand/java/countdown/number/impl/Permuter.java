package top.marchand.java.countdown.number.impl;

import java.util.ArrayList;
import java.util.List;

class Permuter {
  public static List<List<Integer>> permute(int[] arr) {
    List<List<Integer>> res = new ArrayList<>();
    permutations(res, arr, 0);
    return res;
  }

  private static void permutations(List<List<Integer>> res, int[] arr, int idx) {
    if (idx == arr.length) {
      res.add(convertArrayToList(arr));
      return;
    }
    for (int i = idx; i < arr.length; i++) {
      swap(arr, idx, i);
      permutations(res, arr, idx + 1);
      swap(arr, idx, i);
    }
  }

  private static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  private static List<Integer> convertArrayToList(int[] arr) {
    List<Integer> list = new ArrayList<>();
    for (int num : arr) {
      list.add(num);
    }
    return list;
  }
}
