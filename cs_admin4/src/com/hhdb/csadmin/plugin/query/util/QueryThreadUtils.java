package com.hhdb.csadmin.plugin.query.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public final class QueryThreadUtils {
	public static void invokeLater(Runnable runnable) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(runnable);
		} else {
			runnable.run();
		}
	}

	public static void invokeAndWait(Runnable runnable) {
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				// System.err.println("Not EDT");
				SwingUtilities.invokeAndWait(runnable);
			} catch (InterruptedException e) {
				// nothing to do here
			} catch (InvocationTargetException e) {
				// nothing to do here
			}
		} else {
			runnable.run();
		}
	}

	private QueryThreadUtils() {
	}
	
    public static void startWorker(final Runnable runnable) {
        QuerySwingWorker worker = new QuerySwingWorker() {
            public Object construct() {
                try {

                    runnable.run();

                } catch (final Exception e) {

                    invokeAndWait(new Runnable() {
                        public void run() {
                           
                        }
                    });

                }
                return null;
            }
        };
        worker.start();
    }
}
