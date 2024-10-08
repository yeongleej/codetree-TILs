import java.util.*;
import java.io.*;

public class Main {
	
	final static int INF = 0x7fffffff;
	static int Q, N, M;
	static List<Node>[] g;
	static boolean[] isDelete;
	static boolean[] isMade;
	static int[] distance;
	static PriorityQueue<Product> pList;
	static class Node implements Comparable<Node> {
		int u, dist;
		public Node(int u, int dist) {
			this.u = u;
			this.dist = dist;
		}
		@Override
		public int compareTo(Node other) {
			return this.dist - other.dist;
		}
		@Override
		public String toString() {
			return "[u=" + u + ", dist=" + dist + "]";
		}
		
	}
	static class Product implements Comparable<Product> {
		int id, revenue, dest, profit;
		public Product(int id, int revenue, int dest, int profit) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
			this.profit = profit;
		}
		
		@Override
		public int compareTo(Product other) {
			if(this.profit == other.profit) {
				return this.id - other.id;
			}
			return other.profit - this.profit;
		}

		@Override
		public String toString() {
			return "Product [id=" + id + ", revenue=" + revenue + ", dest=" + dest + ", profit=" + profit + "]";
		}
		
		
		
	}
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("src/day1007/코드트리투어.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		Q = Integer.parseInt(st.nextToken());
		pList = new PriorityQueue<>();
		isDelete = new boolean[30001];
		isMade = new boolean[30001];
		for(int q=0; q<Q; q++) {
			st = new StringTokenizer(br.readLine());
			int type = Integer.parseInt(st.nextToken());
			
			if(type == 100) {
				// 랜드 건설
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				g = new ArrayList[N];
				for(int i=0; i<N; i++) {
					g[i] = new ArrayList<>();
				}
				
				for(int i=0; i<M; i++) {
					int v = Integer.parseInt(st.nextToken());
					int u = Integer.parseInt(st.nextToken());
					int e = Integer.parseInt(st.nextToken());
					g[v].add(new Node(u, e));
					g[u].add(new Node(v, e));
				}
				
				// 다익스트라로 출발점(처음은 0)으로부터의 최단거리 구함
				distance = new int[N];
				for(int i=0; i<N; i++) {
					distance[i] = INF;
				}
				dijkstra(0);
//				System.out.println(Arrays.toString(distance));
				
			} else if(type == 200) {
				// 여행상품 생성
				int id = Integer.parseInt(st.nextToken());
				int revenue = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				
				isMade[id] = true;
				pList.add(new Product(id, revenue, dest, revenue-distance[dest]));
				
			} else if(type == 300) {
				// 여행상품 취소(삭제)
				int id = Integer.parseInt(st.nextToken());
//				System.out.println("delete:"+id);
				deleteProduct(id);

				
			} else if(type == 400) {
				// 최적의 상품 판매, 없으면 -1 출력
//				System.out.println(pList);
				
				sellProduct();
				
			} else if (type == 500) {
				// 출발지 변경
				int ns = Integer.parseInt(st.nextToken());
				
				// 다익스트라로 새로운 출발점으로부터의 최단거리 구함
				distance = new int[N];
				for(int i=0; i<N; i++) {
					distance[i] = INF;
				}
				dijkstra(ns);
//				System.out.println(Arrays.toString(distance));
				
				// 거리 재정비
				refreshProduct();
			}
		}

	}
	public static void dijkstra(int start) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		pq.add(new Node(start, 0));
		distance[start] = 0;
		
		while(!pq.isEmpty()) {
			Node now = pq.poll();
			// 이미 처리된 노드 pass
			if(now.dist > distance[now.u]) continue;
			for(Node next : g[now.u]) {
				int cost = now.dist + next.dist;
				if(cost < distance[next.u]) {
					distance[next.u] = cost;
					pq.add(new Node(next.u, cost));
				}
			}
		}
	}
	public static void deleteProduct(int id) {
		if(isMade[id]) isDelete[id] = true;
	}
	public static void sellProduct() {
		int ans = -1;
		int size = pList.size();

		while(!pList.isEmpty()) {
			Product p = pList.peek();
			if(p.profit < 0) break;
			
			pList.poll();
			if(!isDelete[p.id]) {
				ans = p.id;
				break;
			}
		}
		// 정답 출력
		System.out.println(ans);
		
		// 판매가능한 상품이 있다면 삭제
		if(ans != -1) {
			deleteProduct(ans);
		}
		
	}
	public static void refreshProduct() {
		PriorityQueue<Product> npq = new PriorityQueue<>();
		while(!pList.isEmpty()) {
			Product now = pList.poll();
			if(!isDelete[now.id]) {
				now.profit = now.revenue - distance[now.dest];
				npq.add(now);
			}
		}
		
		pList = npq;
		
	}

}