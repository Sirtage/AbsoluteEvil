package me.qigan.abse.fr.dungons.exc;

public class CustomPLBuildFailedException extends RuntimeException {
    public CustomPLBuildFailedException() {
        super();
    }

    public CustomPLBuildFailedException(String str) {
        super(str);
    }

    public CustomPLBuildFailedException(Throwable exc) {
        super(exc);
    }
}
