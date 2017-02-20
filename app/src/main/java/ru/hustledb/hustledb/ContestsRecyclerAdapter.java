package ru.hustledb.hustledb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.ValueClasses.Contest;
import rx.Observer;

class ContestsRecyclerAdapter extends RecyclerView.Adapter<ContestsRecyclerAdapter.ViewHolder>
        implements Observer<List<Contest>>{

    private List<Contest> contests;

    ContestsRecyclerAdapter(List<Contest> contests) {
        this.contests = contests;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        contests.clear();
    }

    // TODO: 06.11.16 Dont just replace the whole list, but add the missing and remove the obsolete
    @Override
    public void onNext(List<Contest> contests) {
        this.contests = contests;
        notifyDataSetChanged();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ciTitle)
        TextView title;
        @BindView(R.id.ciDate)
        TextView date;
        @BindView(R.id.ciLine)
        View line;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ContestsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_item, parent, false);
        return new ContestsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContestsRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(contests.get(position).getId());
        holder.title.setText(contests.get(position).getTitle());
        holder.date.setText(contests.get(position).getPrettyDate());
        try {
            String stringDate = contests.get(position).getDate();
            Date date = Contest.dateFormat.parse(stringDate);
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
        return contests ==null? 0 : contests.size();
    }
}