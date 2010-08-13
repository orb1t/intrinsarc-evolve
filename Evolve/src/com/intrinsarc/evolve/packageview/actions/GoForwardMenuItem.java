package com.intrinsarc.evolve.packageview.actions;

import java.awt.event.*;

import javax.swing.*;

import org.eclipse.uml2.*;
import org.eclipse.uml2.Package;

import com.intrinsarc.evolve.guibase.*;
import com.intrinsarc.evolve.packageview.base.*;
import com.intrinsarc.idraw.foundation.*;
import com.intrinsarc.repositorybase.*;
import com.intrinsarc.swing.*;
import com.intrinsarc.swing.enhanced.*;

public class GoForwardMenuItem extends UpdatingJMenuItem
{
	private static final ImageIcon RIGHT_ICON = IconLoader.loadIcon("right.png");
	private ToolCoordinatorFacet coordinator;
	
	public GoForwardMenuItem(ToolCoordinatorFacet coordinator)
	{
		super("");
		this.coordinator = coordinator;
		setTextAndIcon();
	}
	
	/*
	 * @see com.intrinsarc.swing.enhanced.UpdatingJMenuItem#update(boolean)
	 */
	public boolean update()
	{
		final ReusableDiagramViewFacet reusableView = GlobalPackageViewRegistry.activeRegistry.getFocussedView();
		if (reusableView == null)
			return false;

		DiagramReference ref = reusableView.getDiagramStack().getNext();
		if (ref == null)
			return false;
		final org.eclipse.uml2.Package pkg = getDiagramPackage(ref);
		if (pkg == null)
			return false;
		
		KeyStroke accelerator = getAccelerator();
		setAction(
			new CursorWaitingAction(new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					DiagramStack stack = reusableView.getDiagramStack();
					stack.goForward();
					new GotoDiagramAction(
							pkg,
							"",
							false,
							false).actionPerformed(e);
					coordinator.displayPopup(
							RIGHT_ICON,
							"Opened next diagram in history",
				      "Viewing diagram " + (stack.getCurrentNumber() + 1) + " of " + stack.getTotalNumber(),
				      null,
				      null,
				      1000,
				      true,
				      stack.getCurrentNumber(),
				      stack.getTotalNumber() - 1);
				}					
			},
			coordinator,
			0));
		setTextAndIcon();
		setAccelerator(accelerator);

		return true;
	}
	
	private Package getDiagramPackage(DiagramReference ref)
	{
		// does this package exist
		Element elem = GlobalSubjectRepository.repository.findElementByUUID(ref.getId());
		if (elem instanceof Package)
			return (Package) elem;
		return null;
	}

	private void setTextAndIcon()
	{
		setIcon(RIGHT_ICON);
		setText("Open next diagram");
	}
}
