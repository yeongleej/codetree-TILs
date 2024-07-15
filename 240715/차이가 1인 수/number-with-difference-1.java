import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static long[][] dp;
    final static long mod = 1000000007;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        dp = new long[N+1][10];
        for(int i=1; i<10; i++) {
            dp[1][i] = 1;
        }

        // 자리수
        for(int i=2; i<N+1; i++) {
            // 0~9까지 가장 뒷자리 수
            for(int j=0; j<10; j++) {
                if(j == 0){             // 맨마지막 숫자가 0인경우, 그 앞에는 1만가능
                    dp[i][j] = dp[i-1][1] % mod;
                }
                else if(j == 9) {       // 맨마지막 숫자가 9인경우, 그 앞에는 8만 가능
                    dp[i][j] = dp[i-1][8] % mod;
                } else{                 // 1~8사이의 숫자는, 그 앞에 j-1 && j+1 가능
                    dp[i][j] = (dp[i-1][j-1] + dp[i-1][j+1]) % mod;
                }
            }
        }

        long ans = 0;
        for(int j=0; j<10; j++) {
            ans += dp[N][j];
        }

        System.out.println(ans % mod);
    }
}