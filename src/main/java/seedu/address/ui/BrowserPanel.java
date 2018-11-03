package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL =
            "PersonPage.html";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private Label nameLabel;

    @FXML
    private Label tuitionTimingLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label paymentsLabel;

    @FXML
    private Label subjectsLabel;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadPersonPage(null);
        registerAsAnEventHandler(this);
    }

    /**
     * Loads a person's information into the AnchorPane.
     * @param person The person whose page is to be loaded into the AnchorPane.
     */
    private void loadPersonPage(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            nameLabel.setText(person.getName().fullName);
            nameLabel.setTextFill(Color.web("#0a9241"));

            tuitionTimingLabel.setText(person.getTuitionTiming().value);
            tuitionTimingLabel.setTextFill(Color.web("#0a9241"));

            addressLabel.setText(person.getAddress().value);
            addressLabel.setTextFill(Color.web("#0a9241"));

            emailLabel.setText(person.getEmail().value);
            emailLabel.setTextFill(Color.web("#0a9241"));

            phoneLabel.setText(person.getPhone().value);
            phoneLabel.setTextFill(Color.web("#0a9241"));

            final StringBuilder paymentsBuilder = new StringBuilder();
            List<Payment> payments = new ArrayList<>(person.getPayments());
            for (int i = 0; i < payments.size(); i++) {
                Payment selected = payments.get(i);
                paymentsBuilder.append(String.format("Month:%5d    Year:%5d    Amount:%5d\n",
                        selected.getMonth(), selected.getYear(), selected.getAmount()));
            }
            paymentsLabel.setText(paymentsBuilder.toString());
            paymentsLabel.setTextFill(Color.web("#0a9241"));

            final StringBuilder subjectsBuilder = new StringBuilder();
            List<Subject> subjects = new ArrayList<>(person.getSubjects());
            for (int i = 0; i < subjects.size(); i++) {
                String subject = subjects.get(i).toString();
                subjectsBuilder.append(subject.substring(2, subject.length() - 1) + "\n\n");
            }
            subjectsLabel.setText(subjectsBuilder.toString().trim());
            subjectsLabel.setTextFill(Color.web("#0a9241"));

        } else {
            // Person is null, remove all the text.
            nameLabel.setText("");

            tuitionTimingLabel.setText("");

            addressLabel.setText("");

            emailLabel.setText("");

            phoneLabel.setText("");

            paymentsLabel.setText("");

            subjectsLabel.setText("");
        }
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection());
    }
}
