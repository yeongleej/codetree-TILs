import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int Q;
    static int[][] g;
    
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        g = new int[N+1][N+1];
        for(int i=1; i<N+1; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=1; j<N+1; j++){
                g[i][j] = Integer.parseInt(st.nextToken());
            }
            // System.out.println(Arrays.toString(g[i]));
        }

        // 누적합 배열 구하기
        // 1. 행 기준
        for(int i = 1; i<N+1; i++){
            for(int j = 1; j<N+1; j++){
                g[i][j] += g[i][j-1];
            }
        }
        // 2. 열 기준
        for(int j=1; j<N+1; j++) {
            for(int i=1; i<N+1; i++) {
                g[i][j] += g[i-1][j];
            }
        }

        // 누적합 배열 출력
        // for(int i=0; i<N+1; i++) {
        //     System.out.println(Arrays.toString(g[i]));
        // }

        for(int q=0; q<Q; q++) {
            st = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());

            // System.out.println(x1 + " "+y1+" "+x2+" "+y2);

            int ans = g[x2][y2] - (g[x1-1][y2]+g[x2][y1-1]) + g[x1-1][y1-1];
            System.out.println(ans);

        }
    }
}