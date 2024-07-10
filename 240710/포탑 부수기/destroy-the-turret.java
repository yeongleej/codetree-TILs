import java.util.*;
import java.io.*;

public class Main {
    
    static int N;
    static int M;
    static int K;
    static Potop[][] p;
    // 레이저 방향: 우하좌상
    static int[] rdx = {0, 1, 0, -1};
    static int[] rdy = {1, 0, -1, 0};
    // 포탄던지기 방향 8방향
    static int[] tdx = {0, 1, 0, -1, -1, -1, 1, 1};
    static int[] tdy = {1, 0, -1, 0, -1, 1, -1, 1};
    static class Potop {
        int power, lastTime, x, y;
        boolean isPre;
        public Potop(int power, int lastTime, int x, int y, boolean isPre) {
            this.power = power;
            this.lastTime = lastTime;
            this.x = x;
            this.y = y;
            this.isPre = isPre;
        }
        public String toString(){
            return "power: "+this.power+", lastTime: "+ this.lastTime + ", isPre: "+this.isPre + ", ("+x+","+y+")";
        }
    }
    static Potop weak;
    static Potop strong;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        // 포탑 입력 및 초기화
        p = new Potop[N][M];
        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<M; j++) {
                int power = Integer.parseInt(st.nextToken());
                p[i][j] = new Potop(power, 0, i, j, false);
            }
        }

        // for(int i=0; i<N; i++) {
        //     for(int j=0; j<M; j++) {
        //         System.out.println(p[i][j]);
        //     }
        // }
        // System.out.println(wpq.poll());
        // System.out.println(spq.poll());

        // 공격자 & 대상자 찾기
        // find();
        // System.out.println(weak);
        // weak.power--;
        // System.out.println(p[0][1]);
        // find();
        // System.out.println(weak);
        // System.out.println(strong);


        // K번 진행
        for(int k=1; k<K+1; k++) {
            if(!find()) {
                break;
            }
            // System.out.println("w: "+weak);
            // System.out.println("s: "+strong);
            weak.power += (N+M);
            weak.isPre = true;
            weak.lastTime = k;
            if(!bfs(weak.x, weak.y)) {
                // System.out.print("던지기 실행");
                drop();
            }
            // System.out.println("공격후");
            // print();

            reward();
            // System.out.println("정비후");
            // print();
        }

        // 정답 출력
        int ans = Integer.MIN_VALUE;
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                if(p[i][j].power > ans){
                    ans = p[i][j].power;
                }
            }
        }
        System.out.println(ans);


    }
    public static void print() {
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                System.out.print("("+p[i][j].power +","+p[i][j].isPre+")");
            }
            System.out.println();
        }
    }
    // 공격자와 대상자 찾기
    public static boolean find() {
        // 1. 부서진 포탑 정리 (포탑 정보 갱신해서 다시 PQ에 넣기)
        PriorityQueue<Potop> wpq = new PriorityQueue<>(new Comparator<Potop>() {
            @Override
            public int compare(Potop p1, Potop p2) {
                if(p1.power == p2.power) {
                    if(p1.lastTime == p2.lastTime) {
                        if((p1.x+p1.y) == (p2.x+p2.y)) {
                            return p2.y - p1.y;
                        }
                        return (p2.x+p2.y) - (p1.x+p1.y);
                    }
                    return p2.lastTime - p1.lastTime;
                }
                return p1.power - p2.power;
            }  
        });
        PriorityQueue<Potop> spq = new PriorityQueue<>(new Comparator<Potop>() {
            @Override
            public int compare(Potop p1, Potop p2) {
                if(p1.power == p2.power) {
                    if(p1.lastTime == p2.lastTime) {
                        if((p1.x+p1.y) == (p2.x+p2.y)) {
                            return p1.y - p2.y;
                        }
                        return (p1.x+p1.y) - (p2.x+p2.y);
                    }
                    return p1.lastTime - p2.lastTime;
                }
                return p2.power - p1.power;
            }  
        });
        int cnt = 0;
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                if(p[i][j].power != 0){
                    wpq.add(p[i][j]);
                    spq.add(p[i][j]);
                    cnt++;
                }
            }
        }
        if(cnt <= 1) {
            return false;
        }
        // 2. 공격자와 대상자 찾기
        weak = wpq.peek();
        strong = spq.peek();
        return true;
    }
    static class Pos {
        int x, y;
        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public String toString() {
            return "("+this.x+","+this.y+")";
        }
    }
    public static boolean bfs(int sx, int sy) {
        boolean[][] visited = new boolean[N][M];
        Queue<Pos> q = new LinkedList<>();
        List<Potop> routes = new ArrayList<>();
        boolean isOk = false;

        visited[sx][sy] = true;
        q.add(new Pos(sx, sy));
        while(!q.isEmpty()) {
            Pos now = q.poll();
            if(now.x == strong.x && now.y == strong.y){
                isOk = true;
                break;
            }
            routes.add(p[now.x][now.y]);
            for(int i=0; i<4; i++) {
                int nx = now.x + rdx[i];
                int ny = now.y + rdy[i];
                if(0<=nx && nx<N && 0<=ny && ny<M && p[nx][ny].power != 0 && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    q.add(new Pos(nx, ny));
                    break;
                }
            }
        }
        if(!isOk) {
            return isOk;
        }
        // System.out.println(routes);
        // 경로의 포탑
        for(int i=1; i<routes.size(); i++) {
            Potop potop = routes.get(i);
            potop.power -= (weak.power / 2);
            potop.isPre = true;
        }
        // 대상자의 포탑
        strong.power -= weak.power;
        strong.isPre = true;
        return isOk;
    }
    // 포탄 던질 위치 재조정
    public static int rePos(int p, int s) {
        if (p < 0) {
            return p+s;
        } 
        return p%s;
    }
    // 포탑 던지기
    public static void drop() {
        // 경로 포탑
        for(int i=0; i<8; i++) {
            int nx = strong.x + tdx[i];
            int ny = strong.y + tdy[i];
            if(nx<0 || nx>=N){
                nx = rePos(nx, N);
            }
            if(ny<0 || ny>=M){
                ny = rePos(ny, M);
            }
            if(p[nx][ny].power != 0) {
                p[nx][ny].power -= (weak.power / 2);
                p[nx][ny].isPre = true;
            }
        }
        // 대상자 포탑
        strong.power -= weak.power;
        strong.isPre = true;
    }

    // 포탑 정비
    public static void reward() {
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                if(!p[i][j].isPre) {
                    if(p[i][j].power != 0){
                        p[i][j].power += 1;
                    }
                } else{
                    p[i][j].isPre = false;
                }
            }
        }
    }
}