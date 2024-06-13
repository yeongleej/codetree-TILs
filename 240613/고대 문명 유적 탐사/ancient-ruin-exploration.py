from collections import deque
import copy
import sys
input = sys.stdin.readline

dx = [0, 0, -1, 1]
dy = [-1, 1, 0, 0]

# 회전 중심좌표
c = [
    (1, 1), (1, 2), (1, 3),
    (2, 1), (2, 2), (2, 3),
    (3, 1), (3, 2), (3, 3)   
]

K, M = map(int, input().split())
g = [list(map(int, input().split())) for _ in range(5)]
mrr = list(map(int, input().split()))
ng = [[0]*5 for _ in range(5)]
visited = [[False]*5 for _ in range(5)]

def turn(n, cx, cy):
    N = 3
    sx, sy = cx-1, cy-1
    # print(n, cx, cy)
    for i in range(sx, sx+N):
        for j in range(sy, sy+N):
            x, y = i-sx, j-sy
            # 90도
            if n == 0:
                nx, ny = y, (N-1)-x
            # 180도
            elif n == 1:
                nx, ny = (N-1)-x, (N-1)-y
            # 270도
            else:
                nx, ny = (N-1)-y, x
            ng[nx+sx][ny+sy] = g[i][j]

def in_range(x, y):
    return 0<=x<5 and 0<=y<5

def bfs(sx, sy, num):
    global visited
    q = deque([(sx, sy)])
    aList = [(sx, sy)]
    while q:
        x, y = q.popleft()
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            if in_range(nx, ny) and not visited[nx][ny] and ng[nx][ny] == num:
                visited[nx][ny] = True
                q.append((nx, ny))
                aList.append((nx, ny))

    return aList


def find_anti():
    global visited
    visited = [[False]*5 for _ in range(5)]
    vList = []
    for i in range(5):
        for j in range(5):
            if not visited[i][j]:
                visited[i][j] = True
                res = bfs(i, j, ng[i][j])
                if len(res) >= 3:
                    vList.append(res)
    return vList

def fill_anti(s):
    for j in range(5):
        for i in range(4, -1, -1):
            if ng[i][j] == 0:
                ng[i][j] = mrr[s]
                s += 1
    return s



ans = []
while K > 0:
    score = 0
    # [+유물1차가치리스트, -각도, -열, -행, 그래프]
    arr = []        
    # 3X3 격자 찾기
    for i in range(len(c)):
        cx, cy = c[i]
        # 각도 (90, 180, 270)
        for j in range(3):
            ng = copy.deepcopy(g)
            crr = []

            turn(j, cx, cy)
            
            fList = find_anti()
            
            # crr.append([first_val, j, cy, cx])
            crr.extend([fList, j, cy, cx, ng])
            arr.append(crr)
    arr.sort(key=lambda x : (-len(x[0]), x[1], x[2], x[3]))
    # print(arr[0])
    # arr[0] : 연쇄 탐색할 유물들 정보
    aList = arr[0][0]
    ng = copy.deepcopy(arr[0][4])
    for a in aList:
        score += len(a)
        for ax, ay in a:
            ng[ax][ay] = 0
    
    # 연쇄 진행
    idx = 0
    while True:
        # print(idx)
        # print("b")
        # for i in range(5):
        #     print(ng[i])

        idx = fill_anti(idx)

        # print("a")
        # for i in range(5):
        #     print(ng[i])

        fList = find_anti()
        if len(fList) == 0:
            break
        for i in range(len(fList)):
            score += len(fList[i])
            for fx, fy in fList[i]:
                ng[fx][fy] = 0
        # print("s:", score)
    
    # 그래프 이관
    for i in range(5):
        for j in range(5):
            g[i][j] = ng[i][j] 

    # print(score)
    if score > 0:
        ans.append(str(score))
    K -= 1

print(" ".join(ans))