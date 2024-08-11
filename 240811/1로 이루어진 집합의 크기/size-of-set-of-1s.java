import java.util.*;
import java.io.*;

public class Main {
    static int N, M;
    static int[][] g;
    static boolean[][] visited;
    static int total;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static class Pos {
        int x, y;
        public Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    static List<Pos> wList;
    static List<Pos> bList;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        g = new int[N][M];
        wList = new ArrayList<>();
        bList = new ArrayList<>();
        for(int i=0; i<N; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<M; j++){
                g[i][j] = Integer.parseInt(st.nextToken());
                if(g[i][j] == 0){
                    wList.add(new Pos(i, j));
                } else{
                    bList.add(new Pos(i, j));
                }
            }
        }

        int ans = Integer.MIN_VALUE;
        for(Pos w: wList){
            visited = new boolean[N][M];
            g[w.x][w.y] = 1;
            for(Pos b: bList){
                if(!visited[b.x][b.y]){
                    visited[b.x][b.y] = true;
                    total = 1;
                    bfs(b.x, b.y);
                    ans = Math.max(ans, total);
                }
            }
            g[w.x][w.y] = 0;
        }
        System.out.println(ans);  
   
    }
    public static boolean inRange(int x, int y){
        return x>=0 && x<N && y>=0 && y<M;
    }
    public static void bfs(int sx, int sy){
        Queue<Pos> q = new LinkedList<>();
        q.add(new Pos(sx, sy));

        while(!q.isEmpty()){
            Pos now = q.poll();
            for(int i=0; i<4; i++){
                int nx = now.x+dx[i];
                int ny = now.y+dy[i];
                if(inRange(nx, ny) && g[nx][ny] == 1 && !visited[nx][ny]){
                    visited[nx][ny] = true;
                    total += 1;
                    q.add(new Pos(nx, ny));
                }
            }
        }
    }
}