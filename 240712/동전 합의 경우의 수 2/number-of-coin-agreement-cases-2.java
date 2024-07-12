import java.util.*;
import java.io.*;

public class Main {
    
    static int N;
    static int K;
    static List<Integer> coins;
    static int[][] dp;
    static int MAXV = 10001;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        coins = new ArrayList<>();
        coins.add(0);
        for(int i=1; i<N+1; i++) {
            coins.add(Integer.parseInt(br.readLine()));
        }
        Collections.sort(coins);

        // 초기화
        dp = new int[N+1][K+1];
        for(int i=1; i<N+1; i++) {
            for(int j=0; j<K+1; j++) {
                if(j % coins.get(i) == 0){
                    dp[i][j] = j / coins.get(i);
                } 
            }
        }


        for(int i=1; i<N+1; i++) {
            int v = coins.get(i);
            for(int j=v+1; j<K+1; j++) {
                if(dp[i-1][j-v] != 0 && dp[i][j] != 0) {
                    dp[i][j] = Math.min(dp[i][j], dp[i-1][j-v]+1);
                }
            }
        }


        // for(int i=1; i<N+1; i++) {
        //     System.out.println(Arrays.toString(dp[i]));
        // }
        System.out.println(dp[N][K]);

    }
}