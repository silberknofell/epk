/**
 * Copyright (c) 2013, 2014 ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.controlsfx.dialog;

import java.net.URL;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Effect;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Duration;

import org.controlsfx.tools.Platform;

abstract class FXDialog {
    
    /**************************************************************************
     * 
     * Static fields
     * 
     **************************************************************************/
    
    protected static final URL DIALOGS_CSS_URL = FXDialog.class.getResource("dialogs.css"); //$NON-NLS-1$
    protected static final int HEADER_HEIGHT = 28;
    
    
    
    /**************************************************************************
     * 
     * Private fields
     * 
     **************************************************************************/
    
    protected BorderPane root;
    protected HBox windowBtns;
    protected Button closeButton;
    protected Button minButton;
    protected Button maxButton;
    protected Rectangle resizeCorner;
    protected Label titleLabel;
    protected ToolBar toolBar;
    protected double mouseDragDeltaX = 0;
    protected double mouseDragDeltaY = 0;
    
    protected StackPane lightweightDialog;
    
    // shake support
    private double initialX = 0;
    private final DoubleProperty shakeProperty = new SimpleDoubleProperty(this, "shakeProperty", 0.0) {
        @Override protected void invalidated() {
            setX(initialX + shakeProperty.get() * 25);
        }
    };
    
    
    
    /**************************************************************************
     * 
     * Constructors
     * 
     **************************************************************************/
    
    protected FXDialog() {
        // no-op, but we expect subclasses to call init(title) once they have
        // initialised their abstract property methods.
    }
     
    
    
    /**************************************************************************
     * 
     * Public API
     * 
     **************************************************************************/
    
    protected final void init(String title, DialogStyle style) {
        titleProperty().set(title);
        
        resizableProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable valueModel) {
                resizeCorner.setVisible(resizableProperty().get());
                
                if (maxButton != null) {
                    maxButton.setVisible(resizableProperty().get());
    
                    if (resizableProperty().get()) {
                        if (! windowBtns.getChildren().contains(maxButton)) {
                            windowBtns.getChildren().add(1, maxButton);
                        }
                    } else {
                        windowBtns.getChildren().remove(maxButton);
                    }
                }
            }
        });
        
        root = new BorderPane();
        
//        // we use different CSS to more closely mimic the underlying platform
//        final String platform = Utils.isMac()     ? "mac"     : 
//                                Utils.isUnix()    ? "unix"    :
//                                Utils.isWindows() ? "windows" :
//                                "";
        
        // *** The rest is for adding window decorations ***
        lightweightDialog = new StackPane() {
            @Override protected void layoutChildren() {
                super.layoutChildren();
                if (resizeCorner != null) {
                    resizeCorner.relocate(getWidth() - 20, getHeight() - 20);
                }
            }
        };
        lightweightDialog.getChildren().add(root);
        lightweightDialog.getStyleClass().addAll("dialog", "decorated-root",  //$NON-NLS-1$ //$NON-NLS-2$
                       Platform.getCurrent().getPlatformId());
        
        resizeCorner = new Rectangle(10, 10);
        resizeCorner.getStyleClass().add("window-resize-corner"); //$NON-NLS-1$
        resizeCorner.setManaged(false);
        
        
        if (style != DialogStyle.CROSS_PLATFORM_DARK) {
            return;
        }
        
        
        focusedProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable valueModel) {                
                boolean active = ((ReadOnlyBooleanProperty)valueModel).get();
                lightweightDialog.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, active);
            }
        });

        toolBar = new ToolBar();
        toolBar.getStyleClass().add("window-header"); //$NON-NLS-1$
        toolBar.setPrefHeight(HEADER_HEIGHT);
        toolBar.setMinHeight(HEADER_HEIGHT);
        toolBar.setMaxHeight(HEADER_HEIGHT);
        
        titleLabel = new Label();
        titleLabel.setMaxHeight(Double.MAX_VALUE);
        titleLabel.getStyleClass().add("window-title"); //$NON-NLS-1$
        titleLabel.setText(titleProperty().get());

        titleProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable valueModel) {
                titleLabel.setText(titleProperty().get());
            }
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // add close min max
        closeButton = new WindowButton("close"); //$NON-NLS-1$
        closeButton.setFocusTraversable(false);
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                FXDialog.this.hide();
            }
        });
        minButton = new WindowButton("minimize"); //$NON-NLS-1$
        minButton.setFocusTraversable(false);
        minButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                setIconified(isIconified());
            }
        });

        maxButton = new WindowButton("maximize"); //$NON-NLS-1$
        maxButton.setFocusTraversable(false);

        windowBtns = new HBox(3);
        windowBtns.getStyleClass().add("window-buttons"); //$NON-NLS-1$
        windowBtns.getChildren().addAll(minButton, maxButton, closeButton);

        toolBar.getItems().addAll(titleLabel, spacer, windowBtns);
        root.setTop(toolBar);

        lightweightDialog.getChildren().add(resizeCorner);
    }
    
    
    
    /***************************************************************************
     * 
     * Abstract API
     * 
     **************************************************************************/
    
    public abstract void show();
    
    public abstract void hide();
    
    public abstract Window getWindow();
    
    public abstract void sizeToScene();
    
    public abstract double getX();
    
    public abstract void setX(double x);
    
    public void shake() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(2);

        KeyValue keyValue0 = new KeyValue(shakeProperty, 0.0, Interpolator.EASE_BOTH);
        KeyValue keyValue1 = new KeyValue(shakeProperty, -1.0, Interpolator.EASE_BOTH);
        KeyValue keyValue2 = new KeyValue(shakeProperty, 1.0, Interpolator.EASE_BOTH);
        
        initialX = getX();
        
        final double sectionDuration = 50;
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.ZERO, keyValue0),
            new KeyFrame(Duration.millis(sectionDuration),     keyValue1),
            new KeyFrame(Duration.millis(sectionDuration * 3), keyValue2),
            new KeyFrame(Duration.millis(sectionDuration * 4), keyValue0)
        );
        timeline.play();
    }
    
    // --- resizable
    abstract BooleanProperty resizableProperty();
    
    
    // --- focused
    abstract ReadOnlyBooleanProperty focusedProperty();
    
    
    // --- title
    abstract StringProperty titleProperty();
    
    // --- content
    public abstract void setContentPane(Pane pane);
    
    // --- root
    public abstract Node getRoot();
    
    public abstract ObservableList<String> getStylesheets();
    
    
    // --- width
    /**
     * Property representing the width of the dialog.
     */
    abstract ReadOnlyDoubleProperty widthProperty();
    
    
    // --- height
    /**
     * Property representing the height of the dialog.
     */
    abstract ReadOnlyDoubleProperty heightProperty();
    
    
    /**
     * Sets whether the dialog can be iconified (minimized)
     * @param iconifiable if dialog should be iconifiable
     */
    abstract void setIconifiable(boolean iconifiable);
    
    abstract void setIconified(boolean iconified);
    
    abstract boolean isIconified();
    
    /**
     * Sets whether the dialog can be closed
     * @param iconifiable if dialog should be closable
     */
    abstract void setClosable( boolean closable );
    
    abstract void setModal(boolean modal);
    
    abstract boolean isModal();
    
    abstract void setEffect(Effect e);
    
    /***************************************************************************
     *                                                                         
     * Support Classes                                                 
     *                                                                         
     **************************************************************************/
    
    private static class WindowButton extends Button {
        WindowButton(String name) {
            getStyleClass().setAll("window-button"); //$NON-NLS-1$
            getStyleClass().add("window-"+name+"-button"); //$NON-NLS-1$ //$NON-NLS-2$
            StackPane graphic = new StackPane();
            graphic.getStyleClass().setAll("graphic"); //$NON-NLS-1$
            setGraphic(graphic);
            setMinSize(17, 17);
            setPrefSize(17, 17);
        }
    }
    
    
    
    /***************************************************************************
     *                                                                         
     * Stylesheet Handling                                                     
     *                                                                         
     **************************************************************************/
    protected static final PseudoClass ACTIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("active"); //$NON-NLS-1$


}
