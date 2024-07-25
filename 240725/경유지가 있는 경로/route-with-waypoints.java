import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int M;
    static int K;
    static int tx, ty;
    static int[][] dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        dp = new int[N+1][M+1];

        if(K == 0){
            tx = N;
            ty = M;
        } else{
            tx = K / M;
            ty = K % M;
            if(K % M != 0){
                tx++;
            } else{
                ty = M;
            }
        }
        // System.out.println(tx + ","+ ty);

        dp[0][1] = 1;
        for(int i=1; i<N+1; i++) {
            for(int j=1; j<M+1; j++) {
                if((tx == i && ty < j) || (ty ==j && tx < i)){
                    dp[i][j] = dp[tx][ty];
                    continue;
                }
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        // for(int i=1; i<N+1; i++) {
        //     for(int j=1; j<M+1; j++) {
        //         System.out.print(dp[i][j]+" ");
        //     }
        //     System.out.println();
        // }

        System.out.println(dp[N][M]);

    }
    
}