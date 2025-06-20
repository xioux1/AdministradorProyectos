package ui.form;

import app.AppManager;
import ui.componentes.BotoneraPanel;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractFormPanel extends JPanel {
    protected final AppManager manager;
    private final JPanel volverA;
    protected CamposPanel camposPanel;
    protected BotoneraPanel botonera;

    public AbstractFormPanel(AppManager manager, JPanel volverA) {
        this.manager = manager;
        this.volverA = volverA;
        setLayout(new BorderLayout(10, 10));
        camposPanel = setCamposPanel();
        add(camposPanel, BorderLayout.CENTER);
        botonera = new BotoneraPanel(
                "Guardar", "Cancelar", "Volver",
                e -> onOk(),
                e -> onCancel(),
                e -> manager.mostrar(volverA)
        );
        add(botonera, BorderLayout.SOUTH);
    }

    protected abstract CamposPanel setCamposPanel();
    protected abstract void onOk();
    protected abstract void onCancel();
}
