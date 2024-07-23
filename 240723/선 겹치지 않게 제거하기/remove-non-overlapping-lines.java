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
        // System.out.println(nums);

        dp = new int[N+1];
        for(int i=N-1; i>=0; i--) {
            int cnt = 0;
            for(int j=i-1; j>=0; j--){
                if(nums.get(i).b <= nums.get(j).b) {
                    cnt += 1;
                }
            }
            dp[i] = Math.max(dp[i+1], cnt);
        }
        System.out.println(dp[0]);




    }
}