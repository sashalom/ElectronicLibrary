package net.sashalom.utils;

import org.primefaces.context.RequestContext;

public class DialogHandler {
	
	public static void show(String dialog) {
		RequestContext r = RequestContext.getCurrentInstance();
		r.execute("PF('" + dialog + "').show();");
	}
	
	public static void hide(String dialog) {
		RequestContext r = RequestContext.getCurrentInstance();
		r.execute("PF('" + dialog + "').hide();");
	}

}
