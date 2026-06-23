package com.baidu.baselibrary.global.crash;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class NativeException extends Exception{

	/* Crash dumps begin with a string containing this substring. */
	static final String crash_dump_header =
	  "*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***";

	/* Build fingerprint contains this substring. */
	static final String build_fingerprint_header = "Build fingerprint: 'htccn_chs_cu/cp2dug/cp2dug:4.1.1/JRO03H/209660.1:user/release-keys'";

	/* Regular expression for the process ID information line. */
	static final String pid_header = "pid: %d, tid: %d, name: %s  >>> com.meta.read <<<";


	
    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public NativeException() {
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage
     *            the detail message for this exception.
     */
    public NativeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace, the
     * specified detail message and the specified cause.
     *
     * @param detailMessage
     *            the detail message for this exception.
     * @param throwable
     *            the cause of this exception.
     */
    public NativeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified cause.
     *
     * @param throwable
     *            the cause of this exception.
     */
    public NativeException(Throwable throwable) {
        super(throwable);
    }
    
    /**
     * Counts the number of duplicate stack frames, starting from the
     * end of the stack.
     *
     * @param currentStack a stack to compare
     * @param parentStack a stack to compare
     *
     * @return the number of duplicate stack frames.
     */
    private static int myCountDuplicates(StackTraceElement[] currentStack,
            StackTraceElement[] parentStack) {
        int duplicates = 0;
        int parentIndex = parentStack.length;
        for (int i = currentStack.length; --i >= 0 && --parentIndex >= 0;) {
            StackTraceElement parentFrame = parentStack[parentIndex];
            if (parentFrame.equals(currentStack[i])) {
                duplicates++;
            } else {
                break;
            }
        }
        return duplicates;
    }
    
    private void printHeader(Appendable err) throws IOException {
    	
    	/* print dump header */
    	String tmpString = crash_dump_header;
    	err.append(crash_dump_header);
        err.append("\n");
        Log.e("crash", tmpString);

    	/* print fingerprint header */
        tmpString = build_fingerprint_header;
    	err.append(build_fingerprint_header);
        err.append("\n");
        Log.e("crash", tmpString);

    	/* print pthread header */
        String threadName = Thread.currentThread().getName();
    	long threadId = Thread.currentThread().getId();
    	int pid = android.os.Process.myPid(); 
        @SuppressLint("DefaultLocale") String pidHeader = String.format(pid_header, pid, threadId, threadName);
    	err.append(pidHeader);
        err.append("\n");
        tmpString = pidHeader;
        Log.e("crash", tmpString);
        
    	/* print crash message */
        tmpString = getMessage();
        err.append(getMessage());
        err.append("\n");
        Log.e("crash", tmpString);
        
    	/* print bacttrace header */
        tmpString = "backtrace:";
        err.append("backtrace:\n");
        Log.e("crash", tmpString);
    	
    }
    /**
     * Writes a printable representation of this {@code Throwable}'s stack trace
     * to the {@code System.err} stream.
     *
     */
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }
    

    @Override
    public void printStackTrace(PrintWriter err) {
        try {
        	
            NativeException cause = (NativeException)getCause();
            if (cause != null) {
            	printHeader(err);
                cause.myPrintStackTrace(err, "", null);
            }

        	
//        	myPrintStackTrace(err, "", null);
        } catch (IOException e) {
            // Appendable.append throws IOException but PrintStream.append doesn't.
            throw new AssertionError();
        }
    }
    
    @Override
    public void printStackTrace(PrintStream err) {
        try {

            NativeException cause = (NativeException)getCause();
            if (cause != null) {
            	printHeader(err);
                cause.myPrintStackTrace(err, "", null);
            }
        } catch (IOException e) {
            // Appendable.append throws IOException but PrintStream.append doesn't.
            throw new AssertionError();
        }

    }
    
    private String stackTraceElementToString(StackTraceElement stack) {
        StringBuilder buf = new StringBuilder(80);

        buf.append("pc ");
        buf.append(stack.getMethodName() + "  /");
        buf.append(stack.getClassName());
        buf.append(" ");

        if (stack.isNativeMethod()) {
            buf.append("(Native Method)");
        } else {
            String fName = stack.getFileName();

            if (fName == null) {
                buf.append("(Unknown Source)");
            } else {
                int lineNum = stack.getLineNumber();

                buf.append("(");
                buf.append(fName);
                if (lineNum >= 0) {
                    buf.append(':');
                    buf.append(lineNum);
                }
                buf.append(')');
            }
        }
        return buf.toString();
    	
    }
    
    @SuppressLint("DefaultLocale")
    private void myPrintStackTrace(Appendable err, String indent, StackTraceElement[] parentStack)
            throws IOException {
    	String tmpString = getMessage();

        StackTraceElement[] stack = getStackTrace();
        if (stack != null) {
            int duplicates = parentStack != null ? myCountDuplicates(stack, parentStack) : 0;
            for (int i = 0; i < stack.length - duplicates; i++) {
//                err.append(indent);
//                err.append("\tat ");
				tmpString = "    #";
				err.append("    #");
				tmpString += String.format("%02d", i);
				err.append(String.format("%02d", i));
				tmpString += "  ";
				err.append("  ");
				tmpString += stackTraceElementToString(stack[i]);
                err.append(stackTraceElementToString(stack[i]));
                err.append("\n");
                Log.e("crash", tmpString);
            }

            if (duplicates > 0) {
                err.append(indent);
                err.append("\t... ");
                err.append(Integer.toString(duplicates));
                err.append(" more\n");
            }
        }


        NativeException cause = (NativeException)getCause();
        if (cause != null) {
            err.append(indent);
            err.append("Caused by: ");
            cause.myPrintStackTrace(err, indent, stack);
        }
    }
    
}
