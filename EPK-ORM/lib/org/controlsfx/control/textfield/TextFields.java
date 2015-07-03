package org.controlsfx.control.textfield;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;

import java.util.Arrays;
import java.util.Collection;

import javafx.animation.FadeTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

/**
 * A class containing useful customizations for the JavaFX {@link TextField}.
 * Note that this class is experimental and the API may change in future 
 * releases. Note also that this class makes use of the {@link CustomTextField}
 * class.
 * 
 * @see CustomTextField
 */
public class TextFields {
    private static final Duration FADE_DURATION = Duration.millis(350);

    private TextFields() {
        // no-op
    }

    /***************************************************************************
     *                                                                         *
     * Search fields                                                           *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a TextField that shows a clear button inside the TextField (on
     * the right hand side of it) when text is entered by the user.
     */
    public static TextField createClearableTextField() {
        CustomTextField inputField = new CustomTextField();
        setupClearButtonField(inputField, inputField.rightProperty());
        return inputField;
    }
    
    /**
     * Creates a PasswordField that shows a clear button inside the PasswordField
     * (on the right hand side of it) when text is entered by the user.
     */
    public static PasswordField createClearablePasswordField() {
        CustomPasswordField inputField = new CustomPasswordField();
        setupClearButtonField(inputField, inputField.rightProperty());
        return inputField;
    }
    
    private static void setupClearButtonField(TextField inputField, ObjectProperty<Node> rightProperty) {
        inputField.getStyleClass().add("clearable-field"); //$NON-NLS-1$

        Region clearButton = new Region();
        clearButton.getStyleClass().addAll("graphic"); //$NON-NLS-1$
        StackPane clearButtonPane = new StackPane(clearButton);
        clearButtonPane.getStyleClass().addAll("clear-button"); //$NON-NLS-1$
        clearButtonPane.setOpacity(0.0);
        clearButtonPane.setCursor(Cursor.DEFAULT);
        clearButtonPane.setOnMouseReleased(e -> inputField.clear());

        rightProperty.set(clearButtonPane);

        final FadeTransition fader = new FadeTransition(FADE_DURATION, clearButtonPane);
        fader.setCycleCount(1);

        inputField.textProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable arg0) {
                String text = inputField.getText();
                boolean isTextEmpty = text == null || text.isEmpty();
                boolean isButtonVisible = fader.getNode().getOpacity() > 0;

                if (isTextEmpty && isButtonVisible) {
                    setButtonVisible(false);
                } else if (!isTextEmpty && !isButtonVisible) {
                    setButtonVisible(true);
                }
            }

            private void setButtonVisible( boolean visible ) {
                fader.setFromValue(visible? 0.0: 1.0);
                fader.setToValue(visible? 1.0: 0.0);
                fader.play();
            }
        });
    }

    /***************************************************************************
     *                                                                         *
     * Auto-completion                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * Create a new auto-completion binding between the given textField and the 
     * given suggestion provider.
     * 
     * The {@link TextFields} API has some suggestion-provider builder methods 
     * for simple use cases.
     * 
     * @param textField The {@link TextField} to which auto-completion shall be added
     * @param suggestionProvider A suggestion-provider strategy to use
     * @param converter The converter to be used to convert suggestions to strings
     */
	public static <T> AutoCompletionBinding<T> bindAutoCompletion(TextField textField,
			Callback<ISuggestionRequest, Collection<T>> suggestionProvider,
			StringConverter<T> converter) {
		return new AutoCompletionTextFieldBinding<>(textField,
				suggestionProvider, converter);
	}    
    
    /**
     * Create a new auto-completion binding between the given textField and the 
     * given suggestion provider.
     * 
     * The {@link TextFields} API has some suggestion-provider builder methods 
     * for simple use cases.
     * 
     * @param textField The {@link TextField} to which auto-completion shall be added
     * @param suggestionProvider A suggestion-provider strategy to use
     * @return The AutoCompletionBinding
     */
    public static <T> AutoCompletionBinding<T> bindAutoCompletion(TextField textField, 
    		Callback<ISuggestionRequest, Collection<T>> suggestionProvider){
        return new AutoCompletionTextFieldBinding<>(textField, suggestionProvider);
    }

    /**
     * Create a new auto-completion binding between the given {@link TextField} 
     * using the given auto-complete suggestions
     * 
     * @param textField The {@link TextField} to which auto-completion shall be added
     * @param possibleSuggestions Possible auto-complete suggestions
     * @return The AutoCompletionBinding
     */
	public static <T> AutoCompletionBinding<T> bindAutoCompletion(
			TextField textField, T... possibleSuggestions) {
		return bindAutoCompletion(textField, Arrays.asList(possibleSuggestions));
	}
    
	public static <T> AutoCompletionBinding<T> bindAutoCompletion(
			TextField textField, Collection<T> possibleSuggestions) {
		return new AutoCompletionTextFieldBinding<>(textField,
				SuggestionProvider.create(possibleSuggestions));
	}
}

