package ua.vitamin.redditviewer.allerts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

import ua.vitamin.redditviewer.R;

public class FullScreenImageDialog extends DialogFragment {

    private ImageView fullScreenImageView;
    private Button saveImageButton;
    private String url;

    public FullScreenImageDialog(String url) {
        this.url = url;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.image_dialog, container, false);

        fullScreenImageView = root.findViewById(R.id.fullScreenImageView);
        saveImageButton = root.findViewById(R.id.saveImageButton);

        Picasso.get().load(url).into(fullScreenImageView);

        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
               onSaveImage();
                Toast.makeText(v.getContext(), "Image saved", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().remove(FullScreenImageDialog.this).commit();
            }
        });
        return root;
    }

    private void onSaveImage() {
        BitmapDrawable drawable;
        Bitmap bitmap;
        try {
            drawable = (BitmapDrawable) fullScreenImageView.getDrawable();
            bitmap = drawable.getBitmap();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap,
                    "RedditPost" + System.currentTimeMillis() + ".jpg Card Image", "" + System.currentTimeMillis() + ".jpg Card Image");
            Toast.makeText(getContext(), "Image saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error! This is not a picture. Save canceled.", Toast.LENGTH_LONG).show();
        }

    }
}
