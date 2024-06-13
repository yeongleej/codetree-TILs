from collections import deque
import copy
import sys
input = sys.stdin.readline

dx = [0, 0, -1, 1]
dy = [-1, 1, 0, 0]

rx = [1, 1, -1, -1]
ry = [1, -1, 1, -1]

n, m, k, c = map(int, input().split())
g = [list(map(int, input().split())) for _ in range(n)]
# 제초제 그래프
h = [[0]*n for _ in range(n)]
trees = []

def in_range(x, y):
    return 0<=x<n and 0<=y<n

def decrease():
    for i in range(n):
        for j in range(n):
            if h[i][j] > 0:
                h[i][j] -= 1

def increase_tree():
    for i in range(n):
        for j in range(n):
            if g[i][j] > 0:
                cnt = 0
                for d in range(4):
                    nx = i + dx[d]
                    ny = j + dy[d]
                    if in_range(nx, ny) and g[nx][ny] > 0:
                        cnt += 1
                g[i][j] += cnt

def find_tree():
    global trees
    trees = []
    for i in range(n):
        for j in range(n):
            if g[i][j] > 0:
                trees.append((i, j))

def breed():
    ng = [[0]*n for _ in range(n)]
    for t in range(len(trees)):
        x, y = trees[t]
        ng[x][y] = g[x][y]
        blanks = []
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            if in_range(nx, ny) and h[nx][ny] == 0 and g[nx][ny] == 0:
                blanks.append((nx, ny))
        tree = g[x][y]
        if len(blanks) > 0:
            tCnt = tree // len(blanks)
        # 주변에 번식
        for nx, ny in blanks:
            ng[nx][ny] += tCnt   

    for i in range(n):
        for j in range(n):
            if ng[i][j] > 0:
                g[i][j] = ng[i][j]
    # 나무 갱신 
    find_tree()

def find_pos():
    maxCnt = 0
    tx, ty = -1, -1
    for tree in trees:
        sx, sy = tree
        cnt = g[sx][sy]
        for i in range(4):
            for j in range(1, k+1):
                nx = sx + j*rx[i]
                ny = sy + j*ry[i]
                if in_range(nx, ny):
                    if g[nx][ny] == -1: 
                        break
                    elif g[nx][ny] > 0:
                        cnt += g[nx][ny]
        if cnt > maxCnt:
            maxCnt = cnt
            tx, ty = sx, sy
    return maxCnt, tx, ty

def spread(x, y):
    h[x][y] = c
    g[x][y] = 0
    for i in range(4):
        for j in range(1, k+1):
            nx = x + j*rx[i]
            ny = y + j*ry[i]
            if in_range(nx, ny):
                if g[nx][ny] == -1: 
                    break
                h[nx][ny] = c
                g[nx][ny] = 0
    # 나무 갱신
    find_tree()

# 초기 나무 찾기
find_tree()

ans = 0
while m > 0:
    # print("m:",)

    # 나무 성장
    increase_tree()

    # print('번식전')
    # for i in range(n):
    #     print(g[i])
    
    # 번식
    breed()

    # print('번식후')
    # for i in range(n):
    #     print(g[i])
    
    
    cnt, px, py = find_pos()
    # print(cnt, px, py)
    ans += cnt
    # 제초제 유효기간 감소
    decrease()
    # 제초제 분산
    spread(px, py)

    # print('박멸후 - g')
    # for i in range(n):
    #     print(g[i])

    # print('박멸후 - h')
    # for i in range(n):
    #     print(h[i])

    m -= 1

print(ans)