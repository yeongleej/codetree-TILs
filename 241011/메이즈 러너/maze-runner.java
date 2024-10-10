import java.util.*;
import java.io.*;

public class Main {
	
	static int N, M, K;
	static int dist;
	static int[][] g;
	static Pos[] runners;
	static Pos exit;
	// 회전 대상 참가자들
	static List<Integer> rList;
	static boolean[] isExit;
	static Pos start;
	static int size;
	// 상하좌우 우상, 우하, 좌하, 좌상
	static int[] dx = {-1, 1, 0, 0, -1, 1, 1, -1};
	static int[] dy = {0, 0, -1, 1, 1, 1, -1, -1};
	static class Pos {
		int x, y;
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "Pos [x=" + x + ", y=" + y + "]";
		}
		
		
	}

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		g = new int[N+1][N+1];
		for(int i=1; i<N+1; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<N+1; j++) {
				g[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		runners = new Pos[M];
		isExit = new boolean[M];
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			runners[i] = new Pos(x, y);
		}
		
		st = new StringTokenizer(br.readLine());
		int ex = Integer.parseInt(st.nextToken());
		int ey = Integer.parseInt(st.nextToken());
		exit = new Pos(ex, ey);
		
//		start = new Pos(0, 0);
//		find();
//		System.out.println(start);
		
		dist = 0;
		for(int k=1; k<K+1; k++) {
			// 1. 참가자 이동
			moveRunner();
			
			// 1-1 탈출 성공 여부 파악
			if(isDone()) break;
			
			// 2. 정사각형 찾기 => 좌상단
			// 정사각형 좌상단 가능 목록
			start = new Pos(0, 0);
			size = 0;
			find();
			
			
			// 2-1. 정사각형에 포함된 러너 찾기
			rList = new ArrayList<>();
			findRunner();
			
			
			// 3. 내구성 감소
			decrease(start.x, start.y, size);
			
			// 4. 돌리기
			// 4-1. 배열 돌리기
			turnG(start.x, start.y, size);

			// 4-2. 출구 돌리기
			turnE(start.x, start.y, size);

			// 4-3. 참가자 돌리기
			turnR(start.x, start.y, size);
			
		}
		System.out.println(dist);
        System.out.println(exit.x+" "+exit.y);
		
		
		
	}
	public static void print() {
		for(int i=0; i<N+1; i++) {
			System.out.println(Arrays.toString(g[i]));
		}
	}
	public static boolean inRange(int x, int y) {
		return x>0 && x<=N && y>0 && y<=N;
	}
	public static int calDist(int ex, int ey, int x, int y) {
		return Math.abs(ex-x)+Math.abs(ey-y);
	}
	public static void moveRunner() {
		for(int i=0; i<M; i++) {
			// 탈출러너
			if(isExit[i]) continue;
			
			Pos r = runners[i];
			int mx = 0;
			int my = 0;
			// 움직이기 전 출구와 거리
			int len = calDist(exit.x, exit.y, r.x, r.y);
			for(int d=0; d<4; d++) {			// 상하좌우 순: 우선순위) 상하우선
				int nx = r.x + dx[d];
				int ny = r.y + dy[d];
				if(!inRange(nx, ny)) continue; 	// 범위 밖
				if(g[nx][ny] > 0) continue;		// 벽
				int nd = calDist(exit.x, exit.y, nx, ny);

				if(nd < len) {
					len = nd;
					mx = nx;
					my = ny;
				}
			}
			// 움직일 수 없음
			if(mx ==0 && my == 0) continue;
			// 움직일 수 있다면 move
			r.x = mx;
			r.y = my;

			dist += 1;
			
			// 움직였는데 출구
			if(r.x == exit.x && r.y == exit.y) {
				isExit[i] = true;
			}
		}
	}
	public static boolean isDone() {
		for(int i=0; i<M; i++) {
			if(!isExit[i]) return false;
		}
		return true;
	}
	public static int calLen(int x, int y) {
		return Math.max(Math.abs(exit.x-x), Math.abs(exit.y-y));
	}
	public static void find() {
		// 좌상단 1,1 부터 크기가 2~N*N인 정사각형 만들어보기
		for(int s=2; s<N+1; s++) {
			for(int x=1; x<=N-s+1; x++) {
				for(int y=1; y<=N-s+1; y++) {
					// 정사각형 범위: x~x+s, y~y+s
					// 해당 범위에 탈출구 있음
					if(x<=exit.x && x+s>exit.x && y<=exit.y && y+s>exit.y) {
//						System.out.println(x+","+y+" "+s);
						
						// 러너도 있는지 찾기
						for(int i=0; i<M; i++) {
							if(isExit[i]) continue;
							Pos r = runners[i];
							if(x<=r.x && x+s>r.x && y<=r.y && y+s>r.y) {
								start.x = x;
								start.y = y;
								size = s;
								return;
							}
						}
					}
				}
			}
		}
		
	}
	public static void findRunner() {
		// 러너 반복
		for(int i=0; i<M; i++) {
			// 탈출러너
			if(isExit[i]) continue;
			
			Pos r = runners[i];
			if(r.x >= start.x && r.x < start.x+size && r.y >= start.y && r.y<start.y+size) {
				rList.add(i);
			}
		}
	}
	public static void decrease(int sx, int sy, int size) {
		for(int x=sx; x<sx+size; x++) {
			for(int y=sy; y<sy+size; y++) {
				if(g[x][y] > 0) g[x][y]--;
			}
		}
	}
	public static void turnG(int sx, int sy, int size) {
		// 배열 복사
		int[][] t = new int[N+1][N+1];
		for(int i=1; i<N+1; i++) {
			for(int j=1; j<N+1; j++) {
				t[i][j] = g[i][j];
			}
		}
		
		for(int x=sx; x<sx+size; x++) {
			for(int y=sy; y<sy+size; y++) {
				int ox = x - sx;
				int oy = y - sy;
				int nx = oy;
				int ny = size-ox-1;
				t[nx+sx][ny+sy] = g[x][y];
			}
		}
		
		// 복사
		g = t;
	}
	public static void turnE(int sx, int sy, int size) {
		int ox = exit.x - sx;
		int oy = exit.y - sy;
		int nx = oy;
		int ny = size - ox - 1;
		exit.x = nx + sx;
		exit.y = ny + sy;
	}
	public static void turnR(int sx, int sy, int size) {
		for(int i: rList) {
			Pos r = runners[i];
			int ox = r.x - sx;
			int oy = r.y - sy;
			int nx = oy;
			int ny = size - ox - 1;
			r.x = nx + sx;
			r.y = ny + sy;
		}
	}

}