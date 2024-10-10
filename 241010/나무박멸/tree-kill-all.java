import java.util.*;
import java.io.*;

public class Main {
	
	static int N, M, K, C;
	static int nowYear;
	static int total;
	// 나무 & 벽
	static int[][] g;
	// 제초제 마감년도
	static int[][] p;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static int[] ax = {-1, 1, -1, 1};
	static int[] ay = {1, 1, -1, -1};
	// 없앨 수 있는 나무 카운트
	static int rCnt;
	
	static class Pos implements Comparable<Pos> {
		int x, y, tCnt;

		public Pos(int x, int y, int tCnt) {
			super();
			this.x = x;
			this.y = y;
			this.tCnt = tCnt;
		}

		@Override
		public int compareTo(Pos other) {
			if(this.tCnt == other.tCnt) {
				if(this.x == other.x) {
					return this.y - other.y;
				}
				return this.x - other.x;
			}
			return other.tCnt - this.tCnt;
		}

		@Override
		public String toString() {
			return "Pos [x=" + x + ", y=" + y + ", tCnt=" + tCnt + "]";
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {
		// System.setIn(new FileInputStream("src/day1010/tree.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		
		g = new int[N][N];
		p = new int[N][N];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				g[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
//		print(g);
		
//		nowYear = 1;
//		System.out.println("nowYear: "+nowYear);
//		
//		// 1. 나무 성장
//		System.out.println("growTree");
//		growTree();
//		print(g);
//		// 2. 나무 번식
//		System.out.println("breedTree");
//		breedTree();
//		print(g);
//		// 3-1. 제초제 뿌릴 곳 선정
//		System.out.println("selectPos");
//		Pos spot = selectPos();
//		System.out.println(spot);
//		// 3-2. 제초제 뿌리기
//		System.out.println("spread");
//		total += spot.tCnt;
//		g[spot.x][spot.y] = 0;
//		for(int i=0; i<4; i++) {
//			spread(spot.x, spot.y, i, 1);
//		}
//		System.out.println("g");
//		print(g);
//		System.out.println("p");
//		print(p);
		
		
		total = 0;
		for(int m=1; m<M+1; m++) {
			nowYear = m;
			
			// 1. 나무 성장;
			growTree();
			
			// 2. 나무 번식
			breedTree();

			// 3-1. 제초제 뿌릴 곳 선정
			Pos spot = selectPos();
			
			// 3-2. 제초제 뿌리기
			total += spot.tCnt;
			g[spot.x][spot.y] = 0;
			for(int i=0; i<4; i++) {
				spread(spot.x, spot.y, i, 1);
			}
		}
		System.out.println(total);
		
		
	}
	public static void print(int[][] board) {
		for(int i=0; i<N; i++) {
			System.out.println(Arrays.toString(board[i]));
		}
		System.out.println();
	}
	public static boolean inRange(int x, int y) {
		return x>=0 && x<N && y>=0&& y<N;
	}
	public static void growTree() {
		for(int x=0; x<N; x++) {
			for(int y=0; y<N; y++) {
				// 나무
				if(g[x][y] > 0) {
					// 인접 4칸 확인해서 나무수 구하기
					int cnt = 0;
					for(int d=0; d<4; d++) {
						int nx = x + dx[d];
						int ny = y + dy[d];
						if(!inRange(nx, ny)) continue;
						if(g[nx][ny] <= 0) continue;   // 벽 or 빈칸
						cnt++;
					}
					
					// 인접 나무수 만큼 나무 성장시키기
					g[x][y] += cnt;
				}
			}
		}
	}
	public static void breedTree() {
		// 복사배열
		int[][] tmp = new int[N][N];
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				tmp[i][j] = g[i][j];
			}
		}
		
		for(int x=0; x<N; x++) {
			for(int y=0; y<N; y++) {
				// 나무
				if(g[x][y] > 0) {
					// 번식할 수 있는 방향리스트
					List<Integer> dList = new ArrayList<>();
					// 번식: 빈칸 && 제초제 없는 곳
					for(int d=0; d<4; d++) {
						int nx = x + dx[d];
						int ny = y + dy[d];
						if(!inRange(nx, ny)) continue;
						if(g[nx][ny] > 0) continue;    // 다른 나무칸 X
						if(g[nx][ny] == -1) continue;  // 벽 X
						if(p[nx][ny] != 0 && p[nx][ny] >= nowYear) continue; // 제조체 남아있음 X
						
						dList.add(d);
					}
					int cnt = dList.size();
					// 번식할 수 있는 곳이 없으면 continue;
					if(cnt == 0) continue;
					
					int bTree = g[x][y] / cnt;
					for(int i=0; i<cnt; i++) {
						int d = dList.get(i);
						int nx = x + dx[d];
						int ny = y + dy[d];
						tmp[nx][ny] += bTree;
					}
				}
			}
		}
		
		// 복사
		g = tmp;
	}
	public static void dfs(int x, int y, int d, int depth) {
		if(depth > K) return;
		
		// d방향 대각선으로 depth길이 만큼
		int nx = x + ax[d]*depth;
		int ny = y + ay[d]*depth;
		if(!inRange(nx, ny)) return;
		if(g[nx][ny] <= 0) return;	// 벽 or 빈칸
		rCnt += g[nx][ny];

		dfs(x, y, d, depth+1);
	}
	public static Pos selectPos() {
		PriorityQueue<Pos> pq = new PriorityQueue<>();
		
		// 박멸할 수 있는 나무 카운트
		for(int x=0; x<N; x++) {
			for(int y=0; y<N; y++) {
				// 나무 X
				if(g[x][y] <= 0) continue;
				rCnt = g[x][y];
//				System.out.println("bt:"+x+","+y+" "+rCnt);
				// 4개의 대각선 방향
				for(int d=0; d<4; d++) {					
					dfs(x, y, d, 1);
				}
				pq.add(new Pos(x, y, rCnt));
			}
		}
		
		return pq.poll();
	}
	public static void spread(int x, int y, int d, int depth) {
		if(depth > K) return;
		
		// d방향 대각선으로 depth길이 만큼
		int nx = x + ax[d]*depth;
		int ny = y + ay[d]*depth;
		if(!inRange(nx, ny)) return;
		// 벽 or 빈칸 : 해당칸까지 spread
		if(g[nx][ny] <= 0) { 
			p[nx][ny] = nowYear+C;
			return;	
		}
		
		p[nx][ny] = nowYear+C;
		g[nx][ny] = 0;

		spread(x, y, d, depth+1);
	}

}