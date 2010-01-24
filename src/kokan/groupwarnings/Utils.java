package kokan.groupwarnings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kokan.groupwarnings.model.ResourceCount;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class Utils {
	private static ILog log = null;

	public static void setLog(ILog log) {
		Utils.log = log;
	}

	public static void log(String msg, int status, Exception e) {
		log.log(new Status(Status.INFO, Activator.PLUGIN_ID, status, msg, e));
	}

	private static List<IJavaProject> extractJavaProjects() {
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
		for (int i = 0; i < allProjects.length; i++) {
			IProjectNature nature = null;
			try {
				nature = allProjects[i].getNature("org.eclipse.jdt.core.javanature");
			} catch (CoreException e) {
				continue;
			}
			if (nature == null) {
				/* we are not dealing with java project */
				continue;
			}
			IJavaProject javaProject = JavaCore.create(allProjects[i]);
			javaProjects.add(javaProject);
		}
		return javaProjects;
	}
	
	/**
	 * Uses JDT to enlist all warnings and errors in project
	 * 
	 * @return
	 * @throws CommitWarningCheckerException
	 */
	private static List<IMarker> getFilesWithWarnings(List<IJavaProject> projects) {
		List<IMarker> markers = new ArrayList<IMarker>();
		for (IJavaProject iJavaProject : projects) {
			try {
				IMarker[] marker = iJavaProject.getResource().findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
				markers.addAll(Arrays.asList(marker));
			} catch (CoreException e) {
				log("Error getting project warnings in project " + iJavaProject.getProject().getName(), IStatus.ERROR, e);
			}
		}
		return markers;
	}

	public static List<ResourceCount> getResourceCountList() {
		List<IJavaProject> projects = extractJavaProjects();
		List<IMarker> markers = getFilesWithWarnings(projects);
		List<ResourceCount> list = new ArrayList<ResourceCount>(markers.size());
		for (IMarker iMarker : markers) {
			IResource resource = iMarker.getResource();
			ResourceCount resourceCount = new ResourceCount(resource, Integer.valueOf(1));
			if (list.contains(resourceCount)) {
				ResourceCount oldResourceCount = list.get(list.indexOf(resourceCount));
				list.remove(resourceCount);
				list.add(new ResourceCount(resource, oldResourceCount.getCount() + 1));
			} else {
				list.add(resourceCount);
			}
		}
		Collections.sort(list);
		return list;
	}
}