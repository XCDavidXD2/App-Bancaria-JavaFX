package org.example.appbancariajavafx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Label mensaje;

    @FXML
    private VBox login;

    @FXML
    private VBox cuenta;

    @FXML
    private PasswordField contraseñaField;

    @FXML
    private Label campoSaldo;

    @FXML
    private TextField cantidad;

    @FXML
    private ListView<String> listaTransacciones;

    @FXML
    private ComboBox<String> selectorMoneda;

    double saldo = 1000.0;
    final String contraseña_correcta = "contraseña";
    List<String> transacciones = new ArrayList<>();
    double limiteRetiroDiario = 1000.0;
    double retirado = 0.0;
    static final double limiteComision = 500.0;
    static final double tasaComision = 0.01;

    @FXML
    protected void initialize() {
        selectorMoneda.getItems().addAll("€", "$", "£");
        selectorMoneda.setValue("€");
        actualizarSaldo();
    }

    @FXML
    protected void onLoginButtonClick() {
        if (contraseñaField.getText().equals(contraseña_correcta)) {
            login.setVisible(false);
            cuenta.setVisible(true);
            mensaje.setText("");
        } else {
            mensaje.setText("Contraseña incorrecta");
        }
    }

    @FXML
    protected void onWithdrawButtonClick() {
        double commission;
        double totalAmount;
        double amount;

        try {
            amount = Double.parseDouble(cantidad.getText());

            if (amount <= 0) {
                mensaje.setText("Introduzca una cantidad válida");
                return;
            }

            if (amount > saldo) {
                mensaje.setText("Saldo insuficiente");
                return;
            }

            if (retirado + amount > limiteRetiroDiario) {
                mensaje.setText("Límite diario de retirada superado");
                return;
            }

            if (amount > limiteComision) {
                commission = amount * tasaComision;
            } else {
                commission = 0;
            }

            totalAmount = amount + commission;

            if (totalAmount > saldo) {
                mensaje.setText("Saldo insuficiente (con comisión)");
                return;
            }

            saldo -= totalAmount;
            retirado += amount;

            registrarTransaccion(String.format("Retirada: -%.2f %s (Comisión: %.2f %s)", amount, selectorMoneda.getValue(), commission, selectorMoneda.getValue()));
            actualizarSaldo();

            mensaje.setText("Retirada realizada con éxito");
        } catch (NumberFormatException e) {
            mensaje.setText("Por favor, introduzca un número válido");
        }
        cantidad.clear();
    }

    @FXML
    protected void onDepositButtonClick() {
        double amount;

        try {
            amount = Double.parseDouble(cantidad.getText());

            if (amount <= 0) {
                mensaje.setText("Introduzca una cantidad válida");
                return;
            }

            saldo += amount;
            registrarTransaccion(String.format("Ingreso: +%.2f %s", amount, selectorMoneda.getValue()));
            actualizarSaldo();
            mensaje.setText("Ingreso realizado");
        } catch (NumberFormatException e) {
            mensaje.setText("Por favor, introduzca un número válido");
        }
        cantidad.clear();
    }

    public void actualizarSaldo() {
        campoSaldo.setText(String.format("%.2f %s", saldo, selectorMoneda.getValue()));
    }

    public void registrarTransaccion(String transaccion) {
        String transaccionCompleta = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " - " + transaccion;
        transacciones.add(0, transaccionCompleta);
        listaTransacciones.setItems(javafx.collections.FXCollections.observableArrayList(transacciones));
    }
}