package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalTutorHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * DeleteSyllCommand.
 */
public class DeleteSyllCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexesUnfilteredList_success() throws CommandException {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        String expectedMessage = String.format(DeleteSyllCommand.MESSAGE_DELETESYLL_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Person newPerson = simulateDeleteSyllCommand(personTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(outOfBoundIndex,
                INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidIndexSyllabusUnfilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(personTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, outOfBoundIndex);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        String expectedMessage = String.format(DeleteSyllCommand.MESSAGE_DELETESYLL_SUCCESS, personTarget);
        Person updatedPerson = simulateDeleteSyllCommand(personTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedModel.updatePerson(personTarget, updatedPerson);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorHelper().getPersonList().size());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidIndexSubjectFilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidIndexSyllabusFilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(personTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, outOfBoundIndex);
        assertCommandFailure(deleteSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Person newPerson = simulateDeleteSyllCommand(personTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitTutorHelper();

        // EraseSyll -> first person syllabus is erased
        deleteSyllCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered person list to show all persons
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person deleted again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        // execution failed -> address book state not added into model
        assertCommandFailure(deleteSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        DeleteSyllCommand deleteSyllFirstCommand = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        DeleteSyllCommand deleteSyllSecondCommand = new DeleteSyllCommand(
                INDEX_SECOND_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        // same object -> returns true
        assertEquals(deleteSyllFirstCommand, deleteSyllFirstCommand);

        // same values -> returns true
        DeleteSyllCommand deleteSyllFirstCommandCopy = new DeleteSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        assertEquals(deleteSyllFirstCommand, deleteSyllFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(deleteSyllFirstCommand, 1);

        // null -> returns false
        assertNotEquals(deleteSyllFirstCommand, null);

        // different command -> returns false
        assertNotEquals(deleteSyllFirstCommand, deleteSyllSecondCommand);
    }

    /**
     * Simulates and returns a new {@code Person} created by DeleteSyllCommand.
     */
    private Person simulateDeleteSyllCommand(Person personTarget, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).remove(syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return SubjectsUtil.createPersonWithNewSubjects(personTarget, newSubjects);
    }

}