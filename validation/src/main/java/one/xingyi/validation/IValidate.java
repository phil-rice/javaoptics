package one.xingyi.validation;

import one.xingyi.interfaces.Delegate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static one.xingyi.helpers.ListHelpers.append;

public interface IValidate<T> extends BiFunction<List<String>, T, List<String>> {

    /**
     * This applies a predicate. If the predicate is true, there are no errors. The messagePattern {0} is the path and {1} is T
     */
    static <T> IValidate<T> shouldBe(String messagePattern, Predicate<T> fn) {
        return (path, t) -> {
            return fn.test(t) ? emptyList() : singletonList(MessageFormat.format(messagePattern, path, t));
        };
    }
    /**
     * This applies a predicate. If the predicate is true, there are no errors. The messagePattern {0} is the path and {1} is T
     */
    static <T> IValidate<T> shouldBe(Supplier<String> messagePattern, Predicate<T> fn) {
        return (path, t) -> {
            return fn.test(t) ? emptyList() : singletonList(MessageFormat.format(messagePattern.get(), path, t));
        };
    }
    static <T> IValidate<T> shouldNotBe(String messagePattern, Predicate<T> fn) {
        return shouldBe(messagePattern, fn.negate());
    }
    static <T> IValidate<T> shouldNotBe(Supplier<String> messagePattern, Predicate<T> fn) {
        return shouldBe(messagePattern, fn.negate());
    }

    static <T, Field> IValidate<T> fieldShouldBe(String fieldName, Function<T, Field> fieldFn, String messagePattern, Predicate<Field> fn) {
        return validateChild(fieldName, fieldFn, shouldBe(messagePattern, fn));
    }
    static <T, Field> IValidate<T> fieldShouldBe(String fieldName, Function<T, Field> fieldFn, Supplier<String> messagePattern, Predicate<Field> fn) {
        return validateChild(fieldName, fieldFn, shouldBe(messagePattern, fn));
    }
    static <T> IValidate<T> notNull() {
        return shouldBe("{0} is null", Objects::nonNull);
    }
    static <T> IValidate<T> isOneOf(List<T> legalValues) {
        return shouldBe(() -> "{0} is {1} which is not one of " + legalValues, legalValues::contains);
    }

    static <T, Child> IValidate<T> validateChild(String childName, Function<T, Child> childFn, IValidate<Child> childValidator) {
        return (path, t) -> {
            Child child = childFn.apply(t);
            if (child == null) return emptyList();
            return childValidator.apply(append(path, childName), child);
        };
    }

    static <T, Field> IValidate<T> fieldNotNull(String fieldName, Function<T, Field> fieldFn) {
        return (path, t) -> IValidate.<Field>notNull().apply(append(path, fieldName), fieldFn.apply(t));
    }

    static <T, Field> IValidate<T> fieldIsOneOf(String fieldName, Function<T, Field> fieldFn, List<Field> legalValues) {
        return validateChild(fieldName, fieldFn, isOneOf(legalValues));
    }

    @SafeVarargs
    static <From> IValidate<From> compose(BiFunction<List<String>, From, List<String>>... validators) {
        return (path, from) -> {
            List<String> result = new ArrayList<>();
            for (BiFunction<List<String>, From, List<String>> validator : validators) {
                result.addAll(validator.apply(path, from));
            } return result;
        };
    }
    static <T> T validate(List<String> path, T t, BiFunction<List<String>, T, List<String>> validator) {
        List<String> errors = validator.apply(path, t); if (!errors.isEmpty()) throw new ValidationException(errors);
        return t;
    }

    static <From, To> Delegate<From, To> validateBefore(BiFunction<List<String>, From, List<String>> validator) {
        return bizLogic -> from -> bizLogic.apply(validate(emptyList(), from, validator));
    }
    static <From, To> Delegate<From, To> validateAfter(BiFunction<List<String>, To, List<String>> validator) {
        return bizLogic -> from -> validate(emptyList(), bizLogic.apply(from), validator);
    }

    static <From, To> Delegate<From, To> validateBeforeAndAfter(BiFunction<List<String>, From, List<String>> before, BiFunction<List<String>, To, List<String>> after) {
        return bizLogic -> from -> validate(emptyList(), bizLogic.apply(validate(emptyList(), from, before)), after);
    }

}


