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
package impl.org.controlsfx.spreadsheet;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class GridCellEditor {

    /***************************************************************************
     * * Protected/Private Fields * *
     **************************************************************************/

    private final SpreadsheetHandle handle;
    // transient properties - these fields will change based on the current
    // cell being edited.
    private SpreadsheetCell modelCell;
    private CellView viewCell;

    // The SpreadsheetEditor in order to position the cell for edition.
    private final SpreadsheetEditor spreadsheetEditor;
    private boolean editing = false;
    
    //The cell's editor 
    private SpreadsheetCellEditor spreadsheetCellEditor;
    private CellView lastHover = null;
    
    //The last key pressed in order to select cell below if it was "enter"
    private KeyCode lastKeyPressed;
    private static final double MAX_EDITOR_HEIGHT = 50.0;

    /***************************************************************************
     * * Constructor * *
     **************************************************************************/

    /**
     * Construct the GridCellEditor.
     */
    public GridCellEditor(SpreadsheetHandle handle) {
        this.handle = handle;
        this.spreadsheetEditor = new SpreadsheetEditor();
    }

    /***************************************************************************
     * * Public Methods * *
     **************************************************************************/
    /**
     * Update the internal {@link SpreadsheetCell}.
     * 
     * @param cell
     */
    public void updateDataCell(SpreadsheetCell cell) {
        this.modelCell = cell;
    }

    /**
     * Update the internal {@link CellView}
     * 
     * @param cell
     */
    public void updateSpreadsheetCell(CellView cell) {
        this.viewCell = cell;
    }

    /**
     * Update the SpreadsheetCellEditor
     * 
     * @param spreadsheetCellEditor
     */
    public void updateSpreadsheetCellEditor(final SpreadsheetCellEditor spreadsheetCellEditor) {
        this.spreadsheetCellEditor = spreadsheetCellEditor;
    }

    public CellView getLastHover() {
        return lastHover;
    }

    public void setLastHover(CellView lastHover) {
        this.lastHover = lastHover;
    }

    /**
     * Whenever you want to stop the edition, you call that method.<br/>
     * True means you're trying to commit the value, then 
     * {@link SpreadsheetCellType#match(java.lang.Object) } will be called 
     * in order to verify that the value is correct.<br/>
     * 
     * False means you're trying to cancel the value and it will be follow by
     * {@link #end()}.<br/>
     * See SpreadsheetCellEditor description
     * 
     * @param commitValue true means commit, false means cancel
     */
    public void endEdit(boolean commitValue) {
        if (commitValue && editing) {
            final SpreadsheetView view = handle.getView();
            boolean match = modelCell.getCellType().match(spreadsheetCellEditor.getControlValue());

            if (match && viewCell != null) {
                Object value = modelCell.getCellType().convertValue(spreadsheetCellEditor.getControlValue());

                // We update the value
                view.getGrid().setCellValue(modelCell.getRow(), modelCell.getColumn(), value);
                editing = false;
                viewCell.commitEdit(modelCell);
                end();
                spreadsheetCellEditor.end();
                
                //We select the cell below if "enter" was typed.
                if(lastKeyPressed == KeyCode.ENTER){
                   TablePosition<ObservableList<SpreadsheetCell>, ?> position = (TablePosition<ObservableList<SpreadsheetCell>, ?>) handle.getGridView().
                            getFocusModel().getFocusedCell();
                    if (position != null) {
                        handle.getGridView().getSelectionModel().clearAndSelect(position.getRow() + 1, position.getTableColumn());
                    }
                }
            }
        }
        if (viewCell != null && editing) {
            editing = false;
            viewCell.cancelEdit();
            end();
            spreadsheetCellEditor.end();
        }
    }

    /**
     * Return if this editor is currently being used.
     * 
     * @return if this editor is being used.
     */
    public boolean isEditing() {
        return editing;
    }

    public SpreadsheetCell getModelCell() {
        return modelCell;
    }

    /***************************************************************************
     * * Protected/Private Methods * *
     **************************************************************************/
    void startEdit() {
        editing = true;
        
        handle.getGridView().addEventFilter(KeyEvent.KEY_PRESSED, enterKeyPressed);
        spreadsheetEditor.startEdit();

        handle.getCellsViewSkin().getVBar().valueProperty().addListener(endEditionListener);
        handle.getCellsViewSkin().getHBar().valueProperty().addListener(endEditionListener);
        
        viewCell.setGraphic(spreadsheetCellEditor.getEditor());

        // Then we call the user editor in order for it to be ready
        Object value = modelCell.getItem();
        Double maxHeight = Math.max(handle.getCellsViewSkin().getRowHeight(viewCell.getIndex()), MAX_EDITOR_HEIGHT);
        spreadsheetCellEditor.getEditor().setMaxHeight(maxHeight);
        spreadsheetCellEditor.getEditor().setPrefWidth(viewCell.getWidth());
        
        if(handle.getGridView().getEditWithKey()){
            handle.getGridView().setEditWithKey(false);
            spreadsheetCellEditor.startEdit("");
        }else{
            spreadsheetCellEditor.startEdit(value);
        }
        
        spreadsheetCellEditor.getEditor().focusedProperty().addListener(endEditionListener);
    }

    private void end() {
        spreadsheetEditor.end();
        spreadsheetCellEditor.getEditor().focusedProperty().removeListener(endEditionListener);
        handle.getCellsViewSkin().getVBar().valueProperty().removeListener(endEditionListener);
        handle.getCellsViewSkin().getHBar().valueProperty().removeListener(endEditionListener);
        
        handle.getGridView().removeEventFilter(KeyEvent.KEY_PRESSED, enterKeyPressed);

        this.modelCell = null;
        this.viewCell = null;
    }

    /**
     * When we stop editing a cell, if enter was pressed, we want to go to the next line.
     */
    private final EventHandler<KeyEvent> enterKeyPressed = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent t) {
            lastKeyPressed = t.getCode();
        }
    };
    
    private final InvalidationListener endEditionListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            endEdit(true);
        }
    };
     
    /**
     * This editor is here to ensure that the cell editor 
     * ({@link SpreadsheetCellEditor}) will always be displayed on top of every
     * other Node. 
     * 
     * To ensure this, we must be sure that our cell is added to the last row
     * so that it will not be covered by other node.
     */
    private class SpreadsheetEditor {

        /***********************************************************************
         * * Private Fields * *
         **********************************************************************/
        private GridRow original;
        private boolean isMoved;

        /**
         * Number of rows currently displayed.
         * 
         * @return
         */
        private int getCellCount() {
            return handle.getCellsViewSkin().getCellsSize();
        }

        private boolean addCell(CellView cell) {
            GridRow lastRow = handle.getCellsViewSkin().getRow(getCellCount() - 1);

            // If the row returned is the extra one at the bottom (see RT-31503)
            if (lastRow.getIndex() >= handle.getView().getGrid().getRowCount()) {
                lastRow = handle.getCellsViewSkin().getRow(handle.getView().getGrid().getRowCount() - 1);
            }

            if (lastRow != null) {
                lastRow.addCell(cell);
                return true;
            }
            return false;
        }

        /***********************************************************************
         * * Public Methods * *
         **********************************************************************/

        /**
         * In case the cell is spanning in rows. We want the cell to be fully
         * accessible so we need to remove it from its tableRow and add it to
         * the last row possible. Then we translate the cell so that it's
         * invisible for the user.
         */
        public void startEdit() {
            // Case when RowSpan if larger and we're not on the last row
            if (modelCell != null && modelCell.getRowSpan() > 1 && modelCell.getRow() != getCellCount() - 1) {
                original = (GridRow) viewCell.getTableRow();

                final double temp = viewCell.getLocalToSceneTransform().getTy();
                isMoved = addCell(viewCell);
                if (isMoved) {
                    viewCell.setTranslateY(temp - viewCell.getLocalToSceneTransform().getTy());
                    original.putFixedColumnToBack();
                }
            }
        }

        /**
         * When we have finish editing. We put the cell back to its right
         * TableRow.
         */
        public void end() {
            if (modelCell != null && modelCell.getRowSpan() > 1) {
                viewCell.setTranslateY(0);
                if (isMoved) {
                    original.addCell(viewCell);
                    original.putFixedColumnToBack();
                }
            }
        }
    }
}
