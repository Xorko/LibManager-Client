package org.libmanager.client.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * DurationSpinner is used to indicate a HH:mm time without 24-hours limitation
 *
 * This is an edited version of this code:
 * https://github.com/vJechsmayr/FitForFun_prSoftwareEngineering/blob/master/src/fitandfun/TimeSpinner.java
 */
public class DurationSpinner extends Spinner<Duration> {

    enum Mode {
        HOURS {
            @Override
            Duration increment(Duration duration, int steps) {
                return duration.plusHours(steps);
            }

            @Override
            void select(DurationSpinner spinner) {
                int hrIndex = spinner.getEditor().getText().indexOf('h');
                spinner.getEditor().selectRange(0, hrIndex);
            }
        },
        MINUTES {
            @Override
            Duration increment(Duration duration, int steps) {
                return duration.plusMinutes(steps);
            }

            @Override
            void select(DurationSpinner spinner) {
                int hrIndex = spinner.getEditor().getText().indexOf('h');
                spinner.getEditor().selectRange(hrIndex + 1, spinner.getEditor().getText().length());
            }
        };

        abstract Duration increment(Duration duration, int steps);

        abstract void select(DurationSpinner spinner);

        Duration decrement(Duration duration, int steps) {
            return increment(duration, -steps);
        }
    }

    private final ObjectProperty<DurationSpinner.Mode> mode = new SimpleObjectProperty<>(DurationSpinner.Mode.HOURS);

    public ObjectProperty<DurationSpinner.Mode> modeProperty() {
        return mode;
    }

    public final DurationSpinner.Mode getMode() {
        return modeProperty().get();
    }

    public final void setMode(DurationSpinner.Mode mode) {
        modeProperty().set(mode);
    }

    public DurationSpinner(Duration duration) {
        setEditable(true);

        StringConverter<Duration> durationConverter = new StringConverter<>() {

            @Override
            public String toString(Duration duration) {
                return String.format("%02dh%02d", duration.toHours(), duration.toMinutesPart());
            }

            @Override
            public Duration fromString(String string) {
                String[] tokens = string.split("h");
                long hours = getLongField(tokens, 0);
                long minutes = getLongField(tokens, 1);
                return Duration.of(60 * hours + minutes, ChronoUnit.MINUTES);
            }

            private long getLongField(String[] tokens, int index) {
                if (tokens.length <= index || tokens[index].isEmpty()) {
                    return 0;
                }
                return Long.parseLong(tokens[index]);
            }
        };

        TextFormatter<Duration> textFormatter = new TextFormatter<>(
                c -> {
                    String newText = c.getControlNewText();
                    if (newText.matches("[0-9]+h[0-9]{0,2}")) {
                        return c;
                    }
                    return null;
                });

        SpinnerValueFactory<Duration> valueFactory = new SpinnerValueFactory<>() {
            {
                setConverter(durationConverter);
                setValue(duration);
            }

            @Override
            public void decrement(int steps) {
                setValue(mode.get().decrement(getValue(), steps));
                mode.get().select(DurationSpinner.this);
            }

            @Override
            public void increment(int steps) {
                setValue(mode.get().increment(getValue(), steps));
                mode.get().select(DurationSpinner.this);
            }
        };

        this.setValueFactory(valueFactory);
        this.getEditor().setTextFormatter(textFormatter);

        /*
         * Update the mode when the user interacts with the editor. This is a
         * bit of a hack, e.g. calling spinner.getEditor().positionCaret() could
         * result in incorrect state. Directly observing the caretPostion didn't
         * work well though; getting that to work properly might be a better
         * approach in the long run.
         */
        this.getEditor().addEventHandler(InputEvent.ANY, e -> {
            int caretPos = this.getEditor().getCaretPosition();
            int separatorIndex = this.getEditor().getText().indexOf('h');
            if (caretPos <= separatorIndex) {
                mode.set(DurationSpinner.Mode.HOURS);
            } else {
                mode.set(DurationSpinner.Mode.MINUTES);
            }
        });

        /*
         * Select the mode related portion when it changes
         */
        mode.addListener((obs, oldMode, newMode) -> newMode.select(this));
    }

    public DurationSpinner() {
        this(Duration.of(0, ChronoUnit.SECONDS));
    }
}
