package com.eightfit.codingtask.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Knapsack (0-1) Algotihm Solver. It solves the problem through Dynamic Programming.
 * See: https://en.wikipedia.org/wiki/Knapsack_problem
 * <p>
 * The naming and signature is (as closely as possible) to the naming convention to the knapsack problem
 * so any faster/better implementation can be used when/if required. For example if caching and suboptimal
 * solutions are not important there is another solution that reduces the space complexity having only
 * two rows of the matrix during execution time.
 * (I decided for the complete structure because the space is not a big deal and allows caching for following calls)
 */
@Component
public class KnapSackSolverDP {

    /**
     * Solves the knapsack problem and returns the structure where the result can be found
     * with all the internal sub-problems/solutions for future use (if required)
     *
     * @param W   The maximum weight of the knapsack to use
     * @param wt  The weight of the items
     * @param val The value of the items
     * @param n   The amount of items
     * @return
     */
    public int[][] solve(int W, int wt[], int val[], int n) {
        int i, w;
        int K[][] = new int[n + 1][W + 1];

        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    K[i][w] = 0;
                } else if (wt[i - 1] <= w) { // It fits into the knapsack.
                    int optionA = val[i - 1] + K[i - 1][w - wt[i - 1]];
                    int optionB = K[i - 1][w];
                    if (optionA > optionB) {
                        K[i][w] = optionA;
                    } else {
                        K[i][w] = optionB;
                    }
                } else {
                    K[i][w] = K[i - 1][w];
                }
            }
        }

        // stores the result of Knapsack
        int res = K[n][W];
        return K;
    }

    /**
     * Given a matrix with all solutions to the knapsack(0-1) problem it provides for
     * a given solution the set of items contained in the solution. It uses the structure as it
     * is returned by the method solve in this class.
     *
     * @see KnapSackSolverDP#solve(int, int[], int[], int)
     */
    public List<Integer> knapsackSelection(int[][] K, int wt[], int val[], int n, int w) {
        int res = K[n][w];
        List result = new ArrayList();
        for (int i = n; i > 0 && res > 0; i--) {

            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res == K[i - 1][w])
                continue;
            else {

                // This item is included.
                result.add(i - 1);

                // Since this weight is included its
                // value is deducted
                res = res - val[i - 1];
                w = w - wt[i - 1];
            }
        }
        return result;
    }
}
