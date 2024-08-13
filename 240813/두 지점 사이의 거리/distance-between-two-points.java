import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int N = Integer.parseInt(br.readLine());
        int[] dp = new int[N+1];
        int total = 0;
        for(int i=2; i<N+1; i++){
            int num = Integer.parseInt(br.readLine());
            dp[i] += (dp[i-1]+num);
            total += num;
        }
        int last = Integer.parseInt(br.readLine());
        total += last;
        // System.out.println(Arrays.toString(dp));
        // System.out.println("total: "+total);

        // int ans = Integer.MIN_VALUE;
        // int s = 1;
        // int e = s+1;
        // while(s < N){
        //     System.out.println("s:"+s+", e:"+e);
        //     int c1 = dp[e] - dp[s];
        //     int c2 = total - c1;
        //     while(c1 <= c2 && e <= N){
        //         System.out.println("in while) s:"+s+", e:"+e);
        //         if(s==1 && e==N){
        //             c1 = last;
        //         } 
        //         ans = Math.max(ans, c1);
        //         e++;
        //     }
        //     System.out.println("ans: "+ans);
        //     s++;
        //     e = s+1;
        // }
        
        int ans = last;
        for(int i=1; i<N; i++){
            for(int j=i+1; j<N+1; j++){
                ans = Math.max(ans, Math.min(dp[j]-dp[i], total-(dp[j]-dp[i])));
            }
        }
        System.out.println(ans);
    }
}