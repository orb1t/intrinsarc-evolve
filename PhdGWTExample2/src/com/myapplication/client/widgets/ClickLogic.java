package com.myapplication.client.widgets;

import com.google.gwt.user.client.ui.Widget;

public class ClickLogic
{
// start generated code
// attributes
// required ports
	private com.hopstepjump.backbone.runtime.api.ICreate create;
// provided ports
	private ClickListenerListenerImpl listener_ClickListenerProvided = new ClickListenerListenerImpl();
// setters and getters
	public void setCreate_ICreate(com.hopstepjump.backbone.runtime.api.ICreate create) { this.create = create; }
	public com.google.gwt.user.client.ui.ClickListener getListener_ClickListener(Class<?> required) { return listener_ClickListenerProvided; }
// end generated code


	private class ClickListenerListenerImpl implements com.google.gwt.user.client.ui.ClickListener
	{
		public void onClick(Widget sender)
		{			
			create.create(null);
		}
	}

}
