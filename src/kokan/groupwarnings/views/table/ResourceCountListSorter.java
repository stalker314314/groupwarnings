package kokan.groupwarnings.views.table;



import kokan.groupwarnings.model.ResourceCount;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class ResourceCountListSorter extends ViewerSorter {

	/**
	 * Constructor argument values that indicate to sort items by name or count
	 */
	public final static int RESOURCE_NAME = 1;
	public final static int COUNT = 2;

	// Criteria that the instance uses
	private int criteria;

	/**
	 * Creates a resource count list sorter that will use the given sort criteria.
	 * 
	 * @param criteria
	 *            the sort criterion to use: one of <code>RESOURCE_NAME</code> or <code>COUNT</code>
	 */
	public ResourceCountListSorter(int criteria) {
		super();
		this.criteria = criteria;
	}

	public int compare(Viewer viewer, Object o1, Object o2) {

		ResourceCount task1 = (ResourceCount) o1;
		ResourceCount task2 = (ResourceCount) o2;

		switch (criteria) {
		case RESOURCE_NAME:
			return compareResourceNames(task1, task2);
		case COUNT:
			return compareCounts(task1, task2);
		default:
			return 0;
		}
	}

	private int compareCounts(ResourceCount task1, ResourceCount task2) {
		int result = task1.getCount() - task2.getCount();
		result = result < 0 ? 1 : (result > 0) ? -1 : 0;
		return result;
	}

	@SuppressWarnings("unchecked")
	protected int compareResourceNames(ResourceCount task1, ResourceCount task2) {
		return getComparator().compare(
				task1.getResource().getProjectRelativePath().toString(),
				task2.getResource().getProjectRelativePath().toString());
	}

	/**
	 * Returns the sort criteria of this this sorter.
	 * 
	 * @return the sort criterion
	 */
	public int getCriteria() {
		return criteria;
	}
}
