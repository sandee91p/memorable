package com.container.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingAnimation {
    private ProgressDialog progressDialog;

    /**
     * Shows the loading animation with a custom message and progress.
     *
     * @param context  The context of the application or activity.
     * @param message  The message to display alongside the loading spinner.
     */
    public void showLoading(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message); // Set the initial message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Horizontal progress bar
            progressDialog.setIndeterminate(false); // Show a determinate progress bar
            progressDialog.setCancelable(false); // Prevent dismissal by tapping outside
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Transparent background
        }
        progressDialog.show(); // Show the dialog
    }

    /**
     * Updates the progress and message of the loading animation.
     *
     * @param progress The progress value (0-100).
     * @param message  The message to display.
     */
    public void updateProgress(int progress, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setProgress(progress); // Update progress
            progressDialog.setMessage(message); // Update message
        }
    }

    /**
     * Hides the loading animation if it is currently visible.
     */
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss(); // Dismiss the dialog
        }
    }
}
