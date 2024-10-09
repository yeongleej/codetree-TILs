import java.util.*;
import java.io.*;

public class Main {
	
	static int L, N, Q;
	static Knight[] krr;
	static int[][] g;
	static int[][] s;
	static boolean[] isDie;
	static int[] damage;		// 체력리스트
	static boolean isOk;
	static Set<Integer> moveSet;
	// 상, 우, 하, 좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static class Knight {
		int x, y, h, w, k;

		public Knight(int x, int y, int h, int w, int k) {
			super();
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.k = k;
		}

		@Override
		public String toString() {
			return "Knight [x=" + x + ", y=" + y + ", h=" + h + ", w=" + w + ", k=" + k + "]";
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {
		// System.setIn(new FileInputStream("src/day1009/king.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
	    
		krr = new Knight[N+1];
		g = new int[L+1][L+1];
		for(int i=1; i<L+1; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<L+1; j++) {
				g[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		s = new int[L+1][L+1];
		isDie = new boolean[N+1];
		damage = new int[N+1];
		for(int i=1; i<N+1; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			
			krr[i] = new Knight(r, c, h, w, k);
			damage[i] = k;
			
		}
		
		// 명령 입력
		for(int q=0; q<Q; q++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
			// 사리진 기사에게 명령
			if(isDie[id]) continue;
			
			// 0. 기사 위치 초기화
			initArea();
			
			// 1. 기사 이동 가능 여부 체크
			isOk = true;
			moveSet = new HashSet<>();
			isMove(id, dir);
			
			if(!isOk) continue;
			
			// 2. 이동 가능하다면 이동
			moveKnight(dir);
			
			// 3. 기사 인접행렬 재정렬
			initArea();
			
			// 4. 함정 체크 & damage
			applyDamage(id);
		}
		
		
		// 살아남은 기사 총 대미지 합 출력
		int ans = 0;
		for(int i=1; i<N+1; i++) {
			if(isDie[i]) continue;
			Knight now = krr[i];
			
			ans += (now.k - damage[i]);
		}
		System.out.println(ans);
		
		
		
	}
	public static void print(int[][] board) {
		for(int i=0; i<L+1; i++) {
			System.out.println(Arrays.toString(board[i]));
		}
		System.out.println();
	}
	public static void initArea() {
		s = new int[L+1][L+1];
		
		for(int i=1; i<N+1; i++) {
			if(isDie[i]) continue;
			Knight now = krr[i];
			
			// 초기 기사 위치 세팅
			for(int x=now.x; x<now.x+now.h; x++) {
				for(int y=now.y; y<now.y+now.w; y++) {
					s[x][y] = i;
				}
			}
		}
		
	}
	public static boolean inRange(int x, int y) {
		return x>0 && x<=L && y>0 && y<=L;
	}
	public static void isMove(int id, int dir) {
		Knight now = krr[id];
		moveSet.add(id);
		
		// id의 영역 이동
		for(int i=now.x; i<now.x+now.h; i++) {
			for(int j=now.y; j<now.y+now.w; j++) {
				int nx = i + dx[dir];
				int ny = j + dy[dir];
				// 벽이랑 부딪히면 false
				if(!inRange(nx, ny)) {
					isOk = false;
					return;
				}
				if(g[nx][ny] == 2) {
					isOk = false;
					return;
				}
				// 연쇄 발생
				if(s[nx][ny] > 0 && s[nx][ny] != id) {
					isMove(s[nx][ny], dir);
				}
				
			}
		}
		
	}
	public static void moveKnight(int dir) {
		// 각 기사의 시작위치 변경
		for(int id: moveSet) {
			Knight now = krr[id];
			now.x += dx[dir];
			now.y += dy[dir];			
		}
	}
	
	public static void applyDamage(int id) {
		// 데미지를 받은 기사 선택
		boolean[] drr = new boolean[N+1];
		for(int m: moveSet) {
			if(m == id) continue;
			drr[m] = true;
		}
		
		// 명령을 실행하는 기사는 데미지 X
		for(int i=1; i<L+1; i++) {
			for(int j=1; j<L+1; j++) {
				if(s[i][j] == id) continue;
				int now = s[i][j];
				if(g[i][j] == 1 && drr[now]) {
					damage[now]--;
				}
			}
		}
		
		// 사라짐 체크
		for(int i=1; i<N+1; i++) {
			if(damage[i] <= 0) isDie[i] = true;
		}
	}

}