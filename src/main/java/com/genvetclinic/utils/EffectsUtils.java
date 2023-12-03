package com.genvetclinic.utils;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 * The {@code EffectsUtils} class provides utility methods for applying visual effects to JavaFX controls.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class EffectsUtils {

    /**
     * Adds a hover effect to the specified button, changing its background color and text fill on mouse enter and exit.
     *
     * @param button The button to apply the hover effect.
     */
    public static void addHoverEffect(Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setStyle("-fx-background-color: #30694B; -fx-text-fill: #ffffff; -fx-alignment: center;");
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setStyle("-fx-background-color: #358856; -fx-text-fill: #ffffff; -fx-alignment: center;");
        });
    }

    /**
     * Sets the selection color for rows in a TableView.
     *
     * @param <T>    The type of items in the TableView.
     * @param table  The TableView to set the selection color for.
     * @param color  The color to be used for row selection.
     */
    public static <T> void setTableSelectionColor(TableView<T> table, String color) {
        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setStyle("-fx-selection-bar: " + color + ";");
            return row;
        });
    }

}
