package com.olhu.redditclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olhu.redditclient.R;
import com.olhu.redditclient.model.Topic;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicVH> {
    private List<Topic> topics;
    private Context context;

    public TopicAdapter(Context context) {
        this.context = context;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public TopicVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicVH(rootView);
    }

    @Override
    public void onBindViewHolder(TopicVH holder, int position) {
        Topic topic = topics.get(position);
        setThumbnail(holder.thumbnail, topic);
        holder.title.setText(topic.getTitle());
        holder.commentsNum.setText(getString(R.string.comments, topic.getCommentsNumber()));
        long hoursElapsed = topic.getHoursElapsed();
        holder.createdInfo.setText(getString(R.string.created_info, topic.getAuthor(), getHoursElapsedString(hoursElapsed)));
    }

    private void setThumbnail(AppCompatImageView thumbnail, Topic topic) {
        if (topic.getSourceImage() != null) {
            thumbnail.setOnClickListener(view -> showSourceImage(topic));
        }
        thumbnail.getLayoutParams().width = topic.getThumbnail().getWidth();
        thumbnail.getLayoutParams().height = topic.getThumbnail().getHeight();
        if (topic.getThumbnail().isDefault()) {
            thumbnail.setImageResource(R.drawable.ic_photo_black);
        } else {
            Picasso.with(context)
                    .load(topic.getThumbnail().getUrl())
                    .into(thumbnail);
        }
    }

    private void showSourceImage(Topic topic) {
        Uri webpage = Uri.parse(topic.getSourceImage().getUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        context.startActivity(webIntent);
    }

    @NonNull
    private String getHoursElapsedString(long elapsedHours) {
        String elapsedHoursText;
        if (elapsedHours > 1) {
            elapsedHoursText = getString(R.string.elapsed_time, elapsedHours);
        } else {
            elapsedHoursText = getString(R.string.elapsed_time_one_hour);
        }
        return elapsedHoursText;
    }

    private String getString(int id, Object... formatArgs) {
        return context.getString(id, formatArgs);
    }

    @Override
    public int getItemCount() {
        return topics == null ? 0 : topics.size();
    }

    static class TopicVH extends RecyclerView.ViewHolder {
        AppCompatTextView title;
        AppCompatTextView createdInfo;
        AppCompatTextView commentsNum;
        AppCompatImageView thumbnail;

        TopicVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_txt);
            createdInfo = itemView.findViewById(R.id.created_info_txt);
            commentsNum = itemView.findViewById(R.id.comments_num_txt);
            thumbnail = itemView.findViewById(R.id.thumbnail_img);
        }
    }
}
