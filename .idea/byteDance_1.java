import java.util.*;

public class Main {


    public static void main(String[] args) {

        //String s = "decdefgesanbwerhgcytec";
        String s = "bytedanwwwercebytexxxcvdance";
        System.out.println(getByteDance(s));
    }




    public static int getByteDance(String s) {
        Map<Character, Integer> map = new HashMap<>();

        for (char c : "bytedance".toCharArray()) {
            map.put(c, 0);
        }

        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            }
        }

        int res = 0;
        while (true) {
            for (char c : "bytedance".toCharArray()) {

                map.put(c, map.get(c) - 1);
                if (map.get(c) < 0) return res;

            }
            res++;
        }



    }




}

