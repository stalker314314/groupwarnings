package kokan.groupwarnings.model;

import org.eclipse.core.resources.IResource;

public class ResourceCount implements Comparable<ResourceCount> {

	private final IResource resource;
	private final Integer count;

	public ResourceCount(IResource resource, Integer count) {
		this.resource = resource;
		this.count = count;
	}

	public IResource getResource() {
		return resource;
	}
	
	public Integer getCount() {
		return count;
	}
	
	@Override
	public String toString() {
		return resource.getProjectRelativePath().toString() + " (" + count + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceCount other = (ResourceCount) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	@Override
	public int compareTo(ResourceCount o) {
		return -this.count.compareTo(o.count);
	}
}