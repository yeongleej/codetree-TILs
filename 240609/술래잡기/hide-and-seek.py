from collections import deque
n, m, h, k = map(int, input().split())

# -1 : 술래
# 1~m : 도망자 번호
graph = [[0]*n for _ in range(n)]
graph[n//2][n//2] = -1
trees = [[False]*n for _ in range(n)]
mrr = deque([])
mdir = [[[0, 1], [0, -1]], [[1, 0], [-1, 0]]]
# 도망자 입력
for i in range(1, m+1):
    x, y, d = map(int, input().split())
    graph[x-1][y-1] = i
    mrr.append((x-1, y-1, d-1, 0))
# 나무 입력
for _ in range(h):
    x, y = map(int, input().split())
    trees[x-1][y-1] = True

# 터닝 포인트
sDir = [[[-1, 0, 1, 0], [0, 1, 0, -1]], [[1, 0, -1, 0], [0, 1, 0, -1]]]

turn = [[False]*n for _ in range(n)]
visited = [[False]*n for _ in range(n)]
x, y= 0, 0
turn[x][y] = True
visited[x][y] = True
sd = 0
ex = n // 2
ey = n // 2
while True:
    nx = x + sDir[1][0][sd]
    ny = y + sDir[1][1][sd]
    if 0>nx or nx>=n or 0>ny or ny>=n or visited[nx][ny]:
        turn[x][y] = True
        sd = (sd + 1) % 4
        nx = x + sDir[1][0][sd]
        ny = y + sDir[1][1][sd]
    visited[nx][ny] = True
    x = nx
    y = ny
    if x == n//2 and y == n//2:
        turn[x][y] = True
        break

# for i in range(n):
#     print(turn[i])


score = 0
sx = n // 2
sy = n // 2
st = 0
sd = 0

# print("[초기]")
# for i in range(n):
#     print(graph[i])

for i in range(1, k+1): 
    mCnt = len(mrr)
    # 도망자 턴
    for _ in range(mCnt):
        # md : 좌우, 상하
        # d : 실제 dir
        mx, my, md, d = mrr.popleft()
        if graph[mx][my] == 0:
            continue
        if abs(sx-mx)+abs(sy-my) > 3:
            continue
        nx = mx + mdir[md][d][0]
        ny = my + mdir[md][d][1]
        if 0<=nx<n and 0<=ny<n:
            if graph[nx][ny] != -1:
                num = graph[mx][my]
                graph[mx][my] = 0
                graph[nx][ny] = num
                mrr.append((nx, ny, md, d))
        # 방향 전환
        else:
            # print("방향 전환")
            d = abs(d-1)
            nx = mx + mdir[md][d][0]
            ny = my + mdir[md][d][1]
            if graph[nx][ny] != -1:
                num = graph[mx][my]
                graph[mx][my] = 0
                graph[nx][ny] = num
                mrr.append((nx, ny, md, d))
    # print(">> 도망자 이동 후")
    # for j in range(n):
    #     print(graph[j])

    # 술래 턴(움직이기 -> 방향 틀 시기면 틀기 -> 거리 3 도망자 잡기)
    tx = sx + sDir[st][0][sd]
    ty = sy + sDir[st][1][sd]
    # 중앙 도착
    if tx == n//2 and ty == n//2:
        st = 0
        sd = 0
    # 도착점 도착
    elif tx == 0 and ty == 0:
        st = 1
        sd = 0
    elif turn[tx][ty]:
        sd = (sd + 1) % 4
    # 3칸 탐색
    kill = 0
    for j in range(3):
        rx = tx + j*sDir[st][0][sd]
        ry = ty + j*sDir[st][1][sd]
        if 0<=rx<n and 0<=ry<n and not trees[rx][ry] and graph[rx][ry] > 0:
            graph[tx][ty] = 0
            kill += 1
    # print("t:",i, "kill:", kill)
    score += (i * kill)
    graph[sx][sy] = 0
    sx = tx
    sy = ty
    graph[tx][ty] = -1

    # print(">> 술래 이동 후")
    # for j in range(n):
    #     print(graph[j])

print(score)