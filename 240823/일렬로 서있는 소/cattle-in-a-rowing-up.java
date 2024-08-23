import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        List<Integer> nList = new ArrayList<>();
        for(int i=0; i<N; i++){
            nList.add(Integer.parseInt(br.readLine()));
        }

        Collections.sort(nList);
        // System.out.println(nList);
        int ans = 0;
        for(int x=0; x<N-2; x++){
            for(int y=x+1; y<N-1; y++){
                for(int z=y+1; z<N; z++){
                    if(inRange(nList.get(x), nList.get(y), nList.get(z))){
                        // System.out.println(x+", "+y+", "+z);
                        ans++;
                    }
                }
            }
        }
        System.out.println(ans);
    }
    public static boolean inRange(int x, int y, int z){
        return (y-x) <= (z-y) && (z-y) <= 2*(y-x);
    }
}