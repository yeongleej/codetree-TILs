import heapq
import sys
input = sys.stdin.readline
INF = 1e9

g = []
distance = []
isMade = [False]*30005 # 여행상품이 만들어진적 있는지 저장합니다.
isCancel = [False]*30005  # 여행상품이 취소되었는지 저장합니다.
sList = [False]*30005
h = []
N, M = 0, 0

class rev:
    def __init__(self, idx, r, dest, profit):
        self.idx = idx
        self.r = r
        self.dest = dest
        self.profit = profit
    def __lt__(self, other):
        if self.profit == other.profit:
            return self.idx < other.idx
        return self.profit > other.profit
    def __str__(self):
        return "p: "+str(self.profit)+" idx: "+str(self.idx)+" r: "+str(self.r)+" dest: "+str(self.dest)

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


def make_r(idx, r,dest):
    # isMade[idx] = True
    sList[idx] = True
    profit = r - distance[dest]
    heapq.heappush(h, rev(idx, r, dest, profit))

def remove_r(idx):
    # if isMade[idx]:
    #     isCancel[idx] = True
    sList[idx] = False

def sell():
    while h:
        p = h[0]
        if p.profit < 0:
            break
        heapq.heappop(h)
        # if not isCancel[p.idx]:
        #     return p.idx
        if sList[p.idx]:
            return p.idx
    return -1

def changeStart(start):
    dijkstra(start)
    # print(distance)
    tmp = []
    while h:
        tmp.append(heapq.heappop(h))
    for p in tmp:
        make_r(p.idx, p.r, p.dest)

Q = int(input())
while Q > 0:
    irr = list(map(int, input().split()))
    type = irr[0]
    if type == 100:
        make_g(irr)
        dijkstra(0)
        # print(distance)
    elif type == 200:
        make_r(irr[1], irr[2], irr[3])
    elif type == 300:
        remove_r(irr[1])
    elif type == 400:
        print(sell())
    else:
        start = irr[1]
        changeStart(start)

    Q -= 1