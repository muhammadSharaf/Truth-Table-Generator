package gui;

import expressionOperations.Engine;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Created by M.Sharaf on 26/11/2017.
 */
public class Controller {
    public VBox mainVBox;
    public ScrollPane scrollPane;
    public HBox tableContainer;
    public TextField firstExpression, secondExpression;
    public Label firstLabel, secondLabel, eqLabel;
    private GridPane firstTable, secondTable;
    private Engine engine = new Engine();
    private Byte[] firstValues, secondValues;
    private Object[] firstExpressionTerms, secondExpressionTerms;

    public void initialize() {
        scrollPane.setContent(mainVBox);
        firstExpression.setText("(A > B | !C) & A");
        initTables();
    }

    private void initTables (){
        firstTable = new GridPane();
        firstTable.setGridLinesVisible(true);
        firstTable.setAlignment(Pos.CENTER);
        firstTable.setPadding(new Insets(10, 10, 10, 10));
        HBox.setMargin(firstTable, new Insets(20, 25, 20, 20));

        secondTable = new GridPane();
        secondTable.setGridLinesVisible(true);
        secondTable.setAlignment(Pos.CENTER);
        secondTable.setPadding(new Insets(10, 10, 10, 10));
        HBox.setMargin(secondTable, new Insets(20, 20, 20, 25));

        tableContainer.getChildren().addAll(firstTable, secondTable);
    }

    public void onRunClick() {
        //TODO validation
        tableContainer.getChildren().clear();
        initTables();
        eqLabel.setVisible(false);

        if (!firstExpression.getText().equals("")) {
            firstValues = engine.getValues(firstExpression.getText());
            firstExpressionTerms = engine.getTerms().toArray();
            insertTermsIntoTable(firstTable, firstExpressionTerms);
            insertOutputIntoTable (firstTable, firstValues, firstValues.length);
            checkExpressionState(1, firstLabel, firstValues);
        }

        if (!secondExpression.getText().equals("")) {
            secondValues = engine.getValues(secondExpression.getText());
            secondExpressionTerms = engine.getTerms().toArray();
            insertTermsIntoTable(secondTable, secondExpressionTerms);
            insertOutputIntoTable (secondTable, secondValues, secondValues.length);
            checkExpressionState(2, secondLabel, secondValues);
        }

        if (!firstExpression.getText().equals("") && !secondExpression.getText().equals(""))
            checkEquivelance(firstValues, secondValues);

    }

    private void insertTermsIntoTable(GridPane table, Object[] terms) {
        for (int i = 0; i < terms.length; i++) {
            Label termName = new Label(String.valueOf(terms[i]));
            termName.getStyleClass().add("termText");
            termName.setPadding(new Insets(0, 10, 0, 10));
            table.addColumn(i, termName);
            for (int j = 0; j < Math.pow(2, terms.length); ) {
                for (int k = 1; k <= Math.pow(2, terms.length - i - 1); k++) {
                    Label label = new Label("0");
                    label.getStyleClass().add("combinationText");
                    label.setPadding(new Insets(5, 10, 5, 10));
                    table.add(label, i, j + k);
                }
                j += Math.pow(2, terms.length - i - 1);
                for (int k = 1; k <= Math.pow(2, terms.length - i - 1); k++) {
                    Label label = new Label("1");
                    label.getStyleClass().add("combinationText");
                    label.setPadding(new Insets(5, 10, 5, 10));
                    table.add(label, i, j + k);
                }
                j += Math.pow(2, terms.length - i - 1);
            }
        }
    }

    private void insertOutputIntoTable(GridPane table, Byte[] values, int column) {
        Label output = new Label("Z");
        output.getStyleClass().add("outputText");
        output.setPadding(new Insets(5, 10, 5, 10));
        output.setPadding(new Insets(0, 10, 0, 10));
        table.addColumn(column, output);

        for (int i = 1; i <= values.length; i++) {
            Label value = new Label(String.valueOf(values[i - 1]));
            value.setPadding(new Insets(5, 10, 5, 10));
            value.getStyleClass().add("outputValues");
            table.add(value, column, i);
        }
    }

    private void checkExpressionState(int expNum, Label massage, Byte[] values) {
        boolean state = true;
        for (byte b : values) {
            if (b == 0) {
                state = false;
                break;
            }
        }

        if (state) {
            massage.setText("Expression " + expNum + " Is Tautology");
            return;
        }

        state = true;
        for (byte b : values) {
            if (b == 1) {
                state = false;
                break;
            }
        }

        if (state) {
            massage.setText("Expression " + expNum + " Is Contradiction,");
        } else {
            massage.setText("Expression " + expNum);
        }

    }

    private void checkEquivelance(Byte[] firstValues, Byte[] secondValues) {
        if (firstValues.length == secondValues.length) {
            for (int i = 0; i < firstValues.length; i++) {
                if (firstValues[i] != secondValues[i]) {
                    eqLabel.setVisible(false);
                    return;
                }
            }
            eqLabel.setVisible(true);
        }
    }

    public void onSave (){
        firstLabel.setText(firstExpression.getText());
        secondLabel.setText(secondExpression.getText());
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        WritableImage image = mainVBox.snapshot(new SnapshotParameters(), null);
        File file = fileChooser.showSaveDialog(Main.stage);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {

        }
    }

}
