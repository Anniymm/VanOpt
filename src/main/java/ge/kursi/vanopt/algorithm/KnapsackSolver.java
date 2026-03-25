package ge.kursi.vanopt.algorithm;

import ge.kursi.vanopt.dto.ShipmentDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  dynamic programming - 0/1 knapsack problemistvis
 *- ipovos shipmentebis kombinacia, rac revenues maqsimalurad zrdis
 * ar gadaawharbos vanis maximalur capacity-s
 * @ param - shipment(xelmisawvdomi shipmentebi slisti) da maxvolume(capacity dm^3-shi)
 * unda daabruno slistishercheuli shipmentebis romlebistvisac revenue aris maqsimaluri
 * */
@Component
public class KnapsackSolver {

    public List<ShipmentDto> solve(List<ShipmentDto> shipments, int maxVolume) {
        int n = shipments.size();

        long[][] dp = new long[n + 1][maxVolume + 1];

        for (int i = 1; i <= n; i++) {
            int vol = shipments.get(i - 1).volume();
            long rev = shipments.get(i - 1).revenue();

            for (int v = 0; v <= maxVolume; v++) {
                dp[i][v] = dp[i - 1][v];

                if (v >= vol) {
                    dp[i][v] = Math.max(dp[i][v], dp[i - 1][v - vol] + rev);
                }
            }
        }

        List<ShipmentDto> selected = new ArrayList<>();
        int remainingVolume = maxVolume;

        for (int i = n; i >= 1; i--) {
            if (dp[i][remainingVolume] != dp[i - 1][remainingVolume]) {
                selected.add(shipments.get(i - 1));
                remainingVolume -= shipments.get(i - 1).volume();
            }
        }

        return selected;
    }
}

//dp[v] = max(dp[v], dp[v - 5] + 120)

//Parcel A (volume=5, revenue=120)
//v=15: max(dp[15], dp[10] + 120) = max(0, 0 + 120) = 120
//v=14: max(dp[14], dp[ 9] + 120) = max(0, 0 + 120) = 120
//v=13: max(dp[13], dp[ 8] + 120) = max(0, 0 + 120) = 120
//v=12: max(dp[12], dp[ 7] + 120) = max(0, 0 + 120) = 120
//v=11: max(dp[11], dp[ 6] + 120) = max(0, 0 + 120) = 120
//v=10: max(dp[10], dp[ 5] + 120) = max(0, 0 + 120) = 120
//v= 9: max(dp[ 9], dp[ 4] + 120) = max(0, 0 + 120) = 120
//v= 8: max(dp[ 8], dp[ 3] + 120) = max(0, 0 + 120) = 120
//v= 7: max(dp[ 7], dp[ 2] + 120) = max(0, 0 + 120) = 120
//v= 6: max(dp[ 6], dp[ 1] + 120) = max(0, 0 + 120) = 120
//v= 5: max(dp[ 5], dp[ 0] + 120) = max(0, 0 + 120) = 120
//v = 4stvis gamodis negative index -1

//Parcel B (volume=10, revenue=200)
//v=15: max(dp[15], dp[ 5] + 200) = max(120, 120 + 200) = 320
//v=14: max(dp[14], dp[ 4] + 200) = max(120,   0 + 200) = 200
//v=13: max(dp[13], dp[ 3] + 200) = max(120,   0 + 200) = 200
//v=12: max(dp[12], dp[ 2] + 200) = max(120,   0 + 200) = 200
//v=11: max(dp[11], dp[ 1] + 200) = max(120,   0 + 200) = 200
//v=10: max(dp[10], dp[ 0] + 200) = max(120,   0 + 200) = 200


//Parcel C (volume=3, revenue=80)
//v=15: max(320, dp[ 7] + 160) = max(320, 120 + 160) = 320
//v=13: max(280, dp[ 5] + 160) = max(280, 120 + 160) = 280
//v= 8: max(200, dp[ 0] + 160) = max(200,   0 + 160) = 200
// danarchenebi igivea

//Parcel D (volume=8, revenue=160)
//v=15: max(320, dp[ 7] + 160) = max(320, 120 + 160) = 320
//v=13: max(280, dp[ 5] + 160) = max(280, 120 + 160) = 280
//v= 8: max(200, dp[ 0] + 160) = max(200,   0 + 160) = 200

// -- selected itemebi
