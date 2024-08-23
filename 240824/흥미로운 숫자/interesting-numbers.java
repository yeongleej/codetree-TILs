import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long X = scanner.nextLong();
        long Y = scanner.nextLong();
        scanner.close();

        System.out.println(findInterestingNumbers(X, Y));
    }

    private static int findInterestingNumbers(long X, long Y) {
        Set<Long> interestingNumbers = new HashSet<>();

        // 각 자릿수의 길이를 결정
        for (int length = String.valueOf(X).length(); length <= String.valueOf(Y).length(); length++) {
            for (char digit = '0'; digit <= '9'; digit++) {
                generateInterestingNumbers(X, Y, length, digit, interestingNumbers);
            }
        }

        return interestingNumbers.size();
    }

    private static void generateInterestingNumbers(long X, long Y, int length, char digit, Set<Long> interestingNumbers) {
        // 모든 자릿수가 같은 숫자 생성
        char[] numArray = new char[length];
        for (int i = 0; i < length; i++) {
            numArray[i] = digit;
        }

        // 하나의 자릿수만 다르게 만들어 흥미로운 숫자 생성
        for (int i = 0; i < length; i++) {
            for (char newDigit = '0'; newDigit <= '9'; newDigit++) {
                if (newDigit == digit) continue;

                numArray[i] = newDigit;
                long number = Long.parseLong(new String(numArray));

                // 범위 체크
                if (number >= X && number <= Y && String.valueOf(number).length() == length) {
                    interestingNumbers.add(number);
                }
            }
            numArray[i] = digit; // 원상복구
        }
    }
}