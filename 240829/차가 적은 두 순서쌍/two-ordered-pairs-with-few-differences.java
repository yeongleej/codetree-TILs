import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int[] arr;
    static List<Integer> aList;
    static List<Integer> bList;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        arr = new int[N];

        st = new StringTokenizer(br.readLine());
        for(int i=0; i<N; i++){
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        List<Integer> sums = new ArrayList<>();

        // Generate all possible sums of pairs
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                sums.add(arr[i] + arr[j]);
            }
        }

        // Sort the sums to use two pointers technique
        Collections.sort(sums);
        // System.out.println(sums);

        int minDifference = Integer.MAX_VALUE;
        int len = sums.size();

        // Compare every pair of sums
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                int diff = Math.abs(sums.get(i) - sums.get(j));
                minDifference = Math.min(minDifference, diff);
            }
        }

        System.out.println(minDifference);

    }

}