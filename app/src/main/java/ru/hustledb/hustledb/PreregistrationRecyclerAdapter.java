package ru.hustledb.hustledb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.ValueClasses.Nomination;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import rx.Observer;

public class PreregistrationRecyclerAdapter extends RecyclerView.Adapter<PreregistrationRecyclerAdapter.ViewHolder> implements
        Observer<Preregistration>{

    private List<Nomination> nominations;

    PreregistrationRecyclerAdapter(List<Nomination> nominations){
        this.nominations = nominations;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preregistration_item, parent, false);
        return new PreregistrationRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clas.setText(nominations.get(position).getTitle());
        holder.number.setText(Integer.toString(nominations.get(position).getRecords().size()));
    }

    @Override
    public int getItemCount() {
        return nominations == null? 0 : nominations.size();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        nominations.clear();
    }

    @Override
    public void onNext(Preregistration preregistration) {
        if(this.nominations == null){
            this.nominations = new ArrayList<>();
        } else {
            this.nominations.clear();
        }
        if(preregistration != null) {
            this.nominations.addAll(preregistration.getNominations());
        }
        notifyDataSetChanged();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.piClas)
        TextView clas;
        @BindView(R.id.piNumber)
        TextView number;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}