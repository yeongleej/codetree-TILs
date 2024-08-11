import java.util.*;
import java.io.*;

public class Main {
    static int N, M;
    static int[][] g;
    static boolean[][] visited;
    static int num;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static class Pos {
        int x, y;
        public Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
        @Override
        public String toString(){
            return "("+this.x+", "+this.y+")";
        }
    }
    static List<Pos> wList;
    static List<Pos> bList;
    static Map<Integer, List<Pos>> bMap;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        g = new int[N][M];
        wList = new ArrayList<>();
        bList = new ArrayList<>();
        bMap = new HashMap<>();
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

        visited = new boolean[N][M];
        num = 2;
        for(Pos b: bList){
            if(!visited[b.x][b.y]){
                visited[b.x][b.y] = true;
                g[b.x][b.y] = num;
                List<Pos> list = new ArrayList<>();
                list.add(b);
                bMap.put(num, list);
                bfs(b.x, b.y);
                num++;
            }
        }
        // System.out.println(bMap.size());
        // for(int i=2; i<num; i++){
        //     System.out.println(bMap.get(i));
        // }
        // for(int i=0; i<N; i++){
        //     for(int j=0; j<M; j++){
        //         System.out.print(g[i][j]+" ");
        //     }
        //     System.out.println();
        // }


        int ans = Integer.MIN_VALUE;
        for(Pos w: wList){
            Set<Integer> nSet = new HashSet<>();
            for(int i=0; i<4; i++){
                int nx = w.x+dx[i];
                int ny = w.y+dy[i];
                if(inRange(nx, ny) && g[nx][ny] != 0){
                    nSet.add(g[nx][ny]);
                }
            }
            if(nSet.size() >= 2){
                int total = 1;          // 해당 0칸 포함
                for(Integer n : nSet){
                    total += bMap.get(n).size();
                }
                ans = Math.max(ans, total);
            }
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
                    g[nx][ny] = num;
                    bMap.get(num).add(new Pos(nx, ny));
                    q.add(new Pos(nx, ny));
                }
            }
        }
    }
}