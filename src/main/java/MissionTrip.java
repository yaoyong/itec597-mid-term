import java.util.LinkedList;
import java.util.Queue;

public class MissionTrip {

    private static class Node {
        private int r, c;

        private Node(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public String toString() {
            return String.format("[%d, %d]", r, c);
        }
    }

    private boolean debug = true;

    /**
     * 计算从起点到终点所有的可能走法总数
     *
     * @param paths 旅途，当某处值为1时不允许走过，为0时可以正常走过
     */
    public int resolve(int[][] paths) {
        final long now = System.currentTimeMillis();
        int sum = 0;
        int r = paths.length, c = paths[0].length;
        Node root = new Node(0, 0);

        Queue<Node> nodeQ = new LinkedList<>();
        Queue<StringBuilder> pathQ = new LinkedList<>();
        Queue<boolean[][]> visitQ = new LinkedList<>();
        nodeQ.offer(root);
        pathQ.offer(new StringBuilder());
        visitQ.offer(new boolean[r][c]);

        int[] x = {-1, 1, 0, 0};
        int[] y = {0, 0, -1, 1};
        char[] dirs = {'左', '右', '上', '下'};
        while (!nodeQ.isEmpty()) {
            Node node = nodeQ.poll();
            boolean[][] visit = visitQ.poll();
            StringBuilder sb = pathQ.poll();
            if (node == null || visit == null || sb == null || visit[node.r][node.c])
                continue;

            visit[node.r][node.c] = true;
            sb.append(node.toString());
            if (node.r == r - 1 && node.c == c - 1) {
                if (debug) System.out.println(sb);
                sum++;
                continue;
            } else if (paths[node.r][node.c] == 1) {
                sb.append("!敌人据点!");
                continue;
            }

            for (int i = 0; i < x.length; i++) {
                int nr = node.r + y[i], nc = node.c + x[i];
                if (nr < 0 || nr == r || nc < 0 || nc == c || visit[nr][nc])
                    continue;
                nodeQ.offer(new Node(nr, nc));
                boolean[][] nVisit = new boolean[r][c];
                for (int j = 0; j < r; j++) {
                    nVisit[j] = visit[j].clone();
                }
                visitQ.offer(nVisit);
                pathQ.offer(new StringBuilder(sb).append(" -")
                    .append(dirs[i]).append("-> "));
            }
        }
        System.out.printf("Sum: %d. Time Spent: %.2fS%n", sum,
            (System.currentTimeMillis() - now) / 1000.0);
        return sum;
    }

    public static void main(String[] args) {
        MissionTrip mt = new MissionTrip();
        mt.resolve(new int[3][3]);

        int[][] map = new int[3][3];
        map[1][2] = map[2][1] = 1;
        mt.resolve(map);

        mt.debug = false;
        mt.resolve(new int[2][2]);
        mt.resolve(new int[4][4]);
        mt.resolve(new int[5][5]);
    }
}
