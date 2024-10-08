import java.util.*;
import java.io.*;
public class Main {

    static int N, T, K;
    static Map<String, Integer> sMap;
    static String[] srr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());
        
        sMap = new HashMap<>();
        srr = new String[N];
        for(int i=0; i<N; i++) {
            String str = br.readLine();
            srr[i] = str;
            sMap.put(str, i+1);
        }
        Arrays.sort(srr);
        // System.out.println(Arrays.toString(srr));

        for(int t=0; t<T; t++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            String word = st.nextToken();

            int idx = bSearch(word);
            // System.out.println(idx);
            // substring(0, word.length())
            if(idx!= -1 && idx+n < N 
                && srr[idx+n-1].substring(0, word.length()).equals(word)) {
                System.out.println(sMap.get(srr[idx+n-1]));
            } else {
                System.out.println(-1);
            }
        }

    }
    public static int bSearch(String word) {
        int s = 0;
        int e = srr.length-1;

        int ans = -1;
        while(s <= e) {
            int mid = (s+e) / 2;
            if(srr[mid].compareTo(word) >= 0) {
                ans = mid;
                e = mid - 1;
            } else {
                s = mid + 1;
            }
        }
        return ans;
    }
}