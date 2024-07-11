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
    static class Potop implements Comparable<Potop>{
        int power, lastTime, x, y;
        boolean isPre;
        public Potop(int power, int lastTime, int x, int y, boolean isPre) {
            this.power = power;
            this.lastTime = lastTime;
            this.x = x;
            this.y = y;
            this.isPre = isPre;
        }
        @Override
        public int compareTo(Potop other) {
            // 가장 약한 공격자 뽑기 우선순위
            // 파워는 약한 순
            if(this.power != other.power) return this.power - other.power;
            // 시간은 가장 최근의 시간(큰 순) 
            if(this.lastTime != other.lastTime) return other.lastTime - this.lastTime;
            // (행+열)은 큰 순
            if(this.x+this.y != other.x+other.y) return (other.x+other.y) - (this.x+this.y);
            // 열은 큰 순
            return other.y - this.y;
        }
        public String toString(){
            return "power: "+this.power+", lastTime: "+ this.lastTime + ", isPre: "+this.isPre + ", ("+x+","+y+")";
        }
    }
    static Potop weak;
    static Potop strong;
    static List<Potop> liveList;

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
            // 살아있는 포탑 정리
            liveList = new ArrayList<>();
            for(int i=0; i<N; i++) {
                for(int j=0; j<M; j++) {
                    if(p[i][j].power > 0) {
                        liveList.add(p[i][j]);
                    }
                }
            }
            // 살아있는 포탑이 1개 이하면 중단
            if(liveList.size() <= 1){
                break;
            }

            find();

            // System.out.println("w: "+weak);
            // System.out.println("s: "+strong);
            weak.power += (N+M);
            // System.out.println("weak power : "+weak.power);
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
    public static void find() {
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
        // int cnt = 0;
        // for(int i=0; i<N; i++) {
        //     for(int j=0; j<M; j++) {
        //         if(p[i][j].power != 0){
        //             wpq.add(p[i][j]);
        //             spq.add(p[i][j]);
        //             cnt++;
        //         }
        //     }
        // }
        // if(cnt <= 1) {
        //     return false;
        // }

        Collections.sort(liveList);

        // 2. 공격자와 대상자 찾기
        // weak = wpq.peek();
        // strong = spq.peek();
        weak = liveList.get(0);
        strong = liveList.get(liveList.size()-1);   // 대상자 조건은 공격자와 반대
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
        Pos[][] predecessor = new Pos[N][M];
        boolean isOk = false;

        // predecessor 초기화
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                predecessor[i][j] = new Pos(0, 0);
            }
        }

        visited[sx][sy] = true;
        q.add(new Pos(sx, sy));
        while(!q.isEmpty()) {
            Pos now = q.poll();
            if(now.x == strong.x && now.y == strong.y){
                isOk = true;
                break;
            }
            for(int i=0; i<4; i++) {
                int nx = (now.x + rdx[i] + N) % N;
                int ny = (now.y + rdy[i] + M) % M;
                if(p[nx][ny].power != 0 && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    predecessor[nx][ny] = now;
                    q.add(new Pos(nx, ny));
                }
            }
        }
        if(!isOk) {
            return isOk;
        }
        // 경로 출력
        // for(int i=0; i<N; i++) {
        //     for(int j=0; j<M; j++) {
        //         System.out.print(predecessor[i][j]);
        //     }
        //     System.out.println();
        // }

        //경로의 포탑
        Pos pre = predecessor[strong.x][strong.y];
        while (true) {
            if (pre.x == weak.x && pre.y == weak.y) {
                break;
            }
            p[pre.x][pre.y].power -= (weak.power / 2);
            p[pre.x][pre.y].isPre = true;

            pre = predecessor[pre.x][pre.y];
        }        

        // 대상자의 포탑
        strong.power -= weak.power;
        strong.isPre = true;
        return isOk;
    }
    // 포탑 던지기
    public static void drop() {
        // 경로 포탑
        for(int i=0; i<8; i++) {
            int nx = (strong.x + tdx[i] + N) % N;
            int ny = (strong.y + tdy[i] + M) % M;
            if(nx == weak.x && ny == weak.y) {
                continue;
            }
            if(p[nx][ny].power != 0) {
                // System.out.println("("+nx+","+ny+")");
                p[nx][ny].power -= (weak.power / 2);
                p[nx][ny].isPre = true;
            }
        }
        // 대상자 포탑
        strong.power -= weak.power;
        strong.isPre = true;
    }

    // 포탑 부서짐 & 포탑 정비
    public static void reward() {
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                if(!p[i][j].isPre) {
                    if(p[i][j].power != 0){
                        p[i][j].power += 1;
                    }
                } else{
                    // 이전에 공격 당했던 것들이 0보다 작으면 0으로 초기화
                    if(p[i][j].power < 0){
                        p[i][j].power = 0;
                    }
                    p[i][j].isPre = false;
                }
            }
        }
    }
}