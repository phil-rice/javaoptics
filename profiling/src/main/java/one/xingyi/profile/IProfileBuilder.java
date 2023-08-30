package one.xingyi.profile;

public interface IProfileBuilder extends IProfile {
    IProfileBuilder withPrefix(String prefix);

    IProfileBuilder main(String name);
}
