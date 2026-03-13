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
        long[] dp = new long[maxVolume + 1];

        for (ShipmentDto shipment : shipments) {
            int vol = shipment.volume();
            long rev = shipment.revenue();

            for (int v = maxVolume; v >= vol; v--) {
                dp[v] = Math.max(dp[v], dp[v - vol] + rev);
            }
        }

        List<ShipmentDto> selected = new ArrayList<>();
        int remainingVolume = maxVolume;

        for (int i = n - 1; i >= 0; i--) {
            ShipmentDto shipment = shipments.get(i);
            int vol = shipment.volume();
            long rev = shipment.revenue();

            if (remainingVolume >= vol
                    && dp[remainingVolume] == dp[remainingVolume - vol] + rev) {
                selected.add(shipment);
                remainingVolume -= vol;
            }
        }

        return selected;
    }
}