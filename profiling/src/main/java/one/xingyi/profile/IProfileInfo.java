package one.xingyi.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.helpers.NumberHelpers;

public interface IProfileInfo {
    int getCount();
    long getTotalMs();
    long getSnapshotMs();
    String getPath();

    default long getAvgMs() {return NumberHelpers.avg(getTotalMs(), getCount());}
}

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
class ProfileInfo implements IProfileInfo {
    final String path;
    final int count;
    final long totalMs;
    final long snapshotMs;
}
