package ru.hustledb.hustledb;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.Events.OnNominationLoadCompleteEvent;
import ru.hustledb.hustledb.Events.OnPreregistrationLoadCompleteEvent;
import ru.hustledb.hustledb.ValueClasses.Dancer;
import ru.hustledb.hustledb.ValueClasses.Nomination;
import ru.hustledb.hustledb.ValueClasses.Record;
import rx.Observer;

public class NominationRecyclerAdapter extends RecyclerView.Adapter<NominationRecyclerAdapter.ViewHolder> implements
        Observer<Nomination> {

    private List<Record> records;
    private SparseBooleanArray expandedRecords;
    @Inject
    protected RxBus bus;

    NominationRecyclerAdapter(List<Record> records) {
        this.records = records;
        App.getAppComponent().inject(this);
        expandedRecords = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nomination_item_test, parent, false);
        return new NominationRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.recordNumber.setText(Integer.toString(position + 1));
        Dancer dancer_1 = records.get(position).getDancer_1();
        if(dancer_1 == null){
            holder.rootLayout1.setVisibility(View.GONE);
        } else if (dancer_1.getTitle().equals("В поиске")) {
            holder.rootLayout1.setVisibility(View.VISIBLE);
            holder.title1.setText(Html.fromHtml("<strong><style=\"color:red;\">В поиске</strong>"));
        } else {
            holder.rootLayout1.setVisibility(View.VISIBLE);
            holder.title1.setText(dancer_1.getTitle());
            holder.gender1.setText("M");
            holder.codeAsh1.setText(records.get(position).getDancer_1().getCode_ash());
            holder.clas1.setText(records.get(position).getDancer_1().getDancer_class());
            holder.clubs1.setText(TextUtils.join(", ", records.get(position).getDancer_1().getClubs()));
        }
        setGenderBackground(holder.rootLayout1, true);

        Dancer dancer_2 = records.get(position).getDancer_2();
        if(dancer_2 == null){
            holder.rootLayout2.setVisibility(View.GONE);
        } else if (dancer_2.getTitle().equals("В поиске")) {
            holder.rootLayout2.setVisibility(View.VISIBLE);
            holder.title2.setText(Html.fromHtml("<strong><style=\"color:red;\">В поиске</strong>"));
        } else {
            holder.rootLayout2.setVisibility(View.VISIBLE);
            holder.title2.setText(dancer_2.getTitle());
            holder.gender2.setText("F");
            holder.codeAsh2.setText(records.get(position).getDancer_2().getCode_ash());
            holder.clas2.setText(records.get(position).getDancer_2().getDancer_class());
            holder.clubs2.setText(TextUtils.join(", ", records.get(position).getDancer_2().getClubs()));
        }
        setGenderBackground(holder.rootLayout2, false);
        expandRecord(holder, expandedRecords.get(position));
    }

    private void setGenderBackground(View view, boolean isMale) {
        int[] gradientColors;
        if (isMale) {
            gradientColors = new int[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLUE};
//            gradientColors = new int[]{ Color.BLUE, Color.WHITE, Color.WHITE, Color.WHITE};
        } else {
            gradientColors = new int[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.RED};
//            gradientColors = new int[]{ Color.RED, Color.WHITE, Color.WHITE, Color.WHITE};
        }
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        drawable.setShape(GradientDrawable.RECTANGLE);
//        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
//        drawable.setGradientCenter(1, 0);
//        drawable.setGradientRadius(300);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        view.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        records.clear();
    }

    @Override
    public void onNext(Nomination nomination) {
        if (this.records == null) {
            this.records = new ArrayList<>();
        } else {
            this.records.clear();
        }
        this.records.addAll(nomination.getRecords());
        notifyDataSetChanged();
        if(bus.hasObservers()){
            if(this.records.size() == 0) {
                bus.send(new OnNominationLoadCompleteEvent(true));
            } else {
                bus.send(new OnNominationLoadCompleteEvent(false));
            }
        }
    }

    public void toggleView(int position, ViewHolder viewHolder) {
        if (expandedRecords.get(position)) {
            expandedRecords.put(position, false);
            expandRecord(viewHolder, false);
        } else {
            expandedRecords.put(position, true);
            expandRecord(viewHolder, true);
        }
    }

    private void expandRecord(ViewHolder viewHolder, boolean isExpanded) {
        if (isExpanded) {
            viewHolder.details1.setVisibility(View.VISIBLE);
            viewHolder.details2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.details1.setVisibility(View.GONE);
            viewHolder.details2.setVisibility(View.GONE);
        }
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.niRecordNumber)
        TextView recordNumber;
        @BindView(R.id.niRoot1)
        LinearLayout rootLayout1;
        @BindView(R.id.niRoot2)
        LinearLayout rootLayout2;
        @BindView(R.id.niDetails1)
        LinearLayout details1;
        @BindView(R.id.niDetails2)
        LinearLayout details2;
        @BindView(R.id.niTitle1)
        TextView title1;
        @BindView(R.id.niTitle2)
        TextView title2;
        @BindView(R.id.niGender1)
        TextView gender1;
        @BindView(R.id.niGender2)
        TextView gender2;
        @BindView(R.id.niCodeAsh1)
        TextView codeAsh1;
        @BindView(R.id.niCodeAsh2)
        TextView codeAsh2;
        @BindView(R.id.niClas1)
        TextView clas1;
        @BindView(R.id.niClas2)
        TextView clas2;
        @BindView(R.id.niClubs1)
        TextView clubs1;
        @BindView(R.id.niClubs2)
        TextView clubs2;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}