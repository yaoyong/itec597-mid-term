import java.util.LinkedList;
import java.util.Queue;

public class MissionTrip {

    private int count;

    /**
     * 可参考：https://leetcode.com/problems/unique-paths-iii/
     * 该题要求穿过所有非障碍点且仅穿过一次，因而还需要统计穿过的数量
     * Size: 6, Sum: 1262816. Time Spent: 0.47S
     * Size: 7, Sum: 575780564. Time Spent: 279.35S
     */
    private int resolveDFS(int[][] paths) {
        count = 0;
        dfs(paths, 0, 0, new boolean[paths.length][paths[0].length]);
        return count;
    }

    private void dfs(int[][] paths, int r, int c, boolean[][] marks) {
        if (r < 0 || r >= paths.length || c < 0 || c >= paths[0].length ||
            paths[r][c] == 1 || marks[r][c]) {
            return;
        } else if (r == paths.length - 1 && c == paths[0].length - 1) {
            count++;
            return;
        }

        marks[r][c] = true;
        for (int i = 0; i < rOffsets.length; i++)
            dfs(paths, r + rOffsets[i], c + cOffsets[i], marks);
        marks[r][c] = false;
    }

    private final int[] rOffsets = {-1, 1, 0, 0}, cOffsets = {0, 0, -1, 1};

    /**
     * 计算从起点到终点所有的可能走法总数，采用相对直观的BFS处理方法
     *
     * @param paths 旅途，当某处值为1时不允许走过，为0时可以正常走过
     */
    public int resolve(int[][] paths) {
        int sum = 0;
        int r = paths.length, c = paths[0].length;
        Queue<Integer> nodes = new LinkedList<>();
        // 对r * c <= 64的场景，也可以考虑使用long（64位），基于位运算来判定是否走过
        Queue<boolean[]> visited = new LinkedList<>();
        nodes.offer(0);
        visited.offer(new boolean[r * c]);

        while (!nodes.isEmpty()) {
            Integer node = nodes.poll();
            boolean[] visit = visited.poll();
            if (node == null || visit == null || visit[node])
                continue;

            visit[node] = true;
            int nr = node / c, nc = node % c;
            if (nr == r - 1 && nc == c - 1) {
                sum++;
                continue;
            }

            for (int i = 0; i < rOffsets.length; i++) {
                int cr = nr + rOffsets[i], cc = nc + cOffsets[i];
                if (cr < 0 || cr >= r || cc < 0 || cc >= c ||
                    visit[cr * c + cc] || paths[cr][cc] == 1)
                    continue;
                nodes.offer(cr * c + cc);
                visited.offer(visit.clone());
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        MissionTrip mt = new MissionTrip();
        mt.resolve(new int[3][3]);

        int[][] map = new int[3][3];
        map[1][2] = map[2][1] = 1;
        mt.resolve(map);

        map[2][1] = 0;
        mt.resolve(map);

        mt.resolve(new int[2][2]);
        for (int i = 4; i <= 6; i++) {
            final long now = System.currentTimeMillis();
            long sum = mt.resolve(new int[i][i]);
            System.out.printf("Size: %d, Sum: %d. Time Spent: %.2fS%n", i,
                sum, (System.currentTimeMillis() - now) / 1000.0);
        }

        for (int i = 6; i <= 7; i++) {
            final long now = System.currentTimeMillis();
            long sum = mt.resolveDFS(new int[i][i]);
            System.out.printf("Size: %d, Sum: %d. Time Spent: %.2fS%n", i,
                sum, (System.currentTimeMillis() - now) / 1000.0);
        }
    }

}
