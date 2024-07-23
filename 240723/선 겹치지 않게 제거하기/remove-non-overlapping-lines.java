import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static List<State> nums;
    static int[] dp;

    static class State implements Comparable<State>{
        int a, b;
        public State(int a, int b){
            this.a = a;
            this.b = b;
        }
        @Override
        public int compareTo(State other) {
            if(this.a == other.a){
                return this.b-other.b;
            }
            return this.a - other.a;
        }
        @Override
        public String toString() {
            return "("+this.a+", "+this.b+")";
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        nums = new ArrayList<>();

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            nums.add(new State(a, b));
        }
        Collections.sort(nums);

        dp = new int[N];

        for(int i=0; i<N; i++) {
            dp[i] = 1;  // i번째 직선 선택

            for(int j=0; j<i; j++) {
                // i보다 아래에 있는 직선들 중에 포함 시킬 수 있는 직선의 개수 구함
                if(nums.get(i).b > nums.get(j).b){
                    dp[i] = Math.max(dp[i], dp[j]+1);
                }
            }
        }
        int maxLines = 0;
        for(int i=0; i<N; i++) {
            if(maxLines < dp[i]){
                maxLines = dp[i];
            }
        }
        System.out.println(N-maxLines);



    }
}