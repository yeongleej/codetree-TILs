import sys
from collections import deque
input = sys.stdin.readline
dx = [0, -1, 0, 1]
dy = [1, 0, -1, 0]

rx = [1, 0, -1, 0]
ry = [0, 1, 0, -1]

n, m, k = map(int, input().split())
g = [list(map(int, input().split())) for _ in range(n)]
teams = [[] for _ in range(m)]
visited = [[False]*n for _ in range(n)]
turn = [[0, 0], [n-1, 0], [n-1, n-1], [0, n-1]]

# 공을 잡은 팀의 머리
hx, hy = 0, 0

def in_range(x, y):
    return 0<=x<n and 0<=y<n

# bfs
def find_team(tNum, sx, sy):
    q = deque([(sx, sy)])
    while q:
        x, y = q.popleft()
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            if in_range(nx, ny) and not visited[nx][ny]:
                if g[nx][ny] == 2:
                    visited[nx][ny] = True
                    q.append((nx, ny))
                elif g[nx][ny] == 3:
                    teams[tNum].append([nx, ny])
                    break

def move():
    for t in range(m):
        s = teams[t][0]
        isMove = False
        # 머리 이동
        for i in range(4):
            nx = s[0] + dx[i]
            ny = s[1] + dy[i]
            if in_range(nx, ny):
                if g[nx][ny] == 4:
                    g[nx][ny] = 1
                    g[s[0]][s[1]] = 2
                    teams[t][0][0] = nx
                    teams[t][0][1] = ny
                    isMove = True
                    break
                # 머리의 다음 이동 위치가 꼬리
                elif g[nx][ny] == 3:
                    # 꼬리 위치 찾기
                    for j in range(4):
                        tx = nx + dx[j]
                        ty = ny + dy[j]
                        if in_range(tx, ty) and g[tx][ty] == 2:
                            g[nx][ny] = 1
                            g[s[0]][s[1]] = 2
                            teams[t][0][0] = nx
                            teams[t][0][1] = ny
                            g[tx][ty] = 3
                            teams[t][1][0] = tx
                            teams[t][1][1] = ty
                            break
                    break

        e = teams[t][1]
        # 꼬리 이동
        for i in range(4):
            if not isMove:
                break
            nx = e[0] + dx[i]
            ny = e[1] + dy[i]
            if in_range(nx, ny) and g[nx][ny] == 2:
                g[nx][ny] = 3
                g[e[0]][e[1]] = 4
                teams[t][1][0] = nx
                teams[t][1][1] = ny
                break

# dfs
def get_pos(x, y, depth):
    global ans, visited, hx, hy
    if g[x][y] == 1:
        # print(ans, depth*depth)
        ans += depth*depth
        hx, hy = x, y
        return 
    for i in range(4):
        nx = x + dx[i]
        ny = y + dy[i]
        if in_range(nx, ny) and not visited[nx][ny]:
            if g[nx][ny] == 2 or (depth > 1 and g[nx][ny] == 1) or (g[x][y]==2 and g[nx][ny]==1):
                visited[nx][ny] = True
                get_pos(nx, ny, depth+1)
                visited[nx][ny] = False



def ball_game(sx, sy, d):
    global visited
    for i in range(n):
        nx = sx + i*dx[d]
        ny = sy + i*dy[d]
        if in_range(nx, ny) and 1 <= g[nx][ny] <= 3:
            visited = [[False]*n for _ in range(n)]
            visited[nx][ny] = True
            # print(nx, ny)
            get_pos(nx, ny, 1)


            # 머리랑 꼬리 변경(방향 전환)
            for j in range(m):
                team = teams[j]
                h = team[0]
                if h[0] == hx and h[1] == hy:
                    teams[j][0], teams[j][1] = teams[j][1], teams[j][0]
                    h = teams[j][0]
                    e = teams[j][1]
                    g[h[0]][h[1]] = 1
                    g[e[0]][e[1]] = 3
                    break
            return
        


t = 0
for i in range(n):
    for j in range(n):
        if g[i][j] == 1:
            visited[i][j] = True
            teams[t].append([i, j])
            find_team(t, i, j)
            t += 1

r = 1
ts = 0
td = 0
sx, sy = turn[ts]
d = 0
isTurn = False
ans = 0
while r <= k:
    if r % n == 0:
        isTurn = True
    move()

    # print(r, sx, sy ,d)
    # if r >= 10:
    #     for i in range(n):
    #         print(g[i])
    #     print()


    ball_game(sx, sy, d)

    # if r >= 10:
    #     for i in range(n):
    #         print(g[i])

    r += 1
    if isTurn:
        ts = (ts + 1) % 4
        td = (td + 1) % 4
        d = (d + 1) % 4
        sx, sy = turn[ts]
        isTurn = False
    else:
        sx = sx + rx[td]
        sy = sy + ry[td]

print(ans)