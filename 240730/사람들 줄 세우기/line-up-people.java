import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int M;
    static int K;
    static int[] arr;
    static int[] mrr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        arr = new int[N+1];
        mrr = new int[M];
        st = new StringTokenizer(br.readLine());
        for(int i=0; i<M; i++){
            mrr[i] = Integer.parseInt(st.nextToken());
        }

        int eIdx = 0;
        int fCnt = 0;
        for(int i=0; i<K; i++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int pos = Integer.parseInt(st.nextToken());
            arr[pos] = n;
            for(int j=0; j<M; j++){
                if(n == mrr[j] && eIdx < pos){
                    eIdx = pos;
                    fCnt = j;
                }
            }
        }
        // System.out.println(Arrays.toString(arr));
        // System.out.println(Arrays.toString(mrr));
        // System.out.println(eIdx+", "+fCnt);

        int empty = 0;
        for(int i=1; i<eIdx; i++) {
            if(arr[i] == 0){
                empty++;
            } else{
                for(int j=0; j<M; j++){
                    if(mrr[j] == arr[i]){
                        fCnt--;
                        break;
                    }
                }
            }
        }
        int start = 0;
        if(Math.abs(empty-fCnt) > 0){
            start = 1;
        } else{
            start = eIdx+1;
        }
        int ans = 0;
        for(int i=start; i<N+1; i++) {
            if(arr[i] == 0){
                ans = i;
                break;
            }
        }
        System.out.println(ans);
    }
}