import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int[] dp = new int[N+1];
        int[] t = new int[N+1];
        int[] p = new int[N+1];

        for(int i=1; i<N+1; i++){
            st = new StringTokenizer(br.readLine());
            t[i] = Integer.parseInt(st.nextToken());
            p[i] = Integer.parseInt(st.nextToken());
        }

        for(int i=1; i<N+1; i++){
            dp[i] = Math.max(dp[i-1], dp[i]);
            // 현재 작업을 할 수 있는 경우
            if (i + t[i] - 1 <= N) {
                dp[i + t[i] - 1] = Math.max(dp[i + t[i] - 1], dp[i - 1] + p[i]);
            }
        }
        // System.out.println(Arrays.toString(dp));
        System.out.println(dp[N]);

    }
}