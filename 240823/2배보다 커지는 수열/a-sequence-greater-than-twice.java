import java.util.*;
import java.io.*;

public class Main {
    static int MOD = 1000000007;
    static int N; 
    static int M;
    static int[][] dp;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        dp = new int[N+1][M+1];

        for(int j=1; j<M+1; j++){
            dp[1][j] = 1;
        }

        for(int i=2; i<N+1; i++){
            for(int j=1; j<M+1; j++){
                for(int k=1; k<=j/2; k++){
                    dp[i][j] += (dp[i-1][k] % MOD);
                }
            }
        }
        int ans = 0;
        for(int j=1; j<M+1; j++){
            ans += (dp[N][j] % MOD);
        }
        System.out.println(ans);
    }
}