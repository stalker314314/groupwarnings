package kokan.groupwarnings.views.table;

import kokan.groupwarnings.model.ResourceCountList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ResourceCountTableViewer {
	private ResourceCountList taskList = new ResourceCountList();

	/**
	 * @param parent
	 */
	public ResourceCountTableViewer(Composite parent, ResourceCountList taskList) {
		this.taskList = taskList;
		this.addChildControls(parent);
	}

	// private Shell shell;
	private Table table;
	private TableViewer tableViewer;

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void setTaskList(ResourceCountList taskList) {
		this.taskList = taskList;
	}

	/**
	 * Create a new shell, add the widgets, open the shell
	 * 
	 * @return the shell that was created
	 */
	private void addChildControls(Composite composite) {
		// Create a composite to hold the children
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		composite.setLayoutData(gridData);

		// Set numColumns to 3 for the buttons
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 4;
		composite.setLayout(layout);

		// Create the table
		createTable(composite);

		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(new ResourceCountListProvider());
		tableViewer.setLabelProvider(new ResourceCountTableLabelProvider());
		// The input for the table viewer is the instance of ExampleTaskList
		tableViewer.setInput(taskList);
	}

	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// 1st column with image/checkboxes - NOTE: The SWT.CENTER has no effect!!
		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column.setText("");
		column.setWidth(20);

		// 2nd column with task Description
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Resource");
		column.setWidth(400);
		// Add listener to column so tasks are sorted by description when clicked
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new ResourceCountListSorter(ResourceCountListSorter.RESOURCE_NAME));
			}
		});

		// 3rd column with task Owner
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Count");
		column.setWidth(100);
		// Add listener to column so tasks are sorted by owner when clicked
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new ResourceCountListSorter(ResourceCountListSorter.COUNT));
			}
		});
	}

	/**
	 * Create the TableViewer
	 */
	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		// Set the default sorter for the viewer
		tableViewer.setSorter(new ResourceCountListSorter(ResourceCountListSorter.COUNT));
	}

	/**
	 * Return the parent composite
	 */
	public Control getControl() {
		return table.getParent();
	}

	/**
	 * InnerClass that acts as a proxy for the resource count list providing content for the Table.
	 */
	class ResourceCountListProvider implements IStructuredContentProvider {

		public Object[] getElements(Object parent) {
			return taskList.getResourceCountList().toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
}