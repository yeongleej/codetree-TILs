import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int M;
    static List<int[]> mList;
    static List<Node>[] g;
    static int[] distance;
    static int first;
    static int ans;

    static class Node implements Comparable<Node>{
        int dist, node;
        public Node(int dist, int node){
            this.dist = dist;
            this.node = node;
        }
        @Override
        public int compareTo(Node other){
            return this.dist - other.dist;
        }

        @Override
        public String toString(){
            return "node: "+this.node+" dist: "+this.dist;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        g = new ArrayList[N+1];
        distance = new int[N+1];
        for(int i=0; i<N+1; i++){
            g[i] = new ArrayList<>();
            distance[i] = Integer.MAX_VALUE;
        }
        
        mList = new ArrayList<>();
        for(int i=0; i<M; i++){
            st = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(st.nextToken());
            int n2 = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());
            int[] edge = {n1, n2, dist};
            mList.add(edge);
            g[n1].add(new Node(dist, n2));
            g[n2].add(new Node(dist, n1));
        }

        // for(int i=1; i<N+1; i++){
        //     System.out.println(g[i]);
        // }
        // for(int i=0; i<M; i++){
        //     System.out.println(Arrays.toString(mList.get(i)));
        // }

        dijkstra(1);
        first = distance[N];
        ans = 0;
        for(int i=0; i<M; i++){
            int[] edge = mList.get(i);
            // System.out.println(Arrays.toString(edge));
            // distance 초기화
            for(int j=1; j<N+1; j++){
                distance[j] = Integer.MAX_VALUE;
            }
            // n1 -> n2 간선 변경
            for(Node next: g[edge[0]]){
                if(next.node == edge[1]){
                    next.dist = edge[2]*2;
                    break;
                }
            }
            // n2 -> n1 간선 변경
            for(Node next: g[edge[1]]){
                if(next.node == edge[0]){
                    next.dist = edge[2]*2;
                    break;
                }
            }
            // for(int j=1; j<N+1; j++){
            //     System.out.println(g[j]);
            // }
            // 다익스트라 실행
            dijkstra(1);
            if(ans < distance[N]-first){
                ans = distance[N]-first;
            }

            // n1 -> n2 원상복귀
            for(Node next: g[edge[0]]){
                if(next.node == edge[1]){
                    next.dist = edge[2];
                    break;
                }
            }
            // n2 -> n1 원상복귀
            for(Node next: g[edge[1]]){
                if(next.node == edge[0]){
                    next.dist = edge[2];
                    break;
                }
            }

        }

        System.out.println(ans);

    }
    public static void dijkstra(int start){
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, start));
        distance[start] = 0;

        while(!pq.isEmpty()){
            Node now = pq.poll();
            if(now.dist > distance[now.node]) continue;

            for(Node next : g[now.node]){
                int cost = now.dist + next.dist;
                if(cost < distance[next.node]){
                    distance[next.node] = cost;
                    pq.add(new Node(cost, next.node));
                }
            }
        }
    }
}