package uk.com.a1ms.util;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import uk.com.a1ms.R;

/**
 * Created by priju.jacobpaul on 21/07/16.
 */
public class ImagePresenter extends BaseObservable {

    private Bitmap imageBitmap;

    @Bindable
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
//        notifyPropertyChanged(uk.com.a1ms.util.BR.imageBitmap);

    }

    @BindingAdapter("app:roundedImage")
    public static void fitImageOnView(ImageView view,String imageName){

        Context context = view.getContext();
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(context.getResources().getColor(R.color.colorAccent))
                .borderWidthDp(4)
                .cornerRadiusDp(70)
                .oval(false)
                .build();

        Uri path = Uri.fromFile(new File(imageName));
        if(imageName.equals("default")) {
            path = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.camera);
        }

        Picasso.with(context)
                .load(path)
                .centerInside()
                .fit()
                .transform(transformation)
                .into(view);
    }
}
