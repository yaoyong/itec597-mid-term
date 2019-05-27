import java.util.Arrays;

/**
 * <pre>
 * 参考：
 * https://train.usaco.org/usacoprob2?a=NeGx5tDAvaY&S=money
 * https://drive.google.com/open?id=1bUaAAwt2dxtTohnm4iQl05AgaoHtzGDz
 *
 * https://leetcode.com/problems/coin-change/solution/
 * </pre>
 */
public class KnightsTemplarBank {

    /**
     * 返回圣殿骑士团银行职员可有的兑换方法总数以及
     * 最少使用多少枚不同面值钱币即可完成兑换
     *
     * @param amount  钱币数额
     * @param options 可以使用的面值，最小值未必是1，无特定排列顺序
     */
    public int[] resolve(int amount, int[] options) {
        Arrays.sort(options);

        int[][] dp = new int[amount + 1][options.length];
        //可使用的面值
        for (int i = options[0]; i <= amount; i++) {
            dp[i][0] = i % options[0] == 0 ? 1 : 0;
        }
        for (int i = 0; i < options.length; i++) {
            dp[0][i] = 1;
        }

        for (int i = options[0]; i <= amount; i++) {
            for (int j = 1; j < options.length; j++) {
                for (int k = i; k >= 0; k -= options[j]) {
                    dp[i][j] += dp[k][j - 1];
                }
            }
        }
        //兑换规则
        int all = dp[amount][options.length - 1];
        int min = -1;
        if (all > 0) {
            int[] ret = new int[amount + 1];
            Arrays.fill(ret, amount / options[0] + 1);
            ret[0] = 0;
            for (int i = options[0]; i <= amount; i++) {
                for (int coin : options) {
                    if (coin <= i) {
                        ret[i] = Math.min(ret[i], ret[i - coin] + 1);
                    }
                }
            }
            min = ret[amount];
        } else {
            all = -1;
        }

        if (debug)
            System.out.println(all + " " + min);
        return new int[]{all, min};
    }

    private boolean debug;

    public static void main(String[] args) {
        final long now = System.currentTimeMillis();
        KnightsTemplarBank bank = new KnightsTemplarBank();
        //测试用例
        bank.debug = true;
        bank.resolve(5, new int[]{1, 2, 5});
        bank.resolve(3, new int[]{2, 5, 25});
        bank.resolve(10000, new int[]{1, 5, 10, 25});
        bank.resolve(6249, new int[]{186, 419, 83, 408});
        System.out.printf("Time Spent: %.2fS%n", (System.currentTimeMillis() - now) / 1000.0);
    }

}
