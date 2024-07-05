import java.util.*;
import java.io.*;

public class Main {
    
    static int N;
    static int M;
    static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        arr = new int[N+1];
        st = new StringTokenizer(br.readLine());
        for(int i=1; i<N+1; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // 누적합 계산
        for(int i=1; i<N+1; i++) {
            arr[i] += arr[i-1];
        }

        // 구간 최대합 구하기
        int mAns = Integer.MIN_VALUE;
        int mCnt = 0;
        for(int i=1; i<N-M+2; i++){
            int pSum = arr[i+M-1] - arr[i-1];
            if (pSum > mAns){
                mAns = pSum;
                mCnt = 0;
            }
            if(pSum == mAns) {
                mCnt++;
            }
        }
        System.out.println(mAns);
        if(mAns != 0){
            System.out.println(mCnt);
        }
    }
}