import java.util.*;
import java.io.*;

public class Main {
    
    static int N;
    static int K;
    static int[] coins;
    static int[] dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        coins = new int[N+1];
        for(int i=1; i<N+1; i++) {
            st = new StringTokenizer(br.readLine());
            coins[i] = Integer.parseInt(st.nextToken());
        }
        dp = new int[K+1];
        for(int i=1; i<K+1; i++) {
            dp[i] = 10001;
        }

        int ans = Integer.MAX_VALUE;
        for(int i = 1; i<N+1; i++) {
            int v = coins[i];
            for(int j=v; j<K+1; j++) {
                dp[j] = Math.min(dp[j], dp[j-v]+1);
            }
            // System.out.println(v+": "+Arrays.toString(dp));
        }
        System.out.println(dp[K]);

    }
}