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
        
        for (int length = String.valueOf(X).length(); length <= String.valueOf(Y).length(); length++) {
            for (char digit = '0'; digit <= '9'; digit++) {
                generateInterestingNumbers(X, Y, length, digit, interestingNumbers);
            }
        }

        return interestingNumbers.size();
    }

    private static void generateInterestingNumbers(long X, long Y, int length, char digit, Set<Long> interestingNumbers) {
        // 같은 숫자로만 이루어진 수 생성
        char[] numArray = new char[length];
        for (int i = 0; i < length; i++) {
            numArray[i] = digit;
        }

        // 하나의 자릿수만 다른 숫자로 바꿔서 흥미로운 숫자 생성
        for (int i = 0; i < length; i++) {
            for (char newDigit = '0'; newDigit <= '9'; newDigit++) {
                if (newDigit == digit) continue;

                char originalDigit = numArray[i];
                numArray[i] = newDigit;
                
                long number = Long.parseLong(new String(numArray));
                
                if (number >= X && number <= Y) {
                    interestingNumbers.add(number);
                }
                
                numArray[i] = originalDigit; // 원래 자리로 복구
            }
        }
    }
}