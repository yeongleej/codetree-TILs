import java.util.*;
import java.io.*;

public class Main {
	
	static int N, M, K;
	static int[][] g;
	static boolean[][] e;
	// 북 동 남 서
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static boolean isDone;
	static Golem now;
	static class Golem {
		int exit;
		Pos center;
		List<Pos> area;
		public Golem(int exit, Pos center, List<Pos> area) {
			this.exit = exit;
			this.center = center;
			this.area = area;
		}
		@Override
		public String toString() {
			return this.center+": "+this.exit+" "+area;
		}
	}
	static class Pos {
		int x, y;
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		@Override
		public String toString() {
			return this.x+","+this.y;
		}
	}
	static class Elf {
		int x, y, num;
		public Elf(int x, int y, int num) {
			this.x = x;
			this.y = y;
			this.num = num;
		}
		@Override
		public String toString() {
			return this.x+","+this.y+" "+this.num;
		}
	}
	

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		g = new int[N+3][M+1];
		e = new boolean[N+3][M+1];
		

		int ans = 0;
		
		for(int k=1; k<K+1; k++) {
			st = new StringTokenizer(br.readLine());
			int x = 1;
			int y = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
			// 골렘 생성
			List<Pos> area = new ArrayList<>();
			for(int i=0; i<4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				area.add(new Pos(nx, ny));
			}
			
//			print();
			
			Golem now = new Golem(d, new Pos(x, y), area);
//			System.out.println(now);
			

			// 더해지지 않는 골렘인경우
			boolean die = false;
			while(true) {
				// 가능한 경로 찾기 => 1)남 2)서 3)동
				int dir = find(now);
				
				// 가능한 경로 없음 => g 초기화 후 다시 진행
				if(dir == -1) {
					// reset 해야 되는 경우 : 몸의 일부가 여전히 숲을 벗어남
					if(now.center.x < 4) {
//						System.out.println("reset");
						reset();
						die = true;
					}
					// 숲에 포함되어 있으면 이동 그만하고 정령 이동시키기
					break;
				}
				
				// 가능한 경로 있음 => 골렘 이동
				moveG(dir, now);
				
			}
			
			// 죽은 거라면 포함 X
			if(die) continue;
			
			// 골렘 기록
			g[now.center.x][now.center.y] = k;
			List<Pos> aList = now.area;
			for(Pos p: aList) {
				g[p.x][p.y] = k;
			}
			// 출구 기록
			int ex = now.center.x + dx[now.exit];
			int ey = now.center.y + dy[now.exit];
			e[ex][ey] = true;
			
//			for(int i=0; i<N+3; i++) {
//				System.out.println(Arrays.toString(e[i]));
//			}
			
			// 정령 이동
			ans += (moveE(now.center.x, now.center.y)-2);
			
//			print();
			
//			System.out.println(ans);
		}
		
		System.out.println(ans);

	}
	public static void print() {
		for(int i=0; i<N+3; i++) {
			System.out.println(i+" "+ Arrays.toString(g[i]));
		}
	}
	public static void printG(Golem now) {
		int[][] board = new int[N+3][M+2];
		List<Pos> area = now.area;
		Pos center= now.center;
		board[center.x][center.y] = 1;
		for(Pos p: area) {
			board[p.x][p.y] = 1;
		}
		
		for(int i=0; i<N+3; i++) {
			System.out.println(i+" "+Arrays.toString(board[i]));
		}
	}
	public static boolean inRange(int x, int y) {
		return x>=0 && x<N+3 && y>0 && y<=M;
	}
	public static int find(Golem now) {
		// 남(2), 서(3), 동(1)
		int[] dir = {2, 3, 1};
		List<Pos> area = now.area;
		
		for(int i=0; i<3; i++) {
			// area 임시 복사
			List<Pos> tmp = new ArrayList<>();
			for(Pos p: area) {
				tmp.add(new Pos(p.x, p.y));
			}
//			System.out.println("nowDir:"+i+" :"+dir[i]);
			boolean isPossible = true;
			for(Pos p: tmp) {
				int nx = p.x + dx[dir[i]];
				int ny = p.y + dy[dir[i]];
				// 불가능
				if(!inRange(nx, ny) || g[nx][ny] != 0) {
					isPossible = false;
					break;
				}
				// 가능 => 임시 이동
				p.x = nx;
				p.y = ny;
			}
//			System.out.println(isPossible+" "+tmp);
			if(!isPossible) {
				continue;
			}
			
			// 서 or 동 : 남이동 한번 더 해야함
			if(i > 0) {
				for(Pos p: tmp) {
					int nx = p.x + dx[dir[0]];
					int ny = p.y + dy[dir[0]];
					// 불가능
					if(!inRange(nx, ny) || g[nx][ny] != 0) {
						isPossible = false;
						break;
					}
					p.x = nx;
					p.y = ny;
				}
			}
//			System.out.println(isPossible+" "+tmp);
			if(isPossible) {
				return dir[i];
			}
			
		}
		
		return -1;
		
	}
	public static void moveG(int d, Golem now) {
		Pos center = now.center;
		List<Pos> area = now.area;
	
		// 중심이동
		center.x += dx[d];
		center.y += dy[d];

		for(Pos p: area) {
			p.x += dx[d];
			p.y += dy[d];
		}
		
		// 남이 아닌 경우, 밑으로 한칸 더 이동
		if(d != 2) {
			// 중심이동
			center.x += dx[2];
			center.y += dy[2];
			
			for(Pos p: area) {
				p.x += dx[2];
				p.y += dy[2];
			}
		}
		
		// 서 = 3, 반시계 출구 위치 변경
		if(d == 3) {
			now.exit = (4+now.exit-1) % 4;
		}
		// 동 = 1, 시계 출구 위치 변경
		else if(d == 1) {
			now.exit = (now.exit+1) % 4;
		}
	}
	public static void reset() {
		for(int i=0; i<N+3; i++) {
			for(int j=0; j<M+1; j++) {
				g[i][j] = 0;
				e[i][j] = false;
			}
		}
	}
	public static int moveE(int x, int y) {
		boolean[][] visited = new boolean[N+3][M+1];
		Queue<Elf> q = new LinkedList<>();
		visited[x][y]= true;
		q.add(new Elf(x, y, g[x][y]));
		
		int maxR = 0;
		while(!q.isEmpty()) {
			Elf now = q.poll();
//			System.out.println(now);
			maxR = Math.max(maxR, now.x);
			
			for(int i=0; i<4; i++) {
				int nx = now.x + dx[i];
				int ny = now.y + dy[i];
				if(!inRange(nx, ny)) continue;
				if(visited[nx][ny]) continue;
				if(g[nx][ny] == 0) continue;
				
				// 현재위치가 출구면 인접 골렘 갈 수 있음
				if(e[now.x][now.y]) {
					visited[nx][ny] = true;
					q.add(new Elf(nx, ny, g[nx][ny]));
				} 
				
				// 출구가 아니라면 같은 골렘만
				else if(g[nx][ny] == now.num) {
					visited[nx][ny] = true;
					q.add(new Elf(nx, ny, g[nx][ny]));
				}
			}
		}
		return maxR;
	}

}