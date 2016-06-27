package a1ms.uk.a1ms.ui.uiutil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import a1ms.uk.a1ms.A1MSApplication;
import a1ms.uk.a1ms.R;

/**
 * Created by priju.jacobpaul on 27/06/16.
 */
public class ProgressView {

    public static void addProgressView(ViewGroup rootView, String text){

        // If the view is already added. Just return. Don't add it again.
        if(rootView.findViewById(R.id.progressbar) != null){
            return;
        }

        LayoutInflater inflator = LayoutInflater.from(A1MSApplication.applicationContext);
        View inflatedView = inflator.inflate(R.layout.progress_bar,rootView);

        if(inflatedView != null) {
            ProgressBar progressView = (ProgressBar) inflatedView.findViewById(R.id.progressbar);
            progressView.setVisibility(View.VISIBLE);

            TextView textView = (TextView) inflatedView.findViewById(R.id.progressbar_text);
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }

    }

    public static void removeProgressView(ViewGroup rootView){

        ViewGroup progressView = (ViewGroup)rootView.findViewById(R.id.progressbar_holder);
        if(progressView != null) {
            rootView.removeView(progressView);
        }
    }
}
