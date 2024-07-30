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
        for(int i=0; i<M; i++) {
            mrr[i] = Integer.parseInt(st.nextToken());
        }

        for(int i=0; i<K; i++) {
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            int pos = Integer.parseInt(st.nextToken());
            arr[pos] = num;
        } 

        // System.out.println(Arrays.toString(arr));
        // System.out.println(Arrays.toString(mrr));
        int ans = 0;
        for(int i=1; i<N+1; i++) {
            if(arr[i]==1){
                ans = i;
                break;
            } else if(arr[i]==0){
                arr[i] = 1;
                // System.out.println(Arrays.toString(arr));
                if(isCheck()){
                    ans = i;
                    break;
                }
                arr[i] = 0;
            }
        }
        System.out.println(ans);
    }
    public static boolean isCheck() {
        // int pre = 1;
        int[] irr = new int[M];
        
        // arr 복사
        int[] trr = new int[N+1];
        for(int i=1; i<N+1; i++){
            trr[i] = arr[i];
        }

        for(int i=0; i<M; i++) {
            boolean flag = true;
            for(int j=1; j<N+1; j++){
                if(trr[j]==mrr[i]){
                    irr[i] = j;
                    break;
                } else if(trr[j] == 0 && flag){
                    irr[i] = j;
                    flag = false;
                }
            }
            trr[irr[i]] = mrr[i];
        }
        // System.out.println(Arrays.toString(trr));
        // System.out.println(Arrays.toString(irr));
        for(int i=0; i<M-1; i++){
            if(irr[i] > irr[i+1]){
                return false;
            }
        }
        return true;
    }
}