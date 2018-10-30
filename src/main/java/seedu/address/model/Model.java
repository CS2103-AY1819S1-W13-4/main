package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTutorHelper newData);

    /** Returns the TutorHelper */
    ReadOnlyTutorHelper getTutorHelper();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void updatePerson(Person target, Person editedPerson);

    /**
     * Sorts the list by day.
     */
    void sortByDay();

    /**
     * Sorts the list by time.
     */
    void sortByTime();

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoTutorHelper();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoTutorHelper();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoTutorHelper();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoTutorHelper();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitTutorHelper();
}
