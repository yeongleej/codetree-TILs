import java.util.*;
import java.io.*;

public class Main {
	
	static int N, M, P, C, D;
	// 루돌프 위치
	static int rx, ry;
	static Santa[] srr;
	static int[][] g;
	static int[] p;			// 산타 점수 배열
	static boolean[] isDie;	// 산타 탈락여부
	static int[] dx = {-1, 0, 1, 0, 1, 1, -1, -1};	// 상, 우, 하, 좌 우선순위
	static int[] dy = {0, 1, 0, -1, 1, -1, 1, -1};
	static class Santa {
		int num, x, y, rest;
		public Santa(int num, int x, int y, int rest) {
			this.num = num;
			this.x = x;
			this.y = y;
			this.rest = rest;
		}
		@Override
		public String toString() {
			return "Santa [num=" + num + ", x=" + x + ", y=" + y + ", rest=" + rest + "]";
		}
		
		
	}

	public static void main(String[] args) throws IOException{
		// System.setIn(new FileInputStream("src/day1008/ludol_input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		// 루돌프 초기 위치
		rx = Integer.parseInt(st.nextToken());
		ry = Integer.parseInt(st.nextToken());
		
		g = new int[N+1][N+1];
		srr = new Santa[P+1];
		p = new int[P+1];
		isDie = new boolean[P+1];
		for(int i=0; i<P; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			srr[num] = new Santa(num, x, y, 0);
			g[x][y] = num;
		}
		
		
		
		// 턴 반복
		int t = 1;
		while(M > 0) {
//			System.out.println("TRUN "+t);
//			print();
			
//			System.out.println("moveLudol");
			moveLudol();
			
//			print();
			
//			System.out.println("Ludol: "+rx+", "+ry);
//			System.out.println(Arrays.toString(srr));
//			System.out.println(Arrays.toString(isDie));
//			System.out.println(Arrays.toString(p));
//			
//			System.out.println("moveSanta");
			moveSanta();
			
//			print();
			
//			System.out.println(Arrays.toString(srr));
//			System.out.println(Arrays.toString(isDie));
//			System.out.println(Arrays.toString(p));
			
			// 살아남은 산타 점수 증가
			int cnt = 0;
			for(int i=1; i<P+1; i++) {
				if(isDie[i]) continue;
				p[i]++;
				cnt++;
			}
//			System.out.println("alive: "+cnt);
//			System.out.println(Arrays.toString(p));
			
			// 더이상 산 산타가 없으면 종료
			if(cnt == 0) break;
			
			M--;
			t++;
		}
		
//		System.out.println(Arrays.toString(p));
		for(int i=1; i<P+1; i++) {
			System.out.print(p[i]+" ");
		}
		System.out.println();
		

	}
	public static void print() {
		for(int i=0; i<N+1; i++) {
			System.out.println(Arrays.toString(g[i]));
		}
	}
	public static boolean inRange(int x, int y) {
		return x>0 && x<=N && y>0 && y<=N;
	}
	public static int calDist(int a, int b, int x, int y) {
		return (a-x)*(a-x) + (b-y)*(b-y);
	}
	// 루돌프 이동 방향 구하기
	public static int calDir(int sx, int sy) {
		// 상, 우, 하, 좌, 우하, 좌하, 우상, 좌상 
		// 8방향 중 가장 가까워 질 수 있는 방향으로 
		int dir = -1;
		int dist = Integer.MAX_VALUE;
		for(int i=0; i<8; i++) {
			int nx = rx + dx[i];
			int ny = ry + dy[i];
			if(!inRange(nx, ny)) continue;
			int d = calDist(nx, ny, sx, sy);
			if(d < dist) {
				dir = i;
				dist = d;
			}
		}
		
		return dir;
	}
	// 산타 상호작용
	public static void conSanta(int num, int x, int y, int dir) {
		Santa now = srr[num];
		int nx = x+dx[dir];
		int ny = y+dy[dir];
		if(!inRange(nx, ny)) {
			// 산타 탈락
			isDie[num] = true;
		} else {
			if(g[nx][ny] != 0) {
				int oriNum = g[nx][ny];
				now.x = nx;
				now.y = ny;
				g[now.x][now.y] = now.num;
				// 연쇄 상호작용
				conSanta(oriNum, nx, ny, dir);
				
			} else {
				now.x = nx;
				now.y = ny;
				g[now.x][now.y] = now.num;
			}
		}
		
	}
	// 충돌 발생 C: 루돌프->산타, D: 산타->루돌프
	public static void crush(int num, int x, int y, int dir, int type) {
		// 산타 점수 획득
		p[num] += type;
		
		// 산타 이동 && 기절 처리
		Santa now = srr[num];
		// 기절
		if(type == D) now.rest = 1;
		else now.rest = 2;
		
		int nx = x + dx[dir]*type;
		int ny = y + dy[dir]*type;
		// 산타 아웃
		if(!inRange(nx, ny)) {
			isDie[num] = true;
			g[x][y] = 0;
		} 
		else {
			// 충돌 산타 이동
			g[now.x][now.y] = 0;
			now.x = nx;
			now.y = ny;
			// 상호작용 발생
			int oriNum = g[now.x][now.y];
			g[now.x][now.y] = now.num;
			if(oriNum != 0) {
				conSanta(oriNum, now.x, now.y, dir);
			}
		}
	}
	public static void moveLudol() {
		// 거리가 가장 가깝고, x좌표가 크고, y좌표가 큰
		PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>(){
			@Override
			public int compare(int[] o1, int[] o2){
					// 0번째 원소가 같을 경우, 1번째 원소를 기준으로 삽입
					if (o1[0]==o2[0]) {
						if(o1[1] == o2[1]) {
							return o2[2]-o1[2];
						}
						return o2[1]-o1[1];
					}
					return o1[0]-o2[0];
			}
		});
		
		// 산타 반복
		for(int i=1; i<P+1; i++) {
			Santa now = srr[i];
			// 이미 탈락한 산타
			if(isDie[i]) continue;
			int dist = calDist(rx, ry, now.x, now.y);
			pq.add(new int[] {dist, now.x, now.y, now.num});
		}
		
		int[] s = pq.poll();
		int dir = calDir(s[1], s[2]);
//		System.out.println("Santa: "+s[3]+" "+s[1]+","+s[2]);
//		System.out.println(dir);
		
		// <<루돌프 이동>>
		rx += dx[dir];
		ry += dy[dir];
		
		// 루돌프 -> 산타 충돌 발생
		if(g[rx][ry] != 0) {
//			System.out.println("crushLudol");
			crush(g[rx][ry], rx, ry, dir, C);
		}
	}
	public static void moveSanta() {
		for(int i=1; i<P+1; i++) {
			Santa now = srr[i];
			if(isDie[i]) continue;	// 탈락한 산타
			if(now.rest > 0) {		// 기절 산타
				now.rest--;
				continue;
			}
			int dir = -1;
			int sx = 0;
			int sy = 0;
			int oriDist = calDist(rx, ry, now.x, now.y);
			for(int d = 0; d<4; d++) {
				int nx = now.x + dx[d];
				int ny = now.y + dy[d];
				if(!inRange(nx, ny)) continue;	// 게임 밖 X
				if(g[nx][ny] != 0) continue;	// 다른 산타 있는 곳 X
				int dist = calDist(rx, ry, nx, ny);
				if(dist < oriDist) {
					dir = d;
					sx = nx;
					sy = ny;
					oriDist = dist;
				}
			}
			
			if(dir == -1) continue;	// 가능한 곳 없음
			
			// <<산타 이동>>
			g[now.x][now.y] = 0;
			now.x = sx;
			now.y = sy;
			g[now.x][now.y] = now.num;
			
			// 움직였는데 산타 -> 루돌프 충돌 발생
			if(rx == now.x && ry == now.y) {
//				System.out.println("crushSanta");
				int nd = (dir+2) % 4;
				crush(now.num, now.x, now.y, nd, D);
				
			} 
		}
	}

}