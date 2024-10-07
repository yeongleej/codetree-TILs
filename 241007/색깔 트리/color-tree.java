import java.util.*;
import java.io.*;

public class Main {
	
	static int MAX = 100001;
	static int COLOR = 5;
	// nrr: 노드 정보
	static Node[] nrr;
	// child[i]: i노드의 자식 정보리스트
	static List<Integer>[] child;
	static boolean flag;
	static class Node {
		int id, color, maxDepth, pid;
		int[] crr;
		public Node(int id, int pid, int color, int maxDepth, int[] crr) {
			this.id = id;
			this.pid = pid;
			this.color = color;
			this.maxDepth = maxDepth;
			this.crr = crr;
		}
		@Override
		public String toString() {
			return "Node [id=" + id + ", color=" + color + ", maxDepth=" + maxDepth + ", pid=" + pid + ", crr="
					+ Arrays.toString(crr) + "]";
		}
		
		
	}

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		nrr = new Node[MAX];
		child = new ArrayList[MAX];
		for(int i=0; i<MAX; i++) {
			child[i] = new ArrayList<>();
		}
		
		int N = Integer.parseInt(st.nextToken());
		for(int t=0; t<N; t++) {
			st = new StringTokenizer(br.readLine());
			int type = Integer.parseInt(st.nextToken());
			if(type == 100) {
				// 노드 추가
				int id = Integer.parseInt(st.nextToken());
				int pid = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				int maxDepth = Integer.parseInt(st.nextToken());
//				System.out.println("addNode");
				addNode(id, pid, color, maxDepth);
				
				
			} else if(type == 200) {
				// 색깔 변경
				int id = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
//				System.out.println("changeColor");
				changeColor(id, color);
				
			} else if(type == 300) {
				// 색깔 조회
				int id = Integer.parseInt(st.nextToken());
//				System.out.println("getColor");
				int color = getColor(id);
				System.out.println(color);
			} else if(type == 400) {
				// 점수 조회
//				System.out.println("getScore");
				int score = getScore();
				
				System.out.println(score);
			}
			
//			for(int i=1; i<MAX; i++) {
//				System.out.println(nrr[i]);
//			}
//			System.out.println();
			
		}
		
	}
	public static void canAdd(int pid, int depth) {
		if(pid == -1) {
			flag = true;
			return;
		}
		if(nrr[pid].maxDepth >= depth) {
			canAdd(nrr[pid].pid, depth+1);
		}
	}
	public static void increaseColor(int pid, int color) {
		if(pid == -1) return;
		
		nrr[pid].crr[color]++;
		increaseColor(nrr[pid].pid, color);
		
	}
	public static void addNode(int id, int pid, int color, int maxDepth) {
		// 최상위 루트노드
		if(pid == -1) {
			int[] crr = new int[COLOR+1];
			crr[color]++;
			nrr[id] = new Node(id, pid, color, maxDepth, crr);
			return;
		}
		
		flag = false;
		canAdd(pid, 2);
		if(flag) {
			// 노드 추가
			int[] crr = new int[COLOR+1];
			crr[color]++;
			nrr[id] = new Node(id, pid, color, maxDepth, crr);
			// 부모 노드에 자식으로 추가
			child[pid].add(id);
			// 부모 노드의 자식 색깔 카운트 증가
			increaseColor(pid, color);
		}
	}
	public static void changeChild(int id, int color) {
		// 나 바꾸기
		int[] crr = nrr[id].crr;
		int cnt = 0;
		for(int i=1; i<COLOR+1; i++) {
			cnt += crr[i];
		}
		int[] nCrr = new int[COLOR+1];
		nCrr[color] = cnt;
		nrr[id].crr = nCrr;
		nrr[id].color = color;
		
		// 자식 바꾸기
		List<Integer> cList = child[id];
		for(int c: cList) {
			changeChild(c, color);
		}
		
	}
	public static void changeParent(int pid, int[] orr, int[] changed) {
		if(pid == -1) return;
		
		int[] ori = new int[COLOR+1];
		int[] tmp = new int[COLOR+1];
		for(int i=0; i<COLOR+1; i++) {
			tmp[i] = nrr[pid].crr[i];
			ori[i] = nrr[pid].crr[i];
		}

		// 변경된 자식
		for(int i=1; i<COLOR+1; i++) {
			tmp[i] -= orr[i];
		}
		// 최신 반영
		for(int i=1; i<COLOR+1; i++) {
			tmp[i] += changed[i];
		}
		nrr[pid].crr = tmp;
		
		// 나머지 변경 안된 자식
		changeParent(nrr[pid].pid, ori, nrr[pid].crr);
		
	}
	public static void changeColor(int id, int color) {		
		// 원본
		int[] orr = new int[COLOR+1];
		for(int i=0; i<COLOR+1; i++) {
			orr[i] = nrr[id].crr[i];
		}
		// 나와 자식 컬러 바꾸기
		changeChild(id, color);
		
		// 부모들 컬러카운트 조정하기 (부모id, 원본, 변경본)
		changeParent(nrr[id].pid, orr, nrr[id].crr);
		
	}
	public static int getColor(int id) {
		return nrr[id].color;
	}
	public static int calScore(int[] crr) {
		int cnt = 0;
		for(int i=1; i<COLOR+1; i++) {
			if(crr[i] != 0) cnt++;
		}
		return cnt;
	}
	public static int getScore() {
		int total = 0;
		for(int i=0; i<MAX; i++) {
			if(nrr[i] != null) {
				int cnt = calScore(nrr[i].crr);
				total += (cnt*cnt);
			}
		}
		return total;
	}

}