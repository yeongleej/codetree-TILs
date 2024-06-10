from collections import deque
n, m, h, k = map(int, input().split())

# -1 : 술래
# 1~m : 도망자 번호
graph = [[0]*n for _ in range(n)]
graph[n//2][n//2] = -1
trees = [[False]*n for _ in range(n)]
hiders = [[[] for _ in range(n)] for _ in range(n)]
next_hiders = [[[] for _ in range(n)] for _ in range(n)]
# 도망자 방향(좌우, 상하)
hDir = [[0, 0, 1, -1],[-1, 1, 0, 0]]
# 도망자 입력
for i in range(1, m+1):
    x, y, d = map(int, input().split())
    hiders[x-1][y-1].append(d)

# 나무 입력
for _ in range(h):
    x, y = map(int, input().split())
    trees[x-1][y-1] = True

# 터닝 포인트
# 순방향(상우하좌), 역방향(하우상좌)
sDir = [[[-1, 0, 1, 0], [0, 1, 0, -1]], [[1, 0, -1, 0], [0, 1, 0, -1]]]
nowT = 0 # 순방향
sd = 0
sx, sy = n//2, n//2

turn = [[False]*n for _ in range(n)]
visited = [[False]*n for _ in range(n)]
x, y= 0, 0
turn[x][y] = True
visited[x][y] = True
d = 0
while True:
    nx = x + sDir[1][0][d]
    ny = y + sDir[1][1][d]
    if 0>nx or nx>=n or 0>ny or ny>=n or visited[nx][ny]:
        turn[x][y] = True
        d = (d + 1) % 4
        nx = x + sDir[1][0][d]
        ny = y + sDir[1][1][d]
    visited[nx][ny] = True
    x = nx
    y = ny
    if x == n//2 and y == n//2:
        turn[x][y] = True
        break

# for i in range(n):
#     print(turn[i])

# for i in range(n):
#     print(hiders[i])

def in_range(x, y):
    return 0<=x<n and 0<=y<n

def hider_move(x, y, mDir):
    nx = x + hDir[0][mDir]
    ny = y + hDir[1][mDir]
    # 방향 전환
    if not in_range(nx, ny):
        if mDir < 2:
            mDir = 1 - mDir
        else:
            mDir = 5 - mDir
        nx = x + hDir[0][mDir]
        ny = y + hDir[1][mDir]
    # 술래 있으면, 움직이지 않음
    if sx == nx and sy == ny:
        next_hiders[x][y].append(mDir)
    else:
        next_hiders[nx][ny].append(mDir)

def hiders_move_all():
    # 도망자들의 다음 위치 그래프 초기화
    for i in range(n):
        for j in range(n):
            next_hiders[i][j] = []
    # 도망자들 이동
    for i in range(n):
        for j in range(n):
            if abs(sx-i) + abs(sy-j) <= 3:
                for mDir in hiders[i][j]:
                    hider_move(i, j, mDir)
            else:
                for mDir in hiders[i][j]:
                    next_hiders[i][j].append(mDir)
    # 도망자 위치 최신화
    for i in range(n):
        for j in range(n):
            hiders[i][j] = next_hiders[i][j]

def seeker_move(t):
    global score, sx, sy, sd, nowT
    # 술래 이동
    nx = sx + sDir[nowT][0][sd]
    ny = sy + sDir[nowT][1][sd]
    # 술래 방향 전환
    if nx == 0 and ny == 0:
        nowT = 1
        sd = 0
    elif nx == n//2 and ny == n//2:
        nowT = 0
        sd = 0
    elif turn[nx][ny]:
        sd = (sd+1) % 4
    
    sx = nx
    sy = ny
    # 도망자 잡기
    for i in range(3):
        nx = sx + i*sDir[nowT][0][sd]
        ny = sy + i*sDir[nowT][1][sd]
        if in_range(nx, ny) and not trees[nx][ny]:
            score += (t * len(hiders[nx][ny]))
            hiders[nx][ny] = []
     
score = 0
for i in range(1, k+1):
    hiders_move_all()
    seeker_move(i)

print(score)