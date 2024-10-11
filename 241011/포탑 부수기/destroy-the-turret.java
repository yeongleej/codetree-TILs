import java.util.*;
import java.io.*;

public class Main {
	
	static int N, M, K;
	static int T; // 턴 기록
	static int[][] g;
	// 공격에 참여여부 체크
	static boolean[][] w;
	// 포탑 리스트
	static List<Potop> pList;
	// 참여자 리스트 (중간과정)
	static List<Potop> aList;
	static Potop weak;
	static Potop strong;
	// 우하좌상 + 대각선(우상, 우하, 좌하, 좌상)
	static int[] dx = {0, 1, 0, -1, -1, 1, 1, -1};
	static int[] dy = {1, 0, -1, 0, 1, 1, -1, -1};
	static class Potop implements Comparable<Potop> {
		int x, y, power, lastTime;

		public Potop(int x, int y, int power, int lastTime) {
			super();
			this.x = x;
			this.y = y;
			this.power = power;
			this.lastTime = lastTime;
		}
		// 가장 약한 포탑 기준
		@Override
		public int compareTo(Potop other) {
			// 1.공격력이 가장 낮은
			if(this.power == other.power) {
				// 2. 가장 최근에 공격한 포탑
				if(this.lastTime == other.lastTime) {
					//3. 행과 열의 합이 가장 큰
					if((this.x+this.y) == (other.x+other.y)) {
						// 4. 열 값이 가장 큰
						return other.y - this.y;
					}
					return (other.x+other.y) - (this.x+this.y);
				}
				return other.lastTime - this.lastTime;
			}
			return this.power - other.power;
		}
		@Override
		public String toString() {
			return "Potop [x=" + x + ", y=" + y + ", power=" + power + ", lastTime=" + lastTime + "]";
		}
		
	}
	static class Pos {
		int x, y;
		public Pos(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
		
	}

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		g = new int[N][M];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<M; j++) {
				g[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		// 초기 포탑리스트 생성(부서지지 않은)
		pList = new ArrayList<>();
		initPotop();
		
		// 현재 턴 기록
		T = 1;
		
		for(int k=0; k<K; k++) {
			// 0-1. 공격 참여여부 리스트 초기화
			w = new boolean[N][M];
			
			// 1. 공격자 선정 sort
			Collections.sort(pList);
			weak = pList.get(0);
			
			// 1-1. 공격력 증가 + 공격시간 기록
			increasePower();
			
			// 1-2. 공격 대상자 선정 & 공격
			strong = pList.get(pList.size()-1);
			attack();

			
			// 2-1. 공격 방법 선택 => 1)레이저 2)포탄던지기
			// 2-2. 레이저
			boolean isOk = useLaser();
			
			if(!isOk) {
				// 2-3. 포탄던지기
				useBomb();
			}
			
			// 지속 여부 결정 : 부서지지 않은 포탑이 1개면 즉시 종료
			if(!isCon()) break; 
			
			// 4. 공격에 참여하지 않은 포탄들 공격력 증가
			otherUpPower();
			
			// 5. 포탑리스트 재정비
			remakePList();
			
			T++;
		}
		
		Collections.sort(pList);
		int ans = pList.get(pList.size()-1).power;
		System.out.println(ans);
		
	}
	public static void printG() {
		for(int i=0; i<N; i++) {
			System.out.println(Arrays.toString(g[i]));
		}
		System.out.println();
	}
	public static void printW() {
		for(int i=0; i<N; i++) {
			System.out.println(Arrays.toString(w[i]));
		}
		System.out.println();
	}
	public static void initPotop() {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(g[i][j] > 0) {
					pList.add(new Potop(i, j, g[i][j], 0));
				}
			}
		}
	}
	public static boolean inRange(int x, int y) {
		return x>=0 && x<N && y>=0 && y<M;
	}
	public static void increasePower() {
		weak.power += (N+M);
		g[weak.x][weak.y] = weak.power;
		weak.lastTime = T;
		w[weak.x][weak.y] = true;
		
	}
	public static void attack() {
		strong.power = Math.max(0, strong.power-weak.power);
		g[strong.x][strong.y] = strong.power;
		w[strong.x][strong.y] = true;
	}
	public static boolean useLaser() {
		Queue<Pos> q = new LinkedList<>();
		boolean[][] visited = new boolean[N][M];
        Pos[][] predecessor = new Pos[N][M];
        boolean isOk = false;

        // predecessor 초기화: 경로 배열
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                predecessor[i][j] = new Pos(0, 0);
            }
        }
		
		// 가장 약한 포탑부터 시작
		q.add(new Pos(weak.x, weak.y));
		visited[weak.x][weak.y] = true;
		
		// 최종 경로 리스트
//		List<Pos> res = new ArrayList<>();
		while(!q.isEmpty()) {
			Pos now = q.poll();
			if(now.x == strong.x && now.y == strong.y) {
//				res = now.dList;
				isOk  = true;
				break;
			}
			
			// 우선순위: 우하좌상
			for(int i=0; i<4; i++) {
				int nx = (now.x + dx[i] + N) % N;
				int ny = (now.y + dy[i] + M) % M;
				if(g[nx][ny] == 0) continue;
				if(visited[nx][ny]) continue;
				// 방문가능
				visited[nx][ny] = true;
				// 경로 저장
				predecessor[nx][ny] = now;
				q.add(new Pos(nx, ny));
			}
		}
		
        if(!isOk) return false;
        
//        //경로 출력
//        System.out.println("route");
//        for(int i=0; i<N; i++) {
//            for(int j=0; j<M; j++) {
//                System.out.print(predecessor[i][j]);
//            }
//            System.out.println();
//        }

        //경로의 포탑: 도착지부터 거꾸로 순회
        Pos pre = predecessor[strong.x][strong.y];
        while (true) {
            if (pre.x == weak.x && pre.y == weak.y) {
                break;
            }
            // 공격력 weak.power/2 감소 + 참여 처리
            int hPower = weak.power / 2;
            for(Potop p: pList) {
            	if(pre.x == p.x && pre.y == p.y) {
            		p.power = Math.max(0, p.power-hPower);
            		g[p.x][p.y] = p.power;
            		w[p.x][p.y] = true;
            		break;
            	}
            }
            
            pre = predecessor[pre.x][pre.y];
        }
        return true;
		
	}
	public static void useBomb() {
		int hPower = weak.power / 2;
		
		for(int i=0; i<8; i++) {
			int nx = (strong.x + dx[i] + N) % N;
			int ny = (strong.y + dy[i] + M) % M;
			// 부서진 곳이면 X
			if(g[nx][ny] == 0) continue;
			// 공격자는 X
			if(nx == weak.x && ny == weak.y) continue;
			// 공격 진행
			for(Potop p: pList) {
				if(p.x == nx && p.y == ny) {
					p.power = Math.max(0, p.power-hPower);
					g[p.x][p.y] = p.power;
					w[p.x][p.y] = true;
					break;
				}
			}
		}
	}

	public static void otherUpPower() {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(w[i][j]) continue;
				if(g[i][j] == 0) continue;
				for(Potop p: pList) {
					// 공격에 가담하지 않은 참가자
					if(i==p.x && j==p.y) {
						p.power += 1;
						g[p.x][p.y] = p.power;
					}
				}
			}
		}
	}
	public static boolean isCon() {
		int cnt = 0;
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(g[i][j] > 0) cnt++;
			}
		}
		if(cnt > 1) return true;
		return false;
	}
	public static void remakePList() {
		// 현재 포탑들 중에 0인 것들은 포함 X
		List<Potop> tmp = new ArrayList<>();
		for(Potop p: pList) {
			if(p.power == 0) continue;
			tmp.add(p);
		}
		
		pList = tmp;
	}

}