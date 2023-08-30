package one.xingyi.profile;

public interface IProfileBucket extends IProfileInfo{
    void add(long time);


    static IProfileBucket simple() {
        return new ProfileBucket();
    }

    static IProfileBucket movingAverage() {
        return new ProfileBucket();
    }
}
