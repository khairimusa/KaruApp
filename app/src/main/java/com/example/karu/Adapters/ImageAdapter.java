package com.example.karu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karu.Model.Upload;
import com.example.karu.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

// this class doesnot pull data from firebase database
// instead, it will get passed data from BrowseFragment and Upload model class
// note BrowseFragment is ImagesActivity in tutorial
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Upload> mUploads; // list of upload class/images

    //get value from outside need to write constructor
    public ImageAdapter(Context context, List<Upload> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewRentRate.setText("RM " + uploadCurrent.getRentRate()  + " per Day");
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.progress_animation)
                .fit()
                .centerCrop()
                .into(holder.imageViewBrowseItem);
    }

    @Override
    public int getItemCount() {
        // show as many items we have in our uploads table in databse
        return mUploads.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder
    {
        // create variables for image view ngan text view
        public TextView textViewName;// from image_item.xml id = text_view_name_browse
        public TextView textViewRentRate;
        public ImageView imageViewBrowseItem;// from image_item.xml id = image_view_uploaded_image


        public ImageViewHolder(View itemView)
        {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name_browse);
            textViewRentRate = itemView.findViewById(R.id.browse_fragment_text_view_price);
            imageViewBrowseItem = itemView.findViewById(R.id.image_view_uploaded_image);

        }
    }

}
