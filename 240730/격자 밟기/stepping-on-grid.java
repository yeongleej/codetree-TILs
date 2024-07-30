import java.util.*;
import java.io.*;

public class Main {

    static int[][] g;
    static int[][] visited;
    // static int target;
    static int ans;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        g = new int[5][5];
        visited = new int[5][5];
        ans = 0;
        int K = Integer.parseInt(st.nextToken());
        // target = (23-K) / 2 +1;
        for(int i=0; i<K; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            g[x-1][y-1] = 1;
            visited[x-1][y-1] = -1;
        }
        
        // for(int i=0; i<5; i++){
        //     System.out.println(Arrays.toString(g[i]));
        // }

        visited[0][0] = 1;
        visited[4][4] = 2;

        dfs(0,0,4,4);

        System.out.println(ans);
    }
    public static boolean isRange(int x, int y){
        return x>=0 && x<5 && y>=0 && y<5;
    }
    public static boolean isCover() {
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(visited[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }
    public static void dfs(int ax, int ay, int bx, int by){
        if(ax == bx && ay == by){
            if(isCover()){
                ans++;
                // System.out.println(ax +", "+ ay);
                // for(int i=0; i<5; i++){
                //     System.out.println(Arrays.toString(visited[i]));
                // }
                // System.out.println();
            }
            return;
        }
        for(int i=0; i<4; i++){
            int nx = ax + dx[i];
            int ny = ay + dy[i];
            if(isRange(nx, ny) && visited[nx][ny] == 0 && g[nx][ny] == 0){
                for(int j=0; j<4; j++){
                    int mx = bx + dx[j];
                    int my = by + dy[j];
                    if(isRange(mx, my) && visited[mx][my]==0 && g[mx][my] == 0){
                        // System.out.println(nx+", "+ny+" | "+mx+", "+my);
                        visited[nx][ny] = 1;
                        visited[mx][my] = 2;
                        dfs(nx, ny, mx, my);
                        visited[nx][ny] = 0;
                        visited[mx][my] = 0;
                    }
                }
            }
        }
    }
}