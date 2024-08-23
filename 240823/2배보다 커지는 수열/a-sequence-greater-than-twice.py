MOD = 1000000007

def count_sequences(n, m):
    dp = [[0] * (m + 1) for _ in range(n + 1)]
    
    # 1개의 숫자를 선택하는 경우, 모든 j에 대해 1로 초기화
    for j in range(1, m + 1):
        dp[1][j] = 1
    
    # DP 배열 채우기
    for i in range(2, n + 1):
        for j in range(1, m + 1):
            # dp[i][j]는 dp[i-1][k]들의 합 (k <= j/2)
            dp[i][j] = sum(dp[i-1][k] for k in range(1, j//2 + 1)) % MOD
    
    # n개의 숫자를 선택하는 경우의 수 합산
    return sum(dp[n][j] for j in range(1, m + 1)) % MOD

# 입력 받기
n, m = map(int, input().split())
result = count_sequences(n, m)
print(result)