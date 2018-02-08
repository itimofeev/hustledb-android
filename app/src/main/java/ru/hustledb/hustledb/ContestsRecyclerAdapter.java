package ru.hustledb.hustledb;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.AndroidViewChildren.SymbolView;
import ru.hustledb.hustledb.ValueClasses.Contest;
import rx.Observer;

class ContestsRecyclerAdapter extends RecyclerView.Adapter<ContestsRecyclerAdapter.ViewHolder>
        implements Observer<SparseArray<List<Contest>>> {

    private List<Contest> contests;
    private int pagerPosition;

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
    public void onNext(SparseArray<List<Contest>> contests) {
        int offset = contests.keyAt(0) == ContestsCache.FUTURE_YEAR ? 1 : 0;
        this.contests = contests.get(contests.keyAt(contests.size() - pagerPosition - 1));
        notifyDataSetChanged();
    }

    public void setPagerPosition(int pagerPosition) {
        this.pagerPosition = pagerPosition;
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ciTitle)
        TextView title;
        @BindView(R.id.ciCityName)
        TextView cityName;
        @BindView(R.id.ciDate)
        TextView date;
        @BindView(R.id.symbolView)
        SymbolView symbolView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ContestsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_item, parent, false);
        return new ContestsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContestsRecyclerAdapter.ViewHolder holder, int position) {
        Contest contest = contests.get(position);
        holder.itemView.setTag(contest.getId());
        holder.title.setText(contest.getTitle());
//        holder.date.setText(contest.getPrettyDate() + ", " + contest.getCityName());
        holder.cityName.setText(contest.getCityName());
        holder.date.setText(contest.getDateStr());
        holder.symbolView.setSymbol(contest.getTitle().charAt(0));
    }

    @Override
    public int getItemCount() {
        return contests == null ? 0 : contests.size();
    }
}