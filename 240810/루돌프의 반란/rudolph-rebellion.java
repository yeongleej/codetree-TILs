import java.util.*;
import java.io.*;

public class Main {
    static int N, M, P, C, D;
    static int rx, ry;
    static List<Santa> sList;
    static Map<Integer, Santa> sMap;
    static int[][] g;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static int nowTurn;

    static class Santa {
        int num, score, x, y, isDown;
        public Santa(int num, int score, int x, int y, int isDown){
            this.num = num;
            this.score = score;
            this.x = x;
            this.y = y;
            this.isDown = isDown;
        }
        @Override
        public String toString(){
            return "n:"+this.num+",("+this.x+","+this.y+") isDown:"+this.isDown+"|s:"+this.score;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());

        g = new int[N+1][N+1];
        sList = new ArrayList<>();
        sMap = new HashMap<>();

        st = new StringTokenizer(br.readLine());
        rx = Integer.parseInt(st.nextToken());
        ry = Integer.parseInt(st.nextToken());

        for(int i=0; i<P; i++){
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            g[x][y] = num;
            Santa s = new Santa(num, 0, x, y, 0);
            sList.add(s);
            sMap.put(num, s);
        }

        for(int m=1; m<M+1; m++){
            // 살아있는 산타 확인
            if(!checkSanta()) break;
            nowTurn = m;

//            System.out.println(m+" => 루돌프 이동 전 rx:"+rx+", ry:"+ry);
//            System.out.println("산타이동 전");
//            printG();

            // 루돌프 이동
            moveRu();
//             System.out.println("루돌프 이동 후 rx:"+rx+", ry:"+ry);

            // 산타 이동
            moveSanta();
//             System.out.println("산타이동 후");
//             printG();

            // // 살아있는 산타 1점씩 증가
            addScore();
//            for(Santa s: sList) {
//            	System.out.println(s);
//            }
        }
        for(int i=1; i<P+1; i++){
            System.out.print(sMap.get(i).score+" ");
        }
        System.out.println();
    }
    public static void printG() {
        for(int i=0; i<N+1; i++){
            System.out.println(Arrays.toString(g[i]));
        }
    }
    public static boolean inRange(int x, int y){
        return x>0 && x<=N && y>0 && y<=N;
    }
    public static boolean checkSanta(){
        for(int i=1; i<N+1; i++){
            for(int j=1; j<N+1; j++){
                if(g[i][j] != 0){
                    return true;
                }
            }
        }
        return false;
    }
    public static int calDist(int a, int b, int x, int y){
        return (a-x)*(a-x) + (b-y)*(b-y);
    }
    public static int[] findDir(int x, int y){
        int nx = x - rx;
        int ny = y - ry;
        int[] ans = new int[2];
        if(nx > 1) nx = 1;
        else if(nx < -1) nx = -1;

        if(ny > 1) ny = 1;
        else if(ny < -1) ny = -1;

        ans[0] = nx;
        ans[1] = ny;
        return ans;
    }
    public static void moveRu() {
        int dist = Integer.MAX_VALUE;
        int[] d = new int[2];
        int tx = 0;
        int ty = 0;

        for(Santa s : sList){
            if(!inRange(s.x, s.y)) continue;    // 탈락한 산타는 pass
            int[] dir = findDir(s.x, s.y);
            int nx = rx + dir[0];
            int ny = ry + dir[1];
            if(!inRange(nx, ny)) continue;      // 루돌프가 이동할 위치가 범위를 벗어나면 pass
            int nowDist = calDist(rx, ry, s.x, s.y);
            if(nowDist < dist){
                dist = nowDist;
                tx = s.x;
                ty = s.y;
                d[0] = dir[0];
                d[1] = dir[1];
            } else if(nowDist == dist){
                if(tx < s.x){
                    tx = s.x;
                    ty = s.y;
                    d[0] = dir[0];
                    d[1] = dir[1];
                } else if(tx == s.x && ty < s.y){
                    tx = s.x;
                	ty = s.y;
                	d[0] = dir[0];
                    d[1] = dir[1];
                }
            }
        }
        // System.out.println("tx:"+tx+", ty:"+ty);
        // System.out.println("d:"+Arrays.toString(d));
        rx += d[0];
        ry += d[1];

        // 충돌 발생
        if(g[rx][ry] != 0){
            accident(g[rx][ry], d, C);
        }
    }
    public static void accident(int num, int[] d, int point){
        Santa s = sMap.get(num);
//        System.out.println("충돌 산타: "+s.num+"("+s.x+","+s.y+")");
        int nx = s.x + d[0]*point;
        int ny = s.y + d[1]*point;
        if(inRange(nx, ny)){
            // 산타가 이동한 곳에 다른 산타 있음 => 상호작용 발생
            if(g[nx][ny] != 0) {
                communicate(g[nx][ny], d);
            }
            g[nx][ny] = s.num;
        }
        g[s.x][s.y] = 0;
        s.x = nx;
        s.y = ny;
        s.isDown = nowTurn+2;
        s.score += point;
    }
    public static void communicate(int num, int[] d){
        Santa s = sMap.get(num);
        // System.out.println("상호작용 발생) 대상:"+s.num);

        int nx = s.x + d[0];
        int ny = s.y + d[1];

        if(inRange(nx, ny)){
            // 산타가 이동한 곳에 다른 산타 있음 => 상호작용 발생
            if(g[nx][ny] != 0){
                communicate(g[nx][ny], d);
            }
            g[nx][ny] = s.num;
        }
        // 밀려남
        g[s.x][s.y] = 0;
        s.x = nx;
        s.y = ny;
        // System.out.println("상호작용 후 대상:"+s.num+"("+s.x+","+s.y+")");
    }
    public static void moveSanta() {
        for(int p=1; p<P+1; p++){
        	Santa s = sMap.get(p);
            if(!inRange(s.x, s.y)) continue; // 탈락한 산타
            if(s.isDown > 0){
            	if(s.isDown != nowTurn) {
            		continue;
            	}
                s.isDown = 0;
            }
            int dist = calDist(rx, ry, s.x, s.y);
            int dir = -1;
            boolean isMove = false;
            for(int i=0; i<4; i++){
                int nx = s.x + dx[i];
                int ny = s.y + dy[i];
                if(inRange(nx, ny) && g[nx][ny] == 0){
                    int nowDist = calDist(rx, ry, nx, ny);
                    if(nowDist < dist){
                        dist = nowDist;
                        dir = i;
                        isMove = true;
                    }
                }
            }
            // System.out.println("sNum:"+s.num+", dist:"+dist+", dir:"+dir);
            // 이동할 곳에 루돌프가 있다면 충돌 발생

            // 이동할 수 있으면 이동
            if(isMove){
                g[s.x][s.y] = 0;
                s.x += dx[dir];
                s.y += dy[dir];
                g[s.x][s.y] = s.num;
            }
            // 이동후 충돌 발생
            if(s.x==rx && s.y==ry){
                dir = (dir+2) % 4;
                int[] d = new int[2];
                d[0] = dx[dir];
                d[1] = dy[dir];
                // System.out.println("산타충돌발생) sNum:"+s.num+", sx:"+s.x+", sy:"+s.y+", d:"+Arrays.toString(d));
                accident(g[s.x][s.y], d, D);
            }
        }
    }
    public static void addScore() {
        for(Santa s: sList) {
            if(!inRange(s.x, s.y)) continue;
            s.score += 1;
        }
    }
}