package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TaxMenu1Controller extends MenuController{
    @FXML
    private TextField invading_province;

    @FXML
    private TextArea output_terminal;

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    @FXML
    public void clickedTaxMenu(ActionEvent e) throws IOException {
        getParent().chooseTax(this);
    }

    public void setInvadingProvince(String p) {
        invading_province.setText(p);
    }

    public void appendToTerminal(String message) {
        output_terminal.appendText(message + "\n");
    }

    @FXML
    public void setLowTax(ActionEvent e) throws IOException{
        getParent().setLowTax1(e);
    }

    @FXML
    public void setNormalTax(ActionEvent e) throws IOException{
        getParent().setNormalTax1(e);
    }

    @FXML
    public void setHighTax(ActionEvent e) throws IOException{
        getParent().setHighTax1(e);
    }

    @FXML
    public void setVeryHighTax(ActionEvent e) throws IOException{
        getParent().setVeryHighTax1(e);
    }

}
