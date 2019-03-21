package com.magad.testmyrestapi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.magad.testmyrestapi.models.ArticlesItem;

import java.util.List;

class AdapterApi extends RecyclerView.Adapter<AdapterApi.ViewHolder> {

    List<ArticlesItem> data;
    Context context;
    private OnItemClickListener onItemClickListener;

    public AdapterApi(Context context, List<ArticlesItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.listreycler, null);
        return new ViewHolder(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    final ViewHolder holder = viewHolder;
    ArticlesItem model = data.get(i);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.author.setText(model.getAuthor());
        holder.publishedat.setText(Utils.DateToTimeFormat(model.getPublishedAt()));
        holder.source.setText(model.getSource().getName());

        holder.time.setText("\u2022" + Utils.DateToTimeFormat(model.getPublishedAt()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,author,desc,publishedat,source,time;
        ImageView imageView;
        ProgressBar pb;
        OnItemClickListener onItemClickListener;
        public ViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            source = itemView.findViewById(R.id.Source);
            time = itemView.findViewById(R.id.Time);
            publishedat = itemView.findViewById(R.id.publishedAt);
            imageView = itemView.findViewById(R.id.img);
            pb = itemView.findViewById(R.id.progress_rest);
            desc = itemView.findViewById(R.id.desc);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
