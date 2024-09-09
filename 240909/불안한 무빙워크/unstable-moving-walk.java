import java.util.*;
import java.io.*;
public class Main {
    
    static int N, K;
    static int[] srr;
    static class Person {
        int pos, cnt;
        public Person(int pos, int cnt){
            this.pos = pos;
            this.cnt = cnt;
        }
    }

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        srr = new int[2*N];
        st = new StringTokenizer(br.readLine());
        for(int i=0; i<2*N; i++){
            srr[i] = Integer.parseInt(st.nextToken());
        }
        // System.out.println(Arrays.toString(srr));

        int M = 2*N;
        int test = 1;
        int fail = 0;
        int start = 0;
        boolean[] visited = new boolean[2*N];
        // start = ((start-1)+M) % M;
        // System.out.println(start);
        Queue<Person> q = new LinkedList<>();
        while(true){
            // System.out.println("t:"+test);
            // System.out.println(Arrays.toString(visited));
            // System.out.println(Arrays.toString(srr));
            // 무빙워크 회전 + 사람들 이동카운트 조정
            start = ((start-1)+M) % M;
            int size = q.size();
            for(int i=0; i<size; i++){
                Person now = q.poll();
                now.cnt++;
                if(now.cnt == N) continue;
                q.add(now);
            }

            // 사람이동 + 안정성 확인
            size = q.size();
            for(int i=0; i<size; i++){
                Person now = q.poll();
                int next = (now.pos+1) % M;
                if(!visited[next] && srr[next] > 0){
                    visited[now.pos] = false;
                    visited[next] = true;
                    now.pos = next;
                    now.cnt++;
                    srr[next]--;
                    if(srr[next] == 0) fail++;
                }
                if(now.cnt == N) continue;
                q.add(now);
            }
            if(fail >= K) break;

            // 사람 증가 + 안정성 확인
            if(!visited[start] && srr[start] > 0){
                q.add(new Person(start, 1));
                visited[start] = true;
                srr[start]--;
                if(srr[start] == 0) fail++;
            }

            if(fail >= K) break;

            // 다음 테스트 진행
            test++;
        }
        // System.out.println(Arrays.toString(visited));
        // System.out.println(Arrays.toString(srr));

        System.out.println(test);
    }
}