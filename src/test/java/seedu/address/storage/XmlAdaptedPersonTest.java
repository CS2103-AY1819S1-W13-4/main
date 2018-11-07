package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.Syllabus;
import seedu.address.model.tuitiontiming.TuitionTiming;
import seedu.address.testutil.Assert;

public class XmlAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_SUBJECT = "javanese";
    private static final String INVALID_TUITION_TIMING = "Friday 10.00pm";
    private static final String INVALID_MONTH = "16";
    private static final String INVALID_AMOUNT = "$200";
    private static final String INVALID_YEAR = "$$22";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_TUITION_TIMING = BENSON.getTuitionTiming().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<XmlAdaptedSubject> VALID_SUBJECTS = BENSON.getSubjects().stream()
            .map(XmlAdaptedSubject::new)
            .collect(Collectors.toList());
    private static final List<XmlAdaptedPay> VALID_PAYMENT = BENSON.getPayments().stream()
            .map(XmlAdaptedPay::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        XmlAdaptedPerson person = new XmlAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_SUBJECTS, VALID_TUITION_TIMING, invalidTags, VALID_PAYMENT);
        Assert.assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidSubject_throwsIllegalValueException() {
        List<XmlAdaptedSubject> invalidSubject = new ArrayList<>(VALID_SUBJECTS);
        XmlAdaptedSyllabus syllabus = new XmlAdaptedSyllabus(new Syllabus("Integration", true));
        List<XmlAdaptedSyllabus> s = new ArrayList<XmlAdaptedSyllabus>();
        s.add(syllabus);
        invalidSubject.add(new XmlAdaptedSubject(INVALID_SUBJECT, s, (float) 0.7));

        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        invalidSubject, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);

        String expectedMessage = Subject.MESSAGE_SUBJECT_CONSTRAINTS;

        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullSubject_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                null, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Subject.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTuitionTiming_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_SUBJECTS, INVALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        String expectedMessage = TuitionTiming.MESSAGE_TUITION_TIMING_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPayment_throwsIllegalValueException() {
        List<XmlAdaptedPay> invalidPayment = new ArrayList<>(VALID_PAYMENT);
        invalidPayment.add(new XmlAdaptedPay(Index.fromOneBased(2), INVALID_AMOUNT, INVALID_MONTH, INVALID_YEAR));
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, invalidPayment);
        Assert.assertThrows(NumberFormatException.class, person::toModelType);

    }

}
