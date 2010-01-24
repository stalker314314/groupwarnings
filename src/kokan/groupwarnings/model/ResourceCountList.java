package kokan.groupwarnings.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that plays the role of the domain model in the table viewer.
 */
public class ResourceCountList {

	private List<ResourceCount> resourceCountList;
	
	public ResourceCountList() {
		List<ResourceCount> resourceCountList = new ArrayList<ResourceCount>();		
		init(resourceCountList);
	}

	/**
	 * Constructor
	 */
	public ResourceCountList(List<ResourceCount> resourceCountList) {
		init(resourceCountList);
	}
	
	private void init(List<ResourceCount> resourceCountList) {
		this.resourceCountList = resourceCountList;
	}
	/**
	 * Return the collection of tasks
	 */
	public List<ResourceCount> getResourceCountList() {
		return resourceCountList;
	}
}