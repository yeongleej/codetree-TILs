import java.util.*;
import java.io.*;
public class Main {
    
    static int N, K;
    static int[] srr;
    static int[] prr;
    static class Person {
        int pos;
        public Person(int pos){
            this.pos = pos;
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
        // boolean[] visited = new boolean[2*N];
        
        prr = new int[2*N];     // prr[i] = i칸에 있는 사람의 이동횟수

        // Queue<Person> q = new LinkedList<>();
        while(true){
            // System.out.println("t:"+test+" s:"+start);
            // System.out.println(Arrays.toString(prr));
            // System.out.println(Arrays.toString(srr));

            // # 무빙워크 회전 + 사람들 이동카운트 조정
            start = ((start-1)+M) % M;

            for(int i=0; i<M; i++){
                // 무빙워크 끝에 위치한 사람 out
                if(prr[i]+1 == N){
                    prr[i] = 0;
                }
                else if(prr[i] > 0){
                    prr[i]++;
                }
            }

            // System.out.println("무빙워크 회전후");
            // System.out.println(Arrays.toString(prr));
            // System.out.println(Arrays.toString(srr));
            // # 사람이동 + 안정성 확인
            boolean isContinue = true;
            for(int i=0; i<M; i++){
                int next = (i+1) % M;
                // 사람이동
                if(prr[i]!=0 && prr[next]==0 && srr[next] > 0){
                    prr[next] = prr[i] + 1;
                    srr[next]--;
                    prr[i] = 0;
                    if(prr[next] == N) prr[next] = 0;   // out
                    if(srr[next] == 0) fail++;
                    if(fail >= K) {
                        isContinue = false;
                        break;
                    }
                }
            }
            // System.out.println("사람이동 후");
            // System.out.println(Arrays.toString(prr));
            // System.out.println(Arrays.toString(srr));
            if(!isContinue || fail >= K) break;


            // # 사람 증가 + 안정성 확인
            if(prr[start] == 0 && srr[start] > 0){
                prr[start] = 1;
                srr[start]--;
                if(srr[start] == 0) fail++;
            }
            if(fail >= K) break;

            // 다음 테스트 진행
            test++;
        }
        // System.out.println(Arrays.toString(prr));
        // System.out.println(Arrays.toString(srr));

        System.out.println(test);
    }
}