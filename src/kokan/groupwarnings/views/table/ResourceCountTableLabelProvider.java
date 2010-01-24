package kokan.groupwarnings.views.table;


import kokan.groupwarnings.model.ResourceCount;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Label provider for the table viewer
 * 
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class ResourceCountTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		ResourceCount task = (ResourceCount) element;
		switch (columnIndex) {
		case 0:
			break;
		case 1:
			result = task.getResource().getProjectRelativePath().toString();
			break;
		case 2:
			result = task.getCount().toString();
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		} else {
			return null;
		}
	}

}
