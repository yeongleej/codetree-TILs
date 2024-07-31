import java.util.*;
import java.io.*;

public class Main {

    static int[][] g;
    static boolean[][] visited;
    static int N;
    static int ans;
    static int tmp;
    static boolean isOk;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        g = new int[100][100];
        N = Integer.parseInt(st.nextToken());
        for(int i=0; i<N; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            g[x-1][y-1] = 1;
        }

        visited = new boolean[100][100];
        ans = 0;
        for(int i=0; i<100; i++) {
            for(int j=0; j<100; j++){
                if(g[i][j] == 0 && !visited[i][j]){
                    visited[i][j] = true;
                    tmp = 0;
                    isOk = false;
                    dfs(i, j);
                    if(isOk){
                        ans += tmp;
                    }
                }
            }
        }

        System.out.println(ans);
    }
    public static boolean isSides(int x, int y){
        return x==0 || x==99 || y==0 || y==99;
    }
    public static boolean isRange(int x, int y){
        return x>=0 && x<100 && y>=0 && y<100;
    }
    public static void dfs(int x, int y){
        if(isSides(x, y)){
            isOk = true;
        }
        // 가장 자리 개수 구하기
        for(int i=0; i<4; i++){
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(isRange(nx, ny) && g[nx][ny] == 1){
                tmp++;
            }
        }
        for(int i=0; i<4; i++){
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(isRange(nx, ny) && g[nx][ny] == 0 && !visited[nx][ny]){
                visited[nx][ny] = true;
                dfs(nx, ny);
            }
        }
    }
}