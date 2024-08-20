import java.util.*;
import java.io.*;

public class Main {
    
    static String ans;
    static String S;
    static String T;
    static int N;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        S = br.readLine();
        T = br.readLine();
        N = T.length();

        // findStr(S);
        // System.out.println(ans);
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<S.length(); i++){
            sb.append(S.charAt(i));
            if(S.charAt(i) == T.charAt(N-1)){
                int sLen = sb.length();
                // System.out.println("i:"+i+", sb:"+sb);
                // System.out.println("substring=> "+sb.substring(sLen-N, sLen));
                if(sb.length() >= N && sb.substring(sLen-N, sLen).equals(T)){
                    // System.out.println("delete substring");
                    sb.delete(sLen-N, sLen);
                }
            }
        }
        
        System.out.println(sb.toString());

    }
    public static void findStr(String str){
        boolean isFind = true;
        for(int i=0; i<str.length()-N+1; i++){
            // System.out.println(str.substring(i, i+N));
            if(str.substring(i, i+N).equals(T)){
                // String nStr = str.substring(0, i)+str.substring(i+N);
                findStr(str.substring(0, i)+str.substring(i+N));
                isFind = false;
                break;
            }
        }
        if(isFind){
            ans = str;
        }
    }
}