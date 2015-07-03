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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * Action Map provides an ability to create an action map of any object.
 * Attempts to convert methods annotated with {@link ActionProxy} to {@link Action}. 
 * 
 * <h3>Code Example</h3>
 * Here's a very simple example of how to use ActionMap to register a class (in
 * this class it is the application class itself), and to then retrieve actions
 * out of the ActionMap (via the static {@link ActionMap#action(String)} method:
 * <br/>
 * 
 * <pre>
 * public class ActionMapDemo extends Application {
 *     public ActionMapDemo() {
 *         ActionMap.register(this);
 *         Action action11 = ActionMap.action("action11");
 *         Button actionButton = ActionUtils.createButton(action11);
 *     }
 *  
 *     &#64;ActionProxy(text="Action 1.1", graphic="start.png", accelerator="ctrl+shift+T")
 *     private void action11() {
 *         System.out.println( "Action 1.1 is executed");
 *     }
 * }
 * </pre>
 * 
 * @see ActionProxy
 * @see Action
 */
public class ActionMap {

	private static Map<String, AnnotatedAction> actions = new HashMap<>();

	private ActionMap() {
		// no-op
	}
	
	/**
	 * Attempts to convert target's methods annotated with {@link ActionProxy} to {@link Action}s.
	 * Only two types of methods are currently converted: parameter-less methods and 
     * methods with one parameter of type {@link ActionEvent}.
     * 
     * Actions are registered with their id or method name if id is not defined.
     * @throws IllegalArgumentException on duplicate action id
	 * @param target object to work on
	 */
	public static void register(final Object target) {

		for (final Method method : target.getClass().getDeclaredMethods()) {
			
			// process only methods with no parameters or one parameter of type ActionEvent
			int paramCount = method.getParameterCount();
			if ( paramCount > 1 || (paramCount == 1 && method.getParameterTypes()[0] != ActionEvent.class )){
				continue;
			}
			
			Annotation[] annotations = method.getAnnotationsByType(ActionProxy.class);
			if (annotations.length > 0) {
				ActionProxy annotation = (ActionProxy) annotations[0];
				String id = annotation.id().isEmpty()? method.getName(): annotation.id();
				if ( actions.containsKey(id)) {
					throw new IllegalArgumentException( String.format("Action proxy with key = '%s' already exists", id)); //$NON-NLS-1$
				}
				actions.put(id, new AnnotatedAction( annotation, method, target));
			}
		}

	}
	
	/**
	 * Removes all the actions associated with target object from the action amp
	 * @param target object to work on
	 */
	public void unregister(final Object target) {
		if ( target != null ) {
			for ( String key: actions.keySet() ) {
				if ( actions.get(key).getTarget() == target) {
					actions.remove(key);
				}
			}
		}
	}

	/**
	 * Returns action by it's id
	 * @param id action id
	 * @return action or null if id was found
	 */
	public static Action action(String id) {
		return actions.get(id);
	}

	/**
	 * Returns collection of actions by ids. Useful to create {@link ActionGroup}s.
	 * Ids starting with "---" are converted to {@link ActionUtils#ACTION_SEPARATOR}.
	 * Incorrect ids are ignored. 
	 * @param ids action ids
	 * @return collection of actions
	 */
	public static Collection<Action> actions(String... ids) {
		List<Action> result = new ArrayList<>();
		for( String id: ids ) {
			if ( id.startsWith("---")) result.add(ActionUtils.ACTION_SEPARATOR); //$NON-NLS-1$
			Action action = action(id);
			if ( action != null ) result.add(action);
		}
		return result;
	}
	
	
}

class AnnotatedAction extends AbstractAction {

	private Method method;
	private Object target;

	public AnnotatedAction(ActionProxy annotation, Method method, Object target) {
	    // set text
		super(annotation.text());
		
		// set graphic
		Node graphic = resolveGraphic(annotation);
		this.setGraphic(graphic);
		
		// set long text / tooltip
		String longText = annotation.longText().trim();
		if ( graphic != null ) {
			this.setLongText(longText);
		}
		
		// set accelerator
		String acceleratorText = annotation.accelerator().trim();
		if (acceleratorText != null && ! acceleratorText.isEmpty()) {
		    this.setAccelerator(KeyCombination.keyCombination(acceleratorText));
		}
		
		
		this.method = method;
		this.method.setAccessible(true);
		this.target = target;
	}
	
	private Node resolveGraphic( ActionProxy annotation ) {
		String graphicDef = annotation.graphic().trim();
		if ( !graphicDef.isEmpty()) {
			
			String[] def = graphicDef.split("\\>");  // cannot use ':' because it used in urls //$NON-NLS-1$
			if ( def.length == 1 ) return new ImageView(new Image(def[0]));
			switch (def[0]) {
			   case "font"    : return GlyphFontRegistry.glyph(def[1]);   //$NON-NLS-1$
			   case "image"   : return new ImageView(new Image(def[1])); //$NON-NLS-1$
			   default: throw new IllegalArgumentException( String.format("Unknown ActionProxy graphic protocol: %s", def[0])); //$NON-NLS-1$
			}
		}
		return null;
	}
	
	public Object getTarget() {
		return target;
	}

	@Override
	public void handle(ActionEvent ae) {
		try {
			int paramCount =  method.getParameterCount(); 
			if ( paramCount == 0 ) {
				method.invoke(target);
			} else if ( paramCount == 1 && method.getParameterTypes()[0] == ActionEvent.class ) {
				method.invoke(target, ae);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return getText();
	}

}
