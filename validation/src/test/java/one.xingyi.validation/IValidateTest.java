package one.xingyi.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static one.xingyi.validation.IValidate.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IValidateTest {

    @RequiredArgsConstructor
    @Getter
    @ToString
    static class Person {
        final String name;
        final int age;
        final Address address;
    }
    @RequiredArgsConstructor
    @Getter
    @ToString
    static class Address {
        final String line1;
        final String line2;
    }

    @Test
    void testShouldBe() {
        IValidate<String> validate = shouldBe("message {0}-{1}", s -> s.equals("fred"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), "fred"));
        assertEquals(Collections.singletonList("message [context]-notFred"), validate.apply(Collections.singletonList("context"), "notFred"));
    }

    @Test
    void testShouldBeWithSupplier() {
        IValidate<String> validate = shouldBe(() -> "message {0}-{1}", s -> s.equals("fred"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), "fred"));
        assertEquals(Collections.singletonList("message [context]-notFred"), validate.apply(Collections.singletonList("context"), "notFred"));
    }
    @Test
    void testShouldNotBe() {
        IValidate<String> validate = shouldNotBe("message {0}-{1}", s -> s.equals("fred"));
        assertEquals(Collections.singletonList("message [context]-fred"), validate.apply(Collections.singletonList("context"), "fred"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), "notFred"));
    }

    @Test
    void testShouldNotBeWithSupplier() {
        IValidate<String> validate = shouldNotBe(() -> "message {0}-{1}", s -> s.equals("fred"));
        assertEquals(Collections.singletonList("message [context]-fred"), validate.apply(Collections.singletonList("context"), "fred"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), "notFred"));
    }

    Person validPerson = new Person("fred", 10, new Address("line1ofAddress", "line2OfAddress"));
    Person notFredPerson = new Person("notFred", 10, new Address("line1ofAddress", "line2OfAddress"));
    Person nullPerson = new Person(null, 0, new Address(null, null));
    Person nullPersonNullAddress = new Person(null, 0, null);
    @Test
    void testFieldShouldBe() {
        IValidate<Person> validate = fieldShouldBe("name", Person::getName, "message {0}-{1}", s -> s.equals("fred"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), validPerson));
        assertEquals(Collections.singletonList("message [context, name]-notFred"), validate.apply(Collections.singletonList("context"), notFredPerson));
    }
    @Test
    void testFieldShouldBeWithSupplier() {
        IValidate<Person> validate = fieldShouldBe("name", Person::getName, () -> "message {0}-{1}", s -> s.equals("fred"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), validPerson));
        assertEquals(Collections.singletonList("message [context, name]-notFred"), validate.apply(Collections.singletonList("context"), notFredPerson));
    }
    @Test
    void testValidateNotNull() {
        IValidate<Person> validate = notNull();
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), validPerson));
        assertEquals(Collections.singletonList("[context] is null"), validate.apply(Collections.singletonList("context"), null));
    }
    @Test
    void testFieldNotNull() {
        IValidate<Person> validate = fieldNotNull("name", Person::getName);
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), validPerson));
        assertEquals(Collections.singletonList("[context, name] is null"), validate.apply(Collections.singletonList("context"), nullPerson));
    }

    @Test
    void testIsOneOf() {
        IValidate<String> validate = isOneOf(Arrays.asList("fred", "jim"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), "fred"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), "jim"));
        assertEquals(Collections.singletonList("[context] is notFredOrJim which is not one of [fred, jim]"), validate.apply(Collections.singletonList("context"), "notFredOrJim"));
    }

    @Test
    void testFieldIsOneOf() {
        IValidate<Person> validate = fieldIsOneOf("name", Person::getName, Arrays.asList("fred", "jim"));
        assertEquals(Collections.emptyList(), validate.apply(Collections.singletonList("context"), validPerson));
        assertEquals(Collections.singletonList("[context, name] is notFred which is not one of [fred, jim]"), validate.apply(Collections.singletonList("context"), notFredPerson));
    }
    IValidate<Address> addressV = compose(
            fieldNotNull("line1", Address::getLine1),
            fieldNotNull("line2", Address::getLine2)
    );
    IValidate<Person> personV = compose(
            fieldNotNull("name", Person::getName),
            fieldNotNull("address", Person::getAddress),
            fieldShouldBe("name", Person::getName, "message {0}-{1}", s -> s.equals("fred")),
            validateChild("address", Person::getAddress, addressV));
    @Test
    void testCompose() {

        assertEquals(Collections.emptyList(), personV.apply(Collections.singletonList("context"), validPerson));
        assertEquals(Collections.singletonList("message [context, name]-notFred"), personV.apply(Collections.singletonList("context"), notFredPerson));
        assertEquals(Arrays.asList(
                "[context, name] is null",
                "[context, address, line1] is null",
                "[context, address, line2] is null"), personV.apply(Collections.singletonList("context"), nullPerson));
        assertEquals(Arrays.asList("[context, name] is null", "[context, address] is null"), personV.apply(Collections.singletonList("context"), nullPersonNullAddress));
    }

    List<String> errors(Runnable runnable) {
        try {
            runnable.run();
            throw new RuntimeException("should have thrown");
        } catch (ValidationException e) {
            return e.errors;
        }
    }
    @Test
    void testValidateBefore() {
        Function<Person, String> nameGetter = Person::getName;
        Function<Person, String> withValidate = IValidate.<Person, String>validateBefore(personV).apply(nameGetter);

        assertEquals("fred", withValidate.apply(validPerson));
        assertEquals(Arrays.asList("message [name]-notFred"), errors(() -> withValidate.apply(notFredPerson)));
        assertEquals(Arrays.asList("[name] is null", "[address] is null"), errors(() -> withValidate.apply(nullPersonNullAddress)));
    }

    @Test
    void testValidateAfter() {
        Function<Person, String> nameGetter = Person::getName;
        IValidate<String> validate = shouldBe("message {0}-{1}", s -> s.equals("fred"));
        Function<Person, String> withValidate = IValidate.<Person, String>validateAfter(validate).apply(nameGetter);

        assertEquals("fred", withValidate.apply(validPerson));
        assertEquals(Arrays.asList("message []-notFred"), errors(() -> withValidate.apply(notFredPerson)));
    }

    @Test
    void testValidateBeforeAfter() {
        Function<Person, String> nameGetter = Person::getName;
        IValidate<String> validateAfterOk = shouldBe("message {0}-{1}", s -> s.equals("fred"));
        Function<Person, String> withValidateOk = IValidate.<Person, String>validateBeforeAndAfter(personV, validateAfterOk).apply(nameGetter);

        IValidate<String> validateAfterFail = shouldBe("message {0}-{1}", s -> false);
        Function<Person, String> withValidateFail = IValidate.<Person, String>validateBeforeAndAfter(personV, validateAfterFail).apply(nameGetter);

        assertEquals("fred", withValidateOk.apply(validPerson));
        assertEquals(Arrays.asList("message []-fred"), errors(() -> withValidateFail.apply(validPerson)));
        assertEquals(Arrays.asList("message [name]-notFred"), errors(() -> withValidateOk.apply(notFredPerson)));
    }

}