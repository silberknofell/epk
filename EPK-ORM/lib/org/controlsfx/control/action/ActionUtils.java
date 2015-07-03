/**
 * Copyright (c) 2013, ControlsFX
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
package org.controlsfx.control.action;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.tools.Duplicatable;

/**
 * Convenience class for users of the {@link Action} API. Primarily this class
 * is used to conveniently create UI controls from a given Action (this is 
 * necessary for now as there is no built-in support for Action in JavaFX 
 * UI controls at present).
 * 
 * <p>Some of the methods in this class take a {@link Collection} of 
 * {@link Action actions}. In these cases, it is likely they are designed to
 * work with {@link ActionGroup action groups}. For examples on how to work with
 * these methods, refer to the {@link ActionGroup} class documentation.
 * 
 * @see Action
 * @see ActionGroup
 */
public class ActionUtils {
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    private ActionUtils() {
        // no-op
    }
    
    /***************************************************************************
     *                                                                         *
     * Action API                                                              *
     *                                                                         *
     **************************************************************************/
    
    /**
     * Action text behavior.  
     * Defines uniform action's text behavior for multi-action controls such as toolbars and menus  
     */
    public enum ActionTextBehavior {
        /**
         * Text is shown as usual on related control
         */
        SHOW,
        
        /**
         * Text is not shown on the related control
         */
        HIDE,
    }    
    
    
    /**
     * Takes the provided {@link Action} and returns a {@link Button} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link Button} should bind to.
     * @param textBehavior Defines {@link ActionTextBehavior}
     * @return A {@link Button} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static Button createButton(final Action action, final ActionTextBehavior textBehavior) {
        return configure(new Button(), action, textBehavior);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link Button} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link Button} should bind to.
     * @return A {@link Button} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static Button createButton(final Action action) {
        return configure(new Button(), action, ActionTextBehavior.SHOW);
    }
    
	/**
     * Takes the provided {@link Action} and binds the relevant properties to 
     * the supplied {@link Button}. This allows for the use of Actions 
     * within custom Button subclasses.
     * 
     * @param action The {@link Action} that the {@link Button} should bind to.
     * @param button The {@link Button} that the {@link Action} should be bound to.
     * @return The {@link Button} that was bound to the {@link Action}.
     */
	public static Button configureButton(final Action action, Button button) {
        return configure(button, action, ActionTextBehavior.SHOW);
    }
	    
    /**
     * Takes the provided {@link Action} and returns a {@link MenuButton} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link MenuButton} should bind to.
     * @param textBehavior Defines {@link ActionTextBehavior}
     * @return A {@link MenuButton} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static MenuButton createMenuButton(final Action action, final ActionTextBehavior textBehavior) {
        return configure(new MenuButton(), action, textBehavior);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link MenuButton} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link MenuButton} should bind to.
     * @return A {@link MenuButton} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static MenuButton createMenuButton(final Action action) {
        return configure(new MenuButton(), action, ActionTextBehavior.SHOW);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link Hyperlink} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link Hyperlink} should bind to.
     * @return A {@link Hyperlink} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static Hyperlink createHyperlink(final Action action) {
        return configure(new Hyperlink(), action, ActionTextBehavior.SHOW);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link ToggleButton} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link ToggleButton} should bind to.
     * @param textBehavior Defines {@link ActionTextBehavior}
     * @return A {@link ToggleButton} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static ToggleButton createToggleButton(final Action action, final ActionTextBehavior textBehavior ) {
        return configure(new ToggleButton(), action, textBehavior);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link ToggleButton} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link ToggleButton} should bind to.
     * @return A {@link ToggleButton} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static ToggleButton createToggleButton( final Action action ) {
        return createToggleButton( action, ActionTextBehavior.SHOW );
    }    
    
    /**
     * Takes the provided {@link Collection} of {@link Action}  and returns a {@link SegmentedButton} instance
     * with all relevant properties bound to the properties of the actions.
     * 
     * @param actions The {@link Collection} of {@link Action} that the {@link SegmentedButton} should bind to.
     * @param textBehavior Defines {@link ActionTextBehavior}
     * @return A {@link SegmentedButton} that is bound to the state of the provided {@link Action}s
     */
    public static SegmentedButton createSegmentedButton(final ActionTextBehavior textBehavior, Collection<? extends Action> actions) {
        ObservableList<ToggleButton> buttons = FXCollections.observableArrayList();
        for( Action a: actions ) {
            buttons.add( createToggleButton(a,textBehavior));
        }
        return new SegmentedButton( buttons );
    }
    
    /**
     * Takes the provided {@link Collection} of {@link Action}  and returns a {@link SegmentedButton} instance
     * with all relevant properties bound to the properties of the actions.
     * 
     * @param actions The {@link Collection} of {@link Action} that the {@link SegmentedButton} should bind to.
     * @return A {@link SegmentedButton} that is bound to the state of the provided {@link Action}s
     */
    public static SegmentedButton createSegmentedButton(Collection<? extends Action> actions) {
        return createSegmentedButton( ActionTextBehavior.SHOW, actions);
    }    
  
    /**
     * Takes the provided varargs array of {@link Action}  and returns a {@link SegmentedButton} instance
     * with all relevant properties bound to the properties of the actions.
     * 
     * @param actions A varargs array of {@link Action} that the {@link SegmentedButton} should bind to.
     * @param textBehavior Defines {@link ActionTextBehavior}
     * @return A {@link SegmentedButton} that is bound to the state of the provided {@link Action}s
     */
    public static SegmentedButton createSegmentedButton(ActionTextBehavior textBehavior, Action... actions) {
        return createSegmentedButton(textBehavior, Arrays.asList(actions));
    }
    
    /**
     * Takes the provided varargs array of {@link Action}  and returns a {@link SegmentedButton} instance
     * with all relevant properties bound to the properties of the actions.
     * 
     * @param actions A varargs array of {@link Action} that the {@link SegmentedButton} should bind to.
     * @return A {@link SegmentedButton} that is bound to the state of the provided {@link Action}s
     */
    public static SegmentedButton createSegmentedButton(Action... actions) {
        return createSegmentedButton(ActionTextBehavior.SHOW, Arrays.asList(actions));
    }    
    
    
    
    /**
     * Takes the provided {@link Action} and returns a {@link CheckBox} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link CheckBox} should bind to.
     * @return A {@link CheckBox} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static CheckBox createCheckBox(final Action action) {
        return configure(new CheckBox(), action, ActionTextBehavior.SHOW);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link RadioButton} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link RadioButton} should bind to.
     * @return A {@link RadioButton} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static RadioButton createRadioButton(final Action action) {
        return configure(new RadioButton(), action, ActionTextBehavior.SHOW);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link MenuItem} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link MenuItem} should bind to.
     * @return A {@link MenuItem} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static MenuItem createMenuItem(final Action action) {
        return configure(new MenuItem(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link Menu} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link Menu} should bind to.
     * @return A {@link Menu} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static Menu createMenu(final Action action) {
        return configure(new Menu(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link CheckMenuItem} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link CheckMenuItem} should bind to.
     * @return A {@link CheckMenuItem} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static CheckMenuItem createCheckMenuItem(final Action action) {
        return configure(new CheckMenuItem(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link RadioMenuItem} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link RadioMenuItem} should bind to.
     * @return A {@link RadioMenuItem} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static RadioMenuItem createRadioMenuItem(final Action action) {
        return configure(new RadioMenuItem(action.textProperty().get()), action);
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * ActionGroup API                                                         *
     *                                                                         *
     **************************************************************************/
    
    
    /**
     * Action representation of the generic separator. Adding this action anywhere in the 
     * action tree serves as indication that separator has be created in its place.
     * See {@link ActionGroup} for example of action tree creation
     */
    public static Action ACTION_SEPARATOR = new Action() {

        @Override public StringProperty textProperty() {
            return null;
        }

        @Override public BooleanProperty disabledProperty() {
            return null;
        }

        @Override public StringProperty longTextProperty() {
            return null;
        }

        @Override public ObjectProperty<Node> graphicProperty() {
            return null;
        }
        
        @Override public ObjectProperty<KeyCombination> acceleratorProperty() {
            return null;
        }

        @Override public ObservableMap<Object, Object> getProperties() {
            return null;
        }

        @Override public void handle(ActionEvent ae) {
        }
        
        public String toString() { 
            return "Separator";  //$NON-NLS-1$
        };
        
    };    
    
    /**
     * Takes the provided {@link Collection} of {@link Action} (or subclasses,
     * such as {@link ActionGroup}) instances and returns a {@link ToolBar} 
     * populated with appropriate {@link Node nodes} bound to the provided 
     * {@link Action actions}.
     * 
     * @param actions The {@link Action actions} to place on the {@link ToolBar}.
     * @param textBehavior defines {@link ActionTextBehavior}
     * @return A {@link ToolBar} that contains {@link Node nodes} which are bound 
     *      to the state of the provided {@link Action}
     */
    public static ToolBar createToolBar(Collection<? extends Action> actions, ActionTextBehavior textBehavior) {
        ToolBar toolbar = new ToolBar();
        for (Action action : actions) {
            if ( action instanceof ActionGroup ) {
                MenuButton menu = createMenuButton( action, textBehavior );
                menu.getItems().addAll( toMenuItems( ((ActionGroup)action).getActions()));
                toolbar.getItems().add(menu);
            } else if ( action == ACTION_SEPARATOR ) {
                toolbar.getItems().add( new Separator());
            } else if ( action == null ) {
            } else {
                toolbar.getItems().add( createButton(action,textBehavior));
            }
        }
        
        return toolbar;
    }
    
    /**
     * Takes the provided {@link Collection} of {@link Action} (or subclasses,
     * such as {@link ActionGroup}) instances and returns a {@link MenuBar} 
     * populated with appropriate {@link Node nodes} bound to the provided 
     * {@link Action actions}.
     * 
     * @param actions The {@link Action actions} to place on the {@link MenuBar}.
     * @return A {@link MenuBar} that contains {@link Node nodes} which are bound 
     *      to the state of the provided {@link Action}
     */
    public static MenuBar createMenuBar(Collection<? extends Action> actions) {
        MenuBar menuBar = new MenuBar();
        for (Action action : actions) {
            
            if ( action == ACTION_SEPARATOR ) continue;
            
            Menu menu = createMenu( action );
            
            if ( action instanceof ActionGroup ) {
               menu.getItems().addAll( toMenuItems( ((ActionGroup)action).getActions()));
            } else if ( action == null ) {
            }
            
            menuBar.getMenus().add(menu);
        }
        
        return menuBar;
    }
    
    /**
     * Takes the provided {@link Collection} of {@link Action} (or subclasses,
     * such as {@link ActionGroup}) instances and returns a {@link ButtonBar} 
     * populated with appropriate {@link Node nodes} bound to the provided 
     * {@link Action actions}.
     * 
     * @param actions The {@link Action actions} to place on the {@link ButtonBar}.
     * @return A {@link ButtonBar} that contains {@link Node nodes} which are bound 
     *      to the state of the provided {@link Action}
     */
    public static ButtonBar createButtonBar(Collection<? extends Action> actions) {
        ButtonBar buttonBar = new ButtonBar();
        for (Action action : actions) {
            if ( action instanceof ActionGroup ) {
                // no-op
            } else if ( action == ACTION_SEPARATOR ) {
                // no-op
            } else if ( action == null ) {
                // no-op
            } else {
                buttonBar.getButtons().add(createButton(action, ActionTextBehavior.SHOW));
            }
        }
        
        return buttonBar;
    }
    
    /**
     * Takes the provided {@link Collection} of {@link Action} (or subclasses,
     * such as {@link ActionGroup}) instances and returns a {@link ContextMenu} 
     * populated with appropriate {@link Node nodes} bound to the provided 
     * {@link Action actions}.
     * 
     * @param actions The {@link Action actions} to place on the {@link ContextMenu}.
     * @return A {@link ContextMenu} that contains {@link Node nodes} which are bound 
     *      to the state of the provided {@link Action}
     */    
    public static ContextMenu createContextMenu(Collection<? extends Action> actions) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(toMenuItems(actions));
        return menu;
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/
    
    private static Collection<MenuItem> toMenuItems( Collection<? extends Action> actions ) {
        
        Collection<MenuItem> items = new ArrayList<MenuItem>();
        
        for (Action action : actions) {
            
            if ( action instanceof ActionGroup ) {
                
               Menu menu = createMenu( action );
               menu.getItems().addAll( toMenuItems( ((ActionGroup)action).getActions()));
               items.add(menu);
                
            } else if ( action == ACTION_SEPARATOR ) {
                
                items.add( new SeparatorMenuItem());
                
            } else if ( action == null ) {    
            } else {
                
                items.add( createMenuItem(action));
                
            }
            
        }
        
        return items;
        
    }
    
    private static Node copyNode( Node node ) {
    	if ( node instanceof ImageView ) {
    		return new ImageView( ((ImageView)node).getImage());
    	} else if ( node instanceof Duplicatable<?> ) {
    		return (Node) ((Duplicatable<?>)node).duplicate();
    	} else {
    	    return null;
    	}
    }
    
    private static <T extends ButtonBase> T configure(final T btn, final Action action, final ActionTextBehavior textBahavior ) {
        
        if (action == null) {
            throw new NullPointerException("Action can not be null"); //$NON-NLS-1$
        }
        
        // button bind to action properties
        //btn.textProperty().bind(action.textProperty());
        if ( textBahavior == ActionTextBehavior.SHOW ) {
            btn.textProperty().bind(action.textProperty());
        }
        btn.disableProperty().bind(action.disabledProperty());
        
        
        btn.graphicProperty().bind(new ObjectBinding<Node>() {
            { bind(action.graphicProperty()); }

            @Override protected Node computeValue() {
                return copyNode(action.graphicProperty().get());
            }
        });
        
        
        // add all the properties of the action into the button, and set up
        // a listener so they are always copied across
        btn.getProperties().putAll(action.getProperties());
        action.getProperties().addListener(new ButtonPropertiesMapChangeListener<>(btn, action));
        
        // tooltip requires some special handling (i.e. don't have one when
        // the text property is null
        btn.tooltipProperty().bind(new ObjectBinding<Tooltip>() {
            private Tooltip tooltip = new Tooltip();
            
            { 
                bind(action.longTextProperty()); 
                tooltip.textProperty().bind(action.longTextProperty());
            }
            
            @Override protected Tooltip computeValue() {
                String longText =  action.longTextProperty().get();
                return longText == null || longText.isEmpty() ? null : tooltip;
            } 
        });
        
        // TODO handle the selected state of the button if it is of the applicable
        // type
        
        // Just call the execute method on the action itself when the action
        // event occurs on the button
        btn.setOnAction(action);
        
        return btn;
    }
    
    private static <T extends MenuItem> T configure(final T btn, final Action action) {
        
        if (action == null) {
            throw new NullPointerException("Action can not be null"); //$NON-NLS-1$
        }
        
        // button bind to action properties
        btn.textProperty().bind(action.textProperty());
        btn.disableProperty().bind(action.disabledProperty());
        btn.acceleratorProperty().bind(action.acceleratorProperty());
        
        btn.graphicProperty().bind(new ObjectBinding<Node>() {
            { bind(action.graphicProperty()); }

            @Override protected Node computeValue() {
                return copyNode( action.graphicProperty().get());
            }
        });
        
        
        // add all the properties of the action into the button, and set up
        // a listener so they are always copied across
        btn.getProperties().putAll(action.getProperties());
        action.getProperties().addListener(new MenuItemPropertiesMapChangeListener<>(btn, action));
        
        // TODO handle the selected state of the menu item if it is a 
        // CheckMenuItem or RadioMenuItem
        
        // Just call the execute method on the action itself when the action
        // event occurs on the button
        btn.setOnAction(action);
        
        return btn;
    }

    private static class ButtonPropertiesMapChangeListener<T extends ButtonBase> implements MapChangeListener<Object, Object> {

        private final WeakReference<T> btnWeakReference;
        private final Action action;

        private ButtonPropertiesMapChangeListener(T btn, Action action) {
            btnWeakReference = new WeakReference<>(btn);
            this.action = action;
        }

        public void onChanged(MapChangeListener.Change<?, ?> change) {
            T btn = btnWeakReference.get();
            if (btn == null) {
                action.getProperties().removeListener(this);
            } else {
                btn.getProperties().clear();
                btn.getProperties().putAll(action.getProperties());
            }
        }
    }

    private static class MenuItemPropertiesMapChangeListener<T extends MenuItem> implements MapChangeListener<Object, Object> {

        private final WeakReference<T> btnWeakReference;
        private final Action action;

        private MenuItemPropertiesMapChangeListener(T btn, Action action) {
            btnWeakReference = new WeakReference<>(btn);
            this.action = action;
        }

        public void onChanged(MapChangeListener.Change<?, ?> change) {
            T btn = btnWeakReference.get();
            if (btn == null) {
                action.getProperties().removeListener(this);
            } else {
                btn.getProperties().clear();
                btn.getProperties().putAll(action.getProperties());
            }
        }
    }
}
