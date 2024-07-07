import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int T;
    static HashMap<String, Integer> wMap;
    static List<String> wList;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        wMap = new HashMap<>();
        wList = new ArrayList<>();
        for(int i=1; i<N+1; i++) {
            st = new StringTokenizer(br.readLine());
            String word = st.nextToken();
            wMap.put(word, i);
            wList.add(word);
        }
        Collections.sort(wList);

        // System.out.println(wMap);
        // System.out.println(wList.get(86));

        // System.out.println("aab".compareTo("aaa")); 1
        // System.out.println("aaa".compareTo("aab")); -1
        // System.out.println(wList.get(0).substring(0,2).equals("aa"));

        for(int t=0; t<T; t++) {
            st = new StringTokenizer(br.readLine());
            int k = Integer.parseInt(st.nextToken());
            String prefix = st.nextToken();

            findPos(k, prefix);
        }

    }

    public static void findPos(int k, String pWord) {
        int s = 0;
        int e = N;
        List<String> pList = new ArrayList<>();

        while(s <= e) {
            int mid = (s+e) / 2;

            // pWord가 더 큼 : e를 줄임
            if(wList.get(mid).compareTo(pWord) >= 0) {
                e = mid - 1;
            } else{
                s = mid + 1;
            }
        }
        // System.out.println(s + " "+ e);

        int pLen = pWord.length();
        int idx = s + k - 1;
        // System.out.println(idx);
        if(idx < N && wList.get(idx).length() >= pLen 
                && wList.get(idx).substring(0,pLen).equals(pWord)) {
            System.out.println(wMap.get(wList.get(idx)));
        } else{
            System.out.println(-1);
        }

    }
}