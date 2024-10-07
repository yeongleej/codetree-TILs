import java.util.*;
import java.io.*;

public class Main {

	static int N, K, M;
	static int[][] g;
	static ArrayDeque<Integer> trr;		// 유물
	static int[] dx = {0, 0, -1, 1, 1, 1, -1, -1};
	static int[] dy = {-1, 1, 0, 0, -1, 1, -1, 1};
	static PriorityQueue<Area> aList;
	static boolean[][] visited;
	static PriorityQueue<Pos> pList;
	static class Area implements Comparable<Area> {
		int cx, cy, total, degree;
		int[][] t;
		public Area(int cx, int cy, int total, int degree, int[][] t) {
			this.cx = cx;
			this.cy = cy;
			this.total = total;
			this.degree = degree;
			this.t = t;
		}
		@Override
		public int compareTo(Area other) {
			if(this.total == other.total) {
				if(this.degree == other.degree) {
					if(this.cy == other.cy) {
						return this.cx - other.cx;
					}
					return this.cy - other.cy;
				}
				return this.degree - other.degree;
			}
			return other.total - this.total;
		}
		@Override
		public String toString() {
			return "Area [cx=" + cx + ", cy=" + cy + ", total=" + total + ", degree=" + degree + ", t="
					+ "]";
		}
		
		
	}
	static class Pos implements Comparable<Pos> {
		int x, y;
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		@Override
		public int compareTo(Pos other) {
			if(this.y == other.y) {
				// 열이 같다면 행이 큰순
				return other.x - this.x;					
			}
			// 열이 작은순
			return this.y - other.y;
		}
		@Override
		public String toString() {
			return "Pos [x=" + x + ", y=" + y + "]";
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = 5;
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		g = new int[N+1][N+1];
		for(int i=1; i<N+1; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<N+1; j++) {
				g[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		trr = new ArrayDeque<>();
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<M; i++) {
			trr.add(Integer.parseInt(st.nextToken()));
		}
		
		
		StringBuilder ans = new StringBuilder();
		
		for(int k=0; k<K; k++) {
			int val = 0;
		
			aList = new PriorityQueue<>();
			// 유적지 탐사
			findA();
			if(aList.size() == 0) break;	// 탐색 중단
			
			Area now = aList.poll();
			
			// 배열 돌리기
			for(int i=0; i<N+1; i++) {
				for(int j=0; j<N+1; j++) {
					g[i][j] = now.t[i][j];
				}
			}
			
			// 유물 연쇄탐색
			while(true) {
				// 유물 획득
				pList = new PriorityQueue<>();
				findT();
				if(pList.size() == 0) break;
				val += pList.size();
				
				// 유물 채우기
				while(!pList.isEmpty()) {
					Pos p = pList.poll();
					g[p.x][p.y] = trr.pollFirst();
				}
				
//				print(g);
//				System.out.println();
			}
			
			ans.append(val+" ");
			
		}
		
		System.out.println(ans.toString());
		
		
	}
	public static void print(int[][] b) {
		for(int i=0; i<N+1; i++) {
			System.out.println(Arrays.toString(b[i]));
		}
	}
	public static boolean inRange(int x, int y) {
		return x>0 && x<=N && y>0 && y<=N;
	}
	public static void findA() {
		for(int i=1; i<N-1; i++) {
			for(int j=1; j<N-1; j++) {
				// (i, j) 시작점
				// 3 X 3 정사각형
				// 원본배열 복사
				int[][] t1 = new int[N+1][N+1];
				for(int a=0; a<N+1; a++) {
					for(int b=0; b<N+1; b++) {
						t1[a][b] = g[a][b];
					}
				}
				
//				System.out.println("i:"+i+",j:"+j);
				// 각도 90, 180, 270
				for(int d = 0; d < 3; d++) {	
					// 원본배열 복사
					int[][] t2 = new int[N+1][N+1];
					for(int a=0; a<N+1; a++) {
						for(int b=0; b<N+1; b++) {
							t2[a][b] = t1[a][b];
						}
					}
					// 90도 회전
					for(int x=i; x<i+3; x++) {
						for(int y=j; y<j+3; y++) {
							int ox = x - i;
							int oy = y - j;
							int nx = oy;
							int ny = 3-ox-1;
							t2[nx+i][ny+j] = t1[x][y];
						}
					}
//					System.out.println(">>d:"+d);
//					print(t2);
//					System.out.println();
					t1 = t2;
					
					int res = 0;
					visited = new boolean[N+1][N+1];
					for(int a=1; a<N+1; a++) {
						for(int b=1; b<N+1; b++) {
							if(!visited[a][b]) {
								res += bfs1(a, b, t1);								
							}
						}
					}
					if(res != 0) {
						aList.add(new Area(i+1, j+1, res, d, t1));
					}
				}
				
			}
		}
	}
	public static int bfs1(int x, int y, int[][] t) {
		int total = 1;
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[] {x, y});
		visited[x][y] = true;
		int num = t[x][y];
		
		while(!q.isEmpty()) {
			int[] now = q.poll();
			
			for(int i=0; i<4; i++) {
				int nx = now[0] + dx[i];
				int ny = now[1] + dy[i];
				if(!inRange(nx, ny)) continue;
				if(visited[nx][ny]) continue;
				if(t[nx][ny] != num) continue;
				total += 1;
				visited[nx][ny] = true;
				q.add(new int[] {nx, ny});
			}
		}
		
		if(total < 3) return 0;
		return total;
	}
	public static void bfs2(int x, int y) {
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[] {x, y});
		visited[x][y] = true;
		Queue<int[]> prr = new LinkedList<>();
		prr.add(new int[] {x, y});
		int num = g[x][y];
		
		while(!q.isEmpty()) {
			int[] now = q.poll();
			
			for(int i=0; i<4; i++) {
				int nx = now[0] + dx[i];
				int ny = now[1] + dy[i];
				if(!inRange(nx, ny)) continue;
				if(visited[nx][ny]) continue;
				if(g[nx][ny] != num) continue;
				visited[nx][ny] = true;
				q.add(new int[] {nx, ny});
				prr.add(new int[] {nx, ny});
			}
		}
		
		if(prr.size() >= 3) {
			while(!prr.isEmpty()) {
				int[] now = prr.poll();
				pList.add(new Pos(now[0], now[1]));
			}
		}
	}
	public static void findT() {
		int total = 0;
		visited = new boolean[N+1][N+1];
		
		for(int i=1; i<N+1; i++) {
			for(int j=1; j<N+1; j++) {
				if(!visited[i][j]) {
					bfs2(i, j);
				}
			}
		}
	}

}