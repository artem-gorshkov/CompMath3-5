module ru.itmo.gorshkov {
    requires javafx.controls;
    requires javafx.fxml;
    requires MathParser.org.mXparser;

    opens ru.itmo.gorshkov to javafx.fxml;
    exports ru.itmo.gorshkov;
}