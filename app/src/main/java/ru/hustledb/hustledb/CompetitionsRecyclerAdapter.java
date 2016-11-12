package ru.hustledb.hustledb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;

class CompetitionsRecyclerAdapter extends RecyclerView.Adapter<CompetitionsRecyclerAdapter.ViewHolder>
        implements Observer<List<Competition>>{

    private List<Competition> competitions;

    CompetitionsRecyclerAdapter(List<Competition> competitions) {
        this.competitions = competitions;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        competitions.clear();
    }

    // TODO: 06.11.16 Dont just replace the whole list, but add the missing and remove the obsolete
    @Override
    public void onNext(List<Competition> competitions) {
        this.competitions = competitions;
        notifyDataSetChanged();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        View line;

        ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.ciTitle);
            date = (TextView) view.findViewById(R.id.ciDate);
            line = view.findViewById(R.id.ciLine);
        }
    }

    @Override
    public CompetitionsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_item, parent, false);
        return new CompetitionsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompetitionsRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(competitions.get(position).getId());
        holder.title.setText(competitions.get(position).getTitle());
        holder.date.setText(competitions.get(position).getPrettyDate());
        try {
            String stringDate = competitions.get(position).getDate();
            Date date = Competition.dateFormat.parse(stringDate);
            Date now = new Date();
            if(date.compareTo(now) > 0){
                holder.line.setBackgroundColor(0xff76ff03);
            } else{
                holder.line.setBackgroundColor(0xffd50000);
            }
        } catch (ParseException e){
            //not a date, oh well
        }
    }

    @Override
    public int getItemCount() {
        return competitions==null? 0 : competitions.size();
    }
}