from collections import deque
import copy
import sys
input = sys.stdin.readline

# 상하좌우
dx = [-1, 1, 0, 0]
dy = [0, 0, -1, 1]

N, M, K = map(int, input().split())
g = [list(map(int, input().split())) for _ in range(N)]
pList = []
for _ in range(M):
    x, y = map(int, input().split())
    pList.append((x-1, y-1))
ex, ey = map(int, input().split())
ex -= 1
ey -= 1
dist = 0

def in_range(x, y):
    return 0<=x<N and 0<=y<N

def find_pList():
    global pList
    pList = []
    for i in range(N):
        for j in range(N):
            if g[i][j] < 0:
                pList.append((i, j))

def move():
    global dist, pList, ex, ey
    nList = []
    # print("b :", pList)
    mCnt = 0
    for px, py in pList:
        g[px][py] = 0 # 사람 감소
        isMove = False
        now_dist = abs(px-ex)+abs(py-ey)
        for i in range(4):
            nx = px + dx[i]
            ny = py + dy[i]
            if in_range(nx, ny) and g[nx][ny] <= 0:
                # 다음 위치가 출구임 : 탈출
                if nx == ex and ny == ey:
                    isMove = True
                    mCnt += 1
                    break
                next_dist = abs(nx-ex)+abs(ny-ey)
                # 현재 위치보다 다음 위치가 가까움
                if now_dist > next_dist:
                    nList.append((nx, ny))
                    mCnt += 1
                    isMove = True
                    break
        if not isMove:
            nList.append((px, py))
    
    # print("mCnt:", mCnt)
    dist += mCnt
    pList = nList
    # print("a:", pList)
    # 사용자 동시에 움직이기
    for px, py in pList:
        g[px][py] -= 1
        
def find_square():
    global ex, ey
    # 출구를 시작점으로 주변의 사람 찾기
    visited = [[False]*N for _ in range(N)]
    visited[ex][ey] = True
    q = deque([(ex ,ey)])
    # 참가자 위치
    px, py = -1, -1
    while q:
        x, y = q.popleft()
        if g[x][y] < 0:
            px, py = x, y
            break
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            if in_range(nx, ny) and not visited[nx][ny]:
                visited[nx][ny] = True
                q.append((nx, ny))
    
    # 정사각형 한변의 길이
    s = max(abs(px-ex)+1, abs(py-ey)+1)
    # 정사각형 좌상단
    sx, sy = -1, -1
    for i in range(N):
        isBreak = False
        for j in range(N):
            if i<=ex<=i+(s-1) and i<=px<=i+(s-1) and j<=ey<=j+(s-1) and j<=py<=j+(s-1):
                sx, sy = i, j
                isBreak = True
                break
        if isBreak:
            break
    return sx, sy, s


def rotate(sx, sy, s):
    global ex, ey
    ng = [[0]*N for _ in range(N)]
    for i in range(N):
        for j in range(N):
            ng[i][j] = g[i][j]

    # 회전 진행
    cx, cy = 0, 0
    for i in range(sx, sx+s):
        for j in range(sy, sy+s):
            ox, oy = i-sx, j-sy
            nx, ny = oy, (s-1)-ox
            ng[nx+sx][ny+sy] = g[i][j]
            # 출구 위치 갱신
            if i == ex and j == ey:
                cx, cy = nx+sx, ny+sy
    ex, ey = cx, cy
    
    # 벽 내구성 감소 및 pList 갱신
    for i in range(sx, sx+s):
        for j in range(sy, sy+s):
            if ng[i][j] > 0:
                ng[i][j] -= 1
    
    # 원본 변경
    for i in range(N):
        for j in range(N):
            g[i][j] = ng[i][j]

def rotate_square(sx, sy, square_size):
    global ex, ey
    ng = [[0]*N for _ in range(N)]
    for i in range(N):
        for j in range(N):
            ng[i][j] = g[i][j]
    # 우선 정사각형 안에 있는 벽들을 1 감소시킵니다.
    for x in range(sx, sx + square_size):
        for y in range(sy, sy + square_size):
            if g[x][y]: 
                g[x][y] -= 1

    # 정사각형을 시계방향으로 90' 회전합니다.
    for x in range(sx, sx + square_size):
        for y in range(sy, sy + square_size):
            # Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행합니다. 
            ox, oy = x - sx, y - sy
            # Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) . (y, square_n - x - 1)가 됩니다.
            rx, ry = oy, square_size - ox - 1
            # Step 3. 다시 (sx, sy)를 더해줍니다.
            ng[rx + sx][ry + sy] = g[x][y]

    # next_board 값을 현재 board에 옮겨줍니다.
    for x in range(sx, sx + square_size):
        for y in range(sy, sy + square_size):
            g[x][y] = ng[x][y]
    
    # 출구에도 회전을 진행합니다.
    if sx <= ex and ex < sx + square_size and sy <= ey and ey < sy + square_size:
        # Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행합니다. 
        ox, oy = ex - sx, ey - sy
        # Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) . (y, square_n - x - 1)가 됩니다.
        rx, ry = oy, square_size - ox - 1
        # Step 3. 다시 (sx, sy)를 더해줍니다.
        ex, ey = rx + sx, ry + sy

t = 1            
while t <= K:
    if len(pList) == 0:
        break
    # 참가자 이동
    move()

    # print("이동후")
    # print(ex, ey)
    # for i in range(N):
    #     print(g[i])


    # 정사각형 찾기
    sx, sy, s = find_square()
    # print(sx, sy, s)
    # 회전
    # rotate(sx, sy, s)
    rotate_square(sx, sy, s)

    # print(ex, ey)
    # for i in range(N):
    #     print(g[i])
    # print("time:",t, "dist:", dist)
    
    find_pList()
    t += 1

print(dist)
print(ex+1, ey+1)