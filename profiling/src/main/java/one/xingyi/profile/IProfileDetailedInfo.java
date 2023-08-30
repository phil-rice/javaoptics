package one.xingyi.profile;

import java.util.Map;

public interface IProfileDetailedInfo {
    static IProfileDetailedInfo from(IProfile p) {
        if (p instanceof ProfileImpl) return (IProfileDetailedInfo) p;
        throw new IllegalArgumentException("Can only get detailed info from a ProfileImpl. Was " + p.getClass().getName());
    }

    Map<String, ProfileBuckets<Long>> getMs();

    Map<String, ProfileBuckets<Integer>> getCounts();

    Map<String, Integer> getTotalCounts();

    Map<String, Long> getTotalAvg();
}
