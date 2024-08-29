import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(br.readLine());
		StringTokenizer st = new StringTokenizer(br.readLine());
		int arr[] = new int[N];
		for (int i = 0; i < N; i++)
			arr[i] = Integer.parseInt(st.nextToken());
		Arrays.sort(arr);
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				int snowMan1 = arr[i] + arr[j];
				int start = 0;
				int end = N - 1;
				while (start < end) {
					if (start == i || start == j) {
						start++;
						continue;
					}
					if (end == i || end == j) {
						end--;
						continue;
					}
					int snowMan2 = arr[start] + arr[end];
					min = Math.min(min, Math.abs(snowMan1 - snowMan2));
					if (snowMan1 > snowMan2)
						start++;
					else if (snowMan1 < snowMan2)
						end--;
					else {
						System.out.println(0);
						return;
					}
				}
			}
		}
		System.out.println(min);
	}
}