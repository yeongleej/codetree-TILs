from collections import deque

n = int(input())
g = [list(map(int, input().split())) for _ in range(n)]

dx = [0, 0, 1, -1]
dy = [-1, 1, 0, 0]
# bfs
def bfs(sx, sy, num):
    global visited
    q = deque([(sx, sy)])
    gList = [(sx, sy)]
    # 인접접점
    cList = []
    while q:
        x, y = q.popleft()
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            if 0<=nx<n and 0<=ny<n:
                if g[nx][ny] == num:
                    if not visited[nx][ny]:
                        visited[nx][ny] = True
                        gList.append((nx, ny))
                        q.append((nx, ny))
                else:
                    cList.append((nx, ny))
    return gList, cList

    
# turn 십자가
def turn_ten():
    ng = [[0]*n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            # 1사분면
            if i < n//2 and j == n//2:
                ng[j][i] = g[i][j]
            # 2사분면
            elif i == n//2 and j > n//2:
                ng[(n-1)-j][i] = g[i][j]
            # 3사분면
            elif i == n//2 and j < n//2:
                ng[(n-1)-j][i] = g[i][j]
            # 4사분면
            elif i > n//2 and j == n//2:
                ng[j][i] = g[i][j]
            else:
                ng[i][j] = g[i][j]
    for i in range(n):
        for j in range(n):
            g[i][j] = ng[i][j]

# turn box
def turn_box():
    ng = [[0]*n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            # 1사분면
            if i < n//2 and j < n//2:
                ng[j][(n//2-1)-i] = g[i][j]
            # 2사분면
            elif i < n//2 and j > n //2:
                ng[j-(n//2+1)][(n-1)-i] = g[i][j]
            # 3사분면
            elif i > n//2 and j < n//2:
                ng[j+(n//2+1)][(n-1)-i] = g[i][j]
            # 4사분면
            elif i > n//2 and j > n//2:
                ng[j][n//2+n-i] = g[i][j]
            else:
                ng[i][j] = g[i][j]
    for i in range(n):
        for j in range(n):
            g[i][j] = ng[i][j]

# turn_box()
# for i in range(n):
#     print(g[i])


def group_cal():
    global ans, visited
    gNum = 1
    gMap = dict()
    cMap = dict()
    mg = [[0]*n for _ in range(n)]
    visited = [[False]*n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            if not visited[i][j]:
                visited[i][j] = True
                gList, cList = bfs(i, j, g[i][j])
                gMap[gNum] = gList
                cMap[gNum] = cList
                for x, y in gList:
                    mg[x][y] = gNum
                gNum += 1
    groups = set()
    for i in range(1, gNum):
        aSize = len(gMap[i])
        ag = gMap[i][0]
        aNum = g[ag[0]][ag[1]]
        for j in range(i+1, gNum):
            cList = cMap[i]
            gList = gMap[j]
            isFind = False
            cnt = 0
            for ax, ay in cList:
                for bx, by in gList:
                    if ax == bx and ay == by:
                        cnt += 1
                        if not isFind:
                            groups.add((i, j))
                            isFind = True
            if isFind:
                # print(i, j, cnt)
                bSize = len(gList)
                bg = gList[0]
                bNum = g[bg[0]][bg[1]]
                ans += (aSize + bSize)*aNum*bNum*cnt

ans = 0
# 초기 예술점수
# for i in range(n):
#     print(g[i])
group_cal()


# 1~3회전
for i in range(3):
    turn_ten()
    turn_box()
    # for j in range(n):
    #     print(g[j])
    group_cal()
    # print(ans)


print(ans)