import java.util.*;
import java.io.*;

public class Main {
	
	static int N, Q;
	static int[] level;
	static int[] parents;
	static int[] authority;
	static boolean[] settings;
	static List<Integer>[] childs;
	static int cnt;

	public static void main(String[] args) throws IOException{
		// System.setIn(new FileInputStream("src/day1009/messenger.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());

		level = new int[N+1];
		parents = new int[N+1];
		authority = new int[N+1];

		// 알림망 초기설정 on(true)
		settings = new boolean[N+1];
		for(int i=0; i<N+1; i++) {
			settings[i] = true;
		}
		
		for(int q=0; q<Q; q++) {
			st = new StringTokenizer(br.readLine());
			int type = Integer.parseInt(st.nextToken());
			
			if(type == 100) {
				// 사내 메신저 준비
				parents[0] = -1;
				authority[0] = -1;
				for(int i=1; i<N+1; i++) {
					parents[i] = Integer.parseInt(st.nextToken());
				}
				for(int i=1; i<N+1; i++) {
					authority[i] = Integer.parseInt(st.nextToken());
				}
				
				// 트리의 level(높이) 설정
				int d = 1;
				int nowP = 0;
				for(int i=1; i<N+1; i++) {
					if(nowP != parents[i]) {
						nowP = parents[i];
						d++;
					}
					level[i] = d;
				}
				
//				System.out.println("paretns: "+Arrays.toString(parents));
//				System.out.println("authority: "+Arrays.toString(authority));
//				System.out.println("level: "+Arrays.toString(level));
				// 자식들 설정
				setChild();
				
				
			} else if(type == 200) {
				// 알림망 설정 on/off
				int c = Integer.parseInt(st.nextToken());
//				System.out.println(Arrays.toString(settings));
				turnSetting(c);
//				System.out.println("turn: "+c);
//				System.out.println(Arrays.toString(settings));
				
			} else if(type == 300) {
				// 권한 세기 변경
				int c = Integer.parseInt(st.nextToken());
				int power = Integer.parseInt(st.nextToken());
				
//				System.out.println("modify: "+c+" -> "+power);
//				System.out.println(Arrays.toString(authority));
				modifyPower(c, power);
//				System.out.println(Arrays.toString(authority));
				
			} else if(type == 400) {
				// 부모 채팅방 교환
				int c1 = Integer.parseInt(st.nextToken());
				int c2 = Integer.parseInt(st.nextToken());
//				System.out.println("changParents: "+c1+" <-> "+c2);
//				System.out.println(Arrays.toString(parents));
				changeParents(c1, c2);
//				System.out.println(Arrays.toString(parents));
				
				
			} else if(type == 500) {
				// 알림받을 수 있는 채팅방 수 조회
				int c = Integer.parseInt(st.nextToken());
				
				cnt = 0;
				countRoom(c, 1);
				System.out.println(cnt);
			}
		}
		
	}
	public static void setChild() {
		childs = new ArrayList[N+1];
		for(int i=0; i<N+1; i++) {
			childs[i] = new ArrayList<>();
		}
		
		for(int i=1; i<N+1; i++) {
			int p = parents[i];
			childs[p].add(i);
		}
		
//		System.out.println("childs");
//		for(int i=0; i<N+1; i++) {
//			System.out.println(i+": "+childs[i]);
//		}
	}
	public static void turnSetting(int id) {
		settings[id] = !settings[id];
	}
	public static void modifyPower(int id, int power) {
		authority[id] = power;
	}
	public static void changeParents(int c1, int c2) {
		int tmp = parents[c1];
		parents[c1] = parents[c2];
		parents[c2] = tmp;
		
		setChild();
	}
	public static void countRoom(int id, int depth) {
		// 내 level의 최대 노드수 == 2^level
		// 내 자식들 탐방
		List<Integer> cList = childs[id];
		for(int c: cList) {
			if(!settings[c]) continue;
			if(authority[c] >= depth) cnt++;
			countRoom(c, depth+1);
		}
		
	}

}