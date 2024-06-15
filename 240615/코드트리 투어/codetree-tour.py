import heapq
import sys
input = sys.stdin.readline
INF = 1e9

g = []
distance = []
r = [[] for _ in range(30001)]
N, M = 0, 0

def make_g(irr):
    global N, M, g
    # 초기화
    N, M = irr[1], irr[2]
    g = [[] for _ in range(N)]
    for i in range(3, len(irr), 3):
        v1, v2, e = irr[i], irr[i+1], irr[i+2]
        # print(v1, v2, e)
        if v1 == v2:
            g[v1].append((v2, e))
            continue

        g[v1].append((v2, e))
        g[v2].append((v1, e))

def dijkstra(start):
    global distance
    q = []
    distance = [INF]*N
    heapq.heappush(q, (0, start))
    distance[start] = 0
    while q:
        dist, now = heapq.heappop(q)
        if dist > distance[now]:
            continue
        for j in g[now]:
            cost = dist + j[1]
            if cost < distance[j[0]]:
                distance[j[0]] = cost
                heapq.heappush(q, (cost, j[0]))


def make_r(irr):
    i, rev, dest = irr[1], irr[2], irr[3]
    r[i].append(rev)
    r[i].append(dest)

def remove_r(idx):
    r[idx] = []

def sell():
    sList = []
    for i in range(1, 30001):
        if len(r[i]) == 0:
            continue
        rev, dest = r[i]

        # 도달 불가
        if distance[dest] == INF:
            continue

        if rev - distance[dest] >= 0:
            sList.append((rev-distance[dest], i))
    if len(sList) == 0:
        return -1
    # print(sList)
    sList.sort(key=lambda x : (-x[0], x[1]))
    remove_r(sList[0][1])
    return sList[0][1]    

Q = int(input())
while Q > 0:
    irr = list(map(int, input().split()))
    type = irr[0]
    if type == 100:
        make_g(irr)
        dijkstra(0)
        # print(distance)
    elif type == 200:
        make_r(irr)
    elif type == 300:
        remove_r(irr[1])
    elif type == 400:
        print(sell())
    else:
        start = irr[1]
        dijkstra(start)
        # print(distance)

    Q -= 1