import java.util.*;
import java.io.*;

public class Main {
	
	static int INF = Integer.MAX_VALUE;
	static int Q, N, M;
	static List<Node>[] g;
	static int[] distance;
	static List<Product> pList;
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
		int id, revenue, dest, cost;
		public Product(int id, int revenue, int dest, int cost) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
			this.cost = cost;
		}
		
		@Override
		public int compareTo(Product other) {
			if((this.revenue - this.cost) == (other.revenue-other.cost)) {
				return this.id - other.id;
			}
			return (other.revenue-other.cost) - (this.revenue - this.cost);
		}

		@Override
		public String toString() {
			return "Product [id=" + id + ", revenue=" + revenue + ", dest=" + dest + ", cost=" + cost + "]";
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		Q = Integer.parseInt(st.nextToken());
		pList = new ArrayList<>();
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
				pList.add(new Product(id, revenue, dest, distance[dest]));
				
			} else if(type == 300) {
				// 여행상품 취소(삭제)
				int id = Integer.parseInt(st.nextToken());
//				System.out.println("delete:"+id+" "+pList);
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
		List<Product> tList = new ArrayList<>();
		for(Product p: pList) {
			if(p.id == id) continue;
			tList.add(p);
		}
		pList = tList;
		
	}
	public static void sellProduct() {
		Collections.sort(pList);
		
		int ans = -1;
		for(int i=0; i<pList.size(); i++) {
			Product now = pList.get(i);
//			System.out.println(">> "+now);
			if(now.cost == INF) continue;
			if(now.revenue - now.cost >= 0) {
				ans = now.id;
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
		List<Product> tList = new ArrayList<>();
		for(Product p: pList) {
			tList.add(new Product(p.id, p.revenue, p.dest, distance[p.dest]));
		}
		
		pList = tList;
	}

}