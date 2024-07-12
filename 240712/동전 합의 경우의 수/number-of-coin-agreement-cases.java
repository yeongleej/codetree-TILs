import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        int[] dp = new int[K+1];
        int[] coins = new int[N+1];
        for(int i=1; i<N+1; i++) {
            st = new StringTokenizer(br.readLine());
            coins[i] = Integer.parseInt(st.nextToken());
        }

        dp[0] = 1;
        for(int i=1; i<N+1; i++) {
            int v = coins[i];
            for(int j=v; j<K+1; j++) {
                dp[j] = dp[j] + dp[j-v];
            }
        }

        System.out.println(dp[K]);
    }
}