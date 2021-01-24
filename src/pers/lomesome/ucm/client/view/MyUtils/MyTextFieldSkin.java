package pers.lomesome.ucm.client.view.MyUtils;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MyTextFieldSkin extends TextFieldSkin {
    private char BULLET = '\u25cf';//小圆点
    public MyTextFieldSkin(TextField textField, char bullet) {
        super(textField);
        this.BULLET = bullet;
    }

    public MyTextFieldSkin(TextField textField) {
        super(textField);
    }

    public MyTextFieldSkin(TextField textField, TextFieldBehavior behavior) {
        super(textField, behavior);
    }

    @Override
    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            for (int i = 0; i < n; i++) {
                passwordBuilder.append(BULLET);
            }
            return passwordBuilder.toString();
        } else {
            return txt;
        }
    }

    public char getBULLET() {
        return BULLET;
    }

    public void setBULLET(char BULLET) {
        this.BULLET = BULLET;
    }
}
