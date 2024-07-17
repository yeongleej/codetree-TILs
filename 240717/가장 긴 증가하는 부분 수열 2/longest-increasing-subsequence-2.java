import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int[] arr;
    static List<Integer> dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        arr = new int[N];
        st = new StringTokenizer(br.readLine());
        for(int i=0; i<N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // System.out.println(Arrays.toString(arr));
        dp = new ArrayList<>();
        dp.add(arr[0]);
        for(int i=1; i<N; i++) {
            if(arr[i] > dp.get(dp.size()-1)) {
                dp.add(arr[i]);
            }else{
                int idx = bSearch(arr[i]);
                dp.set(idx, arr[i]);
            }
        }
        
        // System.out.println();
        System.out.println(dp.size());

    }
    public static int bSearch(int num) {
        int s = 0;
        int e = dp.size();

        while(s < e) {
            int mid = (s + e) / 2;
            if(dp.get(mid) >= num) {
                e = mid;
            } else{
                s = mid + 1;
            }
        }
        // System.out.println(dp);
        // System.out.println(num+": "+e+" ");
        // System.out.println(num+": "+ans+" ");
        return s;
    }
}