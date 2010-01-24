package kokan.groupwarnings.views;

import java.util.List;

import kokan.groupwarnings.Utils;
import kokan.groupwarnings.model.ResourceCountList;
import kokan.groupwarnings.model.ResourceCount;
import kokan.groupwarnings.views.table.ResourceCountTableViewer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

public class GroupWarningsByResource extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "groupwarnings.views.GroupWarningsByResource";

	private ResourceCountTableViewer viewer;
	private Action actionRefresh;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for providing objects to the view. It can wrap existing objects in
	 * adapters or simply return objects as-is. These objects may be sensitive to the current input of the view, or
	 * ignore it and always show the same content (like Task List, for example).
	 */

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return new String[] { "One", "Two", "Three" };
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public GroupWarningsByResource() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		// viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		// viewer.setColumnProperties(new String[] {"a", "b"});
		// viewer.setContentProvider(new ViewContentProvider());
		// viewer.setLabelProvider(new ViewLabelProvider());
		// viewer.setSorter(new NameSorter());
		// viewer.setInput(getViewSite());
		makeActions();
		// hookContextMenu();
		// hookDoubleClickAction();
		// contributeToActionBars();

		List<ResourceCount> resourceCountList = Utils.getResourceCountList();
		viewer = new ResourceCountTableViewer(parent, new ResourceCountList(resourceCountList));
		contributeToActionBars();
		hookDoubleClickAction();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Handle a 'close' event by disposing of the view
	 */

	public void handleDispose() {
		this.getSite().getPage().hideView(this);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(actionRefresh);
		// manager.add(new Separator());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(actionRefresh);
	}

	private void makeActions() {
		actionRefresh = new Action() {
			public void run() {
				List<ResourceCount> resourceCountList = Utils.getResourceCountList();
				ResourceCountList taskList = new ResourceCountList(resourceCountList);
				viewer.getTableViewer().setInput(taskList);
				viewer.setTaskList(taskList);
				viewer.getTableViewer().refresh();
			}
		};
		actionRefresh.setText("Refresh");
		actionRefresh.setToolTipText("Refresh");
		actionRefresh.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
				ISharedImages.IMG_OBJS_WARN_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getTableViewer().getSelection();
				ResourceCount resourceCount = (ResourceCount) ((IStructuredSelection) selection).getFirstElement();
				openResourceInEditor(resourceCount.getResource());
			}
		};
	}

	private void openResourceInEditor(IResource resource) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(resource.getFullPath());
		try {
			IDE.openEditor(page, file);
		} catch (PartInitException e) {
			/* do nothing unfortunately */
		}
	}

	private void hookDoubleClickAction() {
		viewer.getTableViewer().addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
}