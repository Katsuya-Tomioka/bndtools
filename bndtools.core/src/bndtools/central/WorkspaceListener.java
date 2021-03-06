package bndtools.central;

import java.io.File;

import org.bndtools.api.ILogger;
import org.bndtools.api.Logger;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.Success;

import aQute.bnd.build.Workspace;
import aQute.bnd.service.BndListener;
import aQute.service.reporter.Reporter;

public final class WorkspaceListener extends BndListener {
    private static final ILogger logger = Logger.getLogger(WorkspaceListener.class);

    public WorkspaceListener(@SuppressWarnings("unused") Workspace workspace) {}

    @Override
    public void changed(final File file) {
        try {
            final RefreshFileJob job = new RefreshFileJob(file, true);
            if (job.needsToSchedule()) {
                Central.onWorkspaceInit(new Success<Workspace,Void>() {
                    @Override
                    public Promise<Void> call(Promise<Workspace> resolved) throws Exception {
                        job.schedule();
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            logger.logError("Error refreshing workspace", e);
        }
    }

    @Override
    public void signal(Reporter reporter) {
        //        errorProcessor.clear();
        //        errorProcessor.getInfo(workspace);
        //
        //        for (String warning : errorProcessor.getWarnings()) {
        //            logger.logWarning(warning, null);
        //        }
        //        for (String error : errorProcessor.getErrors()) {
        //            logger.logError(error, null);
        //        }
    }

}